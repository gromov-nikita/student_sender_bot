package org.bsut.student_sender_bot.service.bot.survey.registration;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import one.util.streamex.StreamEx;
import org.bsut.student_sender_bot.entity.*;
import org.bsut.student_sender_bot.service.bot.enums.BotCommand;
import org.bsut.student_sender_bot.service.bot.enums.BotCommandLevel;
import org.bsut.student_sender_bot.service.bot.keyboard.reply.ReplyKeyboardCreator;
import org.bsut.student_sender_bot.service.bot.SendMessageCreator;
import org.bsut.student_sender_bot.service.bot.survey.Survey;
import org.bsut.student_sender_bot.service.date.DateFormatterCreator;
import org.bsut.student_sender_bot.service.date.DateHandler;
import org.bsut.student_sender_bot.service.data.*;
import org.bsut.student_sender_bot.service.list_handler.Splitter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Setter
@Getter
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ConsultationRegistrationSurvey implements Survey {

    private final SessionService sessionService;
    private final StudentGroupService studentGroupService;
    private final SubjectService subjectService;
    private final DateHandler dateHandler;
    private final StudentRecordService studentRecordService;
    private final ConsultationService consultationService;
    private final ConsultationTypeService consultationTypeService;
    private final RegistrationService registrationService;
    private final ReplyKeyboardCreator replyKeyboardCreator;
    private final SendMessageCreator messageCreator;
    private final DateFormatterCreator dateFormatterCreator;
    private final Splitter splitter;
    private final AppUserService appUserService;

    @Value(value = "${const.student.reg.limit}")
    private int REG_LIMIT;

    private AppUser appUser;
    private boolean hasConsultations;
    private boolean hasReachedConsultationLimit;
    private List<LocalDate> registrationDateList;
    private Session session;
    private LocalDate date;
    private Subject subject;
    private ConsultationType type;

    @PostConstruct
    private void init() {
        this.session = sessionService.getCurrentSession();
    }

    @Override
    @Transactional
    public SendMessage nextMessage(Long chatId) {
        if(Objects.isNull(appUser)) appUser = appUserService.getByChatId(chatId);
        if(Objects.isNull(subject)) return getSubjectMessage(chatId);
        else if(hasReachedConsultationLimit) return null;
        else if (Objects.isNull(date)) return getDateMessage(chatId);
        else if(Objects.isNull(type)) return getTypeMessage(chatId);
        else return null;
    }

    @Override
    @Transactional
    public void handleAnswer(Message message) {
        if(Objects.isNull(subject)) handleSubjectNameMessage(message);
        else if (Objects.isNull(date)) handleDateMessage(message);
        else if(Objects.isNull(type)) handleTypeNameMessage(message);
    }
    @Override
    @Transactional
    public SendMessage closeSurvey(Long chatId) {
        if (!hasConsultations) return closeWithoutConsultations(chatId);
        else if (hasReachedConsultationLimit) return closeHasReachedConsultationLimit(chatId);
        else return closeWithConsultations(chatId);
    }
    private SendMessage closeWithConsultations(Long chatId) {
        StudentRecord record = studentRecordService.save(createStudentRecord());
        return messageCreator.getReplyKeyboardMessage(chatId, stringify(record),
                replyKeyboardCreator.generateCommandsReplyKeyboard(BotCommandLevel.DEFAULT, appUser.getType())
        );
    }
    private SendMessage closeWithoutConsultations(Long chatId) {
        return messageCreator.getReplyKeyboardMessage(chatId, "На данный момент для вас нет доступных консультаций.",
                replyKeyboardCreator.generateCommandsReplyKeyboard(BotCommandLevel.DEFAULT, appUser.getType())
        );
    }
    private SendMessage closeHasReachedConsultationLimit(Long chatId) {
        return messageCreator.getReplyKeyboardMessage(chatId,
                "Предел записей на один предмет: " + REG_LIMIT + ".",
                replyKeyboardCreator.generateCommandsReplyKeyboard(BotCommandLevel.DEFAULT, appUser.getType())
        );
    }
    private String stringify(StudentRecord record) {
        return "Вы зарегистрированы на " +
                record.getRegistration().getDate().format(dateFormatterCreator.getUserLocalDateFormatter()) + " число. \nПодходите с " +
                record.getRegistration().getConsultation().getStartTime() + " до " +
                record.getRegistration().getConsultation().getEndTime() + "." +
                "\nК преподавателям:\n" + StreamEx.of(record.getRegistration().getConsultation().getConsultationTeachers())
                .map(ConsultationTeacher::getAppUser)
                .map(AppUser::getName).map(name->name + "\n").reduce(String::concat).get();
    }
    private StudentRecord createStudentRecord() {
        return StudentRecord.builder().appUser(appUser).type(type).registration(registrationService.getOrSave(
                consultationService.findBySessionAndStudentAndSubject(session, appUser, subject),
                date
        )).build();
    }
    private SendMessage getDateMessage(Long chatId) {
        List<List<LocalDate>> consultationDateGroup = dateHandler.getConsultationDateGroup(Pair.of(session.getStartDate(), session.getEndDate()));
        this.registrationDateList = StreamEx.of(consultationDateGroup).flatMap(List::stream).toList();
        List<List<String>> keyBoardMenu = stringify(consultationDateGroup);
        keyBoardMenu.addLast(BotCommand.getCommand(appUser.getType(),BotCommandLevel.SURVEY));
        return messageCreator.getReplyKeyboardMessage(chatId,
                "Выберите дату посещения консультации. ",
                replyKeyboardCreator.generateReplyKeyboard(keyBoardMenu)
        );
    }
    private List<List<String>> stringify(List<List<LocalDate>> dateGroup) {
        return StreamEx.of(dateGroup).map(row ->
                StreamEx.of(row).map(date -> date.format(dateFormatterCreator.getUserLocalDateFormatter())).toList()
        ).toList();
    }
    private SendMessage getSubjectMessage(Long chatId) {
        List<Consultation> consultations = consultationService.findBySessionAndStudent(session, appUser);
        this.hasConsultations = !consultations.isEmpty();
        return hasConsultations ? handleWithConsultationCase(chatId,consultations) : null;
    }
    private SendMessage handleWithConsultationCase(Long chatId,List<Consultation> consultations) {
        return messageCreator.getReplyKeyboardMessage(chatId,
                "Выберите предмет. ",
                replyKeyboardCreator.generateReplyKeyboard(splitter.split(
                        StreamEx.of(consultations)
                                .map(Consultation::getSubject).map(Subject::getName).sorted()
                                .append(BotCommand.getCommand(appUser.getType(),BotCommandLevel.SURVEY)).toList(),
                        1
                ))
        );
    }
    private SendMessage getTypeMessage(Long chatId) {
        return messageCreator.getReplyKeyboardMessage(chatId,
                "Выберите цель записи.",
                replyKeyboardCreator.generateReplyKeyboard(splitter.split(
                        StreamEx.of(consultationTypeService.findAll()).map(ConsultationType::getName)
                                .append(BotCommand.getCommand(appUser.getType(),BotCommandLevel.SURVEY)).toList(),
                        1
                ))
        );
    }
    private void handleDateMessage(Message message) {
        LocalDate date = LocalDate.parse(message.getText(), dateFormatterCreator.getUserLocalDateFormatter());
        if(registrationDateList.contains(date)) this.date = date;
    }
    private void handleSubjectNameMessage(Message message) {
        this.subject = subjectService.findByName(message.getText());
        updateReachedConsultationLimit();
    }
    private void updateReachedConsultationLimit() {
        this.hasReachedConsultationLimit = studentRecordService.findAllByUserAndDateAfterOrEquallyAndSubject(
                appUser, LocalDate.now(), subject
        ).size() >= REG_LIMIT;
    }
    private void handleTypeNameMessage(Message message) {
        this.type = consultationTypeService.findByName(message.getText());
    }
}

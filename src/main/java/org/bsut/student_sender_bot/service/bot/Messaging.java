package org.bsut.student_sender_bot.service.bot;

import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.bsut.student_sender_bot.entity.ConsultationTeacher;
import org.bsut.student_sender_bot.entity.StudentRecord;
import org.bsut.student_sender_bot.entity.Teacher;
import org.bsut.student_sender_bot.service.bot.enums.BotCommandLevel;
import org.bsut.student_sender_bot.service.bot.enums.CallbackDataPrefix;
import org.bsut.student_sender_bot.service.bot.enums.StudentSenderBotCommand;
import org.bsut.student_sender_bot.service.bot.keyboard.inline.ButtonData;
import org.bsut.student_sender_bot.service.bot.keyboard.inline.InlineKeyboardCreator;
import org.bsut.student_sender_bot.service.bot.keyboard.reply.ReplyKeyboardCreator;
import org.bsut.student_sender_bot.service.bot.survey.registration.RegistrationSurvey;
import org.bsut.student_sender_bot.service.bot.survey.Survey;
import org.bsut.student_sender_bot.service.bot.survey.SurveyService;
import org.bsut.student_sender_bot.service.data.StudentRecordService;
import org.bsut.student_sender_bot.service.date.DateFormatterCreator;
import org.bsut.student_sender_bot.service.list_handler.Splitter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.bsut.student_sender_bot.service.bot.enums.StudentSenderBotCommand.*;

@Service
@RequiredArgsConstructor
public class Messaging {
    private final SurveyService surveyService;
    private final StudentRecordService studentRecordService;
    private final SendMessageCreator messageCreator;
    private final ApplicationContext appContext;
    private final DateFormatterCreator dateFormatterCreator;
    private final ReplyKeyboardCreator replyKeyboardCreator;
    private final InlineKeyboardCreator inlineKeyboardCreator;
    private final Splitter splitter;

    @Transactional
    public SendMessage getAnswer(Message message) {
        if(isSurvey(message)) return doSurvey(message);
        else return switch (message.getText()) {
            case String text when text.equals(REG.getCommand()) -> startConsultationRegistrationSurvey(message.getChatId());
            case String text when text.equals(REG_INF.getCommand()) -> getRegistrationInfoMessage(message.getChatId());
            case String text when text.equals(REG_CANCEL.getCommand()) -> getRegistrationCancelMenuMessage(message.getChatId());
            case String text when text.equals(COMMANDS.getCommand()) -> messageCreator.getDefaultMessage(message.getChatId(),getAllCommandInfo());
            case String text when text.equals(ID.getCommand()) -> messageCreator.getDefaultMessage(message.getChatId(),"Ваш id: " + message.getChatId());
            case String text when text.equals("/start") -> getStartMessage(message);
            default -> messageCreator.getDefaultMessage(message.getChatId(),getIncorrectCommandText(message));
        };
    }
    private boolean isSurvey(Message message) {
        return surveyService.isContain(message.getChatId());
    }
    private SendMessage doSurvey(Message message) {
        if(Objects.nonNull(message.getText()) && message.getText().equals(STOP.getCommand())) return removeSurvey(message.getChatId());
        else {
            Survey surveyState = surveyService.getSurveyState(message.getChatId());
            surveyState.handleAnswer(message);
            return handleSendMessage(surveyState.nextMessage(message.getChatId()),message.getChatId());
        }
    }
    private SendMessage removeSurvey(Long chatId) {
        surveyService.removeSurvey(chatId);
        return messageCreator.getDefaultMessage(chatId,"Вы успешно вышли из опроса. Теперь вы можете вводить команды");
    }
    private String getIncorrectCommandText(Message message) {
        StudentSenderBotCommand botCommand = findCommand(message.getText());
        if(Objects.isNull(botCommand)) return "Извините, " +  message.getChat().getFirstName() + ", я не знаю такой команды.";
        else return "Извините, " +  message.getChat().getFirstName() + ", но эта команда используется в другом состоянии помощника. " + botCommand.getInfo();
    }
    private SendMessage startConsultationRegistrationSurvey(Long chatId) {
        surveyService.startSurvey(chatId,appContext.getBean(RegistrationSurvey.class));
        return handleSendMessage(surveyService.getSurveyState(chatId).nextMessage(chatId),chatId);
    }
    private SendMessage handleSendMessage(SendMessage sendMessage,Long chatId) {
        if(Objects.isNull(sendMessage)) return surveyService.removeSurvey(chatId).closeSurvey(chatId);
        else return sendMessage;
    }
    private SendMessage getRegistrationInfoMessage(Long chatId) {
        List<StudentRecord> recordGroup = studentRecordService.findAllByChatIdAndDateAfter(chatId, LocalDate.now());
        if(recordGroup.isEmpty()) return messageCreator.getDefaultMessage(chatId,
                "На данный момент у вас отсутствуют запланированные консультации."
        );
        else return messageCreator.getDefaultMessage(chatId,
                "Ваш список запланированных консультаций:\n\n"+
                        StreamEx.of(recordGroup)
                                .sortedBy(record -> record.getRegistration().getDate())
                                .map(this::stringify).map(str->str+"\n").reduce(String::concat).get()
        );
    }
    private SendMessage getStartMessage(Message message) {
        return messageCreator.getReplyKeyboardMessage(
                message.getChatId(),
                "Привет, " + "" + message.getChat().getFirstName() + ", вот список всех доступных команд: \n"
                        + getAllCommandInfo(),
                replyKeyboardCreator.generateCommandsReplyKeyboard(BotCommandLevel.DEFAULT));
    }
    private SendMessage getRegistrationCancelMenuMessage(Long chatId) {
        List<StudentRecord> recordGroup = studentRecordService.findAllByChatIdAndDateAfter(chatId, LocalDate.now());
        if(recordGroup.isEmpty()) return messageCreator.getDefaultMessage(chatId,
                "На данный момент у вас отсутствуют запланированные консультации."
        );
        else return messageCreator.getReplyKeyboardMessage(
                chatId,
                "Нажмите на консультации, которые хотите отменить.\n\n",
                inlineKeyboardCreator.generateInlineKeyboard(
                        splitter.split(StreamEx.of(recordGroup).sortedBy(record -> record.getRegistration().getDate())
                                .map(record-> new ButtonData(stringifyToInlineButton(record),
                                        CallbackDataPrefix.REG_CANCEL.getPrefix()+record.getId()
                                )).toList(), 1
                        )
                )
        );
    }
    private String stringifyToInlineButton(StudentRecord studentRecord) {
        return studentRecord.getRegistration().getDate().format(dateFormatterCreator.getUserLocalDateFormatter()) +
                " - " + studentRecord.getRegistration().getConsultation().getSubject().getName() + ".";
    }

    private String stringify(StudentRecord studentRecord) {
        return "Дата: " + studentRecord.getRegistration().getDate().format(dateFormatterCreator.getUserLocalDateFormatter()) + ".\n" +
                "C " + studentRecord.getRegistration().getConsultation().getStartTime() +
                " до " + studentRecord.getRegistration().getConsultation().getEndTime() + ".\n" +
                "Предмет: " + studentRecord.getRegistration().getConsultation().getSubject().getName() + ".\n" +
                "Преподаватели:\n" + StreamEx.of(studentRecord.getRegistration().getConsultation().getConsultationTeachers())
                .map(ConsultationTeacher::getTeacher)
                .map(Teacher::getName).map(name->name + "\n").reduce(String::concat).get();
    }
}

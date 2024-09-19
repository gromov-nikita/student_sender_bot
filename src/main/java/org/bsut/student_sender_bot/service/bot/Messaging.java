package org.bsut.student_sender_bot.service.bot;

import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.bsut.student_sender_bot.entity.ConsultationTeacher;
import org.bsut.student_sender_bot.entity.Registration;
import org.bsut.student_sender_bot.entity.Teacher;
import org.bsut.student_sender_bot.service.bot.enums.StudentSenderBotCommand;
import org.bsut.student_sender_bot.service.bot.survey.registration.RegistrationSurvey;
import org.bsut.student_sender_bot.service.bot.survey.Survey;
import org.bsut.student_sender_bot.service.bot.survey.SurveyService;
import org.bsut.student_sender_bot.service.data.RegistrationService;
import org.bsut.student_sender_bot.service.date.DateFormatterCreator;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDate;
import java.util.Objects;

import static org.bsut.student_sender_bot.service.bot.enums.StudentSenderBotCommand.*;

@Service
@RequiredArgsConstructor
public class Messaging {
    private final SurveyService surveyService;
    private final RegistrationService registrationService;
    private final SendMessageCreator messageCreator;
    private final ApplicationContext appContext;
    private final DateFormatterCreator dateFormatterCreator;

    @Transactional
    public SendMessage getAnswer(Message message) {
        if(isSurvey(message)) return doSurvey(message);
        else return switch (message.getText()) {
            case String text when text.equals(REG.getCommand()) -> startConsultationRegistrationSurvey(message.getChatId());
            case String text when text.equals(REG_INF.getCommand()) -> getRegistrationInfoMessage(message.getChatId());
            case String text when text.equals(COMMANDS.getCommand()) -> messageCreator.getDefaultMessage(message.getChatId(),getAllCommandInfo());
            case String text when text.equals(ID.getCommand()) -> messageCreator.getDefaultMessage(message.getChatId(),"Ваш id: " + message.getChatId());
            case String text when text.equals("/start") -> messageCreator.getDefaultMessage(message.getChatId(),"Привет, " + message.getChat().getFirstName() + ", вот список всех доступных команд: \n" + getAllCommandInfo());
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
        return messageCreator.getDefaultMessage(chatId,
                "Ваш список запланированных консультаций:\n\n"+
                        StreamEx.of(registrationService.findAllByChatIdAndDateAfter(chatId, LocalDate.now()))
                                .sortedBy(Registration::getDate)
                                .map(this::stringify).map(str->str+"\n").reduce(String::concat).get()
        );
    }
    private String stringify(Registration registration) {
        return "Дата: " +
                registration.getDate().format(dateFormatterCreator.getUserLocalDateFormatter()) + ".\n" +
                "C " + registration.getConsultation().getStartTime() + " до " +
                registration.getConsultation().getEndTime() + ".\n" +
                "Преподаватели:\n" + StreamEx.of(registration.getConsultation().getConsultationTeachers())
                .map(ConsultationTeacher::getTeacher)
                .map(Teacher::getName).map(name->name + "\n").reduce(String::concat).get();

    }
}

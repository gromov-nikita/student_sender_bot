package org.bsut.student_sender_bot.service.bot;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.service.bot.enums.StudentSenderBotCommand;
import org.bsut.student_sender_bot.service.bot.survey.ConsultationSurveyState;
import org.bsut.student_sender_bot.service.bot.survey.SurveyService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Objects;

import static org.bsut.student_sender_bot.service.bot.enums.StudentSenderBotCommand.*;
import static org.bsut.student_sender_bot.service.bot.survey.SendMessageCreator.getDefaultMessage;

@Service
@RequiredArgsConstructor
public class Messaging {
    private final SurveyService surveyService;

    public SendMessage getAnswer(Message message) {
        if(isSurvey(message)) return doSurvey(message);
        else return switch (message.getText()) {
            case String text when text.equals(REG.getCommand()) -> startConsultationRegistrationSurvey(message.getChatId());
            case String text when text.equals(COMMANDS.getCommand()) -> getDefaultMessage(message.getChatId(),getAllCommandInfo());
            case String text when text.equals(ID.getCommand()) -> getDefaultMessage(message.getChatId(),"Ваш id: " + message.getChatId());
            case String text when text.equals("/start") -> getDefaultMessage(message.getChatId(),"Привет, " + message.getChat().getFirstName() + ", вот список всех доступных команд: \n" + getAllCommandInfo());
            default -> getDefaultMessage(message.getChatId(),getIncorrectCommandText(message));
        };
    }
    private boolean isSurvey(Message message) {
        return surveyService.isContain(message.getChatId());
    }
    private SendMessage doSurvey(Message message) {
        if(Objects.nonNull(message.getText()) && message.getText().equals(STOP.getCommand())) return removeSurvey(message.getChatId());
        else {
            ConsultationSurveyState surveyState = surveyService.getSurveyState(message.getChatId());
            surveyState.handleAnswer(message);
            return handleSendMessage(surveyState.nextMessage(message.getChatId()),message.getChatId());
        }
    }
    private SendMessage removeSurvey(Long chatId) {
        surveyService.removeSurveyState(chatId);
        return getDefaultMessage(chatId,"Вы успешно вышли из опроса. Теперь вы можете вводить команды");
    }
    private String getIncorrectCommandText(Message message) {
        StudentSenderBotCommand botCommand = findCommand(message.getText());
        if(Objects.isNull(botCommand)) return "Извините, " +  message.getChat().getFirstName() + ", я не знаю такой команды.";
        else return "Извините, " +  message.getChat().getFirstName() + ", но эта команда используется в другом состоянии помощника. " + botCommand.getInfo();
    }
    private SendMessage startConsultationRegistrationSurvey(Long chatId) {
        surveyService.startSurvey(chatId);
        return handleSendMessage(surveyService.getSurveyState(chatId).nextMessage(chatId),chatId);
    }
    private SendMessage handleSendMessage(SendMessage sendMessage,Long chatId) {
        if(Objects.isNull(sendMessage)) return closeSurvey(chatId);
        else return sendMessage;
    }
    private SendMessage closeSurvey(Long chatId) {
        ConsultationSurveyState surveyState = surveyService.getSurveyState(chatId);
        surveyService.removeSurveyState(chatId);
        return getDefaultMessage(chatId,
                "Вы успешно прошли регистрацию! \nДанные которые вы ввели:\n" + surveyState.toString()
        );
    }
}

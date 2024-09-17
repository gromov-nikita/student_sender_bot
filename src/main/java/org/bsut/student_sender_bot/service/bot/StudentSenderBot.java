package org.bsut.student_sender_bot.service.bot;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.bsut.student_sender_bot.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

@Component
@RequiredArgsConstructor
public class StudentSenderBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final Messaging messaging;


    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotUsername();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if(update.hasMessage() && (message.hasText() || message.hasContact()) ) sendMessage(messaging.getAnswer(message));
    }
    private void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}

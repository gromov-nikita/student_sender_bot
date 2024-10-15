package org.bsut.student_sender_bot.service.bot;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.config.BotConfig;
import org.bsut.student_sender_bot.service.bot.callback.CallbackHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final Messaging messaging;
    private final CallbackHandler callbackHandler;


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
        if(update.hasCallbackQuery()) callbackHandler.handle(update.getCallbackQuery().getData(),update.getCallbackQuery().getMessage().getChatId());
        else if(update.hasMessage() && (message.hasText() || message.hasContact())) messaging.handle(message);
    }
    public void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}

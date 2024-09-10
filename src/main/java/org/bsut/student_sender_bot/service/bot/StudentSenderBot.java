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
        if(update.hasMessage() && message.hasText()) {
            String messageText = message.getText();
            long chatId = message.getChatId();
            switch (messageText) {
                case "/start":
                    startCommandRecived(chatId, message.getChat().getFirstName());
                    break;
                case "/id":
                    sendMessage(chatId,message.getFrom().getId().toString());
                    break;
                default :
                    sendMessage(chatId,"Sorry, " +  message.getChat().getFirstName() + " command was not recognized.");
            }
        } else if(message.hasDocument()) {
            handleDocument(message.getDocument(),message.getChatId());
        }
    }
    private void handleDocument(Document document, long chatId) {
        if (document.getFileName().endsWith(".xlsx") || document.getFileName().endsWith(".xls")) {
            try(Workbook workbook = WorkbookFactory.create(downloadFile(document))) {
                sendMessage(chatId,
                        "Document received: " + document.getFileName() +
                                " totalRows: " + workbook.getSheetAt(0).getPhysicalNumberOfRows()
                );

            } catch (Exception e) {
                sendMessage(chatId, "Failed to download or process the document.");
                e.printStackTrace();
            }
        } else sendMessage(chatId, "Please upload an Excel document (.xlsx or .xls).");
    }
    private File downloadFile(Document document) throws TelegramApiException {
        return downloadFile(execute(new GetFile(document.getFileId())).getFilePath());
    }
    private void startCommandRecived(long chatId, String name) {
        String answer = "Hi, " + name + ", nice to meet you!";
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}

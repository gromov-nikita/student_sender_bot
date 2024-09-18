package org.bsut.student_sender_bot.service.bot.survey;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import one.util.streamex.StreamEx;
import org.bsut.student_sender_bot.entity.ConsultationTeacher;
import org.bsut.student_sender_bot.entity.Registration;
import org.bsut.student_sender_bot.entity.Teacher;
import org.bsut.student_sender_bot.service.bot.ReplyKeyBoardCreator;
import org.bsut.student_sender_bot.service.bot.SendMessageCreator;
import org.bsut.student_sender_bot.service.data.RegistrationService;
import org.bsut.student_sender_bot.service.date.DateFormatterCreator;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDate;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Setter
@Getter
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RegistrationInfoSurvey implements Survey {
    private final SendMessageCreator messageCreator;
    private final ReplyKeyBoardCreator replyKeyBoardCreator;
    private final RegistrationService registrationService;
    private final DateFormatterCreator dateFormatterCreator;

    private String phoneNumber;

    @Override
    public SendMessage nextMessage(Long chatId) {
        if(Objects.isNull(phoneNumber)) return messageCreator.getReplyKeyboardMessage(
                chatId,
                "Поделитесь номером телефона.",
                replyKeyBoardCreator.generatePhoneNumberReplyKeyboard()
        );
        else return null;
    }

    @Override
    public void handleAnswer(Message message) {
        if(Objects.isNull(phoneNumber)) this.phoneNumber = message.getContact().getPhoneNumber();
    }

    @Override
    @Transactional
    public SendMessage closeSurvey(Long chatId) {
        return messageCreator.getDefaultMessage(chatId,
                StreamEx.of(registrationService.findAllByPhoneNumberAndDateAfter(phoneNumber, LocalDate.now()))
                        .sortedBy(Registration::getDate)
                        .map(this::stringify).map(str->str+"\n").reduce(String::concat).get()
        );
    }
    private String stringify(Registration registration) {
        return "Ваш список запланированных консультаций:\nДата: " +
                registration.getDate().format(dateFormatterCreator.getUserLocalDateFormatter()) + ".\n" +
                "C " + registration.getConsultation().getStartTime() + " до " +
                registration.getConsultation().getEndTime() + ".\n" +
                "Преподаватели:\n" + StreamEx.of(registration.getConsultation().getConsultationTeachers())
                .map(ConsultationTeacher::getTeacher)
                .map(Teacher::getName).map(name->name + "\n").reduce(String::concat).get();

    }
}

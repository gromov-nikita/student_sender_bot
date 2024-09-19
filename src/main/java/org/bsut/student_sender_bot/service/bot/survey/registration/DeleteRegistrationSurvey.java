package org.bsut.student_sender_bot.service.bot.survey.registration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import one.util.streamex.StreamEx;
import org.bsut.student_sender_bot.entity.Consultation;
import org.bsut.student_sender_bot.entity.StudentGroup;
import org.bsut.student_sender_bot.entity.Subject;
import org.bsut.student_sender_bot.service.bot.ReplyKeyBoardCreator;
import org.bsut.student_sender_bot.service.bot.SendMessageCreator;
import org.bsut.student_sender_bot.service.bot.survey.Survey;
import org.bsut.student_sender_bot.service.data.RegistrationService;
import org.bsut.student_sender_bot.service.date.DateFormatterCreator;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Setter
@Getter
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DeleteRegistrationSurvey implements Survey {
    private final SendMessageCreator messageCreator;
    private final ReplyKeyBoardCreator replyKeyBoardCreator;
    private final RegistrationService registrationService;
    private final DateFormatterCreator dateFormatterCreator;

    private StudentGroup group;
    private Subject subject;

    @Override
    public SendMessage nextMessage(Long chatId) {
        //if(Objects.isNull(group)) return getStudentGroupMessage(chatId);
        //else if(Objects.isNull(subject)) return getSubjectMessage(chatId);
        //else
            return null;
    }

    @Override
    public void handleAnswer(Message message) {}

    @Override
    public SendMessage closeSurvey(Long chatId) {
        return null;
    }
    /*
    private SendMessage getStudentGroupMessage(Long chatId) {
        return messageCreator.getReplyKeyboardMessage(chatId,
                "Выберите вашу группу. Если вашей группы нет в списке, значит у группы нет доступных консультаций.",
                replyKeyBoardCreator.generateReplyKeyboard(splitter.split(StreamEx.of(
                        studentGroupService.getStudentGroupsWithConsultationsInSession(session)
                ).map(StudentGroup::getName).sorted().toList(),2))
        );
    }
    private SendMessage getSubjectMessage(Long chatId) {
        return messageCreator.getReplyKeyboardMessage(chatId,
                "Выберите предмет. ",
                replyKeyboardCreator.generateReplyKeyboard(splitter.split(
                        StreamEx.of(consultationService.findBySessionAndGroup(session,group))
                                .map(Consultation::getSubject).map(Subject::getName).sorted().toList(),
                        1
                ))
        );
    }

     */
}

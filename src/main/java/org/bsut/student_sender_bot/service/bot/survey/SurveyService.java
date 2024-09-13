package org.bsut.student_sender_bot.service.bot.survey;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final Map<Long, ConsultationSurveyState> surveyStateStorage = new ConcurrentHashMap<>();
    private final ApplicationContext appContext;

    public void startSurvey(Long chatId) {
        surveyStateStorage.put(chatId, appContext.getBean(ConsultationSurveyState.class));
    }

    public ConsultationSurveyState getSurveyState(Long chatId) {
        return surveyStateStorage.get(chatId);
    }

    public void removeSurveyState(Long chatId) {
        surveyStateStorage.remove(chatId);
    }
    public boolean isContain(Long chatId) {
        return surveyStateStorage.containsKey(chatId);
    }
}

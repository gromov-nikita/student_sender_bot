package org.bsut.student_sender_bot.service.bot.survey;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final Map<Long, Survey> surveyStateStorage = new ConcurrentHashMap<>();

    public void startSurvey(Long chatId,Survey survey) {
        surveyStateStorage.put(chatId, survey);
    }

    public Survey getSurveyState(Long chatId) {
        return surveyStateStorage.get(chatId);
    }

    public Survey removeSurvey(Long chatId) {
        return surveyStateStorage.remove(chatId);
    }
    public boolean isContain(Long chatId) {
        return surveyStateStorage.containsKey(chatId);
    }

}

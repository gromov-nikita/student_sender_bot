package org.bsut.student_sender_bot.service.data.redis;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.service.bot.enums.CallbackDataPrefix;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CallbackDataService {
    private final RedisTemplate<String, String> redisTemplate;
    public void save(CallbackDataPrefix callbackDataPrefix, long chatId, String value) {
        redisTemplate.opsForValue().set(getKey(callbackDataPrefix, chatId), value, 24, TimeUnit.HOURS);
    }
    public String get(CallbackDataPrefix callbackDataPrefix, long chatId) {
        return redisTemplate.opsForValue().get(getKey(callbackDataPrefix, chatId));
    }
    public String remove(CallbackDataPrefix callbackDataPrefix, long chatId) {
        String value = get(callbackDataPrefix, chatId);
        delete(callbackDataPrefix, chatId);
        return value;
    }
    public void delete(CallbackDataPrefix callbackDataPrefix, long chatId) {
        redisTemplate.delete(getKey(callbackDataPrefix, chatId));
    }
    private String getKey(CallbackDataPrefix callbackDataPrefix, long chatId) {
        return callbackDataPrefix.getPrefix() + chatId;
    }
}

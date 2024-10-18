package org.bsut.student_sender_bot.service.data.sql;

import lombok.RequiredArgsConstructor;
import org.bsut.student_sender_bot.dao.repository.AppUserRepo;
import org.bsut.student_sender_bot.entity.AppUser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AppUserRepo appUserRepo;
    public AppUser getByChatId(long chatId) {
        return appUserRepo.findByChatId(chatId);
    }
    public AppUser getByPhoneNumber(String phoneNumber) {
        return appUserRepo.findByPhoneNumber(phoneNumber);
    }
    public AppUser getByChatIdOrPhoneNumber(long chatId, String phoneNumber) {
        return appUserRepo.findByChatIdOrPhoneNumber(chatId,phoneNumber);
    }
    public AppUser save(AppUser appUser) {
        return appUserRepo.save(appUser);
    }

}

package org.bsut.student_sender_bot.dao.repository;

import jakarta.validation.constraints.NotNull;
import org.bsut.student_sender_bot.entity.AppUser;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface AppUserRepo extends JpaRepository<AppUser,Integer>, JpaSpecificationExecutor<AppUser> {
    @Cacheable(value = "user", key = "#chatId")
    AppUser findByChatId(long chatId);
    @Cacheable(value = "user", key = "#phoneNumber")
    AppUser findByPhoneNumber(String phoneNumber);
    @Cacheable(value = "user", key = "#chatId + '||' + #phoneNumber")
    AppUser findByChatIdOrPhoneNumber(long chatId, String phoneNumber);
    @Override
    @CachePut(value = "user", key = "#appUser.chatId")
    AppUser save(AppUser appUser);
}

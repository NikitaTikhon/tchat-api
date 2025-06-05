package com.moro.tchat.config;

import com.moro.tchat.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.util.Date;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
@AllArgsConstructor
public class SchedulerConfig {

    private VerificationTokenRepository verificationTokenRepository;

    @Scheduled(cron = "@daily")
    public void removeExpiredTokenAndUser() {
        verificationTokenRepository.deleteByExpiryDateLessThanAndUserEnabledFalse(Date.from(Instant.now()));
    }

}

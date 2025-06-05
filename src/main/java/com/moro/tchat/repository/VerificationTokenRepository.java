package com.moro.tchat.repository;

import com.moro.tchat.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);

    void deleteByExpiryDateLessThanAndUserEnabledFalse(Date expiryDate);


}

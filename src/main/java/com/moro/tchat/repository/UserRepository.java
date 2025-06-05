package com.moro.tchat.repository;

import com.moro.tchat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findAllByEmailOrUsername(String email, String username);

    List<User> findAllByEmailStartsWithIgnoreCaseAndIdIsNotInOrUsernameStartsWithIgnoreCaseAndEnabledTrueAndIdIsNotIn(String email, List<Long> ids, String username, List<Long> sameIds);

    List<User> findAllByEmailStartsWithIgnoreCaseOrUsernameStartsWithIgnoreCaseAndEnabledTrue(String email, String username);


}

package com.moro.tchat.repository;

import com.moro.tchat.entity.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    Long removeByPublicIdAndUserAvatarId(String publicId, Long userId);

    Optional<Avatar> findByUserAvatarId(Long userId);

}

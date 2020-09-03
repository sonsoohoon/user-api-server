package com.org.bithumb.apiserver.domain.repository;

import com.org.bithumb.apiserver.domain.entity.PctUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PctUserRepository extends JpaRepository<PctUserEntity, Long> {
    Optional<PctUserEntity> findByUid(String email);
}


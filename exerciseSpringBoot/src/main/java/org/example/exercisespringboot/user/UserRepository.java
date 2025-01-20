package org.example.exercisespringboot.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findByUsername(String username); // 사용자명(username)으로 사용자 정보를 조회하는 메서드
}
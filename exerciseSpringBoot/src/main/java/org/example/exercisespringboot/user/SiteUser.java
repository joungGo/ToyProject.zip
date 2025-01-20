package org.example.exercisespringboot.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
// 사용자 정보
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // GenerationType.IDENTITY 전략은 데이터베이스의 기본 키 자동 증가(AUTO_INCREMENT) 기능
    private Long id;

    @Column(unique = true) // 값을 중복되게 저장할 수 없음
    private String username;

    private String password;

    @Column(unique = true)
    private String email;
}
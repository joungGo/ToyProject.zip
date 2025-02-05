package org.example.bulletinboardrestapi.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SiteUser {
    @Id
    private Long id;
    private  String email;
    private String password;
    private String username;
}

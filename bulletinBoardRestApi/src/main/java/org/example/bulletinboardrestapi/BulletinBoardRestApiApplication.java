package org.example.bulletinboardrestapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BulletinBoardRestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BulletinBoardRestApiApplication.class, args);
    }

}

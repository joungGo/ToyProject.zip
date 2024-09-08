package pjh.dividendmanageproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class DividendManageProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(DividendManageProjectApplication.class, args);
    }

}

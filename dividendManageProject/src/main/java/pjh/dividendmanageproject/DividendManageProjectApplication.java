package pjh.dividendmanageproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DividendManageProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(DividendManageProjectApplication.class, args);
    }

}

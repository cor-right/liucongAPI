package edu.nefu.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class APIApplication {

    public static void main(String[] args) {
        SpringApplication.run(APIApplication.class, args);
    }

}

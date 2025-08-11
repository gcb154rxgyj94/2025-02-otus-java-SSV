package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
//       new MigrationsExecutorFlyway("jdbc:postgresql://localhost:5430/demoDB", "usr", "pwd").executeMigrations();
    }

}

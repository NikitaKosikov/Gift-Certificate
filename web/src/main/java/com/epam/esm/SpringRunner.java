package com.epam.esm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * Class contains method to run Spring Boot application
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@SpringBootApplication
public class SpringRunner {

    /**
     * The entry point of Spring Boot application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringRunner.class, args);
    }

}

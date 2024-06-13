package eu.sadrian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("eu.sadrian")
@SpringBootApplication
public class EnicaSpringBootVuejsApplication {
    public static void main(String[] args) {
        SpringApplication.run(EnicaSpringBootVuejsApplication.class, args);
    }

}

package eu.sadrian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class EnicaSpringBootVuejsApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnicaSpringBootVuejsApplication.class, args);
    }

}

package eu.sadrian.configuration;

import eu.sadrian.model.Ablesung;
import eu.sadrian.repository.AblesungRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(AblesungRepository repository) {
        return args -> {
            log.info("Preloading {}", repository.save(new Ablesung(1, 123, "2021-01-01")));
            log.info("Preloading {}", repository.save(new Ablesung(2, 456, "2021-01-02")));
        };
    }
}

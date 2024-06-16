package eu.sadrian;

import eu.sadrian.model.Zaehler;
import eu.sadrian.repository.ZaehlerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    /*
    @Bean
    CommandLineRunner initDatabase(ZaehlerRepository repository) {
        return args -> {
            log.info("Preloading {}", repository.save(new Zaehler(33813, eu.sadrian.model.zaehlerArt.GAS)));
        };
    }

     */
}

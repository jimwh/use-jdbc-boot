package hiqus.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@EnableAutoConfiguration
@ImportResource({"application-context.xml"})
@ComponentScan("hiqus.lab")
public class Application {

    static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        log.info("application start...");
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        Busboy busboy = ctx.getBean(Busboy.class);
        busboy.test();
        busboy.truncateTables();
        log.info("application end...");
    }
}
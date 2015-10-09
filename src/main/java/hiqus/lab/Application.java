package hiqus.lab;

import hiqus.lab.conf.DataSourceConfig;
import hiqus.lab.service.Busboy;
import hiqus.lab.service.Extractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@ComponentScan("hiqus.lab")
@SpringApplicationConfiguration(classes = {DataSourceConfig.class})
public class Application {

    static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws IOException {
        log.info("application start...");
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        Busboy busboy = ctx.getBean(Busboy.class);
        busboy.test();
        Extractor extractor = ctx.getBean(Extractor.class);
        extractor.start();
        SpringApplication.exit(ctx);
        log.info("application end...");
    }
}
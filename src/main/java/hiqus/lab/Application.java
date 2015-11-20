package hiqus.lab;

import hiqus.lab.conf.DataSourceConfig;
import hiqus.lab.service.Busboy;
import hiqus.lab.service.Extractor;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Date;

@Configuration
@ComponentScan("hiqus.lab")
@SpringApplicationConfiguration(classes = {DataSourceConfig.class})
public class Application {

    static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws IOException {
        log.info("application start...");

        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        testDate();
        /*
        Busboy busboy = ctx.getBean(Busboy.class);
        busboy.test();
        Extractor extractor = ctx.getBean(Extractor.class);
        extractor.start();
        */
        SpringApplication.exit(ctx);
        log.info("application end...");
    }

    static void testDate() {
        // DateTime(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour)
        int plusDay=60;
        DateTime nowDateTime = new DateTime(2015, 11, 20, 2, 0);
        //DateTime d60 = nowDateTime.plusDays(plusDay);
        // 01/19/2016
        DateTime d60 = new DateTime(2016, 1, 19, 11, 59);
        log.info("d60={}", d60.toString("MM/dd/yyyy"));
        log.info("getDaysToExpiration={}", getDaysToExpiration(nowDateTime.toLocalDate(), d60.toDate()));

        //LocalDate now = LocalDate.now();
        //LocalDate d60LocalDate = now.plusDays(plusDay);
        //log.info("d60LocalDate={}", d60LocalDate.toString("MM/dd/yyyy"));
        /*
        LocalDate now = LocalDate.now();
        LocalDate d15 = now.plusDays(15);
        log.info("now={}, expiration={}", now.toString("MM/dd/yyyy"), d15.toString("MM/dd/yyyy") );
        log.info("getDaysToExpiration={}", getDaysToExpiration(now, d15.toDate()));
        //
        now = LocalDate.now();
        LocalDate d30 = now.plusDays(30);
        log.info("now={}, expiration={}", now.toString("MM/dd/yyyy"), d30.toString("MM/dd/yyyy") );
        log.info("getDaysToExpiration={}", getDaysToExpiration(now, d30.toDate()));
        */
    }

    static int getDaysToExpiration(LocalDate runDate, Date date) {
        return Days.daysBetween(runDate.toDateTimeAtStartOfDay(), new DateTime(date)).getDays();
        //
        //return Days.daysBetween(runDate.toDateTimeAtCurrentTime(), new DateTime(date)).getDays();
        //
    }

}
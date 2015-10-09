package hiqus.lab.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class Busboy {

    private static final Logger log = LoggerFactory.getLogger(Busboy.class);

    private final JdbcTemplate primaryJdbcTemplate;

    @Autowired
    public Busboy(@Qualifier("primaryJdbcTemplate") JdbcTemplate primaryJdbcTemplate) {
        this.primaryJdbcTemplate = primaryJdbcTemplate;
    }

    public void test() {
        String sql = "SELECT count(*) FROM document";
        Integer outbox = primaryJdbcTemplate.queryForObject(sql, Integer.class);
        log.info("outbox = {}", outbox);
    }

}

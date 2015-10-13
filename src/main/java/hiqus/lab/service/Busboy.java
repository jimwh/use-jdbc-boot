package hiqus.lab.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@Component
public class Busboy {

    private static final Logger log = LoggerFactory.getLogger(Busboy.class);

    private final JdbcTemplate primaryJdbcTemplate;

    @Autowired
    public Busboy(@Qualifier("primaryJdbcTemplate") JdbcTemplate primaryJdbcTemplate) {
        this.primaryJdbcTemplate = primaryJdbcTemplate;
        try {
            DatabaseMetaData metaData = primaryJdbcTemplate.getDataSource().getConnection().getMetaData();
            String productName = metaData.getDatabaseProductName();
            String version = metaData.getDatabaseProductVersion();
            int major = metaData.getDatabaseMajorVersion();
            int minor = metaData.getDatabaseMinorVersion();
            String info = String.format("product: %s, version: %s, major: %d, minor: %d",
                    productName, version, major, minor);
            log.info(info);
        } catch (SQLException e) {
            log.error("caught: ", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public void test() {
        String sql = "SELECT count(*) FROM document";
        Integer outbox = primaryJdbcTemplate.queryForObject(sql, Integer.class);
        log.info("outbox = {}", outbox);
    }

}

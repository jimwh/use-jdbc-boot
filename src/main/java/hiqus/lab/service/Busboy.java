package hiqus.lab.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@Component
public class Busboy {

    private static final Logger log = LoggerFactory.getLogger(Busboy.class);

    private final JdbcTemplate primaryJdbcTemplate;

    @Autowired
    public Busboy(@Qualifier("primaryJdbcTemplate") final JdbcTemplate primaryJdbcTemplate) {
        this.primaryJdbcTemplate = primaryJdbcTemplate;
        try {
            final Connection connection = primaryJdbcTemplate.getDataSource().getConnection();
            printInfo(connection);
            if( connection.isClosed() ) {
                throw new RuntimeException("connection closed");
            }
        } catch (SQLException e) {
            log.error("caught: ", e);
            throw new RuntimeException(e);
        }
    }

    private void printInfo(final Connection connection) {
        try {
            final DatabaseMetaData metaData = connection.getMetaData();
            final String productName = metaData.getDatabaseProductName();
            final String version = metaData.getDatabaseProductVersion();
            final int major = metaData.getDatabaseMajorVersion();
            final int minor = metaData.getDatabaseMinorVersion();
            final String info = String.format("product: %s, version: %s, major: %d, minor: %d",
                    productName, version, major, minor);
            log.info(info);
        }catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                log.error("caught:", e1);
            }
        }
    }
    public void testInfo() {
        final String sql = "SELECT count(*) FROM document";
        final Integer outbox = primaryJdbcTemplate.queryForObject(sql, Integer.class);
        log.info("outbox = {}", outbox);
    }

}

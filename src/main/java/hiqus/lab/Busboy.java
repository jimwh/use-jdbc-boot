package hiqus.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class Busboy {

    private static final String SQL_TABLE_RASCAL_CUMC_PROTOCOL = "select count(1) from ALL_TABLES where TABLE_NAME='RASCAL_CUMC_PROTOCOL'";
    private static final String SQL_TABLE_RASCAL_CUMC_DOCUMENT = "select count(1) from ALL_TABLES where TABLE_NAME='RASCAL_CUMC_DOCUMENT'";

    private static final String SQL_TABLE_CREATE_RASCAL_CUMC_PROTOCOL =
            "create table RASCAL_CUMC_PROTOCOL (" +
                    " PROTOCOLNUMBER     VARCHAR2(10)  NOT NULL," +
                    " SUFFIX             VARCHAR2(8)   NOT NULL," +
                    " PROTOCOLTITLE      VARCHAR2(500) NOT NULL," +
                    " APPROVALDATE       TIMESTAMP," +
                    " CONSTRAINT r2cprotocol_pk PRIMARY KEY (PROTOCOLNUMBER) )";

    private static final String SQL_TABLE_CREATE_RASCAL_CUMC_DOCUMENT =
            "create table RASCAL_CUMC_DOCUMENT (" +
                    " ID                  VARCHAR2(20)  NOT NULL," +
                    " PROTOCOLNUMBER      VARCHAR2(10)  NOT NULL," +
                    " DOCUMENTTYPE        VARCHAR2(90)  NOT NULL," +
                    " FILENAME            VARCHAR2(255) NOT NULL," +
                    " DOCUMENTIDENTIFIER  VARCHAR2(60)  NOT NULL," +
                    " DOCUMENTDATA        BLOB          NOT NULL," +
                    " CONSTRAINT r2cdocument_pk PRIMARY KEY (ID)," +
                    " CONSTRAINT fk_protocol" +
                    " FOREIGN KEY (PROTOCOLNUMBER)" +
                    " REFERENCES RASCAL_CUMC_PROTOCOL(PROTOCOLNUMBER) )";


    private static final Logger log = LoggerFactory.getLogger(Busboy.class);

    private final JdbcTemplate cumcJdbcTemplate;

    @Autowired
    public Busboy(
            @Qualifier("cumcJdbcTemplate") JdbcTemplate cumcJdbcTemplate) {
        this.cumcJdbcTemplate = cumcJdbcTemplate;
        //log.info(cumcJdbcTemplate.queryForObject("select ora_database_name from dual", String.class));
    }

    private static final String SQL_TEST = "select count(1) from RASCAL_CUMC_PROTOCOL where PROTOCOLNUMBER=?";

    public boolean hasProtocol(String protocolNumber) {
        String sql = "SELECT count(*) FROM Rascal_Protocol";
        // return cumcJdbcTemplate.queryForObject(SQL_TEST, Integer.class, protocolNumber) == 1;

        return cumcJdbcTemplate.queryForObject(sql, Integer.class, protocolNumber) == 1;
    }

    public void test() {
        String sql = "SELECT count(*) FROM PROTOCOL";
        // return cumcJdbcTemplate.queryForObject(SQL_TEST, Integer.class, protocolNumber) == 1;
        Integer outbox = cumcJdbcTemplate.queryForObject(sql, Integer.class);
        log.info("outbox = {}", outbox);
    }

    public void houseKeeper() {

        if (!hasProtocolTable()) {
            log.info("creating RASCAL_CUMC_PROTOCOL table");
            createProtocolTable();
        }else {
            log.info("truncate protocol table now...");
            truncateProtocolTable();
        }

        if (!hasDocumentTable()) {
            log.info("creating RASCAL_CUMC_DOCUMENT table");
            createDocumentTable();
            return;
        }else {
            log.info("truncate document table now...");
            truncateDocumentTable();
        }

    }

    private void truncateProtocolTable() {
        String[] sql = {
                "alter table RASCAL_CUMC_DOCUMENT disable constraint fk_protocol",
                "TRUNCATE table RASCAL_CUMC_PROTOCOL",
                // "TRUNCATE table RASCAL_CUMC_DOCUMENT",
                // "alter table RASCAL_CUMC_DOCUMENT enable constraint fk_protocol",
        };
        cumcJdbcTemplate.batchUpdate(sql);
    }

    private void truncateDocumentTable() {

        String[] sql = {
                "TRUNCATE table RASCAL_CUMC_DOCUMENT",
                // "alter table RASCAL_CUMC_DOCUMENT enable constraint fk_protocol",
        };
        cumcJdbcTemplate.batchUpdate(sql);
    }

    /*
    private void truncateTables() {
        log.info("truncate table now...");
        String[] sql = {
                "alter table RASCAL_CUMC_DOCUMENT disable constraint fk_protocol",
                "TRUNCATE table RASCAL_CUMC_PROTOCOL",
                "TRUNCATE table RASCAL_CUMC_DOCUMENT",
                "alter table RASCAL_CUMC_DOCUMENT enable constraint fk_protocol",
                };
        cumcJdbcTemplate.batchUpdate(sql);
    }
    */

    public boolean hasProtocolTable() {
        //int one = cumcJdbcTemplate.queryForObject(SQL_TABLE_RASCAL_CUMC_PROTOCOL, Integer.class);
        String sql = "SELECT count(*) FROM RASCAL_CUMC_PROTOCOL";
        Integer one = cumcJdbcTemplate.queryForObject(sql, Integer.class);
        return one == null ? false : true;
    }

    private boolean hasDocumentTable() {
        int one = cumcJdbcTemplate.queryForObject(SQL_TABLE_RASCAL_CUMC_DOCUMENT, Integer.class);
        return one == 1;
    }

    private void createProtocolTable() {
        cumcJdbcTemplate.execute(SQL_TABLE_CREATE_RASCAL_CUMC_PROTOCOL);
    }

    private void createDocumentTable() {
        cumcJdbcTemplate.execute(SQL_TABLE_CREATE_RASCAL_CUMC_DOCUMENT);
    }

}

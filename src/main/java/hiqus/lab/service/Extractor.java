package hiqus.lab.service;

import java.io.File;
import java.io.IOException;

import hiqus.lab.ExtractDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Extractor {

    private static final Logger log = LoggerFactory.getLogger(Extractor.class);
    private final JdbcTemplate jdbcTemplate;

    private String downloadDirectory;

    @Resource
    private RascalZipper zipper;

    @Resource
    private Environment env;

    @Autowired
    public Extractor(JdbcTemplate jt) {
        this.jdbcTemplate = jt;
    }

    public void start() throws IOException {
        this.downloadDirectory=env.getProperty("downloadDirectory");
        log.info("downloadDirectory={}", this.downloadDirectory);

        File file = new File(downloadDirectory);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                log.error("failed to create root dir");
                return;
            }
        }

        log.info("start to extract file to directory: {}", downloadDirectory);
        jdbcTemplate.query(ExtractDocument.SQL_EXTRACT_DOCUMENT,
                new ExtractDocument(downloadDirectory));

        zipper.zipFiles(downloadDirectory);

    }

}
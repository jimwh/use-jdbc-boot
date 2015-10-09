package hiqus.lab.service;

import java.io.File;
import java.io.IOException;

import hiqus.lab.ExtractDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Extractor {

    private static final Logger log = LoggerFactory.getLogger(Extractor.class);
    private final JdbcTemplate jdbcTemplate;

    // default directory
    // also it can be defined in application.properties
    // such as: downloadDirectory=/tmp/rascal_to_cum
    public static final String DefaultDownloadDirectory = File.separator + "tmp" + File.separator + "rascal_to_cumc";

    @Resource
    private RascalZipper zipper;

    @Autowired
    public Extractor(JdbcTemplate jt) {
        this.jdbcTemplate = jt;
    }

    @Value("${downloadDirectory}")
    private String downloadDirectory;

    public void start() throws IOException {

        if (downloadDirectory == null) {
            downloadDirectory = DefaultDownloadDirectory;
        }

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

        // zipper.zipFiles(downloadDirectory);

    }

}
package hiqus.lab;

import hiqus.lab.service.BlobToFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowCallbackHandler;

import javax.annotation.Resource;
import java.io.*;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExtractDocument implements RowCallbackHandler {

    public static final String
            SQL_EXTRACT_DOCUMENT =
            "select p.protocolnumber, p.suffix, d.ID, d.documenttype, d.filename, d.documentdata" +
            "  from protocol p, document d" +
            "  where p.protocolnumber = d.protocolnumber" +
            "  order by p.PROTOCOLNUMBER";

    private static final Logger log = LoggerFactory.getLogger(ExtractDocument.class);

    private final String downloadDirectory;

    public ExtractDocument(String downloadDirectory) {
        this.downloadDirectory = downloadDirectory;
    }

    @Override
    public void processRow(ResultSet resultSet) throws SQLException {

        Blob blob = resultSet.getBlob("DOCUMENTDATA");
        if (blob == null)
            throw new SQLException("foo barr...");

        String protocolNumber = resultSet.getString("PROTOCOLNUMBER");
        String suffix = resultSet.getString("SUFFIX");
        String docId = resultSet.getString("ID");
        String fileName = resultSet.getString("FILENAME");
        String documentType = resultSet.getString("DOCUMENTTYPE");

        InputStream in = blob.getBinaryStream();
        if (in == null) return;
        String folder = downloadDirectory + File.separator + protocolNumber+"_"+suffix+"_"+docId;
        fileName = folder + File.separator + fileName;
        new BlobToFile(folder, fileName, in);
        try {
            in.close();
        } catch (IOException e) {
            log.error("caught: ", e);
        }
    }

}

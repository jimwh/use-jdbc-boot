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
            SQL_EXTRACT_DOCUMENT = "select * from document order by protocolNumber";

    private static final Logger log = LoggerFactory.getLogger(ExtractDocument.class);

    private final String downloadDirectory;

//    @Resource
//    private BlobToFile blobToFile;

    public ExtractDocument(String downloadDirectory) {
        this.downloadDirectory = downloadDirectory;
    }

    @Override
    public void processRow(ResultSet resultSet) throws SQLException {

        Blob blob = resultSet.getBlob("DOCUMENTDATA");
        if (blob == null)
            throw new SQLException("foo barr...");

        String protocolNumber = resultSet.getString("PROTOCOLNUMBER");
        String documentType = resultSet.getString("DOCUMENTTYPE");
        String fileName = resultSet.getString("FILENAME");
        String documentIdentifier = resultSet.getString("DOCUMENTIDENTIFIER");

        InputStream in = blob.getBinaryStream();
        String folder = downloadDirectory + File.separator + protocolNumber;
        fileName = folder + File.separator + fileName;
        new BlobToFile(folder, fileName, in);
    }

}

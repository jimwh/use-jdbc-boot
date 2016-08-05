package hiqus.lab.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class BlobToFile {

    private static final Logger log = LoggerFactory.getLogger(BlobToFile.class);

    public BlobToFile(final String dir, final String fileName, final InputStream in) {
        folder(dir);
        blobToFile(fileName, in);
    }

    private void folder(final String dir) {
        final File file = new File(dir);
        if (!file.exists()) {
            boolean bool = file.mkdirs();
        }
    }

    private void blobToFile(final String fileName, final InputStream in) {
        try {
            final OutputStream os = new FileOutputStream(new File(fileName));
            FileCopyUtils.copy(in, os);
            os.close();
            // don't close input stream here, let caller close it
            // in.close();
        } catch (Exception e) {
            log.error("caught: ", e);
        }
    }

}

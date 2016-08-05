package hiqus.lab.service;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class RascalZipper {

    private File dir;
    private String zipFileName;
    private String zipFileRootDirectory;

    public void zipFiles(final String downloadFileDirectory) throws IOException {

        setUpZipFileDirectory(downloadFileDirectory);

        final ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        addDir(dir, out);
        out.close();
    }

    private void setUpZipFileDirectory(final String downloadFileDirectory) {
        this.zipFileRootDirectory =
                downloadFileDirectory.substring(downloadFileDirectory.lastIndexOf(File.separator));
        final DateTime dateTime = DateTime.now();
        this.zipFileName = downloadFileDirectory + "_" + dateTime.toString("yyyyMMdd") + ".zip";
        this.dir = new File(downloadFileDirectory);
    }

    private void addDir(final File dir, final ZipOutputStream out) throws IOException {
        final File[] listFiles = dir.listFiles();
        if(listFiles==null || listFiles.length==0) {
            return;
        }
        byte[] buf = new byte[4096];

        for(final File file : listFiles) {
            if (file.isDirectory()) {
                addDir(file, out);
                continue;
            }
            final FileInputStream in = getFileInputStreamByFile(file.getCanonicalFile());
            if(in == null) {
                continue;
            }
            String path = file.getCanonicalPath();
            path = path.substring(path.indexOf(zipFileRootDirectory));
            out.putNextEntry(new ZipEntry(path));
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.closeEntry();
            in.close();
        }
    }

    private FileInputStream getFileInputStreamByFile(final File file) throws FileNotFoundException {
        return new FileInputStream(file);
    }

}

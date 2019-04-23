package org.kk.uploaddownload;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.apache.log4j.Logger;
import org.bouncycastle.crypto.CryptoException;
import org.kk.constants.Constants;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class Downloader extends BaseTransfer {
    private static Logger LOG = Logger.getLogger(Downloader.class.getName());

    public List<String> downloadAndDeleteCloudFiles() throws GeneralSecurityException, IOException, CryptoException {

        List<String> zipFiles = new ArrayList<>();
        Drive service = getDrive();
        FileList result = service.files().list()
                .setPageSize(1000)
                .setFields("nextPageToken, files(id, name)")
                .execute();

        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            LOG.info("No files found.");
        } else {
            for (File file : files) {
                String pathname = Constants.COMPRESSED_FILE_PATH_TO_DOWNLOAD + file.getId();
                FileOutputStream outputStream = new FileOutputStream(new java.io.File(pathname));
                service.files().get(file.getId())
                        .executeMediaAndDownloadTo(outputStream);
                outputStream.close();
                zipFiles.add(pathname);
                LOG.info("Downloading FileTo=" + file.getName() + " FileId=" + file.getId());
                service.files().delete(file.getId()).execute();
            }
        }
        return zipFiles;
    }

}

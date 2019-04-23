package org.kk.uploaddownload;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.kk.utils.FileOperations.deleteFile;

public class FileUploader extends BaseTransfer {
    private static Logger LOG = Logger.getLogger(FileUploader.class.getName());

    private void upload(String filePath) throws GeneralSecurityException, IOException {
        LOG.info("Upload file to drive starts. FileName=" + filePath);
        Drive service = getDrive();
        File fileMetadata = new File();
        fileMetadata.setName(filePath);
        java.io.File fileToUpload = new java.io.File(filePath);
        FileContent mediaContent = new FileContent("application/zip", fileToUpload);
        File file = service.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();
        LOG.info("Uploaded file to drive. FileName=" + filePath + " FileId=" + file.getId());
    }

    public void uploadAndDeleteZip(String zipFileName) throws GeneralSecurityException, IOException {
        FileUploader fileUploader = new FileUploader();
        fileUploader.upload(zipFileName);
        deleteFile(zipFileName);
    }
}

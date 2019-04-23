package org.kk.compress;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.crypto.CryptoException;
import org.kk.constants.Constants;
import org.kk.encryption.CryptoUtils;
import org.kk.record.AudioRecordingService;
import org.kk.uploaddownload.FileUploader;
import org.kk.utils.FileOperations;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static org.kk.utils.FileNameUtils.getFormattedFileName;

public class CompressionService {

    private static List<String> listOfFilesToDelete = new ArrayList<>();
    private static Logger LOG = Logger.getLogger(AudioRecordingService.class.getName());

    public void compressAndEncrypt(List<String> listOfFiles) {
        try {
            final String zipFileName = getFormattedFileName() + ".zip";
            LOG.info("Files ready to compress. Files going to be included=" + listOfFiles);
            FileOutputStream fileOutputStream = new FileOutputStream(zipFileName);
            ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
            while (!listOfFiles.isEmpty()) {
                String fileName = listOfFiles.get(0);
                LOG.debug("Encryption Starts FileName=" + fileName);
                File file = new File(fileName + "-enc.wav");
                try {
                    CryptoUtils.encrypt(Constants.ENC_KEY, new File(fileName), file);
                } catch (CryptoException e) {
                    e.printStackTrace();
                }
                LOG.debug("Encryption Completed new File=" + file.getName());
                LOG.debug("Compression Starts for File=" + file.getName());
                FileInputStream fis = new FileInputStream(file);
                zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                zipOutputStream.write(IOUtils.toByteArray(fis));
                LOG.debug("Compression Completed for File=" + file.getName());
                listOfFiles.remove(fileName);
                listOfFilesToDelete.add(fileName);
                fis.close();
                file.delete();
            }
            zipOutputStream.closeEntry();
            zipOutputStream.close();
            fileOutputStream.close();
            //new FileUploader().uploadAndDeleteZip(zipFileName);
        } catch (Exception e) {
            LOG.info("Exception While uploading " + e.getMessage(), e);
        }

        for (String file : listOfFilesToDelete) {
            LOG.debug("Removing files after upload=" + file);
            FileOperations.deleteFile(file);
        }
    }

    public List<String> decompressAndDecrypt(String filePath) throws IOException, CryptoException {
        List<String> decryptedFiles = new ArrayList<>();
        ZipFile zipFile = new ZipFile(filePath);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            InputStream stream = zipFile.getInputStream(entry);
            String compressedFile = Constants.COMPRESSED_FILE_PATH_TO_DOWNLOAD + entry.getName();
            java.io.File targetFile = new java.io.File(compressedFile);
            String decryptedFilePath = Constants.DECRYPTED_FILE_PATH + entry.getName();
            FileUtils.copyInputStreamToFile(stream, targetFile);
            LOG.info("Decryption starts for file=" + targetFile.getName());
            CryptoUtils.decrypt(Constants.ENC_KEY, targetFile,
                    new java.io.File(decryptedFilePath));
            LOG.info("Decryption completed for file=" + targetFile.getName() + " New file name is=" + decryptedFilePath);
            decryptedFiles.add(decryptedFilePath);
            stream.close();
            targetFile.delete();
        }
        zipFile.close();
        return decryptedFiles;
    }
}

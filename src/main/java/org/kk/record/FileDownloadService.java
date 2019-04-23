package org.kk.record;

import org.apache.log4j.Logger;
import org.bouncycastle.crypto.CryptoException;
import org.kk.compress.CompressionService;
import org.kk.uploaddownload.Downloader;
import org.kk.utils.FileOperations;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class FileDownloadService {
    private static Logger LOG = Logger.getLogger(FileDownloadService.class.getName());

    public void download() throws GeneralSecurityException, IOException, CryptoException {
        LOG.info("Download Request Received");
        Downloader downloader = new Downloader();
        //List<String> files = downloader.downloadAndDeleteCloudFiles();
        List<String> files = downloader.downloadAndDeleteCloudFiles();
        LOG.info("Download Completed. Files Downloaded=" + files);
        CompressionService compressionService = new CompressionService();
        for (String path : files) {
            LOG.info("Decompression and Decryption Starts for zip file=" + path);
            final List<String> decryptedFiles = compressionService.decompressAndDecrypt(path);
            //FileOperations.deleteFile(path);
            LOG.info("Decompression and Decryption Completed and stored files " + decryptedFiles);
        }
    }


    public void decryptFromLocal() throws GeneralSecurityException, IOException, CryptoException {
        LOG.info("Download Request Received");
        File[] files = extractFromLocal();
        LOG.info("Download Completed. Files Downloaded=" + files);
        CompressionService compressionService = new CompressionService();
        for (File path : files) {
            LOG.info("Decompression and Decryption Starts for zip file=" + path);
            final List<String> decryptedFiles = compressionService.decompressAndDecrypt(path.getPath());
            LOG.info("Decompression and Decryption Completed and stored files " + decryptedFiles);
        }
    }

    private File[] extractFromLocal() {
        File folder = new File("C:\\Users\\Krishnakumar\\Desktop\\Download");
        return folder.listFiles();
    }
}



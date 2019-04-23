package org.kk;

import org.bouncycastle.crypto.CryptoException;
import org.kk.record.AudioRecordingService;
import org.kk.record.FileDownloadService;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class App {

    private static final String DOWNLOAD_PGM_ARGUMENT = "Download";

    public static void main(String[] args) throws InterruptedException, GeneralSecurityException, IOException, CryptoException {
        if (shouldDownload(args)) {
            new FileDownloadService().download();
        } else {
            new AudioRecordingService().record();
        }
    }

    private static boolean shouldDownload(String[] args) {
        return args != null && args.length > 0 && args[0].equalsIgnoreCase(DOWNLOAD_PGM_ARGUMENT);
    }
}

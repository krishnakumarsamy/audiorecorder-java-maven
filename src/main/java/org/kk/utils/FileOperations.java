package org.kk.utils;

import java.io.File;

public class FileOperations {
    public static boolean deleteFile(String zipFileName) {
        return new File(zipFileName).delete();
    }

}

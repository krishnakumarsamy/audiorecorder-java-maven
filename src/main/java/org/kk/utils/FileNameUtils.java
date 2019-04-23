package org.kk.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileNameUtils {

    public static String getFormattedFileName() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSS");
        return LocalDateTime.now().format(formatter);
    }
}

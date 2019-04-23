package org.kk.record;

import org.apache.log4j.Logger;
import org.kk.compress.CompressionService;
import org.kk.utils.FileNameUtils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.kk.constants.Constants.RECORDING_TIME;

public class AudioRecordingService {
    private static Logger LOG = Logger.getLogger(AudioRecordingService.class.getName());
    public static List<String> listOfFiles = new ArrayList<>();

    public void record() throws InterruptedException {
        while (true) {
            if (shouldRecord()) {
                record(RECORDING_TIME);
            } else {
                LOG.info("Skipp recording current Time" + LocalTime.now());
                Thread.sleep(60000);
            }
        }
    }

    private static boolean shouldRecord() {
        return isBetween(LocalTime.now(), LocalTime.of(07, 00), LocalTime.of(14, 00)) ||
                isBetween(LocalTime.now(), LocalTime.of(19, 0), LocalTime.of(23, 59));
    }

    private static boolean isBetween(LocalTime candidate, LocalTime start, LocalTime end) {
        return !candidate.isBefore(start) && !candidate.isAfter(end);  // Inclusive.
    }

    private String record(long recordingTime) {

        final AudioRecorder audioRecorder = new AudioRecorder();
        String fileName = FileNameUtils.getFormattedFileName() + ".wav";
        new Thread(() -> {
            String fileToRecord = fileName;
            try {
                Thread.sleep(recordingTime);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            audioRecorder.finish(fileToRecord);
            LOG.info("Recording time completed. FileName=" + fileToRecord);
            listOfFiles.add(fileToRecord);
            if (listOfFiles.size() > 4) {
                try {
                    new CompressionService().compressAndEncrypt(new ArrayList(listOfFiles));
                } catch (Exception e) {
                    LOG.debug("Error while Compress and Encrypt" + e.getMessage(), e);
                }
                listOfFiles.clear();
            }
        }).start();
        audioRecorder.start(fileName);
        return fileName;
    }
}

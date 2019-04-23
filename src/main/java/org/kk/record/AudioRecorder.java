package org.kk.record;

import org.apache.log4j.Logger;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioRecorder {
    private static Logger LOGGER = Logger.getLogger(AudioRecorder.class.getName());
    private AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    private TargetDataLine line;

    private AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        return new AudioFormat(sampleRate, sampleSizeInBits,
                channels, true, true);
    }

    public void start(String fileName) {
        LOGGER.info("Start Recording with file name=" + fileName);
        try {
            File wavFile = new File(fileName);
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line not supported");
                System.exit(0);
            }
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            AudioInputStream ais = new AudioInputStream(line);
            AudioSystem.write(ais, fileType, wavFile);

        } catch (LineUnavailableException | IOException ex) {
            ex.printStackTrace();
        }
    }

    public void finish(String fileName) {
        line.stop();
        line.close();
        LOGGER.info("Recording completed with file name=" + fileName);
    }

}

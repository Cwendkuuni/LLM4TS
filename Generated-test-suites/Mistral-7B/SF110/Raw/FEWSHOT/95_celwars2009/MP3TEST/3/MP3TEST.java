package org.apache.commons.mp3; // Assuming the package name is org.apache.commons.mp3

import static org.junit.Assert.*;

import javax.sound.sampled.*;
import java.io.*;

import org.junit.Test;

public class MP3Test {

    @Test
    public void testMP3Constructor() {
        String validFilename = "test.mp3";
        MP3 mp3 = new MP3(validFilename);
        assertNotNull(mp3.in);
        assertNotNull(mp3.filename);
        assertEquals(validFilename, mp3.filename);
    }

    @Test(expected = NullPointerException.class)
    public void testMP3ConstructorWithNullFilename() {
        new MP3(null);
    }

    @Test(expected = FileNotFoundException.class)
    public void testMP3ConstructorWithInvalidFilename() {
        String invalidFilename = "nonexistent.mp3";
        new MP3(invalidFilename);
    }

    @Test
    public void testThreadStart() {
        String validFilename = "test.mp3";
        MP3 mp3 = new MP3(validFilename);
        mp3.join();
    }

    @Test
    public void testThreadExecution() throws Exception {
        String validFilename = "test.mp3";
        MP3 mp3 = new MP3(validFilename);
        mp3.join();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 16000, 16, 1, 16, 16000, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();

        byte[] data = new byte[2048];
        int read = 0;
        while ((read = line.read(data, 0, data.length)) != -1) {
            out.write(data, 0, read);
        }
        line.stop();
        line.close();

        byte[] expectedData = out.toByteArray();
        byte[] actualData = mp3.in.readAllBytes();
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testThreadInterruption() {
        String validFilename = "test.mp3";
        MP3 mp3 = new MP3(validFilename);
        mp3.interrupt();
    }

    @Test
    public void testThreadCleanup() {
        String validFilename = "test.mp3";
        MP3 mp3 = new MP3(validFilename);
        mp3.join();

        if (mp3.in != null) {
            try {
                mp3.in.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }
}
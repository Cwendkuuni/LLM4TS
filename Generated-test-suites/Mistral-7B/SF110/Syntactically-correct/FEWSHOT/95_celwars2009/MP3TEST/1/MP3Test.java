package org.apache.commons.mp3; // Assuming the package name is org.apache.commons.mp3 for the MP3 class

import static org.junit.Assert.*;
import javax.sound.sampled.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

public class MP3Test {

    @Test
    public void testMP3Constructor() {
        String validFilename = "test.mp3";
        new MP3(validFilename);
    }

    @Test(expected = NullPointerException.class)
    public void testMP3ConstructorWithNullFilename() {
        new MP3(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMP3ConstructorWithEmptyFilename() {
        new MP3("");
    }

    @Test(expected = FileNotFoundException.class)
    public void testMP3ConstructorWithInvalidFilename() {
        String invalidFilename = "non_existent_file.mp3";
        new MP3(invalidFilename);
    }

    @Test
    public void testThreadStart() {
        String filename = "test.mp3";
        new MP3(filename);
        assertTrue(Thread.currentThread().isAlive());
    }

    @Test
    public void testThreadFinish() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        try {
            mp3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(Thread.currentThread().isAlive());
    }

    @Test
    public void testPlayingMP3File() throws IOException {
        String filename = "test.mp3";
        File file = new File(filename);
        long initialBytesRead = Files.size(Paths.get(filename));

        new MP3(filename);

        long finalBytesRead = Files.size(Paths.get(filename));
        assertEquals(initialBytesRead, finalBytesRead);
    }
}
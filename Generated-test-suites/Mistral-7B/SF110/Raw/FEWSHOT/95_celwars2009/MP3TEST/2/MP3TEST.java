package org.apache.commons.mp3; // Assuming the package name is org.apache.commons.mp3 for the MP3 class

import static org.junit.Assert.*;
import org.junit.Test;

import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

    @Test
    public void testMP3Construction() {
        MP3 mp3 = new MP3("test.mp3");
        assertNotNull(mp3.in);
        assertNotNull(mp3.din);
        assertNotNull(mp3.filename);
        assertEquals("test.mp3", mp3.filename);
    }

    @Test(expected = NullPointerException.class)
    public void testMP3NullFilename() {
        new MP3(null);
    }

    @Test(expected = FileNotFoundException.class)
    public void testMP3NonExistentFile() {
        new MP3("non_existent_file.mp3");
    }

    @Test
    public void testMP3PlayAudio() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        MP3 mp3 = new MP3("test.mp3");
        mp3.join(); // Wait for the thread to finish playing the audio
    }

    @Test
    public void testMP3CloseResources() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.join();

        assertTrue(mp3.in.isClosed());
        assertTrue(mp3.din.isClosed());
    }

    @Test(expected = IOException.class)
    public void testMP3ClosedInputStream() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.join();

        try (InputStream inputStream = mp3.in) {
            inputStream.read(); // This should throw an IOException as the InputStream is closed
        }
    }
}
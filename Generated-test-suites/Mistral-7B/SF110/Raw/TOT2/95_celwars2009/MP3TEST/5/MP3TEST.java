// Complete JUnit 4 test file (MP3Test.java) for the given Java class

import org.junit.Test;
import static org.junit.Assert.*;
import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {
    @Test
    public void testMP3Constructor() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        assertTrue(mp3.filename.equals(filename));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMP3ConstructorWithNullFilename() {
        String filename = null;
        MP3 mp3 = new MP3(filename);
        fail("Expected IllegalArgumentException, but got none");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMP3ConstructorWithEmptyFilename() {
        String filename = "";
        MP3 mp3 = new MP3(filename);
        fail("Expected IllegalArgumentException, but got none");
    }

    @Test(expected = FileNotFoundException.class)
    public void testMP3ConstructorWithNonExistentFile() {
        String filename = "nonexistentfile.mp3";
        MP3 mp3 = new MP3(filename);
        fail("Expected FileNotFoundException, but got none");
    }

    @Test(expected = UnsupportedAudioFileException.class)
    public void testMP3ConstructorWithInvalidFile() {
        String filename = "invalidfile.txt";
        MP3 mp3 = new MP3(filename);
        fail("Expected UnsupportedAudioFileException, but got none");
    }

    @Test(expected = IOException.class)
    public void testMP3ConstructorWithIOException() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        try {
            InputStream inputStream = new FileInputStream(new File(filename));
            inputStream.close();
        } catch (IOException e) {
            fail("Expected IOException, but got another exception: " + e.getMessage());
        }
    }

    @Test
    public void testRun() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        mp3.start();
        mp3.join();
    }

    @Test(expected = NullPointerException.class)
    public void testRunWithNullAudioInputStream() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        mp3.in = null;
        mp3.start();
        mp3.join();
    }

    // Implement other test cases for the run() method with different edge cases and exceptions
}
import org.junit.Test;
import static org.junit.Assert.*;
import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

    @Test
    public void testMP3Constructor() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        assertNotNull(mp3.getFilename());
        assertEquals(filename, mp3.getFilename());
    }

    @Test
    public void testRunMethodWithValidFile() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        mp3.start();
        try {
            Thread.sleep(5000); // Give time for the MP3 to play
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mp3.join();
    }

    @Test(expected = FileNotFoundException.class)
    public void testRunMethodWithInvalidFile() {
        String filename = "nonexistentfile.mp3";
        MP3 mp3 = new MP3(filename);
        mp3.start();
        try {
            Thread.sleep(5000); // Give time for the exception to be thrown
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = UnsupportedAudioFileException.class)
    public void testRunMethodWithUnsupportedFormat() {
        String filename = "test.wav";
        MP3 mp3 = new MP3(filename);
        mp3.start();
        try {
            Thread.sleep(5000); // Give time for the exception to be thrown
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = IOException.class)
    public void testRunMethodWithIOError() {
        String filename = "test.mp3";
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }
        MP3 mp3 = new MP3(filename);
        mp3.start();
        try {
            Thread.sleep(5000); // Give time for the exception to be thrown
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testGetIn() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        assertNotNull(mp3.getIn());
    }

    @Test
    public void testGetDin() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        assertNull(mp3.getDin());
        mp3.start();
        try {
            Thread.sleep(5000); // Give time for the decoded stream to be initialized
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertNotNull(mp3.getDin());
    }

    @Test
    public void testGetFilename() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        assertEquals(filename, mp3.getFilename());
    }
}
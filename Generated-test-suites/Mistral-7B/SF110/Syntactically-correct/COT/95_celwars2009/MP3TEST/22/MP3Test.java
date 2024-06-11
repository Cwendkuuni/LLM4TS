import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

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
    public void testRunMethodNormalCase() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        mp3.run();
    }

    @Test(expected = NullPointerException.class)
    public void testRunMethodNullFilename() {
        MP3 mp3 = new MP3(null);
        mp3.run();
    }

    @Test(expected = FileNotFoundException.class)
    public void testRunMethodFileNotFound() {
        String filename = "does_not_exist.mp3";
        MP3 mp3 = new MP3(filename);
        mp3.run();
    }

    @Test(expected = UnsupportedAudioFileException.class)
    public void testRunMethodUnsupportedFormat() {
        String filename = "test.wav";
        MP3 mp3 = new MP3(filename);
        mp3.run();
    }

    @Test(expected = IOException.class)
    public void testRunMethodIOError() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        throw new RuntimeException("Simulate an IO error during the run method execution");
    }

    @Test
    public void testCloseDin() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        mp3.run();
        assertNull(mp3.din);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenLineInvalidFormat() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.MIDI, 44100, 2, 16, 4, 44100, false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
        SourceDataLine line = (SourceDataLine)AudioSystem.getLine(info);
        mp3.run();
        line.open(decodedFormat);
    }
}
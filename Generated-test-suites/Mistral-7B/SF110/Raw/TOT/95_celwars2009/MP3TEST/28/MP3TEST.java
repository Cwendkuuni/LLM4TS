import org.junit.Test;
import static org.junit.Assert.*;

public class MP3Test {
    @Test
    public void testMP3ConstructionWithValidFilename() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        assertNotNull(mp3.in);
        assertNotNull(mp3.din);
        assertNotNull(mp3.filename);
        assertEquals(filename, mp3.filename);
    }

    
    @Test
    public void testMP3ConstructionWithNullFilename() {
        String filename = null;
        MP3 mp3 = new MP3(filename);
        assertNull(mp3.in);
        assertNull(mp3.din);
        assertNull(mp3.filename);
        assertEquals("", mp3.filename);
    }

    @Test
    public void testMP3ConstructionWithEmptyFilename() {
        String filename = "";
        MP3 mp3 = new MP3(filename);
        assertNull(mp3.in);
        assertNull(mp3.din);
        assertNotNull(mp3.filename);
        assertEquals("", mp3.filename);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMP3ConstructionWithInvalidFilename() {
        String filename = "test.txt";
        MP3 mp3 = new MP3(filename);
    }

    @Test(expected = NullPointerException.class)
    public void testMP3ConstructionWithNullThread() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename, null);
    }

    @Test
    public void testMP3Run() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        mp3.run();
        // Add assertions for audio output verification if possible
    }

    @Test(expected = FileNotFoundException.class)
    public void testMP3RunWithInvalidFile() {
        String filename = "nonexistentfile.mp3";
        MP3 mp3 = new MP3(filename);
        mp3.run();
    }

    @Test(expected = UnsupportedAudioFileException.class)
    public void testMP3RunWithUnsupportedFormat() {
        String filename = "test.wav";
        MP3 mp3 = new MP3(filename);
        mp3.run();
    }

    @Test(expected = IOException.class)
    public void testMP3RunWithIOError() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        doReturn(null).when(mp3).din;
        mp3.run();
    }

    @Test(expected = NullPointerException.class)
    public void testMP3RunWithNullDataLine() {
        SourceDataLine line = null;
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        doReturn(line).when(mp3).getLine(any(DataLine.Info.class));
        mp3.run();
    }

    
}
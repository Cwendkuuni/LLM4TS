import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class MP3Test {

    @Test
    public void constructorTest() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        Assert.assertNotNull(mp3.getFilename());
    }

    @Test(expected = NullPointerException.class)
    public void constructorWithNullFilenameTest() {
        MP3 mp3 = new MP3(null);
    }

    @Test
    public void runTest() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        mp3.run();
    }

    @Test(expected = FileNotFoundException.class)
    public void runWithNonExistentFileTest() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        String filename = "non_existent_file.mp3";
        MP3 mp3 = new MP3(filename);
        mp3.run();
    }

    @Test(expected = IllegalArgumentException.class)
    public void runWithInvalidFormatTest() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        String filename = "invalid_format.mp3";
        MP3 mp3 = new MP3(filename);
        mp3.run();
    }

    @Test
    public void closeTest() throws IOException {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        mp3.run();
        mp3.stop();
        mp3.close();
    }

    @Test
    public void startTest() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        mp3.start();
        mp3.run();
        mp3.stop();
    }
}
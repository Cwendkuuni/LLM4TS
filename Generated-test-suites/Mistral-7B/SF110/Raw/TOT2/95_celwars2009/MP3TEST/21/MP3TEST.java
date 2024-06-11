import org.junit.Assert;
import org.junit.Test;
import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

    @Test
    public void testMP3Constructor() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        assertNotNull(mp3.in);
        assertNotNull(mp3.din);
        assertNotNull(mp3.filename);
        assertEquals(filename, mp3.filename);
    }

    @Test
    public void testInvalidFileName() {
        String invalidFileName = "";
        assertThrows(IllegalArgumentException.class, () -> new MP3(invalidFileName));
    }

    @Test
    public void testMP3Run() throws IOException, LineUnavailableException {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        byte[] data = new byte[4096];
        AudioFormat baseFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4 * 2, 44100, false);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        AudioSystem.write(mp3.in, baseFormat, out);
        AudioInputStream tempIn = new AudioInputStream(new ByteArrayInputStream(out.toByteArray()));
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, baseFormat);
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(baseFormat);
        line.start();
        int nBytesRead;
        while ((nBytesRead = tempIn.read(data, 0, data.length)) != -1) {
            line.write(data, 0, nBytesRead);
        }
        line.drain();
        line.stop();
        line.close();
        tempIn.close();
        mp3.join();
    }

    @Test
    public void testMP3FileNotFound() {
        String filename = "non_existent_file.mp3";
        MP3 mp3 = new MP3(filename);
        assertThrows(FileNotFoundException.class, mp3::run);
    }

    @Test
    public void testMP3FileWithNullFileName() {
        assertThrows(NullPointerException.class, () -> new MP3(null));
        MP3 mp3 = new MP3("");
        assertNotNull(mp3);
        assertNull(mp3.filename);
        assertThrows(IllegalArgumentException.class, mp3::run);
    }

    @Test
    public void testMP3EmptyFileName() {
        MP3 mp3 = new MP3("");
        assertThrows(IllegalArgumentException.class, mp3::run);
    }

    @Test
    public void testMP3NullAudioInputStream() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        mp3.in = null;
        assertThrows(NullPointerException.class, mp3::run);
    }
}
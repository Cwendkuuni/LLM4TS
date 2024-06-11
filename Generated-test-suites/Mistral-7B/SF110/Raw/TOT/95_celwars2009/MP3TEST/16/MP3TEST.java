import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

 private File testFile;
 private MP3 mp3;

 @Before
 public void setUp() throws IOException {
 testFile = new File("test.mp3");
 testFile.createNewFile();
 }

 @After
 public void tearDown() throws Exception {
 testFile.delete();
 }

 @Test
 public void testConstructor() {
 MP3 mp3 = new MP3("test.mp3");
 assertNotNull(mp3);
 }

 @Test
 public void testNullFilename() {
 assertThrows(NullPointerException.class, () -> new MP3(null));
 }

 @Test
 public void testEmptyFilename() {
 assertThrows(IllegalArgumentException.class, () -> new MP3(""));
 }

 @Test
 public void testInvalidFilename() {
 assertThrows(FileNotFoundException.class, () -> new MP3("invalidFile.txt"));
 }

 @Test
 public void testPlayMP3() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
 mp3 = new MP3("test.mp3");
 assertFalse(mp3.isInterrupted());
 mp3.join();
 }

 @Test
 public void testPlayEmptyStream() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
 File emptyFile = new File("");
 mp3 = new MP3(emptyFile.getAbsolutePath());
 assertFalse(mp3.isInterrupted());
 mp3.join();
 }

 @Test(expected = IOException.class)
 public void testPlayFileNotFound() throws UnsupportedAudioFileException, LineUnavailableException {
 File fileNotExist = new File("nonExistentFile.mp3");
 mp3 = new MP3(fileNotExist.getAbsolutePath());
 }

 @Test(expected = LineUnavailableException.class)
 public void testPlayLineUnavailable() throws UnsupportedAudioFileException, IOException {
 mp3 = new MP3("test.mp3");
 SourceDataLine line = (SourceDataLine)AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, new AudioFormat(44100, 16, 1, false, false)));
 assertThrows(LineUnavailableException.class, () -> line.open(new AudioFormat(44100, 16, 1, false, false)));
 }

 @Test(expected = NullPointerException.class)
 public void testPlayNullDataLine() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
 mp3 = new MP3("test.mp3");
 SourceDataLine line = null;
 assertThrows(NullPointerException.class, () -> line.open(new AudioFormat(44100, 16, 1, false, false)));
 }
}
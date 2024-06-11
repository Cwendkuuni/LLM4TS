import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

 private MP3 mp3;
 private String validFilename = "test.mp3";
 private String invalidFilename = "nonexistent.mp3";

 @Before
 public void setUp() {
 mp3 = new MP3(validFilename);
 }

 @After
 public void tearDown() {
 try {
 if (mp3 != null) {
 mp3.join();
 }
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testConstructorWithValidFileName() {
 assertNotNull(mp3);
 assertEquals(validFilename, mp3.filename);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullFileName() {
 mp3 = new MP3(null);
 fail("Expected NullPointerException");
 }

 @Test(expected = FileNotFoundException.class)
 public void testConstructorWithInvalidFileName() {
 mp3 = new MP3(invalidFilename);
 fail("Expected FileNotFoundException");
 }

 @Test
 public void testRunMethod() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
 assertNotNull(mp3.in);
 assertNotNull(mp3.din);
 }

 @Test(expected = IOException.class)
 public void testRunMethodWithIOException() {
 mp3 = new MP3(invalidFilename);
 mp3.run();
 fail("Expected IOException");
 }

 @Test(expected = UnsupportedAudioFileException.class)
 public void testRunMethodWithUnsupportedAudioFileException() {
 ByteArrayInputStream byteStream = new ByteArrayInputStream(new byte[0]);
 AudioInputStream audioStream = AudioSystem.getAudioInputStream(byteStream);
 MP3 mp3WithInvalidStream = new MP3(null);
 mp3WithInvalidStream.in = audioStream;
 mp3WithInvalidStream.run();
 fail("Expected UnsupportedAudioFileException");
 }

 @Test(expected = LineUnavailableException.class)
 public void testRunMethodWithLineUnavailableException() {
 DataLine.Info info = new DataLine.Info(SourceDataLine.class, new AudioFormat(16, 44100, 1, 16, 44100, false));
 SourceDataLine dummyLine = (SourceDataLine) AudioSystem.getLine(info);
 if (dummyLine != null) {
 line.open(new AudioFormat(16, 44100, 1, 16, 44100, false, false));
 }
 MP3 mp3WithNoLine = new MP3(validFilename);
 mp3WithNoLine.run();
 fail("Expected LineUnavailableException");
 }

 @Test
 public void testCloseResources() {
 try {
 mp3.join();
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 mp3 = new MP3(validFilename);
 mp3.run();
 assertNull(mp3.din);
 assertNull(mp3.in);
 }
}
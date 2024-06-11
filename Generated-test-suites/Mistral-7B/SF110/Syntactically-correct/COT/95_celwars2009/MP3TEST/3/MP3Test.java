import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

 private MP3 mp3;
 private String validFilename = "test.mp3";
 private String nonExistentFilename = "nonexistent.mp3";

 @Before
 public void setUp() {
 mp3 = new MP3(validFilename);
 }

 // Test constructor
 @Test
 public void testMP3Constructor() {
 assertNotNull(mp3);
 assertEquals(validFilename, mp3.filename);
 }

 // Test run method with valid filename
 @Test
 public void testRunWithValidFilename() throws Exception {
 Thread.sleep(5000); // give the thread some time to execute
 assertTrue(mp3.isAlive());
 }

 // Test run method with non-existent filename
 @Test(expected = FileNotFoundException.class)
 public void testRunWithNonExistentFilename() {
 mp3 = new MP3(nonExistentFilename);
 mp3.start();
 }

 // Test exception handling in run method
 @Test
 public void testRunWithIOException() {
 mp3 = new MP3(validFilename);
 try {
 mp3.start();
 Thread.sleep(5001); // let the exception be thrown
 fail("Expected IOException not thrown");
 } catch (IOException e) {
 // expected
 }
 }

 // Test exception handling in close method
 @Test
 public void testCloseWithIOException() {
 try {
 mp3 = new MP3(validFilename);
 mp3.start();
 DataLine.Info info = new DataLine.Info(SourceDataLine.class, new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 16, 44100, false));
 SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
 line.open(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 16, 44100, false));
 line.start();
 mp3.close();
 line.close();
 } catch (Exception e) {
 // expected
 }
 }

 // Test null filename in constructor
 @Test(expected = NullPointerException.class)
 public void testMP3ConstructorWithNullFilename() {
 mp3 = new MP3(null);
 }

 // Test empty filename in constructor
 @Test(expected = IllegalArgumentException.class)
 public void testMP3ConstructorWithEmptyFilename() {
 mp3 = new MP3("");
 }
}
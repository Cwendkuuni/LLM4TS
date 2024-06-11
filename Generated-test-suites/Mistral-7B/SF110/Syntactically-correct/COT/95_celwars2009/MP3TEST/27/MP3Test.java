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

 // Test constructor with valid filename
 @Test
 public void testMP3ConstructorWithValidFilename() {
 assertNotNull(mp3);
 assertEquals(validFilename, mp3.filename);
 }

 // Test constructor with null filename
 @Test(expected = NullPointerException.class)
 public void testMP3ConstructorWithNullFilename() {
 mp3 = new MP3(null);
 }

 // Test constructor with empty filename
 @Test(expected = IllegalArgumentException.class)
 public void testMP3ConstructorWithEmptyFilename() {
 mp3 = new MP3("");
 }

 // Test constructor with invalid filename
 @Test(expected = FileNotFoundException.class)
 public void testMP3ConstructorWithInvalidFilename() {
 mp3 = new MP3(invalidFilename);
 }

 // Test run method with valid filename
 @Test
 public void testRunWithValidFilename() throws Exception {
 Thread mp3Thread = new Thread(mp3);
 mp3Thread.start();
 mp3Thread.join();
 assertTrue(mp3Thread.isInterrupted());
 }

 // Test run method with invalid filename
 @Test(expected = FileNotFoundException.class)
 public void testRunWithInvalidFilename() {
 Thread mp3Thread = new Thread(mp3);
 mp3Thread.start();
 mp3Thread.join();
 assertFalse(mp3Thread.isInterrupted());
 }

 // Test exception handling
 @Test
 public void testExceptionHandling() {
 File nonexistentFile = new File("nonexistent.mp3");
 try {
 new MP3(nonexistentFile.getAbsolutePath());
 fail("Expected FileNotFoundException");
 } catch (FileNotFoundException e) {
 // Expected
 }
 }

 // Test closing of AudioInputStream and SourceDataLine in finally block
 @Test
 public void testClosingAudioStreams() throws Exception {
 mp3 = new MP3(validFilename);
 Thread mp3Thread = new Thread(mp3);
 mp3Thread.start();
 mp3Thread.join();
 assertFalse(din.isOpen());
 assertFalse(line.isOpen());
 }

 // Test reading data from AudioInputStream
 @Test
 public void testReadingDataFromAudioInputStream() throws Exception {
 mp3 = new MP3(validFilename);
 Thread mp3Thread = new Thread(mp3);
 mp3Thread.start();
 mp3Thread.join();
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 byte[] data = new byte[4096];
 int bytesRead;
 while ((bytesRead = din.read(data, 0, data.length)) != -1) {
 out.write(data, 0, bytesRead);
 }
 byte[] expectedData = out.toByteArray();
 assertArrayEquals(expectedData, data);
 }
}
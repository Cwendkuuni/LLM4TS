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

 @Test
 public void testMP3ConstructorWithValidFilename() {
 // Testing that the constructor is called correctly with a valid filename
 assertNotNull(mp3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMP3ConstructorWithInvalidFilename() {
 // Testing that an IllegalArgumentException is thrown with an invalid filename
 mp3 = new MP3(invalidFilename);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testPlayingMP3File() throws Exception {
 // Testing that the MP3 file is played correctly
 assertTrue(mp3.isAlive());
 Thread.sleep(5000); // Give the thread some time to play the file
 mp3.join();
 }

 @Test(expected = IOException.class)
 public void testClosingAudioInputStreamWithException() throws Exception {
 // Testing that an IOException is thrown when there's an error while closing the AudioInputStream
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 PrintStream errStream = new PrintStream(out);
 System.setErr(errStream);
 try {
 mp3 = new MP3(validFilename);
 mp3.join();
 } finally {
 mp3.close();
 }
 String expectedErrorMessage = "java.io.IOException: I/O error: Permission denied";
 assertEquals(expectedErrorMessage, out.toString());
 }

 @Test
 public void testClosingAudioInputStreamWithoutException() throws Exception {
 // Testing that there are no exceptions when closing the AudioInputStream correctly
 mp3 = new MP3(validFilename);
 mp3.join();
 mp3.close();
 }

 @Test
 public void testNullAudioInputStream() {
 // Testing that no exceptions are thrown when an AudioInputStream is null
 mp3 = new MP3(null);
 }

 @Test
 public void testNullDataLineInfo() {
 // Testing that an IllegalArgumentException is thrown when DataLine.Info is null
 mp3 = new MP3(validFilename);
 DataLine.Info nullInfo = null;
 try {
 SourceDataLine line = (SourceDataLine) AudioSystem.getLine(nullInfo);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}
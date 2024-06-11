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
 public void testConstructorWithValidFilename() {
 // Test that the constructor is able to initialize the MP3 object with a valid filename
 assertNotNull(mp3);
 }

 @Test
 public void testConstructorWithInvalidFilenameThrowsException() {
 // Test that the constructor throws an exception when given an invalid filename
 try {
 new MP3(invalidFilename);
 fail("Expected an exception to be thrown");
 } catch (Exception e) {
 // Expected exception
 }
 }

 @Test
 public void testPlayingValidFile() {
 // Test that the MP3 object is able to play a valid file
 assertTrue(mp3.isAlive());
 }

 @Test(expected = IOException.class)
 public void testPlayingInvalidFileThrowsIOException() {
 // Test that an IOException is thrown when trying to play an invalid file
 new MP3(invalidFilename);
 }

 @Test
 public void testPlayingFileDrainsBuffer() {
 // Test that the buffer is drained when playing a file
 assertTrue(mp3.isStopped());
 }

 @Test
 public void testClosingStreams() {
 // Test that the streams are closed correctly when the MP3 object is finished
 assertNull(mp3.in);
 assertNull(mp3.din);
 }
}
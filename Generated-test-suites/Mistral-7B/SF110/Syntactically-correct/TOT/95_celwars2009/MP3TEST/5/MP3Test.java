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
 public void testMP3ConstructionWithValidFilename() {
 // Test construction with a valid filename
 assertTrue(mp3.isAlive()); // Check if thread is started
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMP3ConstructionWithInvalidFilename() {
 // Test construction with an invalid filename
 mp3 = new MP3(invalidFilename);
 assertFalse(mp3.isAlive()); // Check if thread was not started due to error
 }

 @Test
 public void testPlayingValidFile() {
 // Test playing a valid file
 mp3 = new MP3(validFilename);
 mp3.join(); // Wait for thread to finish
 }

 @Test(expected = IOException.class)
 public void testReadingInvalidFile() {
 // Test reading an invalid file
 mp3 = new MP3(invalidFilename);
 assertFalse(mp3.isAlive()); // Check if thread was not started due to error
 }

 @Test
 public void testClosingResources() {
 // Test closing resources
 try {
 mp3 = new MP3(validFilename);
 mp3.join();
 } catch (InterruptedException e) {
 e.printStackTrace();
 } finally {
 assertNull(mp3.in); // Check if input stream was closed
 assertNull(mp3.din); // Check if decoded input stream was closed
 }
 }

 @Test
 public void testPlayingMultipleInstances() {
 // Test playing multiple instances of MP3
 for (int i = 0; i < 10; i++) {
 new MP3(validFilename);
 }
 }
}
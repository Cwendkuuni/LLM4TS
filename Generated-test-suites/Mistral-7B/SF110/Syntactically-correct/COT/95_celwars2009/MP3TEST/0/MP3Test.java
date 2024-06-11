import org.junit.Test;
import static org.junit.Assert.*;
import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

 private static final String VALID_MP3_FILE = "test.mp3";
 private static final String NON_EXISTENT_FILE = "non_existent.mp3";

 @Test
 public void testMP3ConstructionWithValidFile() {
     MP3 mp3 = new MP3(VALID_MP3_FILE);
     assertNotNull(mp3.in);
     assertNotNull(mp3.din);
     assertNotNull(mp3.filename);
     assertFalse(mp3.isInterrupted());
 }

 @Test(expected = NullPointerException.class)
 public void testMP3ConstructionWithNullFile() {
     new MP3(null);
 }

 @Test(expected = FileNotFoundException.class)
 public void testMP3ConstructionWithNonExistentFile() {
     new MP3(NON_EXISTENT_FILE);
 }

 @Test
 public void testMP3RunWithValidFile() {
     MP3 mp3 = new MP3(VALID_MP3_FILE);
     mp3.join();
 }

 @Test(expected = InterruptedException.class)
 public void testMP3Interruption() {
     MP3 mp3 = new MP3(VALID_MP3_FILE);
     mp3.interrupt();
 }

 @Test
 public void testMP3RunWithNonExistentFile() {
     MP3 mp3 = new MP3(NON_EXISTENT_FILE);
     try {
         mp3.join();
     } catch (InterruptedException e) {
         // Handle InterruptedException
     } catch (Exception e) {
         fail("Exception should not be thrown: " + e.getMessage());
     }
 }

 @Test
 public void testMP3DisposesResources() {
     MP3 mp3 = new MP3(VALID_MP3_FILE);
     mp3.join();
     assertNull(mp3.in);
     assertNull(mp3.din);
 }

 @Test(expected = UnsupportedAudioFileException.class)
 public void testMP3UnsupportedFileType() {
     new MP3("test.wav");
 }

 @Test
 public void testMP3ThreadInterruption() {
     MP3 mp3 = new MP3(VALID_MP3_FILE);
     mp3.interrupt();
     try {
         mp3.join(500);
     } catch (InterruptedException e) {
         // Handle InterruptedException
     } catch (Exception e) {
         fail("Exception should not be thrown: " + e.getMessage());
     }
     assertTrue(mp3.isInterrupted());
 }

 @Test
 public void testMP3ThreadException() {
     MP3 mp3 = new MP3(VALID_MP3_FILE);
     try {
         mp3.join();
     } catch (InterruptedException e) {
         // Handle InterruptedException
     } catch (Exception e) {
         assertTrue(e instanceof IOException);
     }
 }
}
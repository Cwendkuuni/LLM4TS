import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.sound.sampled.*;
import java.io.*;
import static org.junit.Assert.*;

public class MP3Test {

 private String validFile;
 private String invalidFile;
 private MP3 mp3;

 @Before
 public void setUp() {
 validFile = "test.mp3";
 invalidFile = "nonexistent.mp3";
 }

 @Test
 public void testMP3ConstructionWithValidFile() {
 try {
 mp3 = new MP3(validFile);
 assertTrue(mp3.isAlive());
 } catch (Exception e) {
 fail("Exception should not be thrown during construction with valid file");
 }
 }

 @Test(expected = NullPointerException.class)
 public void testMP3ConstructionWithNullFilename() {
 mp3 = new MP3(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMP3ConstructionWithEmptyFilename() {
 mp3 = new MP3("");
 }

 @Test(expected = FileNotFoundException.class)
 public void testMP3ConstructionWithInvalidFile() {
 try {
 mp3 = new MP3(invalidFile);
 fail("Exception should be thrown during construction with invalid file");
 } catch (FileNotFoundException ex) {
 // Expected
 }
 }

 @Test
 public void testPlayingValidFile() {
 try {
 mp3 = new MP3(validFile);
 mp3.join();
 } catch (InterruptedException e) {
 e.printStackTrace();
 fail("Interrupted during test");
 } catch (Exception ex) {
 fail("Exception should not be thrown during playback of valid file");
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testPlayingTwice() {
 try {
 mp3 = new MP3(validFile);
 mp3.join();
 mp3 = new MP3(validFile);
 fail("IllegalStateException should be thrown during second playback");
 } catch (Exception e) {
 }
 }

 @Test(expected = IOException.class)
 public void testClosingStreamtwice() {
 try {
 mp3 = new MP3(validFile);
 mp3.join();
 } catch (Exception e) {
 }
 try {
 mp3.din.close();
 } catch (IOException ex) {
 fail("IOException should not be thrown during first close");
 }
 try {
 mp3.din.close();
 } catch (IOException ex) {
 // Expected
 }
 }

 @After
 public void tearDown() {
 if (mp3 != null) {
 try {
 mp3.din.close();
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }

}
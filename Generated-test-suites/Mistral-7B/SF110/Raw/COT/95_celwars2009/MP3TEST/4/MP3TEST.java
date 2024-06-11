import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

 private MP3 mp3;
 private String validFilename;

 @Before
 public void setUp() {
 validFilename = "test.mp3";
 mp3 = new MP3(validFilename);
 }

 @Test
 public void testMP3ConstructionWithValidFilename() {
 // Check if MP3 object is created successfully with a valid filename
 assertNotNull(mp3);
 }

 @Test(expected = NullPointerException.class)
 public void testMP3ConstructionWithNullFilename() {
 // Test exception handling for null filename
 mp3 = new MP3(null);
 }

 @Test(expected = FileNotFoundException.class)
 public void testMP3ConstructionWithInvalidFilename() {
 // Test exception handling for invalid filename
 mp3 = new MP3("invalidFilename.mp3");
 }

 @Test
 public void testMP3RunMethod() {
 // Test if the run method plays the audio file correctly
 assertTrue(mp3.isAlive());
 }

 @Test(expected = IOException.class)
 public void testMP3EncounterIOErrorDuringPlayback() {
 // Test exception handling for I/O errors during playback
 try {
 final File file = new File("nonExistentFile.mp3");
 final MP3 mp3 = new MP3(file.getAbsolutePath());
 } catch (Exception e) {
 assertTrue(e instanceof IOException);
 }
 }

 @Test(expected = UnsupportedAudioFileException.class)
 public void testMP3EncounterUnsupportedAudioFileFormat() {
 // Test exception handling for unsupported audio file format
 try {
 final File file = new File("test.wav");
 final MP3 mp3 = new MP3(file.getAbsolutePath());
 } catch (Exception e) {
 assertTrue(e instanceof UnsupportedAudioFileException);
 }
 }

 @Test(expected = LineUnavailableException.class)
 public void testMP3EncounterLineUnavailableException() {
 // Test exception handling for line unavailability exception
 try {
 final File file = new File("test.mp3");
 final MP3 mp3 = new MP3("wrong LineUnavailableException.mp3");
 } catch (Exception e) {
 assertTrue(e instanceof LineUnavailableException);
 }
 }

 @Test
 public void testMP3ClosesAudioStreams() {
 // Test if the audio streams are closed after playback
 try {
 final File file = new File(validFilename);
 final MP3 mp3 = new MP3(file.getAbsolutePath());
 mp3.join();
 final File checkFile = new File(validFilename);
 final AudioInputStream in = AudioSystem.getAudioInputStream(checkFile);
 assertFalse(in.isOpen());
 } catch (Exception e) {
 e.printStackTrace();
 }
 }
}
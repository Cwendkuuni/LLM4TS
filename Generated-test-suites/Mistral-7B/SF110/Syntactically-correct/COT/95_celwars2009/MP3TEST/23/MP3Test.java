import org.junit.Test;
import static org.junit.Assert.*;
import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

 private String validFilename = "test.mp3";
 private String invalidFilename = "invalidFile.mp3";

 @Test
 public void testMP3ConstructorWithValidFilename() {
 MP3 mp3 = new MP3(validFilename);
 assertNotNull(mp3.getFilename());
 }

 @Test(expected = NullPointerException.class)
 public void testMP3ConstructorWithNullFilename() {
 new MP3(null);
 }

 @Test(expected = FileNotFoundException.class)
 public void testMP3ConstructorWithInvalidFilename() {
 new MP3(invalidFilename);
 }

 @Test
 public void testRunMethod() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
 MP3 mp3 = new MP3(validFilename);
 mp3.run();
 }

 @Test(expected = IOException.class)
 public void testRunMethodWithInvalidFile() {
 MP3 mp3 = new MP3(invalidFilename);
 mp3.run();
 }

 @Test(expected = UnsupportedAudioFileException.class)
 public void testRunMethodWithUnsupportedAudioFile() {
 MP3 mp3 = new MP3("unsupportedFile.wav");
 mp3.run();
 }

 @Test(expected = LineUnavailableException.class)
 public void testRunMethodWithLineUnavailableException() {
 MP3 mp3 = new MP3(validFilename);
 doReturn(null).when(mp3).getLine();
 mp3.run();
 }

 @Test
 public void testCloseDataLineInRunMethod() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
 MP3 mp3 = new MP3(validFilename);
 mp3.run();
 mp3.join(); // wait for thread to finish
 assertNull(mp3.getLine());
 }

 @Test
 public void testCloseInputStreamInRunMethod() throws IOException {
 MP3 mp3 = new MP3(validFilename);
 mp3.run();
 mp3.join(); // wait for thread to finish
 assertNull(mp3.getdin());
 }

 @Test
 public void testStartThread() {
 MP3 mp3 = new MP3(validFilename);
 assertFalse(mp3.isInterrupted());
 }

 @Test
 public void testInterruptThread() {
 MP3 mp3 = new MP3(validFilename);
 mp3.start();
 mp3.interrupt();
 assertTrue(mp3.isInterrupted());
 }

}
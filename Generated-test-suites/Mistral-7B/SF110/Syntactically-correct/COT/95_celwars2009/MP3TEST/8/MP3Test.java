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
 public void testMP3Constructor() {
 assertNotNull(mp3);
 assertEquals(validFilename, mp3.filename);
 }

 @Test
 public void testRunMethodWithValidFile() {
 // Check if the thread has started
 assertTrue(mp3.isAlive());

 // Check if the audio is played correctly
 try {
 Thread.sleep(5000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

 AudioFormat baseFormat = null;
 try {
 final File file = new File(mp3.filename);
 final AudioInputStream in = AudioSystem.getAudioInputStream(file);
 baseFormat = in.getFormat();
 } catch (IOException e) {
 e.printStackTrace();
 }

 AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);

 try (AudioInputStream din = AudioSystem.getAudioInputStream(decodedFormat, mp3.in)) {
 int nBytesRead;
 byte[] data = new byte[4096];
 while ((nBytesRead = din.read(data, 0, data.length)) != -1) {
 // Do some validation on the data if necessary
 }
 }

 mp3.join();
 }

 @Test(expected = FileNotFoundException.class)
 public void testRunMethodWithInvalidFile() {
 mp3 = new MP3(invalidFilename);
 mp3.start();
 try {
 mp3.join();
 fail("Expected FileNotFoundException but no exception was thrown.");
 } catch (FileNotFoundException e) {
 // Expected exception
 }
 }

 @Test(expected = IOException.class)
 public void testRunMethodWithIOException() {
 try {
 final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
 mp3 = new MP3(new File(""), inputStream);
 mp3.start();
 fail("Expected IOException but no exception was thrown.");
 } catch (IOException e) {
 // Expected exception
 }
 }

 @Test(expected = UnsupportedAudioFileException.class)
 public void testRunMethodWithUnsupportedFile() {
 try {
 final File file = new File("test.wav");
 mp3 = new MP3(file);
 mp3.start();
 fail("Expected UnsupportedAudioFileException but no exception was thrown.");
 } catch (UnsupportedAudioFileException e) {
 // Expected exception
 }
 }

 @Test
 public void testCloseResources() {
 try {
 mp3 = new MP3(validFilename);
 mp3.start();
 Thread.sleep(5000);
 mp3.join();
 } catch (Exception e) {
 e.printStackTrace();
 }

 assertNull(mp3.in);
 assertNull(mp3.din);
 }
}
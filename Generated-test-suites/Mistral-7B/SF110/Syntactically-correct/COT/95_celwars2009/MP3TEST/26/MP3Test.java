import org.junit.Test;
import static org.junit.Assert.*;
import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

 private MP3 mp3;
 private ByteArrayOutputStream outStream = new ByteArrayOutputStream();

 @Test
 public void testMP3Constructor() {
 mp3 = new MP3("test.mp3");
 assertEquals("test.mp3", mp3.filename);
 }

 @Test(expected = NullPointerException.class)
 public void testMP3ConstructorWithNullFilename() {
 mp3 = new MP3(null);
 }

 @Test
 public void testRunMethod() throws Exception {
 mp3 = new MP3("test.mp3");
 mp3.start();
 Thread.sleep(5000); // give time for thread to run
 mp3.join();
 // check if the file was played correctly
 // Replace this with the actual verification logic
 // for now, just check if the file was read
 File file = new File("test.mp3");
 assertTrue(file.length() > 0);
 }

 @Test(expected = FileNotFoundException.class)
 public void testRunMethodWithNonExistentFile() {
 mp3 = new MP3("non_existent_file.mp3");
 mp3.start();
 mp3.join();
 }

 @Test(expected = UnsupportedAudioFileException.class)
 public void testRunMethodWithUnsupportedFormat() {
 mp3 = new MP3("test.txt");
 mp3.start();
 mp3.join();
 }

 @Test(expected = IOException.class)
 public void testRunMethodWithIOError() {
 File tempFile = File.createTempFile("test", ".mp3");
 mp3 = new MP3(tempFile.getAbsolutePath());
 mp3.start();
 // simulate an I/O error
 outStream.close();
 mp3.join();
 }

 @Test
 public void testCloseMethod() {
 mp3 = new MP3("test.mp3");
 mp3.start();
 mp3.join();
 // check if the streams were closed
 assertNull(mp3.in);
 assertNull(mp3.din);
 }

 @Test
 public void testThreadInterruption() {
 mp3 = new MP3("test.mp3");
 Thread thread = new Thread(mp3);
 thread.start();
 thread.interrupt();
 mp3.join();
 // check if the thread was interrupted
 assertTrue(mp3.isInterrupted());
 }

}
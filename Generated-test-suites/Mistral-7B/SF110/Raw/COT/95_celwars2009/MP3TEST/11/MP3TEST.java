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

 // Test constructor
 @Test
 public void testMP3Constructor() {
 assertNotNull(mp3);
 assertEquals(validFilename, mp3.filename);
 }

 // Test run method - happy path
 @Test
 public void testRunHappyPath() throws Exception {
 Thread.sleep(2000); // give the thread some time to execute
 assertNotNull(mp3.in);
 assertNotNull(mp3.din);
 }

 // Test run method - exception handling
 @Test(expected = FileNotFoundException.class)
 public void testRunFileNotFoundException() {
 mp3 = new MP3(invalidFilename);
 }

 // Test run method - IOException
 @Test(expected = IOException.class)
 public void testRunIOException() {
 try {
 mp3 = new MP3(validFilename);
 AudioFormat baseFormat = mp3.in.getFormat();
 DataFormat property = new DataFormat();
 property.setType("application/mp3");
 AudioInputStream decodedStream = AudioSystem.getAudioInputStream(property, mp3.in);
 mp3.din = decodedStream;
 } catch (Exception e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 try {
 mp3.run();
 fail("Expected IOException");
 } catch (IOException ie) {
 // expected
 }
 }

 // Test run method - DataLineUnavailableException
 @Test(expected = LineUnavailableException.class)
 public void testRunDataLineUnavailableException() {
 try {
 mp3 = new MP3(validFilename);
 } catch (Exception e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 SourceDataLine line = (SourceDataLine)AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, mp3.din.getFormat()));
 try {
 line.open(mp3.din.getFormat());
 fail("Expected LineUnavailableException");
 } catch (LineUnavailableException lu) {
 // expected
 }
 }

 // Test run method - InterruptedException
 @Test
 public void testRunInterruptedException() {
 Thread interrupter = new Thread(() -> mp3.interrupt());
 interrupter.start();
 try {
 mp3.join();
 fail("Expected InterruptedException");
 } catch (InterruptedException ie) {
 // expected
 }
 }

 // Test close method - happy path
 @Test
 public void testCloseHappyPath() {
 try {
 mp3.close();
 } catch (IOException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 assertNull(mp3.in);
 assertNull(mp3.din);
 }

 // Test close method - already closed
 @Test(expected = IllegalStateException.class)
 public void testCloseAlreadyClosed() {
 try {
 mp3.close();
 } catch (IOException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 mp3.close();
 }
}
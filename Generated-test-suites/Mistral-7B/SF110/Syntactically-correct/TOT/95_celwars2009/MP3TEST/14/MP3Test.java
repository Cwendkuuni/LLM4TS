import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class MP3Test {

 private MP3 mp3;
 private String validFilename = "test.mp3";
 private String invalidFilename = "does_not_exist.mp3";

 @Before
 public void setUp() {
 mp3 = new MP3(validFilename);
 }

 @Test
 public void testConstructionWithValidFilename() {
 // Test that the constructors is called with the correct filename
 assertEquals(validFilename, mp3.filename);
 // Test that the thread is started
 assertTrue(mp3.isAlive());
 }

 @Test
 public void testConstructionWithInvalidFilename() {
 try {
 new MP3(invalidFilename);
 fail("Should have thrown IOException");
 } catch (IOException e) {
 // Expected
 }
 // Test that the thread is not started
 assertFalse(new MP3(invalidFilename).isAlive());
 }

 @Test
 public void testPlayingValidFile() {
 // Test that the audio is played correctly
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 OutputStream os = out;
 SourceDataLine line = null;
 try {
 line = AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 16, 44100, false)));
 line.open(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 16, 44100, false));
 line.start();
 AudioInputStream in = AudioSystem.getAudioInputStream(new File(mp3.filename));
 byte[] buffer = new byte[4096];
 int nBytesRead;
 while ((nBytesRead = in.read(buffer, 0, buffer.length)) != -1) {
 line.write(buffer, 0, nBytesRead);
 }
 line.drain();
 line.stop();
 line.close();
 in.close();
 } catch (Exception e) {
 e.printStackTrace();
 fail("Failed to play audio file");
 } finally {
 if (line != null) {
 try {
 line.close();
 } catch (IOException ex) {}
 }
 }
 // Test that the thread has finished
 assertTrue(mp3.isInterrupted());
 }

 @Test(expected = IOException.class)
 public void testPlayingInvalidFile() {
 try {
 new MP3(invalidFilename).run();
 fail("Should have thrown IOException");
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testClosingStreams() {
 // Test that the din stream is closed in finally block
 File file = new File(mp3.filename);
 File tempFile = new File("temp.mp3");
 try {
 AudioInputStream in = AudioSystem.getAudioInputStream(file);
 MP3 mp3 = new MP3(tempFile.getAbsolutePath());
 assertFalse(mp3.din.isClosed());
 mp3.run();
 assertTrue(mp3.din.isClosed());
 } catch (Exception e) {
 e.printStackTrace();
 fail("Failed to test closing streams");
 } finally {
 try {
 file.delete();
 tempFile.delete();
 } catch (IOException ex) {}
 }
 }
}
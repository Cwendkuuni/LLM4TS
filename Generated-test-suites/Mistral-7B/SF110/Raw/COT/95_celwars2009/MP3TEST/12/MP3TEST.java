import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

 private MP3 mp3;
 private String validFilename = "test.mp3";
 private String invalidFilename = "nonexistent.mp3";
 private byte[] data = new byte[4096];

 @Before
 public void setUp() {
 mp3 = new MP3(validFilename);
 }

 @Test
 public void testMP3Constructor() {
 // Test if the constructor initializes the filename correctly
 assertEquals(validFilename, mp3.filename);
 // Test if the thread starts correctly
 assertTrue(mp3.isAlive());
 }

 @Test
 public void testAudioInputStreamInitialization() {
 try {
 AudioInputStream in = mp3.in;
 AudioFormat baseFormat = in.getFormat();
 Assert.assertNotNull(in);
 Assert.assertNotNull(baseFormat);
 } catch (Exception e) {
 fail("Exception occurred during test: " + e.getMessage());
 }
 }

 @Test
 public void testAudioFormatConversion() throws UnsupportedAudioFileException, IOException {
 AudioFormat decodedFormat = mp3.getDecodedFormat();
 Assert.assertNotNull(decodedFormat);
 Assert.assertEquals(AudioFormat.Encoding.PCM_SIGNED, decodedFormat.getEncoding());
 Assert.assertEquals(16, decodedFormat.getSampleSizeInBits());
 }

 @Test
 public void testSourceDataLineInitialization() {
 SourceDataLine line = mp3.getLine();
 Assert.assertNotNull(line);
 }

 @Test
 public void testPlayingMP3() throws UnsupportedAudioFileException, IOException {
 try {
 int bytesRead;
 while ((bytesRead = mp3.read(data, 0, data.length)) != -1) {
 // Do nothing, just testing that data is being read from the MP3
 }
 mp3.stop();
 } catch (Exception e) {
 fail("Exception occurred during test: " + e.getMessage());
 }
 }

 @Test(expected = FileNotFoundException.class)
 public void testInvalidFilename() {
 new MP3(invalidFilename);
 }

 @Test(expected = IOException.class)
 public void testAudioInputStreamClosingException() throws Exception {
 AudioInputStream din = mp3.din;
 try {
 din.close();
 fail("Should have thrown an IOException when closing an already closed AudioInputStream");
 } catch (IOException ex) {}
 }
}
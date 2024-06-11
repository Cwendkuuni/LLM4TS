import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

 private MP3 mp3;
 private ByteArrayOutputStream outputStream;
 private ByteArrayInputStream inputStream;

 @Before
 public void setUp() {
 String filename = "test.mp3";
 mp3 = new MP3(filename);
 outputStream = new ByteArrayOutputStream();
 }

 @After
 public void tearDown() throws Exception {
 mp3.join(); // wait for thread to finish
 inputStream.close();
 outputStream.close();
 }

 @Test
 public void testConstructor() {
 assertNotNull(mp3);
 assertEquals("test.mp3", mp3.filename);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullFilename() {
 mp3 = new MP3(null);
 fail("Expected NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithEmptyFilename() {
 mp3 = new MP3("");
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testRun() throws Exception {
 AudioFormat baseFormat = new AudioFormat(AudioFileFormat.Type.WAVE, 8000, 1, 16, 1, 8000, false);
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 DataLine.Info info = new DataLine.Info(TargetDataLine.class, baseFormat);
 TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
 line.open(baseFormat);
 line.start();

 AudioInputStream testInput = new FileTypeFormat(new File("test.mp3")).getAudioInputStream();
 AudioFormat format = testInput.getFormat();
 Assert.assertEquals(format.getSampleRate(), 8000);
 Assert.assertEquals(format.getChannels(), 1);
 Assert.assertEquals(format.getSampleSizeInBits(), 16);

 mp3.join();
 outputStream.reset();
 mp3.run();
 byte[] data = outputStream.toByteArray();
 Assert.assertArrayEquals(data, output.toByteArray());
 line.close();
 }

 @Test(expected = IOException.class)
 public void testRunWithIOException() throws Exception {
 outputStream = new ByteArrayOutputStream();
 mp3 = new MP3("nonexistentfile.mp3");
 mp3.join(); // wait for thread to finish
 fail("Expected IOException");
 }

 @Test(expected = UnsupportedAudioFileException.class)
 public void testRunWithUnsupportedFormat() throws Exception {
 outputStream = new ByteArrayOutputStream();
 File testFile = new File("test.txt");
 mp3 = new MP3(testFile.getAbsolutePath());
 mp3.join(); // wait for thread to finish
 fail("Expected UnsupportedAudioFileException");
 }

 @Test(expected = LineUnavailableException.class)
 public void testRunWithLineUnavailableException() throws Exception {
 outputStream = new ByteArrayOutputStream();
 mp3 = new MP3("test.mp3");
 mp3.join(); // wait for thread to finish
 fail("Expected LineUnavailableException");
 }
}
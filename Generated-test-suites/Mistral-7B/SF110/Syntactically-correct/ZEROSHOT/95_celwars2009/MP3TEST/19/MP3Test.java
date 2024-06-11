import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

 private MP3 mp3;
 private byte[] data;
 private ByteArrayOutputStream outStream;
 private ByteArrayInputStream inStream;

 @Before
 public void setUp() {
 String fileName = "test.mp3"; // Replace with the actual test file name
 mp3 = new MP3(fileName);
 data = new byte[4096];
 outStream = new ByteArrayOutputStream();
 inStream = new ByteArrayInputStream(new byte[0]);
 }

 @After
 public void tearDown() throws Exception {
 if (mp3 != null) {
 mp3.join();
 if (mp3.in != null) {
 try {
 mp3.in.close();
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 if (mp3.din != null) {
 try {
 mp3.din.close();
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }
 }

 @Test
 public void testMP3Constructor() {
 assertNotNull(mp3);
 assertEquals("test.mp3", mp3.filename);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMP3ConstructorNullFilename() {
 mp3 = new MP3(null);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = FileNotFoundException.class)
 public void testMP3ConstructorInvalidFile() {
 mp3 = new MP3("invalidFile.mp3");
 fail("FileNotFoundException expected but not thrown");
 }

 @Test
 public void testRunMethod() throws Exception {
 mp3.run();
 Thread.sleep(1000); // Allow some time for the thread to finish
 assertTrue(mp3.isInterrupted());
}

 @Test
 public void testReadData() throws IOException, UnsupportedAudioFileException {
 mp3.run();
 int nBytesRead;
 while ((nBytesRead = mp3.din.read(data, 0, data.length)) != -1) {
 outStream.write(data, 0, nBytesRead);
 }
 AudioFileFormat expextedFormat = AudioFileFormat.getType("test.mp3");
 assertNotNull(expextedFormat);
 Assert.assertArrayEquals(outStream.toByteArray(), inStream.readAllBytes());
 }

 @Test(expected = IOException.class)
 public void testReadDataIOException() throws Exception {
 mp3.run();
 try {
 while ((mp3.din.read(data, 0, data.length)) != -1) {
 outStream.write(data, 0, data.length);
 }
 fail("IOException expected but not thrown");
 } catch (IOException e) {
 // Expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testLineNull() throws Exception {
 DataLine.Info info = new DataLine.Info(null, mp3.decodedFormat);
 SourceDataLine line = (SourceDataLine)AudioSystem.getLine(info);
 fail("NullPointerException expected but not thrown");
 }
}
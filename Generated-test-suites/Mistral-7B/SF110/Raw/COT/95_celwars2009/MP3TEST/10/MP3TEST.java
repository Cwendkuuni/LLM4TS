import org.junit.After;
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

 @After
 public void tearDown() {
 try {
 if (mp3 != null) {
 mp3.stop();
 mp3.join();
 }
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testMP3ConstructionWithValidFilename() {
 assertNotNull(mp3);
 }

 @Test(expected = NullPointerException.class)
 public void testMP3ConstructionWithNullFilename() {
 new MP3(null);
 }

 @Test(expected = FileNotFoundException.class)
 public void testMP3ConstructionWithInvalidFilename() {
 new MP3(invalidFilename);
 }

 @Test
 public void testThreadStart() {
 assertTrue(mp3.isAlive());
 }

 @Test
 public void testThreadJoin() throws InterruptedException {
 mp3.join();
 assertFalse(mp3.isAlive());
 }

 @Test(expected = IOException.class)
 public void testReadDataFromInputStreamThrowsIOException() {
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 OutputStream os = outStream;
 ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
 try {
 mp3.run();
 } finally {
 mp3.setIn(inputStream);
 mp3.run();
 }
 }

 @Test
 public void testPlayMP3File() throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
 assertNotNull(mp3.getIn());
 AudioFormat baseFormat = mp3.getIn().getFormat();
 assertEquals(AudioFileType.MP3, AudioFileType.typeForName(baseFormat.getMimeType()));
 AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
 DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
 SourceDataLine line = (SourceDataLine)AudioSystem.getLine(info);
 line.open(decodedFormat);
 byte[] data = new byte[4096];
 int read;
 while ((read = mp3.getIn().read(data, 0, data.length)) != -1) {
 line.write(data, 0, read);
 }
 line.drain();
 line.stop();
 line.close();
 }

 @Test(expected = NullPointerException.class)
 public void testGetInReturnsNull() {
 assertNull(mp3.getIn());
 }

 @Test
 public void testGetDinReturnsNull() {
 assertNull(mp3.getDin());
 }

 @Test(expected = IllegalThreadStateException.class)
 public void testPlayMP3FileWhileThreadIsStopped() throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
 mp3.stop();
 mp3.join();
 assertNotNull(mp3.getIn());
 }

}
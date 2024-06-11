import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

 private MP3 mp3;
 private ByteArrayOutputStream outputStream;

 @Before
 public void setUp() {
 String filename = "test.mp3"; // replace with the actual test MP3 file
 mp3 = new MP3(filename);
 outputStream = new ByteArrayOutputStream();
 }

 @After
 public void tearDown() throws Exception {
 mp3.join(); // wait for thread to finish
 if (mp3.in != null) {
 mp3.in.close();
 }
 if (mp3.din != null) {
 mp3.din.close();
 }
 }

 @Test
 public void testConstructor() {
 assertNotNull(mp3);
 assertEquals(filename, mp3.filename);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorNullFilename() {
 mp3 = new MP3(null);
 fail("Expected NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorEmptyFilename() {
 mp3 = new MP3("");
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IOException.class)
 public void testReadAudioFile() throws Exception {
 AudioInputStream in = AudioSystem.getAudioInputStream(new File(mp3.filename));
 assertEquals(mp3.in.getFormat().getEncoding(), in.getFormat().getEncoding());
 assertEquals(mp3.in.getFormat().isBigEndian(), in.getFormat().isBigEndian());
 assertEquals(mp3.in.getFormat().getSampleRate(), in.getFormat().getSampleRate());
 assertEquals(mp3.in.getFormat().getChannels(), in.getFormat().getChannels());
 }

 @Test(expected = UnsupportedAudioFileException.class)
 public void testUnsupportedAudioFile() throws Exception {
 mp3.filename = "test.wav";
 mp3 = new MP3(mp3.filename);
 fail("Expected UnsupportedAudioFileException");
 }

 @Test
 public void testDecoding() throws Exception {
 DataLine.Info info = new DataLine.Info(SourceDataLine.class, mp3.in.getFormat());
 SourceDataLine line = (SourceDataLine)AudioSystem.getLine(info);
 line.open(mp3.in.getFormat());
 line.start();
 byte[] data = new byte[4096];
 int nBytesRead;
 while ((nBytesRead = mp3.in.read(data, 0, data.length)) != -1) {
 line.write(data, 0, nBytesRead);
 }
 line.drain();
 line.stop();
 line.close();
 mp3.in.close();

 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 AudioInputStream decodedStream = AudioSystem.getAudioInputStream(inputStream);
 AudioFormat decodedFormat = decodedStream.getFormat();
 AudioFormat baseFormat = mp3.in.getFormat();
 assertEquals(baseFormat.getEncoding(), decodedFormat.getEncoding());
 assertEquals(baseFormat.isBigEndian(), decodedFormat.isBigEndian());
 assertEquals(baseFormat.getSampleRate(), decodedFormat.getSampleRate());
 assertEquals(baseFormat.getChannels(), decodedFormat.getChannels());
 }

 @Test
 public void testPlaying() throws Exception {
 SourceDataLine line = null;
 try {
 line = (SourceDataLine)AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, mp3.in.getFormat()));
 line.open(mp3.in.getFormat());
 line.start();

 int nBytesRead;
 byte[] data = new byte[4096];
 while ((nBytesRead = mp3.in.read(data, 0, data.length)) != -1) {
 line.write(data, 0, nBytesRead);
 }
 line.drain();
 line.stop();
 line.close();
 } finally {
 if (line != null) {
 line.close();
 }
 }

 mp3.join();
 }

 @Test
 public void testClosing() {
 mp3.join();
 assertNull(mp3.in);
 assertNull(mp3.din);
 }
}
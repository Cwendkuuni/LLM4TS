import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.sound.sampled.*;

public class MP3Test {

 private MP3 mp3;
 private ByteArrayOutputStream outStream;
 private ByteArrayInputStream inStream;

 @Before
 public void setUp() {
 String testFileName = "test.mp3";
 mp3 = new MP3(testFileName);
 outStream = new ByteArrayOutputStream();
 try {
 AudioSystem.write(AudioFileFormat.getType(testFileName), AudioSystem.read(new File(testFileName).getAbsoluteFile()), outStream);
 inStream = new ByteArrayInputStream(outStream.toByteArray());
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @After
 public void tearDown() {
 try {
 if (mp3.din != null) {
 mp3.din.close();
 }
 if (mp3.in != null) {
 mp3.in.close();
 }
 if (outStream != null) {
 outStream.close();
 }
 if (inStream != null) {
 inStream.close();
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testMP3Constructor() {
 assertNotNull(mp3);
 assertEquals(testFileName, mp3.filename);
 }

 @Test
 public void testThreadStart() throws InterruptedException {
 Thread.State state = mp3.getState();
 assertTrue(state == Thread.State.RUNNABLE || state == Thread.State.RUNNING);
 }

 @Test(expected = NullPointerException.class)
 public void testNullFilename() {
 new MP3(null);
 fail("Expected NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyFilename() {
 new MP3("");
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IOException.class)
 public void testFileNotFound() {
 String notFoundFileName = "nonexistentfile.mp3";
 new MP3(notFoundFileName);
 fail("Expected IOException");
 }

 @Test
 public void testAudioInputStreamGetAudioInputStream() throws IOException {
 AudioInputStream ais1 = AudioSystem.getAudioInputStream(new File(testFileName));
 AudioFormat baseFormat = ais1.getFormat();
 assertEquals(baseFormat.getSampleRate(), mp3.baseFormat.getSampleRate());
 assertEquals(baseFormat.getChannels(), mp3.baseFormat.getChannels());
 }

 @Test
 public void testAudioFormatDecodedFormat() {
 assertEquals(AudioFormat.Encoding.PCM_SIGNED, mp3.decodedFormat.getEncoding());
 assertEquals(mp3.baseFormat.getSampleRate(), mp3.decodedFormat.getSampleRate());
 assertEquals(1, mp3.decodedFormat.getChannels());
 assertEquals(mp3.baseFormat.getChannels() * 2, mp3.decodedFormat.getSampleSizeInBits());
 }

 @Test
 public void testDataLineInfo() throws LineUnavailableException, UnsupportedAudioFileException {
 DataLine.Info info = new DataLine.Info(SourceDataLine.class, mp3.decodedFormat);
 assertNotNull(info.getLineInfo());
 }

 @Test(expected = LineUnavailableException.class)
 public void testDataLineInfoUnavailable() throws UnsupportedAudioFileException {
 new DataLine.Info(SourceDataLine.class, mp3.decodedFormat);
 fail("Expected LineUnavailableException");
 }

 @Test(expected = UnsupportedAudioFileException.class)
 public void testUnsupportedAudioFileFormat() {
 new DataLine.Info(SourceDataLine.class, new AudioFormat(AudioFormat.Encoding.MP3, 1, 1, 1, 1, 1, false));
 fail("Expected UnsupportedAudioFileException");
 }

 @Test
 public void testSourceDataLineOpen() throws LineUnavailableException {
 SourceDataLine line = (SourceDataLine) AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, mp3.decodedFormat));
 line.open(mp3.decodedFormat);
 }

 @Test(expected = LineUnavailableException.class)
 public void testSourceDataLineUnavailable() throws UnsupportedAudioFileException {
 new DataLine.Info(SourceDataLine.class, new AudioFormat(AudioFormat.Encoding.MP3, 1, 1, 1, 1, 1, false));
 fail("Expected LineUnavailableException");
 }

 @Test
 public void testSourceDataLineWrite() throws IOException {
 SourceDataLine line = (SourceDataLine) AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, mp3.decodedFormat));
 line.open(mp3.decodedFormat);
 byte[] data = new byte[4096];
 int nBytesRead;
 while ((nBytesRead = inStream.read(data)) != -1) {
 line.write(data, 0, nBytesRead);
 }
 line.drain();
 line.stop();
 line.close();
 }

 @Test
 public void testSourceDataLineDrain() throws IOException {
 SourceDataLine line = (SourceDataLine) AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, mp3.decodedFormat));
 line.open(mp3.decodedFormat);
 line.start();
 int nBytesRead;
 while ((nBytesRead = inStream.read(new byte[4096])) != -1) {
 line.write(new byte[4096], 0, nBytesRead);
 }
 assertEquals(inStream.available(), 0);
 line.drain();
 line.stop();
 line.close();
 }

 @Test
 public void testSourceDataLineClose() throws IOException {
 SourceDataLine line = (SourceDataLine) AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, mp3.decodedFormat));
 line.open(mp3.decodedFormat);
 line.start();
 int nBytesRead;
 while ((nBytesRead = inStream.read(new byte[4096])) != -1) {
 line.write(new byte[4096], 0, nBytesRead);
 }
 line.drain();
 line.stop();
 line.close();
 }
}
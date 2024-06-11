import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MP3Test {

 private MP3 mp3;
 private AudioInputStream in;
 private AudioInputStream decodedInputStream;
 private SourceDataLine line;
 private AudioFormat baseFormat;
 private AudioFormat decodedFormat;

 @Before
 public void setUp() throws Exception {
 String filename = "test.mp3"; // Replace with the actual test file
 mp3 = new MP3(filename);
 in = AudioSystem.getAudioInputStream(new File(filename));
 baseFormat = in.getFormat();
 decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
 }

 @After
 public void tearDown() throws Exception {
 if (line != null) {
 line.close();
 }
 if (decodedInputStream != null) {
 decodedInputStream.close();
 }
 if (in != null) {
 in.close();
 }
 if (mp3 != null) {
 mp3.interrupt();
 }
 }

 @Test
 public void testConstructor() {
 assertNotNull(mp3);
 assertNotNull(mp3.getFilename());
 assertEquals(filename, mp3.getFilename());
 }

 @Test
 public void testRun() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
 decodedInputStream = AudioSystem.getAudioInputStream(decodedFormat, in);
 line = (SourceDataLine) AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, decodedFormat));
 line.open(decodedFormat);
 int nBytesRead;
 byte[] data = new byte[4096];
 line.start();
 while ((nBytesRead = decodedInputStream.read(data, 0, data.length)) != -1) {
 line.write(data, 0, nBytesRead);
 }
 line.drain();
 line.stop();
 line.close();
 decodedInputStream.close();
 in.close();
 mp3.join();
 assertTrue(mp3.isInterrupted());
 }

 @Test(expected = NullPointerException.class)
 public void testRunWithNullFilename() {
 new MP3(null);
 }

 @Test(expected = IOException.class)
 public void testRunWithInvalidFile() {
 new MP3("invalid_file.mp3");
 }

 @Test(expected = UnsupportedAudioFileException.class)
 public void testRunWithUnsupportedFile() {
 new MP3("unsupported_file.wav");
 }

 @Test(expected = LineUnavailableException.class)
 public void testRunWithLineUnavailable() {
 line = null;
 new MP3(filename);
 }
}
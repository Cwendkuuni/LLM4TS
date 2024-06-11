import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

 private MP3 mp3;
 private String fileName;

 @Before
 public void setUp() {
 fileName = "test.mp3";
 mp3 = new MP3(fileName);
 }

 // Test constructor
 @Test
 public void testMP3Constructor() {
 assertNotNull(mp3);
 assertEquals(fileName, mp3.filename);
 }

 // Test run method with valid file
 @Test
 public void testRunWithValidFile() throws Exception {
 AudioInputStream expectedIn = AudioSystem.getAudioInputStream(new File(fileName));
 AudioFormat baseFormat = expectedIn.getFormat();
 AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);

 ByteArrayOutputStream out = new ByteArrayOutputStream();
 TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(new DataLine.Info(TargetDataLine.class, decodedFormat));
 targetLine.open(decodedFormat);
 targetLine.start();

 byte[] buffer = new byte[1024];
 int numBytesRead = 0;
 int numBytesWritten = 0;

 while ((numBytesRead = mp3.in.read(buffer)) != -1) {
 numBytesWritten = targetLine.write(buffer, 0, numBytesRead);
 }

 targetLine.drain();
 targetLine.stop();
 targetLine.close();

 byte[] actualData = out.toByteArray();
 byte[] expectedData = readFileToByteArray(fileName);
 assertArrayEquals(expectedData, actualData);

 mp3.join();
 }

 // Test run method with invalid file
 @Test(expected = FileNotFoundException.class)
 public void testRunWithInvalidFile() {
 String invalidFileName = "nonexistentfile.mp3";
 mp3 = new MP3(invalidFileName);
 mp3.run();
 }

 // Test run method with IOException
 @Test(expected = IOException.class)
 public void testRunWithIOException() throws Exception {
 File tempFile = File.createTempFile("test", "mp3");
 mp3 = new MP3(tempFile.getAbsolutePath());
 AudioFormat baseFormat = new AudioFormat(16000, 16, 1, 4, 4096, 16000, false);
 AudioInputStream in = new AudioInputStream(new FileInputStream(tempFile), baseFormat, tempFile.length());
 mp3.in = in;
 try {
 mp3.run();
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 tempFile.delete();
 }

 // Test run method with AudioFormat incompatibility
 @Test(expected = UnsupportedAudioFileException.class)
 public void testRunWithIncompatibleFormat() throws Exception {
 File tempFile = File.createTempFile("test", "wav");
 AudioInputStream in = new AudioInputStream(new FileInputStream(tempFile), new AudioFormat(16000, 8, 1, 1, 4096, 16000, false), tempFile.length());
 mp3 = new MP3(tempFile.getAbsolutePath());
 mp3.in = in;
 assertThrows(UnsupportedAudioFileException.class, () -> mp3.run());
 tempFile.delete();
 }

 // Test run method with DataLine.Info unavailability
 @Test(expected = UnsupportedAudioFileException.class)
 public void testRunWithDataLineInfoUnavailability() throws Exception {
 File tempFile = File.createTempFile("test", "wav");
 AudioInputStream in = new AudioInputStream(new FileInputStream(tempFile), new AudioFormat(16000, 1, 1, 1, 4096, 16000, false), tempFile.length());
 mp3 = new MP3(tempFile.getAbsolutePath());
 mp3.in = in;
 DataLine.Info info = new DataLine.Info(SourceDataLine.class, mp3.getDecodedFormat());
 SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
 assertFalse(line != null);
 mp3.run();
 tempFile.delete();
 }

 // Test run method with DataLine.Info availability but unsupported format
 @Test(expected = UnsupportedAudioFileException.class)
 public void testRunWithUnsupportedDataLineFormat() throws Exception {
 File tempFile = File.createTempFile("test", "wav");
 AudioInputStream in = new AudioInputStream(new FileInputStream(tempFile), new AudioFormat(16000, 1, 1, 2, 4096, 16000, false), tempFile.length());
 mp3 = new MP3(tempFile.getAbsolutePath());
 mp3.in = in;
 DataLine.Info info = new DataLine.Info(SourceDataLine.class, mp3.getDecodedFormat());
 SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
 assertFalse(line != null);
 assertThrows(UnsupportedAudioFileException.class, () -> mp3.run());
 tempFile.delete();
 }

 // Test run method with DataLine.Info availability and supported format
 @Test
 public void testRunWithSupportedDataLineFormat() throws Exception {
 File tempFile = File.createTempFile("test", "wav");
 AudioInputStream in = new AudioInputStream(new FileInputStream(tempFile), new AudioFormat(16000, 1, 1, 1, 4096, 16000, false), tempFile.length());
 mp3 = new MP3(tempFile.getAbsolutePath());
 mp3.in = in;
 DataLine.Info info = new DataLine.Info(SourceDataLine.class, mp3.getDecodedFormat());
 SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
 line.open(mp3.getDecodedFormat());
 line.start();
 byte[] buffer = new byte[1024];
 int numBytesRead = 0;
 while ((numBytesRead = mp3.in.read(buffer)) != -1) {
 line.write(buffer, 0, numBytesRead);
 }
 line.drain();
 line.stop();
 line.close();
 mp3.join();
 tempFile.delete();
 }

 // Test close method with valid file
 @Test
 public void testCloseWithValidFile() throws Exception {
 closeFile(fileName);
 }

 // Test close method with invalid file
 @Test(expected = FileNotFoundException.class)
 public void testCloseWithInvalidFile() {
 closeFile("nonexistentfile.mp3");
 }

 private void closeFile(String fileName) {
 if (mp3 == null || fileName == null || !fileName.equals(mp3.filename)) return;
 try {
 if (mp3.din != null) mp3.din.close();
 } catch (IOException ex) {
 // ignore
 }
 mp3 = null;
 }

 private byte[] readFileToByteArray(String fileName) throws IOException {
 File file = new File(fileName);
 FileInputStream stream = new FileInputStream(file);
 byte[] bytes = new byte[(int) file.length()];
 stream.read(bytes);
 stream.close();
 return bytes;
 }
}
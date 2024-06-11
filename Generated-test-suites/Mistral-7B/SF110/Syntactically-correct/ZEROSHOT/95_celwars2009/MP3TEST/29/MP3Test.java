import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

 private MP3 mp3;
 private String testFile;

 @Before
 public void setUp() {
 testFile = "test.mp3";
 mp3 = new MP3(testFile);
 }

 @Test
 public void testConstructor() {
 assertNotNull(mp3);
 assertEquals(testFile, mp3.filename);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullFilename() {
 mp3 = new MP3(null);
 fail("Expected NullPointerException");
 }

 @Test
 public void testRun() throws Exception {
 AudioInputStream expectedIn = AudioSystem.getAudioInputStream(new File(testFile));
 AudioFormat baseFormat = expectedIn.getFormat();
 AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);

 assertNotNull(mp3.in);
 assertSame(baseFormat, mp3.in.getFormat());
 assertNull(mp3.din);

 mp3.run();
 assertNotNull(mp3.din);
 assertSame(decodedFormat, mp3.din.getFormat());
 }

 @Test(expected = IOException.class)
 public void testRunWithInvalidFile() {
 File invalidFile = new File("nonExistentFile.txt");
 mp3 = new MP3(invalidFile.getPath());
 fail("Expected IOException");
 }

 @Test
 public void testPlayMusic() throws Exception {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.WAVE, 44100, 16, 1, 2, 44100, false);
 TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(new DataLine.Info(TargetDataLine.class, targetFormat));
 targetLine.open(targetFormat);

 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 DataInputStream dis = new DataInputStream(inputStream);
 BufferedInputStream bis = new BufferedInputStream(dis);

 AudioInputStream testInput = AudioSystem.getAudioInputStream(bis);
 assertNotNull(testInput);

 mp3.run();

 DataLine.Info info = new DataLine.Info(SourceDataLine.class, mp3.din.getFormat());
 SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
 line.open(mp3.din.getFormat());

 int nBytesRead;
 byte[] data = new byte[4096];
 while ((nBytesRead = mp3.din.read(data, 0, data.length)) != -1) {
 line.write(data, 0, nBytesRead);
 }
 line.drain();
 line.stop();
 line.close();

 mp3.din.close();
 bis.close();
 dis.close();

 File tempFile = File.createTempFile("test", ".wav");
 FileOutputStream fos = new FileOutputStream(tempFile);
 DataOutputStream dos = new DataOutputSteam(fos);

 int bytesRead = 0;
 while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
 dos.write(data, 0, bytesRead);
 }
 dos.close();
 fos.close();

 File expectedFile = new File("expectedFile.wav");
 FileAssert.fileComparison(expectedFile, tempFile);
 }

 @Test(expected = IllegalStateException.class)
 public void testRunTwice() {
 mp3.run();
 mp3.run();
 fail("Expected IllegalStateException");
 }

 @Test
 public void testClose() {
 mp3.run();
 mp3.close();
 assertNull(mp3.din);
 }
}
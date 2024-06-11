import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

 private MP3 mp3;
 private String testFilename;

 @Before
 public void setUp() {
 testFilename = "test.mp3";
 mp3 = new MP3(testFilename);
 }

 @After
 public void tearDown() {
 try {
 if (mp3.getIn() != null) {
 mp3.getIn().close();
 }
 if (mp3.getDin() != null) {
 mp3.getDin().close();
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testMP3Construction() {
 assertNotNull(mp3);
 assertEquals(testFilename, mp3.getFilename());
 }

 @Test(expected = NullPointerException.class)
 public void testMP3ConstructionWithNullFilename() {
 new MP3(null);
 }

 @Test
 public void testOpenStream() {
 assertNotNull(mp3.getIn());
 }

 @Test(expected = IOException.class)
 public void testOpenStreamWithInvalidFile() {
 new MP3("invalidFile.mp3");
 }

 @Test
 public void testDecodingFormat() {
 AudioFormat baseFormat = mp3.getIn().getFormat();
 AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
 assertEquals(decodedFormat, mp3.getDin().getFormat());
 }

 @Test
 public void testDataLine() {
 DataLine.Info info = new DataLine.Info(SourceDataLine.class, mp3.getDin().getFormat());
 SourceDataLine line = (SourceDataLine)AudioSystem.getLine(info);
 assertNotNull(line);
 }

 @Test
 public void testPlaying() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 AudioFormat format = mp3.getDin().getFormat();
 SourceDataLine line = (SourceDataLine)AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, format));
 ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());
 line.open(format);
 line.start();
 byte[] buffer = new byte[4096];
 int bytesRead;
 while ((bytesRead = inputStream.read(buffer)) != -1) {
 line.write(buffer, 0, bytesRead);
 }
 line.drain();
 line.stop();
 line.close();
 inputStream.close();
 assertTrue(out.size() > 0);
 }
}
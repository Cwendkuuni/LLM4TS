import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MP3Test {

 private MP3 mp3;
 private String validFilename = "test.mp3";
 private String invalidFilename = "test.txt";

 @Before
 public void setUp() {
 mp3 = new MP3(validFilename);
 }

 @Test
 public void testConstructor() {
 assertNotNull(mp3);
 assertEquals(validFilename, mp3.filename);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullFilename() {
 mp3 = new MP3(null);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithEmptyFilename() {
 mp3 = new MP3("");
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = FileNotFoundException.class)
 public void testConstructorWithInvalidFilename() {
 mp3 = new MP3(invalidFilename);
 fail("FileNotFoundException expected but not thrown");
 }

 @Test
 public void testRunMethod() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
 mp3.run();
 }

 @Test(expected = IllegalThreadStateException.class)
 public void testRunMethodTwice() {
 mp3.run();
 mp3.run();
 fail("IllegalThreadStateException expected but not thrown");
 }

 @Test
 public void testCloseResources() throws IOException {
 mp3.run();
 mp3.join();
 assertNull(mp3.in);
 assertNull(mp3.din);
 }

 @Test
 public void testGetAudioInputStream() throws FileNotFoundException, UnsupportedAudioFileException {
 File file = new File(mp3.filename);
 AudioInputStream in = AudioSystem.getAudioInputStream(file);
 assertSame(in, mp3.in);
 }

 @Test
 public void testDecodeAudioFormat() throws UnsupportedAudioFileException, IOException {
 AudioFormat baseFormat = mp3.in.getFormat();
 AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
 assertEquals(decodedFormat, mp3.decodedFormat);
 }

 @Test
 public void testGetDataLine() throws LineUnavailableException {
 DataLine.Info info = new DataLine.Info(SourceDataLine.class, mp3.decodedFormat);
 SourceDataLine line = (SourceDataLine)AudioSystem.getLine(info);
 assertSame(line, mp3.line);
 }
}
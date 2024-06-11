import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MP3Test {

 private MP3 mp3;
 private File testFile;

 @Before
 public void setUp() throws Exception {
 testFile = new File("test.mp3"); // Replace with a valid MP3 file path
 mp3 = new MP3(testFile.getAbsolutePath());
 }

 @After
 public void tearDown() throws Exception {
 if (mp3 != null) {
 mp3.join(); // Wait for the thread to finish
 }
 testFile.delete(); // Delete the test file after testing
 }

 @Test
 public void testMP3Constructor() {
 assertNotNull(mp3);
 assertEquals(testFile.getAbsolutePath(), mp3.filename);
 }

 @Test(expected = NullPointerException.class)
 public void testMP3ConstructorNullFileName() {
 new MP3(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMP3ConstructorEmptyFileName() {
 new MP3("");
 }

 @Test(expected = FileNotFoundException.class)
 public void testMP3ConstructorInvalidFileName() {
 new MP3("invalid_file.mp3");
 }

 @Test
 public void testThreadStart() throws InterruptedException {
 assertTrue(mp3.isAlive());
 }

 @Test
 public void testAudioInputStream() throws IOException, UnsupportedAudioFileException {
 assertNotNull(mp3.in);
 assertEquals(AudioFileFormat.Type.WAVE, mp3.in.getFormat().getType());
 }

 @Test
 public void testAudioFormat() {
 AudioFormat baseFormat = mp3.in.getFormat();
 AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
 assertTrue(decodedFormat.isBigEndian());
 assertEquals(16, decodedFormat.getSampleSizeInBits());
 assertEquals(baseFormat.getSampleRate(), decodedFormat.getSampleRate());
 assertEquals(baseFormat.getChannels(), decodedFormat.getChannels());
 }

 @Test
 public void testDataLine() throws LineUnavailableException {
 DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
 assertNotNull(AudioSystem.getLine(info));
 }

 @Test
 public void testThreadFinish() throws InterruptedException {
 mp3.join();
 assertFalse(mp3.isAlive());
 }
}
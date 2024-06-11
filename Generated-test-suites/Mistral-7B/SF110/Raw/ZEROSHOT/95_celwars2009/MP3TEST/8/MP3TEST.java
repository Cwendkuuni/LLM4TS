import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

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

 @After
 public void tearDown() {
 if (mp3 != null) {
 mp3.interrupt();
 }
 }

 @Test
 public void testConstructor() {
 assertNotNull(mp3);
 assertFalse(mp3.isInterrupted());
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullFilename() {
 mp3 = new MP3(null);
 }

 @Test
 public void testRun() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
 assertNotNull(mp3.getThreadGroup());
 assertFalse(mp3.isInterrupted());
 mp3.join();
 }

 @Test(expected = IOException.class)
 public void testReadWithInvalidFile() {
 mp3 = new MP3(invalidFilename);
 }

 @Test(expected = LineUnavailableException.class)
 public void testLineUnavailableException() throws Exception {
 mp3 = new MP3(validFilename);
 DataLine.Info info = new DataLine.Info(SourceDataLine.class, new AudioFormat(16, 44100, 2, true, false));
 assertThrows(LineUnavailableException.class, () -> {
 SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
 });
 }

 @Test
 public void testPlayMusic() throws Exception {
 mp3 = new MP3(validFilename);
 mp3.join();
 }
}
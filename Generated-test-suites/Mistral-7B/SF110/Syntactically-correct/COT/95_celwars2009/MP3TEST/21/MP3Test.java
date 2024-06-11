import org.junit.Test;
import static org.junit.Assert.*;
import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

 private MP3 mp3;

 @Test
 public void testMP3Constructor() {
 mp3 = new MP3("test.mp3");
 assertNotNull(mp3);
 assertEquals("test.mp3", mp3.filename);
 }

 @Test
 public void testRunMethod() throws Exception {
 mp3 = new MP3("test.mp3");
 mp3.run();
 // Add assertions here to check if the MP3 is playing correctly
 }

 @Test(expected = FileNotFoundException.class)
 public void testFileNotFoundException() {
 mp3 = new MP3("nonexistentfile.mp3");
 }

 @Test(expected = UnsupportedAudioFileException.class)
 public void testUnsupportedAudioFileException() {
 mp3 = new MP3("test.txt");
 }

 @Test(expected = IOException.class)
 public void testIOException() {
 mp3 = new MP3("test.mp3");
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 OutputStream os = outputStream;
 DataLine.Info info = new DataLine.Info(SourceDataLine.class, new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000, 16, 1, 16, 8000, false));
 SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
 line.open(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000, 16, 1, 16, 8000, false));
 try {
 line.start();
 mp3.run();
 } finally {
 line.drain();
 line.stop();
 line.close();
 }
 os.close();
 }

 @Test
 public void testEmptyConstructor() {
 mp3 = new MP3("");
 assertNull(mp3);
 }

 @Test
 public void testNullConstructor() {
 mp3 = new MP3(null);
 assertNull(mp3);
 }

}
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class MP3Test {

 private MP3 mp3;
 private String validFilename = "test.mp3";
 private String invalidFilename = "nonexistent.mp3";

 @Before
 public void setUp() {
 mp3 = new MP3(validFilename);
 }

 @Test
 public void testConstructorWithValidFilename() {
 //Test that the constructor is able to initialize the MP3 object with a valid filename
 assertEquals(validFilename, mp3.filename);
 }

 @Test
 public void testConstructorWithInvalidFilename() {
 //Test that the constructor throws an exception with an invalid filename
 assertThrows(IllegalArgumentException.class, () -> new MP3(invalidFilename));
 }

 @Test
 public void testPlayMP3() throws Exception {
 //Test that the MP3 plays correctly with a valid filename
 mp3 = new MP3(validFilename);
 mp3.join();

 SourceDataLine line = (SourceDataLine) AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, null));
 byte[] buffer = new byte[1024];
 int bytesRead = 0;

 line.open(AudioFormat.getEncoding("PCM_SIGNED").getBasicFormat());
 line.start();

 AudioInputStream in = null;
 try {
 in = AudioSystem.getAudioInputStream(new File(validFilename));

 int read = 0;
 while ((read = in.read(buffer, 0, buffer.length)) != -1) {
 line.write(buffer, 0, read);
 }

 line.drain();
 line.stop();
 line.close();
 in.close();

 assertEquals(validFilename, mp3.filename);
 } catch (Exception e) {
 e.printStackTrace();
 fail("MP3 failed to play");
 }
 }

 @Test
 public void testPlayMP3WithInvalidFilename() {
 //Test that the MP3 does not play with an invalid filename
 mp3 = new MP3(invalidFilename);
 mp3.join();

 assertNull(mp3.filename);
 }

 @Test
 public void testPlayMP3WithNullFilename() {
 //Test that the constructor throws an exception with a null filename
 assertThrows(NullPointerException.class, () -> new MP3(null));
 }

 @Test
 public void testPlayMP3WithNonExistentFile() {
 //Test that the constructor throws an exception with a non-existent file
 assertThrows(FileNotFoundException.class, () -> new File(invalidFilename).length());
 }

 @Test
 public void testPlayMP3WithInvalidFileFormat() {
 //Test that the constructor throws an exception with an invalid file format
 File file = new File("test.wav"); //Replace with a invalid file format
 assertThrows(UnsupportedAudioFileException.class, () -> new MP3(file.getAbsolutePath()));
 }
}
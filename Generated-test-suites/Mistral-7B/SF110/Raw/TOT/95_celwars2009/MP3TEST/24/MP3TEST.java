import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

 private MP3 mp3;
 private String validFileName = "test.mp3";
 private String invalidFileName = "nonexistent.mp3";

 @Before
 public void setUp() {
 mp3 = new MP3(validFileName);
 }

 @Test
 public void testConstructionWithValidFileName() {
 // Test that the MP3 object is created successfully with a valid filename
 assertNotNull(mp3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructionWithNullFileName() {
 // Test that an IllegalArgumentException is thrown when the filename is null
 mp3 = new MP3(null);
 }

 @Test(expected = FileNotFoundException.class)
 public void testConstructionWithInvalidFileName() {
 // Test that a FileNotFoundException is thrown when the filename does not exist
 mp3 = new MP3(invalidFileName);
 assertThrows(FileNotFoundException.class, () -> new File(invalidFileName).exists());
 }

 @Test(expected = UnsupportedAudioFileException.class)
 public void testConstructionWithInvalidFileFormat() {
 // Test that an UnsupportedAudioFileException is thrown when the file has an unsupported format
 mp3 = new MP3("test.wav");
 assertThrows(UnsupportedAudioFileException.class, () -> AudioSystem.getAudioInputStream(new File("test.wav")));
 }

 @Test
 public void testPlayingAudioFile() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
 // Test that the audio file is played correctly
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 16 * 2, 44100, false);
 TargetDataLine line = (TargetDataLine) AudioSystem.getLine(new DataLine.Info(TargetDataLine.class, format));
 line.open(format);
 line.start();
 mp3.run();
 int read = 0;
 byte[] data = new byte[4096];
 while ((read = line.read(data, 0, data.length)) != -1) {
 out.write(data, 0, read);
 }
 line.drain();
 line.stop();
 line.close();
 byte[] actualData = out.toByteArray();
 byte[] expectedData = readFileToByteArray(validFileName);
 assertArrayEquals(expectedData, actualData);
 }

 @Test
 public void testClosingResources() throws IOException, InterruptedException {
 // Test that resources are closed properly
 mp3 = new MP3(validFileName);
 mp3.join();
 assertFalse(mp3.isAlive());
 assertTrue(new File(validFileName).exists());
 }

 @Test
 public void testHandlingExceptions() throws IOException, InterruptedException {
 // Test that exceptions are handled correctly
 mp3 = new MP3(invalidFileName);
 mp3.join();
 assertTrue(new File(invalidFileName).exists());
 }

 private byte[] readFileToByteArray(String fileName) throws IOException {
 File file = new File(fileName);
 byte[] bytes = new byte[(int) file.length()];
 FileInputStream in = new FileInputStream(file);
 in.read(bytes);
 in.close();
 return bytes;
 }
}
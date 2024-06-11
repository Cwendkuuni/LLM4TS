import org.junit.jupiter.api.*;
import javax.sound.sampled.*;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

public class MP3Test {

 private static File testFile;

 @BeforeAll
 static void setUpClass() {
 testFile = new File("test.mp3");
 try {
 testFile.createNewFile();
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @AfterAll
 static void tearDownClass() {
 testFile.delete();
 }

 @Test
 void testMP3Constructor() {
 assertThrows(NullPointerException.class, () -> new MP3(null));
 assertThrows(IllegalArgumentException.class, () -> new MP3(""));

 MP3 mp3 = new MP3("test.mp3");
 assertEquals("test.mp3", mp3.filename);
 assertNull(mp3.in);
 assertNull(mp3.din);
 }

 @Test
 void testThreadStart() throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
 MP3 mp3 = new MP3("test.mp3");
 assertFalse(mp3.isInterrupted());

 Thread thread = mp3.getThread();
 thread.join();

 assertTrue(mp3.isInterrupted());
 }

 @Test
 void testReadData() throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
 MP3 mp3 = new MP3("test.mp3");
 Thread thread = mp3.getThread();
 thread.join();

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] data = new byte[4096];

 SourceDataLine line = AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, mp3.getAudioFormat()));
 line.open(mp3.getAudioFormat());
 line.start();

 int count = 0;
 while (line.isOpen()) {
 int numBytesRead = mp3.din.read(data, 0, data.length);
 outputStream.write(data, 0, numBytesRead);
 count += numBytesRead;
 if (numBytesRead != data.length) {
 break;
 }
 }
 line.drain();
 line.stop();
 line.close();
 mp3.din.close();

 byte[] expectedData = outputStream.toByteArray();
 byte[] actualData = mp3.getAudioData();
 assertArrayEquals(expectedData, actualData);
 }

 @Test
 void testExceptionHandling() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
 MP3 mp3 = new MP3("test.mp3");
 assertThrows(IOException.class, () -> mp3.run());
 }
}
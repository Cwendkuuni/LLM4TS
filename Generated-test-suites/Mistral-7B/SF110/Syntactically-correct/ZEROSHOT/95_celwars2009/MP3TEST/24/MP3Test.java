import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MP3Test {

 private MP3 mp3;
 private AudioInputStream audioInputStream;

 @Before
 public void setUp() {
 String filename = "test.mp3"; // Replace with the actual test file name
 mp3 = new MP3(filename);
 try {
 audioInputStream = AudioSystem.getAudioInputStream(new File(filename));
 } catch (IOException e) {
 e.printStackTrace();
 fail("Failed to load test file");
 }
 }

 @After
 public void tearDown() {
 try {
 if (mp3 != null) {
 mp3.join();
 if (mp3.in != null) {
 mp3.in.close();
 }
 if (mp3.din != null) {
 mp3.din.close();
 }
 }
 } catch (InterruptedException | IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testConstructor() {
 assertNotNull(mp3);
 assertEquals(filename, mp3.filename);
 }

 @Test
 public void testRun() throws Exception {
 Thread mp3Thread = new Thread(mp3::run);
 mp3Thread.start();
 mp3Thread.join();

 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioInputStream.getFormat().getSampleRate(), 16, audioInputStream.getFormat().getChannels(), audioInputStream.getFormat().getChannels() * 2, audioInputStream.getFormat().getSampleRate(), false);
 DataLine.Info info = new DataLine.Info(TargetDataLine.class, decodedFormat);
 TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
 if (targetDataLine != null) {
 targetDataLine.open(decodedFormat);
 }

 byte[] data = new byte[1024];
 int bytesRead;
 while ((bytesRead = audioInputStream.read(data, 0, data.length)) != -1) {
 outStream.write(data, 0, bytesRead);
 }
 audioInputStream.close();

 byte[] expectedData = outStream.toByteArray();
 byte[] actualData = new byte[mp3Thread.getThreadGroup().activeCount() * 1024]; // Estimated buffer size
 DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(actualData));
 byte[] readData = new byte[data.length];
 int bytesReadActual = 0;
 while ((bytesReadActual = inputStream.read(readData)) != -1) {
 assertArrayEquals(data, readData);
 }
 inputStream.close();

 targetDataLine.close();
 }
}
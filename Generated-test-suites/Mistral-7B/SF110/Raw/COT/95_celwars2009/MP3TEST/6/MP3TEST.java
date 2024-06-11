import org.junit.Test;
import static org.junit.Assert.*;
import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

 private MP3 mp3UnderTest;

 @Test
 public void testMP3ConstructorWithValidFilename() {
 mp3UnderTest = new MP3("test.mp3");
 assertNotNull(mp3UnderTest.getFilename());
 }

 @Test(expected = NullPointerException.class)
 public void testMP3ConstructorWithNullFilename() {
 mp3UnderTest = new MP3(null);
 }

 @Test(expected = IOException.class)
 public void testMP3ConstructorWithInvalidFilename() {
 mp3UnderTest = new MP3("invalidFile.txt");
 }

 @Test
 public void testRunMethod() {
 mp3UnderTest = new MP3("test.mp3");
 mp3UnderTest.run();
 }

 @Test(expected = IllegalThreadStateException.class)
 public void testRunMethodTwice() {
 mp3UnderTest = new MP3("test.mp3");
 mp3UnderTest.run();
 mp3UnderTest.run();
 }

 @Test
 public void testGetAudioFormat() throws Exception {
 File testFile = new File("test.mp3");
 AudioInputStream in = AudioSystem.getAudioInputStream(testFile);
 AudioFormat expectedFormat = in.getFormat();
 MP3 mp3 = new MP3("test.mp3");
 AudioFormat actualFormat = mp3.getAudioFormat();
 assertEquals(expectedFormat, actualFormat);
 }

 @Test(expected = IOException.class)
 public void testGetAudioFormatWithInvalidFile() {
 MP3 mp3 = new MP3("invalidFile.mp3");
 try {
 mp3.getAudioFormat();
 fail();
 } catch (IOException e) {
 //expected
 }
 }

 @Test
 public void testDecodingFormat() throws Exception {
 File testFile = new File("test.mp3");
 AudioInputStream in = AudioSystem.getAudioInputStream(testFile);
 AudioFormat baseFormat = in.getFormat();
 AudioFormat expectedDecodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
 MP3 mp3 = new MP3("test.mp3");
 AudioFormat actualDecodedFormat = mp3.getDecodedFormat();
 assertEquals(expectedDecodedFormat, actualDecodedFormat);
 }

 @Test(expected = UnsupportedAudioFileException.class)
 public void testDecodingFormatWithUnsupportedFile() {
 MP3 mp3 = new MP3("test.wav");
 try {
 mp3.getDecodedFormat();
 fail();
 } catch (UnsupportedAudioFileException e) {
 //expected
 }
 }

 @Test
 public void testPlaySound() throws Exception {
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 mp3UnderTest = new MP3("test.mp3");
 mp3UnderTest.run();
 mp3UnderTest.join();
 byte[] expectedData = out.toByteArray();
 byte[] actualData = mp3UnderTest.getDataFromOutput();
 assertArrayEquals(expectedData, actualData);
 }

 @Test(expected = InterruptedException.class)
 public void testPlaySoundInterrupted() throws Exception {
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 mp3UnderTest = new MP3("test.mp3");
 Thread interruptThread = new Thread(() -> {
 mp3UnderTest.interrupt();
 });
 interruptThread.start();
 mp3UnderTest.run();
 mp3UnderTest.join();
 byte[] expectedData = out.toByteArray();
 byte[] actualData = mp3UnderTest.getDataFromOutput();
 assertArrayEquals(expectedData, actualData);
 fail(); //InterruptedException should be thrown
 }

}
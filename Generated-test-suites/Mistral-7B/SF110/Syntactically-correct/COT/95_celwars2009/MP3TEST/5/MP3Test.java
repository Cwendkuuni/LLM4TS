import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

 private MP3 mp3;
 private String validFilename = "test.mp3";
 private String invalidFilename = "nonexistent.mp3";

 @Before
 public void setUp() {
 mp3 = new MP3(validFilename);
 }

 @Test
 public void testMP3Constructor() {
 // Test if constructor initializes the filename correctly
 assertEquals(validFilename, mp3.filename);
 }

 @Test
 public void testRunMethod() throws Exception {
 // Test if run method plays the audio file
 AudioFormat baseFormat = AudioSystem.getAudioFileFormat(new File(validFilename)).getFormat();
 AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
 byte[] data = new byte[4096];
 SourceDataLine line = null;
 try {
 line = AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, decodedFormat));
 line.open(decodedFormat);
 line.start();
 int nBytesRead;
 while ((nBytesRead = AudioSystem.getAudioInputStream(new File(validFilename)).read(data, 0, data.length)) != -1) {
 line.write(data, 0, nBytesRead);
 }
 line.drain();
 line.stop();
 line.close();
 AudioInputStream din = mp3.in;
 byte[] testData = new byte[nBytesRead];
 int bytesRead = din.read(testData, 0, testData.length);
 assertArrayEquals(testData, data);
 } finally {
 if (line != null) {
 line.close();
 }
 mp3.join();
 }
}

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFilenameConstructor() {
 // Test if constructor throws IllegalArgumentException with an invalid filename
 mp3 = new MP3(invalidFilename);
 assertNotNull(mp3.in); // This should fail because of the exception
 }

 @Test(expected = FileNotFoundException.class)
 public void testAudioFileNotFoundException() {
 // Test if run method handles FileNotFoundException
 mp3 = new MP3(invalidFilename);
 mp3.run();
 // Check if the thread is still running to ensure exception was not ignored
 assertFalse(mp3.isInterrupted());
}

 @Test(expected = UnsupportedAudioFileException.class)
 public void testUnsupportedAudioFileException() {
 // Test if run method handles UnsupportedAudioFileException
 File invalidFile = new File("test.wav");
 mp3 = new MP3(invalidFile.getAbsolutePath());
 mp3.run();
 // Check if the thread is still running to ensure exception was not ignored
 assertFalse(mp3.isInterrupted());
}

 @Test(expected = IOException.class)
 public void testIOException() {
 // Test if run method handles IOException
 mp3 = new MP3(validFilename);
 ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
 System.setErr(new PrintStream(errorStream));
 try {
 mp3.run();
 fail("run method did not throw an IOException");
 } catch (Exception e) {
 // Check if the exception is an IOException
 assertTrue(e instanceof IOException);
 } finally {
 System.setErr(System.out);
 }
 // Check if the thread is still running to ensure exception was not ignored
 assertFalse(mp3.isInterrupted());
}
}
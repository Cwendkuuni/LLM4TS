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
 public void testConstructorWithValidFilename() {
 // Test that the constructor is called with a valid filename
 assertEquals(validFilename, mp3.filename);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidFilename() {
 // Test that the constructor throws an IllegalArgumentException with an invalid filename
 mp3 = new MP3(invalidFilename);
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test
 public void testAudioStreamsAreOpened() {
 // Test that AudioInputStreams are opened in the constructor
 assertNotNull(mp3.in);
 assertNull(mp3.din);

 try {
 mp3.run();
 } catch (Exception e) {
 // Test that the AudioInputStreams are closed in the finally block
 assertNotNull(mp3.in);
 assertNull(mp3.din);
 }
 }

 @Test
 public void testAudioFormatIsDecoded() {
 // Test that the AudioFormat is decoded correctly
 AudioFormat expectedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 16, 44100, false);
 Assert.assertEquals("Audio formats do not match", expectedFormat, mp3.decodedFormat);
 }

 @Test
 public void testDataIsPlayed() throws IOException, LineUnavailableException {
 // Test that data is played correctly
 DataLine.Info info = new DataLine.Info(SourceDataLine.class, mp3.decodedFormat);
 SourceDataLine line = (SourceDataLine)AudioSystem.getLine(info);
 byte[] data = new byte[4096];
 int nBytesRead;

 line.open(mp3.decodedFormat);
 line.start();

 while ((nBytesRead = mp3.in.read(data, 0, data.length)) != -1) {
 line.write(data, 0, nBytesRead);
 }

 line.drain();
 line.stop();
 line.close();

 mp3.in.close();

 // Test that all data is played
 assertEquals("Not all data was played", data.length, mp3.dataPlayed);
 }

 @Test(expected = IOException.class)
 public void testDataLineIsClosedInFinallyBlock() throws IOException, LineUnavailableException {
 // Test that the DataLine is closed in the finally block
 mp3 = new MP3(validFilename);
 try {
 mp3.run();
 } finally {
 fail("DataLine was not closed");
 }
 }
}
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
 public void testConstructionWithValidFilename() {
 // Test that the MP3 object is constructed correctly with a valid filename
 assertEquals(validFilename, mp3.filename);
 }

 @Test
 public void testConstructionWithInvalidFilename() {
 // Test that an exception is thrown when an invalid filename is provided
 assertThrows(IllegalArgumentException.class, () -> new MP3(invalidFilename));
 }

 @Test
 public void testPlayingAudioFile() throws Exception {
 // Test that the audio file is played correctly
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 16, 44100, false);
 SourceDataLine outLine = AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, targetFormat));
 SourceDataLine inLine = (SourceDataLine) mp3.in;
 DataLine.Info info = new DataLine.Info(MixingBoard.class, targetFormat);
 MixingBoard mixer = (MixingBoard) AudioSystem.getLine(info);
 TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(new DataLine.Info(TargetDataLine.class, targetFormat));

 outLine.open(targetFormat);
 mixer.open(targetFormat);
 targetLine.open(targetFormat);

 mixer.connect(0, outLine);
 inLine.connect(0, mixer);
 targetLine.start();
 outLine.start();
 mp3.start();

 byte[] data = new byte[4096];
 int bytesRead;
 while ((bytesRead = mp3.in.read(data, 0, data.length)) > 0) {
 out.write(data, 0, bytesRead);
 }

 outLine.drain();
 outLine.stop();
 outLine.close();
 mixer.close();
 targetLine.close();

 byte[] expectedData = out.toByteArray();
 byte[] actualData = mp3.getBytesFromInput();
 assertArrayEquals(expectedData, actualData);
 }

 @Test
 public void testPlayingEmptyFile() throws Exception {
 // Test that an exception is thrown when trying to play an empty file
 Files.deleteIfExists(Paths.get(validFilename));
 mp3 = new MP3(validFilename);
 assertThrows(IOException.class, mp3::run);
 }

 @Test
 public void testPlayingFileWithInvalidFormat() throws Exception {
 // Test that an exception is thrown when trying to play a file with an invalid format
 File invalidFile = new File("test_invalid.wav");
 mp3 = new MP3(invalidFile.getAbsolutePath());
 assertThrows(UnsupportedAudioFileException.class, mp3::run);
 }

 @Test
 public void testClosingAudioInputStream() throws Exception {
 // Test that the AudioInputStream is closed properly in the finally block
 mp3 = new MP3(validFilename);
 mp3.run();
 assertTrue(mp3.din.isClosed());
 }
}
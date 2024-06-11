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
 public void testInitializationWithValidFileName() {
 assertNotNull(mp3);
 assertEquals(validFileName, mp3.filename);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitializationWithNullFileName() {
 mp3 = new MP3(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitializationWithEmptyFileName() {
 mp3 = new MP3("");
 }

 @Test(expected = FileNotFoundException.class)
 public void testInitializationWithInvalidFileName() {
 mp3 = new MP3(invalidFileName);
 }

 @Test(expected = UnsupportedAudioFileException.class)
 public void testInitializationWithUnsupportedFileType() {
 File testFile = new File("test.txt");
 mp3 = new MP3(testFile.getAbsolutePath());
 }

 @Test
 public void testPlayingAudioFile() throws Exception {
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 SourceDataLine outLine = AudioSystem.getLine(new DataLine.Info(MidiSystem.getLineInfo("null.synth.output"), null));
 outLine.open(new AudioFormat(44100, 8, 1, 4, 44100, false));
 outLine.start();

 ByteArrayInputStream inStream = new ByteArrayInputStream(mp3.getBytes());
 AudioInputStream in = AudioSystem.getAudioInputStream(inStream);
 Clip clip = AudioSystem.getClip();
 clip.open(in);
 clip.start();

 int bytesRead;
 byte[] buffer = new byte[4096];

 while ((bytesRead = in.read(buffer, 0, buffer.length)) != -1) {
 outLine.write(buffer, 0, bytesRead);
 }

 outLine.drain();
 outLine.stop();
 outLine.close();
 clip.close();

 byte[] expected = new byte[mp3.getBytes().length];
 assertArrayEquals(expected, outStream.toByteArray());
 }

 @Test(expected = IOException.class)
 public void testClosingAudioInputStreamInFinallyBlock() throws Exception {
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 SourceDataLine outLine = AudioSystem.getLine(new DataLine.Info(MidiSystem.getLineInfo("null.synth.output"), null));
 outLine.open(new AudioFormat(44100, 8, 1, 4, 44100, false));
 outLine.start();

 ByteArrayInputStream inStream = new ByteArrayInputStream(mp3.getBytes());
 AudioInputStream in = AudioSystem.getAudioInputStream(inStream);
 Clip clip = AudioSystem.getClip();
 clip.open(in);
 clip.start();

 int bytesRead;
 byte[] buffer = new byte[4096];

 while ((bytesRead = in.read(buffer, 0, buffer.length)) != -1) {
 outLine.write(buffer, 0, bytesRead);
 }

 outLine.drain();
 outLine.stop();
 outLine.close();
 clip.close();
 in.close(); // This line should throw an IOException
 }
}
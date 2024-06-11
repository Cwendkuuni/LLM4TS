import org.junit.Test;
import static org.junit.Assert.*;
import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

 private static final String VALID_MP3_FILE = "test.mp3";
 private static final String NON_EXISTENT_FILE = "non_existent_file.mp3";

 @Test
 public void testMP3Constructor() throws Exception {
 MP3 mp3 = new MP3(VALID_MP3_FILE);
 assertEquals(VALID_MP3_FILE, mp3.filename);
 }

 @Test(expected = NullPointerException.class)
 public void testMP3ConstructorWithNullFilename() {
 new MP3(null);
 }

 @Test(expected = FileNotFoundException.class)
 public void testMP3ConstructorWithNonExistentFile() {
 new MP3(NON_EXISTENT_FILE);
 }

 @Test
 public void testRunMethodWithValidFile() throws Exception {
 MP3 mp3 = new MP3(VALID_MP3_FILE);
 mp3.join();
 }

 @Test(expected = IOException.class)
 public void testRunMethodWithIOException() throws Exception {
 MP3 mp3 = new MP3(VALID_MP3_FILE);
 doReturn(null).when(mp3).getAudioInputStream();
 mp3.join();
 }

 @Test(expected = UnsupportedAudioFileException.class)
 public void testRunMethodWithUnsupportedAudioFile() throws Exception {
 MP3 mp3 = new MP3(new File("test.txt"));
 mp3.join();
 }

 @Test(expected = LineUnavailableException.class)
 public void testRunMethodWithLineUnavailableException() throws Exception {
 doThrow(LineUnavailableException.class).when(AudioSystem.getLine(any(DataLine.Info.class)));
 MP3 mp3 = new MP3(VALID_MP3_FILE);
 mp3.join();
 }

 @Test
 public void testCloseMethodWithValidFile() throws Exception {
 MP3 mp3 = new MP3(VALID_MP3_FILE);
 mp3.join();
 mp3.close();
 }

 @Test
 public void testCloseMethodWithNullFile() {
 MP3 mp3 = new MP3(null);
 mp3.close();
 }

 private AudioInputStream getAudioInputStream(String filename) throws Exception {
 final File file = new File(filename);
 return AudioSystem.getAudioInputStream(file);
 }

 @Test
 public void testGetAudioInputStream() throws Exception {
 final AudioInputStream expected = getAudioInputStream(VALID_MP3_FILE);
 MP3 mp3 = new MP3(VALID_MP3_FILE);
 AudioInputStream actual = mp3.getAudioInputStream();
 assertEquals(expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testGetAudioInputStreamWithNullFilename() {
 new MP3(null).getAudioInputStream();
 }

 @Test(expected = FileNotFoundException.class)
 public void testGetAudioInputStreamWithNonExistentFile() {
 new MP3(NON_EXISTENT_FILE).getAudioInputStream();
 }

}
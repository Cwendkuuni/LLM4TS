import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

 private String validFilename = "test.mp3";
 private String invalidFilename = "";
 private MP3 mp3;

 @Before
 public void setUp() {
 this.mp3 = new MP3(validFilename);
 }

 @After
 public void tearDown() {
 try {
 if (mp3.in != null) {
 mp3.in.close();
 }
 if (mp3.din != null) {
 mp3.din.close();
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testConstructorWithValidFilename() {
 // Test that the constructor is able to initialize the MP3 object with a valid filename
 assertTrue(mp3.isAlive());
 }

 @Test
 public void testConstructorWithInvalidFilename() {
 // Test that the constructor throws an exception when given an invalid filename
 assertThrows(NullPointerException.class, () -> new MP3(invalidFilename));
 }

 @Test
 public void testPlayMP3File() {
 // Test that the MP3 object is able to play a valid mp3 file
 try {
 mp3 = new MP3(validFilename);
 mp3.join();
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testPlayEmptyFile() {
 // Test that the MP3 object is able to handle an empty file
 File emptyFile = new File("empty.mp3");
 mp3 = new MP3(emptyFile.getAbsolutePath());
 mp3.join();
 }

 @Test
 public void testPlayFileWithInvalidFormat() {
 // Test that the MP3 object is able to handle an mp3 file with an invalid format
 File invalidFile = new File("invalid.file");
 mp3 = new MP3(invalidFile.getAbsolutePath());
 assertThrows(UnsupportedAudioFileException.class, mp3::run);
 }

 @Test
 public void testPlayFileWithNullDataLine() {
 // Test that the MP3 object is able to handle a null DataLine
 File validFile = new File("test.mp3");
 mp3 = new MP3(validFile.getAbsolutePath());
 DataLine.Info info = new DataLine.Info(SourceDataLine.class, null);
 assertThrows(NullPointerException.class, () -> {
 mp3.run();
 AudioSystem.getLine(info);
 });
 }
}
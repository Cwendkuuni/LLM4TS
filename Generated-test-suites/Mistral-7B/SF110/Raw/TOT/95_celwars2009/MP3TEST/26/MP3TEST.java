import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.sound.sampled.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class MP3Test {

 private String validFilename = "test.mp3";
 private String invalidFilename = "test.txt";
 private MP3 mp3;

 @Before
 public void setUp() {
 File file = new File(validFilename);
 try {
 Files.copy(Paths.get("path/to/valid/mp3file.mp3"), file.toPath());
 } catch (IOException e) {
 e.printStackTrace();
 }
 mp3 = new MP3(validFilename);
 }

 @After
 public void tearDown() {
 File file = new File(validFilename);
 if (file.exists()) {
 file.delete();
 }
 mp3 = null;
 }

 @Test
 public void testMP3ConstructionWithValidFilename() {
 // Test construction with valid filename
 mp3 = new MP3(validFilename);
 assertTrue(mp3.isAlive());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMP3ConstructionWithInvalidFilename() {
 // Test construction with invalid filename
 mp3 = new MP3(invalidFilename);
 fail("Construction with invalid filename should throw an IllegalArgumentException");
 }

 @Test
 public void testPlayMusicWithValidFile() {
 // Test playing music with valid file
 try {
 Thread.sleep(5000); // Give the thread some time to run
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 // Check if the music was played correctly
 assertEquals(50, mp3.getAudioFormat().getChannels());
 }

 @Test(expected = IOException.class)
 public void testPlayMusicWithInvalidFile() {
 // Test playing music with invalid file
 mp3 = new MP3(invalidFilename);
 try {
 Thread.sleep(1000); // Give the thread some time to fail
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 assertFalse(mp3.isAlive());
 }
}
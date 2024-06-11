import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

 private MP3 mp3;
 private String validFilename = "test.mp3";

 @Before
 public void setUp() {
 mp3 = new MP3(validFilename);
 }

 @Test
 public void testMP3Constructor() {
 Assert.assertNotNull(mp3);
 Assert.assertEquals(validFilename, mp3.filename);
 }

 @Test
 public void testRun_ValidFile() {
 try {
 mp3.run();
 } catch (Exception e) {
 e.printStackTrace();
 fail("Exception should not be thrown");
 }
}

 @Test(expected = FileNotFoundException.class)
 public void testRun_InvalidFile() {
 mp3 = new MP3("non_existent_file.mp3");
 mp3.run();
}

 @Test(expected = UnsupportedAudioFileException.class)
 public void testRun_InvalidFileFormat() {
 File invalidFile = new File("invalid_file.txt");
 mp3 = new MP3(invalidFile.getPath());
 mp3.run();
}

 @Test(expected = IOException.class)
 public void testRun_IOError() {
 File inputFile = new File("input_file.mp3");
 byte[] data = new byte[1];
 try (ByteArrayInputStream bais = new ByteArrayInputStream(data)) {
 mp3 = new MP3(inputFile.getPath());
 mp3.in = bais;
 mp3.run();
 } catch (Exception e) {
 e.printStackTrace();
 fail("IOError should be thrown");
 }
}

 @Test
 public void testRun_CloseResources() {
 try {
 mp3.run();
 } finally {
 Assert.assertNull(mp3.in);
 Assert.assertNull(mp3.din);
 }
}

 @Test
 public void testSetFilename() {
 String newFilename = "new_filename.mp3";
 mp3.filename = newFilename;
 Assert.assertEquals(newFilename, mp3.filename);
}
}
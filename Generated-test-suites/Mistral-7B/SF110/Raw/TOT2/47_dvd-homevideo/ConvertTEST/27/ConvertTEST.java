import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.Arrays;

public class ConvertTest {

 private Convert convert;
 private String outputDir;

 @Before
 public void setUp() {
 outputDir = "test_output_dir";
 System.setProperty("user.dir", outputDir);
 convert = new Convert("dvd-ntsc", "16:9", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 }

 @Test
 public void testInit_noThread() {
 convert.init();
 assertFalse(convert.m_Error);
 }

 @Test
 public void testInit_withThread() {
 convert = new Convert("dvd-ntsc", "16:9", new GUI() {
 @Override
 public boolean menuChkThread() {
 return true;
 }
 }, 0, "dvd-ntsc", "16:9");
 convert.init();
 assertTrue(convert.m_Error);
 }

 @Test
 public void testTranscode_validInput() {
 convert.Transcode();
 File[] files = new File(outputDir + "/dv/").listFiles();
 assertNotNull(files);
 assertEquals(1, files.length);
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testTranscode_invalidInput() {
 convert = new Convert("dvd-ntsc", "16:9", new GUI() {
 @Override
 public boolean menuChkThread() {
 return true;
 }
 }, 0, null, "16:9");
 convert.init();
 convert.Transcode();
 }

 @Test
 public void testMatrix_createFile() {
 convert = new Convert("dvd-ntsc", "16:9", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 convert.matrix();
 File matrixFile = new File(outputDir + "/matrix.txt");
 assertTrue(matrixFile.exists());
 }

 @Test(expected = IOException.class)
 public void testMatrix_fileError() {
 convert = new Convert("dvd-ntsc", "16:9", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 try {
 convert.matrix();
 File matrixFile = new File(outputDir + "/matrix.txt");
 assertFalse(matrixFile.exists());
 }
 catch (IOException e) {
 // expected
 }
 }
}
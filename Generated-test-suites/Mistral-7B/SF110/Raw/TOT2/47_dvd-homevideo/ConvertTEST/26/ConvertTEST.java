import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.Arrays;

public class ConvertTest {

 private Convert convert;
 private String outputDir;

 @Before
 public void setUp() {
 outputDir = "test_output";
 System.setProperty("user.dir", outputDir);
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return true;
 }
 });
 }

 @Test
 public void testConstructorWithFormatAndAspectRatio() {
 convert = new Convert(0, "dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 assertEquals("dvd-ntsc", convert.m_Format);
 assertEquals("4:3", convert.m_AspectRatio);
 }

 @Test
 public void testConstructorWithQualityAndFormatAndAspectRatio() {
 convert = new Convert(1, "dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return true;
 }
 });
 assertEquals("dvd-ntsc", convert.m_Format);
 assertEquals("4:3", convert.m_AspectRatio);
 assertEquals(1, convert.m_Quality);
 }

 @Test
 public void testInitWithThreadCheckboxChecked() {
 convert.init();
 assertTrue(convert.m_Thread.isAlive());
 }

 @Test
 public void testInitWithThreadCheckboxUnchecked() {
 convert = new Convert(0, "dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 assertFalse(convert.m_Thread.isAlive());
 }

 @Test
 public void testTranscodeWithValidInputs() {
 convert.init();
 convert.Transcode();
 File[] files = new File(outputDir + "/dv/").listFiles();
 assertEquals(convert.video_files.length, files.length);
 for (int i = 0; i < files.length; i++) {
 String fileName = files[i].getName();
 assertEquals(convert.video_files[i], fileName.substring(0, 11));
 }
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testTranscodeWithNonStandardFileInDVDDirectory() {
 convert = new Convert(0, "dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return true;
 }
 });
 File nonStandardFile = new File(outputDir + "/dv/test.mp4");
 nonStandardFile.createNewFile();
 convert.init();
}

 @Test
 public void testMatrixFileCreation() {
 convert.init();
 convert.matrix();
 File matrixFile = new File(outputDir + "/matrix.txt");
 assertTrue(matrixFile.exists());
 }

 @Test(expected = IOException.class)
 public void testMatrixFileCreationFailure() {
 convert.init();
 try {
 convert.matrix();
 fail("Matrix file creation should have thrown an IOException");
 }
 catch (IOException e) {
 // expected
 }
 }

 @After
 public void tearDown() {
 if (convert != null) {
 convert.m_GUI.deleteDirectory(outputDir);
 }
 }

}
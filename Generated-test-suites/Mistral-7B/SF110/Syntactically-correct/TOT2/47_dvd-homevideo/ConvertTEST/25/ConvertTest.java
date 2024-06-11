import org.junit.After;
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
 outputDir = "output_directory"; // should be set to a valid output directory
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return true;
 }
 });
 }

 @After
 public void tearDown() {
 if (convert != null) {
 convert.init();
 }
 }

 @Test
 public void testConstructorWithFormatAndAspectRatio() {
 Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
 assertEquals("dvd-ntsc", convert.m_Format);
 assertEquals("4:3", convert.m_AspectRatio);
 }

 @Test
 public void testConstructorWithQualityAndFormatAndAspectRatio() {
 Convert convert = new Convert(5, "dvd-ntsc", "4:3", new GUI());
 assertEquals(5, convert.m_Quality);
 assertEquals("dvd-ntsc", convert.m_Format);
 assertEquals("4:3", convert.m_AspectRatio);
 }

 @Test
 public void testCheckInitWithoutThread() {
 Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
 assertFalse(convert.init());
 }

 @Test
 public void testCheckInitWithThread() {
 Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.init();
 assertTrue(convert.init());
 }

 @Test
 public void testInitThrowsExceptionWithInvalidPathToTranscode() {
 File outputDir = new File("invalid_output_directory");
 Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.m_GUI = new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir.getAbsolutePath();
 }
 };
 assertFalse(convert.init());
 }

 @Test
 public void testTranscodeWithValidFiles() {
 Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.init();
 File[] videoFiles = new File(outputDir).listFiles();
 assertEquals(videoFiles.length, convert.thread_track);
 for (int i = 0; i < videoFiles.length; i++) {
 try {
 convert.video_files = new String[] { videoFiles[i].getName() };
 convert.Transcode();
 }
 catch (IOException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testTranscodeWithInvalidFiles() {
 Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.init();
 File[] videoFiles = new File(outputDir).listFiles();
 if (videoFiles.length < 2) {
 throw new AssertionError("Not enough files in output directory for test.");
 }
 String[] videoFilesNames = new String[videoFiles.length - 1];
 System.arraycopy(videoFiles, 1, videoFilesNames, 0, videoFilesNames.length);
 convert.video_files = videoFilesNames;
 convert.Transcode();
 }
}
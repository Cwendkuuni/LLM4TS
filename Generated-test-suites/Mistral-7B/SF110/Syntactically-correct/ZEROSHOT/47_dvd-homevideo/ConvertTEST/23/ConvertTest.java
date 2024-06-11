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
 // Set up your test environment here, for example by creating a test directory
 outputDir = "testOutputDir";
 File dir = new File(outputDir);
 dir.mkdirs();
 }

 @After
 public void tearDown() {
 // Clean up your test environment here, for example by deleting the test directory
 File dir = new File(outputDir);
 dir.delete();
 }

 @Test
 public void testConvertConstruction() {
 // Test construction with default parameters
 Convert c1 = new Convert("dvd-ntsc", "4:3", null);
 assertNotNull(c1);

 // Test construction with custom parameters
 Convert c2 = new Convert(5, "pal", "16:9", null);
 assertNotNull(c2);
 }

 @Test
 public void testInit() throws InterruptedException {
 // Test init method with no thread
 Convert c = new Convert(5, "dvd-ntsc", "4:3", null);
 assertTrue(c.init());

 // Test init method with thread
 Convert cThread = new Convert(5, "dvd-ntsc", "4:3", null);
 cThread.m_GUI.menuChkThread.setSelected(true);
 Thread t = new Thread(cThread);
 t.start();
 t.join();
 assertTrue(cThread.init());
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testInitWithInvalidDirectory() {
 Convert c = new Convert(5, "dvd-ntsc", "4:3", null);
 File dir = new File(outputDir + "/invalidDirectory");
 c.m_GUI.strOutputDir = dir.getAbsolutePath();
 assertFalse(c.init());
 }

 @Test
 public void testTranscode() throws IOException, InterruptedException {
 Convert c = new Convert(5, "dvd-ntsc", "4:3", null);
 c.init();

 // Test transcode method with no files in directory
 assertFalse(c.transcode());

 // Test transcode method with valid files in directory
 File dir = new File(outputDir + "/dv");
 File[] files = dir.listFiles();
 Arrays.sort(files);
 String firstFile = files[0].getName();
 c.video_files = new String[] { firstFile };
 assertTrue(c.transcode());
 }

 @Test
 public void testMatrix() throws IOException {
 Convert c = new Convert(5, "dvd-ntsc", "4:3", null);
 c.init();
 File file = new File(outputDir + "/matrix.txt");
 assertTrue(file.exists());
 }
}
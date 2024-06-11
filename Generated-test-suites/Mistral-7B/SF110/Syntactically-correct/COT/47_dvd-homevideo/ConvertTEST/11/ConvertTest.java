import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.Arrays;

public class ConvertTest {

 private Convert convert;
 private String outputDir;

 @Before
 public void setup() {
 this.outputDir = "test_output";
 File dir = new File(outputDir);
 dir.mkdir();
 convert = new Convert("dvd-ntsc", "16:9", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 convert.init();
 }

 @Test
 public void testConvertConstructorWithFormatAndAspectRatio() {
 assertEquals("dvd-ntsc", convert.m_Format);
 assertEquals("16:9", convert.m_AspectRatio);
 }

 @Test
 public void testConvertConstructorWithFormatQualityAndAspectRatio() {
 Convert convert1 = new Convert(1, "dvd-ntsc", "16:9", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 assertEquals(1, convert1.m_Quality);
 assertEquals("dvd-ntsc", convert1.m_Format);
 assertEquals("16:9", convert1.m_AspectRatio);
 }

 @Test
 public void testInitWithoutThreadCheck() {
 assertFalse(convert.init());
 assertTrue(new File(outputDir + "/log/transcode.log").exists());
 assertTrue(new File(outputDir + "/log/mplex.log").exists());
 }

 @Test
 public void testInitWithThreadCheck() {
 convert = new Convert("dvd-ntsc", "16:9", new GUI() {
 @Override
 public boolean menuChkThread() {
 return true;
 }
 });
 convert.init();
 Thread thread = convert.m_Thread;
 try {
 thread.join();
 }
 catch (InterruptedException e) {
 fail("Thread was interrupted during join");
 }
 assertTrue(!convert.m_Error);
 assertFalse(new File(outputDir + "/log/transcode.log").exists());
 assertFalse(new File(outputDir + "/log/mplex.log").exists());
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testTranscodeWithInvalidFileInDirectory() {
 convert.video_files = new String[]{"test1.dv", "test2.dv", "test3.dv", "test4.mp4"};
 convert.Transcode();
 }

 @Test
 public void testTranscodeWithValidFiles() {
 File dir = new File(outputDir + "/dv/");
 String[] files = dir.list();
 assertEquals(4, files.length);
 convert.video_files = files;
 convert.Transcode();
 for (int i = 0; i < files.length; i++) {
 String file = files[i];
 assertTrue(new File(outputDir + "/" + file).exists());
 }
 }

 @Test
 public void testMatrix() {
 File file = new File(outputDir + "/matrix.txt");
 assertTrue(file.exists());
 }

 @After
 public void teardown() {
 File dir = new File(outputDir);
 Arrays.stream(dir.listFiles()).forEach(File::delete);
 dir.delete();
 }
}
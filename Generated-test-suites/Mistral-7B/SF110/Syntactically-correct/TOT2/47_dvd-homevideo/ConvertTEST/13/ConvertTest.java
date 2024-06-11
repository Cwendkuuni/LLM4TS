import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.Arrays;

public class ConvertTest {

 private Convert convert;

 @Test
 public void testConvertConstructorWithFormatAndAspectRatio() {
 convert = new Convert("dvd-ntsc", "16:9");
 assertEquals("dvd-ntsc", convert.getFormat());
 assertEquals("16:9", convert.getAspectRatio());
 }

 @Test
 public void testConvertConstructorWithQualityFormatAndAspectRatio() {
 convert = new Convert(1, "dvd-ntsc", "16:9", null);
 assertEquals(1, convert.getQuality());
 assertEquals("dvd-ntsc", convert.getFormat());
 assertEquals("16:9", convert.getAspectRatio());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConvertConstructorWithInvalidFormat() {
 convert = new Convert("invalidFormat", "16:9", null);
 fail("Should throw IllegalArgumentException with message: 'Invalid format: invalidFormat'");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConvertConstructorWithNullAspectRatio() {
 convert = new Convert(1, "dvd-ntsc", null);
 fail("Should throw IllegalArgumentException with message: 'Aspect ratio cannot be null'");
 }

 @Test
 public void testInitNoThread() {
 convert = new Convert(1, "dvd-ntsc", "16:9", null);
 assertFalse(convert.init());
 assertTrue(convert.isError());
 }

 @Test
 public void testInitWithThread() {
 convert = new Convert(1, "dvd-ntsc", "16:9", null);
 convert.m_GUI = new GUI() {};
 assertTrue(convert.init());
 assertFalse(convert.isError());
 }

 @Test(expected = InterruptedException.class)
 public void testThreadInterruptedException() throws InterruptedException {
 convert = new Convert(1, "dvd-ntsc", "16:9", null);
 convert.m_GUI = new GUI() {};
 convert.m_Thread = new Thread(convert);
 convert.m_Thread.start();
 convert.m_Thread.interrupt();
 }

 @Test
 public void testTranscodeWithValidFiles() {
 convert = new Convert(1, "dvd-ntsc", "16:9", null);
 convert.m_GUI = new GUI() {};
 File listDir = new File("dvd");
 File[] files = listDir.listFiles();
 for (int i = 0; i < files.length; i++) {
 if (files[i].isFile()) {
 convert.video_files = new String[] { files[i].getName() };
 convert.Transcode();
 }
 }
 assertFalse(convert.isError());
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testTranscodeWithInvalidFiles() {
 convert = new Convert(1, "dvd-ntsc", "16:9", null);
 convert.m_GUI = new GUI() {};
 convert.video_files = new String[] { "file1.mp4", "file2.mp4" };
 for (int i = 0; i < convert.video_files.length; i++) {
 convert.video_files[i] = "file" + (i + 1) + ".dv";
 }
 convert.Transcode();
 fail("Should throw StringIndexOutOfBoundsException with message: 'Are there non-standard (not .dv)" +
 "\nfiles in the dv directory?\n'");
 }

 @Test
 public void testMatrixFileCreation() {
 convert = new Convert(1, "dvd-ntsc", "16:9", null);
 convert.m_GUI = new GUI() {};
 try {
 convert.matrix();
 File matrixFile = new File(convert.m_GUI.strOutputDir + "/matrix.txt");
 assertTrue(matrixFile.exists());
 }
 catch (IOException e) {
 fail("IO Error: " + e.getMessage());
 }
 }

 @Test
 public void testTranscodeLogFileCreation() {
 convert = new Convert(1, "dvd-ntsc", "16:9", null);
 convert.m_GUI = new GUI() {};
 File logFile = new File(convert.m_GUI.strOutputDir + "/log/transcode.log");
 assertTrue(logFile.exists());
 }

 @Test
 public void testMplexLogFileCreation() {
 convert = new Convert(1, "dvd-ntsc", "16:9", null);
 convert.m_GUI = new GUI() {};
 File mplexLogFile = new File(convert.m_GUI.strOutputDir + "/log/mplex.log");
 assertTrue(mplexLogFile.exists());
 }

}
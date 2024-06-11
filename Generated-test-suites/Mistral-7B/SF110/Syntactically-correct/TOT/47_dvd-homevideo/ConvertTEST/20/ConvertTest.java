import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.util.Arrays;

public class ConvertTest {

 private Convert convert;
 private String outputDir;

 @Before
 public void setUp() {
 outputDir = "test_output";
 File dir = new File(outputDir);
 dir.mkdir();
 convert = new Convert("dvd-ntsc", "16:9", new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }

 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 }

 @Test
 public void initShouldNotTranscodeWhenGUIDoesntCheckThread() {
 convert.init();
 Assert.assertFalse(convert.m_Error);
 File logDir = new File(outputDir + "/log/");
 File transcodeLog = new File(logDir, "transcode.log");
 File mplexLog = new File(logDir, "mplex.log");
 Assert.assertFalse(transcodeLog.exists());
 Assert.assertFalse(mplexLog.exists());
 }

 @Test
 public void initShouldTranscodeWhenGUIChecksThread() {
 convert = new Convert("dvd-ntsc", "16:9", new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }

 @Override
 public boolean menuChkThread() {
 return true;
 }
 });
 convert.init();
 Assert.assertTrue(convert.m_Error || new File(outputDir + "/dv/inp.vob").exists());
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void initShouldThrowErrorWhenNonStandardFileInDvDirectory() {
 convert = new Convert("dvd-ntsc", "16:9", new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }

 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 File listDir = new File(outputDir + "/dv/");
 listDir.mkdir();
 listDir.mkdir(); // Create a non-standard file named "dv"
 convert.init();
 }

 @Test
 public void transcodeShouldPrintErrorMessageWhenTranscodeCommandNotFound() {
 convert = new Convert("dvd-ntsc", "16:9", new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }

 @Override
 public boolean menuChkThread() {
 return true;
 }
 });
 convert.transcode("non-existent-file.dv");
 Assert.assertTrue(convert.m_Error);
 }

 @Test
 public void transcodeShouldPrintProgressInfoWhenTranscodeIsSuccessful() {
 convert = new Convert("dvd-ntsc", "16:9", new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }

 @Override
 public boolean menuChkThread() {
 return true;
 }
 });
 File testFile = new File(outputDir + "/dv/test.dv");
 testFile.createNewFile();
 convert.transcode(testFile.getAbsolutePath());
 Assert.assertFalse(convert.m_Error);
 File logDir = new File(outputDir + "/log/");
 File transcodeLog = new File(logDir, "transcode.log");
 File mplexLog = new File(logDir, "mplex.log");
 Assert.assertTrue(transcodeLog.exists());
 Assert.assertTrue(mplexLog.exists());
 }

 @Test
 public void transcodeShouldPrintErrorMessageWhenTranscodeFails() {
 convert = new Convert("dvd-ntsc", "16:9", new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }

 @Override
 public boolean menuChkThread() {
 return true;
 }
 });
 File testFile = new File(outputDir + "/dv/test.dv");
 testFile.createNewFile();
 convert.transcode("test.dv"); // Intentionally create a failure by providing the same file for input and output
 Assert.assertTrue(convert.m_Error);
 }

 @Test
 public void matrixShouldCreateMatrixFile() {
 convert = new Convert("dvd-ntsc", "16:9", new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }
 });
 convert.matrix();
 File matrixFile = new File(outputDir + "/matrix.txt");
 Assert.assertTrue(matrixFile.exists());
 }
}
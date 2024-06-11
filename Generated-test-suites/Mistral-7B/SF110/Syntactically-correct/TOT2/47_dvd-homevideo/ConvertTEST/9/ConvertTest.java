import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.Arrays;

public class ConvertTest {

 private Convert convert;
 private String outputDir;
 private String[] videoFiles;

 @Before
 public void setup() {
 outputDir = "testOutputDir";
 videoFiles = new String[]{"testFile1.dv", "testFile2.dv"};
 convert = new Convert("dvd-ntsc", "1:1", new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }

 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 convert.init();
 }

 @Test
 public void testInitWithGUI() {
 assertFalse(convert.m_Error);
 }

 @Test
 public void testInitWithoutGUI() {
 convert = new Convert("dvd-ntsc", "1:1", null);
 convert.init();
 assertFalse(convert.m_Error);
 }

 @Test
 public void testInitWithError() {
 convert = new Convert("invalidFormat", "1:1", new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }
 });
 convert.init();
 assertTrue(convert.m_Error);
 }

 @Test
 public void testTranscodeWithValidFiles() {
 final int threadTrack = 0;
 convert.Transcode();
 File[] files = new File(outputDir).listFiles();
 assertEquals(videoFiles.length, files.length);
 String expectedFile1 = videoFiles[threadTrack].substring(0, 11);
 String expectedFile2 = videoFiles[threadTrack + 1].substring(0, 11);
 String actualFile1 = files[threadTrack].getName();
 String actualFile2 = files[threadTrack + 1].getName();
 assertTrue(expectedFile1.equals(actualFile1) || expectedFile1.equals(actualFile2));
 assertTrue(expectedFile2.equals(actualFile1) || expectedFile2.equals(actualFile2));
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testTranscodeWithEmptyDirectory() {
 convert.Thread.interrupt();
 convert.init();
 File dir = new File(outputDir);
 if (dir.isDirectory()) {
 dir.delete();
 }
 convert.Transcode();
 }

 @Test
 public void testTranscodeWithInvalidPath() {
 outputDir = "invalidOutputDir";
 convert = new Convert("dvd-ntsc", "1:1", new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }
 });
 convert.init();
 convert.Transcode();
 assertTrue(convert.m_Error);
 }

 @Test
 public void testTranscodeWithMissingTranscode() {
 System.setProperty("path.separator", File.pathSeparatorChar + File.pathSeparatorChar);
 System.setProperty("java.class.path", "./testClasses:./testLibraries");
 convert = new Convert("dvd-ntsc", "1:1", new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }
 });
 convert.init();
 convert.Transcode();
 assertTrue(convert.m_Error);
 }

 @Test
 public void testTranscodeWithMissingMplex() {
 System.setProperty("path.separator", File.pathSeparatorChar + File.pathSeparatorChar);
 System.setProperty("java.class.path", "./testClasses:./testLibraries");
 convert = new Convert("dvd-ntsc", "1:1", new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }
 });
 convert.init();
 final String mplexCmd = convert.mplex;
 convert.mplex = mplexCmd.replaceAll("mplex", "invalidMplex");
 convert.Transcode();
 assertTrue(convert.m_Error);
 }

 @Test
 public void testTranscodeWithInvalidFileTypes() {
 File dir = new File(outputDir);
 if (!dir.exists()) {
 dir.mkdirs();
 }
 File inputFile = new File(outputDir, "testFile.txt");
 try {
 inputFile.createNewFile();
 }
 catch (IOException e) {
 //do nothing
 }
 convert = new Convert("dvd-ntsc", "1:1", new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }
 });
 convert.init();
 convert.video_files = new String[]{"testFile1.dv", inputFile.getName()};
 convert.Transcode();
 assertTrue(convert.m_Error);
 }

 @Test
 public void testTranscodeWithValidFilesAndErrors() {
 final int threadTrack = 0;
 convert.Thread.interrupt();
 convert.init();
 convert.video_files = Arrays.copyOfRange(videoFiles, 0, videoFiles.length - 1);
 convert.Transcode();
 File[] files = new File(outputDir).listFiles();
 assertEquals(videoFiles.length - 1, files.length);
 String expectedFile1 = videoFiles[threadTrack].substring(0, 11);
 String expectedFile2 = videoFiles[threadTrack + 1].substring(0, 11);
 String actualFile1 = files[threadTrack].getName();
 String actualFile2 = files[threadTrack + 1].getName();
 assertTrue(expectedFile1.equals(actualFile1) || expectedFile1.equals(actualFile2));
 assertTrue(expectedFile2.equals(actualFile1) || expectedFile2.equals(actualFile2));
 File errorLogFile1 = new File(outputDir, "log/transcode.log");
 File errorLogFile2 = new File(outputDir, "log/transcode.err");
 assertTrue(errorLogFile1.isFile());
 assertTrue(errorLogFile2.isFile());
 }
}
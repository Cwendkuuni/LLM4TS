import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import java.io.File;
import java.util.Scanner;

public class ConvertTest {

 private Convert convert;
 private String outputDir;

 @Before
 public void setup() {
 outputDir = "test_output";
 File dir = new File(outputDir);
 if (!dir.exists()) {
 dir.mkdir();
 }
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 convert.init();
 }

 @Test
 public void testInit_ThreadDisabled() {
 assertFalse(convert.init());
 File logFile = new File(outputDir + "/log/transcode.log");
 assertTrue(logFile.exists());
 File errorLogFile = new File(outputDir + "/log/transcode.err");
 assertTrue(errorLogFile.exists());
 }

 @Test
 public void testInit_ThreadEnabled() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return true;
 }
 });
 convert.init();
 assertTrue(convert.m_Thread.isAlive());
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testTranscode_NonStandardFile() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 File[] files = new File(outputDir + "/dv").listFiles();
 convert.video_files = new String[files.length + 1];
 convert.video_files[0] = "non_standard_file.txt";
 convert.Transcode();
 }

 @Test
 public void testTranscode_Success() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 convert.init();
 File[] files = new File(outputDir + "/dv").listFiles();
 for (int i = 0; i < files.length; i++) {
 convert.video_files = new String[files.length];
 convert.video_files[i] = files[i].getName();
 convert.Transcode();
 }
 Scanner scanner = new Scanner(new File(outputDir + "/log/transcode.log"));
 String line = scanner.nextLine();
 assertEquals("encoding", line);
 scanner.close();
 }

 @Test
 public void testTranscode_Error() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return true;
 }
 });
 convert.init();
 File[] files = new File(outputDir + "/dv").listFiles();
 for (int i = 0; i < files.length; i++) {
 convert.video_files = new String[files.length];
 convert.video_files[i] = files[i].getName();
 convert.Transcode();
 }
 File logFile = new File(outputDir + "/log/transcode.log");
 File errLogFile = new File(outputDir + "/log/transcode.err");
 assertTrue(logFile.exists());
 assertTrue(errLogFile.exists());
 String logContent = new String(Files.readAllBytes(logFile.toPath()));
 String errContent = new String(Files.readAllBytes(errLogFile.toPath()));
 assertTrue(logContent.contains("Could not locate transcode in your path."));
 assertTrue(errContent.contains("Could not locate transcode in your path."));
 }
}
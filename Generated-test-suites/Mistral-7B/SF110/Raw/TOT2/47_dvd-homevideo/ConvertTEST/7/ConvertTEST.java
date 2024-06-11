import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ConvertTest {

 private Convert convert;
 private String outputDir;

 @Before
 public void setUp() {
 outputDir = "testOutputDir"; // Set up output directory for testing
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return true;
 }
 });
 }

 @Test
 public void testInit_ThreadRunning() {
 convert.init();
 assertTrue(convert.m_Thread.isAlive());
 }

 @Test(expected = InterruptedException.class)
 public void testInit_ThreadInterrupted() throws InterruptedException {
 convert.init();
 convert.m_Thread.interrupt();
 }

 @Test(expected = IOException.class)
 public void testInit_FailsOnMissingTranscode() {
 System.setProperty("path.to.transcode", ""); // Remove transcode from path
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return true;
 }
 });
 convert.init();
 fail("Should have thrown IOException on missing transcode command");
 }

 @Test
 public void testTranscode_SingleFile() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 String[] videoFiles = { "test1.dv" };
 convert.transcode(videoFiles);
 File logFile = new File(outputDir + "/log/transcode.log");
 assertTrue(logFile.exists());
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testTranscode_NonStandardFileFormat() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 File[] listDir = { new File("testOutputDir/dv/testNonStandard.txt") };
 convert.transcode(listDir);
 }

 @Test
 public void testTranscode_MultipleFiles() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 String[] videoFiles = { "test1.dv", "test2.dv" };
 convert.transcode(videoFiles);
 File[] filesInOutputDir = new File(outputDir).listFiles();
 assertEquals(2, filesInOutputDir.length);
 }

 @Test
 public void testMatrix_CreatesMatrixFile() throws IOException {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 File matrixFile = new File(outputDir + "/matrix.txt");
 convert.matrix();
 assertTrue(matrixFile.exists());
 }

 @Test(expected = IOException.class)
 public void testMatrix_FailsOnIOError() throws IOException {
 System.setProperty("user.dir", "testOutputDir"); // Change working directory to output directory
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 convert.matrix();
 File matrixFile = new File(outputDir + "/matrix.txt");
 matrixFile.delete(); // Delete matrix file
 convert.matrix();
 fail("Should have thrown IOException on file delete failure");
 }

}
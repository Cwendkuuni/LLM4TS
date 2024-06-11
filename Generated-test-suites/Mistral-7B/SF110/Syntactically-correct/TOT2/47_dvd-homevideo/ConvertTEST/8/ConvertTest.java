import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;

public class ConvertTest {

 private Convert convert;
 private String[] videoFiles;

 @Before
 public void setUp() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() { /* stub */ });
 videoFiles = new File("testDirectory/dv").listFiles();
 }

 @Test
 public void testInit() {
 convert.init();
 assertFalse(convert.m_Error);
 }

 @Test
 public void testInitWithoutThreadCheckboxSelected() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() { /* stub */ }, false);
 convert.init();
 assertTrue(convert.m_Error || convert.m_GUI.ErrorCheck(convert.m_GUI.strOutputDir + "/log/transcode.log") || convert.m_GUI.ErrorCheck(convert.m_GUI.strOutputDir + "/log/mplex.log"));
 }

 @Test
 public void testTranscode() {
 convert.Transcode();
 assertTrue(new File(convert.m_GUI.strOutputDir + "/log/transcode.log").exists());
 assertTrue(new File(convert.m_GUI.strOutputDir + "/log/mplex.log").exists());
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testTranscodeWithNonStandardFiles() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() { /* stub */ });
 convert.thread_track = 1;
 convert.video_files = Arrays.copyOfRange(videoFiles, 1, videoFiles.length);
 convert.Transcode();
 fail("Should have thrown StringIndexOutOfBoundsException");
 }

 @Test
 public void testMatrix() {
 convert.matrix();
 File matrixFile = new File(convert.m_GUI.strOutputDir + "/matrix.txt");
 assertTrue(matrixFile.exists());
 assertTrue(new File(matrixFile.getAbsolutePath()).length() > 0);
 }

 @Test(expected = IOException.class)
 public void testMatrixFileWriteFailure() {
 try {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() { /* stub */ });
 convert.matrix();
 fail("Should have thrown IOException");
 }
 catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testRun() {
 convert.run();
 fail("run() should not be called directly");
 }

}
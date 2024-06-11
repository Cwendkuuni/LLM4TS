import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.Arrays;

public class ConvertTest {

 private Convert convert;
 private File outputDir;

 @Before
 public void setUp() {
 outputDir = new File("target/test-outputs");
 convert = new Convert("dvd-ntsc", "16:9", new GUI() {
 @Override
 public boolean menuChkThread() {
 return true;
 }
 });
 convert.init();
 }

 @Test
 public void testInitWithMenuChkThreadSelected() {
 assertTrue(convert.init());
 assertTrue(new File(outputDir, "log/transcode.log").exists());
 assertTrue(new File(outputDir, "log/mplex.log").exists());
 }

 @Test
 public void testInitWithoutMenuChkThreadSelected() {
 convert = new Convert(0, "dvd-ntsc", "16:9", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 convert.init();
 assertTrue(convert.m_Error || new File(outputDir, "log/transcode.log").exists() || new File(outputDir, "log/mplex.log").exists());
 }

 @Test
 public void testInitWithInvalidFormat() {
 convert = new Convert(0, "invalidFormat", "16:9", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 assertFalse(convert.init());
 assertEquals("Transcoding Error - Could not locate transcode in your path.\n", convert.m_GUI.getMessage());
 }

 @Test
 public void testTranscodeWithValidFiles() {
 File[] files = outputDir.listFiles();
 Arrays.sort(files);
 File[] expectedFiles = { new File(outputDir, "inp.vob"), new File(outputDir, "test1.m2v"), new File(outputDir, "test1.ac3") };
 assertArrayEquals(expectedFiles, files);
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testTranscodeWithNonStandardFile() {
 convert = new Convert(0, "dvd-ntsc", "16:9", new GUI() {
 @Override
 public boolean menuChkThread() {
 return true;
 }
 });
 File nonStandardFile = new File(outputDir, "nonStandardFile.txt");
 convert.video_files = new File[2];
 convert.video_files[0] = new File("dv/testFile1.dv");
 convert.video_files[1] = nonStandardFile;
 assertFalse(convert.init());
 }

 @Test
 public void testTranscodeWithErrorDuringExecution() {
 convert = new Convert(0, "dvd-ntsc", "16:9", new GUI() {
 @Override
 public boolean menuChkThread() {
 return true;
 }
 });
 String transcodeCommand = convert.m_transcode;
 convert.m_transcode = transcodeCommand.replaceAll("transcode", "transcodeError");
 assertFalse(convert.init());
 assertEquals("Transcoding Error - Could not execute transcoding command.\n", convert.m_ErrorMessage);
 }

}
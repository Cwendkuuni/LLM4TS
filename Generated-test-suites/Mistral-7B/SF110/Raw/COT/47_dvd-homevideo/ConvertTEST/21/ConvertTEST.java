import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.Arrays;

public class ConvertTest {

 private Convert convert;

 @Test
 public void testConvertConstructorWithFormatDvdNtsc() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 assertEquals(29.97, convert.m_fps, 0.01);
 }

 @Test
 public void testConvertConstructorWithFormatOtherThanDvdNtsc() {
 convert = new Convert("otherFormat", "16:9", new GUI());
 assertEquals(25.0, convert.m_fps, 0.01);
 }

 @Test
 public void testInitWithThreadCheckboxNotSelected() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 assertFalse(convert.init());
 }

 @Test
 public void testInitWithThreadCheckboxSelected() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 assertTrue(convert.init());
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testTranscodeWithNonStandardFileInDvDirectory() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 File listDir = new File(convert.m_GUI.strOutputDir + "/dv/");
 String[] videoFiles = listDir.list();
 String nonStandardFile = "nonStandard.txt";
 convert.video_files = Arrays.copyOfRange(videoFiles, 0, 1);
 convert.thread_track = 0;
 convert.m_transcode = convert.m_transcode.replaceAll("inp", convert.video_files[0]);
 convert.m_transcode = convert.m_transcode.replaceAll("test1", convert.video_files[0].substring(0, 11));
 convert.mplex = convert.mplex.replaceAll("inp", convert.video_files[0].substring(0, 11));
 try {
 convert.Transcode();
 fail("Should have thrown StringIndexOutOfBoundsException");
 }
 catch (Exception e) {
 assertTrue(e instanceof StringIndexOutOfBoundsException);
 }
 }

 @Test
 public void testTranscodeWithValidFiles() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 File listDir = new File(convert.m_GUI.strOutputDir + "/dv/");
 String[] videoFiles = listDir.list();
 convert.video_files = Arrays.copyOfRange(videoFiles, 0, 5);
 convert.thread_track = 0;
 convert.m_transcode = convert.m_transcode.replaceAll("inp", videoFiles[0]);
 convert.m_transcode = convert.m_transcode.replaceAll("test1", videoFiles[0].substring(0, 11));
 convert.mplex = convert.mplex.replaceAll("inp", videoFiles[0].substring(0, 11));
 try {
 convert.Transcode();
 }
 catch (Exception e) {
 fail("Should not have thrown an exception");
 }
 }

 @Test
 public void testMatrixCreation() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 try {
 convert.matrix();
 }
 catch (IOException e) {
 fail("Should not have thrown an IOException");
 }
 }

}
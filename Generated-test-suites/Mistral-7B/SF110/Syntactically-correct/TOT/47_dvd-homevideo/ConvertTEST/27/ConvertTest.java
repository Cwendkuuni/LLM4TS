import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ConvertTest {

 private Convert convert;
 private String strOutputDir;

 @Before
 public void setUp() {
 strOutputDir = "test_output";
 File dir = new File(strOutputDir);
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

 @After
 public void tearDown() {
 if (convert != null) {
 convert.m_Error = true;
 }
 File dir = new File(strOutputDir);
 if (dir.exists()) {
 dir.delete();
 }
 }

 @Test
 public void testInit_NoThread() {
 convert = new Convert(0, "dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 convert.init();
 assertTrue(convert.m_Error || convert.m_GUI.ErrorCheck(strOutputDir + "/log/transcode.log") || convert.m_GUI.ErrorCheck(strOutputDir + "/log/mplex.log"));
 }

 @Test
 public void testInit_Thread() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return true;
 }
 });
 convert.init();
 assertFalse(convert.m_Error || convert.m_GUI.ErrorCheck(strOutputDir + "/log/transcode.log") || convert.m_GUI.ErrorCheck(strOutputDir + "/log/mplex.log"));
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testTranscode_NonStandardFile() {
 convert.thread_track = 0;
 File ListDir = new File(strOutputDir + "/dv/");
 String[] video_files = ListDir.list();
 convert.video_files = Arrays.copyOfRange(video_files, 0, 1);
 convert.Transcode();
 }

 @Test
 public void testTranscode_Success() {
 convert.thread_track = 0;
 File ListDir = new File(strOutputDir + "/dv/");
 convert.video_files = ListDir.list();
 String inputFile = convert.video_files[0];
 convert.m_transcode = convert.m_transcode.replaceAll("inp", inputFile.substring(0, 11));
 convert.m_transcode = convert.m_transcode.replaceAll("test1", inputFile.substring(0, 11));
 convert.mplex = convert.mplex.replaceAll("inp", inputFile.substring(0, 11));
 convert.mplex = convert.mplex.replaceAll("test1", inputFile.substring(0, 11));
 convert.Transcode();
 assertTrue(convert.m_Error || convert.m_GUI.ErrorCheck(strOutputDir + "/log/transcode.log") || convert.m_GUI.ErrorCheck(strOutputDir + "/log/mplex.log"));
 }

 @Test
 public void testTranscode_CommandNotFound() {
 convert.thread_track = 0;
 File ListDir = new File(strOutputDir + "/dv/");
 convert.video_files = ListDir.list();
 convert.m_transcode = convert.m_transcode.replaceAll("transcode", "invalid_command");
 convert.Transcode();
 assertFalse(convert.m_Error);
 assertTrue(convert.m_GUI.MessageBoxCalled);
 assertEquals("Could not locate transcode in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo.", convert.m_GUI.lastMessage);
 }
}
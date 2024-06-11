import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.Arrays;

public class ConvertTest {

 private Convert convert;
 private String outputDir;

 @Before
 public void setUp() {
 outputDir = "target/test-output";
 System.setProperty("user.dir", outputDir);
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 }

 @After
 public void tearDown() {
 new File(outputDir).deleteOnExit();
 }

 @Test
 public void init_noThread_validArgs() {
 convert.init();
 assertFalse(convert.m_Error);
 }

 @Test
 public void init_noThread_invalidArgs() {
 convert = new Convert("", "", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 assertTrue(convert.m_Error);
 }

 @Test
 public void init_thread_validArgs() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return true;
 }
 }, 1);
 convert.init();
 assertFalse(convert.m_Error);
 }

 @Test
 public void init_thread_invalidArgs() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return true;
 }
 }, -1);
 assertTrue(convert.m_Error);
 }

 @Test
 public void init_thread_commandNotFound() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return true;
 }
 });
 System.setProperty("path.separator", File.pathSeparator);
 System.setProperty("os.name", "Windows 10");
 convert.init();
 assertTrue(convert.m_Error);
 }

 @Test
 public void init_thread_ioError() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return true;
 }
 });
 try {
 File outputDir = new File("./test-output");
 File file = new File(outputDir, "log/transcode.log");
 OutputStream outputStream = new ByteArrayOutputStream();
 FileOutputStream logStream = new FileOutputStream(file);
 logStream.write("Error".getBytes());
 logStream.close();
 when(convert.m_GUI.strOutputDir).thenReturn(outputDir.getAbsolutePath());
 when(convert.m_GUI.ErrorCheck(anyString())).thenThrow(new IOException("Test error"));
 }
 catch (IOException e) {
 // expected
 }
 convert.init();
 assertTrue(convert.m_Error);
 }

 @Test
 public void transcode_validArgs() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 String[] files = {"input1.dv", "input2.dv", "input3.dv"};
 convert.video_files = files;
 convert.Transcode();
 assertFalse(convert.m_Error);
 for (String file : files) {
 assertTrue(new File(outputDir, file + ".vob").exists());
 }
 }

 @Test
 public void transcode_invalidArgs() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 String[] files = {"input1.dv", "input2.dv", "input3.dv", "input4.dv"};
 convert.video_files = files;
 convert.Transcode();
 assertTrue(convert.m_Error);
 }

 @Test
 public void transcode_ioError() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 try {
 File file = new File(outputDir, "log/transcode.log");
 FileOutputStream logStream = new FileOutputStream(file);
 logStream.write("Error".getBytes());
 logStream.close();
 File outputDir = new File("./test-output");
 when(convert.m_GUI.strOutputDir).thenReturn(outputDir.getAbsolutePath());
 when(convert.m_GUI.ErrorCheck(anyString())).thenThrow(new IOException("Test error"));
 }
 catch (IOException e) {
 // expected
 }
 convert.Transcode();
 assertTrue(convert.m_Error);
 }

 @Test
 public void transcode_commandNotFound() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 System.setProperty("path.separator", File.pathSeparator);
 System.setProperty("os.name", "Windows 10");
 try {
 String[] transcodeCmd = {"/bin/sh", "-c", convert.m_transcode.replaceAll("inp", "input1.dv")};
 Process process = RT.runCommand(transcodeCmd);
 process.waitFor();
 fail("Process should not have run");
 }
 catch (IOException | InterruptedException e) {
 // expected
 }
 convert.Transcode();
 assertTrue(convert.m_Error);
 }
}
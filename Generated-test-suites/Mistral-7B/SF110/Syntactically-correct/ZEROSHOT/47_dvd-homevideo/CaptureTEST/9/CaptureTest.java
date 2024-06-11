import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class CaptureTest {

 private Capture capture;
 private GUI guiMock;

 @Before
 public void setup() {
 guiMock = new GUI() {
 @Override
 public boolean menuChkThreadIsSelected() {
 return false;
 }

 @Override
 public int messageBox(String message, int button) {
 return 0;
 }

 @Override
 public String strOutputDir() {
 return "output_dir";
 }
 };
 capture = new Capture(0, 0, guiMock);
 }

 @After
 public void teardown() {
 capture = null;
 guiMock = null;
 }

 @Test
 public void testInitNoThread() {
 assertFalse(capture.init());
 assertTrue(capture.m_Error);
 }

 @Test
 public void testInitWithThread() {
 capture = new Capture(1, 1, guiMock);
 capture.m_Thread.interrupt();
 assertTrue(capture.init());
 assertFalse(capture.m_Error);
 }

 @Test(expected = NullPointerException.class)
 public void testInitNullGUI() {
 capture = new Capture(1, 1, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitNegativeMinutes() {
 capture = new Capture(-1, 1, guiMock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitNegativeSeconds() {
 capture = new Capture(1, -1, guiMock);
 }

 @Test
 public void testDV_CaptureValidDirectory() throws IOException, InterruptedException {
 File outputDir = new File("output_dir");
 capture = new Capture(1, 1, guiMock);
 capture.DV_Capture();
 assertFalse(capture.m_Error);
 File expectedOutputDir = new File("output_dir");
 assertTrue(expectedOutputDir.exists() && expectedOutputDir.isDirectory());
 File expectedCaptureDir = new File("output_dir/DVD");
 assertTrue(expectedCaptureDir.exists() && expectedCaptureDir.isDirectory());
 File expectedLogDir = new File("output_dir/log");
 assertTrue(expectedLogDir.exists() && expectedLogDir.isDirectory());
 File expectedCaptureFile = new File("output_dir/dv/dv_file-");
 assertFalse(expectedCaptureFile.exists());
 }

 @Test
 public void testDV_CaptureInvalidDirectory() throws IOException, InterruptedException {
 File outputDir = new File("output_dir");
 capture = new Capture(1, 1, guiMock);
 capture.m_GUI = new GUI() {
 @Override
 public boolean menuChkThreadIsSelected() {
 return true;
 }
 };
 capture.DV_Capture();
 assertTrue(capture.m_Error);
 File outputDirFile = new File("output_dir");
 assertFalse(outputDirFile.exists() || outputDirFile.isDirectory());
 }

 @Test
 public void testDV_CaptureFileExists() throws IOException, InterruptedException {
 File outputDir = new File("output_dir");
 File file = new File("output_dir/file.txt");
 capture = new Capture(1, 1, guiMock);
 capture.DV_Capture();
 assertTrue(capture.m_Error);
 File outputDirFile = new File("output_dir");
 assertFalse(outputDirFile.exists() || outputDirFile.isDirectory());
 File fileExists = new File("output_dir/file.txt");
 assertTrue(fileExists.exists());
 }

 @Test
 public void testDV_CaptureRun() throws IOException, InterruptedException {
 File outputDir = new File("output_dir");
 capture = new Capture(1, 1, guiMock);
 capture.m_Thread = new Thread(capture);
 capture.m_Thread.start();
 capture.m_Thread.join();
 assertFalse(capture.m_Error);
 File expectedOutputDir = new File("output_dir");
 assertTrue(expectedOutputDir.exists() && expectedOutputDir.isDirectory());
 File expectedCaptureDir = new File("output_dir/DVD");
 assertTrue(expectedCaptureDir.exists() && expectedCaptureDir.isDirectory());
 File expectedLogDir = new File("output_dir/log");
 assertTrue(expectedLogDir.exists() && expectedLogDir.isDirectory());
 File expectedCaptureFile = new File("output_dir/dv/dv_file-");
 assertFalse(expectedCaptureFile.exists());
 }

 @Test(expected = IOException.class)
 public void testDV_CaptureInvalidCommand() throws IOException, InterruptedException {
 capture = new Capture(1, 1, guiMock);
 capture.m_dvgrab = "invalid_command";
 capture.DV_Capture();
 assertTrue(capture.m_Error);
 }
}
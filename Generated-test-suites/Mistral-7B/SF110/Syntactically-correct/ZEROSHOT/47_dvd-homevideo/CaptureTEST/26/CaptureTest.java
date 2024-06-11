import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.lang.NullPointerException;
import java.lang.reflect.InvocationTargetException;

public class CaptureTest {

 private Capture capture;
 private String outputDir;

 @Before
 public void setUp() {
 outputDir = "test_output_dir";
 capture = new Capture(1, 2, new GUI() {
 @Override
 public boolean menuChkThreadIsSelected() {
 return false;
 }

 @Override
 public String strOutputDir() {
 return outputDir;
 }

 @Override
 public int MessageBox(String message, int response) {
 return response;
 }
 });
 }

 @After
 public void tearDown() throws IOException {
 File outputDirFile = new File(outputDir);
 if (outputDirFile.exists() && outputDirFile.isDirectory()) {
 outputDirFile.delete();
 }
 }

 @Test
 public void testInitWithThreadDisabled() {
 assertTrue(capture.init());
 assertEquals(0, capture.m_GUI.MessageBoxCallCount);
 }

 @Test(expected = NullPointerException.class)
 public void testInitWithNullGUI() {
 capture = new Capture(1, 2, null);
 capture.init();
 fail("Init should have thrown a NullPointerException");
 }

 @Test(expected = IOException.class)
 public void testInitWithInvalidOutputDirectory() {
 capture = new Capture(1, 2, new GUI() {
 @Override
 public String strOutputDir() {
 return "/non/existent/dir";
 }
 });
 capture.init();
 fail("Init should have thrown an IOException");
 }

 @Test
 public void testInitWithValidOutputDirectory() {
 File outputDirFile = new File(outputDir);
 assertTrue(outputDirFile.mkdirs());
 capture = new Capture(1, 2, new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }
 });
 assertTrue(capture.init());
 assertTrue(outputDirFile.exists() && outputDirFile.isDirectory());
 }

 @Test(expected = NullPointerException.class)
 public void testRunWithNullGUI() {
 capture.m_Thread = null;
 capture.run();
 fail("Run should have thrown a NullPointerException");
 }

 @Test
 public void testRun() throws InterruptedException {
 capture.m_Thread = new Thread(capture);
 capture.m_Thread.start();
 capture.m_Thread.join();
 }

 @Test(expected = IOException.class)
 public void testDvCaptureWithInvalidOutputDirectory() throws InterruptedException {
 capture.m_Thread = new Thread(capture);
 capture.m_Thread.start();
 capture.DV_Capture();
 File outputDirFile = new File(outputDir);
 assertFalse(outputDirFile.exists() || outputDirFile.isDirectory());
 }

 @Test
 public void testDvCaptureWithValidOutputDirectory() throws IOException, InterruptedException {
 capture.m_Thread = new Thread(capture);
 capture.m_Thread.start();
 capture.DV_Capture();
 File outputDirFile = new File(outputDir);
 assertTrue(outputDirFile.exists() && outputDirFile.isDirectory());
 }

 @Test(expected = NullPointerException.class)
 public void testDvCaptureWithNullGUI() throws IOException, InterruptedException {
 capture = new Capture(1, 2, null);
 capture.m_Thread = new Thread(capture);
 capture.m_Thread.start();
 capture.DV_Capture();
 fail("DV_Capture should have thrown a NullPointerException");
 }

 @Test(expected = IOException.class)
 public void testDvCaptureWithNoDvgrabFound() throws IOException, InterruptedException {
 File logFile = new File(outputDir + "/log/dvgrab.log");
 capture = new Capture(1, 2, new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }

 @Override
 public int MessageBox(String message, int response) {
 return response;
 }
 });
 capture.m_Thread = new Thread(capture);
 capture.m_Thread.start();
 capture.DV_Capture();
 assertFalse(logFile.exists());
 }

 @Test(expected = IOException.class)
 public void testDvCaptureWithInvalidCommand() throws IOException, InterruptedException {
 capture = new Capture(1, 2, new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }

 @Override
 public String dvgrab() {
 throw new IOException();
 }
 });
 capture.m_Thread = new Thread(capture);
 capture.m_Thread.start();
 capture.DV_Capture();
 fail("DV_Capture should have thrown an IOException");
 }

 @Test
 public void testDvCaptureWithValidCommand() throws IOException, InterruptedException {
 capture = new Capture(1, 2, new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }

 @Override
 public String dvgrab() {
 return "/bin/sh -c dvgrab --autosplit --size 0 --format raw --opendml --buffers 200 dv/dv_file-";
 }
 });
 capture.m_Thread = new Thread(capture);
 capture.m_Thread.start();
 capture.DV_Capture();
 File logFile = new File(outputDir + "/log/dvgrab.log");
 File dvFile = new File(outputDir + "/dv/dv_file-00001.raw");
 assertTrue(logFile.exists());
 assertTrue(dvFile.exists());
 }

 @Test
 public void testMessageBoxWithError() throws IOException, InterruptedException {
 capture = new Capture(1, 2, new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }

 @Override
 public int MessageBox(String message, int response) {
 return 0;
 }

 @Override
 public void ErrorCheck(String errorLogFile) {
 throw new IOException();
 }
 });
 capture.m_Thread = new Thread(capture);
 capture.m_Thread.start();
 capture.DV_Capture();
 File outputDirFile = new File(outputDir);
 assertFalse(outputDirFile.exists() || outputDirFile.isDirectory());
 File errorLogFile = new File(outputDir + "/log/dvgrab.err");
 assertFalse(errorLogFile.exists());
 }

 @Test
 public void testMessageBoxWithWarning() throws IOException, InterruptedException {
 capture = new Capture(1, 2, new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }

 @Override
 public int MessageBox(String message, int response) {
 return 2;
 }

 @Override
 public void ErrorCheck(String errorLogFile) {
 // do nothing
 }
 });
 capture.m_Thread = new Thread(capture);
 capture.m_Thread.start();
 capture.DV_Capture();
 File outputDirFile = new File(outputDir);
 assertTrue(outputDirFile.exists() && outputDirFile.isDirectory());
 File errorLogFile = new File(outputDir + "/log/dvgrab.err");
 assertFalse(errorLogFile.exists());
 }

 @Test
 public void testMessageBoxWithDeleteConfirmation() throws IOException, InterruptedException {
 capture = new Capture(1, 2, new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }

 @Override
 public int MessageBox(String message, int response) {
 return 0;
 }
 });
 File outputDirFile = new File(outputDir);
 assertTrue(outputDirFile.exists() && outputDirFile.isDirectory());
 capture.m_Thread = new Thread(capture);
 capture.m_Thread.start();
 capture.DV_Capture();
 assertTrue(outputDirFile.exists() && outputDirFile.isDirectory());
 }

 @Test
 public void testMessageBoxWithDeleteCancel() throws IOException, InterruptedException {
 capture = new Capture(1, 2, new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }

 @Override
 public int MessageBox(String message, int response) {
 return 1;
 }
 });
 File outputDirFile = new File(outputDir);
 assertTrue(outputDirFile.exists() && outputDirFile.isDirectory());
 capture.m_Thread = new Thread(capture);
 capture.m_Thread.start();
 capture.DV_Capture();
 assertTrue(outputDirFile.exists() && outputDirFile.isDirectory());
 }

 @Test
 public void testMessageBoxWithDeleteFailure() throws IOException, InterruptedException {
 capture = new Capture(1, 2, new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }

 @Override
 public int MessageBox(String message, int response) {
 return 0;
 }

 @Override
 public void deleteDirectory(File dir) throws IOException {
 throw new IOException();
 }
 });
 File outputDirFile = new File(outputDir);
 assertTrue(outputDirFile.exists() && outputDirFile.isDirectory());
 capture.m_Thread = new Thread(capture);
 capture.m_Thread.start();
 capture.DV_Capture();
 assertTrue(outputDirFile.exists() && outputDirFile.isDirectory());
 }
}
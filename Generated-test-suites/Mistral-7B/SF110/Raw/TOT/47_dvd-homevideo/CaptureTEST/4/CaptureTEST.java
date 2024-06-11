import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CaptureTest {

 private Capture capture;
 private GUI mockGUI;

 @Before
 public void setup() {
 mockGUI = Mockito.mock(GUI.class);
 capture = new Capture(0, 0, mockGUI);
 }

 @After
 public void teardown() {
 capture = null;
 mockGUI = null;
 }

 @Test
 public void testInit_OutputDirectoryAlreadyExists_DeleteIt() throws InterruptedException, IOException {
 File outputDir = new File("outputDir");
 outputDir.mkdir();
 capture = new Capture(0, 0, mockGUI);
 Mockito.when(mockGUI.MessageBox(Mockito.anyString(), Mockito.anyInt())).thenReturn(0);
 Mockito.when(mockGUI.strOutputDir).thenReturn("outputDir");

 capture.init();

 Mockito.verify(mockGUI, Mockito.times(1)).MessageBox(Mockito.eq("The Output Directory outputDir already exists!"), Mockito.eq(0));
 Mockito.verify(mockGUI, Mockito.times(1)).MessageBox(Mockito.eq("Please choose a different name for your title or move the directory to another location."), Mockito.eq(2));

 Runtime runtime = Runtime.getRuntime();
 Process process = Mockito.mock(Process.class);
 Mockito.when(runtime.exec(Mockito.anyArray(String.class), Mockito.eq(null), Mockito.eq(new File("outputDir")))).thenReturn(process);

 capture.init();

 Mockito.verify(process, Mockito.times(1)).waitFor();
 outputDir.deleteRecursively();
 }

 @Test
 public void testInit_OutputDirectoryDoesNotExist_CreateIt() throws InterruptedException, IOException {
 File outputDir = new File("outputDir");
 capture = new Capture(0, 0, mockGUI);
 Mockito.when(mockGUI.MessageBox(Mockito.anyString(), Mockito.anyInt())).thenReturn(0);
 Mockito.when(mockGUI.strOutputDir).thenReturn("outputDir");

 capture.init();

 Mockito.verify(mockGUI, Mockito.times(1)).MessageBox(Mockito.eq("Please choose a name for your title."), Mockito.eq(1));
 File outputDirCreated = new File("outputDir");
 File outputDirDVD = new File(outputDir.getAbsolutePath() + "/DVD");
 File outputDirLog = new File(outputDir.getAbsolutePath() + "/log");
 File outputDirDv = new File(outputDir.getAbsolutePath() + "/dv");

 Assert.assertTrue("Output directory was not created", outputDir.exists() && outputDir.isDirectory());
 Assert.assertTrue("DVD directory was not created", outputDirDVD.exists() && outputDirDVD.isDirectory());
 Assert.assertTrue("Log directory was not created", outputDirLog.exists() && outputDirLog.isDirectory());
 Assert.assertTrue("dv directory was not created", outputDirDv.exists() && outputDirDv.isDirectory());
 }

 @Test(expected = IOException.class)
 public void testInit_OutputDirectoryExistsButIsNotADirectory_ThrowException() throws InterruptedException {
 File outputDir = new File("outputDir");
 outputDir.createNewFile();
 capture = new Capture(0, 0, mockGUI);
 Mockito.when(mockGUI.MessageBox(Mockito.anyString(), Mockito.anyInt())).thenReturn(0);
 Mockito.when(mockGUI.strOutputDir).thenReturn("outputDir");

 capture.init();

 Mockito.verify(mockGUI, Mockito.times(1)).MessageBox(Mockito.eq("There exists a file with the Output directory name at this location!  Would you like to delete it?"), Mockito.eq(0));
 }

 @Test(expected = NullPointerException.class)
 public void testDV_Capture_OutputDirectoryIsNull() throws InterruptedException, IOException {
 capture = new Capture(0, 0, null);
 capture.DV_Capture();
 }

 @Test(expected = IllegalStateException.class)
 public void testDV_Capture_ThreadIsRunning() throws InterruptedException, IOException {
 capture = new Capture(0, 0, mockGUI);
 Mockito.when(mockGUI.menuChkThread.isSelected()).thenReturn(true);
 capture.init();
 capture.DV_Capture();
 }

 @Test
 public void testDV_Capture_NoCameraConnected_ShowMessageBox() throws InterruptedException, IOException {
 capture = new Capture(0, 0, mockGUI);
 Mockito.when(mockGUI.MessageBox(Mockito.anyString(), Mockito.anyInt())).thenReturn(0);
 Mockito.when(mockGUI.strOutputDir).thenReturn("outputDir");

 capture.init();
 Mockito.when(mockGUI.MessageBox(Mockito.anyString(), Mockito.anyInt())).thenReturn(0);

 capture.DV_Capture();

 Mockito.verify(mockGUI, Mockito.times(1)).MessageBox(Mockito.eq("It appears that your camcorder is not connected to your computer!"), Mockito.eq(0));
 }

 @Test
 public void testDV_Capture_SuccessfulCapture() throws InterruptedException, IOException {
 capture = new Capture(0, 0, mockGUI);
 Mockito.when(mockGUI.MessageBox(Mockito.anyString(), Mockito.anyInt())).thenReturn(0);
 Mockito.when(mockGUI.strOutputDir).thenReturn("outputDir");

 capture.init();
 capture.DV_Capture();

 File outputDir = new File("outputDir");
 File outputDirDVD = new File(outputDir.getAbsolutePath() + "/DVD");
 File outputDirLog = new File(outputDir.getAbsolutePath() + "/log");
 File outputDirDv = new File(outputDir.getAbsolutePath() + "/dv");

 Assert.assertFalse("Capture should not be enabled after capture", capture.isEnabled());
 Assert.assertFalse("Capture progress label should not be enabled after capture", capture.isCaptureProgressLabelEnabled());
 Assert.assertFalse("Capture button should not be enabled after capture", capture.isCaptureButtonEnabled());

 Assert.assertTrue("Output directory was not created", outputDir.exists() && outputDir.isDirectory());
 Assert.assertTrue("DVD directory was created", outputDirDVD.exists() && outputDirDVD.isDirectory());
 Assert.assertTrue("Log directory was created", outputDirLog.exists() && outputDirLog.isDirectory());
 Assert.assertTrue("dv directory was created", outputDirDv.exists() && outputDirDv.isDirectory());
 }

 @Test
 public void testDV_Capture_LogFileIsWriten() throws InterruptedException, IOException {
 capture = new Capture(0, 0, mockGUI);
 Mockito.when(mockGUI.MessageBox(Mockito.anyString(), Mockito.anyInt())).thenReturn(0);
 Mockito.when(mockGUI.strOutputDir).thenReturn("outputDir");

 capture.init();
 capture.DV_Capture();

 File logFile = new File(new File("outputDir"), "log/dvgrab.log");

 Assert.assertTrue("Log file was not written", logFile.exists() && logFile.isFile());
 }
}
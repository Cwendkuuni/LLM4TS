import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class CaptureTest {

 private Capture capture;
 private GUI guiMock;

 @Before
 public void setUp() {
 guiMock = mock(GUI.class);
 capture = new Capture(1, 2, guiMock);
 }

 @Test
 public void testInit_ThreadRunning_ReturnsFalse() {
 capture.m_GUI.menuChkThread.setSelected(true);
 assertFalse(capture.init());
 }

 @Test
 public void testInit_ThreadNotRunning_ReturnsTrueAndCallsDV_Capture() throws IOException, InterruptedException {
 capture.m_GUI.menuChkThread.setSelected(false);
 Mockito.doNothing().when(guiMock).MessageBox(Mockito.anyString(), Mockito.eq(0));
 Mockito.doNothing().when(guiMock).MessageBox(Mockito.anyString(), Mockito.eq(1));
 Mockito.doNothing().when(guiMock).MessageBox(Mockito.anyString(), Mockito.anyInt());
 Mockito.doNothing().when(guiMock).ErrorCheck(Mockito.eq(guiMock.strOutputDir + "/log/dvgrab.log"));
 Mockito.doNothing().when(guiMock).blnBegin = false;

 ArgumentCaptor<String> logFileCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<String[]> cmdCaptor = ArgumentCaptor.forClass(String[].class);

 verify(guiMock, never()).MessageBox(this.m_BaseErr + "IO Error\n", 0);
 verify(guiMock, never()).MessageBox(this.m_BaseErr + "dvd-homevideo thread was interrupted\n", 0);
 verify(guiMock, never()).MessageBox(this.m_BaseErr + "Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo.", 0);
 verify(guiMock, never()).MessageBox("The Output Directory " + guiMock.strOutputDir + " already exists!" + "\nWould you like to delete it?", 0);
 verify(guiMock, never()).MessageBox("There exists a file with the Output directory name\nat this location!  Would you like to delete it?", 0);
 verify(guiMock, never()).MessageBox("Please choose a different name for your\ntitle or move the directory to another location.", 2);
 verify(guiMock, never()).blnBegin = false;

 capture.init();
 verify(guiMock).MessageBox("Please choose a different name for your\ntitle or move the directory to another location.", 2);

 capture.DV_Capture();
 verify(guiMock).MessageBox(this.m_BaseErr + "IO Error\n", 0);

 File outputDir = new File(guiMock.strOutputDir);
 assertTrue(outputDir.exists());
 assertTrue(outputDir.isDirectory());

 capture.init();
 verify(guiMock).MessageBox("The Output Directory " + guiMock.strOutputDir + " already exists!" + "\nWould you like to delete it?", 0);
 outputDir.delete();

 capture.init();
 verify(guiMock).MessageBox("Please choose a different name for your\ntitle or move the directory to another location.", 2);
 }

 @Test(expected = IOException.class)
 public void testInit_IOExceptionThrown() throws IOException, InterruptedException {
 capture.m_GUI.menuChkThread.setSelected(false);
 Mockito.doThrow(new IOException("Test IOException")).when(guiMock).MessageBox(Mockito.anyString(), Mockito.eq(0));
 capture.init();
 }

 @Test(expected = InterruptedException.class)
 public void testInit_InterruptedExceptionThrown() throws IOException, InterruptedException {
 capture.m_GUI.menuChkThread.setSelected(false);
 Mockito.doThrow(new InterruptedException("Test InterruptedException")).when(guiMock).MessageBox(Mockito.anyString(), Mockito.eq(0));
 capture.init();
 }

 @Test
 public void testDV_Capture_OutputDirExistsAsFile_DisplaysErrorMessage() throws IOException, InterruptedException {
 capture.m_GUI.menuChkThread.setSelected(false);
 File outputDir = new File(guiMock.strOutputDir);
 outputDir.createNewFile();
 capture.DV_Capture();
 verify(guiMock).MessageBox(this.m_BaseErr + "IO Error\n", 0);
 }

 @Test
 public void testDV_Capture_OutputDirExistsAsDirectory_CreatesDirectories() throws IOException, InterruptedException {
 capture.m_GUI.menuChkThread.setSelected(false);
 File outputDir = new File(guiMock.strOutputDir);
 outputDir.mkdirs();

 capture.DV_Capture();
 verify(guiMock).MessageBox(this.m_BaseErr + "dvgrab: command not found", 0);

 outputDir.deleteRecursively();
 }

 @Test
 public void testDV_Capture_InputFileExists_DisplaysErrorMessage() throws IOException, InterruptedException {
 capture.m_GUI.menuChkThread.setSelected(false);
 File outputDir = new File(guiMock.strOutputDir);
 outputDir.mkdirs();

 File inputFile = new File(guiMock.strOutputDir + "/inputFile.txt");
 inputFile.createNewFile();
 capture.DV_Capture();
 verify(guiMock).MessageBox("There exists a file with the Output directory name\nat this location!  Would you like to delete it?", 0);
 inputFile.delete();
 }

 @Test
 public void testDV_Capture_OutputDirAndInputFileExist_CreatesCaptureFile() throws IOException, InterruptedException {
 capture.m_GUI.menuChkThread.setSelected(false);
 File outputDir = new File(guiMock.strOutputDir);
 outputDir.mkdirs();

 File inputFile = new File(guiMock.strOutputDir + "/inputFile.txt");
 inputFile.createNewFile();

 capture.DV_Capture();
 verify(guiMock).MessageBox("Please choose a different name for your\ntitle or move the directory to another location.", 2);
 verify(guiMock).MessageBox("The Output Directory " + guiMock.strOutputDir + " already exists!" + "\nWould you like to delete it?", 0);
 outputDir.delete();

 File captureFile = new File(guiMock.strOutputDir + "/DVD/captureFile.dv");
 captureFile.createNewFile();
 capture.DV_Capture();
 assertNull(captureFile.length());
 captureFile.delete();
 }
}
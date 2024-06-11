import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class CaptureTest {

 private Capture capture;
 private GUI mockGUI;

 @Before
 public void setup() {
 mockGUI = mock(GUI.class);
 capture = new Capture(1, 2, mockGUI);
 }

 @Test
 public void testInit_noThreadSelected() {
 when(mockGUI.menuChkThread.isSelected()).thenReturn(false);
 assertFalse(capture.init());
 }

 @Test
 public void testInit_threadStarted() {
 when(mockGUI.menuChkThread.isSelected()).thenReturn(true);
 capture.DV_Capture();
 verify(mockGUI).MessageBox(capture.m_BaseErr + "dvgrab started.", 0);
 }

 @Test
 public void testInit_outputDirAlreadyExists() {
 File outputDir = new File("existingOutputDir");
 when(mockGUI.strOutputDir).thenReturn(outputDir.getAbsolutePath());
 when(mockGUI.MessageBox(anyString(), eq(0))).thenReturn(JOptionPane.YES_OPTION);
 try {
 File dir = new File(outputDir, "dv");
 dir.mkdirs();
 }
 catch (SecurityException e) {
 fail("Should not throw SecurityException");
 }
 assertFalse(capture.init());
 }

 @Test
 public void testInit_outputFileExists() {
 File outputDir = new File("outputDir");
 File outputFile = new File(outputDir, "outputFile.txt");
 when(mockGUI.strOutputDir).thenReturn(outputDir.getAbsolutePath());
 when(mockGUI.MessageBox(anyString(), eq(2))).thenReturn(JOptionPane.NO_OPTION);
 assertFalse(capture.init());
 }

 @Test
 public void testInit_dvgrabNotFound() {
 when(mockGUI.MessageBox(capture.m_BaseErr + "dvgrab not found.", 0)).thenReturn(JOptionPane.NO_OPTION);
 assertFalse(capture.init());
 }

 @Test
 public void testInit_cameraNotConnected() {
 when(mockGUI.MessageBox("It appears that your camcorder is not connected to your computer!", 0)).thenReturn(JOptionPane.NO_OPTION);
 assertFalse(capture.init());
 }
}
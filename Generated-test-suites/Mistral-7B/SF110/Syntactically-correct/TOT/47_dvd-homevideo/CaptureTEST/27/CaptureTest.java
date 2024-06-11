import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class CaptureTest {

 private Capture capture;
 private GUI mockGUI;

 @Before
 public void setUp() {
 mockGUI = mock(GUI.class);
 capture = new Capture(1, 2, mockGUI);
 }

 @Test
 public void testInit_OutputDirExistsAndIsDirectory_ReturnsFalse() throws IOException, InterruptedException {
 File outputDir = new File("existingOutputDir");
 File outputDirParent = outputDir.getParentFile();
 outputDir.mkdir();
 outputDirParent.mkdir();
 when(mockGUI.strOutputDir).thenReturn(outputDir.getAbsolutePath());
 assertFalse(capture.init());
 }

 @Test
 public void testInit_OutputDirExistsAndIsFile_ThrowsException() throws IOException, InterruptedException {
 File outputFile = new File("existingOutputFile");
 when(mockGUI.strOutputDir).thenReturn(outputFile.getAbsolutePath());
 assertThrows(IllegalArgumentException.class, () -> capture.init());
 }

 @Test
 public void testInit_OutputDirDoesNotExist_ReturnsTrue() {
 when(mockGUI.strOutputDir).thenReturn("nonExistingOutputDir");
 assertTrue(capture.init());
 }

 @Test
 public void testInit_dvgrabCommandNotfound_ErrorMessageDisplayed() throws IOException, InterruptedException {
 when(mockGUI.MessageBox(anyString(), eq(0))).thenReturn(0);
 when(mockGUI.MessageBox(anyString(), eq(2))).thenReturn(-1);
 doThrow(IOException.class).when(Runtime.getRuntime()).exec(anyStringArray());
 assertFalse(capture.init());
 }

 @Test
 public void testDV_Capture_dvgrabSuccess() throws IOException, InterruptedException {
 when(mockGUI.MessageBox(anyString(), eq(0))).thenReturn(0);
 doReturn(new String("dvgrab successful\n")).when(mockGUI).txtAreaOutput;
 capture.DV_Capture();
 assertTrue(capture.m_Error);
 assertEquals("Capture successful\ndvgrab successful\n", capture.m_GUI.txtAreaOutput.getText());
 }

 @Test
 public void testDV_Capture_dvgrabError() throws IOException, InterruptedException {
 when(mockGUI.MessageBox(anyString(), eq(0))).thenReturn(0);
 doThrow(IOException.class).when(Runtime.getRuntime()).exec(anyStringArray());
 capture.DV_Capture();
 assertTrue(capture.m_Error);
 assertFalse(capture.m_GUI.blnBegin);
 }

 @Test(expected = NullPointerException.class)
 public void testDV_Capture_guiIsNull() {
 capture = new Capture(1, 2, null);
 capture.DV_Capture();
 }
}
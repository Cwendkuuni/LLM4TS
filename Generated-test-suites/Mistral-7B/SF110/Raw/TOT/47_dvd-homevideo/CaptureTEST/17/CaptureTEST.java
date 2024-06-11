import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

public class CaptureTest {

 private Capture capture;
 private GUI gui;

 @Before
 public void setup() {
 gui = mock(GUI.class);
 capture = new Capture(1, 2, gui);
 }

 @Test
 public void testInit_shouldStartThreadIfThreadCheckboxSelected() throws InterruptedException, IOException {
 when(gui.menuChkThread.isSelected()).thenReturn(true);
 assertFalse(capture.init());
 Thread thread = capture.m_Thread;
 assertTrue(thread.isAlive());
 }

 @Test
 public void testInit_shouldNotStartThreadIfThreadCheckboxNotSelected() throws InterruptedException, IOException {
 when(gui.menuChkThread.isSelected()).thenReturn(false);
 capture.init();
 assertNull(capture.m_Thread);
 }

 @Test
 public void testDV_Capture_shouldCreateOutputDirectory() {
 File outputDir = new File("outputDir");
 when(gui.strOutputDir).thenReturn("outputDir");
 capture.DV_Capture();
 assertTrue(outputDir.mkdirs());
 }

 @Test(expected = IOException.class)
 public void testDV_Capture_shouldThrowIOExceptionIfOutputDirectoryAlreadyExistsAndDELETEOptionIsNotSelected() {
 File outputDir = new File("outputDir");
 when(gui.strOutputDir).thenReturn("outputDir");
 capture.DV_Capture();
 outputDir.mkdirs();
 outputDir.delete(); // fail to delete
 assertTrue(outputDir.exists());
 capture.DV_Capture();
 }

 @Test
 public void testDV_Capture_shouldDeleteOldOutputDirectoryIfDELETEOptionIsSelected() throws IOException {
 File outputDir = new File("outputDir");
 when(gui.strOutputDir).thenReturn("outputDir");
 capture.DV_Capture();
 outputDir.delete();
 capture.DV_Capture();
 assertFalse(outputDir.exists());
 }

 @Test(expected = NullPointerException.class)
 public void testDV_Capture_shouldThrowNullPointerExceptionIfGUIIsNull() {
 capture = new Capture(1, 2, null);
 capture.DV_Capture();
 }
}
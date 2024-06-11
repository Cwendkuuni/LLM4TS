import org.junit.Test;
import org.junit.Assert;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CaptureTest {

 private Capture capture;
 private GUI gui;

 @Before
 public void setup() {
 capture = new Capture();
 gui = mock(GUI.class);
 }

 @Test
 public void testCapture() {
 capture.init(1, 2, gui);
 Assert.assertFalse(capture.m_Error);
 }

 @Test
 public void testInitWithThreadCheckboxSelected() {
 capture = new Capture();
 capture.m_GUI = gui;
 capture.m_GUI.menuChkThread.setSelected(true);
 capture.init();
 Assert.assertFalse(capture.m_Error);
 }

 @Test
 public void testInitWithInvalidOutputDirectory() {
 capture = new Capture();
 capture.m_GUI = gui;
 capture.m_GUI.strOutputDir = "/non/existing/directory";
 capture.init();
 Assert.assertTrue(capture.m_Error);
 }

 @Test
 public void testDV_CaptureWithExistingOutputDirectory() {
 capture = new Capture();
 capture.m_GUI = gui;
 capture.m_GUI.strOutputDir = "/existing/output/directory";
 capture.init();
 Assert.assertTrue(capture.m_Error);
 }

 @Test(expected = IOException.class)
 public void testDV_CaptureWithIOException() {
 capture = new Capture();
 capture.m_GUI = gui;
 capture.DV_Capture();
 }

 @Test(expected = InterruptedException.class)
 public void testDV_CaptureWithInterruptedException() {
 capture = new Capture();
 capture.m_GUI = gui;
 when(gui.MessageBox(anyString(), anyInt())).thenThrow(new InterruptedException());
 capture.DV_Capture();
 }

 @Test
 public void testDV_CaptureWithdvgrabNotFound() {
 capture = new Capture();
 capture.m_GUI = gui;
 when(gui.MessageBox(anyString(), anyInt())).thenReturn(0);
 when(gui.strOutputDir).thenReturn("/path/to/output/directory");
 when(gui.MessageBox("Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo.", 0)).thenReturn(0);
 capture.init();
 Assert.assertTrue(capture.m_Error);
 }

 @Test
 public void testDV_CaptureWithCameraNotConnected() {
 capture = new Capture();
 capture.m_GUI = gui;
 when(gui.MessageBox("It appears that your camcorder is not connected to your computer!", 0)).thenReturn(0);
 capture.init();
 Assert.assertTrue(capture.m_Error);
 }

 @Test
 public void testDV_CaptureWithdvgrabError() {
 capture = new Capture();
 capture.m_GUI = gui;
 when(gui.MessageBox("Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo.", 0)).thenReturn(0);
 when(gui.strOutputDir).thenReturn("/path/to/output/directory");
 when(gui.MessageBox("Error: no camera exists", 0)).thenReturn(0);
 capture.init();
 Assert.assertTrue(capture.m_Error);
 }

 @Test
 public void testDV_CaptureWithRuntimeGetRuntimeExecNullPointerException() {
 capture = new Capture();
 capture.m_GUI = gui;
 when(gui.MessageBox("Error executing Runtime.getRuntime().exec()\n", 0)).thenReturn(0);
 capture.init();
 Assert.assertTrue(capture.m_Error);
 }

 @Test
 public void testDV_CaptureWithRuntimeGetRuntimeExecIllegalArgumentException() {
 capture = new Capture();
 capture.m_GUI = gui;
 when(gui.MessageBox("Illegal argument sent to Runtime.getRuntime().exec()\n", 0)).thenReturn(0);
 capture.init();
 Assert.assertTrue(capture.m_Error);
 }

 @Test
 public void testDV_CaptureWithRuntimeGetRuntimeExecException() {
 capture = new Capture();
 capture.m_GUI = gui;
 when(gui.MessageBox("Unknown Error occurred\n", 0)).thenReturn(0);
 capture.init();
 Assert.assertTrue(capture.m_Error);
 }
}
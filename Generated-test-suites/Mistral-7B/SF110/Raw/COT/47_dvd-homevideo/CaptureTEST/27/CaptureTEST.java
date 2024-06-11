import org.junit.Test;
import org.junit.Assert;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;

public class CaptureTest {

 private Capture capture;
 private GUI gui;

 @Before
 public void setUp() {
 capture = new Capture();
 gui = mock(GUI.class);
 }

 @Test
 public void testConstructorWithNoArguments() {
 capture = new Capture();
 Assert.assertEquals("Capture Error - ", capture.m_BaseErr);
 }

 @Test
 public void testConstructorWithArguments() {
 capture = new Capture(1, 2, gui);
 Assert.assertEquals("Capture Error - ", capture.m_BaseErr);
 Assert.assertEquals(1, capture.m_Minutes);
 Assert.assertEquals(2, capture.m_Seconds);
 Assert.assertNotNull(capture.m_GUI);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithNullGUI() {
 capture = new Capture();
 capture.init();
 when(gui.menuChkThread.isSelected()).thenReturn(true);
 when(gui.MessageBox(String.valueOf(Integer.MAX_VALUE))).thenReturn(0);
 capture.m_GUI = null;
 capture.init();
 }

 @Test
 public void testInitWithThreadSelection() {
 capture = new Capture();
 capture.init();
 when(gui.menuChkThread.isSelected()).thenReturn(true);
 when(gui.MessageBox(String.valueOf(Integer.MAX_VALUE))).thenReturn(0);
 Assert.assertFalse(capture.m_Error);
 }

 @Test(expected = IOException.class)
 public void testDV_CaptureWithInvalidOutputDir() throws IOException {
 capture = new Capture();
 capture.m_GUI = gui;
 when(gui.strOutputDir).thenReturn("./");
 capture.DV_Capture();
 }

 @Test(expected = InterruptedException.class)
 public void testDV_CaptureWithInterruptedThread() throws IOException, InterruptedException {
 capture = new Capture();
 capture.m_GUI = gui;
 when(gui.strOutputDir).thenReturn(".");
 when(gui.MessageBox(String.valueOf(Integer.MIN_VALUE))).thenReturn(0);
 capture.init();
 capture.m_Thread.interrupt();
 }

 @Test
 public void testDV_CaptureWithValidOutputDir() throws IOException, InterruptedException {
 capture = new Capture();
 capture.m_GUI = gui;
 when(gui.strOutputDir).thenReturn("./");
 when(gui.MessageBox(String.valueOf(Integer.MAX_VALUE))).thenReturn(0);
 when(gui.MessageBox(String.valueOf(Integer.MIN_VALUE))).thenReturn(0);
 capture.init();
 capture.DV_Capture();
 }

 @Test(expected = IOException.class)
 public void testDV_CaptureWithDvgrabNotFound() throws IOException {
 capture = new Capture();
 capture.m_GUI = gui;
 when(gui.strOutputDir).thenReturn("./");
 when(gui.MessageBox(String.valueOf(Integer.MAX_VALUE))).thenReturn(0);
 when(gui.MessageBox(String.valueOf(Integer.MIN_VALUE))).thenReturn(1);
 capture.init();
 capture.m_Thread = mock(Thread.class);
 when(capture.m_Thread.isInterrupted()).thenReturn(false);
 when(capture.m_Thread.join()).thenThrow(new IOException("dvgrab not found"));
 }

 @Test(expected = InterruptedException.class)
 public void testDV_CaptureWithCameraNotConnected() throws IOException, InterruptedException {
 capture = new Capture();
 capture.m_GUI = gui;
 when(gui.strOutputDir).thenReturn("./");
 when(gui.MessageBox(String.valueOf(Integer.MAX_VALUE))).thenReturn(0);
 when(gui.MessageBox(String.valueOf(Integer.MIN_VALUE))).thenReturn(1);
 capture.init();
 capture.m_Thread = mock(Thread.class);
 when(capture.m_Thread.isInterrupted()).thenReturn(false);
 when(capture.m_Thread.join()).thenThrow(new InterruptedIOException("Camera not connected"));
 }
}
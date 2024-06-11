import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class CaptureTest {

 private Capture capture;
 private GUI gui;

 @Before
 public void setUp() {
 gui = Mockito.mock(GUI.class);
 capture = new Capture();
 capture.m_GUI = gui;
 }

 @Test
 public void testInit_NoThreadCheckboxSelected() throws IOException, InterruptedException {
 capture.init();
 Assert.assertTrue(capture.m_Error);
 }

 @Test
 public void testInit_ThreadCheckboxSelected() throws IOException, InterruptedException {
 capture = new Capture(1, 2, gui);
 capture.init();
 Mockito.verify(gui).MessageBox(Mockito.anyString(), Mockito.eq(0));
 }

 @Test(expected = NullPointerException.class)
 public void testInit_NullGUI() {
 capture = new Capture(1, 2, null);
 capture.init();
 }

 @Test
 public void testDV_Capture_OutputDirExistsAndIsADirectory() {
 capture = new Capture();
 capture.DV_Capture();
 Mockito.verify(gui).MessageBox(Mockito.anyString(), Mockito.eq(2));
 }

 @Test
 public void testDV_Capture_OutputDirExistsAndIsFile() {
 capture = new Capture();
 File outputDir = new File("outputDir");
 capture.m_GUI.strOutputDir = outputDir.getAbsolutePath();
 capture.DV_Capture();
 Mockito.verify(gui).MessageBox(Mockito.anyString(), Mockito.eq(2));
 }

 @Test
 public void testDV_Capture_OutputDirDoesntExist() throws IOException {
 capture = new Capture();
 capture.m_GUI.strOutputDir = "outputDir";
 capture.DV_Capture();
 Mockito.verify(gui).MessageBox(Mockito.anyString(), Mockito.eq(0));
 }

 @Test(expected = IOException.class)
 public void testDV_Capture_dvgrabCommandNotFound() {
 capture = new Capture();
 File outputDir = new File("outputDir");
 capture.m_GUI.strOutputDir = outputDir.getAbsolutePath();
 Mockito.when(gui.MessageBox(Mockito.anyString(), Mockito.eq(0))).thenReturn(0);
 capture.DV_Capture();
 }

 @Test
 public void testRun_ThreadIsInterrupted() throws InterruptedException {
 capture = new Capture();
 capture.m_Thread = Mockito.mock(Thread.class);
 Mockito.when(capture.m_Thread.isInterrupted()).thenReturn(true);
 capture.run();
 }

 @Test
 public void testRun_ThreadIsNotInterrupted() {
 capture = new Capture();
 capture.run();
 }
}
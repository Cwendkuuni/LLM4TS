import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import java.io.File;

public class CaptureTest {

 private Capture capture;
 private GUI gui;

 @Before
 public void setUp() {
 capture = new Capture();
 gui = new GUI();
 }

 @Test
 public void testCaptureConstructor() {
 Capture capture = new Capture();
 assertNotNull(capture);
 }

 @Test
 public void testCaptureConstructorWithArguments() {
 int min = 1;
 int sec = 2;
 GUI gui = new GUI();
 Capture capture = new Capture(min, sec, gui);
 assertNotNull(capture);
 }

 @Test
 public void testInit_noThreadSelected() {
 capture.init();
 assertTrue(capture.m_Error);
 }

 @Test
 public void testInit_threadSelected() {
 capture = new Capture(1, 2, gui);
 capture.m_GUI.menuChkThread.setSelected(true);
 capture.init();
 assertFalse(capture.m_Error);
 }

 @Test(expected = NullPointerException.class)
 public void testDV_Capture_nullGUI() {
 capture = new Capture();
 capture.m_GUI = null;
 capture.DV_Capture();
 }

 @Test
 public void testDV_Capture_outputDirAlreadyExistsAsDirectory() {
 capture = new Capture(1, 2, gui);
 capture.m_GUI.strOutputDir = "/temp/output";
 File outputDir = new File(capture.m_GUI.strOutputDir);
 outputDir.mkdirs();
 captor.init();
 capture.DV_Capture();
 outputDir.delete(); // clean up
 }

 @Test
 public void testDV_Capture_outputDirAlreadyExistsAsFile() {
 capture = new Capture(1, 2, gui);
 capture.m_GUI.strOutputDir = "/temp/output/file.txt";
 File outputFile = new File(capture.m_GUI.strOutputDir);
 outputFile.createNewFile();
 captor.init();
 capture.DV_Capture();
 outputFile.delete(); // clean up
 }

 @Test(expected = IOException.class)
 public void testDV_Capture_ioError() {
 capture = new Capture(1, 2, gui);
 capture.m_GUI.strOutputDir = "/temp/output";
 File outputDir = new File(capture.m_GUI.strOutputDir);
 outputDir.mkdirs();
 when(processOutput.waitFor()).thenThrow(new IOException("WaitFor() failed"));
 captor.init();
 capture.DV_Capture();
 outputDir.delete(); // clean up
 }

 @Test(expected = InterruptedException.class)
 public void testDV_Capture_interrupted() {
 capture = new Capture(1, 2, gui);
 capture.m_GUI.strOutputDir = "/temp/output";
 File outputDir = new File(capture.m_GUI.strOutputDir);
 outputDir.mkdirs();
 when(processOutput.waitFor()).thenThrow(new InterruptedException());
 captor.init();
 capture.DV_Capture();
 outputDir.delete(); // clean up
 }

}
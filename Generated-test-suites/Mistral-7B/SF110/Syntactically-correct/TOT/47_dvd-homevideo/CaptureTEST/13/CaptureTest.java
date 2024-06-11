import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.io.File;

public class CaptureTest {

 private Capture capture;
 private GUI mockGUI;

 @Before
 public void setUp() {
 mockGUI = new GUI() {
 @Override
 public boolean menuChkThread_isSelected() {
 return false;
 }

 @Override
 public int MessageBox(String message, int type) {
 return 1;
 }

 @Override
 public void MessageBox(String message, int type, String title) {
 // Do nothing
 }

 @Override
 public String getstrOutputDir() {
 return "output";
 }
 };
 capture = new Capture(5, 10, mockGUI);
 }

 @After
 public void tearDown() {
 capture = null;
 mockGUI = null;
 }

 @Test
 public void testInit_noThreadCheckboxSelected() {
 capture.init();
 assertFalse(capture.m_Error);
 }

 @Test
 public void testInit_threadCheckboxSelected() {
 mockGUI = new GUI() {
 @Override
 public boolean menuChkThread_isSelected() {
 return true;
 }
 };
 capture = new Capture(5, 10, mockGUI);
 capture.init();
 assertTrue(capture.m_Error);
 }

 @Test
 public void testDV_Capture_outputDirectoryExistsAsFolder() {
 capture.m_GUI = mockGUI;
 File outputDirectory = new File("output");
 outputDirectory.mkdir();
 capture.DV_Capture();
 outputDirectory.delete();
 }

 @Test(expected = IOException.class)
 public void testDV_Capture_outputDirectoryExistsAsFile() {
 capture.m_GUI = mockGUI;
 File outputDirectory = new File("output");
 outputDirectory.mkdir();
 File outputFile = new File(outputDirectory, "outputFile.txt");
 outputFile.createNewFile();
 capture.DV_Capture();
 }

 @Test
 public void testDV_Capture_dvgrabCommandNotFound() {
 capture.m_GUI = mockGUI;
 capture.m_dvgrab = "/bin/sh: dvgrab: command not found";
 capture.DV_Capture();
 assertFalse(capture.m_Error);
 }

 @Test
 public void testDV_Capture_cameraNotConnected() {
 capture.m_GUI = mockGUI;
 capture.m_dvgrab = "dvgrab --input test --output test.avi";
 capture.m_Error = false;
 try {
 capture.DV_Capture();
 assertFalse(capture.m_Error);
 }
 catch (Exception e) {
 // Expected exception
 }
 }

 @Test
 public void testDV_Capture_outputDirectoryDoesNotExist() {
 capture.m_GUI = mockGUI;
 capture.DV_Capture();
 assertNull(new File("output"));
 }
}
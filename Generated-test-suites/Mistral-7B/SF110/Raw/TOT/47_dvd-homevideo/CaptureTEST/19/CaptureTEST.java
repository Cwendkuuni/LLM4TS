import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.lang.InterruptedException;
import java.lang.NullPointerException;

public class CaptureTest {

 private Capture capture;
 private GUI mockGUI;

 @Before
 public void setup() {
 mockGUI = new GUI() {
 @Override
 public boolean menuChkThreadIsSelected() {
 return false;
 }

 @Override
 public int showMessageBox(String title, String message, int type) {
 return type;
 }

 @Override
 public String getStrOutputDir() {
 return "/temp/output";
 }

 @Override
 public void MessageBox(String title, String message, int type) {
 // Do nothing
 }
 };
 capture = new Capture(1, 2, mockGUI);
 }

 @Test
 public void testInitWhenGUIThreadNotSelected() {
 capture.init();
 assertFalse(capture.m_Error);
 }

 @Test
 public void testInitWhenGUIThreadSelected() {
 capture = new Capture();
 capture.m_GUI = mockGUI;
 capture.m_GUI.menuChkThread.setSelected(true);
 assertFalse(capture.init());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithNegativeMinutes() {
 capture = new Capture(-1, 2, mockGUI);
 capture.init();
 fail("Should have thrown IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithNegativeSeconds() {
 capture = new Capture(1, -1, mockGUI);
 capture.init();
 fail("Should have thrown IllegalArgumentException");
 }

 @Test
 public void testDV_CaptureWhenOutputDirExistsAndIsADirectory() throws IOException, InterruptedException {
 File outputDir = new File("/temp/output");
 outputDir.mkdir();
 capture = new Capture(1, 2, mockGUI);
 capture.init();
 assertTrue(outputDir.isDirectory());
 }

 @Test
 public void testDV_CaptureWhenOutputDirExistsAndIsFile() throws IOException, InterruptedException {
 File outputFile = new File("/temp/output");
 outputFile.createNewFile();
 capture = new Capture(1, 2, mockGUI);
 capture.init();
 assertFalse(outputFile.exists());
 }

 @Test(expected = IOException.class)
 public void testDV_CaptureWhenCreateOutDirThrowsIOException() throws IOException, InterruptedException {
 capture = new Capture(1, 2, mockGUI);
 capture.init();
 File outputDir = new File("/temp/output");
 assertFalse(outputDir.mkdir());
 fail("Should have thrown IOException");
 }

 @Test(expected = InterruptedException.class)
 public void testDV_CaptureWhenInterrupted() throws IOException, InterruptedException {
 capture = new Capture(1, 2, mockGUI);
 capture.init();
 Thread.currentThread().interrupt();
 fail("Should have thrown InterruptedException");
 }

 @Test
 public void testDV_CaptureWhenDVGrabCommandNotFound() throws IOException, InterruptedException {
 capture = new Capture(1, 2, mockGUI);
 capture.init();
 mockGUI.MessageBox = (title, message, type) -> {
 throw new IOException("dvgrab: command not found");
 };
 assertFalse(capture.init());
 }
}
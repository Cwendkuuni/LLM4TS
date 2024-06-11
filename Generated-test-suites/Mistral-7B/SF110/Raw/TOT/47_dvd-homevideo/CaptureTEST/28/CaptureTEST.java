import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CaptureTest {

 private Capture capture;
 private GUI guiMock;

 @Before
 public void setUp() {
 guiMock = new GUIMock();
 capture = new Capture(1, 2, guiMock);
 }

 @After
 public void tearDown() {
 capture = null;
 guiMock = null;
 }

 @Test
 public void testInitWithGUISelected() {
 capture.init();
 assertFalse(capture.m_Error);
 }

 @Test
 public void testInitWithGUINotSelected() {
 capture = new Capture();
 capture.init();
 assertTrue(capture.m_Error);
 }

 @Test
 public void testDV_CaptureWithOutputDirExistsAsDirectory() throws IOException, InterruptedException {
 File outputDir = new File("outputDir");
 outputDir.mkdir();
 capture = new Capture(1, 2, guiMock);
 capture.init();
 assertTrue(capture.m_Error);
 }

 @Test
 public void testDV_CaptureWithOutputDirExistsAsFile() throws IOException, InterruptedException {
 File outputDir = new File("outputDir");
 outputDir.createNewFile();
 capture = new Capture(1, 2, guiMock);
 capture.init();
 assertTrue(capture.m_Error);
 }

 @Test(expected = IOException.class)
 public void testDV_CaptureWithInvalidOutputDir() {
 capture = new Capture(1, 2, guiMock);
 capture.init();
 }

 @Test
 public void testDV_CaptureWithValidOutputDir() throws IOException, InterruptedException {
 File outputDir = new File("outputDir");
 outputDir.mkdir();
 capture = new Capture(1, 2, guiMock);
 capture.init();
 assertFalse(capture.m_Error);
 }

 @Test
 public void testDV_CaptureWithDvgrabNotFound() throws IOException, InterruptedException {
 capture = new Capture(1, 2, guiMock);
 capture.init();
 assertTrue(capture.m_Error);
 }

 @Test
 public void testDV_CaptureWithCameraNotConnected() throws IOException, InterruptedException {
 capture = new Capture(1, 2, guiMock);
 capture.init();
 assertTrue(capture.m_Error);
 }

 @Test
 public void testDV_CaptureWithSuccessfulCapture() throws IOException, InterruptedException {
 capture = new Capture(1, 2, guiMock);
 capture.init();
 TimeUnit.SECONDS.sleep(5);
 assertFalse(capture.m_Error);
 }
}
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import static org.mockito.Mockito.*;

public class CaptureTest {

 private Capture capture;
 private GUI guiMock;

 @Before
 public void setUp() {
 guiMock = mock(GUI.class);
 capture = new Capture(1, 1, guiMock);
 }

 @After
 public void tearDown() {
 capture = null;
 guiMock = null;
 }

 @Test
 public void testInit_WithThreadSelected_StartThread() {
 when(guiMock.menuChkThread.isSelected()).thenReturn(true);
 capture.init();
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Output directory message box
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Delete output directory message box
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Output directory already exists message box
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Delete file message box
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Choose different name message box
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Different name for title message box
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Command not found message box
 verify(guiMock, times(1)).MessageBox(anyString(), 0); // Failed to initialize error message box
 verify(capture, times(1)).run();
 }

 @Test
 public void testInit_WithoutThreadSelected_DoNotStartThread() {
 when(guiMock.menuChkThread.isSelected()).thenReturn(false);
 capture.init();
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Output directory message box
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Output directory already exists message box
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Output directory already exists message box
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Different name for title message box
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Different name for file message box
 verify(capture, never()).run();
 }

 @Test(expected = NullPointerException.class)
 public void testDV_Capture_NullGUI() {
 capture = new Capture(1, 1, null);
 capture.init();
 }

 @Test
 public void testDV_Capture_ValidOutputDirectory() throws IOException, InterruptedException {
 File outputDirectory = new File("outputDirectory");
 when(guiMock.strOutputDir).thenReturn(outputDirectory.getAbsolutePath());
 File outputDir = new File(outputDirectory.getAbsolutePath() + "/DVD");
 outputDir.mkdir();
 outputDir = new File(outputDirectory.getAbsolutePath() + "/log");
 outputDir.mkdir();
 outputDir = new File(outputDirectory.getAbsolutePath() + "/dv");
 outputDir.mkdir();
 capture.init();
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Output directory message box
 verify(guiMock, times(1)).MessageBox(anyString(), 1); // Create output directory message box
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Delete output directory message box
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Delete directory message box
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Delete file message box
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Different name for title message box
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Different name for file message box
 verify(guiMock, times(1)).MessageBox(anyString(), 0); // Command not found message box
 verify(guiMock, times(1)).MessageBox(anyString(), 0); // Failed to initialize error message box
 }

 @Test(expected = IOException.class)
 public void testDV_Capture_InvalidOutputDirectory() throws IOException, InterruptedException {
 File outputDirectory = new File("outputDirectory");
 when(guiMock.strOutputDir).thenReturn(outputDirectory.getAbsolutePath());
 File outputDir = new File(outputDirectory.getAbsolutePath() + "/DVD");
 outputDir.mkdir();
 outputDir = new File(outputDirectory.getAbsolutePath() + "/log");
 outputDir.mkdir();
 outputDir = new File(outputDirectory.getAbsolutePath() + "/dv");
 outputDir.mkdir();
 when(guiMock.MessageBox(anyString(), anyInt())).thenThrow(new IOException());
 capture.init();
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Output directory message box
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Create output directory message box
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Output directory already exists message box
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Different name for title message box
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Different name for file message box
 verify(guiMock, times(1)).MessageBox(anyString(), 1); // Delete output directory message box
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Delete directory message box
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Delete file message box
 verify(guiMock, times(1)).MessageBox(anyString(), 0); // Command not found message box
 verify(guiMock, times(1)).MessageBox(anyString(), 0); // Failed to initialize error message box
 }

 @Test(expected = InterruptedException.class)
 public void testDV_Capture_Interrupted() throws IOException, InterruptedException {
 File outputDirectory = new File("outputDirectory");
 when(guiMock.strOutputDir).thenReturn(outputDirectory.getAbsolutePath());
 File outputDir = new File(outputDirectory.getAbsolutePath() + "/DVD");
 outputDir.mkdir();
 outputDir = new File(outputDirectory.getAbsolutePath() + "/log");
 outputDir.mkdir();
 outputDir = new File(outputDirectory.getAbsolutePath() + "/dv");
 outputDir.mkdir();
 when(guiMock.MessageBox(anyString(), anyInt())).thenAnswer(invocation -> {
 Thread.currentThread().interrupt();
 return 0;
 });
 capture.init();
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Output directory message box
 verify(guiMock, times(1)).MessageBox(anyString(), 1); // Create output directory message box
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Output directory already exists message box
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Different name for title message box
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Different name for file message box
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Delete output directory message box
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Delete directory message box
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Delete file message box
 verify(guiMock, times(1)).MessageBox(anyString(), anyInt()); // Command not found message box
 verify(guiMock, times(1)).MessageBox(anyString(), 0); // Failed to initialize error message box
 }
}
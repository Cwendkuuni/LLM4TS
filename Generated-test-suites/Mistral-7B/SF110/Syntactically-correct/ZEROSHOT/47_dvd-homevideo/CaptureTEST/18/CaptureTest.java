import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class CaptureTest {

 private Capture capture;
 private GUI gui;

 @Before
 public void setUp() {
 gui = mock(GUI.class);
 capture = new Capture(1, 2, gui);
 }

 @Test
 public void testInitWithThreadDisabled() {
 when(gui.menuChkThread.isSelected()).thenReturn(false);
 capture.init();
 verify(gui, Mockito.never()).MessageBox(ArgumentMatchers.anyString(), ArgumentMatchers.eq(0));
 }

 @Test
 public void testInitWithThreadEnabled() {
 when(gui.menuChkThread.isSelected()).thenReturn(true);
 capture.init();
 verify(gui).MessageBox(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());
 }

 @Test(expected = IllegalStateException.class)
 public void testInitTwice() {
 capture.init();
 capture.init();
 }

 @Test
 public void testDV_CaptureWithValidOutputDirectory() throws IOException {
 File outputDir = new File("outputDir");
 when(gui.strOutputDir).thenReturn(outputDir.getAbsolutePath());
 capture.DV_Capture();
 verify(gui, Mockito.never()).MessageBox(ArgumentMatchers.anyString(), ArgumentMatchers.eq(0));
 }

 @Test
 public void testDV_CaptureWithExistingOutputDirectory() throws IOException, InterruptedException {
 File outputDir = new File("outputDir");
 File outputDirFile = new File(outputDir, "file.txt");
 outputDir.mkdir();
 outputDirFile.createNewFile();
 when(gui.strOutputDir).thenReturn(outputDir.getAbsolutePath());
 capture.DV_Capture();
 verify(gui).MessageBox(ArgumentMatchers.anyString(), ArgumentMatchers.eq(2));
 }

 @Test
 public void testDV_CaptureWithNonExistentOutputDirectory() throws IOException, InterruptedException {
 File outputDir = new File("outputDir");
 when(gui.strOutputDir).thenReturn(outputDir.getAbsolutePath());
 capture.DV_Capture();
 verify(gui, Mockito.never()).MessageBox(ArgumentMatchers.anyString(), ArgumentMatchers.eq(0));
 verify(gui).MessageBox(ArgumentMatchers.anyString(), ArgumentMatchers.eq(2));
 }

 @Test
 public void testDV_CaptureWithErrorInCreatingOutputDirectory() throws IOException, InterruptedException {
 File outputDir = new File("outputDir");
 when(gui.strOutputDir).thenReturn(outputDir.getAbsolutePath());
 when(gui.MessageBox(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())).thenReturn(0);
 Mockito.doThrow(new IOException("Error creating output directory")).when(gui).mkdirs();
 capture.DV_Capture();
 verify(gui).MessageBox(ArgumentMatchers.anyString(), ArgumentMatchers.eq(0));
 }

 @Test
 public void testRunWithValidOutputDirectory() throws IOException {
 File outputDir = new File("outputDir");
 when(gui.strOutputDir).thenReturn(outputDir.getAbsolutePath());
 capture.run();
 }

 @Test(expected = IllegalThreadStateException.class)
 public void testRunWithThreadAlreadyRunning() {
 capture = new Capture();
 capture.m_Thread = Mockito.mock(Thread.class);
 when(capture.m_Thread.isAlive()).thenReturn(true);
 capture.run();
 }
}
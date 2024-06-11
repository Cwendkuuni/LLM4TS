import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CaptureTest {

 private Capture capture;
 private GUI gui;

 @Before
 public void setup() {
 gui = mock(GUI.class);
 capture = new Capture();
 capture.m_GUI = gui;
 }

 @Test
 public void testInitNoThread() {
 capture = new Capture();
 assertTrue(capture.init());
 }

 @Test
 public void testInitWithThread() {
 capture = new Capture(0, 0, gui);
 assertFalse(capture.init());
 }

 @Test
 public void testRun() throws IOException, InterruptedException {
 capture = new Capture(0, 0, gui);
 capture.m_Thread = Mockito.mock(Thread.class);
 when(capture.m_Thread.isAlive()).thenReturn(true);
 capture.run();
 Mockito.verify(capture.m_Thread).run();
 }

 @Test(expected = NullPointerException.class)
 public void testDV_CaptureNullGui() {
 capture = new Capture();
 capture.m_GUI = null;
 capture.DV_Capture();
 }

 @Test
 public void testDV_CaptureNoErrors() throws IOException, InterruptedException {
 capture = new Capture(0, 0, gui);
 capture.DV_Capture();
 Mockito.verify(gui, Mockito.times(1)).MessageBox(Mockito.eq("The Output Directory already exists!"), Mockito.eq(0));
 Mockito.verify(gui, Mockito.times(1)).MessageBox(Mockito.eq("Please choose a different name for your title or move the directory to another location."), Mockito.eq(2));
 Mockito.verify(gui, Mockito.times(1)).MessageBox(Mockito.eq("Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo."), Mockito.eq(0));
 Mockito.verify(gui, Mockito.times(0)).MessageBox("It appears that your camcorder is not connected to your computer!", Mockito.eq(0));
 }

 @Test
 public void testDV_CaptureErrorDeletingOutputDirectory() throws IOException, InterruptedException {
 capture = new Capture(0, 0, gui);
 when(gui.MessageBox(Mockito.anyString(), Mockito.anyInt())).thenReturn(0);
 File outputDir = new File("outputDirectory");
 when(gui.strOutputDir).thenReturn(outputDir.getAbsolutePath());
 when(gui.MessageBox(Mockito.eq("The Output Directory already exists!"), Mockito.anyInt())).thenReturn(0);
 File outputDirFile = new File(outputDir, "outputFile.txt");
 outputDirFile.createNewFile();
 when(gui.MessageBox(Mockito.eq("There exists a file with the Output directory name at this location!  Would you like to delete it?"), Mockito.anyInt())).thenReturn(0);
 when(gui.MessageBox(Mockito.eq("Please choose a different name for your title or move the file to another location."), Mockito.anyInt())).thenReturn(0);
 when(gui.MessageBox(Mockito.eq("Please choose a different name for your title or move the file to another location."), Mockito.anyInt())).thenReturn(2);
 capture.DV_Capture();
 Mockito.verify(gui, Mockito.times(1)).MessageBox(Mockito.eq("Please choose a different name for your title or move the file to another location."), Mockito.anyInt());
 }

 @Test
 public void testDV_CaptureErrorExecutingCmd() throws IOException, InterruptedException {
 capture = new Capture(0, 0, gui);
 when(gui.MessageBox(Mockito.anyString(), Mockito.anyInt())).thenReturn(0);
 when(gui.MessageBox(Mockito.eq("Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo."), Mockito.anyInt())).thenReturn(0);
 final Process process = Mockito.mock(Process.class);
 when(Runtime.getRuntime().exec(Mockito.anyString(), Mockito.any(String[].class), Mockito.any(File.class))).thenReturn(process);
 when(process.waitFor()).thenThrow(new InterruptedException());
 capture.DV_Capture();
 Mockito.verify(gui, Mockito.times(1)).MessageBox(Mockito.eq("Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo."), Mockito.anyInt());
 }
}
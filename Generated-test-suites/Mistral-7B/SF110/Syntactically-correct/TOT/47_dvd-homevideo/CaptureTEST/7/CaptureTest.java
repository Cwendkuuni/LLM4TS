import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.lang.InterruptedException;
import java.lang.NullPointerException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CaptureTest {

 private Capture capture;
 private GUI gui;

 @Before
 public void init() {
 gui = mock(GUI.class);
 capture = new Capture(1, 2, gui);
 }

 @Test
 public void testInit_shouldReturnFalse_whenThreadCheckboxIsSelected() {
 when(gui.menuChkThread.isSelected()).thenReturn(true);
 assertFalse(capture.init());
 }

 @Test
 public void testInit_shouldReturnTrue_whenThreadCheckboxIsNotSelected() {
 when(gui.menuChkThread.isSelected()).thenReturn(false);
 assertTrue(capture.init());
 }

 @Test(expected = NullPointerException.class)
 public void testInit_shouldThrowNullPointerException_whenGUIIsNull() {
 capture = new Capture(1, 2, null);
 }

 @Test
 public void testDV_Capture_shouldCreateOutputDirectory() throws IOException, InterruptedException {
 File outputDir = new File("outputDir");
 when(gui.strOutputDir).thenReturn(outputDir.getAbsolutePath());
 capture.DV_Capture();
 assertTrue(outputDir.exists() && outputDir.isDirectory());
 }

 @Test
 public void testDV_Capture_shouldDeleteExistingDirectory() throws IOException, InterruptedException {
 File outputDir = new File("outputDir");
 outputDir.mkdir();
 when(gui.strOutputDir).thenReturn(outputDir.getAbsolutePath());
 capture.DV_Capture();
 assertNull(outputDir.listFiles());
 }

 @Test(expected = IOException.class)
 public void testDV_Capture_shouldThrowIOException_whenCannotDeleteExistingFile() throws IOException, InterruptedException {
 File outputDir = new File("outputDir");
 outputDir.createNewFile();
 when(gui.strOutputDir).thenReturn(outputDir.getAbsolutePath());
 capture.DV_Capture();
 }

 @Test(expected = InterruptedException.class)
 public void testDV_Capture_shouldThrowInterruptedException() throws IOException, InterruptedException {
 when(gui.blnBegin).thenReturn(false);
 capture.DV_Capture();
 capture.m_Thread.interrupt();
 }

 @Test
 public void testDV_Capture_shouldExecuteDvgrabCommand() throws IOException, InterruptedException {
 when(gui.strOutputDir).thenReturn("outputDir");
 when(gui.MessageBox(Mockito.anyString(), Mockito.anyInt())).thenReturn(0);
 capture.DV_Capture();
 String expectedCmd = "dvgrab --autosplit --size 0 --format raw --opendml --buffers 200 dv/dv_file-";
 String actualCmd = capture.m_dvgrab.substring(0, capture.m_dvgrab.indexOf(capture.m_GUI.strOutputDir));
 assertEquals(expectedCmd, actualCmd);
 }

 @Test
 public void testDV_Capture_shouldPrintErrorMessage_whenDvgrabCommandNotFound() throws IOException, InterruptedException {
 when(gui.strOutputDir).thenReturn("outputDir");
 when(gui.MessageBox(Mockito.anyString(), Mockito.anyInt())).thenReturn(0);
 when(gui.MessageBox(Mockito.anyString(), Mockito.anyInt())).thenReturn(0);
 capture.DV_Capture();
 String expectedMessage = "Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo.";
 String actualMessage = capture.m_GUI.txtAreaOutput.getText();
 assertEquals(expectedMessage, actualMessage);
 }

}
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CaptureTest {

 private Capture capture;
 private GUI gui;

 @Before
 public void setUp() {
 gui = mock(GUI.class);
 capture = new Capture(1, 2, gui);
 }

 @Test
 public void testInit_OutputDirExistsAndIsADirectory_ShouldReturnFalseAndDeleteIt() throws IOException, InterruptedException {
 File outputDir = Mockito.spy(new File("/path/to/output/dir"));
 when(gui.strOutputDir).thenReturn(outputDir.getAbsolutePath());
 when(outputDir.exists()).thenReturn(true);
 when(outputDir.isDirectory()).thenReturn(true);
 when(outputDir.delete()).thenReturn(true);

 capture.init();

 // Check that init returned false
 assertFalse(capture.init());

 // Check that output directory was deleted
 assertTrue(outputDir.delete());
 }

 @Test
 public void testInit_OutputDirExistsAndIsAFile_ShouldAskUserToChooseADifferentNameOrLocation() {
 File outputDir = Mockito.spy(new File("/path/to/output/file"));
 when(gui.strOutputDir).thenReturn(outputDir.getAbsolutePath());
 when(outputDir.exists()).thenReturn(true);
 when(outputDir.isDirectory()).thenReturn(false);
 when(gui.MessageBox(Mockito.anyString(), Mockito.eq(0))).thenReturn(0);

 capture.init();

 // Check that MessageBox was called with correct message
 Mockito.verify(gui, Mockito.times(1)).MessageBox(Mockito.eq("Please choose a different name for your\ntitle or move the file to another location."), Mockito.eq(2));
 }

 @Test(expected = NullPointerException.class)
 public void testInit_GUIIsNull_ShouldThrowNullPointerException() {
 capture = new Capture(1, 2, null);
 }

 @Test
 public void testDV_Capture_dvgrabCommandNotFound_ShouldShowMessageBoxAndReturnError() {
 when(gui.MessageBox(Mockito.anyString(), Mockito.eq(0))).thenReturn(0);
 when(gui.strOutputDir).thenReturn("/path/to/output/dir");
 when(new File("/bin/sh", "-c", Mockito.anyString()).waitFor()).thenThrow(new IOException(" dvgrab: command not found"));

 capture.init();

 // Check that MessageBox was called with correct message
 Mockito.verify(gui, Mockito.times(1)).MessageBox(Mockito.eq("Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo."), Mockito.eq(0));
 // Check that error flag was set to true
 assertTrue(capture.m_Error);
 }
}
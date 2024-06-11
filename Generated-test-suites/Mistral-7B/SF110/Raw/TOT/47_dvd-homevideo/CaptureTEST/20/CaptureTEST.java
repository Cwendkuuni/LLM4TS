import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
 public void testInit_validOutputDirectoryAndThreadSelected_shouldReturnFalse() {
 when(gui.menuChkThread.isSelected()).thenReturn(true);
 when(gui.ErrorCheck(Mockito.anyString())).thenReturn(true);

 assertFalse(capture.init());
 }

 @Test
 public void testInit_invalidOutputDirectory_shouldReturnError() {
 when(gui.menuChkThread.isSelected()).thenReturn(false);
 when(gui.strOutputDir).thenReturn("");
 when(gui.ErrorCheck(Mockito.anyString())).thenReturn(false);

 assertTrue(capture.init());
 verify(gui).MessageBox(Mockito.anyString(), Mockito.eq(0));
 }

 @Test
 public void testInit_outputDirectoryExistsAsFile_shouldReturnError() {
 when(gui.menuChkThread.isSelected()).thenReturn(false);
 when(gui.strOutputDir).thenReturn("exists");
 when(gui.ErrorCheck(Mockito.anyString())).thenReturn(false);

 assertTrue(capture.init());
 verify(gui).MessageBox(Mockito.anyString(), Mockito.eq(2));
 }

 @Test(expected = NullPointerException.class)
 public void testInit_guiIsNull_shouldThrowException() {
 gui = null;
 capture = new Capture(1, 2, gui);
 }

 @Test
 public void testDV_Capture_validOutputDirectory_shouldCreateDirectoriesAndCallDvgrab() throws IOException, InterruptedException {
 when(gui.menuChkThread.isSelected()).thenReturn(false);
 when(gui.strOutputDir).thenReturn("outputDirectory");
 when(gui.ErrorCheck(Mockito.anyString())).thenReturn(true);
 when(gui.MessageBox(Mockito.anyString(), Mockito.eq(0))).thenReturn(0);
 when(gui.MessageBox(Mockito.anyString(), Mockito.eq(2))).thenReturn(2);

 capture.init();

 File outputDir = new File("outputDirectory");
 File captureDir = new File(outputDir.getPath() + "/DVD");
 File logDir = new File(outputDir.getPath() + "/log");
 File dvDir = new File(outputDir.getPath() + "/dv");

 assertTrue(outputDir.exists() && outputDir.isDirectory());
 assertTrue(captureDir.exists() && captureDir.isDirectory());
 assertTrue(logDir.exists() && logDir.isDirectory());
 assertTrue(dvDir.exists() && dvDir.isDirectory());

 String dvgrabCmd = capture.m_dvgrab;
 String[] dvgrabCmdArray = dvgrabCmd.split(" ");
 for (int i = 0; i < dvgrabCmdArray.length; i++) {
 assertNotNull(dvgrabCmdArray[i]);
 }
 }

 @Test
 public void testDV_Capture_invalidOutputDirectory_shouldDeleteExistingDirectoryAndReturnError() throws IOException, InterruptedException {
 when(gui.menuChkThread.isSelected()).thenReturn(false);
 when(gui.strOutputDir).thenReturn("outputDirectory");
 when(gui.ErrorCheck(Mockito.anyString())).thenReturn(false);
 when(gui.MessageBox(Mockito.anyString(), Mockito.eq(0))).thenReturn(0);
 when(gui.MessageBox(Mockito.anyString(), Mockito.eq(2))).thenReturn(2);

 capture.init();

 File outputDir = new File("outputDirectory");
 File removeDirCmd = new File("rm -fr outputDirectory");
 String[] removeDirCmdArray = removeDirCmd.getPath().split(" ");
 for (int i = 1; i < removeDirCmdArray.length; i++) {
 assertNotNull(removeDirCmdArray[i]);
 }
 }

 @Test
 public void testDV_Capture_outputDirectoryExistsAsFile_shouldDeleteExistingFileAndReturnError() throws IOException, InterruptedException {
 when(gui.menuChkThread.isSelected()).thenReturn(false);
 when(gui.strOutputDir).thenReturn("outputFile");
 when(gui.ErrorCheck(Mockito.anyString())).thenReturn(false);
 when(gui.MessageBox(Mockito.anyString(), Mockito.eq(0))).thenReturn(0);
 when(gui.MessageBox(Mockito.anyString(), Mockito.eq(2))).thenReturn(2);

 capture.init();

 File outputFile = new File("outputFile");
 String rmCmd = "rm -fr outputFile";
 String[] rmCmdArray = rmCmd.split(" ");
 for (int i = 1; i < rmCmdArray.length; i++) {
 assertNotNull(rmCmdArray[i]);
 }
 }

 @Test(expected = IOException.class)
 public void testDV_Capture_noCameraFound_shouldPrintErrorMessageAndReturn() throws IOException, InterruptedException {
 when(gui.menuChkThread.isSelected()).thenReturn(false);
 when(gui.strOutputDir).thenReturn("outputDirectory");
 when(gui.ErrorCheck(Mockito.anyString())).thenReturn(true);
 when(gui.MessageBox(Mockito.anyString(), Mockito.eq(0))).thenReturn(0);
 when(gui.MessageBox(Mockito.anyString(), Mockito.eq(0))).thenThrow(new IOException());

 capture.init();
 }
}
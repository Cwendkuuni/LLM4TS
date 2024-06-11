import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

public class CaptureTest {

 private Capture capture;
 private GUI gui;

 @Before
 public void setup() {
 gui = mock(GUI.class);
 capture = new Capture(1, 2, gui);
 }

 @Test
 public void testInit_noThreadSelected() {
 when(gui.menuChkThread.isSelected()).thenReturn(false);
 assertFalse(capture.init());
 }

 @Test
 public void testInit_threadSelected() {
 when(gui.menuChkThread.isSelected()).thenReturn(true);
 capture.m_Thread = mock(Thread.class);
 when(m_Thread.isAlive()).thenReturn(true);
 when(gui.ErrorCheck(capture.m_GUI.strOutputDir + "/log/dvgrab.log")).thenReturn(true);
 assertTrue(capture.init());
 }

 @Test
 public void testInit_errorDuringErrorCheck() {
 when(gui.ErrorCheck(capture.m_GUI.strOutputDir + "/log/dvgrab.log")).thenThrow(new IOException("Error during error check"));
 assertThrows(RuntimeException.class, () -> capture.init());
 }

 @Test
 public void testRun_noError() throws InterruptedException, IOException {
 capture.run();
 File outputDir = new File("outputDir");
 File expectedOutputDir = new File("outputDir/DVD");
 File expectedLogDir = new File("outputDir/log");
 File expectedDvDir = new File("outputDir/dv");
 assertTrue(outputDir.exists());
 assertTrue(outputDir.isDirectory());
 assertTrue(expectedOutputDir.mkdir());
 assertTrue(expectedOutputDir.exists());
 assertTrue(expectedOutputDir.isDirectory());
 assertTrue(expectedLogDir.mkdir());
 assertTrue(expectedLogDir.exists());
 assertTrue(expectedLogDir.isDirectory());
 assertTrue(expectedDvDir.mkdir());
 assertTrue(expectedDvDir.exists());
 assertTrue(expectedDvDir.isDirectory());
 }

 @Test(expected = IOException.class)
 public void testRun_errorDuringDvgrabExecution() throws InterruptedException, IOException {
 File outputDir = new File("outputDir");
 File logFile = new File("outputDir/log/dvgrab.log");
 when(gui.ErrorCheck(logFile.getAbsolutePath())).thenThrow(new IOException("Error during error check"));
 capture.run();
 assertFalse(outputDir.exists());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRun_errorDuringExecCommand() throws InterruptedException {
 File outputDir = new File("outputDir");
 when(gui.ErrorCheck(outputDir.getAbsolutePath())).thenThrow(new IllegalArgumentException("Error during error check"));
 capture.run();
 assertFalse(outputDir.exists());
 }

 @Test(expected = NullPointerException.class)
 public void testRun_errorDuringCreateDirectories() throws InterruptedException {
 File outputDir = new File("outputDir");
 when(gui.ErrorCheck(outputDir.getAbsolutePath())).thenReturn(true);
 when(gui.MessageBox(anyString(), intThat(isA(Integer.class)), anyInt(), anyString())).thenThrow(new NullPointerException());
 capture.run();
 assertFalse(outputDir.exists());
 }

 @Test
 public void testRun_errorDuringFileRemoval() throws InterruptedException, IOException {
 File outputDir = new File("outputDir");
 File logFile = new File("outputDir/log/dvgrab.log");
 when(gui.MessageBox(anyString(), intThat(isA(Integer.class)), anyInt(), anyString())).thenReturn(0);
 when(new File(outputDir.getAbsolutePath() + "/log/dvgrab.log").exists()).thenReturn(true);
 when(new File(outputDir.getAbsolutePath() + "/log/dvgrab.err").exists()).thenReturn(true);
 when(new File(outputDir.getAbsolutePath() + "/log/dvgrab.log").delete()).thenThrow(new IOException("Error during file removal"));
 capture.run();
 assertFalse(outputDir.exists());
 }

 @Test
 public void testRun_errorDuringProcessExecution() throws InterruptedException, IOException {
 File outputDir = new File("outputDir");
 when(gui.ErrorCheck(outputDir.getAbsolutePath())).thenReturn(true);
 when(gui.MessageBox(anyString(), intThat(isA(Integer.class)), anyInt(), anyString())).thenReturn(0);
 when(new File(outputDir.getAbsolutePath() + "/log/dvgrab.log").exists()).thenReturn(false);
 when(new File(outputDir.getAbsolutePath() + "/log/dvgrab.err").exists()).thenReturn(false);
 Process p = mock(Process.class);
 when(Runtime.getRuntime().exec(anyStringArray())).thenReturn(p);
 when(p.waitFor()).thenThrow(new InterruptedException());
 capture.run();
 assertFalse(outputDir.exists());
 }

 @Test
 public void testRun_errorDuringBufferedReader() throws InterruptedException, IOException {
 File outputDir = new File("outputDir");
 when(gui.ErrorCheck(outputDir.getAbsolutePath())).thenReturn(true);
 when(gui.MessageBox(anyString(), intThat(isA(Integer.class)), anyInt(), anyString())).thenReturn(0);
 when(new File(outputDir.getAbsolutePath() + "/log/dvgrab.log").exists()).thenReturn(true);
 when(new File(outputDir.getAbsolutePath() + "/log/dvgrab.err").exists()).thenReturn(false);
 Process p = mock(Process.class);
 when(Runtime.getRuntime().exec(anyStringArray())).thenReturn(p);
 when(p.waitFor()).thenReturn(0);
 when(p.getInputStream()).thenReturn(new ByteArrayInputStream("dvgrab: command not found".getBytes()));
 when(new BufferedReader(new InputStreamReader(p.getInputStream())).readLine()).thenThrow(new IOException("Error during reading input stream"));
 capture.run();
 assertFalse(outputDir.exists());
 }

 @Test
 public void testRun_success() throws InterruptedException, IOException {
 File outputDir = new File("outputDir");
 when(gui.ErrorCheck(outputDir.getAbsolutePath())).thenReturn(true);
 when(gui.MessageBox(anyString(), intThat(isA(Integer.class)), anyInt(), anyString())).thenReturn(0);
 when(new File(outputDir.getAbsolutePath() + "/log/dvgrab.log").exists()).thenReturn(false);
 when(new File(outputDir.getAbsolutePath() + "/log/dvgrab.err").exists()).thenReturn(false);
 when(gui.txtAreaOutput.append(anyString())).then(invocation -> {
 String line = invocation.getArgument(0);
 System.out.println(line);
 return null;
 });
 Process p = mock(Process.class);
 when(Runtime.getRuntime().exec(anyStringArray())).thenReturn(p);
 when(p.waitFor()).thenReturn(0);
 when(p.getInputStream()).then(new InputStreamReader(new ByteArrayInputStream("dvgrab: using file dv/dv_file-00001.vob as input file\n".getBytes()))::newBufferedReader);
 capture.run();
 File logFile = new File("outputDir/log/dvgrab.log");
 assertTrue(outputDir.exists());
 assertTrue(outputDir.isDirectory());
 assertTrue(logFile.exists());
 assertTrue(logFile.isFile());
 String expectedLogContent = "dvgrab: using file dv/dv_file-00001.vob as input file\n";
 assertEquals(expectedLogContent, new String(Files.readAllBytes(logFile.toPath())));
 }
}
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestRuleChain;
import org.junit.rules.ExpectedException;
import org.mockito.mockitounächst.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;
import java.io.IOException;
import java.lang.InterruptedException;
import java.lang.NullPointerException;
import java.lang.IllegalArgumentException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

public class CaptureTest {

 final Capture capture = new Capture();
 final Capture captureWithParams = new Capture(1, 2, mock(GUI.class));
 final GUI guiMock = mock(GUI.class);
 final Thread threadMock = mock(Thread.class);
 final Process processMock = mock(Process.class);
 final BufferedReader readerMock = mock(BufferedReader.class);
 final BufferedWriter writerMock = mock(BufferedWriter.class);
 final File outputDirMock = mock(File.class);
 final File dirMock = mock(File.class);
 final String[] dvgrabCmdMock = { "/bin/sh", "-c", capture.m_dvgrab };

 @Rule
 public TestRuleChains testRules = new TestRuleChain().around(new ExpectedException());

 @Before
 public void setup() {
 initMocks(this);
 when(guiMock.menuChkThread.isSelected()).thenReturn(false);
 when(guiMock.MessageBox(anyString(), intThat(isA(Integer.class)), anyString(), anyInt())).thenAnswer((Answer<Integer> arg0) -> arg0.getArguments()[2]);
 when(guiMock.ErrorCheck(String.class)).thenReturn(true);
 when(guiMock.strOutputDir).thenReturn("/testOutputDir");
 when(outputDirMock.exists()).thenReturn(true);
 when(outputDirMock.isDirectory()).thenReturn(true);
 when(outputDirMock.mkdir()).thenReturn(true);
 when(outputDirMock.getParent()).thenReturn("/testOutputDir");
 when(outputDirMock.toString()).thenReturn("/testOutputDir");
 when(dirMock.exists()).thenReturn(false);
 when(dirMock.isDirectory()).thenReturn(false);
 when(dirMock.mkdir()).thenReturn(true);
 when(dirMock.getParent()).thenReturn("/testOutputDir/DVD");
 when(dirMock.toString()).thenReturn("/testOutputDir/DVD");
 when(processMock.getInputStream()).thenReturn(readerMock);
 when(processMock.getErrorStream()).thenReturn(readerMock);
 when(processMock.waitFor()).thenReturn(0);
 when(threadMock.isAlive()).thenReturn(false);
 when(threadMock.getPriority()).thenReturn(10);
 when(threadMock.getName()).thenReturn("dvd-homevideo thread");
 when(threadMock.start()).thenReturn(threadMock);
 doReturn("/bin/sh").when(readerMock).readLine();
 doThrow(IOException.class).when(readerMock).readLine();
 doReturn("Error: no camera exists").when(readerMock).readLine();
 doReturn(null).when(writerMock).writeLine(anyString());
 doReturn(null).when(writerMock).flush();
 doReturn(null).when(outputDirMock).deleteOnExit();
 doReturn(null).when(processMock).destroy();
 doReturn(null).when(threadMock).interrupt();
 }

 @After
 public void teardown() {
 reset(guiMock, threadMock, processMock, readerMock, writerMock, outputDirMock, dirMock);
 }

 @Test
 public void testConstructor() {
 capture = new Capture();
 assertNull(capture.m_GUI);
 assertNull(capture.m_Thread);
 assertFalse(capture.m_Error);
 assertNull(capture.m_BaseErr);
 assertNull(capture.m_dvgrab);
 }

 @Test
 public void testConstructorWithParams() {
 captureWithParams = new Capture(1, 2, guiMock);
 assertTrue(captureWithParams.m_GUI != null);
 assertFalse(captureWithParams.m_Error);
 assertEquals(new Integer(1), captureWithParams.m_Minutes);
 assertEquals(new Integer(2), captureWithParams.m_Seconds);
 }

 @Test
 public void testInit_GUIChkThreadIsSelectedFalse() throws InterruptedException {
 capture = new Capture();
 capture.init();
 assertFalse(capture.m_Error);
 verify(guiMock, times(1)).MessageBox(anyString(), eq(0), anyString(), anyInt());
 verify(guiMock, times(1)).ErrorCheck(eq("/testOutputDir/log/dvgrab.log"));
 verify(outputDirMock, times(1)).mkdir();
 }

 @Test
 public void testInit_GUIChkThreadIsSelectedTrue() throws InterruptedException {
 capture = new Capture();
 when(guiMock.menuChkThread.isSelected()).thenReturn(true);
 capture.init();
 assertFalse(capture.m_Error);
 verify(threadMock, times(1)).setPriority(10);
 verify(threadMock, times(1)).start();
 }

 @Test
 public void testInit_ErrorInDVD_GUIErrorCheck() throws IOException, InterruptedException {
 captureWithParams = new Capture(1, 2, guiMock);
 when(guiMock.ErrorCheck(String.class)).thenReturn(false);
 capture.init();
 assertTrue(capture.m_Error);
 verify(guiMock, times(1)).MessageBox(capture.m_BaseErr + "dvgrab: command not found", eq(0));
 }

 @Test
 public void testInit_ErrorInCreateOutputDir() throws IOException, InterruptedException {
 captureWithParams = new Capture(1, 2, guiMock);
 when(guiMock.MessageBox(anyString(), intThat(isA(Integer.class)), anyString(), anyInt())).thenAnswer((Answer<Integer> arg0) -> arg0.getArguments()[2] == 0 ? 0 : 2);
 capture.init();
 assertTrue(capture.m_Error);
 }

 @Test
 public void testRun() throws IOException, InterruptedException {
 captureWithParams = new Capture(1, 2, guiMock);
 when(guiMock.menuChkThread.isSelected()).thenReturn(true);
 capture.run();
 verify(threadMock, times(1)).start();
 }

 @Test
 public void testDV_Capture_ErrorInCreateOutputDir() throws IOException, InterruptedException {
 captureWithParams = new Capture(1, 2, guiMock);
 when(outputDirMock.exists()).thenReturn(false);
 capture.DV_Capture();
 assertFalse(capture.m_Error);
 }

 @Test
 public void testDV_Capture_ErrorInCreateDVDDir() throws IOException, InterruptedException {
 captureWithParams = new Capture(1, 2, guiMock);
 when(outputDirMock.exists()).thenReturn(true);
 when(outputDirMock.isDirectory()).thenReturn(false);
 capture.DV_Capture();
 assertTrue(capture.m_Error);
 }

 @Test
 public void testDV_Capture_ErrorInCreateLogDir() throws IOException, InterruptedException {
 captureWithParams = new Capture(1, 2, guiMock);
 when(outputDirMock.exists()).thenReturn(true);
 when(outputDirMock.isDirectory()).thenReturn(true);
 when(dirMock.exists()).thenReturn(false);
 capture.DV_Capture();
 assertTrue(capture.m_Error);
 }

 @Test
 public void testDV_Capture_ErrorInCreateCaptureDir() throws IOException, InterruptedException {
 captureWithParams = new Capture(1, 2, guiMock);
 when(outputDirMock.exists()).thenReturn(true);
 when(outputDirMock.isDirectory()).thenReturn(true);
 when(dirMock.exists()).thenReturn(true);
 when(dirMock.isDirectory()).thenReturn(false);
 capture.DV_Capture();
 assertTrue(capture.m_Error);
 }

 @Test
 public void testDV_Capture_ErrorInExecuteDvgrabCmd() throws IOException, InterruptedException {
 captureWithParams = new Capture(1, 2, guiMock);
 when(outputDirMock.exists()).thenReturn(true);
 when(outputDirMock.isDirectory()).thenReturn(true);
 when(dirMock.exists()).thenReturn(true);
 when(dirMock.isDirectory()).thenReturn(true);
 when(processMock.getInputStream()).thenThrow(IOException.class);
 capture.DV_Capture();
 assertTrue(capture.m_Error);
 }

 @Test
 public void testDV_Capture_ErrorInReadInputStream() throws IOException, InterruptedException {
 captureWithParams = new Capture(1, 2, guiMock);
 when(outputDirMock.exists()).thenReturn(true);
 when(outputDirMock.isDirectory()).thenReturn(true);
 when(dirMock.exists()).thenReturn(true);
 when(dirMock.isDirectory()).thenReturn(true);
 when(processMock.getInputStream()).thenReturn(readerMock);
 when(readerMock.readLine()).thenThrow(IOException.class);
 capture.DV_Capture();
 assertTrue(capture.m_Error);
 }

 @Test
 public void testDV_Capture_ErrorInReadErrorStream() throws IOException, InterruptedException {
 captureWithParams = new Capture(1, 2, guiMock);
 when(outputDirMock.exists()).thenReturn(true);
 when(outputDirMock.isDirectory()).thenReturn(true);
 when(dirMock.exists()).thenReturn(true);
 when(dirMock.isDirectory()).thenReturn(true);
 when(processMock.getErrorStream()).thenReturn(readerMock);
 when(readerMock.readLine()).thenReturn("Error: no camera exists");
 capture.DV_Capture();
 assertTrue(capture.m_Error);
 }

 @Test
 public void testDV_Capture_Success() throws IOException, InterruptedException {
 captureWithParams = new Capture(1, 2, guiMock);
 when(outputDirMock.exists()).thenReturn(true);
 when(outputDirMock.isDirectory()).thenReturn(true);
 when(dirMock.exists()).thenReturn(true);
 when(dirMock.isDirectory()).thenReturn(true);
 when(processMock.waitFor()).thenReturn(0);
 capture.DV_Capture();
 assertFalse(capture.m_Error);
 }

 @Test
 public void testCapture_InterruptedException() throws IOException, InterruptedException {
 captureWithParams = new Capture(1, 2, guiMock);
 when(outputDirMock.exists()).thenReturn(true);
 when(outputDirMock.isDirectory()).thenReturn(true);
 when(dirMock.exists()).thenReturn(true);
 when(dirMock.isDirectory()).thenReturn(true);
 doThrow(new InterruptedException("Interrupted")).when(threadMock).interrupt();
 capture.DV_Capture();
 assertTrue(capture.m_Error);
 }
}
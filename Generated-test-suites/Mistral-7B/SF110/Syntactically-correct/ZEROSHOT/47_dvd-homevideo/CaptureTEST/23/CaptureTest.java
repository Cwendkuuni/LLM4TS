import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentsMatchers;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CaptureTest {

 private Capture capture;
 private GUI guiMock;
 private ByteArrayInputStream inputStreamMock;
 private BufferedReader bufferedReaderMock;
 private BufferedWriter bufferedWriterMock;
 private Process processMock;

 @Before
 public void setUp() {
 guiMock = Mockito.mock(GUI.class);
 inputStreamMock = new ByteArrayInputStream("dvgrab output".getBytes());
 bufferedReaderMock = Mockito.mock(BufferedReader.class);
 bufferedWriterMock = Mockito.mock(BufferedWriter.class);
 processMock = Mockito.mock(Process.class);

 Mockito.when(guiMock.MessageBox(ArgumentsMatchers.anyString(), ArgumentsMatchers.anyInt())).thenReturn(0);
 Mockito.when(guiMock.strOutputDir).thenReturn("outputDirectory");
 Mockito.when(guiMock.MenuBox(ArgumentsMatchers.anyString())).thenReturn(0);
 Mockito.when(guiMock.ErrorCheck(ArgumentsMatchers.anyString())).thenReturn(true);
 Mockito.when(guiMock.txtAreaOutput).thenReturn(Mockito.mock(TextArea.class));
 Mockito.when(guiMock.prgCapture).thenReturn(Mockito.mock(ProgressBar.class));
 Mockito.when(guiMock.lblCapture).thenReturn(Mockito.mock(Label.class));
 Mockito.when(guiMock.lblCaptureProg).thenReturn(Mockito.mock(Label.class));

 capture = new Capture(1, 2, guiMock);
 }

 @After
 public void tearDown() {
 capture = null;
 guiMock = null;
 inputStreamMock = null;
 bufferedReaderMock = null;
 bufferedWriterMock = null;
 processMock = null;
 }

 @Test
 public void testInit() throws IOException, InterruptedException {
 // Test case when thread checkbox is not selected
 capture.init();
 assertFalse(capture.m_Error);
 // Test case when thread checkbox is selected and capture is successful
 Mockito.when(processMock.waitFor()).thenReturn(0);
 capture = new Capture(1, 2, guiMock);
 capture.m_Thread = new Thread(capture);
 capture.m_Thread.start();
 capture.init();
 assertFalse(capture.m_Error);
 // Test case when thread checkbox is selected and capture fails
 Mockito.when(processMock.waitFor()).thenThrow(new InterruptedException());
 capture = new Capture(1, 2, guiMock);
 capture.m_Thread = new Thread(capture);
 capture.m_Thread.start();
 capture.init();
 assertTrue(capture.m_Error);
 }

 @Test
 public void testRun() throws IOException, InterruptedException {
 // Test case when output directory already exists and user chooses to delete it
 Mockito.when(bufferedReaderMock.ready()).thenReturn(true);
 Mockito.when(bufferedReaderMock.readLine()).thenReturn("/bin/sh: dvgrab: command not found");
 capture.DV_Capture();
 assertTrue(capture.m_Error);
 // Test case when output directory already exists and user chooses to keep it
 Mockito.when(bufferedReaderMock.ready()).thenReturn(false);
 capture = new Capture(1, 2, guiMock);
 capture.DV_Capture();
 assertFalse(capture.m_Error);
 // Test case when output directory does not exist and user chooses to create it
 Mockito.when(bufferedReaderMock.ready()).thenReturn(false);
 Mockito.when(bufferedWriterMock.write(ArgumentsMatchers.anyString())).thenReturn(0);
 capture = new Capture(1, 2, guiMock);
 capture.DV_Capture();
 assertFalse(capture.m_Error);
 }

}
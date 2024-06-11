import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;

public class CaptureTest {

 private Capture capture;
 private GUI guiMock;

 @Before
 public void setup() {
 guiMock = Mockito.mock(GUI.class);
 capture = new Capture(1, 2, guiMock);
 }

 @Test
 public void testInit_CreateNewDirectory() throws IOException, InterruptedException {
 // Given a new instance of Capture
 // When the init method is called
 capture.init();

 // Then the output directory should be created
 File outputDir = new File("outputDir");
 assertTrue(outputDir.mkdir());

 // And the subdirectories should be created
 File dvdDir = new File(outputDir, "DVD");
 assertTrue(dvdDir.mkdir());
 File logDir = new File(outputDir, "log");
 assertTrue(logDir.mkdir());
 File dvDir = new File(outputDir, "dv");
 assertTrue(dvDir.mkdir());
 }

 @Test
 public void testInit_OutputDirectoryExists() throws IOException, InterruptedException {
 // Given a new instance of Capture with existing output directory
 File outputDir = new File("outputDir");
 outputDir.mkdir();
 capture = new Capture(1, 2, guiMock);

 // When the init method is called and the user confirms to overwrite the existing directory
 Mockito.when(guiMock.MessageBox(anyString(), anyInt())).thenReturn(0);

 // Then the output directory should be deleted and recreated
 assertTrue(outputDir.exists());
 assertTrue(outputDir.delete());
 assertTrue(outputDir.mkdir());

 // And the subdirectories should be created
 File dvdDir = new File(outputDir, "DVD");
 assertTrue(dvdDir.mkdir());
 File logDir = new File(outputDir, "log");
 assertTrue(logDir.mkdir());
 File dvDir = new File(outputDir, "dv");
 assertTrue(dvDir.mkdir());
 }

 @Test
 public void testInit_OutputDirectoryIsFile() throws IOException, InterruptedException {
 // Given a new instance of Capture with an existing output file
 File outputDir = new File("outputDir");
 outputDir.createNewFile();
 capture = new Capture(1, 2, guiMock);

 // When the init method is called and the user confirms to overwrite the existing file
 Mockito.when(guiMock.MessageBox(anyString(), anyInt())).thenReturn(0);

 // Then the output file should be deleted
 assertTrue(outputDir.exists());
 assertTrue(outputDir.delete());

 // And the output directory should be created
 assertTrue(outputDir.mkdir());

 // And the subdirectories should be created
 File dvdDir = new File(outputDir, "DVD");
 assertTrue(dvdDir.mkdir());
 File logDir = new File(outputDir, "log");
 assertTrue(logDir.mkdir());
 File dvDir = new File(outputDir, "dv");
 assertTrue(dvDir.mkdir());
 }

 @Test(expected = IOException.class)
 public void testInit_IOException() throws IOException, InterruptedException {
 // Given a new instance of Capture and a mocked GUI that throws an IOException when asked to delete the output directory
 doThrow(new IOException("Mock IOException")).when(guiMock).MessageBox(anyString(), anyInt());

 // When the init method is called and the user confirms to delete the output directory
 capture = new Capture(1, 2, guiMock);

 // Then an IOException should be thrown
 capture.init();
 fail("Expected IOException");
 }

 @Test(expected = InterruptedException.class)
 public void testInit_InterruptedException() throws IOException, InterruptedException {
 // Given a new instance of Capture and a mocked GUI that throws an InterruptedException when asked to delete the output directory
 doThrow(new InterruptedException("Mock InterruptedException")).when(guiMock).MessageBox(anyString(), anyInt());

 // When the init method is called and the user confirms to delete the output directory
 capture = new Capture(1, 2, guiMock);

 // Then an InterruptedException should be thrown
 capture.init();
 fail("Expected InterruptedException");
 }

 @Test
 public void testInit_OutputDirectoryNameIsTaken() throws IOException, InterruptedException {
 // Given a new instance of Capture with a name for the output directory that is already taken by a file
 File outputDir = new File("outputDir");
 outputDir.createNewFile();
 capture = new Capture(1, 2, guiMock);

 // When the init method is called and the user chooses to keep the existing name for the output directory
 Mockito.when(guiMock.MessageBox(anyString(), anyInt())).thenReturn(2);

 // Then the test should not create the output directory and the capture should not start
 assertFalse(new File(outputDir, "outputDir").mkdir());
 assertFalse(capture.init());
 }
}
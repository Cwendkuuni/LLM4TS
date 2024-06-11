import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.lang.NullPointerException;

public class CaptureTest {

 private Capture capture;
 private GUI guiMock;

 @Before
 public void setUp() {
 guiMock = mock(GUI.class);
 capture = new Capture(1, 2, guiMock);
 }

 @Test
 public void testInit_OutputDirectoryAlreadyExists_DeleteIt() throws IOException, InterruptedException {
 when(guiMock.MessageBox(eq("The Output Directory already exists!"), anyInt())).thenReturn(0);
 File outputDir = new File("outputDir");
 when(guiMock.strOutputDir).thenReturn(outputDir.getAbsolutePath());
 when(guiMock.MenuBox(eq("Would you like to delete it?"), eq(0))).thenReturn(0);

 capture.init();
 verify(guiMock).MessageBox(eq("Please choose a different name for your title or move the directory to another location."), eq(2));
 verify(guiMock).blnBegin = false;

 File outputDirExpected = new File("outputDir");
 assertTrue(outputDirExpected.exists() && !outputDirExpected.isDirectory());
 File dir = new File(outputDirExpected, "dv");
 assertTrue(dir.exists());
 File logDir = new File(outputDirExpected, "log");
 assertTrue(logDir.exists());
 }

 @Test
 public void testInit_OutputDirectoryExistsButIsFile_DeleteIt() throws IOException, InterruptedException {
 when(guiMock.MessageBox(eq("There exists a file with the Output directory name at this location!  Would you like to delete it?"), anyInt())).thenReturn(0);
 File outputDir = new File("outputDir");
 when(guiMock.strOutputDir).thenReturn(outputDir.getAbsolutePath());
 File file = new File("outputDir/outputFile.txt");
 when(guiMock.strOutputDir).thenReturn(outputDir.getAbsolutePath());
 when(guiMock.MenuBox(eq("Would you like to delete it?"), eq(0))).thenReturn(0);

 capture.init();
 verify(guiMock).MessageBox(eq("Please choose a different name for your title or move the file to another location."), eq(2));
 assertTrue(file.exists() && file.delete());
 }

 @Test(expected = NullPointerException.class)
 public void testInit_GUIIsNull() throws IOException, InterruptedException {
 when(guiMock.MessageBox(anyString(), anyInt())).thenThrow(NullPointerException.class);
 capture = new Capture(1, 2, null);
 }

 @Test
 public void testDV_Capture_dvgrabCommandNotFound() throws IOException, InterruptedException {
 when(guiMock.MessageBox(eq("Could not locate dvgrab in your path.\nPlease install all necessary dependencies and rerun dvd-homevideo."), eq(0))).thenReturn(0);
 String dvgrab = "dvgrabCommandNotfound";
 capture = new Capture();
 capture.m_dvgrab = dvgrab;
 capture.init();
 verify(guiMock).MessageBox(eq("Could not locate dvgrab in your path.\nPlease install all necessary dependencies and rerun dvd-homevideo."), eq(0));
 }

 @Test
 public void testDV_Capture_CameraNotConnected() throws IOException, InterruptedException {
 when(guiMock.MessageBox(eq("It appears that your camcorder is not connected to your computer!"), eq(0))).thenReturn(0);
 when(guiMock.strOutputDir).thenReturn("outputDir");
 when(guiMock.MenuBox(eq("Would you like to delete it?"), eq(0))).thenReturn(0);
 capture = new Capture();
 capture.init();
 verify(guiMock).MessageBox(eq("It appears that your camcorder is not connected to your computer!"), eq(0));
 }
}
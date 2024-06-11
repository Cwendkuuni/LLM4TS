import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class CaptureTest {

 private Capture capture;
 private GUI mockedGUI;

 @Before
 public void setUp() {
 mockedGUI = mock(GUI.class);
 capture = new Capture(1, 2, mockedGUI);
 }

 @After
 public void tearDown() {
 capture = null;
 mockedGUI = null;
 }

 @Test
 public void testInit_WhenGUIMenuCheckThreadIsSelected_ThreadShouldBeStarted() {
 when(mockedGUI.menuChkThread.isSelected()).thenReturn(true);

 capture.init();

 assertTrue(capture.m_Thread.isAlive());
 }

 @Test
 public void testInit_WhenGUIMenuCheckThreadIsNotSelected_ThreadShouldNotBeStarted() {
 when(mockedGUI.menuChkThread.isSelected()).thenReturn(false);

 capture.init();

 assertFalse(capture.m_Thread.isAlive());
 }

 @Test(expected = IllegalStateException.class)
 public void testInit_WhenBothOutputDirAndFileExists_ShouldThrowException() {
 File outputDir = new File("outputDir");
 outputDir.mkdirs();
 File outputFile = new File(outputDir, "outputFile");
 outputFile.createNewFile();

 when(mockedGUI.strOutputDir).thenReturn(outputDir.getAbsolutePath());

 capture.init();
 }

 @Test
 public void testInit_WhenOutputDirExistsButIsNotADirectory_ShouldAskUserToChooseADifferentNameOrLocation() {
 File outputDir = new File("outputDir");
 outputDir.createNewFile();

 when(mockedGUI.strOutputDir).thenReturn(outputDir.getAbsolutePath());

 capture.init();

 assertTrue(mockedGUI.MessageBox.wasCalled());
 }

 @Test
 public void testInit_WhenOutputDirDoesNotExist_ShouldCreateIt() {
 File outputDir = new File("outputDir");

 when(mockedGUI.strOutputDir).thenReturn(outputDir.getAbsolutePath());

 capture.init();

 assertTrue(outputDir.exists());
 }

 @Test
 public void testDV_Capture_WhenDvgrabIsNotInstalled_ShouldShowErrorMessage() {
 when(mockedGUI.MessageBox).thenAnswer(invocation -> {
 String message = invocation.getArgument(0);
 int response = invocation.getArgument(1);

 System.out.println(message);
 return response;
 });

 capture.DV_Capture();

 String expectedMessage = "Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo.";
 assertTrue(System.out.contains(expectedMessage));
 }

}
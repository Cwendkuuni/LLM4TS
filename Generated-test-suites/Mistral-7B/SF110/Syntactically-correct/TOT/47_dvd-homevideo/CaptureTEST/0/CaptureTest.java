import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;

public class CaptureTest {

 private Capture capture;
 private GUI mockGUI;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 capture = new Capture();
 mockGUI = Mockito.mock(GUI.class);
 }

 @After
 public void tearDown() {
 capture = null;
 mockGUI = null;
 }

 @Test
 public void testInit_validParameters() {
 // Given: valid parameters
 capture = new Capture(1, 2, mockGUI);
 // When: initializing the capture object
 boolean actual = capture.init();
 // Then: initialization should be successful
 assertFalse(actual);
 }

 @Test
 public void testInit_guiMenuChkThreadNotSelected() {
 // Given: GUI menu check thread not selected
 Mockito.when(mockGUI.menuChkThread.isSelected()).thenReturn(false);
 // When: initializing the capture object
 capture = new Capture();
 capture.init();
 // Then: DV_Capture method should be called
 verify(mockGUI).DV_Capture();
 }

 @Test(expected = IllegalStateException.class)
 public void testInit_guiMenuChkThreadSelected_threadAlreadyRunning() {
 // Given: GUI menu check thread selected and thread already running
 Capture existingCapture = new Capture();
 existingCapture.m_Thread = new Thread(existingCapture);
 existingCapture.m_Thread.start();
 capture = new Capture(1, 2, mockGUI);
 // When: initializing the capture object
 capture.init();
 // Then: an IllegalStateException should be thrown
 }

 @Test
 public void testInit_guiDoesNotExist() {
 // Given: GUI does not exist
 mockGUI = null;
 // When: initializing the capture object
 capture = new Capture(1, 2, mockGUI);
 // Then: initialization should fail with an error
 assertTrue(capture.m_Error);
 }

 @Test
 public void testDV_Capture_outputDirectoryExistsAndIsDirectory() {
 // Given: output directory exists and is a directory
 File outputDir = new File("outputDir");
 outputDir.mkdir();
 capture = new Capture(mockGUI, 1, 2);
 capture.m_GUI = mockGUI;
 capture.m_GUI.strOutputDir = outputDir.getAbsolutePath();
 // When: calling DV_Capture method
 capture.DV_Capture();
 // Then: method should not throw any exception
 }

 @Test
 public void testDV_Capture_outputDirectoryExistsAndIsFile() {
 // Given: output directory exists and is a file
 File outputDir = new File("outputDir");
 outputDir.createNewFile();
 capture = new Capture(mockGUI, 1, 2);
 capture.m_GUI = mockGUI;
 capture.m_GUI.strOutputDir = outputDir.getAbsolutePath();
 // When: calling DV_Capture method
 capture.DV_Capture();
 // Then: a message box should be displayed asking to choose a different name for the title or move the file to another location
 Mockito.verify(mockGUI).MessageBox("Please choose a different name for your title or move the file to another location.", 2);
 }

 @Test
 public void testDV_Capture_dvgrabNotExecuted() {
 // Given: dvgrab not executed
 String dvgrabCmd = "dvgrab --autosplit --size 0 --format raw --opendml --buffers 200 dv/dv_file-";
 String expected = "Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo.";
 Mockito.doNothing().when(mockGUI).MessageBox(expected, 0);
 capture = new Capture(mockGUI, 1, 2);
 capture.m_GUI = mockGUI;
 capture.m_GUI.strOutputDir = "outputDir";
 // When: calling DV_Capture method
 capture.DV_Capture();
 // Then: no lines should be written to the log file
 File logFile = new File("outputDir/log/dvgrab.log");
 assertFalse(logFile.exists());
 }

 @Test
 public void testDV_Capture_dvgrabExecutedSuccessfully() throws IOException {
 // Given: dvgrab executed successfully
 String dvgrabCmd = "dvgrab --autosplit --size 0 --format raw --opendml --buffers 200 dv/dv_file-";
 String expectedOutput = "dvgrab command output";
 String expectedError = "dvgrab command error output";
 String logFilePath = "outputDir/log/dvgrab.log";
 File outputDir = new File("outputDir");
 outputDir.mkdirs();
 File logFile = new File(logFilePath);
 try (PrintWriter logWriter = new PrintWriter(logFile)) {
 logWriter.println(expectedOutput);
 logWriter.println(expectedError);
 }
 Mockito.when(mockGUI.MessageBox(anyString(), intThat(isA(Integer.class)).and(isA(Integer.class))))
 .thenAnswer(invocation -> {
 int response = invocation.getArgument(0).intValue();
 if (response == 0) {
 return true;
 }
 return false;
 });
 capture = new Capture(mockGUI, 1, 2);
 capture.m_GUI = mockGUI;
 capture.m_GUI.strOutputDir = outputDir.getAbsolutePath();
 // When: calling DV_Capture method
 capture.DV_Capture();
 // Then: expected output and error lines should be written to the log file
 File actualLogFile = new File(logFilePath);
 assertFalse(actualLogFile.exists());
 capture.DV_Capture();
 assertTrue(actualLogFile.exists());
 String actualOutput = readFileToString(actualLogFile);
 String expectedOutputInFile = expectedOutput + "\n" + expectedError;
 assertEquals(expectedOutputInFile, actualOutput);
 }

 @Test
 public void testDV_Capture_dvgrabExecutedWithError() throws IOException, InterruptedException {
 // Given: dvgrab executed with an error
 String dvgrabCmd = "dvgrab --autosplit --size 0 --format raw --opendml --buffers 200 dv/dv_file-";
 String expectedOutput = "dvgrab command output";
 String expectedError = "dvgrab command error output";
 String logFilePath = "outputDir/log/dvgrab.log";
 File outputDir = new File("outputDir");
 outputDir.mkdirs();
 File logFile = new File(logFilePath);
 try (PrintWriter logWriter = new PrintWriter(logFile)) {
 logWriter.println(expectedOutput);
 logWriter.println(expectedError);
 }
 Mockito.when(mockGUI.MessageBox(anyString(), intThat(isA(Integer.class)).and(isA(Integer.class))))
 .thenAnswer(invocation -> {
 int response = invocation.getArgument(0).intValue();
 if (response == 0) {
 return true;
 }
 return false;
 });
 capture = new Capture(mockGUI, 1, 2);
 capture.m_GUI = mockGUI;
 capture.m_GUI.strOutputDir = outputDir.getAbsolutePath();
 // When: calling DV_Capture method
 capture.DV_Capture();
 // Then: expected output and error lines should be written to the log file
 File actualLogFile = new File(logFilePath);
 assertFalse(actualLogFile.exists());
 capture.DV_Capture();
 assertTrue(actualLogFile.exists());
 String actualOutput = readFileToString(actualLogFile);
 String expectedOutputInFile = expectedOutput + "\n" + expectedError;
 assertEquals(expectedOutputInFile, actualOutput);
 // And: a message box should be displayed with the error message
 Mockito.verify(mockGUI).MessageBox(expectedError, 0);
 }

 private String readFileToString(File file) throws IOException {
 StringBuilder output = new StringBuilder();
 try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
 String line;
 while ((line = reader.readLine()) != null) {
 output.append(line).append("\n");
 }
 }
 return output.toString();
 }
}
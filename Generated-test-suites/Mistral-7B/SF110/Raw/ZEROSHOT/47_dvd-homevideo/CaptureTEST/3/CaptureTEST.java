import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
 public void testInitWithThreadRunning() {
 capture.init();
 verify(mockedGUI).menuChkThread(false);
 }

 @Test(expected = IllegalStateException.class)
 public void testInitWithThreadNotRunning() {
 capture.m_GUI.menuChkThread.setSelected(false);
 capture.init();
 }

 @Test
 public void testDV_CaptureWithOutputDirectoryExistsButIsNotADirectory() {
 File outputDir = Mockito.spy(new File("output_dir"));
 outputDir.exists = true;
 outputDir.isDirectory = false;
 capture.m_GUI = new GUI();
 capture.m_GUI.strOutputDir = "output_dir";
 capture.init();
 verify(mockedGUI).MessageBox(Mockito.eq("There exists a file with the Output directory name\nat this location!  Would you like to delete it?"), Mockito.eq(2));
 }

 @Test
 public void testDV_CaptureWithOutputDirectoryExistsAndIsADirectory() {
 File outputDir = Mockito.spy(new File("output_dir"));
 outputDir.exists = true;
 outputDir.isDirectory = true;
 capture.m_GUI = new GUI();
 capture.m_GUI.strOutputDir = "output_dir";
 capture.init();
 verify(mockedGUI).MessageBox(Mockito.eq("The Output Directory output_dir already exists!"), Mockito.eq(0));
 }

 @Test
 public void testDV_CaptureWithOutputDirectoryDoesNotExist() {
 File outputDir = Mockito.spy(new File("output_dir"));
 capture.m_GUI = new GUI();
 capture.m_GUI.strOutputDir = "output_dir";
 capture.init();
 verify(mockedGUI).MessageBox(Mockito.eq("Please choose a different name for your\ntitle or move the directory to another location."), Mockito.eq(2));
 }

 @Test
 public void testDV_CaptureWithDvgrabCommandNotFound() {
 String[] dvgrabCmd = {"/bin/sh", "-c", "dvgrab: command not found"};
 Process processMock = Mockito.mock(Process.class);
 BufferedReader inMock = Mockito.mock(BufferedReader.class);
 BufferedReader errInMock = Mockito.mock(BufferedReader.class);
 BufferedWriter outMock = Mockito.mock(BufferedWriter.class);
 BufferedWriter errOutMock = Mockito.mock(BufferedWriter.class);
 when(processMock.getInputStream()).thenReturn(inMock);
 when(processMock.getErrorStream()).thenReturn(errInMock);
 when(Runtime.getRuntime().exec(capture.m_dvgrab.split(" "))).thenReturn(processMock);
 capture.m_GUI = new GUI();
 capture.init();
 verify(mockedGUI).MessageBox(Mockito.eq("Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo."), Mockito.eq(0));
 }

 @Test
 public void testDV_CaptureWithCameraNotConnected() {
 String[] dvgrabCmd = {"/bin/sh", "-c", "Error: no camera exists"};
 Process processMock = Mockito.mock(Process.class);
 BufferedReader inMock = Mockito.mock(BufferedReader.class);
 BufferedReader errInMock = Mockito.mock(BufferedReader.class);
 BufferedWriter outMock = Mockito.mock(BufferedWriter.class);
 BufferedWriter errOutMock = Mockito.mock(BufferedWriter.class);
 when(processMock.getInputStream()).thenReturn(inMock);
 when(processMock.getErrorStream()).thenReturn(errInMock);
 when(Runtime.getRuntime().exec(capture.m_dvgrab.split(" "))).thenReturn(processMock);
 capture.m_GUI = new GUI();
 capture.init();
 }

 @Test
 public void testDV_CaptureWithSuccess() {
 String[] dvgrabCmd = {"/bin/sh", "-c", "dvgrab --autosplit --size 0 --format raw --opendml --buffers 200 dv/dv_file-"};
 Process processMock = Mockito.mock(Process.class);
 BufferedReader inMock = Mockito.mock(BufferedReader.class);
 BufferedReader errInMock = Mockito.mock(BufferedReader.class);
 BufferedWriter outMock = Mockito.mock(BufferedWriter.class);
 BufferedWriter errOutMock = Mockito.mock(BufferedWriter.class);
 when(processMock.getInputStream()).thenReturn(inMock);
 when(processMock.getErrorStream()).thenReturn(errInMock);
 when(Runtime.getRuntime().exec(capture.m_dvgrab.split(" "))).thenReturn(processMock);
 capture.m_GUI = new GUI();
 capture.init();
 verify(mockedGUI, Mockito.times(3)).MessageBox(Mockito.anyString(), Mockito.anyInt());
 }
}
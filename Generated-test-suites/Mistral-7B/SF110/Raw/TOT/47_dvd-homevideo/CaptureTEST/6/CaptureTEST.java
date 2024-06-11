import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CaptureTest {

 private Capture capture;
 private GUI gui;

 @Before
 public void setUp() {
 gui = mock(GUI.class);
 capture = new Capture(0, 0, gui);
 }

 @Test
 public void testInit_ThreadIsSelected() {
 capture.init();
 verify(gui).menuChkThread.isSelected();
 ArgumentCaptor<Runnable> runnableArgumentCaptor = ArgumentCaptor.forClass(Runnable.class);
 verify(gui).setThread(runnableArgumentCaptor.capture());
 verify(gui.menuChkThread).setSelected(false);
 }

 @Test(expected = IllegalStateException.class)
 public void testInit_ThreadNotSelected() {
 capture = new Capture(0, 0, gui);
 capture.init();
 }

 @Test
 public void testDV_Capture_OutputDirectoryExists_DeleteIt() {
 File file = new File("outputDirectory");
 file.mkdir();
 capture = new Capture(0, 0, gui);
 capture.DV_Capture();
 verify(gui).MessageBox(Mockito.anyString(), Mockito.eq(0));
 FileUtils.deleteDirectory(file);
 }

 @Test(expected = IOException.class)
 public void testDV_Capture_OutputDirectoryExists_RenameIt() {
 File file = new File("outputDirectory");
 file.mkdir();
 capture = new Capture(0, 0, gui);
 capture.DV_Capture();
 verify(gui).MessageBox(Mockito.anyString(), Mockito.eq(2));
 }

 @Test
 public void testDV_Capture_OutputDirectoryDoesNotExist() {
 capture = new Capture(0, 0, gui);
 capture.DV_Capture();
 verify(gui).MessageBox(Mockito.anyString(), Mockito.eq(2));
 }

 @Test
 public void testDV_Capture_DvgrabCommandNotFound() {
 Mockito.when(gui.MessageBox(Mockito.anyString(), Mockito.anyInt())).thenReturn(JOptionPane.NO_OPTION);
 capture = new Capture(0, 0, gui);
 capture.DV_Capture();
 verify(gui).MessageBox(Capture.this.m_BaseErr + "Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo.", 0);
 }

 @Test
 public void testDV_Capture_Success() {
 capture = new Capture(0, 0, gui);
 capture.DV_Capture();
 // Verify that the methods that should be called during the DVD capture process were called
 verify(gui).MessageBox(Mockito.anyString(), Mockito.eq(2));
 verify(gui).MessageBox(Mockito.anyString(), Mockito.eq(0));
 }
}
import org.junit.Test;
import org.junit.Assert;
import java.io.File;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CaptureTest {

    @Test
    public void testCapture() {
        Capture capture = new Capture();
        capture.init();
        Assert.assertFalse(capture.m_Error);
    }

    @Test
    public void testCaptureWithNullGUI() {
        Capture capture = new Capture();
        capture.m_GUI = null;
        capture.init();
        Assert.assertTrue(capture.m_Error);
    }

    @Test
    public void testCaptureWithInvalidMinutes() {
        Capture capture = new Capture();
        capture.m_Minutes = -1;
        capture.m_Seconds = 0;
        capture.m_GUI = mock(GUI.class);
        when(capture.m_GUI.ErrorCheck(capture.m_GUI.strOutputDir + "/log/dvgrab.log")).thenReturn(false);
        capture.init();
        Assert.assertTrue(capture.m_Error);
    }

    @Test
    public void testCaptureWithInvalidSeconds() {
        Capture capture = new Capture();
        capture.m_Minutes = 0;
        capture.m_Seconds = -1;
        capture.m_GUI = mock(GUI.class);
        when(capture.m_GUI.ErrorCheck(capture.m_GUI.strOutputDir + "/log/dvgrab.log")).thenReturn(false);
        capture.init();
        Assert.assertTrue(capture.m_Error);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCaptureWithNegativeSeconds() {
        Capture capture = new Capture();
        capture.m_Minutes = 0;
        capture.m_Seconds = -1;
        capture.init();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCaptureWithNegativeMinutes() {
        Capture capture = new Capture();
        capture.m_Minutes = -1;
        capture.m_Seconds = 0;
        capture.init();
    }

    @Test
    public void testCaptureWithExistingOutputDirectory() {
        Capture capture = new Capture();
        capture.m_Minutes = 0;
        capture.m_Seconds = 0;
        capture.m_GUI = mock(GUI.class);
        when(capture.m_GUI.menuChkThread.isSelected()).thenReturn(false);
        when(capture.m_GUI.MessageBox(capture.m_BaseErr + "The Output Directory already exists!" + "\nWould you like to delete it?", 0)).thenReturn(JOptionPane.YES_OPTION);
        when(capture.m_GUI.ErrorCheck(capture.m_GUI.strOutputDir + "/log/dvgrab.log")).thenReturn(false);
        capture.init();
        Assert.assertFalse(capture.m_Error);
    }

    @Test
    public void testCaptureWithExistingFileInOutputDirectory() {
        Capture capture = new Capture();
        capture.m_Minutes = 0;
        capture.m_Seconds = 0;
        capture.m_GUI = mock(GUI.class);
        when(capture.m_GUI.menuChkThread.isSelected()).thenReturn(false);
        when(capture.m_GUI.MessageBox(capture.m_BaseErr + "There exists a file with the Output directory name\nat this location!  Would you like to delete it?", 0)).thenReturn(JOptionPane.YES_OPTION);
        when(capture.m_GUI.ErrorCheck(capture.m_GUI.strOutputDir + "/log/dvgrab.log")).thenReturn(false);
        when(capture.m_GUI.strOutputDir).thenReturn(new File("").getAbsolutePath());
        File outputDir = new File(capture.m_GUI.strOutputDir);
        outputDir.mkdirs();
        File existingFile = new File(outputDir, "existingFile.txt");
        existingFile.createNewFile();
        capture.init();
        Assert.assertTrue(capture.m_Error);
    }

    @Test
    public void testCaptureWithNonExistentOutputDirectory() {
        Capture capture = new Capture();
        capture.m_Minutes = 0;
        capture.m_Seconds = 0;
        capture.m_GUI = mock(GUI.class);
        when(capture.m_GUI.menuChkThread.isSelected()).thenReturn(false);
        when(capture.m_GUI.MessageBox("The Output Directory already exists!\nWould you like to delete it?", 0)).thenReturn(JOptionPane.NO_OPTION);
        when(capture.m_GUI.MessageBox("Please choose a different name for your title or move the directory to another location.", 2)).thenReturn(JOptionPane.OK_OPTION);
        when(capture.m_GUI.ErrorCheck(capture.m_GUI.strOutputDir + "/log/dvgrab.log")).thenReturn(false);
        capture.init();
        Assert.assertTrue(capture.m_Error);
    }

    @Test
    public void testCaptureWithDvgrabCommandNotFound() {
        Capture capture = new Capture();
        capture.m_Minutes = 0;
        capture.m_Seconds = 0;
        capture.m_GUI = mock(GUI.class);
        when(capture.m_GUI.menuChkThread.isSelected()).thenReturn(false);
        when(capture.m_GUI.ErrorCheck(capture.m_GUI.strOutputDir + "/log/dvgrab.log")).thenReturn(false);
        capture.init();
        Assert.assertTrue(capture.m_Error);
    }

    @Test
    public void testCaptureWithCameraNotConnected() {
        Capture capture = new Capture();
        capture.m_Minutes = 0;
        capture.m_Seconds = 0;
        capture.m_GUI = mock(GUI.class);
        when(capture.m_GUI.menuChkThread.isSelected()).thenReturn(false);
        when(capture.m_GUI.MessageBox("Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo.", 0)).thenReturn(JOptionPane.OK_OPTION);
        when(capture.m_GUI.ErrorCheck(capture.m_GUI.strOutputDir + "/log/dvgrab.log")).thenReturn(false);
        capture.init();
        Assert.assertTrue(capture.m_Error);
    }
}
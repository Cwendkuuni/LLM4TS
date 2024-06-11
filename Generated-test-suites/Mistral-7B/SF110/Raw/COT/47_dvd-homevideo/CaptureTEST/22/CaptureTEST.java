import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class CaptureTest {

    @Mock
    private GUI m_GUI;

    private Capture capture;

    @Test
    public void testCaptureConstructor() {
        capture = new Capture();
        Assert.assertNotNull(capture.m_dvgrab);
        Assert.assertNull(capture.m_Minutes);
        Assert.assertNull(capture.m_Seconds);
        Assert.assertNull(capture.m_GUI);
        Assert.assertNull(capture.m_Thread);
        Assert.assertFalse(capture.m_Error);
        Assert.assertNull(capture.m_BaseErr);
    }

    @Test
    public void testCaptureConstructorWithArguments() {
        int min = 5;
        int sec = 10;
        capture = new Capture(min, sec, m_GUI);
        Assert.assertEquals(min, capture.m_Minutes);
        Assert.assertEquals(sec, capture.m_Seconds);
        Assert.assertNotNull(capture.m_GUI);
        Assert.assertNull(capture.m_Thread);
        Assert.assertFalse(capture.m_Error);
        Assert.assertNull(capture.m_BaseErr);
    }

    @Test
    public void testInit_OutputDirectoryExistsAndIsADirectory() throws IOException, InterruptedException {
        Mockito.when(m_GUI.MessageBox(Mockito.anyString(), Mockito.eq(0))).thenReturn(JOptionPane.YES_OPTION);
        capture = new Capture(1, 1, m_GUI);
        Mockito.when(m_GUI.strOutputDir).thenReturn("/test/output/directory");
        File outputDir = new File("/test/output/directory");
        Assert.assertTrue(outputDir.exists());
        Assert.assertTrue(outputDir.isDirectory());
        capture.init();
        Mockito.verify(m_GUI).MessageBox(Mockito.eq("The Output Directory /test/output/directory already exists!" +
                "\nWould you like to delete it?"), Mockito.eq(0));
        Mockito.verify(m_GUI).MessageBox(Mockito.eq("Please choose a different name for your title or move the directory to another location."), Mockito.eq(2));
        Assert.assertTrue(capture.m_Error);
    }

    @Test
    public void testInit_OutputDirectoryExistsButIsNotADirectory() throws IOException, InterruptedException {
        Mockito.when(m_GUI.MessageBox(Mockito.anyString(), Mockito.eq(0))).thenReturn(JOptionPane.YES_OPTION);
        capture = new Capture(1, 1, m_GUI);
        Mockito.when(m_GUI.strOutputDir).thenReturn("/test/output/directory/file.txt");
        File outputDir = new File("/test/output/directory/file.txt");
        Assert.assertTrue(outputDir.exists());
        Assert.assertFalse(outputDir.isDirectory());
        capture.init();
        Mockito.verify(m_GUI).MessageBox(Mockito.eq("There exists a file with the Output directory name at this location!  Would you like to delete it?"), Mockito.eq(0));
        Mockito.verify(m_GUI).MessageBox(Mockito.eq("Please choose a different name for your title or move the file to another location."), Mockito.eq(2));
        Assert.assertTrue(capture.m_Error);
    }

    @Test(expected = NullPointerException.class)
    public void testInit_GUIIsNull() throws IOException, InterruptedException {
        capture = new Capture(1, 1, null);
        capture.init();
    }

    @Test(expected = IOException.class)
    public void testDV_Capture_dvgrabNotFound() {
        capture = new Capture();
        Mockito.when(m_GUI.strOutputDir).thenReturn("/test/output/directory");
        capture.m_GUI = m_GUI;
        Mockito.when(m_GUI.MessageBox(Mockito.anyString(), Mockito.eq(0))).thenReturn(JOptionPane.YES_OPTION);
        File outputDir = new File("/test/output/directory");
        Assert.assertTrue(outputDir.mkdir());
        capture.DV_Capture();
        Mockito.verify(m_GUI).MessageBox(Mockito.eq("Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo."), Mockito.eq(0));
    }

    @Test(expected = NullPointerException.class)
    public void testDV_Capture_GUIIsNull() {
        capture = new Capture();
        File outputDir = new File("/test/output/directory");
        capture.m_GUI = null;
        capture.DV_Capture();
    }

    @Test(expected = InterruptedException.class)
    public void testDV_Capture_ThreadInterrupted() throws IOException, InterruptedException {
        capture = new Capture();
        Mockito.when(m_GUI.strOutputDir).thenReturn("/test/output/directory");
        File outputDir = new File("/test/output/directory");
        capture.m_GUI = m_GUI;
        capture.DV_Capture();
        Thread.currentThread().interrupt();
    }

    @Test
    public void testDV_Capture_Success() throws IOException, InterruptedException {
        capture = new Capture();
        Mockito.when(m_GUI.strOutputDir).thenReturn("/test/output/directory");
        File outputDir = new File("/test/output/directory");
        capture.m_GUI = m_GUI;
        Mockito.when(m_GUI.MessageBox(Mockito.anyString(), Mockito.eq(0))).thenReturn(JOptionPane.YES_OPTION);
        Mockito.when(m_GUI.MessageBox(Mockito.anyString(), Mockito.eq(2))).thenReturn(JOptionPane.OK_OPTION);
        capture.DV_Capture();
        Assert.assertFalse(capture.m_Error);
    }
}
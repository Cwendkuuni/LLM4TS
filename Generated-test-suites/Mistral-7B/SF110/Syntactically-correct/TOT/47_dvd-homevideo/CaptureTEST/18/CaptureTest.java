import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CaptureTest {

    private Capture capture;
    private GUI mockGUI;

    @Before
    public void setUp() {
        mockGUI = mock(GUI.class);
        capture = new Capture();
        capture.m_GUI = mockGUI;
    }

    @Test
    public void testInit_WhenGUICheckThreadIsSelected_ShouldStartCaptureThread() throws InterruptedException, IOException, InvocationTargetException {
        when(mockGUI.menuChkThread.isSelected()).thenReturn(true);
        capture.init();
        verify(capture).setPriority(10);
        verify(capture).start();
    }

    @Test
    public void testInit_WhenGUICheckThreadIsNotSelected_ShouldNotStartCaptureThread() throws InterruptedException {
        when(mockGUI.menuChkThread.isSelected()).thenReturn(false);
        capture.init();
        verify(capture, never()).setPriority(10);
        verify(capture, never()).start();
    }

    @Test
    public void testInit_WhenOutputDirectoryExistsAndIsADirectory_ShouldDeleteItAndCreateANewOne() throws IOException {
        File outputDir = new File("/output/directory");
        when(mockGUI.strOutputDir).thenReturn(outputDir.getAbsolutePath());
        capture.init();

        ArgumentCaptor<String> rmCmdArgCaptor = ArgumentCaptor.forClass(String[].class);
        verify(Runtime.getRuntime()).exec(eq("/bin/sh"), null, eq(outputDir));
        verify(Runtime.getRuntime()).exec(rmCmdArgCaptor.capture());
        assertEquals("rm -fr /output/directory", rmCmdArgCaptor.getValue()[0]);
        assertTrue(outputDir.mkdir());
    }

    @Test
    public void testInit_WhenOutputDirectoryExistsAndIsNotADirectory_ShouldDeleteItAndCreateANewOne() throws IOException {
        File outputDir = new File("/output/directory");
        when(mockGUI.strOutputDir).thenReturn(outputDir.getAbsolutePath());
        outputDir.createNewFile();
        capture.init();

        ArgumentCaptor<String> rmCmdArgCaptor = ArgumentCaptor.forClass(String[].class);
        verify(Runtime.getRuntime()).exec(eq("/bin/sh"), null, eq(outputDir));
        verify(Runtime.getRuntime()).exec(rmCmdArgCaptor.capture());
        assertEquals("rm -fr /output/directory", rmCmdArgCaptor.getValue()[0]);
        assertTrue(outputDir.mkdir());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInit_WhenOutputDirectoryDoesNotExistAndIsNotADirectory_ShouldThrowAnIllegalArgumentException() throws IOException {
        File outputDir = new File("/output/nonExistent/file");
        when(mockGUI.strOutputDir).thenReturn(outputDir.getAbsolutePath());
        capture.init();
    }

    @Test(expected = IOException.class)
    public void testInit_WhenDvgrabCommandIsNotFound_ShouldThrowAnIOException() throws IOException {
        when(mockGUI.strOutputDir).thenReturn("/output/directory");
        when(mockGUI.MessageBox(anyString(), anyInt())).thenReturn(0);
        doThrow(IOException.class).when(mockGUI).MessageBox(anyString(), anyInt());

        capture.init();
    }

    @Test
    public void testDV_Capture_WhenOutputDirectoryExistsAndIsADirectory_ShouldCreateSubDirectoriesAndCaptureVideo() throws IOException, InterruptedException {
        File outputDir = new File("/output/directory");
        when(mockGUI.strOutputDir).thenReturn(outputDir.getAbsolutePath());
        when(mockGUI.MessageBox(anyString(), anyInt())).thenReturn(0);

        capture.DV_Capture();

        assertTrue(outputDir.exists() && outputDir.isDirectory());
        File dvDir = new File(outputDir, "dv");
        assertTrue(dvDir.exists() && dvDir.isDirectory());
        File logDir = new File(outputDir, "log");
        assertTrue(logDir.exists() && logDir.isDirectory());
        File file = new File(outputDir, "dv_file-000001.raw");
        assertTrue(file.createNewFile());

        verifyZeroInteractions(mockGUI);
    }

    @Test
    public void testDV_Capture_WhenOutputDirectoryExistsAndIsNotADirectory_ShouldDeleteItAndCreateANewDirectory() throws IOException {
        File outputDir = new File("/output/directory");
        outputDir.createNewFile();

        when(mockGUI.strOutputDir).thenReturn(outputDir.getAbsolutePath());
        when(mockGUI.MessageBox(anyString(), anyInt())).thenReturn(0);

        capture.DV_Capture();

        assertTrue(outputDir.delete());
        assertTrue(outputDir.mkdir());

        verifyZeroInteractions(mockGUI);
    }

    @Test
    public void testDV_Capture_WhenOutputDirectoryDoesNotExist_ShouldCreateANewDirectory() throws IOException {
        File outputDir = new File("/output/directory");
        when(mockGUI.strOutputDir).thenReturn(outputDir.getAbsolutePath());
        when(mockGUI.MessageBox(anyString(), anyInt())).thenReturn(0);

        capture.DV_Capture();

        assertTrue(outputDir.mkdir());

        verifyZeroInteractions(mockGUI);
    }

    @Test(expected = IOException.class)
    public void testDV_Capture_WhenOutputDirectoryExistsAndIsADirectoryButCannotCreateSubDirectories_ShouldThrowAnIOException() throws IOException {
        File outputDir = new File("/output/directory");
        when(mockGUI.strOutputDir).thenReturn(outputDir.getAbsolutePath());
        when(mockGUI.MessageBox(anyString(), anyInt())).thenReturn(0);
        doThrow(IOException.class).when(mockGUI).mkdirs(eq(new File(outputDir, "DVD")));

        capture.DV_Capture();
    }

    @Test(expected = IOException.class)
    public void testDV_Capture_WhenOutputDirectoryExistsAndIsADirectoryButCannotCreateLogDirectory_ShouldThrowAnIOException() throws IOException {
        File outputDir = new File("/output/directory");
        when(mockGUI.strOutputDir).thenReturn(outputDir.getAbsolutePath());
        when(mockGUI.MessageBox(anyString(), anyInt())).thenReturn(0);
        doThrow(IOException.class).when(mockGUI).mkdirs(eq(new File(outputDir, "log")));

        capture.DV_Capture();
    }

    @Test
    public void testDV_Capture_WhenOutputDirectoryExistsAndIsADirectoryButCannotCreateLogFile_ShouldCreateANewLogFile() throws IOException {
        File outputDir = new File("/output/directory");
        when(mockGUI.strOutputDir).thenReturn(outputDir.getAbsolutePath());
        when(mockGUI.MessageBox(anyString(), anyInt())).thenReturn(0);
        doThrow(IOException.class).when(mockGUI).createFile(eq(new File(outputDir, "log/dvgrab.log")));

        capture.DV_Capture();

        File logFile = new File(outputDir, "log/dvgrab.log");
        assertTrue(logFile.createNewFile());

        verifyZeroInteractions(mockGUI);
    }

    @Test(expected = IOException.class)
    public void testDV_Capture_WhenOutputDirectoryDoesNotExistButCannotCreateANewDirectory_ShouldThrowAnIOException() throws IOException {
        File outputDir = new File("/output/nonExistent/directory");
        when(mockGUI.strOutputDir).thenReturn(outputDir.getAbsolutePath());
        when(mockGUI.MessageBox(anyString(), anyInt())).thenReturn(0);
        doThrow(IOException.class).when(mockGUI).mkdirs(eq(outputDir));

        capture.DV_Capture();
    }

    @Test(expected = IOException.class)
    public void testDV_Capture_WhenOutputDirectoryExistsAndIsADirectoryButCannotCaptureVideo_ShouldThrowAnIOException() throws IOException {
        File outputDir = new File("/output/directory");
        when(mockGUI.strOutputDir).thenReturn(outputDir.getAbsolutePath());
        when(mockGUI.MessageBox(anyString(), anyInt())).thenReturn(0);
        doThrow(IOException.class).when(mockGUI).exec(anyStringArray(), (FileOutputStream) anyObject(), (PrintWriter) anyObject());

        capture.DV_Capture();
    }

    @Test(expected = NullPointerException.class)
    public void testDV_Capture_WhenOutputDirectoryDoesNotExistButGUIIsNull_ShouldThrowANullPointerException() throws IOException {
        capture = new Capture() {{
            m_GUI = null;
        }};
        capture.DV_Capture();
    }

    @Test(expected = SecurityException.class)
    public void testDV_Capture_WhenOutputDirectoryExistsButUserDoesNotHaveExecutablePermission_ShouldThrowASecurityException() throws IOException {
        File outputDir = new File("/output/directory");
        when(mockGUI.strOutputDir).thenReturn(outputDir.getAbsolutePath());
        doThrow(SecurityException.class).when(Runtime.getRuntime()).exec(anyStringArray(), null, anyFile());

        capture.DV_Capture();
    }
}
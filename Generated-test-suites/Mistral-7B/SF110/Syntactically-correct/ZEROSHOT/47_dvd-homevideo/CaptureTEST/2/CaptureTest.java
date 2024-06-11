import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentsMatchers;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CaptureTest {

    private Capture capture;
    private GUI gui;
    private File outputDir;
    private PrintWriter logOutput;
    private PrintWriter errOutput;

    @Before
    public void setUp() {
        gui = mock(GUI.class);
        outputDir = mock(File.class);
        logOutput = mock(PrintWriter.class);
        errOutput = mock(PrintWriter.class);
        given(gui.strOutputDir).thenReturn(outputDir.getAbsolutePath());
        given(outputDir.exists()).willReturn(true);
        given(outputDir.isDirectory()).willReturn(true);
        given(gui.MessageBox(ArgumentsMatchers.anyString(), ArgumentsMatchers.anyInt())).willReturn(0);
        capture = new Capture(1, 2, gui);
    }

    @Test
    public void testInit_ThreadSelected_StartCaptureThread() {
        Mockito.doNothing().when(capture).DV_Capture();

        capture.init();

        verify(gui).menuChkThread.isSelected();
        verify(this.capture).setPriority(10);
        verify(this.capture).start();
    }

    @Test
    public void testInit_ThreadNotSelected_DoNotStartCaptureThread() {
        Mockito.doNothing().when(capture).init();

        capture.init();

        verify(gui, Mockito.never()).menuChkThread.isSelected();
        verify(this.capture, Mockito.never()).setPriority(10);
        verify(this.capture, Mockito.never()).start();
    }

    @Test
    public void testDV_Capture_OutputDirExistsAndIsDirectory_DeleteDirAndPromptUser() throws IOException {
        File createOutDir = mock(File.class);
        String rmCommand = "rm -fr " + outputDir.getAbsolutePath();
        given(outputDir.exists()).willReturn(true);
        given(outputDir.isDirectory()).willReturn(true);
        given(outputDir.getAbsolutePath()).willReturn(createOutDir.getAbsolutePath());
        given(gui.MessageBox(ArgumentsMatchers.anyString(), ArgumentsMatchers.anyInt())).willReturn(0);

        capture = new Capture(1, 2, gui);
        capture.DV_Capture();

        verify(createOutDir).exists();
        verify(createOutDir).isDirectory();
        verify(gui).MessageBox("The Output Directory " + outputDir.getAbsolutePath() + " already exists!" + "\nWould you like to delete it?", 0);
    }

    @Test
    public void testDV_Capture_OutputDirExistsButIsNotADirectory_DeleteFileAndPromptUser() throws IOException {
        File createOutDir = mock(File.class);
        String rmCommand = "rm -fr " + outputDir.getAbsolutePath();
        given(outputDir.exists()).willReturn(true);
        given(outputDir.isDirectory()).willReturn(false);
        given(outputDir.getAbsolutePath()).willReturn(createOutDir.getAbsolutePath());
        given(gui.MessageBox(ArgumentsMatchers.anyString(), ArgumentsMatchers.anyInt())).willReturn(0);

        capture = new Capture(1, 2, gui);
        capture.DV_Capture();

        verify(createOutDir).exists();
        verify(createOutDir).isFile();
        verify(gui).MessageBox("There exists a file with the Output directory name" +
                " at this location!  Would you like to delete it?", 0);
    }

    @Test
    public void testDV_Capture_OutputDirDoesNotExist_CreateNewDir() throws IOException {
        given(gui.MessageBox(ArgumentsMatchers.anyString(), ArgumentsMatchers.anyInt())).willReturn(0);

        capture = new Capture(1, 2, gui);
        capture.DV_Capture();

        File expectedCreateOutDir = new File(outputDir.getAbsolutePath() + "/DVD");
        File expectedCreateOutDirDvd = new File(outputDir.getAbsolutePath() + "/log");
        File expectedCreateOutDirDv = new File(outputDir.getAbsolutePath() + "/dv");

        given(outputDir.mkdir()).willReturn(true);
        given(outputDir.mkdirs()).willReturn(true);

        capture.DV_Capture();

        verify(outputDir).mkdir();
        verify(outputDir).mkdirs();
        verify(outputDir).mkdirs();

        File actualCreateOutDir = new File(outputDir.getAbsolutePath() + "/DVD");
        File actualCreateOutDirDvd = new File(outputDir.getAbsolutePath() + "/log");
        File actualCreateOutDirDv = new File(outputDir.getAbsolutePath() + "/dv");

        assertTrue(actualCreateOutDir.exists());
        assertTrue(actualCreateOutDirDvd.exists());
        assertTrue(actualCreateOutDirDv.exists());
    }

    @Test
    public void testDV_Capture_OutputDirDoesNotExist_CreateNewDirsWithError() throws IOException {
        given(gui.MessageBox(ArgumentsMatchers.anyString(), ArgumentsMatchers.anyInt())).willReturn(2);

        capture = new Capture(1, 2, gui);
        capture.DV_Capture();

        verify(gui, Mockito.never()).MessageBox(ArgumentsMatchers.anyString(), ArgumentsMatchers.anyInt());
    }

    @Test
    public void testDV_Capture_DvgrabNotFound_ShowMessageBoxAndDoNotCapture() {
        String dvgrabErrorMessage = "/bin/sh: dvgrab: command not found";
        String expectedMessage = "Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo.";

        given(gui.MessageBox(dvgrabErrorMessage, ArgumentsMatchers.anyInt())).willReturn(0);

        capture = new Capture(1, 2, gui);
        capture.DV_Capture();

        verify(gui).MessageBox(expectedMessage, 0);
    }

    @Test
    public void testDV_Capture_CamcorderNotConnected_ShowMessageBoxAndDeleteOutputDir() throws IOException {
        String errorMessage = "Error: no camera exists";
        String rmCommand = "rm -fr " + outputDir.getAbsolutePath();

        given(gui.MessageBox(errorMessage, ArgumentsMatchers.anyInt())).willReturn(0);

        capture = new Capture(1, 2, gui);
        capture.DV_Capture();

        verify(gui).MessageBox(errorMessage, 0);
        verify(outputDir).deleteOnExit();
    }

    @Test
    public void testDV_Capture_CaptureSuccess() throws IOException, InterruptedException {
        String logFilePath = outputDir.getAbsolutePath() + "/log/dvgrab.log";
        String errFilePath = outputDir.getAbsolutePath() + "/log/dvgrab.err";
        String logContent = "This is a log message.";
        String errContent = "This is an error message.";
        given(gui.MessageBox(ArgumentsMatchers.anyString(), ArgumentsMatchers.anyInt())).willReturn(0);

        ByteArrayOutputStream logOutputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errOutputStream = new ByteArrayOutputStream();
        given(logOutput).willReturn(new PrintWriter(new FileWriter(new File(logFilePath))));
        given(errOutput).willReturn(new PrintWriter(new FileWriter(new File(errFilePath))));

        given(gui.txtAreaOutput.append(ArgumentsMatchers.anyString())).willReturn(null);

        capture = new Capture(1, 2, gui);
        capture.DV_Capture();

        verify(gui).MessageBox("The Output Directory " + outputDir.getAbsolutePath() + " already exists!" + "\nWould you like to delete it?", 0);
        verify(gui).MessageBox("Please choose a different name for your\ntitle or move the directory to another location.", 2);

        given(gui.MessageBox("The Output Directory " + outputDir.getAbsolutePath() + " already exists!" + "\nWould you like to delete it?", 0)).willReturn(1);

        given(gui.MessageBox("Please choose a different name for your\ntitle or move the directory to another location.", 2)).willReturn(1);

        capture = new Capture(1, 2, gui);
        capture.DV_Capture();

        verify(gui).MessageBox("The Output Directory " + outputDir.getAbsolutePath() + " already exists!" + "\nWould you like to delete it?", 0);
        verify(gui).MessageBox("Please choose a different name for your\ntitle or move the directory to another location.", 2);
        verify(gui).MessageBox("Please choose a different name for your\ntitle or move the directory to another location.", 2);

        given(gui.MessageBox("Please choose a different name for your\ntitle or move the directory to another location.", 2)).willReturn(0);

        capture = new Capture(1, 2, gui);
        capture.DV_Capture();

        verify(gui).MessageBox("Please choose a different name for your\ntitle or move the directory to another location.", 0);

        given(gui.MessageBox("Please confirm the output directory name.", 0)).willReturn(0);

        capture = new Capture(1, 2, gui);
        capture.DV_Capture();

        verify(gui).MessageBox("Please confirm the output directory name.", 0);

        given(gui.MessageBox("The Output Directory " + outputDir.getAbsolutePath() + " already exists!" + "\nWould you like to delete it?", 0)).willReturn(1);

        capture = new Capture(1, 2, gui);
        capture.DV_Capture();

        verify(gui).MessageBox("The Output Directory " + outputDir.getAbsolutePath() + " already exists!" + "\nWould you like to delete it?", 0);

        given(gui.MessageBox("Do you wish to start the capture process?", 0)).willReturn(0);

        capture = new Capture(1, 2, gui);
        capture.DV_Capture();

        verify(gui).MessageBox("Do you wish to start the capture process?", 0);

        given(gui.MessageBox("Capture started.", 0)).willReturn(0);

        capture = new Capture(1, 2, gui);
        capture.DV_Capture();

        verify(gui).MessageBox("Capture started.", 0);

        given(gui.txtAreaOutput.append(logContent)).willReturn(null);
        given(gui.txtAreaOutput.append(errContent)).willReturn(null);

        capture = new Capture(1, 2, gui);
        capture.DV_Capture();

        verify(logOutput).write(logContent);
        verify(errOutput).write(errContent);
        verify(gui.txtAreaOutput).append(logContent);
        verify(gui.txtAreaOutput).append(errContent);

        File logFile = new File(logFilePath);
        File errFile = new File(errFilePath);

        assertTrue(logFile.exists());
        assertTrue(errFile.exists());

        StringWriter logWriter = new StringWriter();
        StringWriter errWriter = new StringWriter();
        logOutput.close();
        errOutput.close();

        given(gui.txtAreaOutput.append(ArgumentsMatchers.anyString())).willReturn(logWriter);

        capture = new Capture(1, 2, gui);
        capture.DV_Capture();

        String log = logWriter.toString();
        String err = errWriter.toString();

        assertTrue(log.contains(logContent));
        assertTrue(err.contains(errContent));

        given(gui.txtAreaOutput.setCaretPosition(ArgumentsMatchers.anyInt())).willReturn(null);

        capture = new Capture(1, 2, gui);
        capture.DV_Capture();

        verify(gui.txtAreaOutput).setCaretPosition(log.length());
    }
}
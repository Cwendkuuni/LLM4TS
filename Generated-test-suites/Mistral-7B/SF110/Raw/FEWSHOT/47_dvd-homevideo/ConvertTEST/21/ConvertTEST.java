package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.cli.Convert;
import org.apache.commons.cli.GUI;
import org.junit.Before;
import org.junit.Test;

public class ConvertTest {

    private Convert convert;
    private GUI gui;
    private File outputDir;

    @Before
    public void setup() {
        outputDir = new File("output");
        gui = new GUI();
        convert = new Convert("format1", "aspectRatio1", gui);
    }

    @Test
    public void testInit_NoThread() {
        assertFalse(convert.init());
        assertTrue(gui.ErrorCheck(outputDir.getPath() + "/log/transcode.log"));
        assertTrue(gui.ErrorCheck(outputDir.getPath() + "/log/mplex.log"));
    }

    @Test
    public void testInit_ThreadRunning() {
        gui.menuChkThread.setSelected(true);
        try {
            convert.init();
            assertFalse(convert.init());
            assertTrue(gui.ErrorCheck(outputDir.getPath() + "/log/transcode.log"));
            assertTrue(gui.ErrorCheck(outputDir.getPath() + "/log/mplex.log"));
        } catch (InterruptedException e) {
            // InterruptedException is expected
        }
    }

    @Test
    public void testTranscode_ValidCommand() throws IOException, InterruptedException {
        File listDir = new File(outputDir, "dv/");
        String[] videoFiles = listDir.list();

        convert.video_files = videoFiles;
        convert.m_transcode = convert.m_transcode.replaceAll("inp", videoFiles[0]);
        convert.mplex = convert.mplex.replaceAll("inp", videoFiles[0].substring(0, 11));

        Process p = Runtime.getRuntime().exec(convert.m_transcode.split(" "));
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader err_in = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        BufferedWriter out = new BufferedWriter(new FileWriter(outputDir.getPath() + "/log/transcode.log"));
        BufferedWriter err_out = new BufferedWriter(new FileWriter(outputDir.getPath() + "/log/transcode.err"));

        out.write(convert.m_transcode);
        out.newLine();

        String line;
        while ((line = in.readLine()) != null) {
            out.write(line);
            out.newLine();
        }

        in.close();
        out.close();
        err_in.close();
        err_out.close();

        assertFalse(convert.m_Error);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testTranscode_InvalidFile() throws IOException, InterruptedException {
        File listDir = new File(outputDir, "dv/");
        String[] videoFiles = listDir.list();

        convert.video_files = videoFiles;
        convert.m_transcode = convert.m_transcode.replaceAll("inp", videoFiles[0]);
        convert.m_transcode = convert.m_transcode.replaceAll("test1", videoFiles[0].substring(0, 11));
        convert.mplex = convert.mplex.replaceAll("inp", videoFiles[0].substring(0, 11));

        try {
            Process p = Runtime.getRuntime().exec(convert.m_transcode.split(" "));
            p.waitFor();
            fail("Expected StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
            // Expected StringIndexOutOfBoundsException
        }
    }

    @Test
    public void testMatrixFileCreation() throws IOException {
        File matrixFile = new File(outputDir, "matrix.txt");

        convert.matrix();

        assertTrue(matrixFile.exists());
    }
}
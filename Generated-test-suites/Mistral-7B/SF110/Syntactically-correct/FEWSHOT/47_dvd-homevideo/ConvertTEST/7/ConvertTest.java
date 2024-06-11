package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ConvertTest {

    @Test
    public void testNewInstanceWithDvdNtscFormat() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {});
        assertEquals(29.97, convert.m_fps, 0.0);
    }

    @Test
    public void testNewInstanceWithOtherFormat() {
        Convert convert = new Convert("other", "16:9", new GUI() {});
        assertEquals(25.0, convert.m_fps, 0.0);
    }

    @Test
    public void testInitWithoutThreadCheckboxSelected() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {});
        assertFalse(convert.init());
        assertTrue(convert.m_Error || convert.m_GUI.ErrorCheck(convert.m_GUI.strOutputDir + "/log/transcode.log") || convert.m_GUI.ErrorCheck(convert.m_GUI.strOutputDir + "/log/mplex.log"));
    }

    @Test
    public void testInitWithThreadCheckboxSelected() throws InterruptedException {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {});
        convert.m_Thread = new Thread(convert);
        convert.m_Thread.start();
        convert.m_Thread.join();
        assertFalse(convert.init());
        assertTrue(convert.m_Error || convert.m_GUI.ErrorCheck(convert.m_GUI.strOutputDir + "/log/transcode.log") || convert.m_GUI.ErrorCheck(convert.m_GUI.strOutputDir + "/log/mplex.log"));
    }

    @Test
    public void testTranscodeMethod() throws IOException, InterruptedException {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {});
        File ListDir = new File(convert.m_GUI.strOutputDir + "/dv/");
        String[] videoFiles = ListDir.list();
        assertNotNull(videoFiles);
        assertEquals(2, videoFiles.length);
        convert.Transcode();
    }

    @Test
    public void testTranscodeMethodWithError() throws IOException, InterruptedException {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {
            @Override
            public boolean ErrorCheck(String filePath) {
                return true;
            }
        });
        File ListDir = new File(convert.m_GUI.strOutputDir + "/dv/");
        String[] videoFiles = ListDir.list();
        assertNotNull(videoFiles);
        assertEquals(2, videoFiles.length);
        convert.Transcode();
        assertTrue(convert.m_Error);
    }

    @Test
    public void testRunMethod() throws IOException, InterruptedException {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {});
        convert.run();
        assertFalse(convert.init());
        assertTrue(convert.m_Error || convert.m_GUI.ErrorCheck(convert.m_GUI.strOutputDir + "/log/transcode.log") || convert.m_GUI.ErrorCheck(convert.m_GUI.strOutputDir + "/log/mplex.log"));
    }

    @Test
    public void testMatrixMethod() throws IOException {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI() {});
        File matrixFile = new File(convert.m_GUI.strOutputDir + "/matrix.txt");
        assertTrue(matrixFile.exists());
        List<String> lines = Arrays.asList(matrixFile.toString().split("\n"));
        assertEquals(16, lines.size());
        assertTrue(lines.get(0).startsWith("# High resolution INTRA table"));
    }
}
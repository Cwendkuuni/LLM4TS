package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class ConvertTest {

    @Test
    public void testConvertConstructorWithFormatAndAspectRatio() {
        Convert convert = new Convert("dvd-ntsc", "4:3");
        assertNotNull(convert);
        assertEquals("dvd-ntsc", convert.m_Format);
        assertEquals("4:3", convert.m_AspectRatio);
    }

    @Test
    public void testConvertConstructorWithQualityAndFormatAndAspectRatio() {
        Convert convert = new Convert(3, "dvd-ntsc", "4:3");
        assertNotNull(convert);
        assertEquals(3, convert.m_Quality);
        assertEquals("dvd-ntsc", convert.m_Format);
        assertEquals("4:3", convert.m_AspectRatio);
    }

    @Test
    public void testTranscodeIsInitializedWhenThreadNotStarted() {
        Convert convert = new Convert("dvd-ntsc", "4:3");
        assertFalse(convert.m_Error);
    }

    @Test
    public void testInitTranscodesOnThreadStart() throws InterruptedException {
        Convert convert = new Convert("dvd-ntsc", "4:3");
        convert.m_GUI.menuChkThread.setSelected(true);
        convert.init();
        assertTrue(convert.m_Thread.isAlive());
        convert.m_Thread.join();
        assertTrue(convert.m_Error || convert.m_GUI.ErrorCheck(convert.m_GUI.strOutputDir + "/log/transcode.log") || convert.m_GUI.ErrorCheck(convert.m_GUI.strOutputDir + "/log/mplex.log"));
    }

    @Test
    public void testInitTranscodesOnManualStart() throws InterruptedException {
        Convert convert = new Convert("dvd-ntsc", "4:3");
        convert.m_GUI.menuChkThread.setSelected(false);
        convert.init();
        assertFalse(convert.m_Thread.isAlive());
        assertFalse(convert.m_Error || convert.m_GUI.ErrorCheck(convert.m_GUI.strOutputDir + "/log/transcode.log") || convert.m_GUI.ErrorCheck(convert.m_GUI.strOutputDir + "/log/mplex.log"));
    }

    @Test
    public void testTranscodeWithValidFiles() throws IOException, InterruptedException {
        Convert convert = new Convert("dvd-ntsc", "4:3");
        File listDir = new File(convert.m_GUI.strOutputDir + "/dv/");
        convert.init();
        File[] videoFiles = listDir.listFiles();
        for (int i = 0; i < videoFiles.length; i++) {
            convert.video_files = new String[videoFiles.length];
            convert.video_files[i] = videoFiles[i].getName();
            convert.Transcode();
        }
        assertTrue(convert.m_Error || convert.m_GUI.ErrorCheck(convert.m_GUI.strOutputDir + "/log/transcode.log") || convert.m_GUI.ErrorCheck(convert.m_GUI.strOutputDir + "/log/mplex.log"));
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testTranscodeWithInvalidFileIndex() throws IOException, InterruptedException {
        Convert convert = new Convert("dvd-ntsc", "4:3");
        File listDir = new File(convert.m_GUI.strOutputDir + "/dv/");
        convert.init();
        File[] videoFiles = listDir.listFiles();
        convert.video_files = new String[videoFiles.length + 1];
        convert.video_files[videoFiles.length] = "nonExistentFile.dv";
        convert.Transcode();
    }

    @Test
    public void testCanCalculateRemainingTime() {
        Convert convert = new Convert(null, null, null);
        double[] average_fps = { 25.0, 25.0, 25.0, 25.0, 25.0 };
        int total_time = 120 * 60 + 25 * 60 + 30;
        double current_time = 0.0;
        int captureTipHour = 0;
        int captureTipMinute = 0;
        convert.calculateRemainingTime(average_fps, current_time, total_time);
        assertNotNull(captureTipHour);
        assertEquals(0, captureTipHour);
        assertNotNull(captureTipMinute);
        assertEquals(0, captureTipMinute);

        current_time = 105 * 60 + 30 * 60 + 10;
        convert.calculateRemainingTime(average_fps, current_time, total_time);
        assertNotNull(captureTipHour);
        assertEquals(0, captureTipHour);
        assertNotNull(captureTipMinute);
        assertEquals(9, captureTipMinute);

        current_time = 115 * 60 + 30 * 60 + 20;
        convert.calculateRemainingTime(average_fps, current_time, total_time);
        assertNotNull(captureTipHour);
        assertEquals(0, captureTipHour);
        assertNotNull(captureTipMinute);
        assertEquals(2, captureTipMinute);
    }

    @Test
    public void testCanSetProgressToolTip() {
        Convert convert = new Convert(null, null, null);
        int total_time = 120 * 60 + 25 * 60 + 30;
        convert.setProgressToolTip("Less than 5 minutes remaining");
        assertEquals("Less than 5 minutes remaining", convert.progressToolTip);

        convert.setProgressToolTip("5 minutes or more remaining");
        assertEquals("5 minutes or more remaining", convert.progressToolTip);
    }

    @Test
    public void testCanWriteToMatrixFile() throws IOException {
        Convert convert = new Convert(null, null, null);
        File file = new File(convert.m_GUI.strOutputDir + "/matrix.txt");
        convert.matrix();
        assertTrue(file.exists());

        String content = new String(Files.readAllBytes(file.toPath()));
        assertTrue(content.contains("# High resolution INTRA table"));
        assertTrue(content.contains("# TMPEGEnc NON-INTRA table"));
    }

    @Test(expected = IOException.class)
    public void testCannotWriteToMatrixFile() throws IOException {
        Convert convert = new Convert(null, null, null);
        File file = new File(convert.m_GUI.strOutputDir + "/nonExistentFile.txt");
        convert.matrix();
    }
}
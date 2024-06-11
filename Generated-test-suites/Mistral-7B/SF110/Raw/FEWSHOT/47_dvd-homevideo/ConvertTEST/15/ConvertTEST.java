package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ConvertTest {

    private Convert sut;
    private String[] videoFiles;
    private File listDir;
    private GUI gui;

    @Before
    public void setup() {
        gui = mock(GUI.class);
        sut = new Convert("format", "aspectRatio", gui);
        listDir = new File("outputDir/dv/");
        videoFiles = new String[]{"test1.dv", "test2.dv"};
    }

    @Test
    public void testInit_noThread_noError() {
        when(gui.menuChkThread.isSelected()).thenReturn(false);
        sut.init();
        assertTrue(sut.m_Error == false);
    }

    @Test
    public void testInit_noThread_hasError() {
        when(gui.ErrorCheck(Mockito.eq(gui.strOutputDir + "/log/transcode.log"))).thenReturn(true);
        sut.init();
        assertTrue(sut.m_Error == true);
    }

    @Test
    public void testInit_thread_noError() throws InterruptedException {
        when(gui.menuChkThread.isSelected()).thenReturn(true);
        sut.m_Thread = Mockito.mock(Thread.class);
        when(sut.m_Thread.join()).thenReturn(true);
        sut.init();
        assertTrue(sut.m_Error == false);
    }

    @Test
    public void testInit_thread_hasError() throws InterruptedException {
        when(gui.menuChkThread.isSelected()).thenReturn(true);
        when(sut.m_Thread.join()).thenThrow(new InterruptedException());
        sut.init();
        assertTrue(sut.m_Error == true);
    }

    @Test
    public void testTranscode_noFiles() {
        sut.m_Error = false;
        try {
            sut.Transcode();
            assertTrue(sut.m_Error == true);
        }
        catch (IOException e) {
            //Expected
        }
    }

    @Test
    public void testTranscode_oneFile() throws IOException, InterruptedException {
        final String inputFile = "test1.dv";
        when(gui.menuChkThread.isSelected()).thenReturn(false);
        when(listDir.list()).thenReturn(new String[]{inputFile});
        sut.init();
        sut.video_files = new String[]{inputFile};
        sut.Transcode();
        assertTrue(sut.m_Error == false);
        assertEquals(Arrays.asList(inputFile), Arrays.asList(sut.video_files));
    }

    @Test
    public void testTranscode_multipleFiles() throws IOException, InterruptedException {
        final String inputFile1 = "test1.dv";
        final String inputFile2 = "test2.dv";
        when(gui.menuChkThread.isSelected()).thenReturn(false);
        when(listDir.list()).thenReturn(new String[]{inputFile1, inputFile2});
        sut.init();
        sut.video_files = new String[]{inputFile1, inputFile2};
        sut.Transcode();
        assertTrue(sut.m_Error == false);
        assertEquals(Arrays.asList(inputFile1, inputFile2), Arrays.asList(sut.video_files));
    }

    @Test
    public void testTranscode_invalidFile() throws IOException, InterruptedException {
        final String inputFile = "testInvalidFile.xml";
        when(gui.menuChkThread.isSelected()).thenReturn(false);
        when(listDir.list()).thenReturn(new String[]{inputFile});
        sut.init();
        sut.video_files = new String[]{inputFile};
        sut.Transcode();
        assertTrue(sut.m_Error == true);
        assertFalse(sut.video_files.length > 0);
    }
}
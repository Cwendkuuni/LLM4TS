package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ConvertTest {

    private Convert convert;
    private String[] videoFiles;
    private File outputDir;
    private GUI gui;

    @Before
    public void setup() {
        outputDir = new File("outputDir");
        gui = mock(GUI.class);
        convert = new Convert("format", "aspectRatio", gui);
        videoFiles = new File(outputDir, "dv").list();
    }

    @Test
    public void testInitNoThreadCheck() {
        when(gui.menuChkThread.isSelected()).thenReturn(false);
        convert.init();
        assertTrue(convert.m_Error || convert.m_GUI.ErrorCheck(eq(outputDir.getPath() + "/log/transcode.log")) || convert.m_GUI.ErrorCheck(eq(outputDir.getPath() + "/log/mplex.log")));
    }

    @Test
    public void testInitThreadCheck() {
        when(gui.menuChkThread.isSelected()).thenReturn(true);
        convert = new Convert("format", "aspectRatio", gui);
        convert.Thread thread = mock(Thread.class);
        when(thread.join()).thenThrow(new InterruptedException());
        convert.m_Thread = thread;
        convert.init();
        assertTrue(convert.m_Error);
    }

    @Test
    public void testTranscodeNoFiles() {
        convert.thread_track = 0;
        convert.Transcode();
        verify(gui).MessageBox(eq("Transcoding Error - Are there non-standard (not .dv) files in the dv directory?"), eq(0));
    }

    @Test
    public void testTranscodeOneFile() {
        videoFiles = new String[]{"test1.dv"};
        convert.thread_track = 0;
        convert.Transcode();
        verify(gui).txtAreaOutput.append(eq("Could not locate transcode in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo."));
    }

    @Test
    public void testTranscodeMultipleFiles() throws IOException {
        convert.thread_track = 0;
        when(gui.menuChkThread.isSelected()).thenReturn(false);
        convert.init();
        convert.thread_track = 1;
        String transcodeCmd = convert.m_transcode;
        String[] transcodeArgs = transcodeCmd.split(" ");
        File inputFile = new File(outputDir, "dv/test1.dv");
        String inputFileName = inputFile.getName();
        String outputFileName = "test1";
        String muxFileName = "inp.m2v";
        when(gui.strOutputDir).thenReturn(outputDir.getPath());
        when(gui.MessageBox(eq("Could not locate transcode in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo."), eq(0))).thenReturn(true);
        when(gui.MessageBox(anyString(), eq(0))).thenReturn(true);

        convert.Transcode();
        String expectedTranscodeCmd = "/bin/sh -c transcode -i dv/test1.dv -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr aspectRatio -j 0,8,0,8 --print_status 30";
        assertEquals(expectedTranscodeCmd, transcodeCmd);
        assertEquals(Arrays.asList(transcodeArgs[1], transcodeArgs[2], transcodeArgs[3], transcodeArgs[4], transcodeArgs[5], transcodeArgs[6], transcodeArgs[7], transcodeArgs[8], transcodeArgs[9], transcodeArgs[10], transcodeArgs[11], transcodeArgs[12], transcodeArgs[13], transcodeArgs[14], transcodeArgs[15], transcodeArgs[16]), Arrays.asList("-i", "dv/test1.dv", "-m", "test1.ac3", "-o", "test1", "-w", "bitr", "-x", "dv,dv", "-F", "flags", "-y", "mpeg2enc,raw", "-N", "0x2000", "-b", "256", "--encode_fields", "b", "--E", "48000,16,2", "--J", "resample", "--export_prof", "format", "--export_fps", "frames/s", "--export_asr", "aspectRatio", "--j", "0,8,0,8", "--print_status", "30"));
        File transcodeLogFile = new File(outputDir, "log/transcode.log");
        String logText = readFile(transcodeLogFile);
        assertEquals("Could not locate transcode in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo.\n", logText);

        convert.thread_track = 2;
        String mplexCmd = convert.mplex;
        String[] mplexArgs = mplexCmd.split(" ");
        File muxFile = new File(outputDir, "inp.vob");
        String muxFileNameWithoutExtension = muxFile.getName().substring(0, muxFile.getName().length() - 4);
        assertEquals(Arrays.asList(mplexArgs[1], mplexArgs[2], muxFileNameWithoutExtension), Arrays.asList("-c", "8", "-V", "-o", "inp.vob", "inp.m2v", "inp.ac3"));
    }

    private String readFile(File file) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(System.lineSeparator());
            }
        }
        return stringBuilder.toString();
    }

}
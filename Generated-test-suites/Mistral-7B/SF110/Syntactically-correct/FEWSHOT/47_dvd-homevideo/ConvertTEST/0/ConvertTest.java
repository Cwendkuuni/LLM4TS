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

public class ConvertTest {

    private Convert convert;
    private File listDir;
    private String[] videoFiles;
    private GUI gui;

    @Before
    public void setUp() {
        this.gui = mock(GUI.class);
        this.listDir = new File("outputDir");
        this.videoFiles = new String[]{"video1.dv", "video2.dv"};
        this.convert = new Convert("format", "aspectRatio", this.gui);
    }

    @Test
    public void testInitWithNoThread() {
        when(this.gui.menuChkThread.isSelected()).thenReturn(false);
        when(this.listDir.list()).thenReturn(this.videoFiles);
        assertTrue(this.convert.init());
    }

    @Test
    public void testInitWithThread() throws InterruptedException {
        when(this.gui.menuChkThread.isSelected()).thenReturn(true);
        when(this.listDir.list()).thenReturn(this.videoFiles);
        convert.start();
        Thread.sleep(100); // give the thread some time to start
        assertTrue(this.convert.m_Thread.isAlive());
        assertFalse(this.convert.init());
    }

    @Test
    public void testTranscodeCommand() {
        String inp = "video1.dv";
        String test1 = "test1";
        when(this.gui.strOutputDir).thenReturn("outputDir");

        String transcodeCommand = this.convert.generateTranscodeCommand(inp, test1);
        String expectedTranscodeCommand = "/bin/sh -c transcode -i dv/outputDir/video1.dv -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr aspectRatio -j 0,8,0,8 --print_status 30";

        assertEquals(expectedTranscodeCommand, transcodeCommand);
    }

    @Test
    public void testMplexCommand() {
        String inp = "outputDir/video1.m2v";
        String[] videoFiles = {"outputDir/video1.m2v", "outputDir/video2.ac3"};
        when(this.gui.strOutputDir).thenReturn("outputDir");

        String mplexCommand = this.convert.generateMplexCommand(videoFiles);
        String expectedMplexCommand = "/bin/sh -c mplex -f 8 -V -o inp.vob inp.m2v inp.ac3";

        assertEquals(expectedMplexCommand, mplexCommand);
    }

    @Test
    public void testErrorHandling() throws IOException, InterruptedException {
        when(this.gui.menuChkThread.isSelected()).thenReturn(true);
        when(this.listDir.list()).thenThrow(new StringIndexOutOfBoundsException());

        this.convert.start();
        Thread.sleep(100); // give the thread some time to start
        assertTrue(this.convert.m_Error);
    }

    @Test
    public void testTranscodeExecution() throws IOException, InterruptedException {
        when(this.gui.strOutputDir).thenReturn("outputDir");
        when(this.listDir.list()).thenReturn(this.videoFiles);

        this.convert.start();
        Thread.sleep(100); // give the thread some time to start transcode process

        String inp = this.videoFiles[0];
        String test1 = "test1";
        String transcodeCommand = this.convert.generateTranscodeCommand(inp, test1);
        ProcessBuilder pb = new ProcessBuilder("/bin/sh", "-c", transcodeCommand);
        pb.directory(new File("outputDir"));
        Process process = pb.start();

        String line;
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedWriter out = new BufferedWriter(new FileWriter("outputDir/log/transcode.log"));
        out.write(transcodeCommand);
        out.newLine();

        while ((line = in.readLine()) != null) {
            out.write(line);
            out.newLine();
        }

        in.close();
        out.close();
        process.waitFor();

        // Add more assertions to check the expected output of the transcode process
    }

    @Test
    public void testMplexExecution() throws IOException, InterruptedException {
        when(this.gui.strOutputDir).thenReturn("outputDir");
        String[] videoFiles = {"outputDir/video1.m2v", "outputDir/video2.ac3"};

        this.convert.start();
        Thread.sleep(100); // give the thread some time to start mplex process

        String mplexCommand = this.convert.generateMplexCommand(videoFiles);
        ProcessBuilder pb = new ProcessBuilder("/bin/sh", "-c", mplexCommand);
        pb.directory(new File("outputDir"));
        Process process = pb.start();

        String line;
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedWriter out = new BufferedWriter(new FileWriter("outputDir/log/mplex.log"));
        out.write(mplexCommand);
        out.newLine();

        while ((line = in.readLine()) != null) {
            out.write(line);
            out.newLine();
        }

        in.close();
        out.close();
        process.waitFor();

        // Add more assertions to check the expected output of the mplex process
    }
}
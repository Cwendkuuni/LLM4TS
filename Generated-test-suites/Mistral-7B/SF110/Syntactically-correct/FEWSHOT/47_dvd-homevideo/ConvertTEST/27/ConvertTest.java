package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class ConvertTest {

    private Convert convert;
    private File outputDir;
    private GUI dvdGUI;

    @Before
    public void setup() {
        outputDir = new File("output");
        outputDir.mkdirs();
        dvdGUI = new GUI();
        convert = new Convert("format1", "aspectRatio1", dvdGUI);
    }

    @Test
    public void testConvertConstructor() {
        assertEquals("format1", convert.m_Format);
        assertEquals("aspectRatio1", convert.m_AspectRatio);
        assertFalse(convert.m_Error);
        assertEquals(25.0, convert.m_fps);
        assertNull(convert.m_Thread);
        assertNull(convert.m_Error);
        assertNull(convert.m_BaseErr);
        assertNull(convert.m_transcode);
        assertNull(convert.mplex);
        assertNull(convert.m_Bitrate);
        assertNull(convert.video_files);
        assertNull(convert.m_GUI);
        assertNull(convert.m_Thread);
    }

    @Test
    public void testConvertConstructorQuality() {
        convert = new Convert(1, "format1", "aspectRatio1", dvdGUI);
        assertEquals(1, convert.m_Quality);
    }

    @Test
    public void testInitNoThread() {
        assertFalse(convert.init());
    }

    @Test
    public void testInitWithThreadJoin() throws InterruptedException {
        convert = new Convert(0, "format1", "aspectRatio1", dvdGUI);
        convert.init();
        Thread.sleep(100); // give time for thread to start
        assertTrue(convert.m_Thread.isAlive());
        convert.m_Thread.join();
        assertFalse(convert.m_Thread.isAlive());
        assertFalse(convert.m_Error);
    }

    @Test
    public void testInitWithThreadError() throws InterruptedException {
        convert = new Convert(0, "format1", "aspectRatio1", dvdGUI);
        convert.init();
        Thread.sleep(100); // give time for thread to start
        convert.m_Thread.interrupt();
        assertTrue(convert.m_Error);
    }

    @Test
    public void testTranscodeCommand() {
        convert = new Convert("format1", "aspectRatio1", dvdGUI);
        String command = convert.getTranscodeCommand();
        String expected = "transcode -i dv/inp -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr aspectRatio --print_status 30";
        assertEquals(expected, command);
    }

    @Test
    public void testMplexCommand() {
        convert = new Convert("format1", "aspectRatio1", dvdGUI);
        String command = convert.getMplexCommand();
        String expected = "mplex -f 8 -V -o inp.vob inp.m2v inp.ac3";
        assertEquals(expected, command);
    }

    @Test
    public void testGetTranscodeCommandWithQuality() {
        convert = new Convert(1, "format1", "aspectRatio1", dvdGUI);
        String command = convert.getTranscodeCommand();
        String expected = "transcode -i dv/inp -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0.5 -E -10 -K tmpgenc -R 0";
        assertEquals(expected, command);
    }

    @Test
    public void testTranscodeCommandWithFormatDvdNtsc() {
        convert = new Convert("dvd-ntsc", "aspectRatio1", dvdGUI);
        String command = convert.getTranscodeCommand();
        String expected = "transcode -i dv/inp -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps 29.97 --export_asr aspectRatio --print_status 30";
        assertEquals(expected, command);
    }

    @Test
    public void testTranscodeCommandWithFormatDvdPAL() {
        convert = new Convert("dvd-pal", "aspectRatio1", dvdGUI);
        String command = convert.getTranscodeCommand();
        String expected = "transcode -i dv/inp -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps 25.0 --export_asr aspectRatio --print_status 30";
        assertEquals(expected, command);
    }

    @Test
    public void testMatrixCommand() {
        convert = new Convert("format1", "aspectRatio1", dvdGUI);
        convert.matrix();
        File matrixFile = new File(outputDir, "matrix.txt");
        assertTrue(matrixFile.exists());
        matrixFile.deleteOnExit();
    }

    @Test
    public void testMatrixFileContent() throws IOException {
        convert = new Convert("format1", "aspectRatio1", dvdGUI);
        convert.matrix();
        File matrixFile = new File(outputDir, "matrix.txt");
        String content = new String(Files.readAllBytes(matrixFile.toPath()));
        String[] lines = content.split("\n");
        Arrays.stream(lines).limit(15).forEach(line -> {
            String[] tokens = line.split(",");
            int expected = Integer.parseInt(tokens[0]);
            int actual = Integer.parseInt(tokens[1]);
            assertEquals(expected, actual);
        });
        matrixFile.deleteOnExit();
    }

    @Test
    public void testTranscodeCommandWithError() {
        convert = new Convert("format1", "aspectRatio1", dvdGUI);
        String command = convert.getTranscodeCommand();
        assertThrows(IOException.class, () -> {
            Runtime.getRuntime().exec(command.split(" "));
        });
    }

    @Test
    public void testMplexCommandWithError() {
        convert = new Convert("format1", "aspectRatio1", dvdGUI);
        String command = convert.getMplexCommand();
        assertThrows(IOException.class, () -> {
            Runtime.getRuntime().exec(command.split(" "));
        });
    }

    @Test
    public void testGetVideoFiles() throws IOException {
        convert = new Convert("format1", "aspectRatio1", dvdGUI);
        File outputDir = new File("output");
        File dummyFile = new File(outputDir, "dummy.dv");
        File[] videoFiles = convert.getVideoFiles(outputDir);
        assertEquals(0, videoFiles.length);
        dummyFile.createNewFile();
        videoFiles = convert.getVideoFiles(outputDir);
        assertEquals(1, videoFiles.length);
        assertEquals(dummyFile.getName(), videoFiles[0].getName());
        dummyFile.delete();
    }
}
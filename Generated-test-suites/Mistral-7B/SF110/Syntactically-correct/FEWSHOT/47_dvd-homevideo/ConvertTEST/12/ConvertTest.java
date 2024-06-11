package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

public class ConvertTest {

    private Convert convert;
    private String[] videoFiles;
    private String expectedLogFile;

    @Before
    public void setup() {
        convert = new Convert("format", "aspectRatio", new GUI() {
            @Override
            public boolean menuChkThread_isSelected() {
                return true;
            }

            @Override
            public String strOutputDir() {
                return "output_dir";
            }
        });
        videoFiles = new File("output_dir/dv/").list();
        expectedLogFile = "output_dir/log/transcode.log";
    }

    @Test
    public void testInit_ThreadRunning() {
        assertTrue(convert.init());
        assertFalse(convert.m_Error);
    }

    @Test
    public void testInit_ThreadNotRunning() {
        convert.m_GUI.menuChkThread.setSelected(false);
        assertFalse(convert.init());
        assertTrue(convert.m_Error);
    }

    @Test
    public void testInit_TranscodeError() throws Exception {
        convert.m_transcode = "transcode -i dv/inp -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr aspectRatio -j 0,8,0,8 --print_status 30 --error";

        assertFalse(convert.init());
        assertTrue(convert.m_Error);
    }

    @Test
    public void testInit_MplexError() throws Exception {
        convert.m_transcode = "transcode -i dv/inp -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr aspectRatio -j 0,8,0,8 --print_status 30";
        convert.mplex = "mplex -f 8 -V -o inp.vob inp.m2v inp.ac3 --error";

        assertFalse(convert.init());
        assertTrue(convert.m_Error);
    }

    @Test
    public void testTranscode_NoError() throws Exception {
        Class cl = Class.forName("org.apache.commons.cli.Convert");
        Method method = cl.getDeclaredMethod("Transcode", new Class[0]);
        method.setAccessible(true);
        method.invoke(convert);

        File logFile = new File(expectedLogFile);
        assertTrue(logFile.exists());
        logFile.delete();
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testTranscode_InvalidVideoFile() throws Exception {
        Class cl = Class.forName("org.apache.commons.cli.Convert");
        Method method = cl.getDeclaredMethod("Transcode", new Class[0]);
        method.setAccessible(true);
        method.invoke(convert);

        File[] listDir = new File("output_dir/dv/").listFiles();
        String invalidFile = listDir[listDir.length];
        convert.video_files = new String[listDir.length + 1];
        System.arraycopy(listDir, 0, convert.video_files, 0, listDir.length);
        convert.video_files[listDir.length] = invalidFile.getName();

        method.invoke(convert);
    }

    @Test
    public void testTranscode_TranscodeCommandNotFound() throws Exception {
        convert.m_transcode = "transcode -i dv/inp -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr aspectRatio -j 0,8,0,8 --print_status 30";

        Class cl = Class.forName("org.apache.commons.cli.Convert");
        Method method = cl.getDeclaredMethod("Transcode", new Class[0]);
        method.setAccessible(true);
        method.invoke(convert);

        assertEquals("Could not locate transcode in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo.\n", convert.m_GUI.txtAreaOutput.getText());
    }

    @Test
    public void testTranscode_ErrorDuringExecution() throws Exception {
        convert.m_transcode = "transcode -i dv/inp -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr aspectRatio -j 0,8,0,8 --print_status 30 --error";

        Class cl = Class.forName("org.apache.commons.cli.Convert");
        Method method = cl.getDeclaredMethod("Transcode", new Class[0]);
        method.setAccessible(true);
        method.invoke(convert);

        assertEquals("Transcoding Error - Could not locate transcode in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo.\n", convert.m_BaseErr);
        assertTrue(convert.m_Error);
    }
}
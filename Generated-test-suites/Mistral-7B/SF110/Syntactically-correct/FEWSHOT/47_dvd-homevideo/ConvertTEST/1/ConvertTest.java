package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class ConvertTest {

    @Test
    public void testMatrixFileCreation() {
        Convert convert = new Convert("dvd-ntsc", "1.777:1", null);
        assertTrue(convert.matrix() == true);
        File matrixFile = new File(convert.m_GUI.strOutputDir, "matrix.txt");
        assertTrue(matrixFile.exists());
        matrixFile.delete();
    }

    @Test
    public void testInitNoThread() {
        Convert convert = new Convert("dvd-ntsc", "1.777:1", null);
        convert.init();
        assertFalse(convert.m_Error);
    }

    @Test
    public void testInitWithThread() {
        Convert convert = new Convert("dvd-ntsc", "1.777:1", null);
        convert.m_GUI.menuChkThread.setSelected(true);
        convert.init();
        assertTrue(convert.m_Error || convert.m_GUI.ErrorCheck(convert.m_GUI.strOutputDir + "/log/transcode.log") || convert.m_GUI.ErrorCheck(convert.m_GUI.strOutputDir + "/log/mplex.log"));
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testTranscodeCmdReplaceAllWithInvalidIndex() {
        Convert convert = new Convert("dvd-ntsc", "1.777:1", null);
        String[] videoFiles = new String[]{"file1.dv"};
        convert.video_files = videoFiles;
        convert.m_transcode = "transcode -i dv/inp -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr aspectRatio -j 0,8,0,8 --print_status 30";
        convert.m_transcode = convert.m_transcode.replaceAll("inp", videoFiles[0]);
        convert.m_transcode = convert.m_transcode.replaceAll("test1", videoFiles[0].substring(0, 11));
        convert.m_transcode = convert.m_transcode.replaceAll("test1.ac3", "test2.ac3");
        convert.m_transcode = convert.m_transcode.replaceAll("inp.vob", "inp2.vob");
        convert.m_transcode = convert.m_transcode.replaceAll("inp.m2v", "inp3.m2v");
        convert.m_transcode = convert.m_transcode.replaceAll("file", "nonStandardFile");
    }

    @Test
    public void testTranscodeCmdReplaceAllWithValidIndices() {
        Convert convert = new Convert("dvd-ntsc", "1.777:1", null);
        String[] videoFiles = new String[]{"file1.dv", "file2.dv"};
        convert.video_files = videoFiles;
        convert.m_transcode = "transcode -i dv/inp -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr aspectRatio -j 0,8,0,8 --print_status 30";
        convert.m_transcode = convert.m_transcode.replaceAll("inp", videoFiles[0]);
        convert.m_transcode = convert.m_transcode.replaceAll("test1", videoFiles[0].substring(0, 11));
        convert.m_transcode = convert.m_transcode.replaceAll("test1.ac3", videoFiles[1].substring(0, 11) + ".ac3");
        convert.m_transcode = convert.m_transcode.replaceAll("inp.vob", videoFiles[0].substring(0, 11) + ".vob");
        convert.m_transcode = convert.m_transcode.replaceAll("inp.m2v", videoFiles[0].substring(0, 11) + ".m2v");
    }

    @Test
    public void testTranscodeCmdExecutionError() {
        Convert convert = new Convert("dvd-ntsc", "1.777:1", null);
        String[] videoFiles = new String[]{"file1.dv", "file2.dv"};
        convert.video_files = videoFiles;
        convert.m_transcode = "transcode -i dv/inp -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr aspectRatio -j 0,8,0,8 --print_status 30";
        convert.m_transcode = convert.m_transcode.replaceAll("inp", videoFiles[0]);
        convert.m_transcode = convert.m_transcode.replaceAll("test1", videoFiles[0].substring(0, 11));
        convert.m_transcode = convert.m_transcode.replaceAll("test1.ac3", "not_a_file.ac3");
        convert.init();
        assertTrue(convert.m_Error);
    }
}
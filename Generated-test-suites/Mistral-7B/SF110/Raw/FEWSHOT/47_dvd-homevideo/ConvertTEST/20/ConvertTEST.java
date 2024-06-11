package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class ConvertTest {

    @Test
    public void testInit() {
        Convert convert = new Convert("dvd-ntsc", "1:1", new GUI() {
            @Override
            public boolean menuChkThread() {
                return true;
            }
        });
        assertFalse(convert.m_Error);
    }

    @Test
    public void testInitNoThread() {
        Convert convert = new Convert("dvd-ntsc", "1:1", new GUI() {
            @Override
            public boolean menuChkThread() {
                return false;
            }
        });
        assertFalse(convert.m_Error);
        assertTrue(convert.init());
    }

    @Test
    public void testInitError() {
        Convert convert = new Convert("dvd-ntsc", "1:1", new GUI() {
            @Override
            public boolean menuChkThread() {
                return true;
            }

            @Override
            public boolean ErrorCheck(String filePath) {
                return true;
            }
        });
        assertTrue(convert.m_Error);
    }

    @Test
    public void testTranscodeCommand() {
        Convert convert = new Convert("dvd-ntsc", "1:1", new GUI() {
        });
        String command = convert.m_transcode;
        String expected = "transcode -i dv/inp -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr aspectRatio -j 0,8,0,8 --print_status 30";
        assertEquals(expected, command);
    }

    @Test
    public void testTranscodeCommandWithQuality() {
        Convert convert = new Convert(0, "dvd-ntsc", "1:1", new GUI() {
        });
        String command = convert.m_transcode;
        String expected = "transcode -i dv/inp -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags '8, -c -q 2 -4 1 -2 1 -H -K file=matrix.txt -R 2' -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr aspectRatio -j 0,8,0,8 --print_status 30";
        assertEquals(expected, command);
    }

    @Test
    public void testTranscodeCommandWithQuality1() {
        Convert convert = new Convert(1, "dvd-ntsc", "1:1", new GUI() {
        });
        String command = convert.m_transcode;
        String expected = "transcode -i dv/inp -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags '8, -c -q 4 -4 2 -2 2 -K file=matrix.txt -R 1' -y mpeg2enc,raw -N 0.x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr aspectRatio -j 0,8,0,8 --print_status 30";
        assertEquals(expected, command);
    }

    @Test
    public void testTranscodeCommandWithQuality2() {
        Convert convert = new Convert(2, "dvd-ntsc", "1:1", new GUI() {
        });
        String command = convert.m_transcode;
        String expected = "transcode -i dv/inp -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags '8, -c -q 6 -4 3 -2 3 -N 0.5 -E -10 -K tmpgenc -R 0' -y mpeg2enc,raw -N 0.x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr aspectRatio -j 0,8,0,8 --print_status 30";
        assertEquals(expected, command);
    }
}
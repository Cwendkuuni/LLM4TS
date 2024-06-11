package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class ConvertTest {

    @Test
    public void testInit() {
        Convert converter = new Convert("dvd-ntsc", "4:3", new GUI() {
            @Override
            public boolean menuChkThread() {
                return false;
            }
        });
        assertFalse(converter.init());
    }

    @Test
    public void testInitThread() {
        Convert converter = new Convert("dvd-ntsc", "4:3", new GUI() {
            @Override
            public boolean menuChkThread() {
                return true;
            }
        });
        assertTrue(converter.init());
    }

    @Test
    public void testTranscodeCommand() {
        Convert converter = new Convert("dvd-ntsc", "4:3", new GUI() {
            @Override
            public boolean menuChkThread() {
                return true;
            }
        });
        String expectedTranscodeCommand = "transcode -i dv/inp -m test1.ac3 -o test1 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr aspectRatio -j 0,8,0,8 --print_status 30";
        String actualTranscodeCommand = converter.m_transcode;
        assertEquals(expectedTranscodeCommand, actualTranscodeCommand);
    }

    @Test
    public void testMplexCommand() {
        Convert converter = new Convert("dvd-ntsc", "4:3", new GUI() {
            @Override
            public boolean menuChkThread() {
                return true;
            }
        });
        String expectedMplexCommand = "mplex -f 8 -V -o inp.vob inp.m2v inp.ac3";
        String actualMplexCommand = converter.mplex;
        assertEquals(expectedMplexCommand, actualMplexCommand);
    }

    @Test
    public void testGetFps() {
        Convert converterNTSC = new Convert("dvd-ntsc", "4:3", new GUI() {
            @Override
            public boolean menuChkThread() {
                return false;
            }
        });
        double fpsNTSC = converterNTSC.m_fps;
        assertEquals(29.97, fpsNTSC, 0.01);

        Convert converterPAL = new Convert("dvd-pal", "16:9", new GUI() {
            @Override
            public boolean menuChkThread() {
                return false;
            }
        });
        double fpsPAL = converterPAL.m_fps;
        assertEquals(25.0, fpsPAL, 0.01);
    }

    @Test
    public void testThreadTrackInitialization() {
        Convert converter = new Convert("dvd-ntsc", "4:3", new GUI() {
            @Override
            public boolean menuChkThread() {
                return true;
            }
        });
        String[] videoFiles = new File(converter.m_GUI.strOutputDir + "/dv/").list();
        assertEquals(0, converter.thread_track);
        assertEquals(videoFiles[0], converter.video_files[0]);
        assertEquals(videoFiles[1], converter.video_files[1]);
    }

    @Test
    public void testThreadTrackUpdate() {
        Convert converter = new Convert("dvd-ntsc", "4:3", new GUI() {
            @Override
            public boolean menuChkThread() {
                return true;
            }
        });
        String[] videoFiles = new File(converter.m_GUI.strOutputDir + "/dv/").list();
        converter.thread_track = 1;
        assertEquals(videoFiles[1], converter.video_files[0]);
        assertEquals(videoFiles[2], converter.video_files[1]);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testThreadTrackUpdateWithInvalidIndex() {
        Convert converter = new Convert("dvd-ntsc", "4:3", new GUI() {
            @Override
            public boolean menuChkThread() {
                return true;
            }
        });
        String[] videoFiles = new File(converter.m_GUI.strOutputDir + "/dv/").list();
        converter.thread_track = videoFiles.length + 1;
    }

    @Test
    public void testTranscodeCommandUpdate() {
        Convert converter = new Convert("dvd-ntsc", "4:3", new GUI() {
            @Override
            public boolean menuChkThread() {
                return true;
            }
        });
        String[] videoFiles = new File(converter.m_GUI.strOutputDir + "/dv/").list();
        converter.thread_track = 1;
        String expectedTranscodeCommand = "transcode -i dv/inp-001 -m test1.ac3 -o test1-001 -w bitr -x dv,dv -F flags -y mpeg2enc,raw -N 0x2000 -b 256 --encode_fields b -E 48000,16,2 -J resample --export_prof format --export_fps frames/s --export_asr 16:9 -j 0,8,0,8 --print_status 30";
        String actualTranscodeCommand = converter.m_transcode;
        assertEquals(expectedTranscodeCommand, actualTranscodeCommand);
    }

    @Test
    public void testMplexCommandUpdate() {
        Convert converter = new Convert("dvd-ntsc", "4:3", new GUI() {
            @Override
            public boolean menuChkThread() {
                return true;
            }
        });
        String[] videoFiles = new File(converter.m_GUI.strOutputDir + "/dv/").list();
        converter.thread_track = 1;
        String expectedMplexCommand = "mplex -f 8 -V -o inp.vob inp-001.m2v inp-001.ac3";
        String actualMplexCommand = converter.mplex;
        assertEquals(expectedMplexCommand, actualMplexCommand);
    }
}
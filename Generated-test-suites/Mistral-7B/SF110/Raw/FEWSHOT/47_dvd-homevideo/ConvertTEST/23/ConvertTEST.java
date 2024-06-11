package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

public class ConvertTest {

    @Test
    public void testInit() {
        Convert convert = new Convert("dvd-ntsc", "1:1", new GUI() {
            @Override
            public boolean menuChkThread() {
                return false;
            }
        });
        assertFalse(convert.init());

        Convert convert1 = new Convert(0, "dvd-ntsc", "1:1", new GUI() {
            @Override
            public boolean menuChkThread() {
                return true;
            }
        });
        assertTrue(convert1.init());

        Convert convert2 = new Convert(1, "dvd-ntsc", "1:1", new GUI() {
            @Override
            public boolean menuChkThread() {
                return true;
            }
        });
        assertTrue(convert2.init());

        Convert convert3 = new Convert(6, "dvd-ntsc", "1:1", new GUI() {
            @Override
            public boolean menuChkThread() {
                return true;
            }
        });
        assertTrue(convert3.init());
    }

    @Test
    public void testTranscode() {
        Convert convert = new Convert("dvd-ntsc", "1:1", new GUI() {
            @Override
            public boolean menuChkThread() {
                return false;
            }
        });
        convert.Transcode();
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testTranscodeWithInvalidFile() {
        Convert convert = new Convert("dvd-ntsc", "1:1", new GUI() {
            @Override
            public boolean menuChkThread() {
                return true;
            }
        });
        File ListDir = new File("invalid_directory");
        convert.thread_track = 5;
        convert.video_files = ListDir.list();
        convert.Transcode();
    }

    @Test
    public void testMatrix() {
        Convert convert = new Convert("dvd-ntsc", "1:1", new GUI() {
            @Override
            public boolean menuChkThread() {
                return false;
            }
        });
        convert.matrix();
    }
}
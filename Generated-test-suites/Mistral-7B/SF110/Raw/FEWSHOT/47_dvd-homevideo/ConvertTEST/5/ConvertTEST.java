package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class ConvertTest {

    @Test
    public void testInit() {
        Convert testConvert = new Convert("dvd-ntsc", "4:3", new GUI() {
            @Override
            public boolean menuChkThread() {
                return true;
            }
        });
        assertTrue(testConvert.init());

        testConvert = new Convert(5, "dvd-ntsc", "4:3", new GUI() {
            @Override
            public boolean menuChkThread() {
                return true;
            }
        });
        assertTrue(testConvert.init());

        testConvert = new Convert("dvd-ntsc", "4:3", new GUI() {
            @Override
            public boolean menuChkThread() {
                return false;
            }
        });
        assertFalse(testConvert.init());

        testConvert = new Convert(5, "dvd-ntsc", "4:3", new GUI() {
            @Override
            public boolean menuChkThread() {
                return false;
            }
        });
        assertFalse(testConvert.init());
    }

    @Test
    public void testTranscode() {
        Convert testConvert = new Convert("dvd-ntsc", "4:3", new GUI() {
            @Override
            public boolean menuChkThread() {
                return true;
            }
        });
        testConvert.init();
        assertTrue(testConvert.m_Error);

        testConvert = new Convert(5, "dvd-ntsc", "4:3", new GUI() {
            @Override
            public boolean menuChkThread() {
                return true;
            }
        });
        testConvert.init();
        assertFalse(testConvert.m_Error);
    }

    @Test
    public void testMatrix() {
        Convert testConvert = new Convert("dvd-ntsc", "4:3", new GUI() {
            @Override
            public boolean menuChkThread() {
                return true;
            }
        });
        testConvert.init();
        File file = new File(testConvert.m_GUI.strOutputDir + "/matrix.txt");
        assertTrue(file.exists());
        file.delete();
    }
}
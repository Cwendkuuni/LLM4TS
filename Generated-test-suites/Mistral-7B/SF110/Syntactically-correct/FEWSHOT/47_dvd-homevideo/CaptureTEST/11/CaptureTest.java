package org.apache.commons.cli;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaptureTest {
    private Capture capture;
    private GUI mockedGUI;

    @Before
    public void setUp() {
        mockedGUI = new GUI() {}; // Create a mock GUI instance
        capture = new Capture();
        capture.m_GUI = mockedGUI;
    }

    @Test
    public void testInit_NoThreadSelected() {
        assertFalse(capture.init());
        assertEquals("Capture Error - ", capture.m_BaseErr);
        assertEquals(0, capture.m_Minutes);
        assertEquals(0, capture.m_Seconds);
        assertNull(capture.m_Thread);
        assertFalse(capture.m_Error);
        assertNull(capture.m_BaseErr);
        assertNull(capture.m_dvgrab);
    }

    @Test
    public void testInit_ThreadSelected() {
        mockedGUI = new GUI() {
            @Override
            public boolean menuChkThread_isSelected() {
                return true;
            }
        };
        capture = new Capture(1, 2, mockedGUI);
        assertFalse(capture.init());
        assertNull(capture.m_Thread);
        assertTrue(capture.m_Error);
        assertNotNull(capture.m_BaseErr);
        assertNotNull(capture.m_dvgrab);
    }

    @Test
    public void testDV_Capture_OutputDirExistsAsDirectory() {
        mockedGUI = new GUI() {
            @Override
            public String strOutputDir() {
                return "/output/directory";
            }

            @Override
            public int MessageBox(String message, int responseType) {
                return responseType;
            }
        };

        capture = new Capture(1, 2, mockedGUI);
        capture.init();

        File outputDir = new File("/output/directory");
        assertTrue(outputDir.exists());
        assertTrue(outputDir.isDirectory());

        File outputDirDVD = new File(outputDir.getPath() + "/DVD");
        File outputDirLog = new File(outputDir.getPath() + "/log");
        File outputDirDV = new File(outputDir.getPath() + "/dv");

        assertTrue(outputDirDVD.exists());
        assertTrue(outputDirDVD.isDirectory());
        assertTrue(outputDirLog.exists());
        assertTrue(outputDirLog.isDirectory());
        assertTrue(outputDirDV.exists());
        assertTrue(outputDirDV.isDirectory());
    }

    @Test
    public void testDV_Capture_OutputDirExistsAsFile() {
        mockedGUI = new GUI() {
            @Override
            public String strOutputDir() {
                return "/output/directory";
            }

            @Override
            public int MessageBox(String message, int responseType) {
                return 0;
            }
        };

        capture = new Capture(1, 2, mockedGUI);
        capture.init();

        File outputDir = new File("/output/directory");
        assertTrue(outputDir.exists());
        assertFalse(outputDir.isDirectory());

        try {
            File outputDirDVD = new File(outputDir.getPath() + "/DVD");
            File outputDirLog = new File(outputDir.getPath() + "/log");
            File outputDirDV = new File(outputDir.getPath() + "/dv");

            assertFalse(outputDirDVD.exists());
            assertFalse(outputDirDVD.isDirectory());
            assertFalse(outputDirLog.exists());
            assertFalse(outputDirLog.isDirectory());
            assertFalse(outputDirDV.exists());
            assertFalse(outputDirDV.isDirectory());
        } catch (SecurityException e) {
            // Ignore and continue with the test
        }

        assertEquals("Please choose a different name for your title or move the file to another location.", mockedGUI.MessageBox("", 2));
        assertFalse(capture.m_GUI.blnBegin);
        assertTrue(capture.m_Error);
    }

    @Test
    public void testDV_Capture_CommandNotFound() {
        mockedGUI = new GUI() {
            @Override
            public String strOutputDir() {
                return "/output/directory";
            }

            @Override
            public int MessageBox(String message, int responseType) {
                return 0;
            }
        };

        capture = new Capture(1, 2, mockedGUI);
        capture.m_dvgrab = "dvgrab: command not found";
        capture.init();

        String message = mockedGUI.MessageBox("", 0);
        assertEquals("Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo.", message);
        assertTrue(capture.m_Error);
    }

    @After
    public void tearDown() {
        capture = null;
        mockedGUI = null;
    }
}
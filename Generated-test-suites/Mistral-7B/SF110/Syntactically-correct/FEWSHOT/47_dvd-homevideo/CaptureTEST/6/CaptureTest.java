package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class CaptureTest {

    @Test
    public void testInit() {
        Capture capture = new Capture();
        assertFalse(capture.init());
        assertEquals(false, capture.m_Thread.isAlive());

        capture = new Capture(1, 2, new GUI() {
            @Override
            public boolean menuChkThread_isSelected() {
                return false;
            }
        });
        assertTrue(capture.init());
        assertTrue(capture.m_Thread.isAlive());

        capture.m_GUI.menuChkThread.setSelected(true);
        capture.init();
        assertTrue(capture.m_Thread.isAlive());
    }

    @Test
    public void testDV_Capture() {
        Capture capture = new Capture();
        File outputDir = new File("outputDir");

        // Test case for existing directory
        capture.m_GUI = new GUI() {
            @Override
            String strOutputDir() {
                return outputDir.getAbsolutePath();
            }
        };
        assertFalse(capture.init());
        assertFalse(outputDir.exists());

        // Test case for non-empty directory
        outputDir.mkdirs();
        File file = new File(outputDir, "existingFile.txt");
        file.createNewFile();
        capture.m_GUI = new GUI() {
            @Override
            String strOutputDir() {
                return outputDir.getAbsolutePath();
            }
        };
        assertFalse(capture.init());
        assertFalse(outputDir.delete());

        // Test case for directory name conflict
        outputDir = new File("outputDir");
        capture.m_GUI = new GUI() {
            @Override
            String strOutputDir() {
                return outputDir.getAbsolutePath();
            }
        };
        File existingFile = new File("existingFile.txt");
        assertFalse(capture.init());
        assertTrue(existingFile.delete());
        outputDir.delete();

        // Test case for successful initialization
        capture = new Capture(1, 2, new GUI() {
            @Override
            String strOutputDir() {
                return outputDir.getAbsolutePath();
            }
        });
        assertTrue(capture.init());
        assertTrue(outputDir.exists());
        assertTrue(outputDir.isDirectory());
        assertTrue(capture.m_Thread.isAlive());

        // Test dvgrab command execution
        capture = new Capture(1, 2, new GUI() {
            @Override
            String strOutputDir() {
                return outputDir.getAbsolutePath();
            }

            @Override
            String m_dvgrab() {
                return "dvgrab";
            }
        });
        assertTrue(capture.init());
        capture.DV_Capture();
        assertTrue(new File(outputDir, "dv/dv_file-00000.raw").exists());
    }
}
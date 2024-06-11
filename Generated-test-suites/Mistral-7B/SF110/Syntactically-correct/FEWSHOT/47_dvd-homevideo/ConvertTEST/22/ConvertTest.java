package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class ConvertTest {

    @Test
    public void testInit() {
        Convert convert = new Convert("format1", "aspectRatio1", new GUI() {
            @Override
            public boolean menuChkThread_isSelected() {
                return true;
            }
        });
        assertFalse(convert.m_Error);
    }

    @Test
    public void testInit_noThread() {
        Convert convert = new Convert("format1", "aspectRatio1", new GUI() {
            @Override
            public boolean menuChkThread_isSelected() {
                return false;
            }
        });
        assertTrue(convert.init());
    }

    @Test
    public void testTranscode() {
        // Test with valid file names
        Convert convert = new Convert("format1", "aspectRatio1", new GUI() {
            @Override
            public boolean menuChkThread_isSelected() {
                return true;
            }
        });
        convert.init();
        String[] validFiles = {"file1.dv", "file2.dv", "file3.dv"};
        for (String fileName : validFiles) {
            convert.video_files = new String[]{fileName};
            convert.Transcode();
        }

        // Test with invalid file names (StringIndexOutOfBoundsException)
        Convert convert2 = new Convert("format2", "aspectRatio2", new GUI() {
            @Override
            public boolean menuChkThread_isSelected() {
                return true;
            }
        });
        convert2.init();
        String[] invalidFiles = {"file1.mp4", "file2.avi", "file3.dv", "file4.dv"};
        for (String fileName : invalidFiles) {
            try {
                convert2.video_files = new String[]{fileName, "file5.dv", "file6.dv"};
                convert2.Transcode();
                fail("Expected StringIndexOutOfBoundsException");
            } catch (StringIndexOutOfBoundsException e) {
                // Expected
            }
        }
    }

    @Test
    public void testMatrix() {
        Convert convert = new Convert("format1", "aspectRatio1", new GUI() {
            @Override
            public boolean menuChkThread_isSelected() {
                return true;
            }
        });
        convert.init();
        File directory = new File(convert.m_GUI.strOutputDir + "/dv/");
        File matrixFile = new File(directory, "matrix.txt");
        assertTrue(matrixFile.exists());
    }
}
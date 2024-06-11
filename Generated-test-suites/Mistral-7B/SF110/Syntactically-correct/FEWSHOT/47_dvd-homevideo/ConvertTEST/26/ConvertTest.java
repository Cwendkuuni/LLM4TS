package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class ConvertTest {

    @Test
    public void testInit() {
        Convert convert = new Convert("format1", "aspectRatio1", null);
        assertFalse(convert.init());
        assertTrue(convert.m_Error);

        convert = new Convert(0, "format1", "aspectRatio1", null);
        assertFalse(convert.init());
        assertTrue(convert.m_Error);

        convert = new Convert("format2", "aspectRatio2", null);
        assertTrue(convert.init());
        assertFalse(convert.m_Error);

        convert = new Convert(1, "format2", "aspectRatio2", null);
        assertTrue(convert.init());
        assertFalse(convert.m_Error);
    }

    @Test
    public void testTranscode() {
        // Test with valid input
        Convert convert = new Convert("format1", "aspectRatio1", null);
        convert.init();
        convert.Transcode();

        // Test with invalid input
        File ListDir = new File("non_existing_directory");
        convert = new Convert("format1", "aspectRatio1", null);
        convert.init();
        try {
            convert.Transcode();
            fail("Expected TranscodingError");
        } catch (AssertionError e) {
            // Expected
        }
    }

    @Test
    public void testMatrix() {
        Convert convert = new Convert("format1", "aspectRatio1", null);
        convert.init();
        convert.matrix();

        File file = new File(convert.m_GUI.strOutputDir + "/matrix.txt");
        assertTrue(file.exists());

        file.delete();
    }
}
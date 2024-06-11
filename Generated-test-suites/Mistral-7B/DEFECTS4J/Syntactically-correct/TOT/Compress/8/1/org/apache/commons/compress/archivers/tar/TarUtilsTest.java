package org.apache.commons.compress.archivers.tar.test;

import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * TarUtilsTest class for testing TarUtils class methods.
 */
public class TarUtilsTest {

    /**
     * Test parsing of valid octal strings.
     */
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', 0};
        long expected = 01234567L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);

        buffer = new byte[]{'0', '1', '7', '0', '1', '2', '3', '4', '5', '6', '7', 0};
        expected = 0101234567L;
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);

        buffer = new byte[]{'0', '7', 0};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Pass, expected
        }
    }

    /**
     * Test parsing of invalid octal strings.
     */
    @Test
    public void testParseInvalidOctal() {
        byte[] buffer = new byte[]{'0', '1', '2', '8', '5', '6', 0};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Pass, expected
        }

        buffer = new byte[]{'0', '1', '2', '3', 0};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Pass, expected
        }

        buffer = new byte[]{0};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Pass, expected
        }

        buffer = new byte[]{};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Pass, expected
        }
    }

    /**
     * Test parsing of entry names.
     */
    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{1, 'm', 'y', ' ', 'A', 'r', 'c', 'h', 'i', 'v', 'e', '.', 't', 'a', 'r', 0};
        String expected = "my Arcive.tar";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    /**
     * Test formatting a name into a buffer.
     */
    @Test
    public void testFormatNameBytes() {
        String name = "myArcive.tar";
        byte[] buf = new byte[name.length() + 1];
        int offset = TarUtils.formatNameBytes(name, buf, 0, buf.length);

        // Check name has been correctly copied into the buffer
        for (int i = 0; i < name.length(); ++i) {
            assertEquals((byte) name.charAt(i), buf[i]);
        }

        // Check that the buffer has been filled with NULs
        for (int i = name.length(); i < buf.length; ++i) {
            assertEquals((byte) 0, buf[i]);
        }

        // Check the updated offset
        assertEquals(offset, buf.length);
    }

    /**
     * Test formatting an unsigned octal number into a buffer.
     */
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 01234567L;
        byte[] buf = new byte[11];
        int offset = TarUtils.formatUnsignedOctalString(value, buf, 0, buf.length);

        // Check that the number has been correctly formatted
        for (int i = 0; i < buf.length; ++i) {
            if (i < buf.length - 2) {
                assertEquals((byte) ('0' + (value & 7)), buf[i]);
                value >>>= 3;
            } else {
                assertEquals((byte) ' ', buf[i]);
            }
        }

        // Check the updated offset
        assertEquals(offset, buf.length);
    }

    /**
     * Test writing an octal number into a buffer.
     */
    @Test
    public void testFormatOctalBytes() {
        long value = 01234567L;
        byte[] buf = new byte[11];
        int offset = TarUtils.formatOctalBytes(value, buf, 0, buf.length);

        // Check that the number has been correctly written
        for (int i = 0; i < buf.length - 2; ++i) {
            assertEquals((byte) ('0' + (value & 7)), buf[i]);
            value >>>= 3;
        }

        assertEquals((byte) ' ', buf[buf.length - 2]);
        assertEquals((byte) ' ', buf[buf.length - 1]);

        // Check the updated offset
        assertEquals(offset, buf.length);
    }

    /**
     * Test computing the checksum of a tar entry header.
     */
    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[]{0x1A, 0x2B, 0x3C, 0x4D};
        long expected = 62501L; // 0x1A2B3C4D
        long actual = TarUtils.computeCheckSum(buf);
        assertEquals(expected, actual);
    }
}
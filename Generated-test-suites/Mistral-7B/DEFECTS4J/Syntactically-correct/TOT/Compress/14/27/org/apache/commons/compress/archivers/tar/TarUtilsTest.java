package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.utils.ByteUtils;

import org.junit.Test;

/**
 * Unit tests for the TarUtils class.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid input.
     */
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', ' '};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0175555555555L, result);
    }

    /**
     * Test parseOctal method with invalid input.
     */
    @Test
    public void testParseOctalInvalidInput() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '6'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    /**
     * Test parseOctalOrBinary method with valid binary input.
     */
    @Test
    public void testParseOctalOrBinaryBinary() {
        byte[] buffer = new byte[]{(byte) 0x80, 0x35, 0x12};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(128 + (35 << 8), result);
    }

    /**
     * Test parseOctalOrBinary method with valid octal input.
     */
    @Test
    public void testParseOctalOrBinaryOctal() {
        byte[] buffer = new byte[]{'0', '1', '7', '0', '7', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', ' '};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(0117075555555L, result);
    }

    /**
     * Test parseOctalOrBinary method with invalid input.
     */
    @Test
    public void testParseOctalOrBinaryInvalidInput() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '7'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(buffer, 0, buffer.length));
    }

    /**
     * Test parseBoolean method with valid input.
     */
    @Test
    public void testParseBooleanTrue() {
        byte[] buffer = new byte[]{1};
        boolean result = TarUtils.parseBoolean(buffer, 0);
        assertTrue(result);
    }

    /**
     * Test parseBoolean method with invalid input.
     */
    @Test
    public void testParseBooleanFalse() {
        byte[] buffer = new byte[]{0};
        boolean result = TarUtils.parseBoolean(buffer, 0);
        assertFalse(result);
    }

    /**
     * Test parseName method with valid input.
     */
    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p'};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("tar.gzip", result);
    }

    /**
     * Test parseName method with invalid input.
     */
    @Test
    public void testParseNameInvalidInput() {
        byte[] buffer = new byte[]{'t', 'a', 'r', '.', 'g', 'z', 'i', 'p', '\0'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseName(buffer, 0, buffer.length));
    }

    /**
     * Test formatNameBytes method with valid input.
     */
    @Test
    public void testFormatNameBytes() {
        String name = "myfile.txt";
        byte[] result = new byte[TarConstants.NAMELEN];
        int offset = TarUtils.formatNameBytes(name, result, 0, result.length);
        byte[] expected = {'m', 'y', 'f', 'i', 'l', '.', 't', 'x', 't'};
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
        assertEquals(offset, result.length);
    }

    /**
     * Test formatNameBytes method with invalid input.
     */
    @Test
    public void testFormatNameBytesInvalidInput() {
        String name = "myfile.txt";
        byte[] result = new byte[1];
        int offset = TarUtils.formatNameBytes(name, result, 0, result.length);
        assertEquals(0, offset);
    }

    /**
     * Test formatUnsignedOctalString method with valid input.
     */
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0175555555555L;
        byte[] result = new byte[TarConstants.UIDLEN];
        TarUtils.formatUnsignedOctalString(value, result, 0, result.length);
        byte[] expected = {'0', '1', '7', '0', '7', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', ' '};
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    /**
     * Test formatCheckSumOctalBytes method with valid input.
     */
    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = TarUtils.computeCheckSum(ByteUtils.concat(
            new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p'},
            new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18},
            new byte[]{0x12, 0x34, 0x56, 0x78, 0x90, 0xab, 0xcd, 0xef}));
        byte[] result = new byte[TarConstants.UIDLEN + 2];
        TarUtils.formatCheckSumOctalBytes(value, result, 0, result.length);
        byte[] expected = {'0', '1', '1', '1', '7', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', ' '};
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }
}
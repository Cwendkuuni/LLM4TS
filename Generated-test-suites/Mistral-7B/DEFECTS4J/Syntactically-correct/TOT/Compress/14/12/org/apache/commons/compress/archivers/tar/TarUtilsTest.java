package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarUtils;

import org.junit.Test;

/**
 * Test class for the TarUtils class.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid input.
     */
    @Test
    public void testParseOctalValid() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', ' ', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0123456L, result);
    }

    /**
     * Test parseOctal method with leading NUL.
     */
    @Test
    public void testParseOctalLeadingNUL() {
        byte[] buffer = new byte[]{0, '1', '2', '3', '4', '5', '6', '7', ' ', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0L, result);
    }

    /**
     * Test parseOctal method with invalid input.
     */
    @Test
    public void testParseOctalInvalid() {
        byte[] buffer = new byte[]{'0', '1', '2', 'x', '4', '5', '6', '7', ' ', '0'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));

        byte[] buffer2 = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', ' ', '0', 'x'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer2, 0, buffer2.length));
    }

    /**
     * Test parseOctalOrBinary method with valid binary input.
     */
    @Test
    public void testParseOctalOrBinaryBinaryValid() {
        byte[] buffer = new byte[]{0x80, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(0x7F7F7F7F7FL, result);
    }

    /**
     * Test parseOctalOrBinary method with valid octal input.
     */
    @Test
    public void testParseOctalOrBinaryOctalValid() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7'};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(0123456L, result);
    }

    /**
     * Test parseOctalOrBinary method with invalid binary input.
     */
    @Test
    public void testParseOctalOrBinaryBinaryInvalid() {
        byte[] buffer = new byte[]{0x80, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x80};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(buffer, 0, buffer.length));
    }

    /**
     * Test parseOctalOrBinary method with invalid octal input.
     */
    @Test
    public void testParseOctalOrBinaryOctalInvalid() {
        byte[] buffer = new byte[]{'0', '1', '2', 'x', '4', '5', '6', '7'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(buffer, 0, buffer.length));
    }

    /**
     * Test parseBoolean method with valid input.
     */
    @Test
    public void testParseBooleanValid() {
        byte[] buffer = new byte[]{1};
        assertTrue(TarUtils.parseBoolean(buffer, 0));
    }

    /**
     * Test parseBoolean method with invalid input.
     */
    @Test
    public void testParseBooleanInvalid() {
        byte[] buffer = new byte[]{0};
        assertFalse(TarUtils.parseBoolean(buffer, 0));

        byte[] buffer2 = new byte[]{1, 0};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseBoolean(buffer2, 0));
    }

    /**
     * Test parseName method with valid input.
     */
    @Test
    public void testParseNameValid() {
        byte[] buffer = new byte[]{0, 'f', 'o', 'o', ' ', 'b', 'a', 'r', 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("foo bar", result);
    }

    /**
     * Test parseName method with invalid input.
     */
    @Test
    public void testParseNameInvalid() {
        byte[] buffer = new byte[]{0, 'x', 'x', 'x'};
        assertNull(TarUtils.parseName(buffer, 0, buffer.length));
    }

    /**
     * Test formatNameBytes method with valid input.
     */
    @Test
    public void testFormatNameBytesValid() {
        String name = "foo bar";
        byte[] buffer = new byte[TarConstants.NAME_LENGTH];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertNotNull(buffer);
        assertArrayEquals(new byte[]{'f', 'o', 'o', ' ', 'b', 'a', 'r', 0}, buffer);
        assertEquals(offset, buffer.length);
    }

    /**
     * Test formatNameBytes method with invalid input.
     */
    @Test
    public void testFormatNameBytesInvalid() {
        String name = "foo bar";
        byte[] buffer = new byte[TarConstants.UIDLEN];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertNull(buffer);
    }

    /**
     * Test formatUnsignedOctalString method with valid input.
     */
    @Test
    public void testFormatUnsignedOctalStringValid() {
        long value = 123456789012345L;
        byte[] buffer = new byte[TarConstants.UIDLEN];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '0', '1', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5'}, buffer);
    }

    /**
     * Test formatUnsignedOctalString method with invalid input.
     */
    @Test
    public void testFormatUnsignedOctalStringInvalid() {
        long value = 123456789012345L;
        byte[] buffer = new byte[TarConstants.MAXID + 1];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length));
    }

    /**
     * Test formatCheckSumOctalBytes method with valid input.
     */
    @Test
    public void testFormatCheckSumOctalBytesValid() {
        long checksum = 123456789012345L;
        byte[] buffer = new byte[TarConstants.UIDLEN + 2];
        TarUtils.formatCheckSumOctalBytes(checksum, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '0', '1', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', ' ', '0'}, buffer);
    }

    /**
     * Test formatCheckSumOctalBytes method with invalid input.
     */
    @Test
    public void testFormatCheckSumOctalBytesInvalid() {
        long checksum = 123456789012345L;
        byte[] buffer = new byte[TarConstants.UIDLEN];
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatCheckSumOctalBytes(checksum, buffer, 0, buffer.length));
    }
}
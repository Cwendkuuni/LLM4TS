package org.apache.commons.compress.archivers.tar.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.tar.TarUtils;

import org.junit.Test;

/**
 * Test class for TarUtils methods.
 */
public class TarUtilsTest {

    /**
     * Tests parseOctal method with valid input.
     */
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', ' '};
        int offset = 0;
        int length = 5;
        long expected = 01755;
        long actual = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expected, actual);
    }

    /**
     * Tests parseOctal method with invalid input.
     */
    @Test
    public void testParseOctalInvalid() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '8'}; // trailing non-numeric byte
        int offset = 0;
        int length = 5;
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, offset, length));

        buffer = new byte[]{'0', '7', '5', '5'}; // invalid length
        offset = 0;
        length = 3;
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, offset, length));

        buffer = new byte[]{' '}; // empty buffer
        offset = 0;
        length = 1;
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, offset, length));
    }

    /**
     * Tests parseName method with valid input.
     */
    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', 'G', 'z', ' ', '0', '1', '2', '\u0000'};
        int offset = 0;
        int length = 15;
        String expected = "tar.file.Gz ";
        String actual = TarUtils.parseName(buffer, offset, length);
        assertEquals(expected, actual);
    }

    /**
     * Tests parseName method with invalid input.
     */
    @Test
    public void testParseNameInvalid() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '\u0000'}; // trailing NUL
        int offset = 0;
        int length = 10;
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseName(buffer, offset, length));

        buffer = new byte[]{'t', 'a', 'r', ' '}; // invalid length
        offset = 0;
        length = 2;
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseName(buffer, offset, length));
    }

    /**
     * Tests formatNameBytes method with valid input.
     */
    @Test
    public void testFormatNameBytes() {
        String name = "tar.file.Gz ";
        byte[] buf = new byte[15];
        int offset = 0;
        int length = 15;
        int expected = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(expected, offset + length);

        String result = new String(buf, 0, length);
        assertEquals(name, result);
    }

    /**
     * Tests formatNameBytes method with invalid input.
     */
    @Test
    public void testFormatNameBytesInvalid() {
        String name = "tar.file.Gz"; // name too long
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatNameBytes(name, buf, offset, length));
    }

    /**
     * Tests parseOctal method with valid input and trailing NUL.
     */
    @Test
    public void testParseOctalTrailingNul() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', ' '};
        int offset = 0;
        int length = 5;
        long expected = 01755;
        long actual = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expected, actual);

        byte[] bufferWithTrailingNull = new byte[]{'0', '7', '5', '5', ' '};
        int offsetWithTrailingNull = offset + length;
        assertFalse(TarUtils.parseOctal(bufferWithTrailingNull, offsetWithTrailingNull, 1) instanceof Long);
    }

    /**
     * Tests formatUnsignedOctalString method with valid input.
     */
    @Test
    public void testFormatUnsignedOctalStringValid() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 8;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        String result = new String(buffer, offset, length);
        assertEquals("01235123", result);
    }

    /**
     * Tests formatUnsignedOctalString method with invalid input.
     */
    @Test
    public void testFormatUnsignedOctalStringInvalid() {
        long value = 12345;
        byte[] buffer = new byte[2];
        int offset = 0;
        int length = 2;
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatUnsignedOctalString(value, buffer, offset, length));
    }

    /**
     * Tests computeCheckSum method with valid input.
     */
    @Test
    public void testComputeCheckSumValid() {
        byte[] buffer = new byte[]{0x75, 0x72, 0x6e, 0x63, 0x68, 0x65, 0x72, 0x65, 0x20, 0x61, 0x74, 0x65, 0x72, 0x73, 0x69, 0x6f, 0x6e, 0x20, 0x66, 0x6f, 0x72, 0x20, 0x74, 0x68, 0x65, 0x20, 0x73, 0x63, 0x68, 0x65, 0x6d, 0x65, 0x6d, 0x20, 0x73, 0x79, 0x73, 0x74, 0x65, 0x6d};
        long computedChecksum = TarUtils.computeChecksum(buffer);
        long expectedChecksum = 2133780729;
        assertEquals(expectedChecksum, computedChecksum);
    }

    /**
     * Tests computeCheckSum method with invalid input.
     */
    @Test
    public void testComputeCheckSumInvalid() {
        byte[] buffer = new byte[0];
        assertThrows(IllegalArgumentException.class, () -> TarUtils.computeChecksum(buffer));
    }
}
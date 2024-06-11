package org.apache.commons.compress.archivers.tar;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * TarUtilsTest class contains JUnit 4 test cases for the TarUtils class.
 */
public class TarUtilsTest {

    /**
     * Test case for parseOctal method with valid input.
     */
    @Test
    public void testParseOctalValidInput() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '5', '5', '5', '5', '5', '5', '0'};
        long expected = 0175555555L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    /**
     * Test case for parseOctal method with trailing NUL.
     */
    @Test
    public void testParseOctalTrailingNul() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '5', '5', '5', '5', '5', '5', '0', '0'};
        long expected = 0175555555L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    /**
     * Test case for parseOctal method with missing trailing NUL or space.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalMissingTrailing() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '5', '5', '5', '5', '5', '5'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test case for parseOctal method with invalid byte.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '5', '5', '5', '5', '5', '5', 'A'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test case for parseOctalOrBinary method with valid binary input.
     */
    @Test
    public void testParseOctalOrBinaryValidBinary() {
        byte[] buffer = new byte[]{(byte) 0x81, 0x01, 0x01, 0x01, 0x01};
        long expected = 256 + 1 + 1 + 1 + 1;
        long actual = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    /**
     * Test case for parseOctalOrBinary method with valid octal input.
     */
    @Test
    public void testParseOctalOrBinaryValidOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '5', '5', '5', '5', '5', '5'};
        long expected = 0175555555L;
        long actual = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    /**
     * Test case for parseBoolean method with valid input.
     */
    @Test
    public void testParseBooleanTrue() {
        byte[] buffer = new byte[]{1};
        boolean expected = true;
        boolean actual = TarUtils.parseBoolean(buffer, 0);
        assertEquals(expected, actual);
    }

    /**
     * Test case for parseBoolean method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseBooleanFalse() {
        byte[] buffer = new byte[]{0};
        TarUtils.parseBoolean(buffer, 0);
    }

    /**
     * Test case for parseName method with valid input.
     */
    @Test
    public void testParseNameValidInput() {
        byte[] buffer = new byte[]{0x73, 0x61, 0x6D, 0x70, 0x6C, 0x65, 0x2E, 0x63, 0x61, 0x6D, 0x65, 0x73, 0x73, 0x61, 0x6E, 0x73, 0x76, 0x65, 0x72, 0x00};
        String expected = "myfile.txt";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    /**
     * Test case for formatNameBytes method with valid input.
     */
    @Test
    public void testFormatNameBytesValidInput() {
        String name = "myfile.txt";
        byte[] expected = new byte[]{(byte) 'm', (byte) 'y', (byte) 'f', (byte) 'i', (byte) 'l',
                (byte) 'e', (byte) '.', (byte) 't', (byte) 'x', (byte) 't'};
        byte[] buffer = new byte[name.length() + 1];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals(expected, buffer, 0, offset);
    }

    /**
     * Test case for formatNameBytes method with truncated input.
     */
    @Test
    public void testFormatNameBytesTruncatedInput() {
        String name = "myfile.txt";
        byte[] buffer = new byte[name.length() + 1];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, name.length());
        assertArrayEquals(name.getBytes(), buffer, 0, offset);
    }

    /**
     * Test case for formatUnsignedOctalString method with valid input.
     */
    @Test
    public void testFormatUnsignedOctalStringValidInput() {
        long value = 02777777777L;
        byte[] expected = new byte[]{'0', '2', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7'};
        byte[] buffer = new byte[22];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    /**
     * Test case for formatOctalBytes method with valid input.
     */
    @Test
    public void testFormatOctalBytesValidInput() {
        long value = 02777777777L;
        byte[] expected = new byte[]{'0', '2', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', ' ', '0'};
        byte[] buffer = new byte[23];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    /**
     * Test case for formatLongOctalOrBinaryBytes method with valid input.
     */
    @Test
    public void testFormatLongOctalOrBinaryBytesValidInput() {
        long value = 02777777777777L;
        byte[] expected = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        byte[] buffer = new byte[32];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    /**
     * Test case for formatLongOctalOrBinaryBytes method with binary input.
     */
    @Test
    public void testFormatLongOctalOrBinaryBytesBinaryInput() {
        long value = 12345678901234L;
        byte[] expected = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0x81, 0x34, 0x56, 0x78, 0x90, 0x12, 0x34, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1, 0x2, 0x3};
        byte[] buffer = new byte[31];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    /**
     * Test case for computeCheckSum method with valid input.
     */
    @Test
    public void testComputeCheckSumValidInput() {
        byte[] buffer = new byte[]{'T', 'a', 'r', ' ', 'h', 'e', 'a', 'd', 'e', 'r', ' ', 'f', 'i', 'l', 'e', ':', ' ', 'u', 's', 'e', 'r', ' ', '1', '2', '3', '4'};
        long expected = 2742494097L;
        long actual = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, actual);
    }
}
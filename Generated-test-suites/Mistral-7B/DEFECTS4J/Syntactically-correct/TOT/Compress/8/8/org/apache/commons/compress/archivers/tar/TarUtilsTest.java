package org.apache.commons.compress.archivers.tar.test;

import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for TarUtils.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid input.
     */
    @Test
    public void testParseOctalValidInput() {
        byte[] buffer = new byte[]{'0', '7', '5', '4', '0', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0175400L, result);
    }

    /**
     * Test parseOctal method with leading spaces.
     */
    @Test
    public void testParseOctalLeadingSpaces() {
        byte[] buffer = new byte[]{' ', '0', '7', '5', '4', '0', ' ', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0175400L, result);
    }

    /**
     * Test parseOctal method with trailing spaces.
     */
    @Test
    public void testParseOctalTrailingSpaces() {
        byte[] buffer = new byte[]{'0', '7', '5', '4', '0', '0', ' ', ' '};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0175400L, result);
    }

    /**
     * Test parseOctal method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidInput() {
        byte[] buffer = new byte[]{'0', '7', '5', '4', '0', 'X'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parseName method with valid input.
     */
    @Test
    public void testParseNameValidInput() {
        byte[] buffer = new byte[]{(byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e'};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("abcde", result);
    }

    /**
     * Test parseName method with trailing NUL.
     */
    @Test
    public void testParseNameTrailingNUL() {
        byte[] buffer = new byte[]{(byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("abcde", result);
    }

    /**
     * Test parseName method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseNameInvalidInput() {
        byte[] buffer = new byte[]{(byte) 'a', (byte) 'b', (byte) 'c', (byte) 'X'};
        TarUtils.parseName(buffer, 0, buffer.length);
    }

    /**
     * Test formatNameBytes method with valid input.
     */
    @Test
    public void testFormatNameBytesValidInput() {
        String name = "abcde";
        byte[] buffer = new byte[name.length() + 1];
        int result = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        for (int i = 0; i < name.length(); ++i) {
            assertEquals(name.charAt(i), (char) (buffer[i] & 0xFF));
        }
        assertEquals(name.length() + 1, result);
    }

    /**
     * Test formatNameBytes method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatNameBytesInvalidInput() {
        String name = "abcde";
        byte[] buffer = new byte[name.length()];
        TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
    }

    /**
     * Test parseOctal method with long input.
     */
    @Test
    public void testParseOctalLongInput() {
        byte[] buffer = new byte[]{'0', '1', '7', '7', '7', '7', '7', '7'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(017777777L, result);
    }

    /**
     * Test formatUnsignedOctalString method with valid input.
     */
    @Test
    public void testFormatUnsignedOctalStringValidInput() {
        long value = 017777777L;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals("017777777", new String(buffer));
    }

    /**
     * Test formatUnsignedOctalString method with wrong length input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringWrongLengthInput() {
        long value = 017777777L;
        byte[] buffer = new byte[9];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
    }

    /**
     * Test formatOctalBytes method with valid input.
     */
    @Test
    public void testFormatOctalBytesValidInput() {
        long value = 017777777L;
        byte[] buffer = new byte[12];
        int result = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(10, result);
        for (int i = 0; i < 10; ++i) {
            assertEquals('0' + (char) (value & 7), buffer[i]);
            value >>= 3;
        }
    }

    /**
     * Test formatOctalBytes method with wrong length input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesWrongLengthInput() {
        long value = 017777777L;
        byte[] buffer = new byte[9];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
    }
}
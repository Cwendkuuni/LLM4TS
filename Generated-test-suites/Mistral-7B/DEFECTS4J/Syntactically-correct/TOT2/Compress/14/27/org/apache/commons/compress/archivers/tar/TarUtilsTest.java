package org.apache.commons.compress.archivers.tar;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * TarUtilsTest
 */
public class TarUtilsTest {

    @Test
    public void testParseOctal_ValidCase() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7'};
        long expectedResult = 01234567L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseOctal_LeadingNUL() {
        byte[] buffer = new byte[]{0, '1', '2', '3', '4', '5', '6', '7'};
        long expectedResult = 0L; // ignored due to leading NUL
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseOctal_TrailingNUL() {
        byte[] buffer = new byte[]{'1', '2', '3', '4', '5', '6', '7', 0};
        long expectedResult = 01234567L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidLength() {
        byte[] buffer = new byte[]{'1', '2'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidByte() {
        byte[] buffer = new byte[]{'1', '2', '8'}; // byte '8' is invalid in octal
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseOctalOrBinary_ValidBinaryCase() {
        byte[] buffer = new byte[]{0, 0x81, 0x01, 0x2A, 0x3F, 0x5E, 0xFF, 0x7F};
        long expectedResult = 0x012A3F5E7FFL;
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseOctalOrBinary_ValidOctalCase() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7'};
        long expectedResult = 01234567L;
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinary_InvalidLength() {
        byte[] buffer = new byte[]{'1'};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinary_InvalidByte_Binary() {
        byte[] buffer = new byte[]{0, 0x82, 0x01, 0x2A, 0x3F, 0x5E, 0xFF, 0x7F}; // byte '2' is not valid in binary
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinary_BinaryOverflow() {
        byte[] buffer = new byte[]{0, 0x80, 0x01, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    @Test
    public void testParseBoolean_ValidCase() {
        byte[] buffer = new byte[]{1};
        boolean expectedResult = true;
        boolean result = TarUtils.parseBoolean(buffer, 0);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseBoolean_InvalidCase() {
        byte[] buffer = new byte[]{0};
        boolean expectedResult = false;
        boolean result = TarUtils.parseBoolean(buffer, 0);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBoolean_InvalidByte() {
        byte[] buffer = new byte[]{2};
        TarUtils.parseBoolean(buffer, 0);
    }

    @Test
    public void testParseName_ValidCase() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'x', 't'};
        String expectedResult = "tar.txt";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    @Test   
    public void testFormatNameBytes_ValidCase() {
        String name = "myfile.tar.gz";
        byte[] expectedBuffer = new byte[]{'m', 'y', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g', 'z'};
        byte[] buffer = new byte[expectedBuffer.length];
        int offset = 0;
        int length = expectedBuffer.length;
        TarUtils.formatNameBytes(name, buffer, offset, length);
        for (int i = 0; i < expectedBuffer.length; ++i) {
            assertEquals(expectedBuffer[i], buffer[i]);
        }
    }

    @Test
    public void testFormatUnsignedOctalString_ValidCase() {
        long value = 1234567L;
        byte[] expectedBuffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7'};
        byte[] buffer = new byte[8];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        for (int i = 0; i < expectedBuffer.length; ++i) {
            assertEquals(expectedBuffer[i], buffer[i]);
        }
    }

    @Test
    public void testFormatOctalBytes_ValidCase() {
        long value = 01234567L;
        byte[] expectedBuffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', ' '};
        byte[] buffer = new byte[10];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        for (int i = 0; i < expectedBuffer.length; ++i) {
            assertEquals(expectedBuffer[i], buffer[i]);
        }
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes_ValidCase() {
        long value = 0x1234567890ABCDEF0L;
        byte[] expectedBuffer = new byte[]{0, 0x80, (byte) 0x34, 0x56, 0x78, 0x90, (byte) 0xAB, 0xCD, 0xEF};
        byte[] buffer = new byte[10];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        for (int i = 0; i < expectedBuffer.length; ++i) {
            assertEquals(expectedBuffer[i], buffer[i]);
        }
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes_OverflowCase() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[TarConstants.MAXSIZE + 1];
        assertThrows(IllegalArgumentException.class, () -> {
            TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        });
    }
}
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarUtils;

import org.junit.Test;

/**
 * This class contains unit tests for the TarUtils class.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid input.
     */
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0', ' ', 0};
        long expectedResult = 017554L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    /**
     * Test parseOctal method with invalid input - missing trailing space.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalMissingTrailingSpace() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parseOctal method with invalid input - invalid byte.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '8'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parseOctalOrBinary method with valid input - octal.
     */
    @Test
    public void testParseOctalOrBinaryOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0', ' ', 0};
        long expectedResult = 017554L;
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    /**
     * Test parseOctalOrBinary method with valid input - binary.
     */
    @Test
    public void testParseOctalOrBinaryBinary() {
        byte[] buffer = new byte[]{(byte) 0x81, 0x2A, 0x00, 0x2A};
        long expectedResult = 524285L;
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    /**
     * Test parseOctalOrBinary method with invalid input - missing trailing space.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryMissingTrailingSpace() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0'};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    /**
     * Test parseOctalOrBinary method with invalid input - binary number exceeds maximum signed long value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryBinaryNumberTooLarge() {
        byte[] buffer = new byte[]{0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    /**
     * Test parseBoolean method with valid input.
     */
    @Test
    public void testParseBooleanTrue() {
        byte[] buffer = new byte[]{1};
        boolean expectedResult = true;
        boolean result = TarUtils.parseBoolean(buffer, 0);
        assertEquals(expectedResult, result);
    }

    /**
     * Test parseBoolean method with valid input.
     */
    @Test
    public void testParseBooleanFalse() {
        byte[] buffer = new byte[]{0};
        boolean expectedResult = false;
        boolean result = TarUtils.parseBoolean(buffer, 0);
        assertEquals(expectedResult, result);
    }

    /**
     * Test parseName method with valid input.
     */
    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'a', 'b', 'c', 0};
        String expectedResult = "abc";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    /**
     * Test formatNameBytes method with valid input.
     */
    @Test
    public void testFormatNameBytes() {
        String name = "MyName";
        byte[] buf = new byte[TarConstants.MAXNAMELEN];
        int offset = TarUtils.formatNameBytes(name, buf, 0, buf.length);
        String parsedName = TarUtils.parseName(buf, 0, offset);
        assertEquals(name, parsedName);
    }

    /**
     * Test formatUnsignedOctalString method with valid input.
     */
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345L;
        byte[] buffer = new byte[TarConstants.MAXID + 2];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String expectedResult = "012346";
        String result = new String(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    /**
     * Test formatOctalBytes method with valid input.
     */
    @Test
    public void testFormatOctalBytes() {
        long value = 12345L;
        byte[] buf = new byte[TarConstants.MAXID + 2];
        TarUtils.formatOctalBytes(value, buf, 0, buf.length);
        String expectedResult = "012345 ";
        String result = new String(buf, 0, buf.length);
        assertEquals(expectedResult, result);
    }

    /**
     * Test formatLongOctalBytes method with valid input.
     */
    @Test
    public void testFormatLongOctalBytes() {
        long value = 1234567890L;
        byte[] buf = new byte[TarConstants.MAXID + 2];
        TarUtils.formatLongOctalBytes(value, buf, 0, buf.length);
        String expectedResult = "01234575506";
        String result = new String(buf, 0, buf.length);
        assertEquals(expectedResult, result);
    }

    /**
     * Test formatLongOctalOrBinaryBytes method with valid input.
     */
    @Test
    public void testFormatLongOctalOrBinaryBytesOctal() {
        long value = 1234567890L;
        byte[] buf = new byte[TarConstants.MAXID + 2];
        TarUtils.formatLongOctalOrBinaryBytes(value, buf, 0, buf.length);
        String expectedResult = "01234575506 ";
        String result = new String(buf, 0, buf.length);
        assertEquals(expectedResult, result);
    }

    /**
     * Test formatLongOctalOrBinaryBytes method with valid input.
     */
    @Test
    public void testFormatLongOctalOrBinaryBytesBinary() {
        long value = -1234567890L;
        byte[] buf = new byte[TarConstants.MAXID + 2];
        TarUtils.formatLongOctalOrBinaryBytes(value, buf, 0, buf.length);
        String expectedResult = "\u0080\u0080\u0080\u0080\u0080\u0080\u0080\u0080";
        String result = new String(buf, 0, buf.length);
        assertEquals(expectedResult, result);
    }

    /**
     * Test computeCheckSum method with valid input.
     */
    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'a', 'b', 'c'};
        long expectedResult = TarUtils.computeCheckSum(buffer);
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expectedResult, result);
    }
}
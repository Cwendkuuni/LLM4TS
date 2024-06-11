package org.apache.commons.compress.archivers.tar;

import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.compress.archivers.tar.TarConstants.MAXID;
import static org.apache.commons.compress.archivers.tar.TarConstants.MAXSIZE;

/**
 * Tests for {@link TarUtils}.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid octal strings.
     */
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', ' ', '0'};
        long expected = 0123456L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);

        byte[] buffer2 = new byte[]{'0', '1', '7', '7', '4', '4', '4', '4', ' ', '0'};
        long expected2 = 01774444L;
        long result2 = TarUtils.parseOctal(buffer2, 0, buffer2.length);
        Assert.assertEquals(expected2, result2);

        byte[] buffer3 = new byte[]{'0', '1', '0', '0', ' ', '0'};
        long expected3 = 0L;
        long result3 = TarUtils.parseOctal(buffer3, 0, buffer3.length);
        Assert.assertEquals(expected3, result3);
    }

    /**
     * Test parseOctal method with invalid octal strings.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalid() {
        byte[] buffer = new byte[]{'0', '1', '2', 'a', '5', '6', '7', '7', ' ', '0'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parseOctalOrBinary method with valid binary numbers.
     */
    @Test
    public void testParseOctalOrBinaryValidBinary() {
        byte[] buffer = new byte[]{0, 0, 0, 0, 0, 0, 0, 0x80, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36};
        long expected = 0x012345680L;
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test parseOctalOrBinary method with valid octal strings.
     */
    @Test
    public void testParseOctalOrBinaryValidOctal() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', ' ', '0'};
        long expected = 0123456L;
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test parseOctalOrBinary method with invalid binary numbers.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryInvalidBinary() {
        byte[] buffer = new byte[]{0, 0, 0, 0, (byte) 0x81, 0, 0, 0x7f, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    /**
     * Test parseBoolean method with valid boolean byte.
     */
    @Test
    public void testParseBooleanTrue() {
        byte[] buffer = new byte[]{1};
        boolean expected = true;
        boolean result = TarUtils.parseBoolean(buffer, 0);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test parseBoolean method with invalid boolean byte.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseBooleanFalse() {
        byte[] buffer = new byte[]{0};
        TarUtils.parseBoolean(buffer, 0);
    }

    /**
     * Test parseName method with valid name.
     */
    @Test
    public void testParseNameValid() {
        byte[] buffer = new byte[]{0, 'a', 0, 'b', 0, 'c', 0, 'd', 0};
        String expected = "abcd";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test parseName method with name longer than buffer.
     */
    @Test
    public void testParseNameTooLong() {
        byte[] buffer = new byte[]{0, 'a', 0, 'b', 0};
        String expected = "ab";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test formatNameBytes method with valid name.
     */
    @Test
    public void testFormatNameBytesValid() {
        String name = "myfile.txt";
        byte[] buffer = new byte[TarConstants.NAMELEN];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        String result = new String(buffer, 0, offset);
        Assert.assertEquals(name, result);
    }

    /**
     * Test formatNameBytes method with name longer than buffer.
     */
    @Test
    public void testFormatNameBytesTooLong() {
        String name = "myfile.txtmyfile2.txt";
        byte[] buffer = new byte[TarConstants.NAMELEN];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        String result = new String(buffer, 0, offset);
        Assert.assertEquals("myfile.txt", result);
    }

    /**
     * Test formatUnsignedOctalString method with valid value.
     */
    @Test
    public void testFormatUnsignedOctalStringValid() {
        long value = 0x1234567;
        byte[] buffer = new byte[TarConstants.UIDLEN];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String expected = "0123456";
        String result = new String(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test formatUnsignedOctalString method with value longer than buffer.
     */
    @Test
    public void testFormatUnsignedOctalStringTooLong() {
        long value = 0x123456789012345L;
        byte[] buffer = new byte[TarConstants.UIDLEN];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String expected = "0123456";
        String result = new String(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test formatOctalBytes method with valid value.
     */
    @Test
    public void testFormatOctalBytesValid() {
        long value = 0x1234567;
        byte[] buffer = new byte[TarConstants.UIDLEN + 2];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        String expected = "0123456\0 ";
        String result = new String(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test formatOctalBytes method with value longer than buffer.
     */
    @Test
    public void testFormatOctalBytesTooLong() {
        long value = 0x123456789012345L;
        byte[] buffer = new byte[TarConstants.UIDLEN + 2];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        String expected = "0123456\0 ";
        String result = new String(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test formatLongOctalBytes method with valid value.
     */
    @Test
    public void testFormatLongOctalBytesValid() {
        long value = 0x123456789012345L;
        byte[] buffer = new byte[TarConstants.MAXSIZE + 1];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        String expected = "0123456789012345";
        String result = new String(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test formatLongOctalBytes method with value longer than buffer.
     */
    @Test
    public void testFormatLongOctalBytesTooLong() {
        long value = 0x1234567890123456789012345L;
        byte[] buffer = new byte[TarConstants.MAXSIZE + 1];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        String expected = "0123456789012345";
        String result = new String(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test formatLongOctalOrBinaryBytes method with valid value.
     */
    @Test
    public void testFormatLongOctalOrBinaryBytesValid() {
        long value = 0x123456789012345L;
        byte[] buffer = new byte[TarConstants.MAXSIZE];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        String expected = "0123456789012345";
        String result = new String(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test formatLongOctalOrBinaryBytes method with value longer than buffer.
     */
    @Test
    public void testFormatLongOctalOrBinaryBytesTooLong() {
        long value = 0x1234567890123456789012345L;
        byte[] buffer = new byte[TarConstants.MAXSIZE];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        String expected = "0123456789012345";
        String result = new String(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test computeCheckSum method with valid buffer.
     */
    @Test
    public void testComputeCheckSumValid() {
        byte[] buffer = new byte[]{0, 1, 2, 3, 4, 5, 6, 7};
        long expected = 0x5e;
        long result = TarUtils.computeCheckSum(buffer);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test computeCheckSum method with invalid buffer.
     */
    @Test
    public void testComputeCheckSumInvalid() {
        byte[] buffer = new byte[]{0, 0, 0};
        long result = TarUtils.computeCheckSum(buffer);
        Assert.assertEquals(0, result);
    }
}
package org.apache.commons.compress.archivers.tar;

import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.compress.archivers.tar.TarConstants.*;

/**
 * Test class for TarUtils.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid octal strings.
     */
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', ' ', '0'};
        long expected = 01755L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(expected, actual);

        buffer = new byte[]{'0', '1', '2', '7', '5', '5', ' ', '0'};
        expected = 0112755L;
        actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(expected, actual);

        buffer = new byte[]{'0', '1', '7', '7', '7', ' ', '0'};
        expected = 01777L;
        actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test parseOctal method with invalid octal strings.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalid() {
        byte[] buffer = new byte[]{'0', '1', '2', 'x', '5', ' ', '0'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parseOctal method with empty octal strings.
     */
    @Test
    public void testParseOctalEmpty() {
        byte[] buffer = new byte[]{'\u0000'};
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(0, actual);

        buffer = new byte[]{'\u0000', ' '};
        actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(0, actual);
    }

    /**
     * Test parseOctalOrBinary method with valid binary strings.
     */
    @Test
    public void testParseOctalOrBinaryBinary() {
        byte[] buffer = new byte[]{'\u0001', '1', '0', '\u0000'};
        long expected = 128 + 1 * 256;
        long actual = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assert.assertEquals(expected, actual);

        buffer = new byte[]{'\u0001', '1', '1', '1', '1'};
        expected = (1 * 256 * 256 * 256) + (1 * 256 * 256) + (1 * 256) + 1;
        actual = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test parseOctalOrBinary method with valid octal strings.
     */
    @Test
    public void testParseOctalOrBinaryOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', ' ', '0'};
        long expected = 01755L;
        long actual = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test parseOctalOrBinary method with invalid inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryInvalid() {
        byte[] buffer = new byte[]{'1', '2', '3', '4'};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    /**
     * Test parseBoolean method with valid boolean values.
     */
    @Test
    public void testParseBooleanTrue() {
        byte[] buffer = new byte[]{1};
        boolean expected = true;
        boolean actual = TarUtils.parseBoolean(buffer, 0);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test parseBoolean method with invalid boolean values.
     */
    @Test
    public void testParseBooleanFalse() {
        byte[] buffer = new byte[]{0};
        boolean expected = false;
        boolean actual = TarUtils.parseBoolean(buffer, 0);
        Assert.assertEquals(expected, actual);

        buffer = new byte[]{'\u0001'};
        actual = TarUtils.parseBoolean(buffer, 0);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test parseName method with valid names.
     */
    @Test
    public void testParseNameValid() {
        byte[] buffer = new byte[]{'m', 'y', ' ', 'F', 'i', 'l', 'e', '.', 't', 'x', 't', '\u0000'};
        String expected = "my File.txt";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test parseName method with invalid inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseNameInvalid() {
        byte[] buffer = new byte[]{'m', 'y', ' ', 'F', 'i', 'l', 'e', '.', 't', 'x', '\u0001'};
        TarUtils.parseName(buffer, 0, buffer.length);
    }

    /**
     * Test formatNameBytes method with valid names.
     */
    @Test
    public void testFormatNameBytesValid() {
        String name = "my File.txt";
        byte[] buffer = new byte[NAME_LENGTH];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, NAME_LENGTH);

        String actual = new String(buffer, 0, offset);
        String expected = "my File.txt";
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test formatNameBytes method with invalid inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatNameBytesInvalid() {
        String name = "my File.txt";
        byte[] buffer = new byte[NAME_LENGTH];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, 0);
        TarUtils.formatNameBytes(name, buffer, offset, NAME_LENGTH);
    }

    /**
     * Test formatUnsignedOctalString method with valid numbers.
     */
    @Test
    public void testFormatUnsignedOctalStringValid() {
        long value = 12345;
        byte[] buffer = new byte[7];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String expected = "012345";
        String actual = new String(buffer, 0, buffer.length);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test formatUnsignedOctalString method with invalid numbers.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringInvalid() {
        long value = 123456789012345L;
        byte[] buffer = new byte[7];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
    }

    /**
     * Test formatOctalBytes method with valid numbers.
     */
    @Test
    public void testFormatOctalBytesValid() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        String expected = "012345\u0020\u0000";
        String actual = new String(buffer, 0, offset);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test formatOctalBytes method with invalid numbers.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesInvalid() {
        long value = 123456789012345L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
    }

    /**
     * Test formatLongOctalOrBinaryBytes method with valid numbers.
     */
    @Test
    public void testFormatLongOctalOrBinaryBytesValid() {
        long value = 123456789012345L;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);

        if (value <= MAXID) {
            String expected = String.format("%o%c%c", value, ' ', '\u0000');
            String actual = new String(buffer, 0, offset);
            Assert.assertEquals(expected, actual);
        } else {
            String expected = String.format("%c%c", (byte) 0x80, (byte) 0);
            String actual = new String(buffer, 0, offset);
            Assert.assertEquals(expected, actual);
        }
    }

    /**
     * Test formatLongOctalOrBinaryBytes method with invalid numbers.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatLongOctalOrBinaryBytesInvalid() {
        long value = 1234567890123456789012345L;
        byte[] buffer = new byte[11];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
    }

    /**
     * Test computeCheckSum method with valid inputs.
     */
    @Test
    public void testComputeCheckSumValid() {
        byte[] buffer = new byte[]{'m', 'y', ' ', 'F', 'i', 'l', 'e', '.', 't', 'x', 't', '\u0000', '0', '1', '2', '3'};
        long expected = TarUtils.computeCheckSum(buffer);
        long actual = TarUtils.computeCheckSum(buffer);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test computeCheckSum method with empty inputs.
     */
    @Test
    public void testComputeCheckSumEmpty() {
        byte[] buffer = new byte[0];
        long actual = TarUtils.computeCheckSum(buffer);
        long expected = 0;
        Assert.assertEquals(expected, actual);
    }
}
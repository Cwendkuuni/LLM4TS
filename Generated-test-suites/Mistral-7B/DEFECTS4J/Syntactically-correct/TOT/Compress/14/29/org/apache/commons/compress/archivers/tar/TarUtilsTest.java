package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.utils.ByteStreams;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * Test class for TarUtils.
 */
public class TarUtilsTest {

    /**
     * Test for parseOctal method with valid octal strings.
     */
    @Test
    public void testParseOctalValid() {
        byte[] buffer = new byte[]{'0', '0', '7', '5', '5', '5', '5', '5', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals("Invalid octal value parsed", 0755555L, result);

        buffer = new byte[]{'0', '1', '2', '0', '7', '5', '5', '5', '5', '0'};
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals("Invalid octal value parsed", 012075555L, result);

        buffer = new byte[]{'0', '1', '7', '7', '7', '7', '7', '7', '7', '0'};
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals("Invalid octal value parsed", 0177777777L, result);
    }

    /**
     * Test for parseOctal method with invalid octal strings.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalid() {
        byte[] buffer = new byte[]{'0', '1', '2', '3'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test for parseOctalOrBinary method with valid binary numbers.
     */
    @Test
    public void testParseOctalOrBinaryValidBinary() {
        byte[] buffer = new byte[]{0, 0, 0, 0, 0xff, 0x7f};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assert.assertEquals("Invalid binary value parsed", 32767L, result);

        buffer = new byte[]{0, 0, 0, 0, 0xff, (byte) 0x80};
        result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assert.assertEquals("Invalid binary value parsed", 32768L, result);
    }

    /**
     * Test for parseOctalOrBinary method with valid octal strings.
     */
    @Test
    public void testParseOctalOrBinaryValidOctal() {
        byte[] buffer = new byte[]{'0', '0', '7', '5', '5', '5', '5', '5', '0'};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assert.assertEquals("Invalid octal value parsed", 0755555L, result);
    }

    /**
     * Test for parseOctalOrBinary method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryInvalid() {
        byte[] buffer = new byte[]{0};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    /**
     * Test for parseBoolean method with valid boolean values.
     */
    @Test
    public void testParseBooleanValid() {
        byte[] buffer = new byte[]{1};
        boolean result = TarUtils.parseBoolean(buffer, 0);
        Assert.assertTrue("Invalid boolean value parsed", result);

        buffer = new byte[]{0};
        result = TarUtils.parseBoolean(buffer, 0);
        Assert.assertFalse("Invalid boolean value parsed", result);
    }

    /**
     * Test for parseBoolean method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseBooleanInvalid() {
        byte[] buffer = new byte[]{2};
        TarUtils.parseBoolean(buffer, 0);
    }

    /**
     * Test for parseName method with valid names.
     */
    @Test
    public void testParseNameValid() {
        byte[] buffer = new byte[]{'.'.byteValue(), 'a', '.', 'b', '.', 'c', 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals("Invalid name parsed", "a.b.c", result);

        buffer = new byte[]{'/', '/', 'a', '/', 'b', '/', 'c', 0};
        result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals("Invalid name parsed", "a/b/c", result);
    }

    /**
     * Test for parseName method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseNameInvalid() {
        byte[] buffer = new byte[]{'/', '/', 0};
        TarUtils.parseName(buffer, 0, buffer.length);
    }

    /**
     * Test for formatNameBytes method with valid names.
     */
    @Test
    public void testFormatNameBytesValid() {
        String name = "a.b.c";
        byte[] result = new byte[256];
        int offset = TarUtils.formatNameBytes(name, result, 0, result.length);
        Assert.assertEquals("Invalid formatNameBytes result", 24, offset);

        name = "a/b/c";
        offset = TarUtils.formatNameBytes(name, result, 0, result.length);
        Assert.assertEquals("Invalid formatNameBytes result", 24, offset);
    }

    /**
     * Test for formatNameBytes method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatNameBytesInvalid() {
        String name = "";
        byte[] result = new byte[256];
        TarUtils.formatNameBytes(name, result, 0, result.length);
    }

    /**
     * Test for formatUnsignedOctalString method with valid unsigned octal numbers.
     */
    @Test
    public void testFormatUnsignedOctalStringValid() {
        long value = 0;
        byte[] result = new byte[12];
        int offset = 0;

        value = 0;
        offset = TarUtils.formatUnsignedOctalString(value, result, offset, result.length);
        Assert.assertEquals("Invalid formatUnsignedOctalString result", 11, offset);
        Assert.assertArrayEquals("Invalid formatUnsignedOctalString result", new byte[]{'0', '0', '0'}, result, 0, 3);

        value = 7;
        offset = TarUtils.formatUnsignedOctalString(value, result, offset, result.length);
        Assert.assertEquals("Invalid formatUnsignedOctalString result", 11, offset);
        Assert.assertArrayEquals("Invalid formatUnsignedOctalString result", new byte[]{'0', '1', '1'}, result, 0, 3);

        value = 123;
        offset = TarUtils.formatUnsignedOctalString(value, result, offset, result.length);
        Assert.assertEquals("Invalid formatUnsignedOctalString result", 12, offset);
        Assert.assertArrayEquals("Invalid formatUnsignedOctalString result", new byte[]{'0', '1', '2', '3'}, result, 0, 4);
    }

    /**
     * Test for formatUnsignedOctalString method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringInvalid() {
        long value = Long.MAX_VALUE + 1;
        byte[] result = new byte[12];
        TarUtils.formatUnsignedOctalString(value, result, 0, result.length);
    }

    /**
     * Test for formatCheckSumOctalBytes method with valid checksums.
     */
    @Test
    public void testFormatCheckSumOctalBytesValid() {
        long value = TarUtils.computeCheckSum(new byte[]{'0', '1', '2', '3', '4'});
        byte[] result = new byte[12];
        int offset = TarUtils.formatCheckSumOctalBytes(value, result, 0, result.length);
        Assert.assertEquals("Invalid formatCheckSumOctalBytes result", 11, offset);
        Assert.assertArrayEquals("Invalid formatCheckSumOctalBytes result", new byte[]{'0', '1', '2', '3', '4', ' ', '0'}, result, 0, 7);
    }

    /**
     * Test for formatCheckSumOctalBytes method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatCheckSumOctalBytesInvalid() {
        byte[] buffer = new byte[12];
        TarUtils.formatCheckSumOctalBytes(-1, buffer, 0, buffer.length);
    }
}
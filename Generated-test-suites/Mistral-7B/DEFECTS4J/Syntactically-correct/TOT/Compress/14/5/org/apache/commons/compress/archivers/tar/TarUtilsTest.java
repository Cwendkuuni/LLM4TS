package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.utils.ByteStreams;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.compress.utils.IOUtils.BYTE_ARRAY_OUT;

/**
 * Test class for TarUtils methods.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid octal strings.
     */
    @Test
    public void testParseOctalValid() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', ' ', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(0755L, result);

        buffer = new byte[]{'0', '1', '1', '1', ' ', '0'};
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(0111L, result);

        buffer = new byte[]{'0', '0', ' ', ' '};
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(0L, result);
    }

    /**
     * Test parseOctal method with invalid octal strings.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalid() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', ' ', '0'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parseOctalOrBinary method with valid binary strings.
     */
    @Test
    public void testParseOctalOrBinaryValidBinary() {
        byte[] buffer = new byte[]{(byte) 0x81, 0x01, 0x02, 0x03, 0x04, 0x05};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assert.assertEquals(184325L, result); // 0x81_01_02_03_04_05
    }

    /**
     * Test parseOctalOrBinary method with valid octal strings.
     */
    @Test
    public void testParseOctalOrBinaryValidOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', ' ', '0'};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assert.assertEquals(0755L, result);
    }

    /**
     * Test parseOctalOrBinary method with invalid binary strings.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryInvalidBinary() {
        byte[] buffer = new byte[]{(byte) 0x81, 0x01, 0x02, 0x03, (byte) 0x80};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    /**
     * Test parseOctalOrBinary method with invalid octal strings.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryInvalidOctal() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', ' ', '0'};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    /**
     * Test parseBoolean method.
     */
    @Test
    public void testParseBooleanTrue() {
        byte[] buffer = new byte[]{1};
        boolean result = TarUtils.parseBoolean(buffer, 0);
        Assert.assertTrue(result);
    }

    /**
     * Test parseBoolean method.
     */
    @Test
    public void testParseBooleanFalse() {
        byte[] buffer = new byte[]{0};
        boolean result = TarUtils.parseBoolean(buffer, 0);
        Assert.assertFalse(result);
    }

    /**
     * Test parseName method.
     */
    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'f', 'i', 'l', 'e', '.', 't', 'e', 's', 't', 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals("file.test", result);
    }

    /**
     * Test formatNameBytes method.
     */
    @Test
    public void testFormatNameBytes() {
        String name = "file.test";
        byte[] buffer = new byte[TarConstants.MAXNAMELEN];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);

        byte[] expected = new byte[]{'f', 'i', 'l', 'e', '.', 't', 'e', 's', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        for (int i = 0; i < offset; i++) {
            Assert.assertEquals(expected[i], buffer[i]);
        }
    }

    /**
     * Test formatUnsignedOctalString method.
     */
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0777;
        byte[] buffer = new byte[TarConstants.MAXID + 2];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);

        byte[] expected = new byte[]{'0', '1', '1', '1', '7', '7', ' ', '0'};
        for (int i = 0; i < buffer.length; i++) {
            Assert.assertEquals(expected[i], buffer[i]);
        }
    }

    /**
     * Test formatOctalBytes method.
     */
    @Test
    public void testFormatOctalBytes() {
        long value = 0777;
        byte[] buffer = new byte[TarConstants.MAXID + 2];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);

        byte[] expected = new byte[]{'0', '1', '1', '1', '7', '7', ' ', '0', ' ', 0};
        for (int i = 0; i < offset; i++) {
            Assert.assertEquals(expected[i], buffer[i]);
        }
    }

    /**
     * Test formatLongOctalBytes method.
     */
    @Test
    public void testFormatLongOctalBytes() {
        long value = 01777777777L;
        byte[] buffer = new byte[TarConstants.MAXID + 2];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);

        byte[] expected = new byte[]{'0', '1', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', ' ', 0};
        for (int i = 0; i < offset; i++) {
            Assert.assertEquals(expected[i], buffer[i]);
        }
    }

    /**
     * Test formatLongOctalOrBinaryBytes method.
     */
    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 123456789012345L;
        byte[] buffer = new byte[TarConstants.MAXID + 2];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);

        // Check octal format
        byte[] expectedOctal = new byte[]{'0', '1', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', ' ', '0'};
        for (int i = 0; i < offset; i++) {
            Assert.assertEquals(expectedOctal[i], buffer[i]);
        }

        // Check binary format
        byte[] expectedBinary = new byte[]{0x81, 0x13, 0x45, 0x67, 0x88, 0x90, 0x12, 0x34, 0x56, 0x78, 0x90, 0x12, 0x34, 0x5L, 0x78, 0x90, 0x12, 0x34, 0x5L, 0x67, 0x88, 0x90, 0x12, 0x34, 0x5L};
        for (int i = offset; i < buffer.length; i++) {
            Assert.assertEquals(expectedBinary[i - offset], buffer[i]);
        }
    }

    /**
     * Test computeCheckSum method.
     */
    @Test
    public void testComputeCheckSum() {
        byte[] header = new byte[]{0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
        long expected = TarUtils.computeCheckSum(header);
        long result = TarUtils.computeCheckSum(header);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test parseCheckSumOctalBytes method.
     */
    @Test
    public void testParseCheckSumOctalBytes() {
        long checksum = 01234567;
        byte[] buffer = new byte[]{0, ' ', '0', '1', '2', '3', '4', '5', '6', '7'};
        long expected = checksum;
        long result = TarUtils.parseCheckSumOctalBytes(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }
}
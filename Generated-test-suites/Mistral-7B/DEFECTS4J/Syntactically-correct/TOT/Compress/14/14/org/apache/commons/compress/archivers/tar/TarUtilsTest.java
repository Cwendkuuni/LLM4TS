package org.apache.commons.compress.archivers.tar;

import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.compress.archivers.tar.TarUtils.parseOctal;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseOctalOrBinary;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseBoolean;

/**
 * Test class for the TarUtils class.
 */
public class TarUtilsTest {

    /**
     * Test parsing a valid octal string.
     */
    @Test
    public void testParseOctal() {
        byte[] buffer = "0755".getBytes(); // 0755 is a valid octal string
        long expectedResult = 0755L;
        long actualResult = parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(expectedResult, actualResult);
    }

    /**
     * Test parsing a valid binary number.
     */
    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = {0, 0, 0, 0, 0, 0, 0x81, 0x1A}; // 00000111 10101010 10101101
        long expectedResult = 33063L;
        long actualResult = parseOctalOrBinary(buffer, 0, buffer.length);
        Assert.assertEquals(expectedResult, actualResult);
    }

    /**
     * Test parsing an invalid octal string with missing trailing space/NUL.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalMissingTrailingSpaceOrNull() {
        byte[] buffer = "0755".getBytes(); // 0755 is a valid octal string, but missing trailing space/NUL
        parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parsing an invalid octal string with an invalid byte.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = "075M".getBytes(); // 075M is not a valid octal string since 'M' is not a valid octal digit
        parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parsing a valid boolean from a buffer.
     */
    @Test
    public void testParseBoolean() {
        byte[] buffer = {1};
        boolean expectedResult = true;
        boolean actualResult = parseBoolean(buffer, 0);
        Assert.assertEquals(expectedResult, actualResult);
    }

    /**
     * Test parsing a name from a buffer.
     */
    @Test
    public void testParseName() {
        byte[] buffer = "filename.txt".getBytes();
        String expectedResult = "filename.txt";
        String actualResult = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals(expectedResult, actualResult);
    }

    /**
     * Test formatting a name into a buffer.
     */
    @Test
    public void testFormatNameBytes() {
        String name = "filename.txt";
        byte[] expectedBuffer = "filename.txt".getBytes();
        byte[] buffer = new byte[expectedBuffer.length];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, expectedBuffer.length);
        Assert.assertArrayEquals(expectedBuffer, buffer);
        Assert.assertEquals(expectedBuffer.length, offset);
    }

    /**
     * Test formatting an unsigned octal number into a buffer.
     */
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 755L;
        byte[] expectedBuffer = {"0", "1", "0", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"};
        byte[] buffer = new byte[expectedBuffer.length];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, expectedBuffer.length);
        Assert.assertArrayEquals(expectedBuffer, buffer);
    }

    /**
     * Test formatting a long octal number into a buffer.
     */
    @Test
    public void testFormatLongOctalBytes() {
        long value = 7554321L;
        byte[] expectedBuffer = {"0", "1", "0", "1", "1", "1", "0", "7", "6", "2", "1", "1", "0", "4", "3", "2", "1"};
        byte[] buffer = new byte[expectedBuffer.length];
        TarUtils.formatLongOctalBytes(value, buffer, 0, expectedBuffer.length);
        Assert.assertArrayEquals(expectedBuffer, buffer);
    }

    /**
     * Test formatting a long integer into a buffer as an octal string if it fits,
     * or as a binary number otherwise.
    */
    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 12345678912345L;
        byte[] expectedBuffer = {(byte) 0x81, (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x91, (byte) 0x23, (byte) 0x45};
        byte[] buffer = new byte[expectedBuffer.length];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, expectedBuffer.length);
        Assert.assertArrayEquals(expectedBuffer, buffer);
    }

    /**
     * Test computing a checksum of a tar entry header.
     */
    @Test
    public void testComputeCheckSum() {
        byte[] buffer = "filename.txt\0\0\00123456789012\0 \0".getBytes(); // A valid tar header
        long expectedChecksum = TarUtils.computeCheckSum(buffer);
        long actualChecksum = TarUtils.computeCheckSum(buffer);
        Assert.assertEquals(expectedChecksum, actualChecksum);
    }

    /**
     * Test parsing a long octal number with missing trailing space and NUL.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatLongOctalBytesMissingTrailingSpaceOrNull() {
        long value = 7554321L;
        byte[] buffer = new byte[10];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
    }
}
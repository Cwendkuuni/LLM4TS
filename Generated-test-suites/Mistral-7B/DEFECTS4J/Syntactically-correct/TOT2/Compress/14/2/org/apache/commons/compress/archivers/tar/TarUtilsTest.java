package org.apache.commons.compress.archivers.tar.test;

import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for the TarUtils class.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid input.
     */
    @Test
    public void testParseOctal_ValidInput() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', ' ', 'N'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(01234567, result);
    }

    /**
     * Test parseOctal method with invalid input - missing trailing space or NUL.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_MissingTrailingSpaceOrNull() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parseOctal method with invalid input - invalid byte detected.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidByteDetected() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parseOctalOrBinary method with valid input.
     */
    @Test
    public void testParseOctalOrBinary_ValidInput() {
        byte[] buffer = new byte[]{(byte) 0x81, '1', '2', '3', '4', '5', '6', '7', '8'};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assert.assertEquals(01234567, result);
    }

    /**
     * Test parseOctalOrBinary method with valid input - binary number.
     */
    @Test
    public void testParseOctalOrBinary_BinaryNumber() {
        byte[] buffer = new byte[]{(byte) 0x81, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assert.assertEquals(128 + (1L << 10) + (1L << 20), result);
    }

    /**
     * Test parseOctalOrBinary method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinary_InvalidInput() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    /**
     * Test parseBoolean method with valid input.
     */
    @Test
    public void testParseBoolean_ValidInput() {
        byte[] buffer = new byte[]{1};
        boolean result = TarUtils.parseBoolean(buffer, 0);
        Assert.assertTrue(result);
    }

    /**
     * Test parseBoolean method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseBoolean_InvalidInput() {
        byte[] buffer = new byte[]{0};
        TarUtils.parseBoolean(buffer, 0);
    }

    /**
     * Test parseName method with valid input.
     */
    @Test
    public void testParseName_ValidInput() {
        byte[] buffer = new byte[]{'/', 'a', 'b', 'c', 'd', '/', 'e', 'f', 'g', 'h', 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertThat(result, is("abcd/efgkh"));
    }

    /**
     * Test parseName method with missing trailing null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseName_MissingTrailingNull() {
        byte[] buffer = new byte[]{'/', 'a', 'b', 'c', 'd', '/', 'e', 'f', 'g', 'h'};
        TarUtils.parseName(buffer, 0, buffer.length);
    }

    /**
     * Test formatNameBytes method with valid input.
     */
    @Test
    public void testFormatNameBytes_ValidInput() {
        String name = "abcd/efgkh";
        byte[] buffer = new byte[TarConstants.MAXNAMELEN];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, TarConstants.MAXNAMELEN);
        assertThat(new String(buffer, 0, offset), is(name));
    }

    /**
     * Test formatUnsignedOctalString method with valid input.
     */
    @Test
    public void testFormatUnsignedOctalString_ValidInput() {
        long value = 01234567;
        byte[] buffer = new byte[TarConstants.MAXID];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String result = new String(buffer, 0, offset);
        assertThat(result, is("01234567"));
    }

    /**
     * Test formatCheckSumOctalBytes method with valid input.
     */
    @Test
    public void testFormatCheckSumOctalBytes_ValidInput() {
        long value = 01234567;
        byte[] buffer = new byte[TarConstants.CHECKSUMSIZE];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        String result = new String(buffer, 0, offset - 1); // exclude trailing space
        assertThat(result, is("01234567"));
    }

    /**
     * Test computeCheckSum method with valid input.
     */
    @Test
    public void testComputeCheckSum_ValidInput() {
        byte[] buffer = new byte[]{0x1, 0x2, 0x3, 0x4, 0x5, 0x6};
        long checksum = TarUtils.computeCheckSum(buffer);
        long computedChecksum = 0;
        for (byte b : buffer) {
            computedChecksum += TarUtils.BYTE_MASK & b;
        }
        Assert.assertEquals(checksum, computedChecksum);
    }
}
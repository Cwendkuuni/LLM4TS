package org.apache.commons.compress.archivers.tar.test;

import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.compress.archivers.tar.TarUtils.BYTE_MASK;

/**
 * Test class for TarUtils class.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid input.
     */
    @Test
    public void testParseOctalValidInput() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7'};
        long expectedResult = 01234567L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(expectedResult, result);
    }

    /**
     * Test parseOctal method with null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalNullInput() {
        TarUtils.parseOctal(null, 0, 0);
    }

    /**
     * Test parseOctal method with negative offset.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testParseOctalNegativeOffset() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7'};
        TarUtils.parseOctal(buffer, -1, buffer.length);
    }

    /**
     * Test parseOctal method with length greater than buffer size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalLengthGreaterThanBufferSize() {
        byte[] buffer = new byte[]{'0', '1', '2', '3'};
        TarUtils.parseOctal(buffer, 0, buffer.length + 1);
    }

    /**
     * Test parseOctal method with non-octal input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalNonOctalInput() {
        byte[] buffer = new byte[]{'A', 'B', 'C', 'D'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parseName method with valid input.
     */
    @Test
    public void testParseNameValidInput() {
        byte[] buffer = new byte[]{'/', 't', 'a', 'r', '.', 'd', 'i', 'r', '/', 'f', 'i', 'l', 'e', '.', 't', 'x', 't', 'x'};
        String expectedResult = "/tar.dir/file.txt";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals(expectedResult, result);
    }

    /**
     * Test parseName method with null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseNameNullInput() {
        TarUtils.parseName(null, 0, 0);
    }

    /**
     * Test parseName method with negative offset.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testParseNameNegativeOffset() {
        byte[] buffer = new byte[]{'/', 't', 'a', 'r', '.', 'd', 'i', 'r', '/', 'f', 'i', 'l', 'e', '.', 't', 'x', 't', 'x'};
        TarUtils.parseName(buffer, -1, buffer.length);
    }

    /**
     * Test parseName method with length greater than buffer size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseNameLengthGreaterThanBufferSize() {
        byte[] buffer = new byte[]{'/', 't', 'a', 'r', '.'};
        TarUtils.parseName(buffer, 0, buffer.length + 1);
    }

    /**
     * Test formatNameBytes method with valid input.
     */
    @Test
    public void testFormatNameBytesValidInput() {
        String name = "filename.ext";
        byte[] buffer = new byte[20];
        int offset = 0;
        int length = name.length();
        int result = TarUtils.formatNameBytes(name, buffer, offset, length);

        for (int i = 0; i < length; ++i) {
            Assert.assertEquals(name.charAt(i), (char) buffer[offset + i]);
        }
        Assert.assertEquals(offset + length, result);
    }

    /**
     * Test formatNameBytes method with null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatNameBytesNullInput() {
        TarUtils.formatNameBytes(null, new byte[10], 0, 10);
    }

    /**
     * Test formatNameBytes method with negative offset.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testFormatNameBytesNegativeOffset() {
        String name = "filename.ext";
        byte[] buffer = new byte[10];
        TarUtils.formatNameBytes(name, buffer, -1, 10);
    }

    /**
     * Test formatNameBytes method with length greater than buffer size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatNameBytesLengthGreaterThanBufferSize() {
        String name = "filename.ext";
        byte[] buffer = new byte[5];
        TarUtils.formatNameBytes(name, buffer, 0, name.length() + 1);
    }

    /**
     * Test formatUnsignedOctalString method with valid input.
     */
    @Test
    public void testFormatUnsignedOctalStringValidInput() {
        long value = 123456789L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 9;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        for (int i = 0; i < length; ++i) {
            Assert.assertEquals(Byte.toUnsignedInt(buffer[offset + i]) & BYTE_MASK, (value >> (i * 3)) & 0x7);
        }
    }

    /**
     * Test formatUnsignedOctalString method with negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringNegativeLength() {
        long value = 123456789L;
        byte[] buffer = new byte[5];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, -1);
    }

    /**
     * Test computeCheckSum method with valid input.
     */
    @Test
    public void testComputeCheckSumValidInput() {
        byte[] buffer = new byte[]{1, 2, 3, 4, 5};
        long expectedCheckSum = 2111110111L;
        long checkSum = TarUtils.computeCheckSum(buffer);
        Assert.assertEquals(expectedCheckSum, checkSum);
    }

    /**
     * Test computeCheckSum method with null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testComputeCheckSumNullInput() {
        TarUtils.computeCheckSum(null);
    }

    /**
     * Test formatOctalBytes method with valid input.
     */
    @Test
    public void testFormatOctalBytesValidInput() {
        long value = 123456789L;
        byte[] buffer = new byte[12];
        int offset = 0;
        int length = 9;
        TarUtils.formatOctalBytes(value, buffer, offset, length);

        for (int i = 0; i < length; ++i) {
            Assert.assertEquals(Byte.toUnsignedInt(buffer[offset + i]) & BYTE_MASK, (value >> (i * 3)) & 0x7);
        }

        Assert.assertEquals(offset + length, buffer.length);
        Assert.assertEquals((byte) ' ', buffer[offset + length]);
        Assert.assertEquals((byte) 0, buffer[offset + length + 1]);
    }

    /**
     * Test formatOctalBytes method with negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesNegativeLength() {
        long value = 123456789L;
        byte[] buffer = new byte[5];
        TarUtils.formatOctalBytes(value, buffer, 0, -1);
    }
}
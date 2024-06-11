package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.utils.ByteStreams;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.compress.archivers.tar.TarUtils.BYTE_MASK;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseOctal;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseName;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatNameBytes;

/**
 * Tests for the TarUtils class.
 */
public class TarUtilsTest {

    /**
     * Test parsing of valid octal strings.
     */
    @Test
    public void testParseValidOctalStrings() {
        byte[] testData = new byte[]{'0', '7', '5', '5', '0', ' '};
        long result = TarUtils.parseOctal(testData, 0, testData.length);
        Assert.assertEquals("Expected 017550", (long) 017550, result);

        testData = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '0'};
        result = TarUtils.parseOctal(testData, 0, testData.length);
        Assert.assertEquals("Expected 012345670", (long) 012345670, result);

        testData = new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', '0'};
        result = TarUtils.parseOctal(testData, 0, testData.length);
        Assert.assertEquals("Expected 077777770", (long) 077777770, result);
    }

    /**
     * Test parsing of invalid octal strings with non-octal characters.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidOctalStrings() {
        byte[] testData = new byte[]{'0', '1', '2', '3', 'x', '4', '5', '6', '7', '0'};
        TarUtils.parseOctal(testData, 0, testData.length);
    }

    /**
     * Test parsing of invalid octal strings with length less than 2 bytes.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidOctalStringsLength() {
        byte[] testData = new byte[]{0};
        TarUtils.parseOctal(testData, 0, testData.length);
    }

    /**
     * Test parsing of empty octal strings.
     */
    @Test
    public void testParseEmptyOctalStrings() {
        byte[] testData = new byte[]{0};
        long result = TarUtils.parseOctal(testData, 0, testData.length);
        Assert.assertEquals("Expected 0", (long) 0, result);
    }

    /**
     * Test parsing of null octal strings.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseNullOctalStrings() {
        TarUtils.parseOctal(null, 0, 0);
    }

    /**
     * Test parsing of name strings.
     */
    @Test
    public void testParseNameStrings() {
        byte[] testData = new byte[]{0x20, 0x73, 0x74, 0x61, 0x72, 0x74, 0x65, 0x64, 0x00};
        String result = TarUtils.parseName(testData, 0, testData.length);
        Assert.assertEquals("Expected 'stats'", result);

        testData = new byte[]{0x00};
        result = TarUtils.parseName(testData, 0, testData.length);
        Assert.assertEquals("Expected ''", result);

        testData = new byte[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '.', '0', 0};
        result = TarUtils.parseName(testData, 0, testData.length);
        Assert.assertEquals("Expected 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789..' for the maximum length of a tar entry name", result);
    }

    /**
     * Test formatting name strings into byte arrays.
     */
    @Test
    public void testFormatNameStrings() {
        String name = "TestName";
        byte[] result = new byte[name.length() + 1];
        TarUtils.formatNameBytes(name, result, 0, result.length);
        byte[] expected = new byte[]{'T', 'e', 's', 't', 'N', 'a', 'm', 'e'};
        Assert.assertArrayEquals("Expected 'TestName'", expected, result);

        name = "";
        result = new byte[name.length() + 1];
        TarUtils.formatNameBytes(name, result, 0, result.length);
        Assert.assertArrayEquals("Expected ''", new byte[]{0}, result);
    }

    /**
     * Test formatting unsigned octal numbers into byte arrays.
     */
    @Test
    public void testFormatUnsignedOctalNumbers() {
        long value = 0;
        byte[] result = new byte[7];
        TarUtils.formatUnsignedOctalString(value, result, 0, result.length);
        byte[] expected = new byte[]{0};
        Assert.assertArrayEquals("Expected '0'", expected, result);

        value = 1;
        TarUtils.formatUnsignedOctalString(value, result, 0, result.length);
        expected = new byte[]{0, '1'};
        Assert.assertArrayEquals("Expected '01'", expected, result);

        value = 77;
        TarUtils.formatUnsignedOctalString(value, result, 0, result.length);
        expected = new byte[]{'0', '1', '1'};
        Assert.assertArrayEquals("Expected '011'", expected, result);

        value = 255;
        TarUtils.formatUnsignedOctalString(value, result, 0, result.length);
        expected = new byte[]{'0', '3', '7', '7'};
        Assert.assertArrayEquals("Expected '0377'", expected, result);

        value = 65535;
        TarUtils.formatUnsignedOctalString(value, result, 0, result.length);
        expected = new byte[]{'0', '7', '7', '7', '7'};
        Assert.assertArrayEquals("Expected '07777'", expected, result);

        value = Long.MAX_VALUE;
        TarUtils.formatUnsignedOctalString(value, result, 0, result.length);
        expected = new byte[]{'3', '7', '7', '7', '7', '7', '7'};
        Assert.assertArrayEquals("Expected '377777777'", expected, result);
    }

    /**
     * Test formatting octal long integers into byte arrays.
     */
    @Test
    public void testFormatLongOctalNumbers() {
        long value = 0;
        byte[] result = new byte[6];
        TarUtils.formatLongOctalBytes(value, result, 0, result.length);
        byte[] expected = new byte[]{0, '0'};
        Assert.assertArrayEquals("Expected '00'", expected, result);

        value = 1;
        TarUtils.formatLongOctalBytes(value, result, 0, result.length);
        expected = new byte[]{0, '1'};
        Assert.assertArrayEquals("Expected '01'", expected, result);

        value = 10;
        TarUtils.formatLongOctalBytes(value, result, 0, result.length);
        expected = new byte[]{0, '1', '2'};
        Assert.assertArrayEquals("Expected '012'", expected, result);

        value = 100;
        TarUtils.formatLongOctalBytes(value, result, 0, result.length);
        expected = new byte[]{0, '1', '4', '0'};
        Assert.assertArrayEquals("Expected '0140'", expected, result);

        value = 1000;
        TarUtils.formatLongOctalBytes(value, result, 0, result.length);
        expected = new byte[]{0, '2', '0', '0'};
        Assert.assertArrayEquals("Expected '0200'", expected, result);

        value = 10000;
        TarUtils.formatLongOctalBytes(value, result, 0, result.length);
        expected = new byte[]{0, '2', '2', '0'};
        Assert.assertArrayEquals("Expected '0220'", expected, result);

        value = 100000;
        TarUtils.formatLongOctalBytes(value, result, 0, result.length);
        expected = new byte[]{0, '3', '0', '0'};
        Assert.assertArrayEquals("Expected '0300'", expected, result);

        value = 1000000;
        TarUtils.formatLongOctalBytes(value, result, 0, result.length);
        expected = new byte[]{0, '3', '4', '0'};
        Assert.assertArrayEquals("Expected '0340'", expected, result);

        value = Long.MAX_VALUE;
        TarUtils.formatLongOctalBytes(value, result, 0, result.length);
        expected = new byte[]{"3", "7", "7", "7", "7", "7", "7", '7'};
        Assert.assertArrayEquals("Expected '377777777'", expected, result);

        value = Long.MIN_VALUE;
        TarUtils.formatLongOctalBytes(value, result, 0, result.length);
        expected = new byte[]{"3", "7", "7", "7", "7", "7", "7", "3"};
        Assert.assertArrayEquals("Expected '377777773'", expected, result);
    }

    /**
     * Test formatting checksum octal numbers into byte arrays.
     */
    @Test
    public void testFormatChecksumOctalNumbers() {
        long value = 0;
        byte[] result = new byte[5];
        TarUtils.formatCheckSumOctalBytes(value, result, 0, result.length);
        byte[] expected = new byte[]{0, '0', '0', '0'};
        Assert.assertArrayEquals("Expected '0000'", expected, result);

        value = 1;
        TarUtils.formatCheckSumOctalBytes(value, result, 0, result.length);
        expected = new byte[]{0, '0', '0', '1'};
        Assert.assertArrayEquals("Expected '0001'", expected, result);

        value = 77;
        TarUtils.formatCheckSumOctalBytes(value, result, 0, result.length);
        expected = new byte[]{0, '1', '1', '1'};
        Assert.assertArrayEquals("Expected '0111'", expected, result);

        value = 255;
        TarUtils.formatCheckSumOctalBytes(value, result, 0, result.length);
        expected = new byte[]{"0", "3", "7", "7", '0'};
        Assert.assertArrayEquals("Expected '03770'", expected, result);

        value = 65535;
        TarUtils.formatCheckSumOctalBytes(value, result, 0, result.length);
        expected = new byte[]{"0", "7", "7", "7", '7'};
        Assert.assertArrayEquals("Expected '07777'", expected, result);

        value = Long.MAX_VALUE;
        TarUtils.formatCheckSumOctalBytes(value, result, 0, result.length);
        expected = new byte[]{"3", "7", "7", "7", '7', '7'};
        Assert.assertArrayEquals("Expected '377777777'", expected, result);

        value = Long.MIN_VALUE;
        TarUtils.formatCheckSumOctalBytes(value, result, 0, result.length);
        expected = new byte[]{"3", "7", "7", "7", '7', '3'};
        Assert.assertArrayEquals("Expected '37777773'", expected, result);
    }

    /**
     * Test computing checksums.
     */
    @Test
    public void testComputeChecksums() {
        byte[] testData = new byte[]{0x66, 0x6D, 0x6F, 0x00, 0x14, 0x00, 0x00, 0x00};
        long checksum = TarUtils.computeCheckSum(testData);
        long expected = 0x87217C64231546D3L;
        Assert.assertEquals("Expected 0x87217C64231546D3", Long.toHexString(expected), Long.toHexString(checksum));

        testData = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        checksum = TarUtils.computeCheckSum(testData);
        expected = 0L;
        Assert.assertEquals("Expected 0", Long.toHexString(expected), Long.toHexString(checksum));
    }
}
package org.apache.commons.compress.archivers.tar;

import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.compress.archivers.tar.TarUtils.BYTE_MASK;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatCheckSumOctalBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatLongOctalBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatNameBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatUnsignedOctalString;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseOctal;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseName;

/**
 * Unit tests for the TarUtils class.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid octal strings.
     */
    @Test
    public void testParseOctal() {
        byte[] testData = new byte[]{'0', '1', '7', '7', '5', '5', '5', '5', '0'};
        long result = TarUtils.parseOctal(testData, 0, testData.length);
        long expected = 017755550L;
        Assert.assertEquals(expected, result);

        testData = new byte[]{'0', '0', '7', '7', '5', '5', '5', '5', '0'};
        result = TarUtils.parseOctal(testData, 0, testData.length);
        expected = 007755550L;
        Assert.assertEquals(expected, result);

        testData = new byte[]{'0', '1', '7', '0', '5', '5', '5', '5', '0'};
        result = TarUtils.parseOctal(testData, 0, testData.length);
        expected = 017055550L;
        Assert.assertEquals(expected, result);
    }

    /**
     * Test parseOctal method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidInput() {
        byte[] testData1 = new byte[]{'0', '1', '7', '8', '5', '5', '5', '5', '0'};
        TarUtils.parseOctal(testData1, 0, testData1.length);
    }

    /**
     * Test parseName method with valid names.
     */
    @Test
    public void testParseName() {
        byte[] testData = new byte[]{'/', 'a', '.', 'b', 'c', '.', 'd', '.', 'e', '.', 'f', '/', 0};
        String result = TarUtils.parseName(testData, 0, testData.length);
        String expected = "/a/b.c./d./e./f/";
        Assert.assertEquals(expected, result);
    }

    /**
     * Test parseName method with empty name.
     */
    @Test
    public void testParseNameEmptyName() {
        byte[] testData = new byte[]{'/'};
        String result = TarUtils.parseName(testData, 0, testData.length);
        Assert.assertEquals("", result);
    }

    /**
     * Test formatNameBytes method with valid names and different buffer sizes.
     */
    @Test
    public void testFormatNameBytes() {
        String name = "/a/b.c./d./e./f/";
        byte[] result1 = new byte[name.length() + 1];
        int offset = TarUtils.formatNameBytes(name, result1, 0, result1.length);

        byte[] result2 = new byte[name.length()];
        offset = TarUtils.formatNameBytes(name, result2, 0, result2.length);

        Assert.assertArrayEquals(result1, result2);
    }

    /**
     * Test formatUnsignedOctalString method with valid values.
     */
    @Test
    public void testFormatUnsignedOctalString() {
        byte[] result1 = new byte[10];
        long value1 = 177555550L;
        int offset = TarUtils.formatUnsignedOctalString(value1, result1, 0, result1.length);

        byte[] result2 = new byte[8];
        offset = TarUtils.formatUnsignedOctalString(value1, result2, 0, result2.length);

        Assert.assertArrayEquals(result1, result2);
    }

    /**
     * Test formatUnsignedOctalString method with invalid value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringInvalidValue() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
    }

    /**
     * Test formatOctalBytes method with valid values.
     */
    @Test
    public void testFormatOctalBytes() {
        long value = 177555550L;
        byte[] result1 = new byte[11];
        int offset = TarUtils.formatOctalBytes(value, result1, 0, result1.length);

        byte[] result2 = new byte[10];
        offset = TarUtils.formatOctalBytes(value, result2, 0, result2.length);

        Assert.assertArrayEquals(result1, result2);
    }

    /**
     * Test formatLongOctalBytes method with valid values.
     */
    @Test
    public void testFormatLongOctalBytes() {
        long value = 17755555017755550L;
        byte[] result = new byte[13];
        int offset = TarUtils.formatLongOctalBytes(value, result, 0, result.length);

        Assert.assertEquals(13, offset);
    }

    /**
     * Test formatCheckSumOctalBytes method with valid values.
     */
    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = TarUtils.computeCheckSum(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        byte[] result = new byte[12];
        int offset = TarUtils.formatCheckSumOctalBytes(value, result, 0, result.length);

        Assert.assertEquals(12, offset);
    }
}
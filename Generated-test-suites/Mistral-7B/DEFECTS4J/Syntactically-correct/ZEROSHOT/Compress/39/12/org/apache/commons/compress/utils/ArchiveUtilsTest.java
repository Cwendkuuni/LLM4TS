package org.apache.commons.compress.utils;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Test class for {@code ArchiveUtils}
 */
public class ArchiveUtilsTest {

    /**
     * Test method for {@link ArchiveUtils#toString(ArchiveEntry)}
     */
    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        String result = ArchiveUtils.toString(entry);
        String expected = "-        0 test.txt";
        Assert.assertEquals(expected, result);

        entry = new ArchiveEntry("test.txt");
        entry.setDirectory(true);
        result = ArchiveUtils.toString(entry);
        expected = "d        0 test.txt";
        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for {@link ArchiveUtils#matchAsciiBuffer(String, byte[], int, int)}
     */
    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = "test".getBytes(CharsetNames.US_ASCII);

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length);
        Assert.assertTrue(result);
    }

    /**
     * Test method for {@link ArchiveUtils#matchAsciiBuffer(String, byte[])}
     */
    @Test
    public void testMatchAsciiBufferWithOffset() {
        String expected = "test";
        byte[] buffer = new byte[]{0x74, 0x65, 0x73, 0x74};

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertTrue(result);
    }

    /**
     * Test method for {@link ArchiveUtils#toAsciiBytes(String)}
     */
    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        byte[] expected = new byte[]{0x74, 0x65, 0x73, 0x74};
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link ArchiveUtils#toAsciiString(byte[])}
     */
    @Test
    public void testToAsciiString() {
        byte[] inputBytes = new byte[]{0x74, 0x65, 0x73, 0x74};
        String result = ArchiveUtils.toAsciiString(inputBytes);
        String expected = "test";
        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for {@link ArchiveUtils#toAsciiString(byte[], int, int)}
     */
    @Test
    public void testToAsciiStringWithOffset() {
        byte[] inputBytes = new byte[]{0x74, 0x65, 0x73, 0x74};
        String result = ArchiveUtils.toAsciiString(inputBytes, 0, inputBytes.length);
        String expected = "test";
        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for {@link ArchiveUtils#isEqual(byte[], int, int, byte[], int, int, boolean)}
     */
    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{0x74, 0x65, 0x73, 0x74};
        byte[] buffer2 = new byte[]{0x74, 0x65, 0x73, 0x74};

        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
        Assert.assertTrue(result);

        buffer2 = new byte[]{0x74, 0x65, 0x73, 0x75};
        result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
        Assert.assertFalse(result);
    }

    /**
     * Test method for {@link ArchiveUtils#isEqual(byte[], int, int, byte[], int, int)}
     */
    @Test
    public void testIsEqualWithOffset() {
        byte[] buffer1 = new byte[]{0x74, 0x65, 0x73, 0x74};
        byte[] buffer2 = new byte[]{0x74, 0x65, 0x73, 0x74};

        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
        Assert.assertTrue(result);

        buffer2 = new byte[]{0x74, 0x65, 0x73, 0x75};
        result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
        Assert.assertFalse(result);
    }

    /**
     * Test method for {@link ArchiveUtils#isEqual(byte[], byte[])}
     */
    @Test
    public void testIsEqualWithoutOffset() {
        byte[] buffer1 = new byte[]{0x74, 0x65, 0x73, 0x74};
        byte[] buffer2 = new byte[]{0x74, 0x65, 0x73, 0x74};

        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        Assert.assertTrue(result);

        buffer2 = new byte[]{0x74, 0x65, 0x73, 0x75};
        result = ArchiveUtils.isEqual(buffer1, buffer2);
        Assert.assertFalse(result);
    }

    /**
     * Test method for {@link ArchiveUtils#isEqual(byte[], byte[], boolean)}
     */
    @Test
    public void testIsEqualWithIgnoreTraillingNulls() {
        byte[] buffer1 = new byte[]{0x74, 0x65, 0x73, 0x74, 0};
        byte[] buffer2 = new byte[]{0x74, 0x65, 0x73, 0x74, 0};

        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
        Assert.assertTrue(result);

        buffer2 = new byte[]{0x74, 0x65, 0x73, 0x75};
        result = ArchiveUtils.isEqual(buffer1, buffer2, true);
        Assert.assertFalse(result);

        buffer1 = new byte[]{0, 0, 0, 0};
        buffer2 = new byte[]{0, 0, 0, 0, 0};
        result = ArchiveUtils.isEqual(buffer1, buffer2, true);
        Assert.assertTrue(result);
    }

    /**
     * Test method for {@link ArchiveUtils#isArrayZero(byte[], int)}
     */
    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[5];

        boolean result = ArchiveUtils.isArrayZero(a, 5);
        Assert.assertFalse(result);

        Arrays.fill(a, (byte) 0);
        result = ArchiveUtils.isArrayZero(a, 5);
        Assert.assertTrue(result);
    }

    /**
     * Test method for {@link ArchiveUtils#sanitize(String)}
     */
    @Test
    public void testSanitize() {
        String input = "test\u0000\u0011\u007Ftest";
        String result = ArchiveUtils.sanitize(input);
        String expected = "test??test";
        Assert.assertEquals(expected, result);
    }
}
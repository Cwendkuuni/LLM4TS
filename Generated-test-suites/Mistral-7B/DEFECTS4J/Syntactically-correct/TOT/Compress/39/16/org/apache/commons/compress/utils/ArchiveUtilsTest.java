package org.apache.commons.compress.utils;

import org.apache.commons.compress.archivers.test.TestArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Test class for ArchiveUtils
 */
public class ArchiveUtilsTest {

    /**
     * Test method for {@link ArchiveUtils#toString(ArchiveEntry)}
     */
    @Test
    public void testToString() {
        final ArchiveEntry entry = new TestArchiveEntry("test.txt", 10, false);
        final String result = ArchiveUtils.toString(entry);
        final String expected = "-       10 test.txt";
        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for {@link ArchiveUtils#matchAsciiBuffer(String, byte[], int, int)}
     */
    @Test
    public void testMatchAsciiBuffer() {
        final String expected = "test";
        final byte[] buffer = toAsciiBytes(expected);
        final boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length);
        Assert.assertTrue(result);
    }

    /**
     * Test method for {@link ArchiveUtils#matchAsciiBuffer(String, byte[])}
     */
    @Test
    public void testMatchAsciiBufferWithArrayOffset() {
        final String expected = "test";
        final byte[] buffer = toAsciiBytes(expected);
        final boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertTrue(result);
    }

    /**
     * Test method for {@link ArchiveUtils#toAsciiBytes(String)}
     */
    @Test
    public void testToAsciiBytes() {
        final String expected = "test";
        final byte[] result = ArchiveUtils.toAsciiBytes(expected);
        Assert.assertArrayEquals(toAsciiBytes(expected), result);
    }

    /**
     * Test method for {@link ArchiveUtils#toAsciiString(byte[])}
     */
    @Test
    public void testToAsciiString() {
        final String expected = "test";
        final byte[] inputBytes = toAsciiBytes(expected);
        final String result = ArchiveUtils.toAsciiString(inputBytes);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for {@link ArchiveUtils#toAsciiString(byte[], int, int)}
     */
    @Test
    public void testToAsciiStringWithArrayOffsetAndLength() {
        final String expected = "test";
        final byte[] inputBytes = toAsciiBytes(expected);
        final String result = ArchiveUtils.toAsciiString(inputBytes, 0, inputBytes.length);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for {@link ArchiveUtils#isEqual(byte[], int, int, byte[], int, int, boolean)}
     */
    @Test
    public void testIsEqual() {
        final byte[] buffer1 = toAsciiBytes("test1");
        final byte[] buffer2 = toAsciiBytes("test2");
        final boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
        Assert.assertFalse(result);

        final byte[] buffer3 = toAsciiBytes("test1");
        final boolean result2 = ArchiveUtils.isEqual(buffer3, 0, buffer3.length, buffer3, 0, buffer3.length, false);
        Assert.assertTrue(result2);

        final byte[] buffer4 = toAsciiBytes("test1");
        final byte[] buffer5 = Arrays.copyOfRange(buffer4, 0, 5);
        final boolean result3 = ArchiveUtils.isEqual(buffer4, 0, buffer4.length, buffer5, 0, buffer5.length, false);
        Assert.assertTrue(result3);

        final byte[] buffer6 = toAsciiBytes("test1");
        final byte[] buffer7 = Arrays.copyOfRange(buffer4, 5, buffer4.length);
        final boolean result4 = ArchiveUtils.isEqual(buffer6, 0, buffer6.length, buffer7, 0, buffer7.length, false);
        Assert.assertFalse(result4);

        final byte[] buffer8 = toAsciiBytes("test1");
        final byte[] buffer9 = Arrays.copyOfRange(buffer4, 0, buffer4.length + 1);
        final boolean result5 = ArchiveUtils.isEqual(buffer8, 0, buffer8.length, buffer9, 0, buffer9.length, false);
        Assert.assertFalse(result5);

        final byte[] buffer10 = toAsciiBytes("test1");
        final byte[] buffer11 = Arrays.copyOfRange(buffer4, 0, buffer4.length);
        final boolean result6 = ArchiveUtils.isEqualWithNull(buffer10, 0, buffer10.length, buffer11, 0, buffer11.length);
        Assert.assertTrue(result6);
    }

    /**
     * Test method for {@link ArchiveUtils#isArrayZero(byte[], int)}
     */
    @Test
    public void testIsArrayZero() {
        final byte[] buffer = new byte[10];
        Arrays.fill(buffer, (byte) 0);
        final boolean result = ArchiveUtils.isArrayZero(buffer, 10);
        Assert.assertTrue(result);
    }

    /**
     * Test method for {@link ArchiveUtils#sanitize(String)}
     */
    @Test
    public void testSanitize() {
        final String input = "\u0001test\u0002\u0123\u0124ABCDEFGHIJKLMNOPQRSTUVWXYZ\u0125";
        final String expected = "??test?? ??????????????????ABCDEFGHIJKLMNOPQRSTUVWXYZ???";
        final String result = ArchiveUtils.sanitize(input);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for {@link ArchiveUtils#toString(ArchiveEntry)} with an array of {@link ArchiveEntry}
     */
    @Test
    public void testToStringWithArrayOfArchiveEntry() throws IOException {
        final String testData = "testData";
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(toAsciiBytes(testData));
        final byte[] testDataBytes = outputStream.toByteArray();

        final ArchiveEntry entry1 = new TestArchiveEntry("test1.txt", testDataBytes.length, false);
        final ArchiveEntry entry2 = new TestArchiveEntry("test2.txt", testDataBytes.length, false);
        final ArchiveEntry entry3 = new TestArchiveEntry("test3.txt", testDataBytes.length, false);

        final byte[] expected = String.join("\n", Arrays.asList(
                "-        " + testDataBytes.length + " test1.txt",
                "-        " + testDataBytes.length + " test2.txt",
                "-        " + testDataBytes.length + " test3.txt"
        )).getBytes(StandardCharsets.US_ASCII);

        final String result = ArchiveUtils.toString(new ArchiveEntry[] {entry1, entry2, entry3});
        Assert.assertArrayEquals(expected, result);
    }
}
package org.apache.commons.compress.utils;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Test class for ArchiveUtils
 */
public class ArchiveUtilsTest {

    /**
     * Test toString method
     */
    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        String actual = ArchiveUtils.toString(entry);
        String expected = "-       0 test.txt";
        Assert.assertEquals(expected, actual);

        entry.setDirectory(true);
        actual = ArchiveUtils.toString(entry);
        expected = "d        0 test.txt";
        Assert.assertEquals(expected, actual);

        long size = 1024L;
        entry.setSize(size);
        actual = ArchiveUtils.toString(entry);
        int paddingLength = 7 + Long.toString(size.intValue()).length();
        expected = String.format("%-" + paddingLength + "d %s", size, "test.txt");
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test matchAsciiBuffer method
     */
    @Test
    public void testMatchAsciiBuffer() {
        String expected = "Hello World!";
        byte[] buffer = toAsciiBytes(expected);

        boolean actual = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertTrue(actual);

        byte[] buffer2 = Arrays.copyOfRange(buffer, 5, buffer.length);
        actual = ArchiveUtils.matchAsciiBuffer(expected, buffer2);
        Assert.assertFalse(actual);

        String unexpected = "Hallo Welt!";
        actual = ArchiveUtils.matchAsciiBuffer(unexpected, buffer);
        Assert.assertFalse(actual);
    }

    /**
     * Test toAsciiBytes method
     */
    @Test
    public void testToAsciiBytes() {
        String inputString = "Hello World!";
        byte[] expected = Arrays.copyOf(toAsciiBytes(inputString), toAsciiBytes(inputString).length);

        byte[] actual = ArchiveUtils.toAsciiBytes(inputString);
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test toAsciiString method
     */
    @Test
    public void testToAsciiString() {
        byte[] inputBytes = toAsciiBytes("Hello World!");
        String expected = "Hello World!";

        String actual = ArchiveUtils.toAsciiString(inputBytes);
        Assert.assertEquals(expected, actual);

        byte[] inputBytes2 = toAsciiBytes("Hellö World!");
        expected = "Hellö World!";
        actual = ArchiveUtils.toAsciiString(inputBytes2);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test isEqual method with offset and length
     */
    @Test
    public void testIsEqualWithOffsetAndLength() {
        byte[] buffer1 = toAsciiBytes("Hello World!");
        byte[] buffer2 = toAsciiBytes("Hello World!");

        boolean actual = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
        Assert.assertTrue(actual);

        byte[] buffer3 = Arrays.copyOfRange(buffer1, 5, buffer1.length);
        actual = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, false);
        Assert.assertFalse(actual);

        byte[] buffer4 = Arrays.copyOf(buffer1, buffer1.length + 1);
        actual = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer4, 0, buffer4.length, false);
        Assert.assertFalse(actual);

        actual = ArchiveUtils.isEqual(buffer1, 5, buffer1.length - 5, buffer4, 0, buffer4.length, false);
        Assert.assertFalse(actual);

        byte[] buffer5 = Arrays.copyOfRange(buffer1, 0, 5);
        actual = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer5, 0, buffer5.length, false);
        Assert.assertFalse(actual);

        byte[] buffer6 = Arrays.copyOfRange(buffer1, 0, 5);
        actual = ArchiveUtils.isEqual(buffer1, buffer1.length - 5, 5, buffer6, 0, buffer6.length, false);
        Assert.assertFalse(actual);

        byte[] buffer7 = Arrays.copyOfRange(buffer1, 5, buffer1.length);
        actual = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer7, 0, buffer7.length, false);
        Assert.assertFalse(actual);

        byte[] buffer8 = Arrays.copyOfRange(buffer1, 5, buffer1.length);
        actual = ArchiveUtils.isEqual(buffer1, buffer1.length - 5, 5, buffer8, 0, buffer8.length, false);
        Assert.assertFalse(actual);
    }

    /**
     * Test isEqual method without offset and length
     */
    @Test
    public void testIsEqualWithoutOffsetAndLength() {
        byte[] buffer1 = toAsciiBytes("Hello World!");
        byte[] buffer2 = toAsciiBytes("Hello World!");

        boolean actual = ArchiveUtils.isEqual(buffer1, buffer2);
        Assert.assertTrue(actual);

        byte[] buffer3 = Arrays.copyOfRange(buffer1, 5, buffer1.length);
        actual = ArchiveUtils.isEqual(buffer1, buffer3);
        Assert.assertFalse(actual);

        byte[] buffer4 = Arrays.copyOf(buffer1, buffer1.length + 1);
        actual = ArchiveUtils.isEqual(buffer1, buffer4);
        Assert.assertFalse(actual);

        byte[] buffer5 = Arrays.copyOfRange(buffer1, 0, 5);
        actual = ArchiveUtils.isEqual(buffer1, buffer5);
        Assert.assertFalse(actual);

        byte[] buffer6 = Arrays.copyOfRange(buffer1, 0, 5);
        actual = ArchiveUtils.isEqual(buffer1, buffer6, true);
        Assert.assertTrue(actual);

        byte[] buffer7 = Arrays.copyOfRange(buffer1, 5, buffer1.length);
        actual = ArchiveUtils.isEqual(buffer1, buffer7, true);
        Assert.assertTrue(actual);
    }

    /**
     * Test isEqual method with ignoreTrailingNulls flag
     */
    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = toAsciiBytes("Hello World!");
        byte[] buffer2 = Arrays.copyOf(buffer1, buffer1.length + 1);
        buffer2[buffer2.length - 1] = 0;

        boolean actual = ArchiveUtils.isEqual(buffer1, buffer2, true);
        Assert.assertTrue(actual);

        buffer1 = Arrays.copyOfRange(buffer1, 0, buffer1.length - 1);
        actual = ArchiveUtils.isEqual(buffer1, buffer2, true);
        Assert.assertFalse(actual);

        buffer1 = Arrays.copyOfRange(buffer1, 0, buffer1.length);
        actual = ArchiveUtils.isEqual(buffer1, buffer2, true);
        Assert.assertTrue(actual);

        byte[] buffer3 = toAsciiBytes("Hello\0World!");
        byte[] buffer4 = Arrays.copyOfRange(buffer3, 0, 5);
        buffer4[buffer4.length - 1] = 0;

        actual = ArchiveUtils.isEqual(buffer3, buffer4, true);
        Assert.assertTrue(actual);

        actual = ArchiveUtils.isEqual(buffer3, buffer4);
        Assert.assertFalse(actual);
    }

    /**
     * Test isEqualWithNull method
     */
    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = toAsciiBytes("Hello World!");
        byte[] buffer2 = Arrays.copyOf(buffer1, buffer1.length + 1);
        buffer2[buffer2.length - 1] = 0;

        boolean actual = ArchiveUtils.isEqualWithNull(buffer1, buffer2);
        Assert.assertTrue(actual);

        actual = ArchiveUtils.isEqualWithNull(buffer1, buffer2, true);
        Assert.assertTrue(actual);
    }

    /**
     * Test isArrayZero method
     */
    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[5];

        boolean actual = ArchiveUtils.isArrayZero(array, 5);
        Assert.assertTrue(actual);

        actual = ArchiveUtils.isArrayZero(array, 6);
        Assert.assertFalse(actual);

        byte[] array2 = new byte[10];
        Arrays.fill(array2, (byte) 0);

        actual = ArchiveUtils.isArrayZero(array2, 10);
        Assert.assertTrue(actual);

        actual = ArchiveUtils.isArrayZero(array2, 11);
        Assert.assertFalse(actual);
    }

    /**
     * Test sanitize method
     */
    @Test
    public void testSanitize() {
        String input = "Hello World!\n\u0000ẁẇỤỨỤỤẠẠẠẠẠ";
        String expected = "Hello World!\n???????";

        String actual = ArchiveUtils.sanitize(input);
        Assert.assertEquals(expected, actual);
    }
}
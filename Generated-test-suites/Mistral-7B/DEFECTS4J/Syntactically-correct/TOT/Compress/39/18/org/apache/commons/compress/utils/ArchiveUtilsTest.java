package org.apache.commons.compress.utils;

import org.apache.commons.compress.archivers.test.SimpleTestHelper;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Test for ArchiveUtils class
 */
public class ArchiveUtilsTest {

    /**
     * Test toString method with valid ArchiveEntry
     */
    @Test
    public void testToStringValidEntry() {
        ArchiveEntry entry = SimpleTestHelper.generateTestEntry("testfile.txt", true, 1024);
        String expectedOutput = "- 1024 testfile.txt";
        String actualOutput = ArchiveUtils.toString(entry);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test toString method with invalid ArchiveEntry
     */
    @Test
    public void testToStringInvalidEntry() {
        ArchiveEntry entry = null;
        String actualOutput = ArchiveUtils.toString(entry);
        String expectedOutput = "";
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test matchAsciiBuffer method with valid input
     */
    @Test
    public void testMatchAsciiBufferValidInput() {
        String expected = "teststring";
        byte[] buffer = toAsciiBytes(expected);
        boolean actual = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertTrue(actual);
    }

    /**
     * Test matchAsciiBuffer method with invalid input
     */
    @Test
    public void testMatchAsciiBufferInvalidInput() {
        String expected = "teststring";
        byte[] buffer = new byte[]{1, 2, 3};
        boolean actual = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertFalse(actual);
    }

    /**
     * Test isEqual method with valid input
     */
    @Test
    public void testIsEqualValidInput() {
        byte[] buffer1 = new byte[]{'a', 'b', 'c'};
        byte[] buffer2 = new byte[]{'a', 'b', 'c'};
        boolean actual = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertTrue(actual);
    }

    /**
     * Test isEqual method with invalid input
     */
    @Test
    public void testIsEqualInvalidInput() {
        byte[] buffer1 = new byte[]{'a', 'b', 'c'};
        byte[] buffer2 = new byte[]{'b', 'b', 'c'};
        boolean actual = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertFalse(actual);
    }

    /**
     * Test isEqual method with trailing null bytes
     */
    @Test
    public void testIsEqualTrailingNullBytes() {
        byte[] buffer1 = new byte[]{'a', 'b', 'c', 0, 0};
        byte[] buffer2 = new byte[]{'a', 'b', 'c'};
        boolean actual = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertTrue(actual);
    }

    /**
     * Test isEqual method with different lengths
     */
    @Test
    public void testIsEqualDifferentLengths() {
        byte[] buffer1 = new byte[]{'a', 'b', 'c'};
        byte[] buffer2 = new byte[]{'a', 'b', 'c', 'd'};
        boolean actual = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertFalse(actual);
    }

    /**
     * Test isEqual method with offsets
     */
    @Test
    public void testIsEqualOffsets() {
        byte[] buffer1 = new byte[]{'a', 'b', 'c'};
        byte[] buffer2 = new byte[]{'b', 'c', 'a'};
        boolean actual = ArchiveUtils.isEqual(buffer1, 1, buffer1.length - 1, buffer2, 0, buffer2.length, false);
        Assert.assertFalse(actual);
    }

    /**
     * Test toAsciiString method with valid input
     */
    @Test
    public void testToAsciiStringValidInput() {
        byte[] bytes = new byte[]{72, 101, 108, 108, 111};
        String actual = ArchiveUtils.toAsciiString(bytes);
        String expected = "hsstt";
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test toAsciiString method with invalid input
     */
    @Test
    public void testToAsciiStringInvalidInput() {
        byte[] bytes = new byte[0];
        String actual = ArchiveUtils.toAsciiString(bytes);
        Assert.assertEquals("", actual);
    }

    /**
     * Test ArrayUtils.isEqual method with valid input
     */
    @Test
    public void testIsEqualValidInput() {
        byte[] buffer1 = new byte[]{1, 2, 3};
        byte[] buffer2 = new byte[]{1, 2, 3};
        boolean actual = ArchiveUtils.isEqual(buffer1, buffer2);
        Assert.assertTrue(actual);
    }

    /**
     * Test ArrayUtils.isEqual method with invalid input
     */
    @Test
    public void testIsEqualInvalidInput() {
        byte[] buffer1 = new byte[]{1, 2, 3};
        byte[] buffer2 = new byte[]{4, 5, 6};
        boolean actual = ArchiveUtils.isEqual(buffer1, buffer2);
        Assert.assertFalse(actual);
    }

    /**
     * Test ArrayUtils.isEqual method with trailing null bytes
     */
    @Test
    public void testIsEqualTrailingNullBytes() {
        byte[] buffer1 = new byte[]{1, 2, 3, 0, 0};
        byte[] buffer2 = new byte[]{1, 2, 3};
        boolean actual = ArchiveUtils.isEqualWithNull(buffer1, buffer1.length - 2, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertTrue(actual);
    }

    /**
     * Test ArrayUtils.isEqual method with offsets
     */
    @Test
    public void testIsEqualOffsets() {
        byte[] buffer1 = new byte[]{1, 2, 3};
        byte[] buffer2 = new byte[]{3, 2, 1};
        boolean actual = ArchiveUtils.isEqual(buffer1, 1, buffer1.length - 1, buffer2, 0, buffer2.length, false);
        Assert.assertFalse(actual);
    }

    /**
     * Test sanitize method with valid input
     */
    @Test
    public void testSanitizeValidInput() {
        String input = "teststring";
        String expectedOutput = "teststring";
        String actualOutput = ArchiveUtils.sanitize(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test sanitize method with special characters
     */
    @Test
    public void testSanitizeSpecialCharacters() {
        String input = "\u0000\u0001\u007f\u007e\u0007\u001f\u001e\u001b\u000B\u0005\u0002\u20AC";
        String expectedOutput = "???????????????????";
        String actualOutput = ArchiveUtils.sanitize(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test sanitize method with long strings
     */
    @Test
    public void testSanitizeLongStrings() {
        String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String expectedOutput = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String actualOutput = ArchiveUtils.sanitize(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }
}
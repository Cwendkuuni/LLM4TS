package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Test;

/**
 * Test class for {@link ArchiveUtils}
 */
public class ArchiveUtilsTest {

    /**
     * Test toString method.
     *
     * @param entry the ArchiveEntry to test
     */
    @Test
    public void testToString(ArchiveEntry entry) {
        String result = ArchiveUtils.toString(entry);
        String expected;
        if (entry.isDirectory()) {
            expected = "d     100 testfiles";
        } else {
            expected = "-   2000 main.c";
        }
        assertEquals(expected, result);
    }

    /**
     * Test matchAsciiBuffer method with a valid string and buffer.
     */
    @Test
    public void testMatchAsciiBufferValid() {
        byte[] buffer = "Hello World".getBytes();
        String expected = "Hello World";
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    /**
     * Test matchAsciiBuffer method with a invalid string and buffer.
     */
    @Test
    public void testMatchAsciiBufferInvalid() {
        byte[] buffer = "Hello World".getBytes();
        String expected = "Hello Worlds";
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    /**
     * Test toAsciiBytes method.
     */
    @Test
    public void testToAsciiBytes() {
        String inputString = "Hello World";
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        byte[] expected = "Hello World".getBytes();
        assertArrayEquals(expected, result);
    }

    /**
     * Test toAsciiString method with a valid byte array.
     */
    @Test
    public void testToAsciiStringValid() {
        byte[] buffer = "Hello World".getBytes();
        String result = ArchiveUtils.toAsciiString(buffer);
        String expected = "Hello World";
        assertEquals(expected, result);
    }

    /**
     * Test isEqual method with valid byte arrays.
     */
    @Test
    public void testIsEqualValid() {
        byte[] buffer1 = "Hello World".getBytes();
        byte[] buffer2 = "Hello World".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
    }

    /**
     * Test isEqual method with invalid byte arrays.
     */
    @Test
    public void testIsEqualInvalid() {
        byte[] buffer1 = "Hello World".getBytes();
        byte[] buffer2 = "Hello Worlds".getBytes();
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
    }

    /**
     * Test isEqual method with trailing nulls.
     */
    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = {0, 0, 0, 0, 68, 101, 108, 101, 114, 108, 111};
        byte[] buffer2 = {68, 101, 108, 101, 114, 108, 111, 0, 0};
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    /**
     * Test isArrayZero method.
     */
    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[10];
        assertTrue(ArchiveUtils.isArrayZero(array, 5));
        assertFalse(ArchiveUtils.isArrayZero(array, 6));
    }

    /**
     * Test sanitize method.
     */
    @Test
    public void testSanitize() {
        String input = "abçdef\u007Fghij";
        String expected = "ab?cdef??ghij?";
        String result = ArchiveUtils.sanitize(input);
        assertEquals(expected, result);
    }
}
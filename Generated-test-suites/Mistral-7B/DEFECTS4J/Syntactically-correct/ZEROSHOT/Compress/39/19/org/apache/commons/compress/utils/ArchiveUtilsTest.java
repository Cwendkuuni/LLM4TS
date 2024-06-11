package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.TestArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;

import org.junit.Test;

/**
 * JUnit 4 test file for ArchiveUtils class
 */
public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry archiveEntry = new TestArchiveEntry("testfiles/", "testfiles/test1.txt", false, 1234);
        String result = ArchiveUtils.toString(archiveEntry);
        String expected = "-      1234 testfiles/test1.txt";
        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer1 = toAsciiBytes(expected);
        byte[] buffer2 = Arrays.copyOfRange(buffer1, 0, 3);
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer1));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer2));
    }

    @Test
    public void testMatchAsciiBufferFalse() {
        String expected = "test";
        byte[] buffer1 = toAsciiBytes(expected);
        byte[] buffer2 = Arrays.copyOfRange(buffer1, 0, 4);
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer2));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = { (byte) 'T', (byte) 'e', (byte) 's', (byte) 't' };
        byte[] buffer2 = { (byte) 'T', (byte) 'e', (byte) 's', (byte) 't' };
        byte[] buffer3 = { (byte) 'T', (byte) 'e', (byte) 's', (byte) 't', (byte) 'x' };
        byte[] buffer4 = { (byte) 'T', (byte) 'e', (byte) 's', (byte) 't', (byte) 'y' };

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer4, 0, buffer4.length));
    }

    @Test
    public void testIsEqualIgnoreTrailingNulls() {
        byte[] buffer1 = { (byte) 'T', (byte) 'e', (byte) 's', (byte) 't', (byte) 0, (byte) 0 };
        byte[] buffer2 = { (byte) 'T', (byte) 'e', (byte) 's', (byte) 't', (byte) 0, (byte) 0 };
        byte[] buffer3 = { (byte) 'T', (byte) 'e', (byte) 's', (byte) 't', (byte) 'x', (byte) 0 };
        byte[] buffer4 = { (byte) 'T', (byte) 'e', (byte) 's', (byte) 't', (byte) 'y', (byte) 0 };

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, true));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer4, 0, buffer4.length, true));
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer4, 0, buffer4.length, false));
    }

    @Test
    public void testToAsciiBytes() {
        String expected = "test";
        byte[] result = ArchiveUtils.toAsciiBytes(expected);

        assertEquals(expected.length(), result.length);
        for (int i = 0; i < expected.length(); i++) {
            assertEquals(expected.charAt(i), (char) result[i]);
        }
    }

    @Test
    public void testToAsciiString() {
        byte[] buffer = { (byte) 'T', (byte) 'e', (byte) 's', (byte) 't' };
        String expected = "test";
        String result = ArchiveUtils.toAsciiString(buffer);

        assertEquals(expected, result);
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] buffer = { (byte) 'T', (byte) 'e', (byte) 's', (byte) 't' };
        String expected = "test";
        String result = ArchiveUtils.toAsciiString(buffer, 0, 4);

        assertEquals(expected, result);
    }

    @Test
    public void testSanitize() {
        String input = "test\u201cstring\u201dwith\u2018special\u2019characters";
        String expected = "test?string?with?special?characters";
        String result = ArchiveUtils.sanitize(input);

        assertEquals(expected, result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] buffer = new byte[10];
        assertTrue(ArchiveUtils.isArrayZero(buffer, 0));
        assertTrue(ArchiveUtils.isArrayZero(buffer, 5));
        assertFalse(ArchiveUtils.isArrayZero(buffer, 7));
    }
}
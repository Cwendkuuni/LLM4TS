package org.apache.commons.compress.utils;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static org.apache.commons.compress.utils.ArchiveUtils.isArrayZero;
import static org.apache.commons.compress.utils.ArchiveUtils.matchAsciiBuffer;
import static org.apache.commons.compress.utils.ArchiveUtils.sanitize;
import static org.apache.commons.compress.utils.ArchiveUtils.toString;

/**
 * Test class for ArchiveUtils
 */
public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        String result = ArchiveUtils.toString(entry);
        String expected = "-       0 test.txt";
        assertEquals(expected, result);
    }

    @Test
    public void testToStringWithDirectory() {
        ArchiveEntry entry = new ArchiveEntry("test/");
        String result = ArchiveUtils.toString(entry);
        String expected = "d       0 test/";
        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        byte[] buffer = new byte[]{'M', 'P', 'E', 'G', 0x0A};
        String expected = "MEG\n";
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testMatchAsciiBufferWithOffset() {
        byte[] buffer = new byte[]{'M', 'P', 'E', 'G', 0x0A};
        String expected = "MEG\n";
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, 5));
    }

    @Test
    public void testMatchAsciiBufferFalse() {
        byte[] buffer = new byte[]{'M', 'P', 'E', 'G', 'A'};
        String expected = "MEG\n";
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testMatchAsciiBufferNull() {
        byte[] buffer = null;
        String expected = "MEG\n";
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, 0));
    }

    @Test
    public void testToStringWithLongEntries() {
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        entry.setSize(Long.MAX_VALUE);
        String result = ArchiveUtils.toString(entry);
        String expected = "d        9223372036854775807 test.txt";
        assertEquals(expected, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{'M', 'P', 'E', 'G', 0x0A};
        byte[] buffer2 = new byte[]{'M', 'P', 'E', 'G', 0x0A};
        byte[] buffer3 = new byte[]{'M', 'P', 'E', 'G', 'A'};
        byte[] buffer4 = new byte[]{'M', 'P', 'E', 'G', 0x0B};
        byte[] buffer5 = new byte[0];

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, 5, buffer1, 0, 5, false));
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, 5, buffer2, 0, 5, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 5, buffer3, 0, 5, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 5, buffer4, 0, 5, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 5, buffer5, 0, 0, false));

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, 5, buffer1, 0, 5, true));
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, 5, buffer2, 0, 5, true));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 5, buffer3, 0, 5, true));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 5, buffer4, 0, 5, true));
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, 5, buffer5, 0, 5, true));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[]{'M', 'P', 'E', 'G', 0x0A};
        byte[] buffer2 = new byte[]{'M', 'P', 'E', 'G', 0x0A};
        byte[] buffer3 = null;

        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, 5, buffer1, 0, 5));
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, 5, buffer2, 0, 5));
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, 5, buffer3, 0, 0));
    }

    @Test
    public void testIsEqualWithZeroArrays() {
        byte[] buffer1 = new byte[0];
        byte[] buffer2 = new byte[0];

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, 0, buffer2, 0, 0, false));
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, 0, buffer2, 0, 0, true));
    }

    @Test
    public void testIsArrayZero() {
        byte[] buffer = new byte[0];
        byte[] buffer2 = new byte[]{'M', 'P', 'E', 'G', 0x0A};

        assertTrue(ArchiveUtils.isArrayZero(buffer, 0));
        assertFalse(ArchiveUtils.isArrayZero(buffer2, 0));
    }

    @Test
    public void testSanitize() {
        String input = "test\\test\t\f\n\r\b\u0000\u007Ftest";
        String expected = "test\\test?	?\f?\n?\r?\b?\u0000?\u007Ftest";
        String result = ArchiveUtils.sanitize(input);
        assertEquals(expected, result);
    }

    @Test
    public void testSanitizeEmptyString() {
        String input = "";
        String expected = "";
        String result = ArchiveUtils.sanitize(input);
        assertEquals(expected, result);
    }
}
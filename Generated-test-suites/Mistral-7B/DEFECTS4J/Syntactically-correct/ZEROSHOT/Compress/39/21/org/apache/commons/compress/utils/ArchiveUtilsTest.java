package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.test.TestHelpers;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Test class for ArchiveUtils
 */
public class ArchiveUtilsTest {

    @Test
    public void testToString() throws IOException {
        ArchiveEntry testEntry = TestHelpers.createTestFileEntry("test.txt", false, 10, new ByteArrayInputStream(new byte[0]));
        String result = ArchiveUtils.toString(testEntry);
        String expected = "-        0 test.txt";
        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = "test".getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testMatchAsciiBufferFalse() {
        String expected = "test";
        byte[] buffer = new byte[5];
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testMatchAsciiBufferDifferentLength() {
        String expected = "test";
        byte[] buffer = "tester".getBytes();
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testToStringAsciiBytes() {
        String inputString = "test";
        byte[] inputBytes = inputString.getBytes(CharsetNames.US_ASCII);
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        assertArrayEquals(result, inputBytes);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = new byte[]{0x65, 0x6e, 0x63, 0x6f, 0x6d, 0x65};
        String expected = "example";
        String result = ArchiveUtils.toAsciiString(inputBytes);
        assertEquals(expected, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{'A', 'B', 'C'};
        byte[] buffer2 = new byte[]{'A', 'B', 'C'};
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsEqualFalse() {
        byte[] buffer1 = new byte[]{'A', 'B', 'C'};
        byte[] buffer2 = new byte[]{'A', 'B', 'D'};
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsEqualDifferentLength() {
        byte[] buffer1 = new byte[]{'A', 'B', 'C'};
        byte[] buffer2 = new byte[]{'A', 'B', 'C', 'D'};
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsEqualIgnoreTrailingNulls() {
        byte[] buffer1 = new byte[]{'A', 'B', 'C', 0, 0, 0};
        byte[] buffer2 = new byte[]{'A', 'B', 'C'};
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true));
    }

    @Test
    public void testIsEqualIgnoreTrailingNullsFalse() {
        byte[] buffer1 = new byte[]{'A', 'B', 'C', 0, 0, 0};
        byte[] buffer2 = new byte[]{'A', 'B', 'D'};
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[5];
        assertFalse(ArchiveUtils.isArrayZero(a, 5));
        byte[] zeroArray = new byte[5];
        Arrays.fill(zeroArray, (byte) 0);
        assertTrue(ArchiveUtils.isArrayZero(zeroArray, 5));
    }

    @Test
    public void testSanitize() {
        String s = "test\u0001string\u001Fwith\u001Bspecial\u0080characters.";
        String result = ArchiveUtils.sanitize(s);
        String expected = "test?string?with?special?characters.";
        assertEquals(expected, result);
    }
}
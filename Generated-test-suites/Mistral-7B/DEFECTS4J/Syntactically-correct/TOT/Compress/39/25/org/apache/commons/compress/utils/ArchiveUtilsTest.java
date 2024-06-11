package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarEntry;
import org.apache.commons.compress.archivers.tar.TarInputStream;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new TarEntry("testfile.txt");
        entry.setSize(100);
        entry.setDirectory(false);

        String expected = "-       100 testfile.txt";
        String result = ArchiveUtils.toString(entry);

        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "teststring";
        byte[] buffer = toAsciiBytes(expected);

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);

        assertTrue(result);
    }

    @Test
    public void testMatchAsciiBufferWithOffset() {
        String expected = "teststring";
        byte[] buffer = new byte[10];
        System.arraycopy(toAsciiBytes(expected), 0, buffer, 0, expected.length());

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, expected.length());

        assertTrue(result);
    }

    @Test
    public void testMatchAsciiBufferWithWrongLength() {
        String expected = "teststring";
        byte[] buffer = new byte[expected.length() + 1];
        System.arraycopy(toAsciiBytes(expected), 0, buffer, 0, expected.length());

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);

        assertFalse(result);
    }

    @Test
    public void testMatchAsciiBufferWithWrongBuffer() {
        String expected = "teststring";
        byte[] buffer = new byte[5];

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);

        assertFalse(result);
    }

    @Test
    public void testToStringWithNullEntry() {
        ArchiveEntry entry = null;
        String result = ArchiveUtils.toString(entry);

        assertNull(result);
    }

    @Test
    public void testMatchAsciiBufferWithNullString() {
        String expected = null;
        byte[] buffer = toAsciiBytes("teststring".getBytes());

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);

        assertFalse(result);
    }

    @Test
    public void testMatchAsciiBufferWithNullBuffer() {
        String expected = "teststring";
        byte[] buffer = null;

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);

        assertFalse(result);
    }

    @Test
    public void testToStringWithNullEntryName() {
        ArchiveEntry entry = new TarEntry(null);
        entry.setSize(100);
        entry.setDirectory(false);

        String result = ArchiveUtils.toString(entry);

        assertEquals("", result);
    }

    @Test
    public void testToStringWithLongEntryName() {
        String longName = "a".repeat(255);
        ArchiveEntry entry = new TarEntry(longName);
        entry.setSize(100);
        entry.setDirectory(false);

        String result = ArchiveUtils.toString(entry);

        assertEquals("-       100 " + longName.substring(0, 254) + "...", result);
    }

    @Test
    public void testIsEqualWithNullBuffer1() {
        byte[] buffer1 = null;
        byte[] buffer2 = new byte[5];

        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);

        assertFalse(result);
    }

    @Test
    public void testIsEqualWithNullBuffer2() {
        byte[] buffer1 = new byte[5];
        byte[] buffer2 = null;

        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);

        assertFalse(result);
    }

    @Test
    public void testIsEqualWithDifferentLengths() {
        byte[] buffer1 = new byte[5];
        byte[] buffer2 = new byte[10];

        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);

        assertFalse(result);
    }

    @Test
    public void testIsEqualWithDifferentContents() {
        byte[] buffer1 = new byte[] {1, 2, 3};
        byte[] buffer2 = new byte[] {1, 2, 4};

        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);

        assertFalse(result);
    }

    @Test
    public void testIsEqualWithSameContents() {
        byte[] buffer1 = new byte[] {1, 2, 3};
        byte[] buffer2 = new byte[] {1, 2, 3};

        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);

        assertTrue(result);
    }

    @Test
    public void testIsEqualWithSameContentsIgnoringTrailingNulls() {
        byte[] buffer1 = new byte[] {1, 2, 3, 0};
        byte[] buffer2 = new byte[] {1, 2, 3, 0, 0};

        boolean result = ArchiveUtils.isEqualWithNull(buffer1, buffer2);

        assertTrue(result);
    }

    @Test
    public void testIsEqualWithSameContentsIgnoringTrailingNulls2() {
        byte[] buffer1 = new byte[] {1, 2, 3, 0};
        byte[] buffer2 = new byte[] {1, 2, 3};

        boolean result = ArchiveUtils.isEqualWithNull(buffer1, buffer2);

        assertFalse(result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[10];

        assertTrue(ArchiveUtils.isArrayZero(a, 0));
        assertTrue(ArchiveUtils.isArrayZero(a, 1));
        assertTrue(ArchiveUtils.isArrayZero(a, 2));
        assertTrue(ArchiveUtils.isArrayZero(a, 3));
        assertTrue(ArchiveUtils.isArrayZero(a, 4));
        assertTrue(ArchiveUtils.isArrayZero(a, 5));
        assertTrue(ArchiveUtils.isArrayZero(a, 6));
        assertTrue(ArchiveUtils.isArrayZero(a, 7));
        assertTrue(ArchiveUtils.isArrayZero(a, 8));
        assertTrue(ArchiveUtils.isArrayZero(a, 9));

        assertFalse(ArchiveUtils.isArrayZero(a, 10));
    }

    @Test
    public void testIsEqualWithNullStream1() {
        byte[] buffer1 = new byte[10];
        ByteArrayInputStream inputStream1 = new ByteArrayInputStream(buffer1);

        byte[] buffer2 = null;

        boolean result = ArchiveUtils.isEqualWithNull(buffer1, buffer2, inputStream1, null);

        assertFalse(result);
    }

    @Test
    public void testIsEqualWithNullStream2() {
        byte[] buffer1 = new byte[10];
        ByteArrayInputStream inputStream1 = new ByteArrayInputStream(buffer1);

        byte[] buffer2 = new byte[10];
        ByteArrayInputStream inputStream2 = null;

        boolean result = ArchiveUtils.isEqualWithNull(buffer1, buffer2, inputStream1, inputStream2);

        assertFalse(result);
    }

    @Test
    public void testIsEqualWithSameStreams() throws IOException {
        byte[] buffer1 = new byte[] {1, 2, 3};
        byte[] buffer2 = new byte[] {1, 2, 3};

        ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();

        TarOutputStream tarOutputStream1 = new TarOutputStream(outputStream1);
        TarOutputStream tarOutputStream2 = new TarOutputStream(outputStream2);

        tarOutputStream1.putNextEntry(new TarEntry("testfile.txt"));
        tarOutputStream1.write(buffer1);
        tarOutputStream1.closeEntry();

        tarOutputStream2.putNextEntry(new TarEntry("testfile.txt"));
        tarOutputStream2.write(buffer2);
        tarOutputStream2.closeEntry();

        tarOutputStream1.finish();
        tarOutputStream2.finish();

        byte[] buffer1FromStream = ((ByteArrayInputStream) outputStream1.toInputStream()).readAllBytes();
        byte[] buffer2FromStream = ((ByteArrayInputStream) outputStream2.toInputStream()).readAllBytes();

        boolean result = ArchiveUtils.isEqualWithNull(buffer1, buffer2FromStream);

        assertTrue(result);
    }

    @Test
    public void testSanitize() {
        String input = "teststring with special characters: !\"#$%&'()*+,;<=>?@[\\]^_`{|}~";
        String expected = "teststring with special characters: !\"#$%&'()*+,;<=>?@[\\]^_`{|}~".replaceAll("([\\p{Cntrl}])", "?").substring(0, 255);

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
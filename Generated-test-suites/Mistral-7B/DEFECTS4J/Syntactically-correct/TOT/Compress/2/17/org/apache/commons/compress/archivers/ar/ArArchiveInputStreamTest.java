package org.apache.commons.compress.archivers.ar;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        String testData = "name\0lastmodified\001\0userid\0groupid\0755\0length\0100\000\000\0TRAILER\0";
        InputStream testInput = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        ArArchiveInputStream testArchiveInputStream = new ArArchiveInputStream(testInput);

        ArchiveEntry nextEntry = testArchiveInputStream.getNextEntry();
        ArArchiveEntry arEntry = (ArArchiveEntry) nextEntry;
        Assert.assertEquals("name", arEntry.getName());
        Assert.assertEquals(100L, arEntry.getSize());

        nextEntry = testArchiveInputStream.getNextEntry();
        Assert.assertNull(nextEntry);

        testArchiveInputStream.close();
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[8];
        signature[0] = (byte) 0x21;
        signature[1] = (byte) 0x3c;
        signature[2] = (byte) 0x61;
        signature[3] = (byte) 0x72;
        signature[4] = (byte) 0x63;
        signature[5] = (byte) 0x68;
        signature[6] = (byte) 0x3e;
        signature[7] = (byte) 0x0a;

        Assert.assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testGetInvalidEntry() throws IOException {
        String testData = "name\0lastmodified\001\0userid\0groupid\0755\0length\0100\001\0TRAILER\0"; // Invalid length
        InputStream testInput = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        ArArchiveInputStream testArchiveInputStream = new ArArchiveInputStream(testInput);
        ArchiveEntry nextEntry = testArchiveInputStream.getNextEntry();

        Assert.assertNull(nextEntry);

        testArchiveInputStream.close();
    }

    @Test
    public void testGetEntryWithZeroLength() throws IOException {
        String testData = "name\0lastmodified\001\0userid\0groupid\0755\0length\0\0TRAILER\0";
        InputStream testInput = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        ArArchiveInputStream testArchiveInputStream = new ArArchiveInputStream(testInput);

        ArchiveEntry nextEntry = testArchiveInputStream.getNextEntry();
        ArArchiveEntry arEntry = (ArArchiveEntry) nextEntry;
        Assert.assertEquals("name", arEntry.getName());
        Assert.assertEquals(0L, arEntry.getSize());

        nextEntry = testArchiveInputStream.getNextEntry();
        Assert.assertNull(nextEntry);

        testArchiveInputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        String testData = "name\0lastmodified\001\0userid\0groupid\0755\0length\0100\000\000\0TRAILER\0";
        InputStream testInput = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        ArArchiveInputStream testArchiveInputStream = new ArArchiveInputStream(testInput);
        ArchiveEntry nextEntry = testArchiveInputStream.getNextEntry();

        Assert.assertEquals("name", nextEntry.getName());
        testArchiveInputStream.close();
        nextEntry = testArchiveInputStream.getNextEntry();
        Assert.assertNull(nextEntry);
    }

    @Test
    public void testGetNextEntryTwice() throws IOException {
        String testData = "name\0lastmodified\001\0userid\0groupid\0755\0length\0100\000\000\0TRAILER\0";
        InputStream testInput = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        ArArchiveInputStream testArchiveInputStream = new ArArchiveInputStream(testInput);

        ArchiveEntry firstEntry = testArchiveInputStream.getNextEntry();
        ArArchiveEntry firstEntryExpected = new ArArchiveEntry("name", 100L);
        Assert.assertEquals(firstEntryExpected, firstEntry);

        ArchiveEntry secondEntry = testArchiveInputStream.getNextEntry();
        Assert.assertNull(secondEntry);

        testArchiveInputStream.close();
    }

    @Test
    public void testRead() throws IOException {
        String testData = "name\0lastmodified\001\0userid\0groupid\0755\0length\0100\000\000\0TRAILER\0";
        InputStream testInput = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        ArArchiveInputStream testArchiveInputStream = new ArArchiveInputStream(testInput);

        byte[] result = new byte[5];
        int read = testArchiveInputStream.read(result, 0, result.length);

        Assert.assertEquals(16, read);
        Assert.assertArrayEquals(ArArchiveEntry.HEADER.getBytes(), result);

        testArchiveInputStream.close();
    }
}
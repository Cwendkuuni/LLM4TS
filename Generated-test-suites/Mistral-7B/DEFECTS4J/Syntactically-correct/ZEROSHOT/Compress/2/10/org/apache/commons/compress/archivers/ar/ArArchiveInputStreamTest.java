package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for ArArchiveInputStream.
 */
public class ArArchiveInputStreamTest {

    private InputStream inputStream;
    private ArArchiveInputStream archiveInputStream;

    @Before
    public void setup() {
        byte[] header = ArArchiveInputStream.HEADER.getBytes();
        byte[] name = "test.txt".getBytes();
        byte[] lastModified = "1999-12-17 15:18:20".getBytes();
        byte[] userID = "user".getBytes();
        byte[] groupID = "group".getBytes();
        byte[] fileMode = new byte[]{0664, 0};
        byte[] length = "12345".getBytes();
        byte[] trailer = ArArchiveInputStream.TRAILER.getBytes();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(header);
        byteArrayOutputStream.write(name);
        byteArrayOutputStream.write(lastModified);
        byteArrayOutputStream.write(userID);
        byteArrayOutputStream.write(groupID);
        byteArrayOutputStream.write(fileMode);
        byteArrayOutputStream.write(length);
        byteArrayOutputStream.write(trailer);

        inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        archiveInputStream = new ArArchiveInputStream(inputStream);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry arEntry = archiveInputStream.getNextArEntry();
        ArchiveEntry expectedEntry = new ArArchiveEntry("test.txt", 12345);

        assertEquals(expectedEntry.getName(), arEntry.getName());
        assertEquals(expectedEntry.getSize(), arEntry.getSize());

        ArArchiveEntry nextEntry = archiveInputStream.getNextArEntry();
        assertNull(nextEntry);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryInvalidHeader() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(ArArchiveInputStream.HEADER.getBytes().subArray(0, 6));
        byteArrayOutputStream.write(ArArchiveInputStream.TRAILER.getBytes());

        InputStream invalidHeaderInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ArArchiveInputStream invalidHeaderArchiveInputStream = new ArArchiveInputStream(invalidHeaderInputStream);
        invalidHeaderArchiveInputStream.getNextArEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryNoEntry() throws IOException {
        archiveInputStream.getNextArEntry();
        archiveInputStream.getNextArEntry();

        archiveInputStream.close();

        archiveInputStream = new ArArchiveInputStream(inputStream);

        archiveInputStream.getNextArEntry();
        archiveInputStream.getNextArEntry();

        archiveInputStream.getNextArEntry();
    }

    @Test
    public void testMatches() {
        byte[] testSignature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};

        assertTrue(ArArchiveInputStream.matches(testSignature, 8));
    }

    @Test
    public void testMatchesInvalidLength() {
        byte[] testSignature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e};

        assertFalse(ArArchiveInputStream.matches(testSignature, 7));
    }
}
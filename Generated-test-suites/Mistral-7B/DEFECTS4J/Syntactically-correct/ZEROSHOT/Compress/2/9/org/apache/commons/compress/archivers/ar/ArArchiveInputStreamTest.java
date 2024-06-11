package org.apache.commons.compress.archivers.ar;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for {@link ArArchiveInputStream} class.
 */
public class ArArchiveInputStreamTest {

    private InputStream inputStream;
    private ArArchiveInputStream arArchiveInputStream;

    @Before
    public void setUp() throws IOException {
        byte[] header = ArArchiveEntry.HEADER.getBytes();
        byte[] name = "test.txt".getBytes();
        byte[] lastModified = "1970-01-01 00:00:01".getBytes();
        byte[] userId = "user".getBytes();
        byte[] groupId = "group".getBytes();
        byte[] fileMode = "rwxrwxrwx".getBytes();
        byte[] length = "1024".getBytes();
        byte[] trailer = ArArchiveEntry.TRAILER.getBytes();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(header);
        byteArrayOutputStream.write(name);
        byteArrayOutputStream.write(lastModified);
        byteArrayOutputStream.write(userId);
        byteArrayOutputStream.write(groupId);
        byteArrayOutputStream.write(fileMode);
        byteArrayOutputStream.write(length);
        byteArrayOutputStream.write(trailer);

        inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        arArchiveInputStream = new ArArchiveInputStream(inputStream);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry arArchiveEntry = arArchiveInputStream.getNextArEntry();
        assertEquals(new String(name), arArchiveEntry.getName());
        assertEquals(1024L, arArchiveEntry.getSize());

        ArArchiveEntry nextArArchiveEntry = arArchiveInputStream.getNextArEntry();
        assertNull(nextArArchiveEntry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry archiveEntry = arArchiveInputStream.getNextEntry();
        assertEquals(ArArchiveEntry.class, archiveEntry.getClass());
        ArArchiveEntry arArchiveEntry = (ArArchiveEntry) archiveEntry;
        assertEquals(new String(name), arArchiveEntry.getName());
        assertEquals(1024L, arArchiveEntry.getSize());

        ArchiveEntry nextEntry = arArchiveInputStream.getNextEntry();
        assertNull(nextEntry);
    }

    @Test(expected = IOException.class)
    public void testInvalidHeader() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("Invalid header".getBytes());
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.getNextArEntry();
    }

    @Test(expected = IOException.class)
    public void testInvalidTrailer() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(ArArchiveEntry.HEADER.getBytes());
        byteArrayOutputStream.write("Invalid trailer".getBytes());
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.getNextArEntry();
    }

    @Test(expected = IOException.class)
    public void testInvalidNameLength() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] name = new byte[17];
        Arrays.fill(name, (byte) 0);
        byteArrayOutputStream.write(ArArchiveEntry.HEADER.getBytes());
        byteArrayOutputStream.write(name);
        byteArrayOutputStream.write(lastModified);
        byteArrayOutputStream.write(userId);
        byteArrayOutputStream.write(groupId);
        byteArrayOutputStream.write(fileMode);
        byteArrayOutputStream.write(length);
        byteArrayOutputStream.write(ArArchiveEntry.TRAILER.getBytes());

        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.getNextArEntry();
    }

    @Test(expected = IOException.class)
    public void testInvalidLengthLength() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] name = "test.txt".getBytes();
        byte[] lastModified = "1970-01-01 00:00:01".getBytes();
        byte[] userId = "user".getBytes();
        byte[] groupId = "group".getBytes();
        byte[] fileMode = "rwxrwxrwx".getBytes();
        byte[] length = new byte[11];
        Arrays.fill(length, (byte) 0);
        byte[] trailer = ArArchiveEntry.TRAILER.getBytes();

        byteArrayOutputStream.write(ArArchiveEntry.HEADER.getBytes());
        byteArrayOutputStream.write(name);
        byteArrayOutputStream.write(lastModified);
        byteArrayOutputStream.write(userId);
        byteArrayOutputStream.write(groupId);
        byteArrayOutputStream.write(fileMode);
        byteArrayOutputStream.write(length);
        byteArrayOutputStream.write(trailer);

        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.getNextArEntry();
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[8];
        byte[] expectedSignature = ArArchiveEntry.HEADER.getBytes();
        System.arraycopy(expectedSignature, 0, signature, 0, expectedSignature.length);
        assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }
}
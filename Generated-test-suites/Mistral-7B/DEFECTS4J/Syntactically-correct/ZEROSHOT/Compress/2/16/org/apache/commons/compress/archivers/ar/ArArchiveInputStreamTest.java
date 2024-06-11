package org.apache.commons.compress.archivers.ar;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for ArArchiveInputStream
 */
public class ArArchiveInputStreamTest {

    private InputStream inputStream;
    private ArArchiveInputStream arArchiveInputStream;

    @Before
    public void setUp() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(ArArchiveEntry.HEADER);
        outputStream.write(new byte[]{"test", 0}); // name
        outputStream.write(new byte[]{"1234567890123", 0}); // lastModified
        outputStream.write(new byte[]{"user", 0}); // userid
        outputStream.write(new byte[]{"group", 0}); // groupid
        outputStream.write(new byte[]{(byte) 0644, 0}); // filemode
        outputStream.write(new byte[]{"10", 0}); // length
        outputStream.write(ArArchiveEntry.TRAILER);
        byte[] header = outputStream.toByteArray();

        ByteArrayInputStream inputStreamMock = new ByteArrayInputStream(header);
        this.inputStream = inputStreamMock;
        this.arArchiveInputStream = new ArArchiveInputStream(inputStream);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry nextEntry = arArchiveInputStream.getNextArEntry();
        assertEquals("test", nextEntry.getName());
        assertEquals(10L, nextEntry.getSize());

        nextEntry = arArchiveInputStream.getNextArEntry();
        assertNull(nextEntry);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryUnexpectedFormat() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[]{(byte) 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a});
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        arArchiveInputStream = new ArArchiveInputStream(inputStream);

        arArchiveInputStream.getNextArEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryUnexpectedEndOfStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(ArArchiveEntry.HEADER);
        outputStream.write(new byte[]{"test", 0}); // name
        outputStream.write(new byte[]{"1234567890123", 0}); // lastModified
        outputStream.write(new byte[]{"user", 0}); // userid
        outputStream.write(new byte[]{"group", 0}); // groupid
        outputStream.write(new byte[]{(byte) 0644, 0}); // filemode
        outputStream.write(new byte[]{"5", 0}); // length
        outputStream.write(ArArchiveEntry.TRAILER);
        byte[] header = outputStream.toByteArray();

        ByteArrayInputStream inputStreamMock = new ByteArrayInputStream(header);
        inputStream = inputStreamMock;
        arArchiveInputStream = new ArArchiveInputStream(inputStream);

        arArchiveInputStream.getNextArEntry();
        arArchiveInputStream.getNextArEntry(); // consume next entry

        inputStream = IOUtils.toInputStream(new byte[0]);
        arArchiveInputStream = new ArArchiveInputStream(inputStream);

        arArchiveInputStream.getNextArEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryAfterClosingStream() throws IOException {
        arArchiveInputStream.close();
        arArchiveInputStream.getNextArEntry();
    }
}
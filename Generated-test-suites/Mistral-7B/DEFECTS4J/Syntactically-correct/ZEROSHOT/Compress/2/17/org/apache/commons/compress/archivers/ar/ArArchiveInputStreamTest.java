package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link ArArchiveInputStream}.
 */
public class ArArchiveInputStreamTest {

    private InputStream inputStream;

    @Before
    public void setup() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(ArArchiveEntry.HEADER);
        outputStream.write("testName.txt\0\0\0\0\0\0\0\0\0\0\0\0ArArchiveEntry.TRAILER".getBytes());
        outputStream.write("testContent".getBytes());
        byte[] bytes = outputStream.toByteArray();
        inputStream = new ByteArrayInputStream(bytes);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        ArArchiveEntry arArchiveEntry = arArchiveInputStream.getNextArEntry();
        assertTrue(arArchiveEntry instanceof ArArchiveEntry);
        assertEquals("testName.txt", arArchiveEntry.getName());
        assertEquals(14, arArchiveEntry.getSize());

        ArArchiveEntry nextArEntry = arArchiveInputStream.getNextArEntry();
        assertNull(nextArEntry);

        arArchiveInputStream.close();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        ArchiveEntry archiveEntry = arArchiveInputStream.getNextEntry();
        assertTrue(archiveEntry instanceof ArArchiveEntry);
        assertEquals("testName.txt", archiveEntry.getName());
        assertEquals(14, archiveEntry.getSize());

        ArchiveEntry nextEntry = arArchiveInputStream.getNextEntry();
        assertNull(nextEntry);

        arArchiveInputStream.close();
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[] { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a };
        assertTrue(ArArchiveInputStream.matches(signature, signature.length));

        byte[] signatureInvalid = new byte[] { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0b };
        assertFalse(ArArchiveInputStream.matches(signatureInvalid, signatureInvalid.length));
    }

    private void assertEquals(byte[] expected, byte[] realized) {
        assertArrayEquals(expected, realized);
    }
}
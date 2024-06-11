package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for ArArchiveInputStream.
 */
public class ArArchiveInputStreamTest {

    private static final byte[] AR_HEADER = "!<arch>\0\0  type  d name   length modtime uid gid\0".getBytes();
    private static final byte[] AR_TRAILER = "|<eom>\0".getBytes();

    /**
     * Test method for {@link ArArchiveInputStream#ArArchiveInputStream(InputStream)} .
     */
    @Test
    public void testArArchiveInputStreamConstruction() {
        final InputStream input = new ByteArrayInputStream(AR_HEADER);
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        assertFalse(arArchiveInputStream.closed);
        assertEquals(input, arArchiveInputStream.input);
    }

    /**
     * Test method for {@link ArArchiveInputStream#getNextArEntry()} .
     */
    @Test
    public void testGetNextArEntry() throws IOException {
        final ByteArrayInputStream input = new ByteArrayInputStream(AR_HEADER);
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        final ArArchiveEntry actual = arArchiveInputStream.getNextArEntry();
        assertNotNull(actual);
        assertEquals("name", actual.getName());
        assertEquals(16L, actual.getSize());
        assertNull(arArchiveInputStream.getNextArEntry());

        // test eof
        input.mark(0);
        input.read();
        assertNull(arArchiveInputStream.getNextArEntry());
    }

    /**
     * Test method for {@link ArArchiveInputStream#getNextEntry()} .
     */
    @Test
    public void testGetNextEntry() throws IOException {
        testGetNextArEntry();
    }

    /**
     * Test method for {@link ArArchiveInputStream#close()} .
     */
    @Test
    public void testClose() throws IOException {
        final ByteArrayInputStream input = new ByteArrayInputStream(AR_HEADER);
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        arArchiveInputStream.close();
        assertTrue(arArchiveInputStream.closed);
    }

    /**
     * Test method for {@link ArArchiveInputStream#read()} .
     */
    @Test
    public void testRead() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(new ByteArrayInputStream(AR_HEADER));
        final byte[] expected = "!<arch>\0\0  type  d name   length modtime uid gid\0|<eom>\0".getBytes();
        final int read;

        read = arArchiveInputStream.read();
        assertEquals(AR_HEADER[0], (byte) read);
        output.write(read);

        read = arArchiveInputStream.read();
        assertEquals(AR_HEADER[1], (byte) read);
        output.write(read);

        read = arArchiveInputStream.read(new byte[1]);
        assertEquals(AR_HEADER[2], (byte) read);
        output.write(read);

        read = arArchiveInputStream.read(new byte[16]);
        assertEquals(AR_HEADER.length - 3, read);
        output.write(read);

        assertEquals(AR_TRAILER.length, arArchiveInputStream.read(new byte[AR_TRAILER.length]));
        output.write(AR_TRAILER);

        final byte[] actual = output.toByteArray();
        assertArrayEquals(expected, actual);
    }

    /**
     * Test method for {@link ArArchiveInputStream#matches(byte[], int)} .
     */
    @Test
    public void testMatches() {
        assertTrue(ArArchiveInputStream.matches(AR_HEADER, AR_HEADER.length));
        assertFalse(ArArchiveInputStream.matches(new byte[0], 0));
        assertFalse(ArArchiveInputStream.matches(new byte[]{"!"[0], 'a'}, 2));
    }
}
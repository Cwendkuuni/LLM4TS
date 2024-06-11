package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test cases for ArArchiveInputStream class.
 */
public class ArArchiveInputStreamTest {

    /**
     * Tests getNextArEntry method with valid archive input stream.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testGetNextArEntry() throws IOException {
        final String archiveContent = "AR\n" +
                "0001 02 01 01 02 01 01 02 01 01 02 01 01 02 01 01 02 01 01 02 0a3e\n" +
                "test1.txt\n" +
                "0002 02 01 01 02 01 01 02 01 01 02 01 01 02 01 01 02 01 01 02 0a3e\n" +
                "test2.txt\n" +
                "0003 02 01 01 02 01 01 02 01 01 02 01 01 02 01 01 02 01 01 02 0a3e\n" +
                "test3.txt\n" +
                "9999 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0a3e";

        final ByteArrayInputStream input = new ByteArrayInputStream(archiveContent.getBytes());
        final ArArchiveInputStream archiveInput = new ArArchiveInputStream(input);

        ArchiveEntry entry = null;
        ArchiveEntry expectedEntry;

        // Test reading archive entries
        for (int i = 1; i <= 3; i++) {
            entry = archiveInput.getNextArEntry();
            expectedEntry = new ArArchiveEntry("test" + i + ".txt", 1);
            assertTrue(entry instanceof ArArchiveEntry);
            assertTrue(expectedEntry.getName().equals(entry.getName()));
            assertTrue(expectedEntry.getSize() == entry.getSize());
        }

        // Test end of archive
        entry = archiveInput.getNextArEntry();
        assertNull(entry);

        // Test closing and reopening the archive input stream
        archiveInput.close();
        archiveInput.close();
        archiveInput = new ArArchiveInputStream(input);
        entry = archiveInput.getNextArEntry();
        expectedEntry = new ArArchiveEntry("test1.txt", 1);
        assertTrue(entry instanceof ArArchiveEntry);
        assertTrue(expectedEntry.getName().equals(entry.getName()));
        assertTrue(expectedEntry.getSize() == entry.getSize());
    }

    /**
     * Tests getNextArEntry method with invalid archive input stream.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test(expected = IOException.class)
    public void testGetNextArEntryWithInvalidInput() throws IOException {
        final ByteArrayInputStream input = new ByteArrayInputStream("test".getBytes());
        final ArArchiveInputStream archiveInput = new ArArchiveInputStream(input);
        archiveInput.getNextArEntry();
    }

    /**
     * Tests matches method.
     */
    @Test
    public void testMatches() {
        final byte[] signature = new byte[]{0x3c, 0x21, 0x72, 0x61, 0x68, 0x63, 0x0a, 0x3e};
        assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }

    /**
     * Tests read method.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testRead() throws IOException {
        final String inputContent = "test";
        final InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes());
        final ArArchiveInputStream archiveInput = new ArArchiveInputStream(inputStream);

        final byte[] buffer = new byte[1];
        int read = 0;

        // Test reading one byte
        read = archiveInput.read(buffer);
        assertTrue(read == 1);
        assertArrayEquals(new byte[]{inputContent.charAt(0)}, buffer);

        // Test end of archive
        read = archiveInput.read(buffer);
        assertTrue(read == -1);

        // Test closing the archive input stream
        archiveInput.close();
        read = archiveInput.read(buffer);
        assertTrue(read == -1);
    }

    /**
     * Tests read method with specified offset and length.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        final String inputContent = "test12345";
        final InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes());
        final ArArchiveInputStream archiveInput = new ArArchiveInputStream(inputStream);

        final byte[] buffer = new byte[5];
        int read = 0;

        // Test reading 5 bytes starting at offset 1
        read = archiveInput.read(buffer, 1, 5);
        assertTrue(read == 5);
        assertArrayEquals(new byte[]{"2", "3", "4", "5", "5"}, buffer);

        // Test end of archive
        read = archiveInput.read(buffer, 0, 5);
        assertTrue(read == -1);

        // Test closing the archive input stream
        archiveInput.close();
        read = archiveInput.read(buffer, 0, 5);
        assertTrue(read == -1);
    }

    /**
     * Tests close method.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testClose() throws IOException {
        final String inputContent = "test";
        final InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes());
        final ArArchiveInputStream archiveInput = new ArArchiveInputStream(inputStream);

        archiveInput.close();

        final int read = archiveInput.read();
        assertTrue(read == -1);
    }
}
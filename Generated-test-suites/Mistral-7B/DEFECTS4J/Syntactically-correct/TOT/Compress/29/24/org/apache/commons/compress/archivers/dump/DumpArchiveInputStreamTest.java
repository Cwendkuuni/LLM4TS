package org.apache.commons.compress.archivers.dump;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

/**
 * Tests for the {@link DumpArchiveInputStream} class.
 */
public class DumpArchiveInputStreamTest {

    /**
     * Test that an empty stream returns null for {@link #getNextEntry()}.
     *
     * @throws IOException if an error occurs while reading the input stream.
     */
    @Test
    public void testEmptyStreamReturnsNull() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        DumpArchiveInputStream stream = new DumpArchiveInputStream(input);
        assertNull(stream.getNextEntry());
    }

    /**
     * Test that a stream with a single entry returns the expected entry.
     *
     * @throws IOException if an error occurs while reading the input stream.
     */
    @Test
    public void testSingleEntry() throws IOException {
        String entryName = "test.txt";
        long entrySize = 13;
        long entryOffset = 0;

        byte[] headerBytes = DumpArchiveEntry.createHeader(
                DumpArchiveConstants.SEGMENT_TYPE.REGULAR,
                entryName,
                entrySize,
                entryOffset);

        InputStream input = new ByteArrayInputStream(
                new byte[] { 'D', 'u', 'm', 'p' }
                // NFS header
                );
        input = new ByteArrayInputStream(
                new byte[] { DumpArchiveConstants.NFS_MAGIC, 0, 0, 0 }
                // NFS magic
                );
        input = new ByteArrayInputStream(headerBytes);
        input = new ByteArrayInputStream(new byte[1]); // EOF

        DumpArchiveInputStream stream = new DumpArchiveInputStream(input);
        ArchiveEntry entry = stream.getNextEntry();

        assertEquals(DumpArchiveConstants.SEGMENT_TYPE.REGULAR, entry.getType());
        assertEquals(entryName, entry.getName());
        assertEquals(entrySize, entry.getSize());
        assertEquals(entryOffset, entry.getFileOffset());
    }

    /**
     * Test that a stream with a compressed entry can be read.
     *
     * @throws IOException if an error occurs while reading the input stream.
     */
    @Test
    public void testCompressedEntry() throws IOException {
        String entryName = "test.gz";
        long entrySize = 13;
        long entryOffset = 0;

        byte[] headerBytes = DumpArchiveEntry.createHeader(
                DumpArchiveConstants.SEGMENT_TYPE.REGULAR,
                entryName,
                entrySize,
                entryOffset);

        byte[] compressedData = new byte[] { 0x1f, 0x8b, 0x08, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x02, 0x40, 0x1a, 0x58, 0x9e, 0x21, 0x0b, 0x08, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x8b, 0x05, 0x93, 0x01, 0x00, 0x00
        };

        InputStream input = new ByteArrayInputStream(
                new byte[] { 'D', 'u', 'm', 'p' }
                // NFS header
                );
        input = new ByteArrayInputStream(new byte[1]); // CLRI segment marker
        input = new ByteArrayInputStream(headerBytes);
        input = new GzipCompressorInputStream(
                new ByteArrayInputStream(compressedData));
        input = new ByteArrayInputStream(new byte[1]); // EOF

        DumpArchiveInputStream stream = new DumpArchiveInputStream(input);
        ArchiveEntry entry = stream.getNextEntry();

        assertEquals(DumpArchiveConstants.SEGMENT_TYPE.REGULAR, entry.getType());
        assertEquals(entryName, entry.getName());
        assertEquals(entrySize, entry.getSize());
        assertEquals(entryOffset, entry.getFileOffset());

        InputStream inputStream = stream;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, outputStream, 1024);
        byte[] expectedData = outputStream.toByteArray();
        byte[] actualData = new byte[expectedData.length];
        IOUtils.copy(inputStream, new ByteArrayInputStream(actualData), actualData.length);
        assertArrayEquals(expectedData, actualData);
    }

    /**
     * Test that a stream with a sparse entry can be read.
     *
     * @throws IOException if an error occurs while reading the input stream.
     */
    @Test
    public void testSparseEntry() throws IOException {
        String entryName = "test.sparse";
        long entrySize = 13;
        long entryOffset = 0;

        byte[] headerBytes = DumpArchiveEntry.createHeader(
                DumpArchiveConstants.SEGMENT_TYPE.SPARSE,
                entryName,
                entrySize,
                entryOffset);

        InputStream input = new ByteArrayInputStream(headerBytes);
        input = new ByteArrayInputStream(new byte[1]); // EOF

        DumpArchiveInputStream stream = new DumpArchiveInputStream(input);
        ArchiveEntry entry = stream.getNextEntry();

        assertEquals(DumpArchiveConstants.SEGMENT_TYPE.SPARSE, entry.getType());
        assertEquals(entryName, entry.getName());
        assertEquals(entrySize, entry.getSize());
        assertEquals(entryOffset, entry.getFileOffset());

        // Sparse entries are not read as data, so we just need to check that
        // reading the entry returns the expected file size and offset.
        InputStream inputStream = stream;
        byte[] buffer = new byte[1024];
        long fileSize = 0;
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            fileSize += bytesRead;
        }

        assertEquals(entrySize, fileSize);
        assertEquals(entryOffset, stream.getFilePointer());
    }

    /**
     * Test that a stream with a directory entry can be read.
     *
     * @throws IOException if an error occurs while reading the input stream.
     */
    @Test
    public void testDirectoryEntry() throws IOException {
        String entryName = "testdir";
        long entrySize = 4096;
        long entryOffset = 0;

        byte[] headerBytes1 = DumpArchiveEntry.createHeader(
                DumpArchiveConstants.SEGMENT_TYPE.INODE,
                entryName,
                0,
                entryOffset);
        byte[] headerBytes2 = DumpArchiveEntry.createHeader(
                DumpArchiveConstants.SEGMENT_TYPE.DIR,
                "",
                0,
                entryOffset + DumpArchiveConstants.TP_SIZE);

        InputStream input = new ByteArrayInputStream(headerBytes1);
        input = new ByteArrayInputStream(headerBytes2);
        input = new ByteArrayInputStream(new byte[1]); // EOF

        DumpArchiveInputStream stream = new DumpArchiveInputStream(input);
        ArchiveEntry entry = stream.getNextEntry();

        assertEquals(DumpArchiveConstants.SEGMENT_TYPE.DIR, entry.getType());
        assertFalse(entry.isDirectory());
        assertEquals(entryName, entry.getName());
        assertEquals(entrySize, entry.getSize());
        assertEquals(entryOffset, entry.getFileOffset());

        entry = stream.getNextEntry();
        assertTrue(entry.isDirectory());
        assertEquals("", entry.getName());
        assertEquals(0, entry.getSize());
        assertEquals(entryOffset + DumpArchiveConstants.TP_SIZE, entry.getFileOffset());

        InputStream inputStream = stream;
        ArchiveInputStream archiveStream = new DumpArchiveInputStream(inputStream);
        ArchiveEntry directoryEntry = archiveStream.getNextEntry();
        assertEquals(entryName, directoryEntry.getName());
        assertEquals(DumpArchiveConstants.TYPE_DIRECTORY, directoryEntry.getType());

        ArchiveEntry fileEntry1 = archiveStream.getNextEntry();
        assertEquals("", fileEntry1.getName());
        assertEquals(DumpArchiveConstants.TYPE_file, fileEntry1.getType());
        assertNull(fileEntry1.getName());

        ArchiveEntry fileEntry2 = archiveStream.getNextEntry();
        assertEquals("", fileEntry2.getName());
        assertEquals(DumpArchiveConstants.TYPE_file, fileEntry2.getType());
        assertNull(fileEntry2.getName());

        assertFalse(archiveStream.hasNextEntry());
    }

    /**
     * Test that a stream with a missing directory entry can be read.
     *
     * @throws IOException if an error occurs while reading the input stream.
     */
    @Test
    public void testMissingDirectoryEntry() throws IOException {
        String entryName = "testdir";
        long entrySize = 4096;
        long entryOffset = 0;

        byte[] headerBytes1 = DumpArchiveEntry.createHeader(
                DumpArchiveConstants.SEGMENT_TYPE.INODE,
                entryName,
                0,
                entryOffset);

        InputStream input = new ByteArrayInputStream(headerBytes1);
        input = new ByteArrayInputStream(new byte[1]); // EOF

        DumpArchiveInputStream stream = new DumpArchiveInputStream(input);
        ArchiveEntry entry = stream.getNextEntry();

        assertEquals(DumpArchiveConstants.SEGMENT_TYPE.INODE, entry.getType());
        assertFalse(entry.isDirectory());
        assertEquals(entryName, entry.getName());
        assertEquals(entrySize, entry.getSize());
        assertEquals(entryOffset, entry.getFileOffset());

        ArchiveEntry directoryEntry = stream.getNextEntry();
        assertNull(directoryEntry);
    }

    /**
     * Test that a stream with a missing entry can be read.
     *
     * @throws IOException if an error occurs while reading the input stream.
     */
    @Test
    public void testMissingEntry() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        DumpArchiveInputStream stream = new DumpArchiveInputStream(input);
        assertNull(stream.getNextEntry());
    }

    /**
     * Test that a stream with a non-matching archive format
     * cannot be read.
     *
     * @throws IOException if an error occurs while reading the input stream.
     */
    @Test(expected = InvalidFormatException.class)
    public void testNonMatchingArchiveFormat() throws IOException {
        byte[] headerBytes = new byte[] { 'D', 'u', 'm', 'p' };
        InputStream input = new ByteArrayInputStream(headerBytes);

        new DumpArchiveInputStream(input);
    }
}
package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.test.ArchiveTestHelper;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test case for ArArchiveInputStream.
 */
public class ArArchiveInputStreamTest {

    private static final ArchiveStreamFactory FACTORY = new ArchiveStreamFactory();

    @Test
    public void testEmptyInput() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        final ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);

        assertNull(archiveInputStream.getNextArEntry());
        archiveInputStream.close();
    }

    @Test
    public void testInvalidHeader() throws IOException {
        final byte[] invalidHeader = new byte[16];
        // Set the first byte to an invalid magic number.
        invalidHeader[0] = 0x01;

        final ByteArrayInputStream input = new ByteArrayInputStream(invalidHeader);
        final ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);

        archiveInputStream.close();
    }

    @Test
    public void testValidArHeader() throws IOException {
        // Ar header TEST_CASE.ar
        final byte[] arHeader = ArArchiveEntry.HEADER.getBytes();

        final ByteArrayInputStream input = new ByteArrayInputStream(arHeader);
        final ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);

        final ArArchiveEntry arEntry = archiveInputStream.getNextArEntry();
        assertTrue(arEntry != null);

        archiveInputStream.close();
    }

    @Test
    public void testReadEntryContent() throws IOException {
        final String testFile = "test_file.txt";
        final String testContent = "Hello, World!";
        final byte[] testContentBytes = testContent.getBytes();

        // Ar header TEST_CASE.ar
        final byte[] arHeader = ArArchiveEntry.HEADER.getBytes();

        // Ar entry TEST_CASE.ar: test_file.txt
        final byte[] nameBytes = testFile.getBytes();
        final long size = testContentBytes.length;
        final byte[] lengthBytes = ArchiveTestHelper.getLongBytes(size);
        final byte[] entryContent = new byte[nameBytes.length + lengthBytes.length + testContentBytes.length];

        System.arraycopy(ArArchiveEntry.HEADER.getBytes(), 0, entryContent, 0, ArArchiveEntry.HEADER.length);
        System.arraycopy(nameBytes, 0, entryContent, ArArchiveEntry.HEADER.length, nameBytes.length);
        System.arraycopy(lengthBytes, 0, entryContent, ArArchiveEntry.HEADER.length + nameBytes.length, lengthBytes.length);
        System.arraycopy(testContentBytes, 0, entryContent, ArArchiveEntry.HEADER.length + nameBytes.length + lengthBytes.length, testContentBytes.length);

        final byte[] arEntryBytes = new byte[ArArchiveEntry.TRAILER.length + entryContent.length];
        System.arraycopy(ArArchiveEntry.TRAILER.getBytes(), 0, arEntryBytes, 0, ArArchiveEntry.TRAILER.length);
        System.arraycopy(entryContent, 0, arEntryBytes, ArArchiveEntry.TRAILER.length, entryContent.length);

        final byte[] archiveContent = new byte[arHeader.length + arEntryBytes.length];
        System.arraycopy(arHeader, 0, archiveContent, 0, arHeader.length);
        System.arraycopy(arEntryBytes, 0, archiveContent, arHeader.length, arEntryBytes.length);

        final ByteArrayInputStream input = new ByteArrayInputStream(archiveContent);
        final ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);

        final ArArchiveEntry arEntry = archiveInputStream.getNextArEntry();
        assertTrue(arEntry != null);
        assertEquals(testFile, arEntry.getName());
        assertEquals(size, arEntry.getSize());

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = archiveInputStream.read(buffer, 0, buffer.length)) > 0) {
            output.write(buffer, 0, len);
        }
        assertArrayEquals(testContentBytes, output.toByteArray());

        archiveInputStream.close();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        final String testFile1 = "test_file_1.txt";
        final String testFile2 = "test_file_2.txt";

        // Ar header TEST_CASE.ar
        final byte[] arHeader = new byte[ArArchiveEntry.HEADER.length * 2];

        // Ar entry TEST_CASE.ar: test_file_1.txt
        final byte[] nameBytes1 = testFile1.getBytes();
        final long size1 = 17;
        final byte[] lengthBytes1 = ArchiveTestHelper.getLongBytes(size1);
        final byte[] entryContent1 = new byte[nameBytes1.length + lengthBytes1.length + new byte[5][0].length];
        System.arraycopy(ArArchiveEntry.HEADER.getBytes(), 0, entryContent1, 0, ArArchiveEntry.HEADER.length);
        System.arraycopy(nameBytes1, 0, entryContent1, ArArchiveEntry.HEADER.length, nameBytes1.length);
        System.arraycopy(lengthBytes1, 0, entryContent1, ArArchiveEntry.HEADER.length + nameBytes1.length, lengthBytes1.length);
        System.arraycopy(ArArchiveEntry.TRAILER.getBytes(), 0, entryContent1, ArArchiveEntry.HEADER.length + nameBytes1.length + lengthBytes1.length, ArArchiveEntry.TRAILER.length);

        // Ar entry TEST_CASE.ar: test_file_2.txt
        final byte[] nameBytes2 = testFile2.getBytes();
        final long size2 = 23;
        final byte[] lengthBytes2 = ArchiveTestHelper.getLongBytes(size2);
        final byte[] entryContent2 = new byte[nameBytes2.length + lengthBytes2.length + new byte[5][0].length];
        System.arraycopy(ArArchiveEntry.HEADER.getBytes(), 0, entryContent2, 0, ArArchiveEntry.HEADER.length);
        System.arraycopy(nameBytes2, 0, entryContent2, ArArchiveEntry.HEADER.length, nameBytes2.length);
        System.arraycopy(lengthBytes2, 0, entryContent2, ArArchiveEntry.HEADER.length + nameBytes2.length, lengthBytes2.length);
        System.arraycopy(ArArchiveEntry.TRAILER.getBytes(), 0, entryContent2, ArArchiveEntry.HEADER.length + nameBytes2.length + lengthBytes2.length, ArArchiveEntry.TRAILER.length);

        final byte[] arEntryBytes1 = new byte[entryContent1.length];
        System.arraycopy(entryContent1, 0, arEntryBytes1, 0, entryContent1.length);

        final byte[] arEntryBytes2 = new byte[entryContent2.length];
        System.arraycopy(entryContent2, 0, arEntryBytes2, 0, entryContent2.length);

        final byte[] archiveContent = new byte[arHeader.length + arEntryBytes1.length + arEntryBytes2.length];
        System.arraycopy(arHeader, 0, archiveContent, 0, arHeader.length);
        System.arraycopy(arEntryBytes1, 0, archiveContent, arHeader.length, arEntryBytes1.length);
        System.arraycopy(arEntryBytes2, 0, archiveContent, arHeader.length + arEntryBytes1.length, arEntryBytes2.length);

        final ByteArrayInputStream input = new ByteArrayInputStream(archiveContent);
        final ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);

        final ArArchiveEntry arEntry1 = archiveInputStream.getNextArEntry();
        assertTrue(arEntry1 != null);
        assertEquals(testFile1, arEntry1.getName());
        assertEquals(size1, arEntry1.getSize());

        final ArArchiveEntry arEntry2 = archiveInputStream.getNextArEntry();
        assertTrue(arEntry2 != null);
        assertEquals(testFile2, arEntry2.getName());
        assertEquals(size2, arEntry2.getSize());

        archiveInputStream.close();
    }

}
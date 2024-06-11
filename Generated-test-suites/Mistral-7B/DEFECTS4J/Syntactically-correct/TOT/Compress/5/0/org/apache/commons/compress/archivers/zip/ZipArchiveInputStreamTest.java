package org.apache.commons.compress.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ZipArchiveEntry;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.ZipLong;

public class ZipArchiveInputStreamTest {

    /**
     * Test reading a valid zip archive.
     *
     * @throws IOException if the test fails
     */
    @org.junit.Test
    public void testValidZipArchive() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(os);

        byte[] testData = "Test data".getBytes();

        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        zipOut.putNextEntry(entry);
        zipOut.write(testData, 0, testData.length);
        zipOut.closeEntry();
        zipOut.finish();

        InputStream is = new ByteArrayInputStream(os.toByteArray());
        ZipArchiveInputStream zis = new ZipArchiveInputStream(is, ZipEncodingHelper.UTF8, true);
        ZipArchiveEntry entryRead = zis.getNextZipEntry();
        assertNotNull(entryRead);
        assertEquals("test.txt", entryRead.getName());
        assertArrayEquals(testData, entryRead.getInputStream().readAllBytes());
        zis.closeEntry();
        zis.close();
    }

    /**
     * Test reading a zip archive with a corrupted header.
     *
     * @throws IOException if the test fails
     */
    @org.junit.Test
    public void testCorruptedHeader() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(0x04);
        os.write(0x03);
        os.write(0x4b);
        os.write(0x50);
        os.write("test.txt".getBytes());
        os.flush();
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        ZipArchiveInputStream zis = new ZipArchiveInputStream(is, ZipEncodingHelper.UTF8, true);
        assertNull(zis.getNextZipEntry());
        zis.close();
    }

    /**
     * Test reading a zip archive with an empty entry.
     *
     * @throws IOException if the test fails
     */
    @org.junit.Test
    public void testEmptyEntry() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(os);

        ZipArchiveEntry entry = new ZipArchiveEntry("empty.txt");
        zipOut.putNextEntry(entry);
        zipOut.closeEntry();
        zipOut.finish();

        InputStream is = new ByteArrayInputStream(os.toByteArray());
        ZipArchiveInputStream zis = new ZipArchiveInputStream(is, ZipEncodingHelper.UTF8, true);
        ZipArchiveEntry entryRead = zis.getNextZipEntry();
        assertNotNull(entryRead);
        assertEquals("empty.txt", entryRead.getName());
        assertNotNull(entryRead.getInputStream());
        assertEquals(0, entryRead.getSize());
        zis.closeEntry();
        zis.close();
    }

    /**
     * Test reading a zip archive with a data descriptor.
     *
     * @throws IOException if the test fails
     */
    @org.junit.Test
    public void testDataDescriptor() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(os);

        byte[] testData = "Test data".getBytes();

        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        zipOut.putNextEntry(entry);
        zipOut.setComment("Test comment");
        zipOut.write(testData, 0, testData.length);
        zipOut.closeEntry();
        zipOut.finish();

        InputStream is = new ByteArrayInputStream(os.toByteArray());
        ZipArchiveInputStream zis = new ZipArchiveInputStream(is, ZipEncodingHelper.UTF8, true);
        ZipArchiveEntry entryRead = zis.getNextZipEntry();
        assertNotNull(entryRead);
        assertEquals("test.txt", entryRead.getName());
        assertEquals("Test comment", entryRead.getComment());
        assertArrayEquals(testData, entryRead.getInputStream().readAllBytes());
        zis.closeEntry();
        zis.close();
    }

    /**
     * Test reading a zip archive with a self-extracting zip file.
     *
     * @throws IOException if the test fails
     */
    @org.junit.Test
    public void testSelfExtractingZip() throws IOException {
        ByteArrayOutputStream archiveOs = new ByteArrayOutputStream();
        ZipArchiveOutputStream archiveOut = new ZipArchiveOutputStream(archiveOs);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(os);

        byte[] testData = "Test data".getBytes();

        ZipArchiveEntry archiveEntry = new ZipArchiveEntry("archive.zip");
        archiveEntry.setMethod(ZipArchiveOutputStream.DEFLATED);
        archiveOut.putNextEntry(archiveEntry);
        archiveOut.writeTo(archiveEntry.getData());

        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        zipOut.putNextEntry(entry);
        zipOut.write(testData, 0, testData.length);
        zipOut.closeEntry();
        zipOut.finish();

        archiveOut.putNextEntry(zipOut.getNextSfEntry());
        archiveOut.closeEntry();
        archiveOut.finish();

        InputStream is = new ByteArrayInputStream(archiveOs.toByteArray());
        ZipArchiveInputStream zis = new ZipArchiveInputStream(is, ZipEncodingHelper.UTF8, true);
        ZipArchiveEntry archiveEntryRead = zis.getNextZipEntry();
        assertNotNull(archiveEntryRead);
        assertEquals("archive.zip", archiveEntryRead.getName());
        assertTrue(archiveEntryRead.isDirectory());

        ZipArchiveEntry entryRead = zis.getNextZipEntry();
        assertNotNull(entryRead);
        assertEquals("test.txt", entryRead.getName());
        assertArrayEquals(testData, entryRead.getInputStream().readAllBytes());
        zis.closeEntry();
        zis.close();
    }
}
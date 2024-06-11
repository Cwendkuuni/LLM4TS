package org.apache.commons.compress.archivers.dump;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.test.common.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Test class for {@link DumpArchiveInputStream}.
 */
public class DumpArchiveInputStreamTest {

    private static final ZipEncoding ZIP_ENCODING = ZipEncodingHelper.getZipEncoding(null);

    @Test
    public void testEmptyArchive() throws IOException, ArchiveException {
        byte[] headerBytes = DumpArchiveUtil.createEmptyHeader();
        InputStream inputStream = new ByteArrayInputStream(headerBytes);
        DumpArchiveInputStream sut = new DumpArchiveInputStream(inputStream, null);
        DumpArchiveEntry nextEntry = sut.getNextEntry();
        Assert.assertNull(nextEntry);
        sut.close();
    }

    @Test
    public void testSimpleArchive() throws IOException, ArchiveException {
        byte[] headerBytes = DumpArchiveUtil.createTestHeader();
        InputStream inputStream = new ByteArrayInputStream(headerBytes);
        DumpArchiveInputStream sut = new DumpArchiveInputStream(inputStream, ZIP_ENCODING);
        DumpArchiveEntry nextEntry = null;

        // Test getting next entry
        nextEntry = sut.getNextEntry();
        Assert.assertNotNull(nextEntry);
        Assert.assertEquals("test/testfile1.txt", nextEntry.getName());
        Assert.assertFalse(nextEntry.isDirectory());
        Assert.assertEquals(10, nextEntry.getSize());
        Assert.assertArrayEquals(new byte[10], sut.read(new byte[10], 0, 10));
        nextEntry = sut.getNextEntry();
        Assert.assertNull(nextEntry);

        // Test closing stream
        sut.close();
    }

    @Test(expected = ArchiveException.class)
    public void testInvalidFormatException() throws IOException {
        byte[] headerBytes = DumpArchiveUtil.createInvalidFormatHeader();
        InputStream inputStream = new ByteArrayInputStream(headerBytes);
        DumpArchiveInputStream sut = new DumpArchiveInputStream(inputStream, null);
        DumpArchiveEntry nextEntry = sut.getNextEntry();
    }

    @Test(expected = IOException.class)
    public void testEOFException() throws IOException, ArchiveException {
        byte[] headerBytes = DumpArchiveUtil.createTestHeader();
        InputStream inputStream = new ByteArrayInputStream(headerBytes);
        DumpArchiveInputStream sut = new DumpArchiveInputStream(inputStream, ZIP_ENCODING);
        sut.read(new byte[1], 0, 1);
        sut.getNextEntry();
        sut.getNextEntry();
        sut.getNextEntry();
        sut.getNextEntry();
        sut.getNextEntry();
        sut.getNextEntry();
        sut.getNextEntry();
        byte[] buffer = new byte[1];
        sut.read(buffer, 0, 1);
    }

    @Test
    public void testGetSummary() throws IOException, ArchiveException {
        byte[] headerBytes = DumpArchiveUtil.createTestHeader();
        InputStream inputStream = new ByteArrayInputStream(headerBytes);
        DumpArchiveInputStream sut = new DumpArchiveInputStream(inputStream, ZIP_ENCODING);
        DumpArchiveSummary summary = sut.getSummary();
        Assert.assertEquals(1, summary.getEntryCount());
        Assert.assertEquals("test/testfile1.txt", summary.getFileName(0));
        Assert.assertEquals(10, summary.getFileLength(0));
        sut.close();
    }

    @Test
    public void testGetPath() throws IOException, ArchiveException {
        byte[] headerBytes = DumpArchiveUtil.createTestHeader();
        InputStream inputStream = new ByteArrayInputStream(headerBytes);
        DumpArchiveInputStream sut = new DumpArchiveInputStream(inputStream, ZIP_ENCODING);
        DumpArchiveEntry entry = sut.getNextEntry();
        Assert.assertEquals("/test/testfile1.txt", sut.getPath(entry));
        sut.close();
    }
}
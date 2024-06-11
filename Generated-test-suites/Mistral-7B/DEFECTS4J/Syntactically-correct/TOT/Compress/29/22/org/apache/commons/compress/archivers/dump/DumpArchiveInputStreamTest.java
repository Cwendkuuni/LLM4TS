package org.apache.commons.compress.archivers.dump;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.test.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Tests for the DumpArchiveInputStream class.
 */
public class DumpArchiveInputStreamTest {

    /**
     * Test reading a valid dump archive.
     *
     * @throws IOException on error reading the test data
     * @throws ArchiveException on error creating the input stream
     */
    @Test
    public void testValidDumpArchive() throws IOException, ArchiveException {
        byte[] testData = TestUtils.getBytesFromResource("/dump-archive-test.dump");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);

        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, null);

        DumpArchiveEntry entry = null;
        while ((entry = dumpArchiveInputStream.getNextEntry()) != null) {
            Assert.assertNotNull(entry);
            Assert.assertTrue(entry.isDirectory() || entry.isFile());
            if (entry.isDirectory()) {
                Assert.assertEquals("", entry.getName());
                Assert.assertFalse(entry.isFile());
            } else {
                Assert.assertEquals("file1.txt", entry.getName());
                Assert.assertTrue(entry.isFile());
            }
        }

        dumpArchiveInputStream.close();
    }

    /**
     * Test reading a dump archive with missing directory entries.
     *
     * @throws IOException on error reading the test data
     * @throws ArchiveException on error creating the input stream
     */
    @Test
    public void testMissingDirectoryEntries() throws IOException, ArchiveException {
        byte[] testData = TestUtils.getBytesFromResource("/dump-archive-missing-dirs.dump");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);

        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, null);

        DumpArchiveEntry entry = null;
        String fullPath = null;
        while ((entry = dumpArchiveInputStream.getNextEntry()) != null) {
            fullPath = entry.getFullPath();
            if (fullPath != null) {
                System.out.println("Found entry: " + fullPath);
            }
        }

        dumpArchiveInputStream.close();

        Assert.assertNotNull(fullPath);
        Assert.assertEquals("/dir1/file1.txt", fullPath);
    }

    /**
     * Test reading a dump archive with empty directory entries.
     *
     * @throws IOException on error reading the test data
     * @throws ArchiveException on error creating the input stream
     */
    @Test
    public void testEmptyDirectoryEntries() throws IOException, ArchiveException {
        byte[] testData = TestUtils.getBytesFromResource("/dump-archive-empty-dirs.dump");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);

        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, null);

        DumpArchiveEntry entry = null;
        String fullPath = null;
        while ((entry = dumpArchiveInputStream.getNextEntry()) != null) {
            fullPath = entry.getFullPath();
            if (fullPath != null) {
                System.out.println("Found entry: " + fullPath);
                Assert.assertEquals("/dir1/", fullPath);

                // Verify that the directory entry points to a valid directory
                entry = dumpArchiveInputStream.getNextEntry();
                Assert.assertNotNull(entry);
                Assert.assertEquals("file1.txt", entry.getName());
            }
        }

        dumpArchiveInputStream.close();
    }

    /**
     * Test reading a dump archive with a missing file entry.
     *
     * @throws IOException on error reading the test data
     * @throws ArchiveException on error creating the input stream
     */
    @Test(expected = IOException.class)
    public void testMissingFileEntries() throws IOException, ArchiveException {
        byte[] testData = TestUtils.getBytesFromResource("/dump-archive-missing-files.dump");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);

        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, null);

        DumpArchiveEntry entry = null;
        while ((entry = dumpArchiveInputStream.getNextEntry()) != null) {
            if (entry.getName().equals("file2.txt")) {
                Assert.fail("Found unexpected file entry");
            }
        }

        dumpArchiveInputStream.close();
    }

    /**
     * Test reading a dump archive with a corrupt record.
     *
     * @throws IOException on error reading the test data
     * @throws ArchiveException on error creating the input stream
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorruptRecord() throws IOException, ArchiveException {
        byte[] testData = TestUtils.getBytesFromResource("/dump-archive-corrupt.dump");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);

        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, null);

        dumpArchiveInputStream.getNextEntry();
    }

    /**
     * Test reading a dump archive with no records.
     *
     * @throws IOException on error creating the input stream
     */
    @Test
    public void testEmptyDumpArchive() throws IOException {
        RandomAccessFile testFile = new RandomAccessFile(
                TestUtils.getFixtureFile("dump-archive-empty.dump"), "r");
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(testFile, null);

        DumpArchiveEntry entry = null;
        while ((entry = dumpArchiveInputStream.getNextEntry()) != null) {
            // This should never happen since the test file is empty
            Assert.fail("Found unexpected entry: " + entry.getName());
        }

        dumpArchiveInputStream.close();
        testFile.close();
    }

    /**
     * Test reading a dump archive with a custom encoding.
     *
     * @throws IOException on error reading the test data
     * @throws ArchiveException on error creating the input stream
     */
    @Test
    public void testCustomEncoding() throws IOException, ArchiveException {
        byte[] testData = TestUtils.getBytesFromResource("/dump-archive-custom-encoding.dump");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);

        ZipEncoding customEncoding = ZipEncodingHelper.getZipEncoding("UTF-16BE");
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, customEncoding.getName());

        DumpArchiveEntry entry = null;
        String fullPath = null;
        while ((entry = dumpArchiveInputStream.getNextEntry()) != null) {
            fullPath = entry.getFullPath();
            if (fullPath != null) {
                System.out.println("Found entry: " + fullPath);
                Assert.assertEquals("/dir1/file1.txt", fullPath);
            }
        }

        dumpArchiveInputStream.close();
    }
}
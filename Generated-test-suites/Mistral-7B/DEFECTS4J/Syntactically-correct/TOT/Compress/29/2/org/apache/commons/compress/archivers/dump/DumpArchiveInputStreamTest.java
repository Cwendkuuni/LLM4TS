package org.apache.commons.compress.archivers.dump;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

public class DumpArchiveInputStreamTest {

    @Test
    public void testDumpArchiveInputStream() throws IOException, ArchiveException {
        String testFile = "testData.dump";

        // write test data to file
        try (FileOutputStream fos = new FileOutputStream(testFile)) {
            byte[] testData = TestUtils.getBytesFromResource("/testdata.dump");
            fos.write(testData);
        }

        // create input stream from test file
        try (InputStream is = new FileInputStream(testFile)) {
            String encoding = null; // use platform's default encoding
            DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is, encoding);

            // test getNextEntry() method
            DumpArchiveEntry entry = inputStream.getNextEntry();
            Assert.assertNotNull(entry);
            Assert.assertEquals("file1.txt", entry.getName());
            Assert.assertFalse(entry.isDirectory());
            Assert.assertEquals(30, entry.getSize());
            Assert.assertEquals(0L, entry.getOffset());

            entry = inputStream.getNextEntry();
            Assert.assertNotNull(entry);
            Assert.assertEquals("dir1/", entry.getName());
            Assert.assertTrue(entry.isDirectory());

            entry = inputStream.getNextEntry();
            Assert.assertNull(entry); // end of archive reached

            // test read() method
            byte[] buffer = new byte[10];
            int bytesRead = inputStream.read(buffer, 0, buffer.length);
            Assert.assertEquals(5, bytesRead); // first 5 bytes of "file1.txt"

            bytesRead = inputStream.read(buffer, 0, buffer.length);
            Assert.assertEquals(-1, bytesRead); // EOF reached

            // test close() method
            inputStream.close();
        }

        // delete test file
        File file = new File(testFile);
        file.delete();
    }

    @Test(expected = ArchiveException.class)
    public void testInvalidDumpArchive() throws IOException {
        String testFile = "testDataInvalid.dump";

        // write invalid test data to file
        try (FileOutputStream fos = new FileOutputStream(testFile)) {
            byte[] testData = TestUtils.getBytesFromResource("/testdataInvalid.dump");
            fos.write(testData);
        }

        // create input stream from test file
        try (InputStream is = new FileInputStream(testFile)) {
            String encoding = null; // use platform's default encoding
            DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is, encoding);
            inputStream.getNextEntry(); // should throw an exception here
        }

        // delete test file
        File file = new File(testFile);
        file.delete();
    }

    @Test(expected = IOException.class)
    public void testInvalidInputStream() throws ArchiveException {
        String testFile = "testData.dump";

        // create invalid input stream
        InputStream is = new InvalidInputStream();
        String encoding = null; // use platform's default encoding
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(is, encoding);

        inputStream.getNextEntry(); // should throw an IOException here
    }

    // InvalidInputStream class used to test IOException when passing an invalid input stream
    private static class InvalidInputStream extends InputStream {
        @Override
        public int read() throws IOException {
            throw new IOException("Invalid input stream");
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            throw new IOException("Invalid input stream");
        }
    }
}
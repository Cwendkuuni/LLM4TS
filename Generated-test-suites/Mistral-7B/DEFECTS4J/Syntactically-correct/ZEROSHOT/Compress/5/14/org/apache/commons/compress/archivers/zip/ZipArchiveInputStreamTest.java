import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.utils.ZipLong;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

public class ZipArchiveInputStreamTest {

    @Test
    public void testGetNextZipEntry() throws IOException {
        byte[] zipFileData = new byte[] {
                0x04, 0x03, 0x4b, 0x50, // local file header signature
                0x02, 0x01, 0x0b, 0x05, 0x06, 0x07, 0x08, 0x09, // version made by
                0x02, 0x00, // version needed to extract
                0x08, 0x07, // compression method (STORED)
                0x50, 0x4b, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, // last mod file time
                0x20, 0x28, 0x11, 0x12, // last mod file date
                0x86, 0x53, 0x3f, 0xd2, 0x2d, 0x04, 0x14, 0xcc, 0x1d, 0x21, 0x00, 0x00, 0x00, 0x00, // crc-32
                0x00, 0x00, 0x00, 0x00, // compressed size
                0x03, 0x04, 0x05, 0x06, // uncompressed size
                0x10, 0x07, // file name length
                0x02, 0x01, // extra field length
                0x54, 0x65, 0x73, 0x74, 0x20, 0x41, 0x70, 0x70, 0x53, 0x79, 0x73, 0x74, 0x65, 0x6d, 0x00, 0x00, // file name
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // data
                0x06, 0x05, 0x2b, 0x50, // end of central directory signature
                0x02, 0x01, // disk number starting with this entry
                0x01, 0x00, // disk number of central directory
                0x1a, 0x5b, 0x03, 0x5c, 0x5d, 0x01, 0x00, // total number of entries on this disk
                0x01, 0x00, // total number of entries in the central directory
                0x01, 0x02, // size of central directory
                0x01, 0x01, // number of disk with local header table
                0x01, 0x00, // number of disk with central directory
                0x01, 0x00, // number of comment length
                0x00, 0x00 // size of comment
        };

        InputStream testInputStream = new ByteArrayInputStream(zipFileData);
        ZipArchiveInputStream testZipInputStream = new ZipArchiveInputStream(testInputStream, ZipEncoding.UTF8, true);

        ZipArchiveEntry entry = testZipInputStream.getNextZipEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("test.txt", entry.getName());

        testZipInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntryWhenEndOfStreamIsReached() throws IOException {
        byte[] zipFileData = new byte[] {
                0x04, 0x03, 0x4b, 0x50, // local file header signature
                0x02, 0x01, 0x0b, 0x05, 0x06, 0x07, 0x08, 0x09, // version made by
                0x02, 0x00, // version needed to extract
                0x08, 0x07, // compression method (STORED)
                0x50, 0x4b, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, // last mod file time
                0x20, 0x28, 0x11, 0x12, // last mod file date
                0x86, 0x53, 0x3f, 0xd2, 0x2d, 0x04, 0x14, 0xcc, 0x1d, 0x21, 0x00, 0x00, 0x00, 0x00, // crc-32
                0x00, 0x00, 0x00, 0x00, // compressed size
                0x03, 0x04, 0x05, 0x06, // uncompressed size
                0x10, 0x07, // file name length
                0x02, 0x01, // extra field length
                0x54, 0x65, 0x73, 0x74, 0x20, 0x41, 0x70, 0x70, 0x53, 0x79, 0x73, 0x74, 0x65, 0x6d, 0x00, 0x00, // file name
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // data
                0x06, 0x05, 0x2b, 0x50, // end of central directory signature
                0x02, 0x01, // disk number starting with this entry
                0x01, 0x00, // disk number of central directory
                0x1a, 0x5b, 0x03, 0x5c, 0x5d, 0x01, 0x00, // total number of entries on this disk
                0x01, 0x00, // total number of entries in the central directory
                0x01, 0x02, // size of central directory
                0x01, 0x01, // number of disk with local header table
                0x01, 0x00, // number of disk with central directory
                0x01, 0x00, // number of comment length
                0x00, 0x00 // size of comment
        };

        InputStream testInputStream = new ByteArrayInputStream(zipFileData);
        ZipArchiveInputStream testZipInputStream = new ZipArchiveInputStream(testInputStream, ZipEncoding.UTF8, true);

        while (testZipInputStream.getNextZipEntry() != null) {
        }

        testZipInputStream.getNextZipEntry();
    }

    @Test
    public void testRead() throws IOException {
        byte[] testData = new byte[] { 0x01, 0x02, 0x03, 0x04 };
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2];

        InputStream testInputStream = new ByteArrayInputStream(testData);
        ZipArchiveInputStream testZipInputStream = new ZipArchiveInputStream(testInputStream, ZipEncoding.UTF8, true);
        ZipArchiveEntry entry = testZipInputStream.getNextZipEntry();

        int n = 0;
        while (n >= 0) {
            n = testZipInputStream.read(buffer, 0, buffer.length);
            if (n > 0) {
                outputStream.write(buffer, 0, n);
            }
        }

        Assert.assertEquals(testData, outputStream.toByteArray());

        testZipInputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        byte[] testData = new byte[] { 0x01, 0x02, 0x03, 0x04 };
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2];

        InputStream testInputStream = new ByteArrayInputStream(testData);
        ZipArchiveInputStream testZipInputStream = new ZipArchiveInputStream(testInputStream, ZipEncoding.UTF8, true);
        ZipArchiveEntry entry = testZipInputStream.getNextZipEntry();

        int n = 0;
        while (n >= 0) {
            n = testZipInputStream.read(buffer, 0, buffer.length);
            if (n > 0) {
                outputStream.write(buffer, 0, n);
            }
        }

        testZipInputStream.close();
        Assert.assertEquals(testData, outputStream.toByteArray());

        testZipInputStream = new ZipArchiveInputStream(testInputStream, ZipEncoding.UTF8, true);
        try {
            testZipInputStream.getNextZipEntry();
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
        testZipInputStream.close();
    }
}
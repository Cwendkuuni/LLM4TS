import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

public class ZipArchiveInputStreamTest {

    private static final String TEST_FILE = "TestZipFile.zip";

    @Test
    public void testZipArchiveInputStream() throws IOException {
        // Test basic functionality
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
                new FileInputStream(TEST_FILE), ZipEncoding.UTF8, true);
        ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("TestEntry.txt", entry.getName());

        // Test reading data
        byte[] buffer = new byte[1024];
        int length;
        while ((length = zipInputStream.read(buffer)) > 0) {
            Assert.assertArrayEquals("Unexpected data", "Test data".getBytes(),
                    Arrays.copyOfRange(buffer, 0, length));
        }
        Assert.assertEquals(-1, zipInputStream.read(buffer));

        // Test handling of different compression methods
        ZipArchiveInputStream deflatedInputStream = new ZipArchiveInputStream(
                new FileInputStream(TEST_FILE), ZipEncoding.UTF8, true);
        ZipArchiveEntry deflatedEntry = deflatedInputStream.getNextZipEntry();
        Assert.assertNotNull(deflatedEntry);
        Assert.assertEquals("DeflatedEntry.txt", deflatedEntry.getName());
        CRC32 crc = new CRC32();
        byte[] deflatedBuffer = new byte[1024];
        while ((length = deflatedInputStream.read(deflatedBuffer)) > 0) {
            crc.update(deflatedBuffer, 0, length);
        }
        Assert.assertEquals("d9091a122b870a12a12a12a1a1a1a1a1",
                Long.toHexString(crc.getValue()));

        // Test handling of empty zip files
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(ZipArchiveOutputStream.LFH_SIG);
        outputStream.write(0x14); // version made by
        outputStream.write(0x12); // version needed to extract
        outputStream.write(0); // general purpose bit flag
        outputStream.write(0); // compression method
        outputStream.writeShort((short) 0); // last mod file time
        outputStream.writeShort((short) 0); // last mod file date
        outputStream.write(0); // crc-32
        outputStream.write(0); // compressed size
        outputStream.write(0); // uncompressed size
        outputStream.writeShort((short) 0); // file name length
        outputStream.writeShort((short) 0); // extra field length
        outputStream.write(0); // comment length
        InputStream emptyInputStream = new ByteArrayInputStream(
                outputStream.toByteArray());
        ZipArchiveInputStream emptyZipInputStream = new ZipArchiveInputStream(
                emptyInputStream, ZipEncoding.UTF8, true);
        Assert.assertNull(emptyZipInputStream.getNextZipEntry());

        // Test handling of invalid zip files
        InputStream invalidInputStream = new ByteArrayInputStream(
                new byte[]{0x04, 0x03, 0x4b, 0x50, 0x02, 0x01, 0x00, 0x00});
        ZipArchiveInputStream invalidZipInputStream = new ZipArchiveInputStream(
                invalidInputStream, ZipEncoding.UTF8, true);
        Assert.assertNull(invalidZipInputStream.getNextZipEntry());

        // Test closing the stream
        zipInputStream.close();
        deflatedInputStream.close();
        emptyZipInputStream.close();
        invalidZipInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testInvalidFileStream() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
                new ByteArrayInputStream(new byte[0]), ZipEncoding.UTF8, true);
        zipInputStream.getNextZipEntry();
    }

    @Test(expected = IOException.class)
    public void testUnexpectedEOF() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(ZipArchiveOutputStream.LFH_SIG);
        outputStream.write(0x14); // version made by
        outputStream.write(0x12); // version needed to extract
        outputStream.write(0); // general purpose bit flag
        outputStream.write(0); // compression method
        outputStream.writeShort((short) 0); // last mod file time
        outputStream.writeShort((short) 0); // last mod file date
        outputStream.write(0); // crc-32
        outputStream.write(0x80); // compressed size
        outputStream.write(0); // uncompressed size
        outputStream.writeShort((short) 0); // file name length
        outputStream.writeShort((short) 0); // extra field length
        outputStream.write(0); // comment length
        InputStream unexpectedEOFInputStream = new ByteArrayInputStream(
                outputStream.toByteArray());
        ZipArchiveInputStream unexpectedEOFZipInputStream = new ZipArchiveInputStream(
                unexpectedEOFInputStream, ZipEncoding.UTF8, true);
        unexpectedEOFZipInputStream.getNextZipEntry();
        unexpectedEOFZipInputStream.read(new byte[1]);
    }
}
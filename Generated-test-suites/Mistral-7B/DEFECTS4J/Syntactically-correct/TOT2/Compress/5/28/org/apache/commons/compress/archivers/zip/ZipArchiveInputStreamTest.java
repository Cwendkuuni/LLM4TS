import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

public class ZipArchiveInputStreamTest {

    private static final String TEST_FILE = "test.zip";
    private static final String TEST_DATA = "Test data for ZipArchiveInputStream testing.";

    @Test
    public void testGetNextEntry() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
                new File(TEST_FILE).getInputStream(), ZipEncoding.UTF8, true);
        ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals(TEST_DATA, entry.getName());
        zipInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextEntry_AfterClosed() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
                new File(TEST_FILE).getInputStream(), ZipEncoding.UTF8, true);
        zipInputStream.close();
        zipInputStream.getNextZipEntry();
    }

    @Test
    public void testRead() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
                new ByteArrayInputStream(TEST_DATA.getBytes(StandardCharsets.UTF_8)),
                ZipEncoding.UTF8, true);
        byte[] buffer = new byte[10];
        int numBytesRead = zipInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(TEST_DATA.length(), numBytesRead);
        zipInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testRead_AfterClosed() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
                new ByteArrayInputStream(TEST_DATA.getBytes(StandardCharsets.UTF_8)),
                ZipEncoding.UTF8, true);
        zipInputStream.close();
        zipInputStream.read(new byte[10], 0, 10);
    }

    @Test
    public void testSkip() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
                new ByteArrayInputStream(TEST_DATA.getBytes(StandardCharsets.UTF_8)),
                ZipEncoding.UTF8, true);
        zipInputStream.skip(TEST_DATA.length());
        byte[] buffer = new byte[10];
        int numBytesRead = zipInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(0, numBytesRead);
        zipInputStream.close();
    }

    @Test
    public void testCloseEntry() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
                new ByteArrayInputStream(TEST_DATA.getBytes(StandardCharsets.UTF_8)),
                ZipEncoding.UTF8, true);
        ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
        zipInputStream.closeEntry();
        zipInputStream.close();
    }

    @Test
    public void testEmptyZipFile() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
                new ByteArrayInputStream(new byte[0]), ZipEncoding.UTF8, true);
        Assert.assertFalse(zipInputStream.getNextZipEntry() != null);
        zipInputStream.close();
    }

    @Test
    public void testEOFException() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
                new ByteArrayInputStream(TEST_DATA.getBytes(StandardCharsets.UTF_8)),
                ZipEncoding.UTF8, true);
        byte[] buffer = new byte[10];
        int numBytesRead = zipInputStream.read(buffer, 0, buffer.length);
        zipInputStream.close();
        Assert.assertThrows(EOFException.class, () -> {
            zipInputStream.read(buffer, 0, buffer.length);
        });
    }

    @Test
    public void testDataDescriptor() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
                new ByteArrayInputStream(TEST_DATA.getBytes(StandardCharsets.UTF_8)),
                ZipEncoding.UTF8, true);
        ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
        long expectedCRC = new CRC32().value();
        expectedCRC = (entry.getSize() > 0) ? expectedCRC ^ TEST_DATA.hashCode() : expectedCRC;
        Assert.assertEquals(expectedCRC, zipInputStream.getCrc().getValue());
        zipInputStream.close();
    }

    @Test
    public void testInvalidSignature() throws IOException {
        byte[] signature = new byte[]{0x04, 0x03, 0x4b, 0x50, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        InputStream inputStream = new ByteArrayInputStream(signature);
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(inputStream, ZipEncoding.UTF8, true);
        Assert.assertFalse(zipInputStream.getNextZipEntry() != null);
        zipInputStream.close();
    }
}
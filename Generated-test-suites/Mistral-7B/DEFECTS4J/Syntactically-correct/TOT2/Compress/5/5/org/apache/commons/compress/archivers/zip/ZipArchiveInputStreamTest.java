import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.DataFormatException;
import java.util.zip.ZipEntry;

public class ZipArchiveInputStreamTest {

    @Test
    public void testNormalScenario() throws IOException, DataFormatException {
        ZipFile zipFile = new ZipFile(new File("test.zip"));
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(zipFile.getInputStream("test.txt"));
        byte[] buffer = new byte[1024];
        int length;
        while ((length = zipArchiveInputStream.read(buffer)) != -1) {
            Assert.assertArrayEquals(buffer, new byte[length]);
        }
        zipArchiveInputStream.close();
    }

    @Test
    public void testReadEmptyFile() throws IOException {
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        int length = zipArchiveInputStream.read(new byte[1024]);
        Assert.assertEquals(-1, length);
        zipArchiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testInvalidSignature() throws IOException {
        byte[] signature = new byte[5];
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(signature));
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.close();
    }

    @Test
    public void testReadZipFileWithInvalidEntry() throws IOException {
        ZipFile zipFile = new ZipFile(new File("test.zip"));
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(zipFile.getInputStream("invalid_entry.txt"));
        zipArchiveInputStream.close();
        zipFile.close();
    }

    @Test(expected = IOException.class)
    public void testReadZipFileWithMissingCentralDirectory() throws IOException {
        ZipFile zipFile = new ZipFile(new File("test.zip"));
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(zipFile.getInputStream(""));
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.close();
        zipFile.close();
    }

    @Test
    public void testReadCompressedFile() throws IOException, DataFormatException {
        ZipFile zipFile = new ZipFile(new File("test.zip"));
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(zipFile.getInputStream("compressed.txt"));
        byte[] buffer = new byte[1024];
        int length;
        while ((length = zipArchiveInputStream.read(buffer)) != -1) {
            Assert.assertArrayEquals(buffer, new byte[length]);
        }
        zipArchiveInputStream.close();
        zipFile.close();
    }

    @Test
    public void testReadUncompressedFile() throws IOException, DataFormatException {
        ZipFile zipFile = new ZipFile(new File("test.zip"));
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(zipFile.getInputStream("uncompressed.txt"));
        byte[] expected = "Hello World!".getBytes(StandardCharsets.UTF_8);
        byte[] buffer = new byte[expected.length];
        int length = zipArchiveInputStream.read(buffer);
        Assert.assertArrayEquals(expected, buffer);
        zipArchiveInputStream.close();
        zipFile.close();
    }

    @Test
    public void testReadBigFile() throws IOException, DataFormatException {
        ZipFile zipFile = new ZipFile(new File("test.zip"));
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(zipFile.getInputStream("big.txt"));
        byte[] buffer = new byte[1024];
        int length;
        long totalRead = 0;
        while ((length = zipArchiveInputStream.read(buffer)) != -1) {
            totalRead += length;
        }
        Assert.assertEquals(new File("test.zip").length(), totalRead);
        zipArchiveInputStream.close();
        zipFile.close();
    }

    @Test
    public void testReadFileWithExtraFields() throws IOException, DataFormatException {
        ZipFile zipFile = new ZipFile(new File("test.zip"));
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(zipFile.getInputStream("extra_fields.txt"));
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("extra_fields.txt", entry.getName());
        zipArchiveInputStream.close();
        zipFile.close();
    }

    @Test
    public void testReadFileWithUTF8Encoding() throws IOException, DataFormatException {
        ZipFile zipFile = new ZipFile(new File("test.zip"));
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(zipFile.getInputStream("utf8.txt"), StandardCharsets.UTF_8.name());
        String expected = "你好，世界！";
        String actual = zipArchiveInputStream.getNextZipEntry().getName();
        Assert.assertEquals(expected, actual);
        zipArchiveInputStream.close();
        zipFile.close();
    }
}
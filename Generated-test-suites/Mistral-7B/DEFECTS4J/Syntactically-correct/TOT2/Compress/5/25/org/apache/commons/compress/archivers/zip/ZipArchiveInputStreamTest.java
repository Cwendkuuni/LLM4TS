import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ZipArchiveInputStreamTest {

    @Test
    public void testReadValidZipFile() throws IOException {
        String testFile = "test.zip";
        ZipFile zipFile = new ZipFile(new File(testFile));
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
                new FileInputStream(testFile), null, false);

        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(entry);

        byte[] buffer = new byte[1024];
        int bytesRead = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertTrue(bytesRead > 0);

        zipArchiveInputStream.close();
        zipFile.close();
    }

    @Test
    public void testReadEmptyZipFile() throws IOException {
        String testFile = "empty.zip";
        ZipFile zipFile = new ZipFile(new File(testFile));
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
                new FileInputStream(testFile), null, false);

        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNull(entry);

        int bytesRead = zipArchiveInputStream.read(new byte[1024], 0, 1024);
        Assert.assertEquals(-1, bytesRead);

        zipArchiveInputStream.close();
        zipFile.close();
    }

    @Test(expected = IOException.class)
    public void testReadCorruptedZipFile() throws IOException {
        String testFile = "corrupted.zip";
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
                new FileInputStream(testFile), null, false);

        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.close();

        Assert.assertNull(entry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadNegativeBufferLength() throws IOException {
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
                new ByteArrayInputStream(new byte[0]), null, false);

        int bytesRead = zipArchiveInputStream.read(new byte[1024], -1, 1024);
        zipArchiveInputStream.close();

        Assert.fail();
    }

    @Test(expected = IOException.class)
    public void testReadNegativeBytesToRead() throws IOException {
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
                new ByteArrayInputStream(new byte[0]), null, false);

        byte[] buffer = new byte[1024];
        int bytesRead = zipArchiveInputStream.read(buffer, 0, -1);
        zipArchiveInputStream.close();

        Assert.fail();
    }

    @Test
    public void testReadZipFileWithEmptyEntry() throws IOException {
        String testFile = "test_empty.zip";
        ZipFile zipFile = new ZipFile(new File(testFile));
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
                new FileInputStream(testFile), null, false);

        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("", entry.getName());

        byte[] buffer = new byte[1024];
        int bytesRead = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, bytesRead);

        zipArchiveInputStream.close();
        zipFile.close();
    }

    @Test
    public void testReadZipFileWithUTF8Encoding() throws IOException {
        String testFile = "test_utf8.zip";
        ZipFile zipFile = new ZipFile(new File(testFile));
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
                new FileInputStream(testFile), "UTF-8", false);

        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(entry);
        Assert.assertTrue(entry.getName().contains("UTF-8"));

        byte[] buffer = new byte[1024];
        int bytesRead = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertTrue(bytesRead > 0);

        String decodedName = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
        Assert.assertTrue(decodedName.contains("UTF-8"));

        zipArchiveInputStream.close();
        zipFile.close();
    }

    @Test(expected = IOException.class)
    public void testReadUnknownEncoding() throws IOException {
        String testFile = "test.zip";
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
                new FileInputStream(testFile), "Unknown", false);

        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.close();

        Assert.assertNull(entry);
    }
}
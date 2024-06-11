import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

public class ZipArchiveInputStreamTest {

    @Test
    public void testValidZipFile() throws IOException {
        ZipFile zipFile = new ZipFile("path/to/your/valid.zip");
        InputStream inputStream = zipFile.getInputStream(ZipFile.NAME);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals(entry.getName(), ZipFile.NAME);
        zipArchiveInputStream.close();
        zipFile.close();
    }

    @Test(expected = IOException.class)
    public void testInvalidZipFile() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.getNextZipEntry();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ZipFile zipFile = new ZipFile("path/to/your/zipfile.zip");
        InputStream inputStream = zipFile.getInputStream(ZipFile.NAME);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry entry1 = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(entry1);
        ZipArchiveEntry entry2 = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(entry2);
        zipArchiveInputStream.close();
        zipFile.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryAfterInvalidFile() throws IOException {
        ZipFile zipFile = new ZipFile("path/to/your/zipfile.zip");
        InputStream inputStream = zipFile.getInputStream(ZipFile.NAME);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry entry1 = zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry();
    }

    @Test
    public void testRead() throws IOException {
        ZipFile zipFile = new ZipFile("path/to/your/zipfile.zip");
        InputStream inputStream = zipFile.getInputStream(ZipFile.NAME);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        int bytesRead = 0;
        while ((bytesRead = zipArchiveInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        byte[] expectedBytes = zipFile.getInputStream(ZipFile.NAME).readAllBytes();
        Assert.assertArrayEquals(expectedBytes, outputStream.toByteArray());
        zipArchiveInputStream.close();
        zipFile.close();
    }

    @Test
    public void testClose() throws IOException {
        ZipFile zipFile = new ZipFile("path/to/your/zipfile.zip");
        InputStream inputStream = zipFile.getInputStream(ZipFile.NAME);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.close();
        ZipEntry entry = zipFile.getEntry(ZipFile.NAME);
        Assert.assertNull(zipArchiveInputStream.getNextZipEntry());
        zipFile.close();
    }

    @Test
    public void testReadWhenClosed() throws IOException {
        ZipFile zipFile = new ZipFile("path/to/your/zipfile.zip");
        InputStream inputStream = zipFile.getInputStream(ZipFile.NAME);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.close();
        byte[] buffer = new byte[1024];
        Assert.assertEquals(-1, zipArchiveInputStream.read(buffer));
        zipArchiveInputStream.getNextZipEntry();
        zipFile.close();
    }
}
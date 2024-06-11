import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ZipArchiveInputStreamTest {

    @Test
    public void testZipArchiveInputStream_basicUsage() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(outputStream);
        zipArchiveOutputStream.putNextEntry(new ZipArchiveEntry("test.txt"));
        zipArchiveOutputStream.write("Hello World!".getBytes());
        zipArchiveOutputStream.closeEntry();
        zipArchiveOutputStream.finish();
        byte[] zipBytes = outputStream.toByteArray();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(zipBytes);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(zipArchiveEntry);
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
        Assert.assertEquals(24, zipArchiveEntry.getSize());
        Assert.assertEquals(14, zipArchiveEntry.getCompressedSize());
        byte[] bytes = new byte[1024];
        int length = zipArchiveInputStream.read(bytes);
        Assert.assertEquals(14, length);
        Assert.assertEquals("Hello World!".getBytes(), bytes, 0, 14);
        zipArchiveInputStream.close();
    }

    @Test
    public void testZipArchiveInputStream_nullInputStream() {
        Assert.assertThrows(NullPointerException.class, () -> new ZipArchiveInputStream(null));
    }

    @Test
    public void testZipArchiveInputStream_emptyInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        Assert.assertNull(zipArchiveInputStream.getNextZipEntry());
        zipArchiveInputStream.close();
    }

    @Test
    public void testZipArchiveInputStream_invalidSignature() throws IOException {
        byte[] signature = new byte[]{0x78, 0x53, 0x43, 0x12, 0x34, 0x56, 0x78, 0x90};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(signature);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        Assert.assertNull(zipArchiveInputStream.getNextZipEntry());
        zipArchiveInputStream.close();
    }

    @Test
    public void testZipArchiveInputStream_centralDirectoryEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(outputStream);
        zipArchiveOutputStream.putNextEntry(new ZipArchiveEntry("test.txt"));
        zipArchiveOutputStream.write("Hello World!".getBytes());
        zipArchiveOutputStream.putNextEntry(new ZipArchiveEntry("test2.txt"));
        zipArchiveOutputStream.write("Test 2".getBytes());
        zipArchiveOutputStream.finish();
        byte[] zipBytes = outputStream.toByteArray();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(zipBytes);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry zipArchiveEntry1 = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(zipArchiveEntry1);
        Assert.assertEquals("test.txt", zipArchiveEntry1.getName());
        Assert.assertNotNull(zipArchiveEntry1.getSize());
        Assert.assertNotNull(zipArchiveEntry1.getCompressedSize());
        zipArchiveInputStream.closeEntry();
        ZipArchiveEntry zipArchiveEntry2 = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(zipArchiveEntry2);
        Assert.assertEquals("test2.txt", zipArchiveEntry2.getName());
        Assert.assertNotNull(zipArchiveEntry2.getSize());
        Assert.assertNotNull(zipArchiveEntry2.getCompressedSize());
        zipArchiveInputStream.closeEntry();
        Assert.assertNull(zipArchiveInputStream.getNextZipEntry());
        zipArchiveInputStream.close();
    }

    @Test
    public void testZipArchiveInputStream_dataDescriptor() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(outputStream);
        zipArchiveOutputStream.putNextEntry(new ZipArchiveEntry("test.txt"));
        zipArchiveOutputStream.write("Hello World!".getBytes());
        zipArchiveOutputStream.putNextEntry(new ZipArchiveEntry("test2.txt"));
        zipArchiveOutputStream.write("Test 2".getBytes());
        zipArchiveOutputStream.setComment("Test Comment");
        zipArchiveOutputStream.finish();
        byte[] zipBytes = outputStream.toByteArray();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(zipBytes);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry zipArchiveEntry1 = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(zipArchiveEntry1);
        Assert.assertEquals("test.txt", zipArchiveEntry1.getName());
        Assert.assertNotNull(zipArchiveEntry1.getSize());
        Assert.assertNotNull(zipArchiveEntry1.getCompressedSize());
        Assert.assertEquals("Test Comment", zipArchiveEntry1.getComment());
        zipArchiveInputStream.closeEntry();
        ZipArchiveEntry zipArchiveEntry2 = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(zipArchiveEntry2);
        Assert.assertEquals("test2.txt", zipArchiveEntry2.getName());
        Assert.assertNotNull(zipArchiveEntry2.getSize());
        Assert.assertNotNull(zipArchiveEntry2.getCompressedSize());
        Assert.assertEquals("Test Comment", zipArchiveEntry2.getComment());
        zipArchiveInputStream.closeEntry();
        Assert.assertNull(zipArchiveInputStream.getNextZipEntry());
        zipArchiveInputStream.close();
    }

    @Test
    public void testZipArchiveInputStream_invalidDataDescriptor() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(outputStream);
        zipArchiveOutputStream.putNextEntry(new ZipArchiveEntry("test.txt"));
        zipArchiveOutputStream.write("Hello World!".getBytes());
        zipArchiveOutputStream.finish();
        byte[] zipBytes = outputStream.toByteArray();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(zipBytes);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.closeEntry();
        zipArchiveInputStream.getNextZipEntry();
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNull(zipArchiveEntry);
        zipArchiveInputStream.close();
    }

    @Test
    public void testZipArchiveInputStream_streamClosed() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(outputStream);
        zipArchiveOutputStream.putNextEntry(new ZipArchiveEntry("test.txt"));
        zipArchiveOutputStream.write("Hello World!".getBytes());
        zipArchiveOutputStream.closeEntry();
        zipArchiveOutputStream.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        Assert.assertNull(zipArchiveInputStream.getNextZipEntry());
        zipArchiveInputStream.close();
    }

    @Test
    public void testZipArchiveInputStream_largeFile() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(outputStream);
        zipArchiveOutputStream.putNextEntry(new ZipArchiveEntry("largeFile.txt"));
        ByteArrayOutputStream largeFileOutputStream = new ByteArrayOutputStream();
        byte[] largeFileBytes = new byte[1024 * 1024 * 10];
        largeFileOutputStream.write(largeFileBytes);
        InputStream largeFileInputStream = new ByteArrayInputStream(largeFileBytes);
        IOUtils.copy(largeFileInputStream, zipArchiveOutputStream, 1024 * 1024);
        zipArchiveOutputStream.closeEntry();
        zipArchiveOutputStream.finish();
        byte[] zipBytes = outputStream.toByteArray();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(zipBytes);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(zipArchiveEntry);
        Assert.assertEquals("largeFile.txt", zipArchiveEntry.getName());
        Assert.assertNotNull(zipArchiveEntry.getSize());
        Assert.assertNotNull(zipArchiveEntry.getCompressedSize());
        byte[] bytes = new byte[1024 * 1024];
        int length = zipArchiveInputStream.read(bytes, 0, bytes.length);
        Assert.assertEquals(length, bytes.length);
        zipArchiveInputStream.closeEntry();
        Assert.assertNull(zipArchiveInputStream.getNextZipEntry());
        zipArchiveInputStream.close();
    }
}
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipException;

public class ZipArchiveInputStreamTest {

    @Test
    public void testValidZipFile() throws IOException {
        byte[] zipSignature = new byte[]{
                0x04, 0x03, 0x4b, 0x50, 
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, 
                // version made by
                (byte) 0, 0x14, 
                (byte) 0, 0x08, 
                // general purpose bit flag
                0x00, 0x02, 
                // compression method
                0x08, 0x00, 
                // last mod file time
                0x22, 0x31, (byte) 0x99, 0x8B, // 1998-12-22
                (byte) 0, 0x31, 
                // last mod file date
                0x2B, 0x0F, 
                // crc-32
                0x51, 0x64, 0x3B, 0x01, 0x67, 0x8B, 0x6B, 0x8E, 
                // compressed size
                0x00, 0x00, 0x00, 0x02, 
                // uncompressed size
                0x00, 0x00, 0x00, 0x03, 
                // file name length
                0x06, 
                // extra field length
                0x00
        };

        byte[] fileName = "sample.txt".getBytes("UTF-8");
        byte[] fileContent = "This is a test file".getBytes();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream os = outputStream;
        ZipArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(os, ZipEncoding.UTF8, true);

        ZipArchiveEntry entry = new ZipArchiveEntry("sample.txt");
        zipOutputStream.putNextEntry(entry);
        zipOutputStream.write(fileContent, 0, fileContent.length);
        zipOutputStream.closeEntry();
        zipOutputStream.finish();
        os.close();

        byte[] zipFile = new byte[outputStream.size()];
        System.arraycopy(outputStream.toByteArray(), 0, zipFile, 0, outputStream.size());

        ByteArrayInputStream inputStream = new ByteArrayInputStream(zipFile);
        ZipArchiveInputStream zis = new ZipArchiveInputStream(inputStream, ZipEncoding.UTF8, true);

        ZipArchiveEntry entryFromStream = zis.getNextZipEntry();
        Assert.assertEquals("sample.txt", entryFromStream.getName());

        byte[] expectedContent = new byte[fileContent.length];
        int bytesRead = zis.read(expectedContent, 0, fileContent.length);
        Assert.assertEquals(fileContent.length, bytesRead);
        Assert.assertArrayEquals(fileContent, expectedContent);

        zis.close();
    }

    @Test
    public void testEmptyZipFile() throws IOException {
        byte[] zipSignature = new byte[]{
                0x04, 0x03, 0x4b, 0x50, 
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, 
                // version made by
                (byte) 0, 0x14, 
                (byte) 0, 0x08, 
                // general purpose bit flag
                0x00, 0x02, 
                // compression method
                0x00, 0x00, 
                // last mod file time
                0x00, 0x00, 
                // last mod file date
                0x00, 0x00, 
                // crc-32
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                // compressed size
                0x00, 0x00, 0x00, 0x00, 
                // uncompressed size
                0x00, 0x00, 0x00, 0x00, 
                // file name length
                0x00, 
                // extra field length
                0x00
        };

        ByteArrayInputStream inputStream = new ByteArrayInputStream(zipSignature);
        ZipArchiveInputStream zis = new ZipArchiveInputStream(inputStream, ZipEncoding.UTF8, true);

        Assert.assertNull(zis.getNextZipEntry());
        zis.close();
    }

    @Test(expected = ZipException.class)
    public void testInvalidZipFile() throws IOException {
        byte[] zipSignature = new byte[]{
                0x04, 0x03, 0x4b, 0x50, 
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, 
                // version made by
                (byte) 0, 0x14, 
                (byte) 0, 0x08, 
                // general purpose bit flag
                0x00, 0x02, 
                // compression method
                0x08, 0x01, 
                // last mod file time
                0x22, 0x31, (byte) 0x99, 0x8B, // 1998-12-22
                (byte) 0, 0x31, 
                // last mod file date
                0x2B, 0x0F, 
                // crc-32
                0x51, 0x64, 0x3B, 0x01, 0x67, 0x8B, 0x6B, 0x8E, 
                // compressed size
                0x00, 0x00, 0x00, 0x02, 
                // uncompressed size
                0x00, 0x00, 0x00, 0x03, 
                // file name length
                0x06, 
                // extra field length
                0x00
        };

        ByteArrayInputStream inputStream = new ByteArrayInputStream(zipSignature);
        ZipArchiveInputStream zis = new ZipArchiveInputStream(inputStream, ZipEncoding.UTF8, true);

        zis.getNextZipEntry();
    }
}
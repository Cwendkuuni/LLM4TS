import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipArchiveInputStream;
    private ZipFile zipFile;
    private File testFile;

    @Before
    public void setUp() throws IOException, ZipException {
        String testData = "Test Data";
        String testFileName = "test.zip";
        String testFilePath = "testZipArchiveInputStream.test";

        testFile = new File(testFileName);
        testFile.createNewFile();
        FileOutputStream testFileOutputStream = new FileOutputStream(testFile);
        ZipOutputStream testZipOutputStream = new ZipOutputStream(testFileOutputStream);

        ZipEntry testEntry = new ZipEntry(testFilePath);
        testEntry.setSize(-1);
        testEntry.setMethod(ZipEntry.STORED);
        testZipOutputStream.putNextEntry(testEntry);
        testZipOutputStream.write(testData.getBytes(StandardCharsets.UTF_8));
        testZipOutputStream.finish();
        testZipOutputStream.close();

        testFileOutputStream.close();

        zipFile = new ZipFile(testFile);
        InputStream inputStream = zipFile.getInputStream(testFileName);
        zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals(testFileName, entry.getName());
        zipArchiveInputStream.closeEntry();

        entry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNull(entry);
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[100];
        int bytesRead = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, bytesRead);

        bytesRead = zipArchiveInputStream.read(buffer, 0, buffer.length);
        String testData = new String(buffer, StandardCharsets.UTF_8);
        Assert.assertEquals(testData, testData.substring(0, testData.length() - 1));
        Assert.assertEquals(testData.length() - 1, bytesRead);
    }

    @Test(expected = IOException.class)
    public void testReadWithClosedStream() throws IOException {
        zipArchiveInputStream.close();
        zipArchiveInputStream.read(new byte[100], 0, 100);
    }

    @Test(expected = IOException.class)
    public void testReadWithNullBuffer() throws IOException {
        zipArchiveInputStream.read(null, 0, 100);
    }

    @Test(expected = ZipException.class)
    public void testReadWithInvalidZipFile() throws IOException, ZipException {
        InputStream invalidInputStream = new ByteArrayInputStream(new byte[]{-1, -1, -1, -1, -1});
        ZipArchiveInputStream invalidZipArchiveInputStream = new ZipArchiveInputStream(invalidInputStream);
        byte[] buffer = new byte[1];
        invalidZipArchiveInputStream.read(buffer, 0, 1);
    }

    @Test(expected = ZipException.class)
    public void testReadWithInvalidEntry() throws IOException, ZipException {
        ZipFile invalidZipFile = new ZipFile(new File("nonexistent.zip"));
        InputStream invalidInputStream = invalidZipFile.getInputStream("nonexistentEntry");
        ZipArchiveInputStream invalidZipArchiveInputStream = new ZipArchiveInputStream(invalidInputStream);
        byte[] buffer = new byte[1];
        invalidZipArchiveInputStream.read(buffer, 0, 1);
    }

    @Test
    public void testClose() throws IOException, ZipException {
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(entry);
        zipArchiveInputStream.closeEntry();
        zipArchiveInputStream.close();

        entry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNull(entry);

        zipArchiveInputStream = new ZipArchiveInputStream(zipFile.getInputStream(testFileName));
        entry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(entry);
        zipArchiveInputStream.closeEntry();
        zipArchiveInputStream.close();

        zipFile.close();
        testFile.delete();
    }
}
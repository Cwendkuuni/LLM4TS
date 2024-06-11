import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.DataFormatException;
import java.util.zip.ZipEntry;

public class ZipArchiveInputStreamTest {

    @Test
    public void testValidZipFile() throws IOException, DataFormatException {
        String testZipFile = "testData/valid.zip";
        ZipFile zipFile = new ZipFile(testZipFile);
        ZipArchiveInputStream inputStream = new ZipArchiveInputStream(zipFile.getInputStream(zipFile.getEntry("testFile.txt")));
        ZipArchiveEntry entry = inputStream.getNextZipEntry();
        byte[] buffer = new byte[1024];
        int length = inputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals("Invalid file content", "Test content".getBytes(StandardCharsets.UTF_8), buffer, 0, length);
        inputStream.close();
        zipFile.close();
    }

    @Test(expected = IOException.class)
    public void testInvalidZipFile() throws IOException, DataFormatException {
        String testZipFile = "testData/invalid.zip";
        ZipFile zipFile = new ZipFile(testZipFile);
        ZipArchiveInputStream inputStream = new ZipArchiveInputStream(zipFile.getInputStream(zipFile.getEntry("testFile.txt")));
        ZipArchiveEntry entry = inputStream.getNextZipEntry();
        inputStream.close();
        zipFile.close();
    }

    @Test
    public void testEmptyZipFile() throws IOException, DataFormatException {
        String testZipFile = "testData/empty.zip";
        ZipFile zipFile = new ZipFile(testZipFile);
        ZipArchiveInputStream inputStream = new ZipArchiveInputStream(zipFile.getInputStream(zipFile.getEntry("")));
        ZipArchiveEntry entry = inputStream.getNextZipEntry();
        byte[] buffer = new byte[1024];
        int length = inputStream.read(buffer, 0, buffer.length);
        Assert.assertArrayEquals("Invalid file content", new byte[0], buffer);
        inputStream.close();
        zipFile.close();
    }

    @Test
    public void testInvalidSignature() throws IOException, DataFormatException {
        byte[] testSignature = new byte[ZipArchiveOutputStream.LFH_SIG.length];
        for (int i = 0; i < testSignature.length; i++) {
            testSignature[i] = (byte) (ZipArchiveOutputStream.LFH_SIG[i] ^ 0x01);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(testSignature);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        Assert.assertFalse("Invalid signature should not return a valid ZipArchiveEntry", zipArchiveInputStream.getNextZipEntry() != null);
        zipArchiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testNegativeSize() throws IOException {
        ZipArchiveInputStream inputStream = new ZipArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        inputStream.setSize(-1);
        int length = inputStream.read(new byte[1024], 0, 1024);
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testNullEntryName() throws IOException {
        ZipArchiveInputStream inputStream = new ZipArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        inputStream.setName(null);
        inputStream.getNextZipEntry();
        inputStream.close();
    }

    @Test
    public void testGetName() throws IOException {
        ZipArchiveInputStream inputStream = new ZipArchiveInputStream(new ByteArrayInputStream("testData/valid.zip".getBytes(StandardCharsets.UTF_8)));
        ZipArchiveEntry entry = inputStream.getNextZipEntry();
        Assert.assertEquals("Invalid file name", "testFile.txt", entry.getName());
        inputStream.close();
    }

    @Test
    public void testGetExtra() throws IOException {
        ZipArchiveInputStream inputStream = new ZipArchiveInputStream(new ByteArrayInputStream("testData/valid.zip".getBytes(StandardCharsets.UTF_8)));
        ZipArchiveEntry entry = inputStream.getNextZipEntry();
        byte[] extraData = entry.getExtra();
        Assert.assertArrayEquals("Invalid extra data", new byte[0], extraData);
        inputStream.close();
    }

    @Test
    public void testGetSize() throws IOException {
        ZipArchiveInputStream inputStream = new ZipArchiveInputStream(new ByteArrayInputStream("testData/valid.zip".getBytes(StandardCharsets.UTF_8)));
        ZipArchiveEntry entry = inputStream.getNextZipEntry();
        Assert.assertEquals("Invalid file size", 10, entry.getSize());
        inputStream.close();
    }

    @Test
    public void testGetCompressedSize() throws IOException {
        ZipArchiveInputStream inputStream = new ZipArchiveInputStream(new ByteArrayInputStream("testData/valid.zip".getBytes(StandardCharsets.UTF_8)));
        ZipArchiveEntry entry = inputStream.getNextZipEntry();
        Assert.assertEquals("Invalid compressed size", 5, entry.getCompressedSize());
        inputStream.close();
    }

    @Test
    public void testRead() throws IOException {
        String testZipFile = "testData/valid.zip";
        ZipFile zipFile = new ZipFile(testZipFile);
        ZipArchiveInputStream inputStream = new ZipArchiveInputStream(zipFile.getInputStream(zipFile.getEntry("testFile.txt")));
        byte[] buffer = new byte[1024];
        int length = inputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals("Invalid file content", "Test content".getBytes(StandardCharsets.UTF_8), buffer, 0, length);
        inputStream.close();
        zipFile.close();
    }

    @Test
    public void testReadWithNullBuffer() throws IOException {
        String testZipFile = "testData/valid.zip";
        ZipFile zipFile = new ZipFile(testZipFile);
        ZipArchiveInputStream inputStream = new ZipArchiveInputStream(zipFile.getInputStream(zipFile.getEntry("testFile.txt")));
        int length = inputStream.read(null, 0, 1024);
        inputStream.close();
        zipFile.close();
    }

    @Test
    public void testReadWithNegativeLength() throws IOException {
        String testZipFile = "testData/valid.zip";
        ZipFile zipFile = new ZipFile(testZipFile);
        ZipArchiveInputStream inputStream = new ZipArchiveInputStream(zipFile.getInputStream(zipFile.getEntry("testFile.txt")));
        int length = inputStream.read(new byte[1024], -1, 1024);
        inputStream.close();
        zipFile.close();
    }

    @Test
    public void testReadWithZeroLength() throws IOException {
        String testZipFile = "testData/valid.zip";
        ZipFile zipFile = new ZipFile(testZipFile);
        ZipArchiveInputStream inputStream = new ZipArchiveInputStream(zipFile.getInputStream(zipFile.getEntry("testFile.txt")));
        int length = inputStream.read(new byte[1024], 0, 0);
        inputStream.close();
        zipFile.close();
    }

    @Test
    public void testClose() throws IOException {
        String testZipFile = "testData/valid.zip";
        ZipFile zipFile = new ZipFile(testZipFile);
        ZipArchiveInputStream inputStream = new ZipArchiveInputStream(zipFile.getInputStream(zipFile.getEntry("testFile.txt")));
        inputStream.close();
        ZipArchiveEntry entry = inputStream.getNextZipEntry();
        Assert.assertNull("Invalid next entry after closing the stream", entry);
        zipFile.close();
    }

    @Test
    public void testCloseMultipleTimes() throws IOException {
        String testZipFile = "testData/valid.zip";
        ZipFile zipFile = new ZipFile(testZipFile);
        ZipArchiveInputStream inputStream = new ZipArchiveInputStream(zipFile.getInputStream(zipFile.getEntry("testFile.txt")));
        inputStream.close();
        inputStream.close();
        ZipArchiveEntry entry = inputStream.getNextZipEntry();
        Assert.assertNull("Invalid next entry after closing the stream multiple times", entry);
        zipFile.close();
    }

    @Test
    public void testCloseAfterInvalidEntry() throws IOException {
        String testZipFile = "testData/valid.zip";
        ZipFile zipFile = new ZipFile(testZipFile);
        ZipArchiveInputStream inputStream = new ZipArchiveInputStream(zipFile.getInputStream(zipFile.getEntry("nonExistentEntry")));
        inputStream.close();
        ZipArchiveEntry entry = inputStream.getNextZipEntry();
        Assert.assertNull("Invalid next entry after closing the stream with an invalid entry", entry);
        zipFile.close();
    }

    @Test(expected = IOException.class)
    public void testCloseAfterReadError() throws IOException {
        String testZipFile = "testData/valid.zip";
        ZipFile zipFile = new ZipFile(testZipFile);
        ZipArchiveInputStream inputStream = new ZipArchiveInputStream(zipFile.getInputStream(zipFile.getEntry("testFile.txt")));
        try {
            inputStream.read(new byte[1024], 0, 1024);
            inputStream.close();
        } catch (IOException e) {
            throw new IOException("Invalid error message", e);
        }
        ZipArchiveEntry entry = inputStream.getNextZipEntry();
        Assert.assertNull("Invalid next entry after closing the stream with a read error", entry);
        zipFile.close();
    }

    @Test(expected = EOFException.class)
    public void testReadUntilEndOfStream() throws IOException {
        String testZipFile = "testData/valid.zip";
        ZipFile zipFile = new ZipFile(testZipFile);
        ZipArchiveInputStream inputStream = new ZipArchiveInputStream(zipFile.getInputStream(zipFile.getEntry("testFile.txt")));
        byte[] buffer = new byte[1024];
        int length = inputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals("Invalid file content", "Test content".getBytes(StandardCharsets.UTF_8), buffer, 0, length);
        length = inputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals("Invalid number of bytes read", -1, length);
        inputStream.close();
        zipFile.close();
    }

    @Test(expected = EOFException.class)
    public void testReadUntilEndOfStreamWithException() throws IOException {
        String testZipFile = "testData/valid.zip";
        ZipFile zipFile = new ZipFile(testZipFile);
        ZipArchiveInputStream inputStream = new ZipArchiveInputStream(zipFile.getInputStream(zipFile.getEntry("testFile.txt")));
        try {
            inputStream.read(new byte[1024], 0, 1024);
            inputStream.read(new byte[1024], 0, 1024);
            inputStream.read(new byte[1024], 0, 1024);
        } catch (IOException e) {
            Assert.assertEquals("Invalid error message", "End of input", e.getMessage());
            throw e;
        }
        inputStream.close();
        zipFile.close();
    }

    @Test(expected = IOException.class)
    public void testReadPastEndOfStream() throws IOException {
        String testZipFile = "testData/valid.zip";
        ZipFile zipFile = new ZipFile(testZipFile);
        ZipArchiveInputStream inputStream = new ZipArchiveInputStream(zipFile.getInputStream(zipFile.getEntry("testFile.txt")));
        byte[] buffer = new byte[1024];
        int length = inputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals("Invalid file content", "Test content".getBytes(StandardCharsets.UTF_8), buffer, 0, length);
        length = inputStream.read(buffer, 0, buffer.length + 1);
        inputStream.close();
        zipFile.close();
    }

    @Test(expected = IOException.class)
    public void testReadWithClosedStream() throws IOException {
        String testZipFile = "testData/valid.zip";
        ZipFile zipFile = new ZipFile(testZipFile);
        ZipArchiveInputStream inputStream = new ZipArchiveInputStream(zipFile.getInputStream(zipFile.getEntry("testFile.txt")));
        inputStream.close();
        byte[] buffer = new byte[1024];
        int length = inputStream.read(buffer, 0, buffer.length);
        inputStream.close();
        zipFile.close();
    }

    @Test(expected = IOException.class)
    public void testReadWithNullStream() throws IOException {
        ZipArchiveInputStream inputStream = new ZipArchiveInputStream(null);
        byte[] buffer = new byte[1024];
        int length = inputStream.read(buffer, 0, buffer.length);
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testReadWithClosedAndNullStream() throws IOException {
        ZipArchiveInputStream inputStream = new ZipArchiveInputStream(null);
        inputStream.close();
        byte[] buffer = new byte[1024];
        int length = inputStream.read(buffer, 0, buffer.length);
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testReadWithInvalidStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        inputStream.mark(1024);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        byte[] buffer = new byte[1024];
        int length = zipArchiveInputStream.read(buffer, 0, buffer.length);
        zipArchiveInputStream.close();
        inputStream.reset();
    }

    @Test(expected = IOException.class)
    public void testReadWithStreamMarkSet() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.read(new byte[1024], 0, 1024);
        inputStream.mark(1024);
        byte[] buffer = new byte[1024];
        int length = zipArchiveInputStream.read(buffer, 0, buffer.length);
        zipArchiveInputStream.close();
        inputStream.reset();
    }

    @Test(expected = IOException.class)
    public void testReadWithStreamMarkNotSet() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.read(new byte[1024], 0, 1024);
        inputStream.reset();
        byte[] buffer = new byte[1024];
        int length = zipArchiveInputStream.read(buffer, 0, buffer.length);
        zipArchiveInputStream.close();
    }
}
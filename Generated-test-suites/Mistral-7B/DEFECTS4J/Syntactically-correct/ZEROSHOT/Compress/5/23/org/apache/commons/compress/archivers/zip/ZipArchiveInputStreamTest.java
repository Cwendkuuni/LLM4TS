import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.utils.ZipLong;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ZipArchiveInputStreamTest {

    private InputStream inputStream;
    private ZipArchiveInputStream zipArchiveInputStream;
    private ZipArchiveEntry zipArchiveEntry;

    @Before
    public void setUp() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(ZipArchiveOutputStream.LFH_SIG.bytes);
        outputStream.write(ZipShort.toBytes((short) 20)); // version made by
        outputStream.write(ZipShort.toBytes((short) 0x0a08)); // general purpose bit flag
        outputStream.write(ZipShort.toBytes((short) 1)); // compression method
        outputStream.write(ZipShort.toBytes((short) 2012, (short) 12)); // last mod file time
        outputStream.write(ZipShort.toBytes((short) 2013, (short) 1)); // last mod file date
        outputStream.write(ZipLong.toBytes(0x12345678L)); // crc-32
        outputStream.write(ZipLong.toBytes(1234L)); // compressed size
        outputStream.write(ZipLong.toBytes(5678L)); // uncompressed size
        outputStream.write(ZipShort.toBytes((short) 10)); // file name length
        outputStream.write(ZipShort.toBytes((short) 5)); // extra field length
        outputStream.write("test.txt\0".getBytes()); // file name
        outputStream.close();
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        zipArchiveInputStream = new ZipArchiveInputStream(inputStream, ZipEncoding.UTF8, true);
    }

    @After
    public void tearDown() throws IOException {
        if (zipArchiveInputStream != null) {
            zipArchiveInputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ZipArchiveEntry expected = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry actual = zipArchiveInputStream.getNextZipEntry();
        assertEquals(expected, actual);
        expected = null;
        actual = zipArchiveInputStream.getNextZipEntry();
        assertNull(actual);
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntryAfterClosed() throws IOException {
        zipArchiveInputStream.close();
        zipArchiveInputStream.getNextZipEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntryBeforeFirstEntry() throws IOException {
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry();
        ZipArchiveEntry actual = zipArchiveInputStream.getNextZipEntry();
        assertNull(actual);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ZipArchiveEntry actual = zipArchiveInputStream.getNextEntry();
        assertEquals(zipArchiveEntry, actual);
        actual = zipArchiveInputStream.getNextEntry();
        assertNull(actual);
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryAfterClosed() throws IOException {
        zipArchiveInputStream.close();
        zipArchiveInputStream.getNextEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryBeforeFirstEntry() throws IOException {
        zipArchiveInputStream.getNextEntry();
        zipArchiveInputStream.getNextEntry();
        zipArchiveInputStream.getNextEntry();
        ZipArchiveEntry actual = zipArchiveInputStream.getNextEntry();
        assertNull(actual);
    }

    @Test
    public void testRead() throws IOException, DataFormatException, ZipException {
        byte[] buffer = new byte[10];
        int result = zipArchiveInputStream.read(buffer);
        assertEquals(10, result);
        for (int i = 0; i < 10; i++) {
            assertEquals("test.txt".charAt(i), (char) buffer[i]);
        }

        result = zipArchiveInputStream.read(buffer);
        assertEquals(-1, result);
    }

    @Test(expected = ZipException.class)
    public void testReadWithInvalidSignature() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[]{-1, -1, -1, -1, 0x04}); // invalid signature
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        zipArchiveInputStream = new ZipArchiveInputStream(inputStream, ZipEncoding.UTF8, true);
        byte[] buffer = new byte[10];
        zipArchiveInputStream.read(buffer);
    }
}
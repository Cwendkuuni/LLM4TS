import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream inputStream;
    private ZipFile zipFile;

    @BeforeEach
    public void setUp() throws IOException {
        // Initialize ZipFile for testing
        byte[] zipBytes = new byte[]{(byte) 0x04, (byte) 0x03, (byte) 0x4b, (byte) 0x50,
                (byte) 0x02, (byte) 0x01, (byte) 0x0b, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08,
                (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0xff, (byte) 0xff,
                0, 2, 0, 2, 0, 0, 4, 4, 0, 0, 0, 0, 2, 2, 4, 0, 32, 0};
        zipFile = new ZipFile(new ByteArrayInputStream(zipBytes), ZipEncodingHelper.UTF8);
        inputStream = new ZipArchiveInputStream(zipFile.getInputStream(zipFile.getNameEntry("test.txt")));
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ZipArchiveEntry entry = inputStream.getNextZipEntry();
        Assertions.assertNotNull(entry);
        Assertions.assertEquals("test.txt", entry.getName());
        Assertions.assertEquals("UTF-8", entry.getEncoding());
        Assertions.assertEquals(ZipArchiveOutputStream.STORED, entry.getMethod());
        Assertions.assertFalse(entry.isDirectory());
        Assertions.assertFalse(entry.isCompressed());
        Assertions.assertFalse(entry.isExtra());
        Assertions.assertEquals(-1, entry.getSize());

        entry = inputStream.getNextZipEntry();
        Assertions.assertNull(entry);
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[10];

        int readBytes = inputStream.read(buffer);
        Assertions.assertEquals(buffer.length, readBytes);
        Assertions.assertArrayEquals(inputStream.readAllBytes(), buffer);

        inputStream.close();

        inputStream = new ZipArchiveInputStream(zipFile.getInputStream(zipFile.getNameEntry("test.txt")));
        readBytes = inputStream.read(buffer);
        Assertions.assertEquals(-1, readBytes);

        inputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        byte[] buffer = new byte[10];

        int readBytes = inputStream.read(buffer);
        Assertions.assertEquals(buffer.length, readBytes);
        Assertions.assertArrayEquals(inputStream.readAllBytes(), buffer);

        inputStream.close();
        Assertions.assertFalse(inputStream.isOpen());

        inputStream = new ZipArchiveInputStream(zipFile.getInputStream(zipFile.getNameEntry("test.txt")));
        readBytes = inputStream.read(buffer);
        Assertions.assertEquals(-1, readBytes);

        inputStream.close();
        Assertions.assertFalse(inputStream.isOpen());
    }

    @Test
    public void testReadAllBytes() throws IOException {
        byte[] expected = new byte[]{(byte) 0x65, (byte) 0x78, (byte) 0x61, (byte) 0x6d,
                (byte) 0x70, (byte) 0x6c, (byte) 0x65, (byte) 0x2e, (byte) 0x67, (byte) 0x7a,
                (byte) 0x6d, (byte) 0x70, (byte) 0x6c, (byte) 0x61, (byte) 0x79};

        byte[] buffer = new byte[expected.length];
        int readBytes = inputStream.read(buffer);
        Assertions.assertEquals(expected.length, readBytes);
        Assertions.assertArrayEquals(expected, buffer);

        inputStream.close();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] testBytes = new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3};
        outputStream.write(testBytes);
        outputStream.close();
        byte[] inputBytes = outputStream.toByteArray();

        inputStream = new ZipArchiveInputStream(new ByteArrayInputStream(inputBytes));
        buffer = new byte[inputBytes.length];
        readBytes = inputStream.read(buffer);
        Assertions.assertEquals(inputBytes.length, readBytes);
        Assertions.assertArrayEquals(inputBytes, buffer);

        inputStream.close();

        ByteArrayOutputStream emptyOutputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[0]);
        outputStream.close();
        byte[] emptyInputBytes = outputStream.toByteArray();

        inputStream = new ZipArchiveInputStream(new ByteArrayInputStream(emptyInputBytes));
        readBytes = inputStream.read(buffer);
        Assertions.assertEquals(-1, readBytes);

        inputStream.close();
    }

    @Test
    public void testSkip() throws IOException {
        long skipBytes = 10;
        long skippedBytes = inputStream.skip(skipBytes);
        Assertions.assertEquals(skipBytes, skippedBytes);

        byte[] buffer = new byte[10];
        int readBytes = inputStream.read(buffer);
        Assertions.assertEquals(-1, readBytes);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] expected = new byte[]{(byte) 0x65, (byte) 0x78, (byte) 0x61, (byte) 0x6d,
                (byte) 0x70, (byte) 0x6c, (byte) 0x65, (byte) 0x2e, (byte) 0x67, (byte) 0x7a,
                (byte) 0x6d, (byte) 0x70, (byte) 0x6c, (byte) 0x61, (byte) 0x79};

        byte[] buffer = new byte[expected.length];
        inputStream.readFully(buffer);
        Assertions.assertArrayEquals(expected, buffer);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] testBytes = new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3};
        outputStream.write(testBytes);
        outputStream.close();
        byte[] inputBytes = outputStream.toByteArray();

        byte[] testBuffer = new byte[inputBytes.length];
        inputStream = new ZipArchiveInputStream(new ByteArrayInputStream(inputBytes));
        inputStream.readFully(testBuffer);
        Assertions.assertArrayEquals(inputBytes, testBuffer);

        ByteArrayOutputStream emptyOutputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[0]);
        outputStream.close();
        byte[] emptyInputBytes = outputStream.toByteArray();

        inputStream = new ZipArchiveInputStream(new ByteArrayInputStream(emptyInputBytes));
        inputStream.readFully(buffer);
        Assertions.assertEquals(0, buffer.length);

        inputStream.close();
    }

    @Test
    public void testMatchesSignature() throws IOException {
        Assertions.assertTrue(ZipArchiveInputStream.matches(inputStream.getInputStream().readAllBytes(), 0));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] testBytes = new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3};
        outputStream.write(testBytes);
        outputStream.close();
        byte[] testInputBytes = outputStream.toByteArray();

        Assertions.assertFalse(ZipArchiveInputStream.matches(testInputBytes, 0));
    }
}
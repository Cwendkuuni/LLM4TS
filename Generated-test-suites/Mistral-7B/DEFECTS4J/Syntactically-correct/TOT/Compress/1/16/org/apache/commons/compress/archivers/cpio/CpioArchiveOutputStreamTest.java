import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.util.CpioUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link CpioArchiveOutputStream}.
 */
public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        if (cpioOutputStream != null) {
            cpioOutputStream.close();
        }
    }

    @Test
    public void testPutNextEntryWithValidParameters() throws IOException {
        String fileName = "testFile";
        long fileSize = 10;
        String fileContent = "Test content";

        cpioOutputStream.putNextEntry(new CpioArchiveEntry(fileName, fileSize));
        cpioOutputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
        cpioOutputStream.closeArchiveEntry();

        assertEquals(CpioConstants.FORMAT_NEW, cpioOutputStream.getFormat());
        assertEquals(fileName, cpioOutputStream.getNextEntry().getName());
        assertEquals(fileSize, cpioOutputStream.getNextEntry().getSize());
        assertNull(cpioOutputStream.getNextEntry().getChecksum());

        byte[] output = outputStream.toByteArray();
        assertEquals(getExpectedHeaderSize() + fileSize + 1, output.length);
        assertEquals(getExpectedHeaderBytes(fileName, fileSize), output, 0, getExpectedHeaderSize());
        assertEquals(fileContent, new String(getCpioEntryContent(output), StandardCharsets.UTF_8));
    }

    @Test
    public void testPutNextEntryWithInvalidFormat() throws IOException {
        cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        try {
            cpioOutputStream.putNextEntry(new CpioArchiveEntry("testFile", 10, CpioConstants.FORMAT_OLD_ASCII));
            fail("Should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPutNextEntryWithDuplicateEntry() throws IOException {
        String fileName = "testFile";
        long fileSize = 10;

        cpioOutputStream.putNextEntry(new CpioArchiveEntry(fileName, fileSize));
        cpioOutputStream.putNextEntry(new CpioArchiveEntry(fileName, fileSize));

        try {
            cpioOutputStream.putNextEntry(new CpioArchiveEntry(fileName, fileSize));
            fail("Should throw an IOException");
        } catch (IOException e) {
            // Expected
        }

        byte[] output = outputStream.toByteArray();
        assertEquals(getExpectedHeaderSize() + fileSize + 1, output.length);
    }

    @Test
    public void testPutNextEntryWithInvalidName() throws IOException {
        try {
            cpioOutputStream.putNextEntry(new CpioArchiveEntry("", 10));
            fail("Should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            cpioOutputStream.putNextEntry(new CpioArchiveEntry(null, 10));
            fail("Should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            cpioOutputStream.putNextEntry(new CpioArchiveEntry("\u0000testFile", 10));
            fail("Should throw an UnsupportedEncodingException");
        } catch (UnsupportedEncodingException e) {
            // Expected
        }
    }

    @Test
    public void testPutNextEntryWithInvalidSize() throws IOException {
        try {
            cpioOutputStream.putNextEntry(new CpioArchiveEntry("testFile", Long.MAX_VALUE));
            fail("Should throw an IOException");
        } catch (IOException e) {
            // Expected
        }

        try {
            cpioOutputStream.putNextEntry(new CpioArchiveEntry("testFile", -1));
            fail("Should throw an IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testPutNextEntryWithInvalidChecksum() throws IOException {
        String fileName = "testFile";
        long fileSize = 10;
        long checksum = 12345;

        cpioOutputStream.putNextEntry(new CpioArchiveEntry(fileName, fileSize, checksum));
        cpioOutputStream.write(new byte[fileSize]);
        cpioOutputStream.closeArchiveEntry();

        byte[] output = outputStream.toByteArray();
        assertEquals(getExpectedHeaderSize() + fileSize + 1, output.length);
        assertEquals(getExpectedHeaderBytes(fileName, fileSize), output, 0, getExpectedHeaderSize());
        assertEquals(checksum, CpioUtil.readCrc32(getCpioEntryContent(output)));
    }

    @Test
    public void testWriteWithValidParameters() throws IOException {
        String fileName = "testFile";
        long fileSize = 10;
        String fileContent = "Test content";

        cpioOutputStream.putNextEntry(new CpioArchiveEntry(fileName, fileSize));
        cpioOutputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
        cpioOutputStream.closeArchiveEntry();

        byte[] output = outputStream.toByteArray();
        assertEquals(getExpectedHeaderSize() + fileSize + 1, output.length);
        assertEquals(getExpectedHeaderBytes(fileName, fileSize), output, 0, getExpectedHeaderSize());
        assertEquals(fileContent, new String(getCpioEntryContent(output), StandardCharsets.UTF_8));
    }

    @Test
    public void testWriteWithInvalidEntry() throws IOException {
        try {
            cpioOutputStream.write("Test content".getBytes(), 0, 10);
            fail("Should throw an IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testFinish() throws IOException {
        String fileName1 = "testFile1";
        long fileSize1 = 5;
        String fileName2 = "testFile2";
        long fileSize2 = 8;

        cpioOutputStream.putNextEntry(new CpioArchiveEntry(fileName1, fileSize1));
        cpioOutputStream.write(new byte[fileSize1]);
        cpioOutputStream.closeArchiveEntry();

        cpioOutputStream.putNextEntry(new CpioArchiveEntry(fileName2, fileSize2));
        cpioOutputStream.write(new byte[fileSize2]);
        cpioOutputStream.closeArchiveEntry();

        cpioOutputStream.finish();

        byte[] output = outputStream.toByteArray();
        assertEquals(getExpectedHeaderSize() + fileSize1 + 1 + getExpectedHeaderSize() + fileSize2 + 1, output.length);
        assertEquals(getExpectedTrailerBytes(), output, output.length - getExpectedTrailerSize(), getExpectedTrailerSize());
    }

    private static byte[] getCpioEntryContent(byte[] bytes) {
        int headerSize = getExpectedHeaderSize();
        int trailerSize = getExpectedTrailerSize();
        return new byte[bytes.length - headerSize - trailerSize];
    }

    private static int getExpectedHeaderSize() {
        return 12; // magic number, inode, mode, uid, gid, number of links, time, size, device maj, device min, name length, checksum
    }

    private static int getExpectedTrailerSize() {
        return 6; // magic number, name length, size
    }

    private static byte[] getExpectedHeaderBytes(String fileName, long fileSize) {
        int headerSize = getExpectedHeaderSize();
        String name = fileName;
        long inode = 1;
        int mode = 0644;
        long uid = 1000;
        long gid = 1000;
        int numberOfLinks = 1;
        long time = System.currentTimeMillis();

        byte[] bytes = new byte[headerSize];
        System.arraycopy(CpioConstants.MAGIC_NEW.getBytes(), 0, bytes, 0, 8);
        writeAsciiLong(bytes, 8, inode);
        writeAsciiLong(bytes, 16, mode);
        writeAsciiLong(bytes, 24, uid);
        writeAsciiLong(bytes, 32, gid);
        writeAsciiLong(bytes, 36, numberOfLinks);
        writeAsciiLong(bytes, 40, time);
        writeAsciiLong(bytes, 48, fileSize);
        writeAsciiLong(bytes, 56, 0);
        writeAsciiLong(bytes, 60, 0);
        writeAsciiLong(bytes, 64, 0);
        writeAsciiLong(bytes, 68, 0);
        writeAsciiLong(bytes, 72, name.length() + 1);
        writeCrc32(bytes, 76, name.getBytes(StandardCharsets.UTF_8), fileSize);

        return bytes;
    }

    private static void writeAsciiLong(byte[] bytes, int offset, long number) {
        String numberString = Long.toString(number, 8);
        for (int i = 0; i < numberString.length(); i++) {
            bytes[offset + i] = (byte) numberString.charAt(i);
        }
    }

    private static void writeCrc32(byte[] bytes, int offset, byte[] content, long fileSize) {
        long crc = CpioUtil.crc32(content, 0, content.length);
        for (int i = 0; i < 4; i++) {
            bytes[offset + i] = (byte) (crc & 0xFF);
            crc >>= 8;
        }
        long remaining = fileSize - content.length;
        if (remaining > 0) {
            for (int i = 0; i < remaining; i++) {
                bytes[offset + 4 + i] = 0;
            }
        }
    }
}
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private ByteArrayInputStream byteArrayInputStream;
    private Map<String, CpioArchiveEntry> entryMap;

    @Before
    public void setUp() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_NEW);
    }

    @Test
    public void testPutNextEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);

        outputStream.putNextEntry(entry);
        Assert.assertEquals(CpioConstants.MAGIC_NEW, byteArrayOutputStream.toByteArray(), 0, 8);

        writeNewEntryHeader(outputStream, entry);

        outputStream.write("Test data".getBytes());
        outputStream.closeArchiveEntry();

        Assert.assertEquals(13, byteArrayOutputStream.size());
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithDuplicateEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);

        outputStream.putNextEntry(entry);
        outputStream.putNextEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithInvalidFormat() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setFormat(-1);

        outputStream.putNextEntry(entry);
    }

    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);

        outputStream.putNextEntry(entry);
        outputStream.write("Test data".getBytes());
        outputStream.finish();

        byte[] expected = new byte[8 + 13 + 1];
        System.arraycopy(CpioConstants.MAGIC_NEW, 0, expected, 0, 8);
        System.arraycopy(writeNewEntryHeader(outputStream, entry).array(), 0, expected, 8, writeNewEntryHeader(outputStream, entry).array().length);
        expected[8 + 13] = 'T';
        expected[8 + 13 + 1] = 0;

        Assert.assertArrayEquals(expected, byteArrayOutputStream.toByteArray());
    }

    @Test
    public void testClose() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);

        outputStream.putNextEntry(entry);
        outputStream.write("Test data".getBytes());
        outputStream.close();

        byte[] expected = new byte[8 + 13 + 1];
        System.arraycopy(CpioConstants.MAGIC_NEW, 0, expected, 0, 8);
        System.arraycopy(writeNewEntryHeader(outputStream, entry).array(), 0, expected, 8, writeNewEntryHeader(outputStream, entry).array().length);
        expected[8 + 13] = 'T';
        expected[8 + 13 + 1] = 0;

        Assert.assertArrayEquals(expected, byteArrayOutputStream.toByteArray());
    }

    @Test
    public void testWrite() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);

        outputStream.putNextEntry(entry);

        byte[] testData = "Test data".getBytes();

        outputStream.write(testData, 0, testData.length);

        Assert.assertArrayEquals(testData, byteArrayOutputStream.toByteArray(), 8, testData.length);
    }

    private ArchiveEntry writeNewEntryHeader(CpioArchiveOutputStream outputStream, ArchiveEntry entry) throws IOException {
        byte[] header = null;
        switch (entry.getFormat()) {
            case CpioConstants.FORMAT_NEW:
                header = writeNewEntry(outputStream, entry);
                break;
            case CpioConstants.FORMAT_OLD_ASCII:
                header = writeOldAsciiEntry(outputStream, entry);
                break;
            case CpioConstants.FORMAT_OLD_BINARY:
                header = writeOldBinaryEntry(outputStream, entry);
                break;
        }
        outputStream.write(header);
        return entry;
    }

    private byte[] writeNewEntry(CpioArchiveOutputStream outputStream, ArchiveEntry entry) throws IOException {
        byte[] nameBytes = entry.getName().getBytes();
        byte[] nameLengthBytes = CpioUtil.long2byteArray(nameBytes.length + 1, 8, false);

        byte[] header = new byte[nameLengthBytes.length + 8];
        System.arraycopy(nameLengthBytes, 0, header, 0, nameLengthBytes.length);
        System.arraycopy(CpioConstants.MAGIC_NEW.getBytes(), 0, header, nameLengthBytes.length, CpioConstants.MAGIC_NEW.length);

        return header;
    }

    private byte[] writeOldAsciiEntry(CpioArchiveOutputStream outputStream, ArchiveEntry entry) throws IOException {
        byte[] nameBytes = entry.getName().getBytes();
        byte[] nameLengthBytes = CpioUtil.long2byteArray(nameBytes.length + 1, 6, false);
        byte[] header = new byte[nameLengthBytes.length + 11];
        System.arraycopy(nameLengthBytes, 0, header, 0, nameLengthBytes.length);
        System.arraycopy(nameBytes, 0, header, nameLengthBytes.length, nameBytes.length);

        return header;
    }

    private byte[] writeOldBinaryEntry(CpioArchiveOutputStream outputStream, ArchiveEntry entry) throws IOException {
        byte[] nameBytes = entry.getName().getBytes();
        byte[] nameLengthBytes = CpioUtil.long2byteArray(nameBytes.length + 1, 2, false);

        byte[] header = new byte[nameLengthBytes.length + 11];
        System.arraycopy(nameLengthBytes, 0, header, 0, nameLengthBytes.length);
        System.arraycopy(nameBytes, 0, header, nameLengthBytes.length, nameBytes.length);

        return header;
    }

    @After
    public void tearDown() throws IOException {
        outputStream.close();
        byteArrayOutputStream = null;
        byteArrayInputStream = null;
    }
}
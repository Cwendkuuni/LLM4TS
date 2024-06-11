import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for CpioArchiveOutputStream class
 */
public class CpioArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private CpioArchiveOutputStream cpioOutputStream;
    private ByteArrayInputStream inputStream;
    private Map<String, CpioArchiveEntry> entries = new HashMap<>();

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        cpioOutputStream.close();
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        cpioOutputStream = null;
        outputStream = null;
    }

    @Test
    public void testPutNextEntryAndWrite() throws IOException {
        String entryName = "testEntry";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(10);

        cpioOutputStream.putNextEntry(entry);
        byte[] testData = "Test data".getBytes();
        cpioOutputStream.write(testData);
        cpioOutputStream.closeArchiveEntry();

        byte[] expectedData = new byte[entry.getSize() + testData.length];
        System.arraycopy(testData, 0, expectedData, 0, testData.length);
        byte[] readData = new byte[entry.getSize() + testData.length];
        int bytesRead = inputStream.read(readData);

        Assert.assertEquals(bytesRead, entry.getSize() + testData.length);
        Assert.assertArrayEquals(expectedData, readData);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithDuplicateName() throws IOException {
        String entryName = "testEntry";
        CpioArchiveEntry firstEntry = new CpioArchiveEntry();
        firstEntry.setName(entryName);
        cpioOutputStream.putNextEntry(firstEntry);

        CpioArchiveEntry secondEntry = new CpioArchiveEntry();
        secondEntry.setName(entryName);
        cpioOutputStream.putNextEntry(secondEntry);
    }

    @Test
    public void testPutNextEntryWithOldFormat() throws IOException {
        String entryName = "testEntry";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setFormat(CpioConstants.FORMAT_OLD_ASCII);
        entry.setSize(10);

        cpioOutputStream.putNextEntry(entry);
        byte[] testData = "Test data".getBytes();
        cpioOutputStream.write(testData);
        cpioOutputStream.closeArchiveEntry();

        byte[] expectedData = new byte[entry.getSize() + testData.length + 1];
        System.arraycopy(testData, 0, expectedData, 0, testData.length);
        expectedData[expectedData.length - 1] = 0;
        byte[] readData = new byte[entry.getSize() + testData.length + 1];
        int bytesRead = inputStream.read(readData);

        Assert.assertEquals(bytesRead, expectedData.length);
        Assert.assertArrayEquals(expectedData, readData);

        entries.put(entryName, entry);
    }

    @Test
    public void testPutNextEntryWithNewFormatAndCRC() throws IOException {
        String entryName = "testEntry";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setFormat(CpioConstants.FORMAT_NEW_CRC);
        entry.setSize(10);

        cpioOutputStream.putNextEntry(entry);
        byte[] testData = "Test data".getBytes();
        cpioOutputStream.write(testData);
        cpioOutputStream.finish();

        byte[] expectedData = new byte[entry.getSize() + testData.length];
        System.arraycopy(testData, 0, expectedData, 0, testData.length);
        byte[] readData = new byte[entry.getSize() + testData.length];
        int bytesRead = inputStream.read(readData);

        Assert.assertEquals(bytesRead, entry.getSize() + testData.length);
        Assert.assertArrayEquals(expectedData, readData);

        byte[] crcBytes = new byte[4];
        inputStream.read(crcBytes);
        long expectedCrc = CpioUtil.crc32(expectedData);
        Assert.assertEquals(CpioUtil.bytesToLong(crcBytes), expectedCrc);

        entries.put(entryName, entry);
    }
}
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CpioArchiveOutputStreamTest {

    private static final String TEST_FILE_NAME = "testFile.cpio";
    private static final int TEST_DATA_LENGTH = 10;
    private CpioArchiveOutputStream outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private OutputStream byteArrayOutputStreamWrapper;

    @Before
    public void setUp() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStreamWrapper = new FilterOutputStream(byteArrayOutputStream);
        outputStream = new CpioArchiveOutputStream(byteArrayOutputStreamWrapper, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        outputStream.close();
    }

    @Test
    public void testPutNextEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testEntry");
        entry.setSize(TEST_DATA_LENGTH);
        outputStream.putNextEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryDuplicateEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testEntry");
        outputStream.putNextEntry(entry);
        outputStream.putNextEntry(entry);
    }

    @Test
    public void testWrite() throws IOException {
        byte[] testData = new byte[TEST_DATA_LENGTH];
        for (int i = 0; i < TEST_DATA_LENGTH; i++) {
            testData[i] = (byte) i;
        }
        outputStream.write(testData, 0, TEST_DATA_LENGTH);
        byte[] actualData = byteArrayOutputStream.toByteArray();
        byte[] expectedData = new byte[TEST_DATA_LENGTH + 1];
        System.arraycopy(testData, 0, expectedData, 0, TEST_DATA_LENGTH);
        expectedData[TEST_DATA_LENGTH] = 0;
        assertEquals(new String(expectedData), new String(actualData));
    }

    @Test
    public void testWriteMultipleEntries() throws IOException {
        for (int i = 0; i < 5; i++) {
            CpioArchiveEntry entry = new CpioArchiveEntry();
            entry.setName("testEntry" + i);
            entry.setSize(TEST_DATA_LENGTH);
            outputStream.putNextEntry(entry);
            byte[] testData = new byte[TEST_DATA_LENGTH];
            for (int j = 0; j < TEST_DATA_LENGTH; j++) {
                testData[j] = (byte) (i * TEST_DATA_LENGTH + j);
            }
            outputStream.write(testData, 0, TEST_DATA_LENGTH);
        }
        byte[] actualData = byteArrayOutputStream.toByteArray();
        byte[] expectedData = new byte[TEST_DATA_LENGTH * 5 + 5 * 11 + 1];
        int index = 0;
        for (Map.Entry<String, CpioArchiveEntry> entry : outputStream.getNames().entrySet()) {
            String name = entry.getKey();
            CpioArchiveEntry cpioArchiveEntry = entry.getValue();
            System.arraycopy(CpioUtil.long2byteArray(cpioArchiveEntry.getInode(), 8, false), 0, expectedData, index, 8);
            index += 8;
            System.arraycopy(CpioUtil.long2byteArray(cpioArchiveEntry.getMode(), 8, false), 0, expectedData, index, 8);
            index += 8;
            System.arraycopy(CpioUtil.long2byteArray(cpioArchiveEntry.getUID(), 8, false), 0, expectedData, index, 8);
            index += 8;
            System.arraycopy(CpioUtil.long2byteArray(cpioArchiveEntry.getGID(), 8, false), 0, expectedData, index, 8);
            index += 8;
            System.arraycopy(CpioUtil.long2byteArray(cpioArchiveEntry.getSize(), 8, false), 0, expectedData, index, 8);
            index += 8;
            System.arraycopy(CpioUtil.long2byteArray(cpioArchiveEntry.getTime(), 8, false), 0, expectedData, index, 8);
            index += 8;
            System.arraycopy(CpioUtil.long2byteArray(cpioArchiveEntry.getName().length() + 1, 8, false), 0, expectedData, index, 8);
            index += 8;
            System.arraycopy(cpioArchiveEntry.getName().getBytes(), 0, expectedData, index, cpioArchiveEntry.getName().length());
            index += cpioArchiveEntry.getName().length();
        }
        assertEquals(new String(expectedData), new String(actualData));
    }

    @Test(expected = IOException.class)
    public void testWriteAfterClose() throws IOException {
        outputStream.close();
        byte[] testData = new byte[TEST_DATA_LENGTH];
        outputStream.write(testData, 0, TEST_DATA_LENGTH);
    }
}
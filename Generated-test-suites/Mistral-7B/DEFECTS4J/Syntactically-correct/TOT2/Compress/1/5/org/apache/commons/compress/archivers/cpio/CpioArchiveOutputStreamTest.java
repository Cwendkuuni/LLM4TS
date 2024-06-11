import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CpioArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        cpioArchiveOutputStream.close();
    }

    @Test
    public void testPutNextEntryWithValidEntry() throws IOException {
        String entryName = "testEntry";
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(entryName);
        cpioArchiveEntry.setFormat(CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        assertEquals(MAGIC_NEW.length, outputStream.size());
        outputStream.reset();
        assertEquals(entryName.getBytes().length + 1, outputStream.read(new byte[entryName.getBytes().length + 1]));
        assertEquals(CpioConstants.FORMAT_NEW, outputStream.read(new byte[Short.BYTES]));
    }

    @Test
    public void testPutNextEntryWithDuplicateEntry() throws IOException {
        String entryName = "testEntry";
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(entryName);
        cpioArchiveEntry.setFormat(CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        assertThrows(IOException.class, () -> cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry));
    }

    @Test
    public void testWriteWithValidData() throws IOException {
        String entryName = "testEntry";
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(entryName);
        cpioArchiveEntry.setFormat(CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        byte[] testData = "Test data".getBytes();
        cpioArchiveOutputStream.write(testData, 0, testData.length);
        assertEquals(testData.length + cpioArchiveEntry.getHeaderSize(), outputStream.size());
        byte[] output = outputStream.toByteArray();
        Map<String, CpioArchiveEntry> entryMap = new HashMap<>();
        entryMap.put(entryName, cpioArchiveEntry);
        CpioArchiveEntry readEntry = CpioUtil.readEntry(output, 0, entryMap);
        byte[] readData = new byte[testData.length];
        int readBytes = outputStream.read(readData, 0, testData.length);
        assert readBytes == testData.length;
        assertEquals(new String(readData), new String(testData));
    }

    @Test(expected = IOException.class)
    public void testWriteWithInvalidOffset() throws IOException {
        String entryName = "testEntry";
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(entryName);
        cpioArchiveEntry.setFormat(CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        byte[] testData = "Test data".getBytes();
        cpioArchiveOutputStream.write(testData, 1, testData.length);
    }

    @Test(expected = IOException.class)
    public void testWriteWithInvalidLength() throws IOException {
        String entryName = "testEntry";
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(entryName);
        cpioArchiveEntry.setFormat(CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        byte[] testData = "Test data".getBytes();
        cpioArchiveOutputStream.write(testData, 0, -1);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        String entryName = "testEntry";
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(entryName);
        cpioArchiveEntry.setFormat(CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        byte[] testData = "Test data".getBytes();
        cpioArchiveOutputStream.write(testData, 0, testData.length);
        cpioArchiveOutputStream.closeArchiveEntry();
        byte[] output = outputStream.toByteArray();
        Map<String, CpioArchiveEntry> entryMap = new HashMap<>();
        entryMap.put(entryName, cpioArchiveEntry);
        CpioArchiveEntry readEntry = CpioUtil.readEntry(output, 0, entryMap);
        assertEquals(readEntry.getSize(), testData.length);
    }

    @Test(expected = IOException.class)
    public void testWriteAfterClosed() throws IOException {
        String entryName = "testEntry";
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(entryName);
        cpioArchiveEntry.setFormat(CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        cpioArchiveOutputStream.write(new byte[1], 0, 1);
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.write(new byte[1], 0, 1);
    }

    @Test
    public void testFinish() throws IOException {
        String entryName = "testEntry";
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(entryName);
        cpioArchiveEntry.setFormat(CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        byte[] testData = "Test data".getBytes();
        cpioArchiveOutputStream.write(testData, 0, testData.length);
        cpioArchiveOutputStream.finish();
        byte[] output = outputStream.toByteArray();
        Map<String, CpioArchiveEntry> entryMap = new HashMap<>();
        entryMap.put(entryName, cpioArchiveEntry);
        CpioArchiveEntry readEntry = CpioUtil.readEntry(output, 0, entryMap);
        assertEquals(readEntry.getSize(), testData.length);
        byte[] trailerData = new byte[CpioConstants.TRAILER_LENGTH];
        int readBytes = outputStream.read(trailerData, 0, trailerData.length);
        assertEquals(trailerData.length, readBytes);
    }

    @Test(expected = IOException.class)
    public void testFinishTwice() throws IOException {
        String entryName = "testEntry";
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(entryName);
        cpioArchiveEntry.setFormat(CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.finish();
    }
}
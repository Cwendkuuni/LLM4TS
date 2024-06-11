import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream outputStream;
    private ByteArrayOutputStream outputStreamBuffer;
    private Map<String, CpioArchiveEntry> entries;

    @Before
    public void setUp() throws IOException {
        outputStreamBuffer = new ByteArrayOutputStream();
        outputStream = new CpioArchiveOutputStream(outputStreamBuffer, CpioConstants.FORMAT_NEW);
        entries = new HashMap<>();
    }

    @After
    public void tearDown() throws IOException {
        if (outputStream != null) {
            outputStream.close();
        }
    }

    @Test
    public void testPutNextEntryWithValidEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        outputStream.putNextEntry(entry);
        assertNotNull(entry.getHeader());
        assertEquals(entry.getFormat(), CpioConstants.FORMAT_NEW);
        assertEquals(outputStreamBuffer.size(), entry.getHeader().length + 10);
    }

    @Test
    public void testPutNextEntryWithInvalidEntryFormat() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(-1);
        assertThrows(IllegalArgumentException.class, () -> outputStream.putNextEntry(entry));
    }

    @Test
    public void testPutNextEntryWithDuplicateEntryName() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile");
        outputStream.putNextEntry(entry1);
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile");
        assertThrows(IOException.class, () -> outputStream.putNextEntry(entry2));
    }

    @Test
    public void testWriteBytes() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        outputStream.putNextEntry(entry);
        byte[] data = "data".getBytes(StandardCharsets.UTF_8);
        outputStream.write(data, 0, data.length);
        byte[] result = outputStreamBuffer.toByteArray();
        assertEquals(data.length + entry.getHeader().length, result.length);
        assertTrue(CpioUtil.bytesEqual(entry.getHeader(), result, 0, entry.getHeader().length));
        assertTrue(CpioUtil.bytesEqual(data, result, entry.getHeader().length, data.length));
    }

    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile1");
        entry1.setFileSize(10);
        outputStream.putNextEntry(entry1);
        byte[] data1 = "data1".getBytes(StandardCharsets.UTF_8);
        outputStream.write(data1, 0, data1.length);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile2");
        entry2.setFileSize(11);
        outputStream.putNextEntry(entry2);
        byte[] data2 = "data2".getBytes(StandardCharsets.UTF_8);
        outputStream.write(data2, 0, data2.length);

        outputStream.finish();

        byte[] result = outputStreamBuffer.toByteArray();
        CpioArchiveEntry trailer = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        trailer.setName("TRAILER!!!");
        trailer.setNumberOfLinks(1);
        long trailerHeaderSize = trailer.getHeader().length;
        byte[] trailerHeader = trailer.getHeader();

        assertEquals(trailerHeaderSize + result.length, outputStreamBuffer.size());
        assertTrue(CpioUtil.bytesEqual(trailerHeader, result, result.length - trailerHeaderSize, trailerHeaderSize));
    }

    @Test(expected = IOException.class)
    public void testWriteBytesAfterFinish() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        outputStream.putNextEntry(entry);
        byte[] data = "data".getBytes(StandardCharsets.UTF_8);
        outputStream.write(data, 0, data.length);
        outputStream.finish();
        outputStream.write(data, 0, data.length);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        outputStream.putNextEntry(entry);
        byte[] data = "data".getBytes(StandardCharsets.UTF_8);
        outputStream.write(data, 0, data.length);
        outputStream.closeArchiveEntry();
        byte[] result = outputStreamBuffer.toByteArray();
        assertEquals(entry.getHeader().length + data.length, result.length);
        assertTrue(CpioUtil.bytesEqual(entry.getHeader(), result, 0, entry.getHeader().length));
        assertTrue(CpioUtil.bytesEqual(data, result, entry.getHeader().length, data.length));
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryTwice() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        outputStream.putNextEntry(entry);
        byte[] data = "data".getBytes(StandardCharsets.UTF_8);
        outputStream.write(data, 0, data.length);
        outputStream.closeArchiveEntry();
        outputStream.closeArchiveEntry();
    }

    @Test
    public void testCloseArchiveEntryWithoutData() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        outputStream.putNextEntry(entry);
        outputStream.closeArchiveEntry();
        byte[] result = outputStreamBuffer.toByteArray();
        assertEquals(entry.getHeader().length, result.length);
        assertTrue(CpioUtil.bytesEqual(entry.getHeader(), result, 0, entry.getHeader().length));
    }

    @Test
    public void testCloseArchiveStream() throws IOException {
        outputStream.close();
        byte[] result = outputStreamBuffer.toByteArray();
        assertTrue(result.length == 0);
    }
}
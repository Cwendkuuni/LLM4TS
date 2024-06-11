import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CpioArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;
    private Map<String, CpioArchiveEntry> names;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        names = new HashMap<>();
    }

    @After
    public void tearDown() throws Exception {
        if (cpioArchiveOutputStream != null) {
            cpioArchiveOutputStream.close();
        }
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        entry.setSize(10);
        cpioArchiveOutputStream.putArchiveEntry(entry);
        assertNotNull(entry.getName(), cpioArchiveOutputStream.getNextEntry());
        assertEquals("test.txt", cpioArchiveOutputStream.getNextEntry().getName());
        assertEquals(1, names.size());
        assertEquals(entry, names.values().iterator().next());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryDuplicateEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        CpioArchiveOutputStream cpioArchiveOutputStreamWithInvalidFormat = new CpioArchiveOutputStream(outputStream, (short) 123);
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        cpioArchiveOutputStreamWithInvalidFormat.putArchiveEntry(entry);
    }

    @Test
    public void testWrite() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        entry.setSize(10);
        cpioArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "Hello World!".getBytes();
        cpioArchiveOutputStream.write(data, 0, data.length);
        assertEquals(data.length, cpioArchiveOutputStream.getNextEntry().getSize());
    }

    @Test
    public void testWriteWithInvalidOffset() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        cpioArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "Hello World!".getBytes();
        cpioArchiveOutputStream.write(data, 10, data.length - 10);
    }

    @Test
    public void testWriteWithInvalidLength() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        cpioArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "Hello World!".getBytes();
        cpioArchiveOutputStream.write(data, 0, data.length + 1);
    }

    @Test
    public void testWriteEmptyData() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(new byte[0], 0, 0);
    }

    @Test
    public void testWriteWithZeroSizeEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        entry.setSize(0);
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(new byte[0], 0, 0);
        cpioArchiveOutputStream.close();
        assertNull(cpioArchiveOutputStream.getNextEntry());
    }

    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        cpioArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "Hello World!".getBytes();
        cpioArchiveOutputStream.write(data, 0, data.length);
        cpioArchiveOutputStream.finish();
        assertEquals( ArchiveUtils.getFileSize(outputStream.toByteArray()), entry.getSize() + data.length);
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.putArchiveEntry(entry);
    }
}
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    private static final String TEST_FILE_NAME = "test.cpio";
    private CpioArchiveOutputStream outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private ByteArrayInputStream byteArrayInputStream;

    @Before
    public void setUp() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_NEW);
    }

    @Test
    public void testPutArchiveEntry_ValidEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        String contents = "12345";
        entry.setFileSize(contents.length());
        entry.setMode(CpioConstants.C_ISREG);
        outputStream.putArchiveEntry(entry);
        outputStream.write(contents.getBytes(StandardCharsets.UTF_8));
        outputStream.closeArchiveEntry();
        outputStream.close();

        Assert.assertArrayEquals(ArchiveUtils.readFile(new File(TEST_FILE_NAME)), byteArrayOutputStream.toByteArray());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntry_DuplicateEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        outputStream.putArchiveEntry(entry);
        outputStream.closeArchiveEntry();

        outputStream.putArchiveEntry(entry);
    }

    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntry_InvalidEntryType() throws IOException {
        ArchiveEntry invalidEntry = new File("test.txt");
        outputStream.putArchiveEntry(invalidEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntry_InvalidFormat() throws IOException {
        outputStream = new CpioArchiveOutputStream(byteArrayOutputStream, (short) 0xFF);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        outputStream.putArchiveEntry(entry);
    }

    @Test
    public void testWrite_ValidData() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        byte[] data = "Test Data".getBytes(StandardCharsets.UTF_8);
        entry.setFileSize(data.length);
        outputStream.putArchiveEntry(entry);
        outputStream.write(data);
        outputStream.closeArchiveEntry();
        outputStream.close();

        byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        byte[] readData = new byte[data.length];
        int length = byteArrayInputStream.read(readData, 0, data.length);
        Assert.assertArrayEquals(data, readData);
    }

    @Test(expected = IOException.class)
    public void testWrite_InvalidDataLength() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        outputStream.putArchiveEntry(entry);
        outputStream.write(new byte[65536 + 1]);
    }

    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        outputStream.putArchiveEntry(entry);
        outputStream.write("Test Data".getBytes(StandardCharsets.UTF_8));
        outputStream.finish();
        outputStream.close();

        Assert.assertArrayEquals(CpioUtil.readTrailer(new File(TEST_FILE_NAME)), outputStream.getHeaderTrailer());
    }

    @Test(expected = IOException.class)
    public void testFinish_InvalidFormat() throws IOException {
        outputStream = new CpioArchiveOutputStream(byteArrayOutputStream, (short) 0xFF);
        outputStream.putArchiveEntry(new CpioArchiveEntry());
        outputStream.write("Test Data".getBytes(StandardCharsets.UTF_8));
        outputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        outputStream.close();
        outputStream.write("Test Data".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        outputStream.putArchiveEntry(entry);
        outputStream.write("Test Data".getBytes(StandardCharsets.UTF_8));
        outputStream.closeArchiveEntry();

        Map<String, CpioArchiveEntry> actualEntries = new HashMap<>();
        actualEntries.put(entry.getName(), entry);
        Assert.assertEquals(actualEntries, outputStream.getEntries());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("test.txt");
        CpioArchiveEntry entry = outputStream.createArchiveEntry(inputFile, "testfile");

        Assert.assertEquals("testfile", entry.getName());
        Assert.assertEquals(CpioConstants.C_ISREG, entry.getMode());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateArchiveEntry_InvalidFile() throws IOException {
        outputStream.createArchiveEntry(new File("test.nonexistent"), "testfile");
    }
}
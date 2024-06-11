import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioUtil;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CpioArchiveOutputStreamTest {

    private File testFile;
    private ByteArrayOutputStream out;
    private CpioArchiveOutputStream cpioOut;
    private Map<String, CpioArchiveEntry> entries = new HashMap<>();

    @Before
    public void setup() throws IOException {
        testFile = File.createTempFile("test", ".cpio");
        out = new ByteArrayOutputStream();
        cpioOut = new CpioArchiveOutputStream(new FileOutputStream(testFile), CpioConstants.FORMAT_NEW);
    }

    @After
    public void cleanup() throws IOException {
        cpioOut.close();
        testFile.delete();
    }

    @Test
    public void testCreateAndWriteEntry() throws IOException {
        String entryName = "testEntry.txt";
        byte[] testData = "Hello, World!".getBytes();
        CpioArchiveEntry entry = new CpioArchiveEntry("testEntry.txt");
        entry.setName(entryName);
        entry.setSize(testData.length);

        cpioOut.putArchiveEntry(entry);
        cpioOut.write(testData, 0, testData.length);
        cpioOut.closeArchiveEntry();

        assertEquals(testData.length, cpioOut.written);
        assertEquals(1, entries.size());
        CpioArchiveEntry readEntry = entries.get(entryName);
        assertEquals(CpioConstants.FORMAT_NEW, readEntry.getFormat());
    }

    @Test(expected = ClassCastException.class)
    public void testInvalidEntryType() throws IOException {
        ArchiveEntry invalidEntry = new File("test.txt");
        cpioOut.putArchiveEntry(invalidEntry);
    }

    @Test(expected = IOException.class)
    public void testDuplicateEntry() throws IOException {
        String entryName = "testEntry.txt";
        byte[] testData = "Hello, World!".getBytes();
        CpioArchiveEntry entry = new CpioArchiveEntry("testEntry.txt");
        entry.setName(entryName);
        entry.setSize(testData.length);

        cpioOut.putArchiveEntry(entry);
        cpioOut.write(testData, 0, testData.length);
        cpioOut.closeArchiveEntry();

        cpioOut.putArchiveEntry(entry);
    }

    @Test
    public void testWriteBinaryEntry() throws IOException {
        String entryName = "testBinaryEntry.bin";
        byte[] testData = {0x01, 0x02, 0x03, 0x04};
        CpioArchiveEntry entry = new CpioArchiveEntry("testBinaryEntry.bin");
        entry.setName(entryName);
        entry.setSize(testData.length);
        entry.setFormat(CpioConstants.FORMAT_OLD_BINARY);

        cpioOut.putArchiveEntry(entry);
        cpioOut.write(testData, 0, testData.length);
        cpioOut.closeArchiveEntry();

        CpioArchiveEntry readEntry = entries.get(entryName);
        assertEquals(CpioConstants.FORMAT_OLD_BINARY, readEntry.getFormat());

        byte[] readData = new byte[testData.length];
        cpioOut.read(readData, 0, testData.length);
        assertEquals(testData, readData);
    }

    @Test
    public void testWriteCrcEntry() throws IOException {
        String entryName = "testCrcEntry.txt";
        byte[] testData = "Hello, World!".getBytes();
        CpioArchiveEntry entry = new CpioArchiveEntry("testCrcEntry.txt");
        entry.setName(entryName);
        entry.setSize(testData.length);
        entry.setFormat(CpioConstants.FORMAT_NEW_CRC);

        cpioOut.putArchiveEntry(entry);
        cpioOut.write(testData, 0, testData.length);
        cpioOut.closeArchiveEntry();

        CpioArchiveEntry readEntry = entries.get(entryName);
        assertEquals(CpioConstants.FORMAT_NEW_CRC, readEntry.getFormat());

        byte[] readData = new byte[testData.length];
        cpioOut.read(readData, 0, testData.length);
        assertEquals(testData, readData);

        byte[] crc = new byte[4];
        cpioOut.read(crc, 0, 4);
        byte[] expectedCrc = CpioUtil.byteArrayToLong(testData, true).getIntBits();
        assertEquals(expectedCrc, CpioUtil.byteArrayToLong(crc, true).getIntBits());
    }

    @Test
    public void testWriteTrailer() throws IOException {
        cpioOut.finish();

        byte[] trailer = new byte[ArchiveUtils.TRAILER_LENGTH];
        cpioOut.read(trailer, 0, ArchiveUtils.TRAILER_LENGTH);
        assertEquals(ArchiveUtils.TRAILER_MAGIC, CpioUtil.bytesToShort(trailer, 0));
    }
}
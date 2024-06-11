import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.compress.archivers.dump.DumpArchiveConstants.*;

public class DumpArchiveInputStreamTest {

    private InputStream inputStream;
    private DumpArchiveInputStream dumpArchiveInputStream;

    @Before
    public void setup() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] headerBytes = DumpArchiveUtil.createHeaderBytes(new byte[TP_SIZE], "test.dmp");
        outputStream.write(headerBytes);
        outputStream.write(DumpArchiveEntry.createCLRIHeaderBytes(TP_SIZE));
        outputStream.write(DumpArchiveEntry.createBITSHeaderBytes(TP_SIZE));
        outputStream.write(DumpArchiveEntry.createFileEntryBytes(TP_SIZE, 1, "test.txt", 10));
        outputStream.write(DumpArchiveEntry.createDirectoryEntryBytes(TP_SIZE, 2, "test"));
        outputStream.write(DumpArchiveConstants.TP_SIZE);
        outputStream.write(TP_SIZE);
        outputStream.close();

        byte[] testData = outputStream.toByteArray();
        inputStream = new ByteArrayInputStream(testData);
        dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, null);
    }

    @Test
    public void testGetSummary() throws IOException {
        DumpArchiveSummary summary = dumpArchiveInputStream.getSummary();
        Assert.assertEquals(1, summary.getEntryCount());
        Assert.assertEquals("test.dmp", summary.getName());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        DumpArchiveEntry entry = dumpArchiveInputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertEquals(1, entry.getIno());
        Assert.assertEquals(10, entry.getSize());
        Assert.assertEquals(0, entry.getOffset());

        entry = dumpArchiveInputStream.getNextEntry();
        Assert.assertNull(entry);

        dumpArchiveInputStream.close();
        entry = dumpArchiveInputStream.getNextEntry();
        Assert.assertNull(entry);
    }

    @Test
    public void testRead() throws IOException {
        final byte[] testData = new byte[10];
        int bytesRead = dumpArchiveInputStream.read(testData, 0, testData.length);
        Assert.assertEquals(testData.length, bytesRead);
        Assert.assertArrayEquals(testData, testData);

        bytesRead = dumpArchiveInputStream.read(testData, 0, testData.length * 2);
        Assert.assertEquals(-1, bytesRead);
    }

    @Test(expected = ArchiveException.class)
    public void testInvalidFormatException() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[]{"X"});
        DumpArchiveInputStream invalidDumpArchiveInputStream = new DumpArchiveInputStream(inputStream, null);
        invalidDumpArchiveInputStream.getNextEntry();
    }

    @Test(expected = EOFException.class)
    public void testEOFException() throws IOException {
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(
                new ByteArrayInputStream(new byte[TP_SIZE]), null);
        dumpArchiveInputStream.getNextEntry();
        dumpArchiveInputStream.getNextEntry();
        dumpArchiveInputStream.getNextEntry();
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(
                new ByteArrayInputStream(new byte[TP_SIZE]), null);
        dumpArchiveInputStream.getNextEntry();
        dumpArchiveInputStream.getNextEntry();
        dumpArchiveInputStream.getNextEntry();
        dumpArchiveInputStream.getNextEntry();

        InputStream inputStream = new ByteArrayInputStream(new byte[TP_SIZE]);
        dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, null);
    }

    @Test
    public void testGetPath() throws IOException {
        List<String> expectedElements = Arrays.asList("test", "test.txt");
        Assert.assertEquals(expectedElements.toString(),
                dumpArchiveInputStream.getPath(dumpArchiveInputStream.getNextEntry()));
    }
}
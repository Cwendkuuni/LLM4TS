import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Test cases for {@link DumpArchiveInputStream}.
 */
public class DumpArchiveInputStreamTest {

    private static final String TEST_FILE = "test-file.dump";
    private static final int TEST_FILE_SIZE = 1024;
    private static final int TEST_HEADER_SIZE = 32;
    private static final int TEST_BLOCK_SIZE = 4 * TEST_TP_SIZE;
    private static final int TEST_TP_SIZE = 512;

    private DumpArchiveInputStream inputStream;
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream testInputStream;

    @Before
    public void setUp() throws IOException {
        // Create test data
        byte[] testData = new byte[TEST_FILE_SIZE];
        Arrays.fill(testData, (byte) 'A');

        byte[] header = new byte[TEST_HEADER_SIZE];
        byte[] magic = new byte[4];
        System.arraycopy(DumpArchiveConstants.NFS_MAGIC, 0, magic, 0, 4);
        System.arraycopy(magic, 0, header, 24, 4);

        outputStream = new ByteArrayOutputStream();
        IOUtils.write(header, outputStream);
        IOUtils.write(testData, outputStream, TEST_TP_SIZE);
        byte[] testFileBytes = outputStream.toByteArray();

        testInputStream = new ByteArrayInputStream(testFileBytes);

        // Create test input stream
        inputStream = new DumpArchiveInputStream(testInputStream, null);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        // Test getting next entry
        DumpArchiveEntry firstEntry = inputStream.getNextEntry();
        Assert.assertNotNull(firstEntry);
        Assert.assertEquals("test-file.dump", firstEntry.getName());
        Assert.assertEquals(TEST_FILE_SIZE, firstEntry.getSize());

        DumpArchiveEntry secondEntry = inputStream.getNextEntry();
        Assert.assertNull(secondEntry);
    }

    @Test(expected = IOException.class)
    public void testExceptionOnMalformedHeader() throws IOException {
        // Test exception on malformed header
        ByteArrayInputStream malformedTestInputStream = new ByteArrayInputStream(
                new byte[]{"A", "B"});
        DumpArchiveInputStream malformedInputStream = new DumpArchiveInputStream(
                malformedTestInputStream, null);
        malformedInputStream.getNextEntry();
    }

    @Test(expected = IOException.class)
    public void testExceptionOnEOF() throws IOException {
        // Test exception on EOF
        DumpArchiveInputStream eofInputStream = new DumpArchiveInputStream(
                new ByteArrayInputStream(new byte[0]), null);
        eofInputStream.getNextEntry();
        eofInputStream.getNextEntry();
    }

    @Test
    public void testReadData() throws IOException {
        // Test reading data
        byte[] readBuffer = new byte[TEST_TP_SIZE];
        int bytesRead = inputStream.read(readBuffer);
        Assert.assertEquals(TEST_TP_SIZE, bytesRead);
        Assert.assertArrayEquals(testData, Arrays.copyOfRange(testData, 0, TEST_TP_SIZE));

        bytesRead = inputStream.read(readBuffer);
        Assert.assertEquals(TEST_TP_SIZE, bytesRead);
        Assert.assertArrayEquals(
                Arrays.copyOfRange(testData, TEST_TP_SIZE, TEST_TP_SIZE * 2),
                readBuffer);

        // Test reading the rest of the file
        byte[] remainingData = new byte[TEST_FILE_SIZE - TEST_TP_SIZE * 2];
        while (inputStream.read(remainingData) != -1) {}
        Assert.assertArrayEquals(Arrays.copyOfRange(testData, TEST_TP_SIZE * 2, TEST_FILE_SIZE),
                remainingData);
    }

    @Test
    public void testGetSummary() throws IOException {
        // Test getting the summary
        DumpArchiveSummary summary = inputStream.getSummary();
        Assert.assertEquals(TEST_FILE_SIZE, summary.getSize());
        Assert.assertEquals(1, summary.getEntries().size());
        DumpArchiveEntry summaryEntry = summary.getEntries().get(0);
        Assert.assertEquals("test-file.dump", summaryEntry.getName());
        Assert.assertEquals(TEST_FILE_SIZE, summaryEntry.getSize());
    }

    @Test(expected = IOException.class)
    public void testExceptionOnInvalidFileFormat() throws IOException {
        // Test exception on an invalid file format
        ByteArrayInputStream invalidTestInputStream = new ByteArrayInputStream(
                new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
                        15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26});
        DumpArchiveInputStream invalidInputStream = new DumpArchiveInputStream(
                invalidTestInputStream, null);
        invalidInputStream.getNextEntry();
    }

    @Test
    public void testGetSummaryFromMalformedFile() throws IOException {
        // Test getting the summary from a malformed file
        ByteArrayInputStream malformedTestInputStream = new ByteArrayInputStream(
                new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
                        15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27});
        DumpArchiveInputStream malformedInputStream = new DumpArchiveInputStream(
                malformedTestInputStream, null);
        DumpArchiveSummary malformedSummary = malformedInputStream.getSummary();
        List<DumpArchiveEntry> malformedEntries = malformedSummary.getEntries();
        Assert.assertEquals(0, malformedEntries.size());
    }

    @Test
    public void testZipEncoding() throws IOException {
        // Test reading with a custom encoding
        ZipEncoding customEncoding = ZipEncodingHelper.getZipEncoding("UTF-8");
        DumpArchiveInputStream customInputStream = new DumpArchiveInputStream(
                testInputStream, customEncoding.getName());
        DumpArchiveEntry customEntry = customInputStream.getNextEntry();
        Assert.assertEquals("test-file.dump", customEntry.getName());

        String customEntryName = customEntry.getName();
        Assert.assertEquals("test-file.dump", customEntryName);
        Assert.assertEquals("test-file.dump", customEntry.getSimpleName());

        // Test reading with the default encoding
        DumpArchiveEntry defaultEntry = inputStream.getNextEntry();
        String defaultEntryName = defaultEntry.getName();
        Assert.assertEquals("test-file.dump", defaultEntryName);
        Assert.assertEquals("test-file.dump", defaultEntry.getSimpleName());
    }
}
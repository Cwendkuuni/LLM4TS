import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Test class for DumpArchiveInputStream
 */
public class DumpArchiveInputStreamTest {

    private static final String TEST_FILE = "testFile.dump";
    private static final long TEST_SIZE = 1024L;
    private static final int TEST_COUNT = 5;

    private DumpArchiveInputStream inputStream;
    private byte[] testData;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        // Prepare test data
        outputStream = new ByteArrayOutputStream(TEST_SIZE);
        testData = new byte[TEST_SIZE];

        for (int i = 0; i < TEST_SIZE; i++) {
            testData[i] = (byte) i;
            outputStream.write(i);
        }

        outputStream.flush();
        byte[] header = new byte[DumpArchiveConstants.TP_SIZE];
        System.arraycopy(DumpArchiveUtil.MAGIC, 0, header, 0, DumpArchiveUtil.MAGIC.length);
        byte[] summaryData = DumpArchiveSummary.createSummaryData(TEST_COUNT, TEST_SIZE);
        System.arraycopy(summaryData, 0, header, DumpArchiveUtil.MAGIC.length, summaryData.length);

        // Create input stream with test data
        ByteArrayInputStream inputStream = new ByteArrayInputStream(header);
        inputStream.read(testData, 0, header.length);
        this.inputStream = new DumpArchiveInputStream(inputStream, null);
    }

    /**
     * Test method for {@link DumpArchiveInputStream#getNextEntry()}
     */
    @Test
    public void testGetNextEntry() throws IOException, InterruptedException {
        DumpArchiveEntry entry;
        int i = 0;

        // Test normal usage
        for (i = 0; i < TEST_COUNT; i++) {
            entry = inputStream.getNextEntry();
            Assert.assertNotNull(entry);
            Assert.assertEquals(TEST_SIZE, entry.getEntrySize());
            Assert.assertEquals(TEST_SIZE, inputStream.read(testData, 0, TEST_SIZE));
            Assert.assertEquals(TEST_DATA, new String(testData, 0, TEST_SIZE));
            inputStream.closeEntry();
        }

        // Test end of archive
        entry = inputStream.getNextEntry();
        Assert.assertNull(entry);
    }

    /**
     * Test method for {@link DumpArchiveInputStream#getNextEntry()} with empty archive
     */
    @Test(expected = ArchiveException.class)
    public void testGetNextEntryEmptyArchive() throws IOException, InterruptedException {
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(new byte[0]), null);
        DumpArchiveEntry entry = inputStream.getNextEntry();
        Assert.assertNull(entry);
        inputStream.getNextEntry();
    }

    /**
     * Test method for {@link DumpArchiveInputStream#getNextEntry()} with invalid archive
     */
    @Test(expected = InvalidFormatException.class)
    public void testGetNextEntryInvalidArchive() throws IOException {
        byte[] invalidHeader = new byte[DumpArchiveConstants.TP_SIZE];
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(invalidHeader), null);
        inputStream.getNextEntry();
    }

    /**
     * Test method for {@link DumpArchiveInputStream#close()}
     */
    @Test
    public void testClose() throws IOException {
        inputStream.close();
        Assert.assertTrue(inputStream.isClosed());
    }

    /**
     * Test method for {@link DumpArchiveInputStream#close()} with empty archive
     */
    @Test
    public void testCloseEmptyArchive() throws IOException {
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(new byte[0]), null);
        inputStream.close();
        Assert.assertTrue(inputStream.isClosed());
    }

    /**
     * Test method for {@link DumpArchiveInputStream#read(byte[], int, int)}
     */
    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[TEST_SIZE];
        int read = inputStream.read(buffer, 0, TEST_SIZE);
        Assert.assertEquals(TEST_SIZE, read);
        Assert.assertArrayEquals(TEST_DATA, Arrays.copyOfRange(buffer, 0, TEST_SIZE));
    }

    /**
     * Test method for {@link DumpArchiveInputStream#read(byte[], int, int)} with empty buffer
     */
    @Test
    public void testReadEmptyBuffer() throws IOException {
        byte[] buffer = new byte[0];
        int read = inputStream.read(buffer, 0, TEST_SIZE);
        Assert.assertEquals(0, read);
    }

    /**
     * Test method for {@link DumpArchiveInputStream#read(byte[], int, int)} with zero length
     */
    @Test
    public void testReadZeroLength() throws IOException {
        byte[] buffer = new byte[TEST_SIZE];
        int read = inputStream.read(buffer, TEST_SIZE, TEST_SIZE);
        Assert.assertEquals(0, read);
    }

    /**
     * Test method for {@link DumpArchiveInputStream#matches(byte[], int)}
     */
    @Test
    public void testMatches() {
        Assert.assertTrue(DumpArchiveInputStream.matches(testData, TEST_SIZE));
    }

    /**
     * Test method for {@link DumpArchiveInputStream#matches(byte[], int)} with invalid data
     */
    @Test
    public void testMatchesInvalidData() {
        byte[] invalidData = new byte[TEST_SIZE];
        Arrays.fill(invalidData, (byte) 0);
        Assert.assertFalse(DumpArchiveInputStream.matches(invalidData, TEST_SIZE));
    }
}
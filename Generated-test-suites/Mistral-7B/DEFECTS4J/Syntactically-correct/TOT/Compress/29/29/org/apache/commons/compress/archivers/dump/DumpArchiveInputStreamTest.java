import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;

import static org.apache.commons.compress.archivers.dump.DumpArchiveConstants.*;

public class DumpArchiveInputStreamTest {

    private DumpArchiveInputStream inputStream;
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream testData;

    @Before
    public void setup() throws IOException {
        // Setup test data
        outputStream = new ByteArrayOutputStream();
        testData = new ByteArrayInputStream(createTestData());

        // Create test input stream
        inputStream = new DumpArchiveInputStream(testData, null);
    }

    /**
     * Test that the getNextEntry method returns expected values
     */
    @Test
    public void testGetNextEntry() throws IOException {
        DumpArchiveEntry entry = inputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("testFile1", entry.getName());
        Assert.assertEquals("testDir/testFile1", entry.getFullName());
        Assert.assertFalse(entry.isDirectory());

        entry = inputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("testDir", entry.getName());
        Assert.assertEquals(".", entry.getFullName());
        Assert.assertTrue(entry.isDirectory());

        entry = inputStream.getNextEntry();
        Assert.assertNull(entry);
    }

    /**
     * Test that the read method works correctly
     */
    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[TP_SIZE];
        int bytesRead;

        // Read first entry
        bytesRead = inputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(TP_SIZE, bytesRead);
        Assert.assertArrayEquals(createExpectedMagicBytes(), buffer, 0, TP_SIZE);

        // Read second entry
        bytesRead = inputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(TP_SIZE, bytesRead);
        Assert.assertArrayEquals(createExpectedDirectoryBytes(), buffer, 0, TP_SIZE);

        // Read data from first file
        bytesRead = inputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(TP_SIZE, bytesRead);
        Assert.assertArrayEquals(createExpectedFileDataBytes(), buffer, 0, TP_SIZE);

        // Read EOF
        bytesRead = inputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, bytesRead);

        // Verify that input stream is closed
        Assert.assertTrue(inputStream.isClosed());
    }

    /**
     * Test handling of EOFException
     */
    @Test(expected = EOFException.class)
    public void testReadException() throws IOException {
        inputStream.read(new byte[TP_SIZE], 0, TP_SIZE);
        inputStream.getNextEntry();
        inputStream.read(new byte[TP_SIZE], 0, TP_SIZE);
        inputStream.getNextEntry();
        inputStream.read(new byte[TP_SIZE], 0, TP_SIZE);
    }

    /**
     * Test that an empty archive is handled correctly
     */
    @Test
    public void testEmptyArchive() throws IOException {
        testData = new ByteArrayInputStream(new byte[0]);
        inputStream = new DumpArchiveInputStream(testData, null);
        DumpArchiveEntry entry = inputStream.getNextEntry();
        Assert.assertNull(entry);
        Assert.assertTrue(inputStream.isClosed());
    }

    /**
     * Test that an invalid archive is handled correctly
     */
    @Test(expected = InvalidFormatException.class)
    public void testInvalidArchive() throws IOException {
        // Setup test data with invalid magic value
        testData = new ByteArrayInputStream(new byte[]{-1, -60, -66, -6D, 73,
                73, 73, 73, 73, 73, 73, 73, 73, 73, 73, 73, 1, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 1, 0, 0, 0});
        inputStream = new DumpArchiveInputStream(testData, null);
    }

    /**
     * Creates test data for the test
     *
     * @return test data as a byte array
     */
    private byte[] createTestData() {
        ByteArrayOutputStream testStream = new ByteArrayOutputStream();
        ByteArrayOutputStream fileStream = new ByteArrayOutputStream();

        // Write test magic value
        testStream.write(TP_MAGIC_NUMBER);

        // Write directory entry
        testStream.write(INODE_MAGIC);
        testStream.write(DIR);
        testStream.write(2);
        testStream.write(2);
        testStream.write(10);
        testStream.write(0);
        testStream.write(4);
        testStream.write(0);
        testStream.write(0);
        testStream.write(11);
        testStream.write('.');
        testStream.write('.');
        testStream.write('.');
        testStream.write('\0');
        testStream.write(0);
        testStream.write(10);
        testStream.write('t'.byteValue());
        testStream.write('e').write('s'.byteValue());
        testStream.write('t'.byteValue());
        testStream.write('D'.byteValue());
        testStream.write('i'.byteValue());
        testStream.write('r'.byteValue());
        testStream.write('/').write('t'.byteValue());
        testStream.write('e').write('s'.byteValue());
        testStream.write('t'.byteValue());
        testStream.write('F'.byteValue());
        testStream.write('i'.byteValue());
        testStream.write('l'.byteValue());
        testStream.write('e'.byteValue());
        testStream.write('1'.byteValue());
        testStream.write('\0');

        // Write file entry
        testStream.write(INODE_MAGIC);
        testStream.write(FILE);
        testStream.write(2);
        testStream.write(3);
        testStream.write(10);
        testStream.write(0);
        testStream.write(1);
        testStream.write(0);
        testStream.write(TP_SIZE);
        testStream.write(11);
        testStream.write('t'.byteValue());
        testStream.write('e'.byteValue());
        testStream.write('s'.byteValue());
        testStream.write('t'.byteValue());
        testStream.write('F'.byteValue());
        testStream.write('i'.byteValue());
        testStream.write('l'.byteValue());
        testStream.write('e'.byteValue());
        testStream.write('1'.byteValue());
        testStream.write('\0');
        testStream.write(TP_SIZE);

        // Write file data
        fileStream.write("This is a test file".getBytes());
        testStream.write(fileStream.size());

        // Write test data to test stream
        testStream.write(TP_SIZE);
        testStream.write(fileStream.toByteArray(), 0, fileStream.size());

        // Write checksum
        testStream.write(DumpArchiveUtil.checksum(testStream.toByteArray(),
                TP_SIZE - 8));

        // Write end marker
        testStream.write(END);

        return testStream.toByteArray();
    }

    /**
     * Returns expected magic bytes
     *
     * @return expected magic bytes as an array of bytes
     */
    private byte[] createExpectedMagicBytes() {
        return new byte[]{TP_MAGIC_NUMBER};
    }

    /**
     * Returns expected directory bytes
     *
     * @return expected directory bytes as an array of bytes
     */
    private byte[] createExpectedDirectoryBytes() {
        return new byte[]{INODE_MAGIC, DIR, 2, 3, 10, 0, 4, 0, 0, 11, 0, 0,
                10, '.', '.', '.', '\0', 0, 10, 't'.byteValue(), 'e'.byteValue(),
                's'.byteValue(), 't'.byteValue(), 'D'.byteValue(), 'i'.byteValue(),
                'r'.byteValue(), '/', 't'.byteValue(), 'e'.byteValue(), 's'.byteValue(),
                't'.byteValue(), 'F'.byteValue(), 'i'.byteValue(), 'l'.byteValue(),
                'e'.byteValue(), '1'.byteValue(), '\0'};
    }

    /**
     * Returns expected file data bytes
     *
     * @return expected file data bytes as an array of bytes
     */
    private byte[] createExpectedFileDataBytes() {
        return new byte[]{"This is a test file".getBytes()};
    }

    /**
     * Closes all test streams
     */
    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(outputStream);
        IOUtils.closeQuietly(testData);
        IOUtils.closeQuietly(inputStream);
    }
}
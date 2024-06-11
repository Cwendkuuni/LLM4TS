import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * TarArchiveOutputStreamTest class.
 */
public class TarArchiveOutputStreamTest {

    private OutputStream out;
    private TarOutputStream tarOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    /**
     * Sets up the test environment.
     *
     * @throws Exception if an error occurs during setup
     */
    @Before
    public void setUp() throws Exception {
        out = new ByteArrayOutputStream();
        byteArrayOutputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(out, 512, 512);
    }

    /**
     * Cleans up the test environment.
     *
     * @throws IOException if an error occurs during cleanup
     */
    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
    }

    /**
     * Tests the constructor with a valid output stream.
     *
     * @throws IOException if an error occurs during the test
     */
    @Test
    public void testConstructorWithValidOutputStream() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(out);
        assertNull(tarArchiveOutputStream.getBuffer());
        assertNull(tarArchiveOutputStream.getAssemBuf());
        assertNull(tarArchiveOutputStream.getRecordBuf());
        assertNull(tarArchiveOutputStream.getAssemLen());
        assertNull(tarArchiveOutputStream.getLongFileMode());
        assertNull(tarArchiveOutputStream.getOut());
        assertNull(tarArchiveOutputStream.getClosed());
        assertNull(tarArchiveOutputStream.getCurrSize());
        assertNull(tarArchiveOutputStream.getCurrName());
        assertNull(tarArchiveOutputStream.getCurrBytes());
    }

    /**
     * Tests the constructor with a valid output stream, block size, and record size.
     *
     * @throws IOException if an error occurs during the test
     */
    @Test
    public void testConstructorWithValidOutputStreamBlockSizeAndRecordSize()
            throws IOException {
        tarOutputStream = new TarArchiveOutputStream(out, 512, 512);
        assertEquals(512, tarOutputStream.getBuffer().getBlockSize());
        assertEquals(512, tarOutputStream.getBuffer().getRecordSize());
        assertNull(tarArchiveOutputStream.getAssemBuf());
        assertNull(tarArchiveOutputStream.getRecordBuf());
        assertNull(tarArchiveOutputStream.getAssemLen());
        assertNull(tarArchiveOutputStream.getLongFileMode());
        assertEquals(out, tarArchiveOutputStream.getOut());
        assertFalse(tarArchiveOutputStream.isClosed());
        assertNull(tarArchiveOutputStream.getCurrSize());
        assertNull(tarArchiveOutputStream.getCurrName());
        assertNull(tarArchiveOutputStream.getCurrBytes());
    }

    /**
     * Tests setting the long file mode.
     *
     * @throws IOException if an error occurs during the test
     */
    @Test
    public void testSetLongFileMode() throws IOException {
        tarOutputStream = new TarArchiveOutputStream(out, 512, 512);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE,
                tarArchiveOutputStream.getLongFileMode());
    }

    /**
     * Tests the finish() method.
     *
     * @throws IOException if an error occurs during the test
     */
    @Test
    public void testFinish() throws IOException {
        tarOutputStream = new TarArchiveOutputStream(out, 512, 512);
        tarOutputStream.finish();
        byte[] buffer = out.toByteArray();
        int eofRecordsCount = 2;
        int expectedEOFRecordSize = TarConstants.STAR_MAGIC_SIZE +
                TarConstants.HEADER_SIZE + 2 * TarConstants.RECORD_SIZE;
        int totalSize = buffer.length;
        assertEquals(eofRecordsCount * TarConstants.RECORD_SIZE + totalSize,
                tarOutputStream.getSize());
        assertEquals(expectedEOFRecordSize, buffer.length % expectedEOFRecordSize);
    }

    /**
     * Tests the close() method.
     *
     * @throws IOException if an error occurs during the test
     */
    @Test
    public void testClose() throws IOException {
        tarOutputStream = new TarArchiveOutputStream(out, 512, 512);
        tarOutputStream.close();
        byte[] buffer = out.toByteArray();
        int expectedEOFRecordSize = TarConstants.STAR_MAGIC_SIZE +
                TarConstants.HEADER_SIZE + 2 * TarConstants.RECORD_SIZE;
        int totalSize = buffer.length;
        assertEquals(expectedEOFRecordSize, buffer.length % expectedEOFRecordSize);
        assertTrue(tarOutputStream.isClosed());
    }

    /**
     * Tests putting an ArchiveEntry with a valid name and size.
     *
     * @throws IOException if an error occurs during the test
     */
    @Test
    public void testPutArchiveEntryWithValidNameAndSize() throws IOException {
        String entryName = "testEntry.txt";
        long entrySize = 10;
        TarArchiveEntry entry = new TarArchiveEntry(entryName, entrySize);
        tarOutputStream.putArchiveEntry(entry);
        assertEquals(entryName, tarOutputStream.getCurrName());
        assertEquals(entrySize, tarOutputStream.getCurrSize());
        assertEquals(entrySize, tarOutputStream.getBytesWritten());
    }

    /**
     * Tests putting an ArchiveEntry with a long file name.
     *
     * @throws IOException if an error occurs during the test
     */
    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String entryName = "testEntryWithLongFileName.txt";
        long entrySize = 10;
        TarArchiveEntry entry = new TarArchiveEntry(entryName, entrySize);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream.putArchiveEntry(entry);
        assertTrue(tarOutputStream.getCurrName().contains(entryName));
        assertEquals(entrySize, tarOutputStream.getBytesWritten());
    }

    /**
     * Tests putting an ArchiveEntry with an invalid name length.
     *
     * @throws IOException if an error occurs during the test
     */
    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidNameLength() throws IOException {
        String entryName = "testEntryWithInvalidNameLength.txt";
        long entrySize = 10;
        TarArchiveEntry entry = new TarArchiveEntry(entryName, entrySize);
        tarOutputStream.putArchiveEntry(entry);
    }

    /**
     * Tests closing an ArchiveEntry without writing its contents.
     *
     * @throws IOException if an error occurs during the test
     */
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutWritingContents() throws IOException {
        String entryName = "testEntry.txt";
        long entrySize = 10;
        TarArchiveEntry entry = new TarArchiveEntry(entryName, entrySize);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        assertNull(tarOutputStream.getCurrName());
        assertNull(tarOutputStream.getCurrSize());
        assertNull(tarOutputStream.getBytesWritten());
    }

    /**
     * Tests writing bytes to the current archive entry.
     *
     * @throws IOException if an error occurs during the test
     */
    @Test
    public void testWriteToCurrentArchiveEntry() throws IOException {
        String entryName = "testEntry.txt";
        long entrySize = 20;
        TarArchiveEntry entry = new TarArchiveEntry(entryName, entrySize);
        tarOutputStream.putArchiveEntry(entry);
        byte[] data = "Test data".getBytes();
        int dataLength = data.length;
        tarOutputStream.write(data);
        int bytesWritten = tarOutputStream.getBytesWritten();
        assertEquals(dataLength, bytesWritten);
        tarOutputStream.closeArchiveEntry();
        byte[] buffer = out.toByteArray();
        int recordSize = 512;
        int headerSize = TarConstants.HEADER_SIZE;
        int starMagicSize = TarConstants.STAR_MAGIC_SIZE;
        int expectedDataSize = dataLength + headerSize + starMagicSize;
        int expectedTotalSize = expectedDataSize + entrySize;
        int expectedEOFRecordSize = TarConstants.STAR_MAGIC_SIZE +
                TarConstants.HEADER_SIZE + 2 * TarConstants.RECORD_SIZE;
        int dataPosition = TarConstants.STAR_MAGIC_SIZE + TarConstants.HEADER_SIZE;
        for (int i = 0; i < dataLength; i++) {
            assertEquals(data[i], buffer[dataPosition + i]);
        }
        assertEquals(expectedTotalSize, tarOutputStream.getSize());
        assertEquals(expectedEOFRecordSize, buffer.length % expectedEOFRecordSize);
    }
}
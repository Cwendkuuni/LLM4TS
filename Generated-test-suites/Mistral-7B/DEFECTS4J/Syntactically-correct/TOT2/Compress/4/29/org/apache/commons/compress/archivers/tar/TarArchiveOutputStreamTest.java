import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testConstructorWithOutputStream() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testConstructorWithOutputStreamAndBlockSizeAndRecordSize() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 1024);
        assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeWithInvalidValue() {
        tarArchiveOutputStream.setLongFileMode(3);
    }

    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.finish();
        byte[] archiveContent = outputStream.toByteArray();
        int eofRecordSize = TarConstants.STANDARD_HEADER_LENGTH + 2 * TarConstants.RECORD_SIZE;
        int expectedArchiveSize = TarConstants.STANDARD_HEADER_LENGTH + eofRecordSize;
        assertEquals(expectedArchiveSize, archiveContent.length);
    }

    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.close();
        assertFalse(tarArchiveOutputStream.isOpen());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidArchiveEntry() {
        tarArchiveOutputStream.putArchiveEntry(new Object());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", "test content");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] archiveContent = outputStream.toByteArray();
        int headerSize = TarConstants.STANDARD_HEADER_LENGTH;
        int recordSize = TarConstants.RECORD_SIZE;
        int expectedArchiveSize = headerSize + data.length + recordSize;
        assertEquals(expectedArchiveSize, archiveContent.length);
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "test/long/path/to/file.txt";
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(longFileName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(new byte[0], 0, 0);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] archiveContent = outputStream.toByteArray();
        int headerSize = TarConstants.STANDARD_HEADER_LENGTH;
        int recordSize = TarConstants.RECORD_SIZE;
        int longFileNameSize = (int) Math.ceil(longFileName.getBytes(StandardCharsets.UTF_8).length / (float) TarConstants.NAMELEN) * TarConstants.NAMELEN;
        int expectedArchiveSize = headerSize + longFileNameSize + recordSize;
        assertEquals(expectedArchiveSize, archiveContent.length);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", "test content");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] archiveContent = outputStream.toByteArray();
        int headerSize = TarConstants.STANDARD_HEADER_LENGTH;
        int recordSize = TarConstants.RECORD_SIZE;
        int expectedArchiveSize = headerSize + data.length + recordSize;
        assertEquals(expectedArchiveSize, archiveContent.length);
    }

    @Test
    public void testWriteWithClosedArchiveEntry() throws IOException {
        byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", "test content");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.write(new byte[0], 0, 0);
        byte[] archiveContent = outputStream.toByteArray();
        int headerSize = TarConstants.STANDARD_HEADER_LENGTH;
        int recordSize = TarConstants.RECORD_SIZE;
        int expectedArchiveSize = headerSize + data.length + recordSize;
        assertEquals(expectedArchiveSize, archiveContent.length);
    }

    @Test
    public void testWriteWithEmptyBuffer() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", "test content");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] emptyBuffer = new byte[0];
        tarArchiveOutputStream.write(emptyBuffer, 0, 0);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] archiveContent = outputStream.toByteArray();
        int headerSize = TarConstants.STANDARD_HEADER_LENGTH;
        int recordSize = TarConstants.RECORD_SIZE;
        int expectedArchiveSize = headerSize + TarConstants.RECORD_SIZE;
        assertEquals(expectedArchiveSize, archiveContent.length);
    }

    @Test
    public void testWriteWithPartialBuffer() throws IOException {
        byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", "test content");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] partialBuffer = new byte[5];
        System.arraycopy(data, 0, partialBuffer, 0, 5);
        tarArchiveOutputStream.write(partialBuffer, 0, 5);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] archiveContent = outputStream.toByteArray();
        int headerSize = TarConstants.STANDARD_HEADER_LENGTH;
        int recordSize = TarConstants.RECORD_SIZE;
        int expectedArchiveSize = headerSize + 5 + recordSize;
        assertEquals(expectedArchiveSize, archiveContent.length);
    }

    @Test
    public void testWriteWithFullBuffer() throws IOException {
        byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", "test content");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] archiveContent = outputStream.toByteArray();
        int headerSize = TarConstants.STANDARD_HEADER_LENGTH;
        int recordSize = TarConstants.RECORD_SIZE;
        int expectedArchiveSize = headerSize + data.length + recordSize;
        assertEquals(expectedArchiveSize, archiveContent.length);
    }

    @Test
    public void testWriteWithMultipleBuffers() throws IOException {
        byte[] data1 = "test data 1".getBytes(StandardCharsets.UTF_8);
        byte[] data2 = "test data 2".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", "test content");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(data1, 0, data1.length);
        tarArchiveOutputStream.write(data2, 0, data2.length);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] archiveContent = outputStream.toByteArray();
        int headerSize = TarConstants.STANDARD_HEADER_LENGTH;
        int recordSize = TarConstants.RECORD_SIZE;
        int expectedArchiveSize = headerSize + data1.length + data2.length + recordSize;
        assertEquals(expectedArchiveSize, archiveContent.length);
    }

    @Test
    public void testPutArchiveEntryWithDirectory() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test/dir/", null);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.finish();
        byte[] archiveContent = outputStream.toByteArray();
        int headerSize = TarConstants.STANDARD_HEADER_LENGTH;
        int recordSize = TarConstants.RECORD_SIZE;
        int expectedArchiveSize = headerSize + TarConstants.RECORD_SIZE;
        assertEquals(expectedArchiveSize, archiveContent.length);
    }

    @Test
    public void testWriteToFile() throws IOException {
        File outputFile = new File("test.tar");
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputFile);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", "test content");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        byte[] archiveContent = new ByteArrayInputStream(outputFile.length() > 0 ? Files.readAllBytes(outputFile.toPath()) : new byte[0]).readAllBytes();
        int headerSize = TarConstants.STANDARD_HEADER_LENGTH;
        int recordSize = TarConstants.RECORD_SIZE;
        int expectedArchiveSize = headerSize + data.length + recordSize;
        assertEquals(expectedArchiveSize, archiveContent.length);
        assertSame(outputFile, tarArchiveOutputStream.getSourceFile());
    }

    @Test(expected = IOException.class)
    public void testWriteToFileWithInvalidFileName() throws IOException {
        File outputFile = new File("test.tar");
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputFile);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test/invalidName", "test content");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.finish();
    }
}
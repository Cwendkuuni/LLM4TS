import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * TarArchiveOutputStreamTest class to test all methods in TarArchiveOutputStream.
 */
public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testConstructorWithOutputStream() {
        assert tarOutputStream != null;
    }

    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeWithInvalidValue() {
        tarOutputStream.setLongFileMode(3);
    }

    @Test
    public void testFinish() throws IOException {
        // Write empty archive
        tarOutputStream.finish();
        byte[] expectedEmptyArchive = new byte[TarConstants.STANDARD_MAGIC_NUMBER.length + 2 * TarConstants.RECORD_SIZE];
        Assert.assertArrayEquals(expectedEmptyArchive, outputStream.toByteArray());

        // Write archive with one file entry
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write("Hello, World!".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        byte[] expectedArchiveWithOneFile = new byte[TarConstants.STANDARD_MAGIC_NUMBER.length + 3 * TarConstants.RECORD_SIZE];
        System.arraycopy(TarConstants.STANDARD_MAGIC_NUMBER, 0, expectedArchiveWithOneFile, 0, TarConstants.STANDARD_MAGIC_NUMBER.length);
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH] = TarConstants.HEADER_TYPE_NEW;
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.NAME_LENGTH] = 'A';
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.NAME_LENGTH + 1] = 't';
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.NAME_LENGTH + 2] = 'e';
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.NAME_LENGTH + 3] = 's';
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.NAME_LENGTH + 4] = 't';
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.NAME_LENGTH + 5] = 'F';
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.NAME_LENGTH + 6] = 'i';
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.NAME_LENGTH + 7] = 'l';
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.NAME_LENGTH + 8] = 'e';
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.NAME_LENGTH + 9] = '.';
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.NAME_LENGTH + 10] = 't';
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.NAME_LENGTH + 11] = 'x';
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.NAME_LENGTH + 12] = 't';
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.NAME_LENGTH + 13] = 0;
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + 512] = 0; // Null terminator

        Assert.assertArrayEquals(expectedArchiveWithOneFile, outputStream.toByteArray());
    }

    @Test
    public void testClose() throws IOException {
        // Write empty archive
        tarOutputStream.close();
        Assert.assertEquals(outputStream.size(), 0);

        // Write archive with one file entry
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write("Hello, World!".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();

        Assert.assertEquals(outputStream.size(), 0);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutCloseArchiveEntry() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithNullArchiveEntry() {
        tarOutputStream.putArchiveEntry(null);
    }

    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryWithNonTarArchiveEntry() {
        ArchiveEntry archiveEntry = new ArchiveEntry("testFile.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);

        byte[] testData = "Hello, World!".getBytes();
        int testDataLength = testData.length;

        tarOutputStream.write(testData, 0, testDataLength);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();

        byte[] expectedArchiveWithOneFile = new byte[TarConstants.STANDARD_MAGIC_NUMBER.length + 3 * TarConstants.RECORD_SIZE];
        System.arraycopy(TarConstants.STANDARD_MAGIC_NUMBER, 0, expectedArchiveWithOneFile, 0, TarConstants.STANDARD_MAGIC_NUMBER.length);
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH] = TarConstants.HEADER_TYPE_NEW;
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.NAME_LENGTH] = 'A';
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.NAME_LENGTH + 1] = 't';
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.NAME_LENGTH + 2] = 'e';
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.NAME_LENGTH + 3] = 's';
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.NAME_LENGTH + 4] = 't';
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.NAME_LENGTH + 5] = 'F';
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.NAME_LENGTH + 6] = 'i';
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.NAME_LENGTH + 7] = 'l';
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.NAME_LENGTH + 8] = 'e';
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.NAME_LENGTH + 9] = '.';
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.NAME_LENGTH + 10] = 't';
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.NAME_LENGTH + 11] = 'x';
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.NAME_LENGTH + 12] = 't';
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.NAME_LENGTH + 13] = 0;
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.FILE_SIZE] = (byte) testDataLength;
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.FILE_SIZE + TarConstants.CHECKSUM_LENGTH] = 0; // Checksum

        for (int i = 0; i < testDataLength; i++) {
            expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.FILE_SIZE + i] = testData[i];
        }

        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + TarConstants.FILE_SIZE + testDataLength] = 0; // null terminator
        expectedArchiveWithOneFile[TarConstants.STANDARD_MAGIC_NUMBER.length + TarConstants.HEADER_LENGTH + 512] = 0; // Null terminator

        Assert.assertArrayEquals(expectedArchiveWithOneFile, outputStream.toByteArray());
    }

    @Test(expected = IOException.class)
    public void testWriteWithNullData() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(null, 0, 0);
        tarOutputStream.closeArchiveEntry();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testWriteWithNegativeOffset() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        byte[] testData = "Hello, World!".getBytes();
        tarOutputStream.write(testData, -1, testData.length);
        tarOutputStream.closeArchiveEntry();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testWriteWithNegativeLength() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        byte[] testData = "Hello, World!".getBytes();
        tarOutputStream.write(testData, 0, -1);
        tarOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testWriteWithInvalidLength() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        byte[] testData = "Hello, World!".getBytes();
        tarOutputStream.write(testData, 0, testData.length + 1);
        tarOutputStream.closeArchiveEntry();
    }
}
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarArchiveOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testPutArchiveEntryWithValidEntry() throws IOException {
        String expectedEntryName = "testEntry.txt";
        long expectedEntrySize = 10L;

        TarArchiveEntry archiveEntry = new TarArchiveEntry(expectedEntryName, expectedEntrySize);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] entryContent = "Test file content".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(entryContent, 0, entryContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();

        byte[] expectedHeader = buildExpectedHeader(expectedEntryName, expectedEntrySize);
        byte[] actualHeader = getRecordFromByteArray(outputStream.toByteArray(), 0);

        Assert.assertArrayEquals(expectedHeader, actualHeader);

        byte[] expectedContent = "Test file content".getBytes(StandardCharsets.UTF_8);
        byte[] actualContent = getRecordFromByteArray(outputStream.toByteArray(), expectedHeader.length);

        Assert.assertArrayEquals(expectedContent, actualContent);
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "a/long/path/for/test/entry.txt";

        TarArchiveEntry archiveEntry = new TarArchiveEntry(longFileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] entryContent = "Long file name test content".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(entryContent, 0, entryContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();

        // Checking that long file name has been truncated as per LONGFILE_TRUNCATE mode
        String truncatedFileName = TarConstants.LONG_FILE_NAME_TRUNCATED;
        byte[] expectedHeader = buildExpectedHeader(truncatedFileName, 0L); // size is irrelevant for long file names
        byte[] actualHeader = getRecordFromByteArray(outputStream.toByteArray(), 0);

        Assert.assertArrayEquals(expectedHeader, actualHeader);
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithLongFileNameAndLONGFILE_ERRORMode() throws IOException {
        String longFileName = "a/long/path/for/test/entry.txt";

        TarArchiveEntry archiveEntry = new TarArchiveEntry(longFileName);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] entryContent = "Long file name test content".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(entryContent, 0, entryContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();

        // Exception should be thrown as per LONGFILE_ERROR mode
    }

    @Test
    public void testPutArchiveEntryWithDirectoryEntry() throws IOException {
        String expectedDirectoryName = "testDirectory";

        TarArchiveEntry archiveEntry = new TarArchiveEntry(expectedDirectoryName, 0);
        archiveEntry.setDirectory(true);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.finish();

        byte[] expectedHeader = buildExpectedHeader(expectedDirectoryName, 0L);
        byte[] actualHeader = getRecordFromByteArray(outputStream.toByteArray(), 0);

        Assert.assertArrayEquals(expectedHeader, actualHeader);
    }

    @Test
    public void testClose() throws IOException {
        String entryName = "testEntry.txt";
        long entrySize = 10L;

        TarArchiveEntry archiveEntry = new TarArchiveEntry(entryName, entrySize);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] entryContent = "Test file content".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(entryContent, 0, entryContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();

        // Write an empty record to check if the stream is actually closed
        tarArchiveOutputStream.write(new byte[TarArchiveOutputStream.recordSize], 0, TarArchiveOutputStream.recordSize);

        // Another attempt to write should throw an exception
        tarArchiveOutputStream.write(new byte[1], 0, 1);
    }

    private byte[] buildExpectedHeader(String name, long size) {
        // Header for the entry record
        byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);
        byte[] header = new byte[TarArchiveOutputStream.recordSize];

        for (int i = 0; i < TarConstants.NAMELEN; i++) {
            header[i] = (byte) nameBytes[i % TarConstants.NAMELEN];
        }

        header[TarConstants.NAMELEN] = (byte) (size >>> 48 & 0xFF);
        header[TarConstants.NAMELEN + 1] = (byte) (size >>> 40 & 0xFF);
        header[TarConstants.NAMELEN + 2] = (byte) (size >>> 32 & 0xFF);
        header[TarConstants.NAMELEN + 3] = (byte) (size >>> 24 & 0xFF);
        header[TarConstants.NAMELEN + 4] = (byte) (size >>> 16 & 0xFF);
        header[TarConstants.NAMELEN + 5] = (byte) (size >>> 8 & 0xFF);
        header[TarConstants.NAMELEN + 6] = (byte) (size & 0xFF);

        return header;
    }

    private byte[] getRecordFromByteArray(byte[] byteArray, int offset) {
        byte[] record = new byte[TarArchiveOutputStream.recordSize];
        System.arraycopy(byteArray, offset, record, 0, TarArchiveOutputStream.recordSize);
        return record;
    }
}
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * TarArchiveOutputStreamTest.
 */
public class TarArchiveOutputStreamTest {
    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws Exception {
        tarArchiveOutputStream.close();
        outputStream.close();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        // Given
        String fileName = "testFile.txt";
        String expectedFileName = "testFile.txt";
        TarArchiveEntry testArchiveEntry = new TarArchiveEntry(fileName);
        byte[] testFileContent = "Test File Content".getBytes(StandardCharsets.UTF_8);

        // When
        tarArchiveOutputStream.putNextEntry(testArchiveEntry);
        tarArchiveOutputStream.write(testFileContent, 0, testFileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        // Then
        byte[] tarEntryBytes = outputStream.toByteArray();
        TarArchiveEntry tarEntry = new TarArchiveEntry(tarEntries -> Arrays.copyOfRange(tarEntries, 0, TarConstants.HEADER_LENGTH));
        tarEntry.readHeader(tarEntryBytes, 0);
        assertEquals(expectedFileName, tarEntry.getName());
        assertEquals(-1, tarEntry.getSize()); // Indicates a regular file

        // Verify the content was written correctly
        byte[] contentInArchive = new byte[tarEntry.getSize()];
        int bytesRead = tarArchiveOutputStream.read(contentInArchive, 0, contentInArchive.length);
        assertEquals(testFileContent.length, bytesRead);
        assertNotNull(new String(contentInArchive, StandardCharsets.UTF_8));
        assertEquals(new String(testFileContent, StandardCharsets.UTF_8), new String(contentInArchive, StandardCharsets.UTF_8));
    }

    @Test(expected = IOException.class)
    public void testLongFileNameWhenLongFileModeIsError() throws IOException {
        // Given
        String longFileName = "longFileNameThatIsTooLong.txt";
        TarArchiveEntry testArchiveEntry = new TarArchiveEntry(longFileName);

        // When & Then
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        tarArchiveOutputStream.putArchiveEntry(testArchiveEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullArchiveEntry() throws IOException {
        // Given

        // When & Then
        tarArchiveOutputStream.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidArchiveEntryClass() throws IOException {
        // Given
        Object invalidArchiveEntry = new Object();

        // When & Then
        tarArchiveOutputStream.putArchiveEntry(invalidArchiveEntry);
    }

    @Test
    public void testFinish() throws IOException {
        // Given

        // When
        tarArchiveOutputStream.finish();

        // Then
        int eofRecordLength = TarConstants.TAR_BLK0SIZE;
        byte[] eofRecord = new byte[eofRecordLength];
        Arrays.fill(eofRecord, (byte) 0);
        byte[] tarArchive = outputStream.toByteArray();
        int indexOfEofRecord = Arrays.binarySearch(tarArchive, (byte) 0);
        assertEquals(-1 + eofRecordLength, indexOfEofRecord);
        assertArrayEquals(eofRecord, Arrays.copyOfRange(tarArchive, indexOfEofRecord, indexOfEofRecord + eofRecord.length));
    }
}
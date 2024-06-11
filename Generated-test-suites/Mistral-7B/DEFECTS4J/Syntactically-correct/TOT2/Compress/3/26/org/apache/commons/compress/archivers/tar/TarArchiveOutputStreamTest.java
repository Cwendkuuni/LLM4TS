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
import java.nio.file.Files;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * TarArchiveOutputStreamTest class.
 */
public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setup() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    /**
     * Test for putArchiveEntry method with valid arguments.
     * @throws IOException on error
     */
    @Test
    public void testPutArchiveEntryWithValidArguments() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt", "testContent");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] testContent = "testContent".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testContent, 0, testContent.length);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] expectedOutput = createExpectedOutput("testFile.txt", testContent);
        assertEquals(expectedOutput, outputStream.toByteArray());
    }

    /**
     * Test for putArchiveEntry method with long file name.
     * @throws IOException on error
     */
    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "longFileNameTest" + TarConstants.NAMELEN + "A";
        TarArchiveEntry archiveEntry = new TarArchiveEntry(longFileName, "testContent");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] testContent = "testContent".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testContent, 0, testContent.length);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] expectedOutput = createExpectedOutput(longFileName, testContent);
        assertEquals(expectedOutput, outputStream.toByteArray());
    }

    /**
     * Test for putArchiveEntry method with file that doesn't exist.
     */
    @Test
    public void testPutArchiveEntryWithNonExistingFile() {
        File nonExistingFile = new File("nonExistingFile.txt");
        TarArchiveEntry archiveEntry = new TarArchiveEntry(nonExistingFile, "nonExistingFile");
        assertThrows(IOException.class, () -> tarArchiveOutputStream.putArchiveEntry(archiveEntry));
    }

    /**
     * Test for putArchiveEntry method with null ArchiveEntry.
     */
    @Test
    public void testPutArchiveEntryWithNullArchiveEntry() {
        assertThrows(NullPointerException.class, () -> tarArchiveOutputStream.putArchiveEntry(null));
    }

    /**
     * Test for closeArchiveEntry method.
     */
    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt", "testContent");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] testContent = "testContent".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testContent, 0, testContent.length);
        tarArchiveOutputStream.closeArchiveEntry();
        assertNull(tarArchiveOutputStream.getNextEntry());
    }

    /**
     * Test for finish method.
     * @throws IOException on error
     */
    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt", "testContent");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] testContent = "testContent".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testContent, 0, testContent.length);
        tarArchiveOutputStream.finish();
        byte[] expectedOutput = createExpectedOutput("testFile.txt", testContent);
        assertEquals(expectedOutput, outputStream.toByteArray());
    }

    /**
     * Test for close method.
     * @throws IOException on error
     */
    @Test
    public void testClose() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt", "testContent");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] testContent = "testContent".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testContent, 0, testContent.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();
        assertNull(tarArchiveOutputStream.getNextEntry());
    }

    /**
     * Test for setLongFileMode method.
     */
    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new File("src/test/resources/output.tar"), TarArchiveOutputStream.LONGFILE_TRUNCATE);
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt", "testContent");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        assertFalse(tarArchiveOutputStream.isLongFileModeGNU());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertTrue(tarArchiveOutputStream.isLongFileModeGNU());
    }

    /**
     * Test for writing too many bytes to an entry.
     * @throws IOException on error
     */
    @Test
    public void testWriteTooManyBytesToAnEntry() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt", "testContent");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] testContent = "testContent".getBytes(StandardCharsets.UTF_8);
        byte[] extraContent = "extraContent".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testContent, 0, testContent.length);
        tarArchiveOutputStream.write(extraContent, 0, extraContent.length);
        byte[] expectedOutput = createExpectedOutput("testFile.txt", testContent);
        assertEquals(expectedOutput, outputStream.toByteArray());
    }

    /**
     * Test for writing to a closed archive.
     */
    @Test
    public void testWriteToClosedArchive() {
        assertThrows(IOException.class, () -> tarArchiveOutputStream.write(new byte[1], 0, 1));
    }

    /**
     * Test for writing to an empty archive.
     * @throws IOException on error
     */
    @Test
    public void testWriteToEmptyArchive() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new File("src/test/resources/output.tar"), TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarArchiveOutputStream.write(new byte[1], 0, 1);
        byte[] expectedOutput = new byte[1];
        expectedOutput[0] = (byte) 0x14; // expected byte for the first record in a tar archive
        assertEquals(expectedOutput, Arrays.copyOfRange(outputStream.toByteArray(), 0, 1));
    }

    /**
     * Test for writing to archive with existing file.
     * @throws IOException on error
     */
    @Test
    public void testWriteToArchiveWithExistingFile() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new File("src/test/resources/output.tar"), TarArchiveOutputStream.LONGFILE_TRUNCATE);
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt", "testContent");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] testContent = "testContent".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testContent, 0, testContent.length);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] existingFileContent = Files.readAllBytes(new File("src/test/resources/testFile.txt").toPath());
        byte[] expectedOutput = createExpectedOutput("testFile.txt", testContent);
        assertEquals(expectedOutput, Arrays.copyOfRange(outputStream.toByteArray(), 0, outputStream.size()));
        assertEquals(existingFileContent, Files.readAllBytes(new File("src/test/resources/testFile.txt").toPath()));
    }

    private byte[] createExpectedOutput(String fileName, byte[] content) {
        byte[] headerBytes = new byte[512];
        byte[] recordBytes = new byte[512];
        int headerLength = TarConstants.TAR_HEADER_LENGTH;
        int recordLength = TarConstants.TAR_RECORD_LENGTH;

        // Write header
        System.arraycopy(TarConstants.MAGIC, 0, headerBytes, 0, TarConstants.MAGIC.length);
        headerBytes[TarConstants.NAME_INDEX] = (byte) (fileName.length() >> 3);
        headerBytes[TarConstants.NAME_length] = (byte) (fileName.length() & 0xFF);
        headerBytes[TarConstants.FILE_SIZE_index] = (byte) (content.length >> 24);
        headerBytes[TarConstants.FILE_SIZE_index + 1] = (byte) (content.length >> 16);
        headerBytes[TarConstants.FILE_SIZE_index + 2] = (byte) (content.length >> 8);
        headerBytes[TarConstants.FILE_SIZE_index + 3] = (byte) content.length;
        headerBytes[TarConstants.MOD_TIME_index] = 0;
        headerBytes[TarConstants.MOD_TIME_index + 9] = 0;
        System.arraycopy(TarConstants.UNIX_MODE, 0, headerBytes, TarConstants.UNIX_MODE_index, TarConstants.UNIX_MODE.length);
        headerBytes[TarConstants.UNIX_UID_index] = 0;
        headerBytes[TarConstants.UNIX_GID_index] = 0;
        headerBytes[TarConstants.FILE_TYPE_index] = (byte) TarConstants.NORMAL_FILE;

        // Write record
        int offset = 0;
        System.arraycopy(headerBytes, offset, recordBytes, offset, headerLength);
        offset += headerLength;
        System.arraycopy(content, 0, recordBytes, offset, content.length);
        offset += content.length;

        byte[] expectedOutput = new byte[headerLength + content.length];
        System.arraycopy(headerBytes, 0, expectedOutput, 0, headerLength);
        System.arraycopy(content, 0, expectedOutput, headerLength, content.length);

        return expectedOutput;
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
        outputStream.close();
    }
}
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    private static final int RECORD_SIZE = 512;
    private static final int BLOCK_SIZE = 512;
    private static final int NAME_LENGTH = 256;

    private TarArchiveOutputStream tarArchiveOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, BLOCK_SIZE, RECORD_SIZE);
    }

    @After
    public void tearDown() throws Exception {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithNormalFile() throws IOException {
        File inputFile = new File("testFile.txt");
        File outputFile = new File("test.tar");
        byte[] fileContent = "Hello World".getBytes(StandardCharsets.UTF_8);
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt", fileContent);

        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedOutput = buildExpectedOutput("testFile.txt", fileContent.length, fileContent);
        byte[] actualOutput = outputStream.toByteArray();

        assertArrayEquals("Unexpected output", expectedOutput, actualOutput);
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "a/long/filename/with/spaces.txt";
        byte[] fileContent = "LongFileNameContent".getBytes(StandardCharsets.UTF_8);
        ArchiveEntry archiveEntry = new TarArchiveEntry(longFileName, fileContent);

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(longFileName.getBytes(StandardCharsets.UTF_8), 0, longFileName.getBytes(StandardCharsets.UTF_8).length);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedOutput = buildExpectedOutput(longFileName, fileContent.length, fileContent);
        byte[] actualOutput = outputStream.toByteArray();

        assertArrayEquals("Unexpected output", expectedOutput, actualOutput);
    }

    @Test
    public void testPutArchiveEntryWithDirectory() throws IOException {
        File inputDirectory = new File("testDirectory");
        String directoryName = "testDirectory/";

        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry(directoryName));
        tarArchiveOutputStream.closeEntry();

        File testDirectoryFile = new File(inputDirectory.getAbsolutePath() + File.separator + "testDirectory_");
        testDirectoryFile.mkdir();

        File testFile = new File(testDirectoryFile.getAbsolutePath() + File.separator + "testFile.txt");
        byte[] fileContent = "DirectoryContent".getBytes(StandardCharsets.UTF_8);
        ArchiveEntry archiveEntry = new TarArchiveEntry(testFile.getAbsolutePath());

        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        testDirectoryFile.delete();

        byte[] expectedOutput = buildExpectedOutput(directoryName, fileContent.length, fileContent);
        byte[] actualOutput = outputStream.toByteArray();

        assertArrayEquals("Unexpected output", expectedOutput, actualOutput);
    }

    @Test
    public void testPutArchiveEntryWithInvalidLongFileName() throws IOException {
        String longFileName = "a/long/filename/with/spaces.txt";
        byte[] fileContent = "LongFileNameContent".getBytes(StandardCharsets.UTF_8);
        ArchiveEntry archiveEntry = new TarArchiveEntry(longFileName, fileContent);

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        try {
            tarArchiveOutputStream.putArchiveEntry(archiveEntry);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        byte[] actualOutput = outputStream.toByteArray();
        assertTrue("Unexpected output", actualOutput.length == 0);
    }

    @Test
    public void testFinish() throws IOException {
        byte[] fileContent = "FinishContent".getBytes(StandardCharsets.UTF_8);
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt", fileContent);

        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();

        byte[] expectedOutput = buildExpectedOutput("testFile.txt", fileContent.length, fileContent);
        byte[] actualOutput = outputStream.toByteArray();

        assertArrayEquals("Unexpected output", expectedOutput, actualOutput);
    }

    @Test
    public void testClose() throws IOException {
        byte[] fileContent = "CloseContent".getBytes(StandardCharsets.UTF_8);
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt", fileContent);

        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedOutput = buildExpectedOutput("testFile.txt", fileContent.length, fileContent);
        byte[] actualOutput = outputStream.toByteArray();

        assertArrayEquals("Unexpected output", expectedOutput, actualOutput);

        tarArchiveOutputStream.close();
    }

    private byte[] buildExpectedOutput(String entryName, int fileLength, byte[] fileContent) {
        byte[] nameBytes = entryName.getBytes(StandardCharsets.UTF_8);
        byte[] header = new TarArchiveEntry(entryName, fileLength).getHeader();
        byte[] record = new byte[RECORD_SIZE];
        int assemLen = 0;

        System.arraycopy(header, 0, record, 0, header.length);
        System.arraycopy(nameBytes, 0, record, 122, nameBytes.length);

        if (fileLength > 0) {
            System.arraycopy(fileContent, 0, record, 272, fileContent.length);
        }

        if (entryName.length() > NAME_LENGTH) {
            byte[] longNameRecord = new byte[RECORD_SIZE];
            int i = 0;

            longNameRecord[i++] = TarConstants.LF_GNUTYPE_LONGNAME;
            longNameRecord[i++] = (byte) (entryName.length() >> 8);
            longNameRecord[i++] = (byte) (entryName.length() & 0xFF);

            System.arraycopy(nameBytes, 0, longNameRecord, i, nameBytes.length);

            return Arrays.copyOfRange(longNameRecord, 0, longNameRecord.length + record.length);
        }

        return Arrays.copyOfRange(record, 0, record.length + fileContent.length);
    }
}
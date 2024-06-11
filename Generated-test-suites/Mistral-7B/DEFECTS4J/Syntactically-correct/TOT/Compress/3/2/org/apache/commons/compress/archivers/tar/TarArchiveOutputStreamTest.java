import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link TarArchiveOutputStream}.
 */
public class TarArchiveOutputStreamTest {

    private static final String TEST_FILE_NAME = "testFile.txt";
    private static final String TEST_DIRECTORY_NAME = "testDirectory/";
    private static final String TAR_FILE_NAME = "archive.tar";
    private static final int BLOCK_SIZE = 512;
    private static final int RECORD_SIZE = 512;

    private TarOutputStream tarOutputStream;
    private File outputFile;

    @Before
    public void setUp() throws IOException {
        outputFile = new File(System.getProperty("java.io.tmpdir"), TAR_FILE_NAME);
        tarOutputStream = new TarArchiveOutputStream(new FileOutputStream(outputFile), BLOCK_SIZE, RECORD_SIZE);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    @After
    public void tearDown() throws IOException {
        if (tarOutputStream != null) {
            tarOutputStream.finish();
            tarOutputStream.close();
        }
        if (outputFile.exists()) {
            outputFile.delete();
        }
    }

    @Test
    public void testPutArchiveEntryWithValidFileAndName() throws IOException {
        File testFile = new File("testFile.txt");
        byte[] fileContent = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry entry = new TarArchiveEntry(TEST_FILE_NAME);
        try (InputStream inputStream = new ByteArrayInputStream(fileContent);) {
            tarOutputStream.putNextEntry(entry);
            tarOutputStream.write(fileContent, 0, fileContent.length);
            tarOutputStream.closeArchiveEntry();
        }
        assertNotNull(entry);
        assertEquals(TEST_FILE_NAME, entry.getName());
        assertEquals(fileContent.length, entry.getSize());

        try (InputStream inputStream = new FileInputStream(outputFile)) {
            byte[] readContent = new byte[fileContent.length];
            int bytesRead = inputStream.read(readContent, 0, fileContent.length);
            assertEquals(bytesRead, fileContent.length);
            assertArrayEquals(fileContent, readContent);
        }
    }

    @Test
    public void testPutArchiveEntryWithValidDirectoryAndName() throws IOException {
        File testDirectory = new File(TEST_DIRECTORY_NAME);
        tarOutputStream.putNextEntry(new TarArchiveEntry(TEST_DIRECTORY_NAME, false));
        tarOutputStream.finish();
        tarOutputStream.close();
        assertFalse(testDirectory.exists());
        File testDirectoryInArchive = new File(outputFile, TEST_DIRECTORY_NAME);
        assertNotNull(testDirectoryInArchive);
        assertTrue(testDirectoryInArchive.isDirectory());
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "very_long_filename.txt";
        byte[] fileContent = "long file content".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        entry.setSize(fileContent.length);
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.write(fileContent, 0, fileContent.length);
        tarOutputStream.closeArchiveEntry();
        try (InputStream inputStream = new FileInputStream(outputFile)) {
            byte[] readContent = new byte[fileContent.length];
            int bytesRead = inputStream.read(readContent, 0, fileContent.length);
            assertEquals(bytesRead, fileContent.length);
            assertArrayEquals(fileContent, readContent);
        }
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithLongFileNameLongFileModeError() throws IOException {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        String longFileName = "very_long_filename.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        try {
            tarOutputStream.putNextEntry(entry);
        } finally {
            tarOutputStream.close();
        }
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameLongFileTruncate() throws IOException {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        String longFileName = "very_long_filename.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        try {
            tarOutputStream.putNextEntry(entry);
        } finally {
            tarOutputStream.close();
        }
    }

    @Test
    public void testPutArchiveEntryWithEmptyFile() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(TEST_FILE_NAME);
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.closeArchiveEntry();

        try (InputStream inputStream = new FileInputStream(outputFile)) {
            byte[] readContent = new byte[1];
            int bytesRead = inputStream.read(readContent, 0, 1);
            assertEquals(bytesRead, 0);
        }
    }

    @Test
    public void testPutArchiveEntryWithNullEntry() {
        assertThrows(NullPointerException.class, () -> tarOutputStream.putNextEntry(null));
    }

    @Test
    public void testPutArchiveEntryTwiceWithSameName() {
        TarArchiveEntry entry = new TarArchiveEntry(TEST_FILE_NAME);
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.putNextEntry(entry);
        tarOutputStream.close();

        try (InputStream inputStream = new FileInputStream(outputFile)) {
            byte[] readContent = new byte[1];
            int bytesRead = inputStream.read(readContent, 0, 1);
            assertEquals(bytesRead, 0);
        }
    }

    @Test
    public void testPutArchiveEntryWithInvalidDirectoryPath() {
        assertThrows(IllegalArgumentException.class, () -> tarOutputStream.putNextEntry(new TarArchiveEntry(TEST_FILE_NAME, "/")));
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry testFileEntry = new TarArchiveEntry(TEST_FILE_NAME);
        TarArchiveEntry testDirectoryEntry = new TarArchiveEntry(TEST_DIRECTORY_NAME, false);
        tarOutputStream.putNextEntry(testFileEntry);
        tarOutputStream.putNextEntry(testDirectoryEntry);
        tarOutputStream.finish();
        tarOutputStream.close();

        try (InputStream inputStream = new FileInputStream(outputFile)) {
            byte[] readContent = new byte[TarConstants.MAGIC_LENGTH + TEST_FILE_NAME.length()];
            int bytesRead = inputStream.read(readContent, 0, TarConstants.MAGIC_LENGTH + TEST_FILE_NAME.length());
            assertEquals(bytesRead, TarConstants.MAGIC_LENGTH + TEST_FILE_NAME.length());
            assertEquals(TarConstants.MAGIC_NUMBER, Arrays.copyOfRange(readContent, 0, TarConstants.MAGIC_LENGTH));
            assertEquals(TEST_FILE_NAME, new String(readContent, TarConstants.MAGIC_LENGTH, TEST_FILE_NAME.length(), StandardCharsets.UTF_8));

            byte[] nextEntryBytes = new byte[TarConstants.HEADER_LENGTH + TEST_FILE_NAME.length()];
            bytesRead = inputStream.read(nextEntryBytes, 0, TarConstants.HEADER_LENGTH + TEST_FILE_NAME.length());
            assertEquals(bytesRead, TarConstants.HEADER_LENGTH + TEST_FILE_NAME.length());
            TarArchiveEntry nextEntry = new TarArchiveEntry(nextEntryBytes);
            assertEquals(TEST_FILE_NAME, nextEntry.getName());
            assertEquals(fileContent.length, nextEntry.getSize());
            assertEquals(TarConstants.STANDARD, nextEntry.getMode());
            assertEquals(TarConstants.NORMAL, nextEntry.getTypeFlag());
            assertEquals(TarConstants.UNIX, nextEntry.getFileType());
            assertEquals("", nextEntry.getLinkName());
            assertNull(nextEntry.getExtraField());
            assertNull(nextEntry.getCompressionMethod());
            assertNull(nextEntry.getCompressedSize());
            assertNull(nextEntry.getCrc());
        }

        try (InputStream inputStream = new FileInputStream(outputFile)) {
            byte[] readContent = new byte[TarConstants.MAGIC_LENGTH + TEST_DIRECTORY_NAME.length()];
            int bytesRead = inputStream.read(readContent, 0, TarConstants.MAGIC_LENGTH + TEST_DIRECTORY_NAME.length());
            assertEquals(bytesRead, TarConstants.MAGIC_LENGTH + TEST_DIRECTORY_NAME.length());
            assertEquals(TarConstants.MAGIC_NUMBER, Arrays.copyOfRange(readContent, 0, TarConstants.MAGIC_LENGTH));
            assertEquals(TEST_DIRECTORY_NAME, new String(readContent, TarConstants.MAGIC_LENGTH, TEST_DIRECTORY_NAME.length(), StandardCharsets.UTF_8));
            assertEquals(TarConstants.GNU_STND, nextEntry.getFileType());
            assertEquals(TarConstants.ST_DIR, nextEntry.getFileMode());
        }
    }
}
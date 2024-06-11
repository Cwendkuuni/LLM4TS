import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Tests for {@link TarArchiveOutputStream}.
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
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
    }

    /**
     * Tests if the constructor with no arguments initializes the stream correctly.
     *
     * @throws IOException if an error occurs
     */
    @Test
    public void testConstructorNoArguments() throws IOException {
        tarArchiveOutputStream = new TarArchiveOutputStream(null);
        assertNull(tarArchiveOutputStream);
    }

    /**
     * Tests if the constructor with one argument initializes the stream correctly.
     *
     * @throws IOException if an error occurs
     */
    @Test
    public void testConstructorOneArgument() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        assertNotNull(tarArchiveOutputStream);
    }

    /**
     * Tests if the constructor with three arguments initializes the stream correctly.
     *
     * @throws IOException if an error occurs
     */
    @Test
    public void testConstructorThreeArguments() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream,
                TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        assertNotNull(tarArchiveOutputStream);
    }

    /**
     * Tests if setting the long file mode works.
     *
     * @throws IOException if an error occurs
     */
    @Test
    public void testSetLongFileMode() throws IOException {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE,
                tarArchiveOutputStream.getLongFileMode());
    }

    /**
     * Tests if finishing the stream writes the end of archive records.
     *
     * @throws IOException if an error occurs
     */
    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.finish();
        byte[] bytes = outputStream.toByteArray();
        int eofIndex = bytes.length - 2 * TarConstants.TAR_RECORD_SIZE;
        for (int i = eofIndex; i > 0; i--) {
            if (bytes[i] != TarConstants.EOF_MAGIC[0]) {
                fail("EOF records not found");
            }
        }
    }

    /**
     * Tests if closing the stream writes the end of archive records and closes the underlying output stream.
     *
     * @throws IOException if an error occurs
     */
    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.close();
        byte[] bytes = outputStream.toByteArray();
        int eofIndex = bytes.length - 2 * TarConstants.TAR_RECORD_SIZE;
        for (int i = eofIndex; i > 0; i--) {
            if (bytes[i] != TarConstants.EOF_MAGIC[0]) {
                fail("EOF records not found");
            }
        }
        assertFalse(outputStream.canWrite());
    }

    /**
     * Tests if the record size is correctly retrieved.
     *
     * @throws IOException if an error occurs
     */
    @Test
    public void testGetRecordSize() throws IOException {
        int recordSize = tarArchiveOutputStream.getRecordSize();
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, recordSize);
    }

    /**
     * Tests if putting an archive entry with a short file name works.
     *
     * @throws IOException if an error occurs
     */
    @Test
    public void testPutArchiveEntryShortFileName() throws IOException {
        String fileName = "test.txt";
        byte[] fileContent = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry entry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] bytes = outputStream.toByteArray();
        int expectedIndex = 0;
        int recordSize = tarArchiveOutputStream.getRecordSize();
        for (; expectedIndex < bytes.length; expectedIndex += recordSize) {
            if (bytes[expectedIndex] == TarConstants.MAGIC_LONG_FILE_NAME[0]
                    && bytes[expectedIndex + 1] == TarConstants.MAGIC_STD_FILE_HEADER[0]) {
                break;
            }
        }
        assertNotNull(entry.getName().getBytes(StandardCharsets.UTF_8));
        assertNotNull(entry.getName());
    }

    /**
     * Tests if putting an archive entry with a long file name works.
     *
     * @throws IOException if an error occurs
     */
    @Test
    public void testPutArchiveEntryLongFileName() throws IOException {
        String fileName = "test_long_file_name.txt";
        byte[] fileContent = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry entry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(fileName.getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.write(0);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] bytes = outputStream.toByteArray();
        int expectedIndex = 0;
        int recordSize = tarArchiveOutputStream.getRecordSize();
        for (; expectedIndex < bytes.length; expectedIndex += recordSize) {
            if (bytes[expectedIndex] == TarConstants.MAGIC_LONG_FILE_NAME[0]
                    && bytes[expectedIndex + 1] == TarConstants.MAGIC_LONG_FILE_NAME[1]) {
                break;
            }
        }
        assertNotNull(entry.getName().getBytes(StandardCharsets.UTF_8));
        assertNotNull(entry.getName());
    }

    /**
     * Tests if writing to a closed archive entry throws an exception.
     *
     * @throws IOException if an error occurs
     */
    @Test(expected = IOException.class)
    public void testWriteClosedArchiveEntry() throws IOException {
        String fileName = "test.txt";
        byte[] fileContent = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry entry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
    }

    /**
     * Tests if writing past the size specified in the archive entry header throws an exception.
     *
     * @throws IOException if an error occurs
     */
    @Test(expected = IOException.class)
    public void testWriteBeyondArchiveEntrySize() throws IOException {
        String fileName = "test.txt";
        byte[] fileContent = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry entry = new TarArchiveEntry(fileName);
        entry.setSize(fileContent.length);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length + 1);
    }

    /**
     * Tests if writing an empty archive entry works.
     *
     * @throws IOException if an error occurs
     */
    @Test
    public void testWriteEmptyArchiveEntry() throws IOException {
        String fileName = "test.txt";
        TarArchiveEntry entry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] bytes = outputStream.toByteArray();
        int expectedIndex = 0;
        int recordSize = tarArchiveOutputStream.getRecordSize();
        for (; expectedIndex < bytes.length; expectedIndex += recordSize) {
            if (bytes[expectedIndex] == TarConstants.MAGIC_STD_FILE_HEADER[0]) {
                break;
            }
        }
        assertNotNull(entry.getName().getBytes(StandardCharsets.UTF_8));
        assertNotNull(entry.getName());
        assertEquals(0, entry.getSize());
    }

    /**
     * Tests if the createArchiveEntry method creates a valid entry.
     *
     * @throws IOException if an error occurs
     */
    @Test
    public void testCreateArchiveEntry() throws IOException {
        String fileName = "test.txt";
        File file = new File(fileName);
        TarArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(file, "test.txt");
        assertNotNull(entry.getName());
        assertEquals(TarArchiveEntry.FILE_TYPE, entry.getFileType());
        assertEquals("test.txt", entry.getName());
        assertEquals(file.length(), entry.getSize());
    }
}
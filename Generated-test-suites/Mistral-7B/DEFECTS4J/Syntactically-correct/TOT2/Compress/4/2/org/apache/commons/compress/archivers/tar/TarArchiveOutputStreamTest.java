import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * TarArchiveOutputStreamTest class for testing the TarArchiveOutputStream class.
 */
public class TarArchiveOutputStreamTest {

    private File tempFile;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        tempFile = Files.createTempFile("junit-test-", ".tar").toFile();
        tarOutputStream = new TarArchiveOutputStream(new FileOutputStream(tempFile));
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.close();
        Files.delete(Paths.get(tempFile.getAbsolutePath()));
    }

    /**
     * Test constructor when an OutputStream is provided.
     */
    @Test
    public void testConstructorWithOutputStream() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new MemoryOutputStream());
        assertEquals("Unexpected record size", TarArchiveOutputStream.TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
    }

    /**
     * Test setLongFileMode method.
     */
    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    /**
     * Test finish method.
     */
    @Test
    public void testFinish() throws IOException {
        assertThrows(IllegalStateException.class, tarOutputStream::finish);

        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.write("Test content".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        assertEquals("Unexpected archive size", 2 * TarArchiveOutputStream.TarBuffer.DEFAULT_BLKSIZE, tarOutputStream.getSize());
    }

    /**
     * Test close method.
     */
    @Test
    public void testClose() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.write("Test content".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();
    }

    /**
     * Test putArchiveEntry method with a valid ArchiveEntry.
     */
    @Test
    public void testPutArchiveEntryWithValidArchiveEntry() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);

        byte[] testContent = "Test content".getBytes();
        tarOutputStream.write(testContent);
        tarOutputStream.closeArchiveEntry();
    }

    /**
     * Test putArchiveEntry method with a null ArchiveEntry.
     */
    @Test(expected = NullPointerException.class)
    public void testPutArchiveEntryWithNullArchiveEntry() throws IOException {
        tarOutputStream.putArchiveEntry(null);
    }

    /**
     * Test closeArchiveEntry method with an unclosed entry.
     */
    @Test(expected = IllegalStateException.class)
    public void testCloseArchiveEntryWithoutPutArchiveEntry() throws IOException {
        tarOutputStream.closeArchiveEntry();
    }

    /**
     * Test closeArchiveEntry method with a closed entry.
     */
    @Test
    public void testCloseArchiveEntryWithClosedEntry() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.write("Test content".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.closeArchiveEntry();
    }

    /**
     * Test write method with valid data.
     */
    @Test
    public void testWriteWithValidData() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);

        byte[] testContent = "Test content".getBytes();
        tarOutputStream.write(testContent);
        tarOutputStream.closeArchiveEntry();

        byte[] expectedContent = "Test content\000\000".getBytes();
        File tempFileBackup = new File(tempFile.getAbsolutePath() + ".bak");
        Files.copy(Paths.get(tempFile.getAbsolutePath()), Paths.get(tempFile.getAbsolutePath() + ".bak"));

        byte[] actualContent = Files.readAllBytes(Paths.get(tempFile.getAbsolutePath()));
        assertEquals("Unexpected archive content", expectedContent, actualContent);

        Files.delete(Paths.get(tempFile.getAbsolutePath() + ".bak"));
    }

    /**
     * Test write method with invalid data (exceeds entry size).
     */
    @Test(expected = IOException.class)
    public void testWriteWithInvalidData() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.write("Test content".getBytes());
        tarOutputStream.write("Invalid data".getBytes());
        tarOutputStream.closeArchiveEntry();
    }
}
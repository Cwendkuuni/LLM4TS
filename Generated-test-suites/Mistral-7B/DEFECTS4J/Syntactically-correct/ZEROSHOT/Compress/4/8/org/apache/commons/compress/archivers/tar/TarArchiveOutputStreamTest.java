import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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

    @Test
    public void testConstructorWithOutputStream() {
        assertNull(tarArchiveOutputStream.getBuffer());
        assertNull(tarArchiveOutputStream.getAssembler());
        assertEquals(TarConstants.DEFAULT_BLKSIZE, tarArchiveOutputStream.getBlockSize());
        assertEquals(TarConstants.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testConstructorWithOutputStreamBlockSize() {
        assertNull(tarArchiveOutputStream.getBuffer());
        assertNull(tarArchiveOutputStream.getAssembler());
        assertEquals(512, tarArchiveOutputStream.getBlockSize());
        assertEquals(TarConstants.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testConstructorWithOutputStreamBlockSizeRecordSize() {
        assertNull(tarArchiveOutputStream.getBuffer());
        assertNull(tarArchiveOutputStream.getAssembler());
        assertEquals(512, tarArchiveOutputStream.getBlockSize());
        assertEquals(1024, tarArchiveOutputStream.getRecordSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullOutputStream() {
        new TarArchiveOutputStream(null);
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeWithInvalidMode() {
        tarArchiveOutputStream.setLongFileMode(3);
    }

    @Test
    public void testFinish() throws IOException {
        ArchiveEntry archiveEntry = createTestArchiveEntry("test.txt", "Test content.");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.finish();

        byte[] output = outputStream.toByteArray();
        assertEquals(2 * TarConstants.RECORD_LENGTH_512, output.length);
    }

    @Test(expected = IllegalStateException.class)
    public void testFinishWithoutPuttingAnEntry() throws IOException {
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testWrite() throws IOException {
        ArchiveEntry archiveEntry = createTestArchiveEntry("test.txt", "Test content.");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] testContent = "Test content.".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testContent, 0, testContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] output = outputStream.toByteArray();
        assertEquals(archiveEntry.getSize(), output.length);
    }

    @Test(expected = IOException.class)
    public void testWriteMoreBytesThanSpecifiedInArchiveEntry() throws IOException {
        ArchiveEntry archiveEntry = createTestArchiveEntry("test.txt", "Test content.");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] testContent = "Test content.".getBytes(StandardCharsets.UTF_8);
        byte[] moreBytes = "More bytes than specified in archive entry.".getBytes(StandardCharsets.UTF_8);

        tarArchiveOutputStream.write(testContent, 0, testContent.length);
        tarArchiveOutputStream.write(moreBytes, 0, moreBytes.length);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testWriteBeforeClosingEntry() throws IOException {
        ArchiveEntry archiveEntry = createTestArchiveEntry("test.txt", "Test content.");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] testContent = "Test content.".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testContent, 0, testContent.length);

        IOException exception = assertThrows(IOException.class, () -> tarArchiveOutputStream.closeArchiveEntry());
        assertEquals("Have unclosed entry in TarArchiveOutputStream", exception.getMessage());
    }

    @Test
    public void testClose() throws IOException {
        ArchiveEntry archiveEntry = createTestArchiveEntry("test.txt", "Test content.");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] testContent = "Test content.".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testContent, 0, testContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] output = outputStream.toByteArray();
        assertEquals(archiveEntry.getSize(), output.length);

        tarArchiveOutputStream.close();
    }

    @Test(expected = IllegalStateException.class)
    public void testCloseTwice() throws IOException {
        ArchiveEntry archiveEntry = createTestArchiveEntry("test.txt", "Test content.");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] testContent = "Test content.".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testContent, 0, testContent.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testWriteToClosedOutputStream() throws IOException {
        tarArchiveOutputStream.close();
        tarArchiveOutputStream.write(new byte[0], 0, 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testPutArchiveEntryWithoutClosingPreviousEntry() throws IOException {
        ArchiveEntry archiveEntry1 = createTestArchiveEntry("test1.txt", "Test content 1.");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry1);

        ArchiveEntry archiveEntry2 = createTestArchiveEntry("test2.txt", "Test content 2.");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry2);

        IOException exception = assertThrows(IllegalStateException.class, () -> tarArchiveOutputStream.finish());
        assertEquals("This archives contains unclosed entries.", exception.getMessage());
    }

    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryWithNonTarArchiveEntry() throws IOException {
        Object nonTarArchiveEntry = new Object();
        tarArchiveOutputStream.putArchiveEntry(nonTarArchiveEntry);
    }

    @Test
    public void testFlush() throws IOException {
        ArchiveEntry archiveEntry = createTestArchiveEntry("test.txt", "Test content.");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] testContent = "Test content.".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testContent, 0, testContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.flush();

        byte[] output = outputStream.toByteArray();
        assertEquals(archiveEntry.getSize(), output.length);
    }

    @Test
    public void testCreateArchiveEntry() {
        File testFile = Files.createTempFile("test", ".txt");
        ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(testFile, "Test content.");
        assertEquals("test" + File.separator + "test.txt", archiveEntry.getName());
        assertEquals(testFile.length(), archiveEntry.getSize());

        Files.delete(testFile);
    }

    private ArchiveEntry createTestArchiveEntry(String archiveEntryName, String archiveEntryContent) {
        Path testFilePath = Files.createTempFile("test", ".txt");
        try {
            Files.write(testFilePath, archiveEntryContent.getBytes(StandardCharsets.UTF_8));
            return tarArchiveOutputStream.createArchiveEntry(testFilePath.toFile(), archiveEntryName);
        } finally {
            Files.delete(testFilePath);
        }
    }
}
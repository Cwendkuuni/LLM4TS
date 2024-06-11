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
import java.util.Arrays;

import static org.junit.Assert.*;

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
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testConstructorWithOutputStreamBlockSize() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, 1024);
        assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testConstructorWithOutputStreamBlockSizeRecordSize() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, 1024, 512);
        assertNotNull(tarArchiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullOutputStream() {
        new TarArchiveOutputStream(null);
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", "test content".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        assertTrue(Arrays.equals(tarArchiveEntry.getSize(), tarArchiveOutputStream.getRecordSize()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryNullEntry() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        final String longFileName = "a" + TarConstants.NAMELEN + "b";
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(longFileName, "test content".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.finish();
        Path path = Files.newTempFile("test", "tar");
        Files.write(path, outputStream.toByteArray());
        byte[] bytes = Files.readAllBytes(path);
        Files.delete(path);
        int recordSize = tarArchiveOutputStream.getRecordSize();
        int expectedLength = (int) Math.ceil((long) longFileName.getBytes(StandardCharsets.UTF_8).length / (double) recordSize) * recordSize;
        int actualLength = Arrays.stream(Files.readAllBytes(path)).sum();
        assertEquals("Unexpected length of the output Tar file.", expectedLength, actualLength);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameLongPathModeError() throws IOException {
        final String longFileName = "a" + TarConstants.NAMELEN + "b";
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(longFileName, "test content".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", "test content".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.finish();
        Path path = Files.newTempFile("test", "tar");
        Files.write(path, outputStream.toByteArray());
        byte[] bytes = Files.readAllBytes(path);
        Files.delete(path);
        int recordSize = tarArchiveOutputStream.getRecordSize();
        int expectedLength = tarArchiveOutputStream.getSize() + 2 * recordSize;
        int actualLength = Arrays.stream(Files.readAllBytes(path)).sum();
        assertEquals("Unexpected length of the output Tar file.", expectedLength, actualLength);
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt", "test content".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.close();
        Path path = Files.newTempFile("test", "tar");
        Files.write(path, outputStream.toByteArray());
        byte[] bytes = Files.readAllBytes(path);
        Files.delete(path);
        int recordSize = tarArchiveOutputStream.getRecordSize();
        int expectedLength = tarArchiveOutputStream.getSize() + 2 * recordSize;
        int actualLength = Arrays.stream(Files.readAllBytes(path)).sum();
        assertEquals("Unexpected length of the output Tar file.", expectedLength, actualLength);
    }
}
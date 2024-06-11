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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Test class for TarArchiveOutputStream.
 */
public class TarArchiveOutputStreamTest {

    private File tempFile;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        tempFile = File.createTempFile("junit_test_", null);
        tarOutputStream = new TarArchiveOutputStream(new FileOutputStream(tempFile));
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
        Files.deleteIfExists(Paths.get(tempFile.getAbsolutePath()));
    }

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertNotNull(tarOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test_file.txt");
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.putArchiveEntry(testEntry);
        assertNotNull(tarOutputStream.getNextEntry());
        assertEquals(testEntry.getName(), tarOutputStream.getNextEntry().getName());
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "test_file_with_very_long_name.txt";
        TarArchiveEntry testEntry = new TarArchiveEntry(longFileName);
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream.putArchiveEntry(testEntry);
        assertNotNull(tarOutputStream.getNextEntry());
        assertEquals(testEntry.getName(), tarOutputStream.getNextEntry().getName());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test_file.txt");
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.write(new byte[1024]);
        tarOutputStream.closeArchiveEntry();
        assertNull(tarOutputStream.getNextEntry());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidEntry() throws IOException {
        Object invalidEntry = new Object();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.putArchiveEntry(invalidEntry);
    }

    @Test(expected = IOException.class)
    public void testWriteWithoutPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.write(new byte[1024]);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test_file.txt");
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.write(new byte[1024]);
        tarOutputStream.finish();
        assertFalse(tarOutputStream.hasNextEntry());
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test_file.txt");
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.write(new byte[1024]);
        tarOutputStream.close();
        assertFalse(tarOutputStream.hasNextEntry());
    }

    @Test
    public void testWriteRecord() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        byte[] record = new byte[TarConstants.STD_REC_SIZE];
        tarOutputStream.writeRecord(record);
        assertEquals(record, tarOutputStream.getBuffer().getRecordBuffer());
    }
}
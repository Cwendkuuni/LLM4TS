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
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class TarArchiveOutputStreamTest {

    private static final int BLOCK_SIZE = 1024;
    private static final int RECORD_SIZE = 512;
    private ByteArrayOutputStream outputStream;
    private TarOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream(1024 * 1024); // 1MB buffer
        tarOutputStream = new TarArchiveOutputStream(outputStream, BLOCK_SIZE, RECORD_SIZE);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
        outputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithShortFileName() throws IOException {
        String fileName = "testFile.txt";
        File inputFile = new File("src/test/resources/" + fileName);
        TarArchiveEntry entry = new TarArchiveEntry(inputFile, fileName);
        tarOutputStream.putArchiveEntry(entry);
        byte[] fileContent = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        tarOutputStream.write(fileContent, 0, fileContent.length);
        tarOutputStream.closeArchiveEntry();

        TarArchiveEntry readEntry = (TarArchiveEntry) tarOutputStream.getNextEntry();
        assertNotNull(readEntry);
        assertEquals(fileName, readEntry.getName());
        assertEquals(inputFile.length(), readEntry.getSize());
        assertFalse(readEntry.isDirectory());
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String fileName = "testFile/with/long/filename.txt";
        File inputFile = new File("src/test/resources/" + fileName);
        TarArchiveEntry entry = new TarArchiveEntry(inputFile, fileName);

        // Test long file name with error mode
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertThrows(RuntimeException.class, () -> tarOutputStream.putArchiveEntry(entry));

        // Test long file name with truncate mode
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        byte[] longFileNameBytes = fileName.getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry longLinkEntry = new TarArchiveEntry(TarConstants.GNU_LONGLINK,
                TarConstants.LF_GNUTYPE_LONGNAME);
        longLinkEntry.setSize(longFileNameBytes.length + 1); // +1 for NUL
        tarOutputStream.putArchiveEntry(longLinkEntry);
        tarOutputStream.write(longFileNameBytes);
        tarOutputStream.write(0); // NUL terminator
        tarOutputStream.closeArchiveEntry();

        // Test long file name with GNU mode
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        TarArchiveEntry newEntry = new TarArchiveEntry(inputFile, fileName);
        tarOutputStream.putArchiveEntry(newEntry);
        byte[] nameBytes = fileName.getBytes(StandardCharsets.UTF_8);
        tarOutputStream.write(nameBytes);
        tarOutputStream.write(0); // NUL terminator
        tarOutputStream.closeArchiveEntry();

        TarArchiveEntry readEntry = (TarArchiveEntry) tarOutputStream.getNextEntry();
        assertNotNull(readEntry);
        assertEquals(fileName, readEntry.getName());
        assertEquals(inputFile.length(), readEntry.getSize());
        assertFalse(readEntry.isDirectory());
    }

    @Test
    public void testPutArchiveEntryWithDirectory() throws IOException {
        String dirName = "testDir";
        File directory = new File("src/test/resources/" + dirName);
        TarArchiveEntry entry = new TarArchiveEntry(directory, dirName);
        TarArchiveEntry[] entries = directory.listEntries();

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();

        TarArchiveEntry readEntry = (TarArchiveEntry) tarOutputStream.getNextEntry();
        assertNotNull(readEntry);
        assertEquals(dirName, readEntry.getName());
        assertTrue(readEntry.isDirectory());

        for (TarArchiveEntry fileEntry : Arrays.asList(entries)) {
            byte[] fileNameBytes = fileEntry.getName().getBytes(StandardCharsets.UTF_8);
            tarOutputStream.putArchiveEntry(fileEntry);
            tarOutputStream.write(fileNameBytes);
            tarOutputStream.write(0); // NUL terminator
            tarOutputStream.closeArchiveEntry();
        }

        TarArchiveEntry nextEntry = null;

        while ((nextEntry = (TarArchiveEntry) tarOutputStream.getNextEntry()) != null) {
            assertFalse(nextEntry.isDirectory());
        }
    }

    @Test
    public void testFinishAndClose() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.close();

        byte[] expectedEOFRecord = new byte[RECORD_SIZE];
        Arrays.fill(expectedEOFRecord, (byte) 0);
        assertArrayEquals(expectedEOFRecord, outputStream.toByteArray(), 0, RECORD_SIZE);
    }
}
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private File tempFile;
    private TarArchiveOutputStream tarStream;

    @Before
    public void setUp() throws IOException {
        tempFile = File.createTempFile("junit-test-", ".tar");
        tarStream = new TarArchiveOutputStream(new FileOutputStream(tempFile));
    }

    @After
    public void tearDown() throws IOException {
        if (tarStream != null) {
            tarStream.finish();
            tarStream.close();
        }
        if (tempFile != null) {
            tempFile.delete();
        }
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        // Create a test entry
        File testFile = File.createTempFile("test-", ".txt");
        String testName = "test-entry.txt";
        TarArchiveEntry testEntry = tarStream.createArchiveEntry(testFile, testName);

        // Validate the test entry
        assertNotNull(testEntry);
        assertEquals(testName, testEntry.getName());
        assertEquals(testFile.length(), testEntry.getSize());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidArgument() throws IOException {
        // Test putArchiveEntry with null argument
        tarStream.putArchiveEntry(null);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        // Create a test entry
        File testFile = File.createTempFile("test-", ".txt");
        String testName = "test-entry.txt";
        TarArchiveEntry testEntry = tarStream.createArchiveEntry(testFile, testName);

        // Put the test entry
        tarStream.putArchiveEntry(testEntry);

        // Validate the test entry was written correctly
        TarArchiveEntry readEntry = (TarArchiveEntry) tarStream.getNextEntry();
        assertNotNull(readEntry);
        assertEquals(testName, readEntry.getName());
        assertEquals(testFile.length(), readEntry.getSize());
        assertEquals(0, tarStream.getNextEntry() instanceof TarArchiveEntry ? 1 : -1);

        // Write some data to the test entry
        byte[] testData = "This is a test entry".getBytes(StandardCharsets.UTF_8);
        tarStream.write(testData, 0, testData.length);
        tarStream.closeArchiveEntry();

        // Read the data back from the archive
        byte[] readData = new byte[testData.length];
        tarStream.read(readData, 0, testData.length);
        assertEquals(testData, readData);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryAfterFinish() throws IOException {
        // Test putArchiveEntry after finish
        tarStream.finish();
        tarStream.putArchiveEntry(new TarArchiveEntry("test-entry.txt"));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithUnclosedEntry() throws IOException {
        // Test putArchiveEntry with an unclosed entry
        File testFile = File.createTempFile("test-", ".txt");
        String testName = "test-entry.txt";
        TarArchiveEntry testEntry = tarStream.createArchiveEntry(testFile, testName);

        // Put the test entry
        tarStream.putArchiveEntry(testEntry);

        // Validate the test entry was written correctly
        TarArchiveEntry readEntry = (TarArchiveEntry) tarStream.getNextEntry();
        assertNotNull(readEntry);
        assertEquals(testName, readEntry.getName());
        assertEquals(testFile.length(), readEntry.getSize());
        assertFalse(tarStream.hasNextEntry());

        // Attempt to put another entry, should fail
        tarStream.putArchiveEntry(new TarArchiveEntry("test-entry2.txt"));
    }

    @Test
    public void testFinish() throws IOException {
        // Test finish
        File testFile1 = File.createTempFile("test-", ".txt");
        TarArchiveEntry testEntry1 = tarStream.createArchiveEntry(testFile1, "test-entry1.txt");
        tarStream.putArchiveEntry(testEntry1);

        File testFile2 = File.createTempFile("test-", ".txt");
        TarArchiveEntry testEntry2 = tarStream.createArchiveEntry(testFile2, "test-entry2.txt");
        tarStream.putArchiveEntry(testEntry2);

        tarStream.finish();

        // Validate the archive contains the expected entries
        TarArchiveEntry readEntry1 = (TarArchiveEntry) tarStream.getNextEntry();
        assertEquals("test-entry1.txt", readEntry1.getName());
        assertEquals(testFile1.length(), readEntry1.getSize());

        TarArchiveEntry readEntry2 = (TarArchiveEntry) tarStream.getNextEntry();
        assertEquals("test-entry2.txt", readEntry2.getName());
        assertEquals(testFile2.length(), readEntry2.getSize());

        assertNull(tarStream.getNextEntry());
    }
}
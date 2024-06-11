import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private File tempFile;
    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        tempFile = File.createTempFile("junit", "tar");
        outputStream = new FileOutputStream(tempFile);
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws Exception {
        tarArchiveOutputStream.close();
        outputStream.close();
        IOUtils.deleteQuietly(tempFile);
    }

    @Test
    public void testPutArchiveEntryAndClose() throws IOException {
        // Create a test entry
        String entryName = "test.txt";
        File inputFile = new File("src/test/resources/test.txt");
        TarArchiveEntry archiveEntry = new TarArchiveEntry(inputFile, entryName);

        // Put the archive entry and write its contents
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] fileContent = IOUtils.toByteArray(new FileInputStream(inputFile));
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        // Check the output file
        byte[] expectedFileContent = IOUtils.toByteArray(new FileInputStream(tempFile));
        byte[] actualFileContent = new byte[expectedFileContent.length + archiveEntry.getSize()];
        System.arraycopy(expectedFileContent, 0, actualFileContent, 0, expectedFileContent.length);
        assertEquals(actualFileContent, expectedFileContent);

        // Check the length of the output file
        assertEquals(expectedFileContent.length + archiveEntry.getSize(), tempFile.length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryNullName() throws IOException {
        // Create a null name archive entry
        TarArchiveEntry archiveEntry = new TarArchiveEntry(null, "test.txt");

        // Try to put the archive entry
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryNullFile() throws IOException {
        // Create a null file archive entry
        TarArchiveEntry archiveEntry = new TarArchiveEntry(null, "test.txt");

        // Try to put the archive entry
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryNullOutputStream() throws IOException {
        // Create a null output stream
        OutputStream outputStream = null;
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        // Try to put an archive entry
        TarArchiveEntry archiveEntry = new TarArchiveEntry(new File("src/test/resources/test.txt"), "test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryNegativeSize() throws IOException {
        // Create a test entry with a negative size
        String entryName = "test.txt";
        File inputFile = new File("src/test/resources/test.txt");
        TarArchiveEntry archiveEntry = new TarArchiveEntry(inputFile, entryName);
        archiveEntry.setSize(-1);

        // Try to put the archive entry
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryLongFileName() throws IOException {
        // Create a test entry with a long name
        String entryName = "test_long_name_" + new String(new char[TarConstants.NAMELEN + 1]) + ".txt";
        File inputFile = new File("src/test/resources/test.txt");
        TarArchiveEntry archiveEntry = new TarArchiveEntry(inputFile, entryName);

        // Set long file name mode
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        // Try to put the archive entry
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryAfterFinish() throws IOException {
        // Finish the archive
        tarArchiveOutputStream.finish();

        // Try to put a new archive entry
        TarArchiveEntry archiveEntry = new TarArchiveEntry(new File("src/test/resources/test.txt"), "test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryBeforePutArchiveEntry() throws IOException {
        // Try to close an archive entry before putting a new one
        TarArchiveEntry archiveEntry = new TarArchiveEntry(new File("src/test/resources/test.txt"), "test.txt");
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test(expected = IOException.class)
    public void testWriteAfterFinish() throws IOException {
        // Finish the archive
        tarArchiveOutputStream.finish();

        // Try to write data to the archive
        byte[] data = new byte[1024];
        tarArchiveOutputStream.write(data, 0, data.length);
    }

    @Test(expected = IOException.class)
    public void testWriteAfterClose() throws IOException {
        // Close the archive
        tarArchiveOutputStream.close();

        // Try to write data to the archive
        byte[] data = new byte[1024];
        tarArchiveOutputStream.write(data, 0, data.length);
    }
}
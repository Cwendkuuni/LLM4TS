import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    private File testFile;
    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        testFile = File.createTempFile("test", null);
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(outputStream));
    }

    @After
    public void tearDown() throws Exception {
        tarOutputStream.close();
        testFile.delete();
    }

    @Test
    public void testCreateWithOutputStream() {
        // Create a TarArchiveOutputStream with an OutputStream
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        assertNotNull(tarOutputStream);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateWithNullOutputStream() {
        // Create a TarArchiveOutputStream with a null OutputStream
        new TarArchiveOutputStream(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNegativeBlockSize() {
        // Create a TarArchiveOutputStream with a negative block size
        new TarArchiveOutputStream(outputStream, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNegativeRecordSize() {
        // Create a TarArchiveOutputStream with a negative record size
        new TarArchiveOutputStream(outputStream, 1, -1);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        // Create a TarArchiveEntry and put it into the TarArchiveOutputStream
        TarArchiveEntry tarEntry = new TarArchiveEntry("testFileName", "testContent");
        tarOutputStream.putNextEntry(tarEntry);
        tarOutputStream.write("testContent".getBytes());
        tarOutputStream.closeArchiveEntry();

        // Create an InputStream from the TarArchiveOutputStream output
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry testEntry = new TarArchiveEntry(inputStream);

        // Check that the TarArchiveEntry name and size match the input
        assertEquals("testFileName", testEntry.getName());
        assertEquals("testContent".length(), testEntry.getSize());

        // Read the contents of the TarArchiveEntry and check they match the input
        byte[] expectedContent = "testContent".getBytes();
        byte[] actualContent = new byte[expectedContent.length];
        int bytesRead = inputStream.read(actualContent);
        assertEquals(expectedContent.length, bytesRead);
        assertArrayEquals(expectedContent, actualContent);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        // Try to put a null TarArchiveEntry into the TarArchiveOutputStream
        tarOutputStream.putNextEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNonTarArchiveEntry() throws IOException {
        // Try to put a non-TarArchiveEntry into the TarArchiveOutputStream
        ArchiveEntry nonTarEntry = new File("testFile").toPath().toJavaFileObject();
        tarOutputStream.putNextEntry(nonTarEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithUnclosedEntry() throws IOException {
        // Try to put a new entry without closing the previous one
        TarArchiveEntry tarEntry1 = new TarArchiveEntry("testFileName1", "testContent1");
        tarOutputStream.putNextEntry(tarEntry1);
        tarOutputStream.write("testContent1".getBytes());

        TarArchiveEntry tarEntry2 = new TarArchiveEntry("testFileName2", "testContent2");
        tarOutputStream.putNextEntry(tarEntry2);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "a" + TarConstants.NAMELEN + "b";
        TarArchiveEntry tarEntry = new TarArchiveEntry(longFileName, "testContent");
        tarOutputStream.putNextEntry(tarEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameAndLongFileModeError() throws IOException {
        String longFileName = "a" + TarConstants.NAMELEN + "b";
        TarArchiveEntry tarEntry = new TarArchiveEntry(longFileName, "testContent");
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        tarOutputStream.putNextEntry(tarEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameAndLongFileModeTruncate() throws IOException {
        String longFileName = "a" + TarConstants.NAMELEN + "b";
        TarArchiveEntry tarEntry = new TarArchiveEntry(longFileName, "testContent");
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOutputStream.putNextEntry(tarEntry);
    }

    @Test
    public void testFinish() throws IOException {
        // Write some data to the TarArchiveOutputStream
        TarArchiveEntry tarEntry = new TarArchiveEntry("testFileName", "testContent");
        tarOutputStream.putNextEntry(tarEntry);
        tarOutputStream.write("testContent".getBytes());
        tarOutputStream.closeArchiveEntry();

        // Finish the TarArchiveOutputStream
        tarOutputStream.finish();

        // Create an InputStream from the TarArchiveOutputStream output
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        // Read the entire archive and check that it matches the expected format
        byte[] expectedHeader = Arrays.copyOfRange(TarConstants.MAGIC, 0, TarConstants.MAGIC.length);
        byte[] actualHeader = new byte[expectedHeader.length];
        int bytesRead = inputStream.read(actualHeader);
        assertEquals(expectedHeader.length, bytesRead);
        assertArrayEquals(expectedHeader, actualHeader);

        int recordSize = tarOutputStream.getRecordSize();
        int blockSize = tarOutputStream.getBlockSize();

        TarArchiveEntry testEntry = new TarArchiveEntry(inputStream);

        // Check that the first entry header matches the input
        assertEquals("testFileName", testEntry.getName());
        assertEquals("testContent".length(), testEntry.getSize());

        // Read the contents of the first entry and check they match the input
        expectedContent = "testContent".getBytes();
        actualContent = new byte[expectedContent.length];
        bytesRead = inputStream.read(actualContent);
        assertEquals(expectedContent.length, bytesRead);
        assertArrayEquals(expectedContent, actualContent);

        // Check that the second entry header is null and the EOF record has been written
        TarArchiveEntry nextEntry = new TarArchiveEntry(inputStream);
        assertNull(nextEntry);

        long bytesWritten = outputStream.size();
        assertEquals(2 * (recordSize + TarConstants.HEADER_SIZE), bytesWritten);
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnfinishedArchive() throws IOException {
        // Try to finish an unfinished archive
        tarOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        // Try to finish an archive with an unclosed entry
        TarArchiveEntry tarEntry = new TarArchiveEntry("testFileName", "testContent");
        tarOutputStream.putNextEntry(tarEntry);
        tarOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryAfterFinish() throws IOException {
        // Try to close an entry after finish
        TarArchiveEntry tarEntry = new TarArchiveEntry("testFileName", "testContent");
        tarOutputStream.putNextEntry(tarEntry);
        tarOutputStream.finish();
        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testClose() throws IOException {
        // Write some data to the TarArchiveOutputStream
        TarArchiveEntry tarEntry = new TarArchiveEntry("testFileName", "testContent");
        tarOutputStream.putNextEntry(tarEntry);
        tarOutputStream.write("testContent".getBytes());
        tarOutputStream.closeArchiveEntry();

        // Finish and close the TarArchiveOutputStream
        tarOutputStream.finish();
        tarOutputStream.close();

        // Create an InputStream from the TarArchiveOutputStream output
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        // Read the entire archive and check that it matches the expected format
        byte[] expectedHeader = Arrays.copyOfRange(TarConstants.MAGIC, 0, TarConstants.MAGIC.length);
        byte[] actualHeader = new byte[expectedHeader.length];
        int bytesRead = inputStream.read(actualHeader);
        assertEquals(expectedHeader.length, bytesRead);
        assertArrayEquals(expectedHeader, actualHeader);

        int recordSize = tarOutputStream.getRecordSize();
        int blockSize = tarOutputStream.getBlockSize();

        TarArchiveEntry testEntry = new TarArchiveEntry(inputStream);

        // Check that the first entry header matches the input
        assertEquals("testFileName", testEntry.getName());
        assertEquals("testContent".length(), testEntry.getSize());

        // Read the contents of the first entry and check they match the input
        byte[] expectedContent = "testContent".getBytes();
        byte[] actualContent = new byte[expectedContent.length];
        bytesRead = inputStream.read(actualContent);
        assertEquals(expectedContent.length, bytesRead);
        assertArrayEquals(expectedContent, actualContent);

        // Check that the second entry header is null and the EOF record has been written
        TarArchiveEntry nextEntry = new TarArchiveEntry(inputStream);
        assertNull(nextEntry);

        // The TarArchiveOutputStream should be closed
        assertFalse(tarOutputStream.isOpen());
    }

    @Test(expected = IOException.class)
    public void testCloseAfterFinishWithUnfinishedArchive() throws IOException {
        // Try to close an unfinished archive
        tarOutputStream.finish();
        tarOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testCloseAfterFinishWithUnclosedEntry() throws IOException {
        // Try to close an archive with an unclosed entry
        TarArchiveEntry tarEntry = new TarArchiveEntry("testFileName", "testContent");
        tarOutputStream.putNextEntry(tarEntry);
        tarOutputStream.finish();
        tarOutputStream.close();
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        // Set the long file mode to LONGFILE_TRUNCATE and write a long file name
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        TarArchiveEntry tarEntry = new TarArchiveEntry("a" + TarConstants.NAMELEN + "b", "testContent");
        tarOutputStream.putNextEntry(tarEntry);
        tarOutputStream.write("testContent".getBytes());
        tarOutputStream.closeArchiveEntry();

        // Finish and check that the long file name has been truncated
        tarOutputStream.finish();
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry testEntry = new TarArchiveEntry(inputStream);
        assertEquals("a" + TarConstants.NAMELEN + "b", testEntry.getName());
        assertNull(testEntry.getFileLength());
    }
}
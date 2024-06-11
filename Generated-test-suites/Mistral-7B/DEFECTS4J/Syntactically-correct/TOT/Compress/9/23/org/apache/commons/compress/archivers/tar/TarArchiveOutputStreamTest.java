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
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setup() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
        IOUtils.closeQuietly(outputStream);
    }

    @Test
    public void testPutArchiveEntryAndWriteBytes() throws IOException {
        // Create an entry, write some bytes to it, and close it
        String fileName = "testFile.txt";
        TarArchiveEntry entry = new TarArchiveEntry(fileName);
        tarOutputStream.putArchiveEntry(entry);
        byte[] testData = "Test data for TarArchiveOutputStreamTest".getBytes();
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();

        // Read back the data and verify it
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry readEntry = new TarArchiveEntry(inputStream);
        byte[] readData = new byte[testData.length];
        inputStream.read(readData, 0, testData.length);
        assertEquals(new String(testData), new String(readData));
        assertFalse(inputStream.available() > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        tarOutputStream.putArchiveEntry(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithNullFileName() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(null);
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "a" + new String(new char[256]).replace("\0", "a");
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameAndErrorMode() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), TarArchiveOutputStream.LONGFILE_ERROR);
        String longFileName = "a" + new String(new char[256]).replace("\0", "a");
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameAndTruncateMode() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), TarArchiveOutputStream.LONGFILE_TRUNCATE);
        String longFileName = "a" + new String(new char[256]).replace("\0", "a");
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryWithNonTarArchiveEntry() throws IOException {
        File file = new File("testFile.txt");
        ArchiveEntry archiveEntry = new FileArchiveEntry(file);
        tarOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithExistingEntry() throws IOException {
        String fileName = "testFile.txt";
        TarArchiveEntry entry1 = new TarArchiveEntry(fileName);
        tarOutputStream.putArchiveEntry(entry1);
        tarOutputStream.finish();
        tarOutputStream.reset();

        TarArchiveEntry entry2 = new TarArchiveEntry(fileName);
        tarOutputStream.putArchiveEntry(entry2);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithUnclosedEntry() throws IOException {
        String fileName = "testFile.txt";
        TarArchiveEntry entry = new TarArchiveEntry(fileName);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();

        tarOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDirectoryEntry() throws IOException {
        String dirName = "testDir";
        TarArchiveEntry entry = new TarArchiveEntry(dirName, true);
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testWriteBytesWithClosedStream() throws IOException {
        byte[] testData = "Test data for TarArchiveOutputStreamTest".getBytes();
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.close();
        tarOutputStream.write(testData, 0, testData.length);
    }

    @Test(expected = IOException.class)
    public void testWriteBytesWithUnclosedEntry() throws IOException {
        String fileName = "testFile.txt";
        TarArchiveEntry entry = new TarArchiveEntry(fileName);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(new byte[1024], 0, 1024);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.write(new byte[1024], 0, 1024);
    }

    @Test
    public void testFinish() throws IOException {
        String fileName1 = "testFile1.txt";
        String fileName2 = "testFile2.txt";
        TarArchiveEntry entry1 = new TarArchiveEntry(fileName1);
        TarArchiveEntry entry2 = new TarArchiveEntry(fileName2);

        tarOutputStream.putArchiveEntry(entry1);
        tarOutputStream.write(new byte[1024], 0, 1024);
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.putArchiveEntry(entry2);
        tarOutputStream.write(new byte[1024], 0, 1024);
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry readEntry1 = new TarArchiveEntry(inputStream);
        TarArchiveEntry readEntry2 = new TarArchiveEntry(inputStream);

        assertEquals(fileName1, readEntry1.getName());
        assertEquals(fileName2, readEntry2.getName());
        assertTrue(inputStream.available() == 0);
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        String fileName = "testFile.txt";
        TarArchiveEntry entry = new TarArchiveEntry(fileName);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(new byte[1024], 0, 1024);
        tarOutputStream.finish();
    }

    @Test
    public void testReset() throws IOException {
        String fileName1 = "testFile1.txt";
        String fileName2 = "testFile2.txt";
        TarArchiveEntry entry1 = new TarArchiveEntry(fileName1);
        TarArchiveEntry entry2 = new TarArchiveEntry(fileName2);

        tarOutputStream.putArchiveEntry(entry1);
        tarOutputStream.write(new byte[1024], 0, 1024);
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.putArchiveEntry(entry2);
        tarOutputStream.write(new byte[1024], 0, 1024);
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.finish();

        tarOutputStream.reset();

        TarArchiveEntry resetEntry1 = new TarArchiveEntry(tarOutputStream);
        TarArchiveEntry resetEntry2 = new TarArchiveEntry(tarOutputStream);

        assertEquals(null, resetEntry1);
        assertEquals(null, resetEntry2);
    }
}
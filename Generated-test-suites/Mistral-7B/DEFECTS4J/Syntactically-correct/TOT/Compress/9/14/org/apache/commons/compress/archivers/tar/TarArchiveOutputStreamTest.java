import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TarArchiveOutputStreamTest {

    private File testFile;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        testFile = new File("testArchive.tar");
        tarArchiveOutputStream = new TarArchiveOutputStream(new FileOutputStream(testFile));
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
        testFile.delete();
    }

    @Test
    public void testPutArchiveEntryAndClose() throws IOException {
        // Put an entry
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        // Write some data to the entry
        String data = "This is a test.";
        tarArchiveOutputStream.write(data.getBytes());

        // Close the entry
        tarArchiveOutputStream.closeArchiveEntry();

        // Verify the entry was written correctly
        ArchiveEntry readEntry = tarArchiveOutputStream.getNextEntry();
        assertNotNull(readEntry);
        assertEquals(entry.getName(), readEntry.getName());
        assertEquals(entry.getSize(), readEntry.getSize());

        // Read the data back and verify it's the same
        byte[] readData = new byte[data.length()];
        tarArchiveOutputStream.read(readData, 0, data.length());
        assertEquals(data, new String(readData));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutClose() throws IOException {
        // Put an entry
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        // Write some data to the entry
        String data = "This is a test.";
        tarArchiveOutputStream.write(data.getBytes());

        // Do not close the entry
        tarArchiveOutputStream.getNextEntry(); // consume the next entry

        // Attempt to put another entry
        TarArchiveEntry entry2 = new TarArchiveEntry("test2.txt");
        tarArchiveOutputStream.putArchiveEntry(entry2);
    }

    @Test(expected = IOException.class)
    public void testLongFileName() throws IOException {
        String longFileName = "a/long/file/name/with/more/than/100/characters.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarArchiveOutputStream.putArchiveEntry(entry);

        // Throws an exception if the longFileMode is set to LONGFILE_ERROR
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        tarArchiveOutputStream.write(longFileName.getBytes());
    }

    @Test
    public void testLongFileNameWithLongFileMode() throws IOException {
        String longFileName = "a/long/file/name/with/more/than/100/characters.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);

        // Set the longFileMode to LONGFILE_GNU
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        // Write the long file name as a separate entry
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(longFileName.getBytes());
        tarArchiveOutputStream.write(0); // NUL terminator
        tarArchiveOutputStream.closeArchiveEntry();

        // Write the data for the file
        String data = "This is a test.";
        tarArchiveOutputStream.write(data.getBytes());
        tarArchiveOutputStream.closeArchiveEntry();

        // Verify the entry was written correctly
        ArchiveEntry readEntry = tarArchiveOutputStream.getNextEntry();
        assertNotNull(readEntry);
        assertEquals(entry.getName(), readEntry.getName());
        assertEquals(entry.getSize(), data.length() + 1); // +1 for NUL terminator

        // Read the data back and verify it's the same
        byte[] readData = new byte[data.length()];
        tarArchiveOutputStream.read(readData, 0, data.length());
        assertEquals(data, new String(readData));
    }
}
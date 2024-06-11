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
import static org.junit.Assert.assertThrows;

/**
 * Tests for {TarArchiveOutputStream} class.
 */
public class TarArchiveOutputStreamTest {

    private File outputFile;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        outputFile = new File("test.tar");
        tarArchiveOutputStream = new TarArchiveOutputStream(new FileOutputStream(outputFile));
    }

    @After
    public void tearDown() throws Exception {
        IOUtils.closeQuietly(tarArchiveOutputStream);
        outputFile.delete();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        final String entryName = "testEntry.txt";
        final String expectedEntryContent = "Hello, World!";

        final byte[] entryContentBytes = expectedEntryContent.getBytes(StandardCharsets.UTF_8);

        final File inputFile = new File("testInput.txt");
        inputFile.createNewFile();
        IOUtils.write(expectedEntryContent.getBytes(StandardCharsets.UTF_8), inputFile, false);

        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry(inputFile, entryName));
        tarArchiveOutputStream.write(entryContentBytes, 0, entryContentBytes.length);
        tarArchiveOutputStream.closeArchiveEntry();

        final TarArchiveEntry archiveEntry = tarArchiveOutputStream.getNextEntry();
        assertNotNull(archiveEntry);
        assertEquals(entryName, archiveEntry.getName());

        // Test reading the content
        final byte[] readContent = new byte[entryContentBytes.length];
        tarArchiveOutputStream.read(readContent, 0, readContent.length);
        assertEquals(expectedEntryContent, new String(readContent, StandardCharsets.UTF_8));

        archive.close();
    }

    @Test
    public void testPutArchiveEntryWithDirectory() throws IOException {
        final String directoryName = "testDir";

        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry(directoryName));
        tarArchiveOutputStream.closeArchiveEntry();

        final ArchiveEntry archiveEntry = tarArchiveOutputStream.getNextEntry();
        assertTrue(archiveEntry instanceof TarArchiveEntry);
        assertEquals(directoryName, ((TarArchiveEntry) archiveEntry).getName());
        assertFalse(((TarArchiveEntry) archiveEntry).isDirectory());
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        final String longEntryName = "a/long/entry/name/with/more/than/100/chars.txt";

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry(longEntryName));
        tarArchiveOutputStream.finish();

        final ArchiveEntry archiveEntry = tarArchiveOutputStream.getNextEntry();
        assertTrue(archiveEntry instanceof TarArchiveEntry);
        assertEquals(longEntryName, ((TarArchiveEntry) archiveEntry).getName());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName_LongFileNameError()
            throws IOException {
        final String longEntryName = "a/long/entry/name/with/more/than/100/chars.txt";

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);

        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry(longEntryName));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName_LongFileNameTruncate()
            throws IOException {
        final String longEntryName = "a/long/entry/name/with/more/than/100/chars.txt";

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry(longEntryName));
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        final String entryName = "testEntry.txt";
        final String expectedEntryContent = "Hello, World!";

        final byte[] entryContentBytes = expectedEntryContent.getBytes(StandardCharsets.UTF_8);

        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry(entryName));
        tarArchiveOutputStream.write(entryContentBytes, 0, entryContentBytes.length);

        assertThrows(IOException.class, () -> tarArchiveOutputStream.closeArchiveEntry());

        tarArchiveOutputStream.closeArchiveEntry();

        final TarArchiveEntry archiveEntry = tarArchiveOutputStream.getNextEntry();
        assertNull(archiveEntry);
    }

    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.finish();

        final ArchiveEntry archiveEntry = tarArchiveOutputStream.getNextEntry();
        assertNull(archiveEntry);
    }
}
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class TarArchiveOutputStreamTest {

    private File testFile;
    private OutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        testFile = new File("test.tar");
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    @After
    public void tearDown() throws IOException {
        if (tarOutputStream != null) {
            tarOutputStream.close();
        }
        if (testFile != null && testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testPutArchiveEntryWithValidFileName() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] testData = "Hello World".getBytes(StandardCharsets.UTF_8);
        entry.setSize(testData.length);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry testEntry = new TarArchiveEntry(inputStream);
        assertEquals("test.txt", testEntry.getName());
        assertEquals(testData.length, testEntry.getSize());
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "longFileNameThatIsLongerThanNameLength.txt";
        ArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();

        // Check long file name is truncated
        assertFalse(new File(testFile, longFileName).exists());

        // Check that the archive contains the entry with truncated name
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry testEntry = new TarArchiveEntry(inputStream);
        assertEquals("longFileNameThatIsLongerThanNameLength.txt", testEntry.getName().substring(0, TarConstants.NAMELEN));
    }

    @Test
    public void testPutArchiveEntryWithInvalidFileName() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test\0.txt");
        tarOutputStream.putArchiveEntry(entry);
        assertThrows(IllegalArgumentException.class, () -> tarOutputStream.write(new byte[0], 0, 0));
        tarOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        assertNull(new TarArchiveEntry(inputStream));
    }

    @Test
    public void testPutArchiveEntryWithDirectory() throws IOException {
        ArchiveEntry directoryEntry = new TarArchiveEntry("test");
        directoryEntry.setDirectory(true);
        tarOutputStream.putArchiveEntry(directoryEntry);
        tarOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry testEntry = new TarArchiveEntry(inputStream);
        assertEquals("test/", testEntry.getName());
        assertFalse(testEntry.isFile());
    }

    @Test
    public void testCloseArchiveEntryWithUnwrittenData() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] testData = "Hello World".getBytes(StandardCharsets.UTF_8);
        entry.setSize(testData.length);
        tarOutputStream.putArchiveEntry(entry);

        assertThrows(IllegalStateException.class, () -> tarOutputStream.closeArchiveEntry());
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testCloseArchiveEntryWithUnclosedEntry() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] testData = "Hello World".getBytes(StandardCharsets.UTF_8);
        entry.setSize(testData.length);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(testData, 0, testData.length);

        assertThrows(IllegalStateException.class, () -> tarOutputStream.putArchiveEntry(new TarArchiveEntry("test2.txt")));
        tarOutputStream.closeArchiveEntry();

        byte[] archiveBytes = outputStream.toByteArray();
        ByteArrayInputStream archiveInputStream = new ByteArrayInputStream(archiveBytes);
        GZIPInputStream gzipInputStream = new GZIPInputStream(archiveInputStream);

        TarArchiveEntry testEntry = new TarArchiveEntry(gzipInputStream);
        assertEquals("test.txt", testEntry.getName());
    }

    @Test
    public void testFinishWithoutPuttingAnyEntries() throws IOException {
        tarOutputStream.finish();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        assertNull(new TarArchiveEntry(inputStream));
    }

    @Test
    public void testFinishWithPuttingEntries() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] testData = "Hello World".getBytes(StandardCharsets.UTF_8);
        entry.setSize(testData.length);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();

        TarArchiveEntry testEntry = new TarArchiveEntry(new ByteArrayInputStream(outputStream.toByteArray()));
        assertEquals("test.txt", testEntry.getName());
        assertEquals(testData.length, testEntry.getSize());

        tarOutputStream.finish();
    }
}
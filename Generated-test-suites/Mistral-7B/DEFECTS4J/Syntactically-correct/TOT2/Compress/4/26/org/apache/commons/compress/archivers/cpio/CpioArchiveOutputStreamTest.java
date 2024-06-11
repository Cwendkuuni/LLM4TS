import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CpioArchiveOutputStreamTest {

    private File tempFile;
    private CpioArchiveOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        tempFile = File.createTempFile("test", ".cpio");
        outputStream = new CpioArchiveOutputStream(new FileOutputStream(tempFile));
    }

    @After
    public void tearDown() throws IOException {
        outputStream.close();
        tempFile.delete();
    }

    @Test
    public void testPutArchiveEntryWithValidParameters() throws IOException {
        String entryName = "testEntry";
        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(testData.length);

        outputStream.putArchiveEntry(entry);
        outputStream.write(testData, 0, testData.length);
        outputStream.closeArchiveEntry();

        ArchiveEntry archiveEntry = outputStream.getNextEntry();
        assertNotNull(archiveEntry);
        assertEquals(entryName, archiveEntry.getName());
        assertEquals(testData.length, archiveEntry.getSize());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        outputStream.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateEntry() throws IOException {
        String entryName = "testEntry";
        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(testData.length);

        outputStream.putArchiveEntry(entry);
        outputStream.write(testData, 0, testData.length);
        outputStream.closeArchiveEntry();

        entry = new CpioArchiveEntry();
        entry.setName(entryName);
        outputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        short invalidFormat = 6;
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setFormat(invalidFormat);

        outputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidName() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(null);

        outputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullInputFile() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry(null, "testEntry");

        outputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithEmptyName() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("", "testEntry");

        outputStream.putArchiveEntry(entry);
    }

    @Test
    public void testWriteAndCloseArchiveEntry() throws IOException {
        String entryName = "testEntry";
        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(testData.length);

        outputStream.putArchiveEntry(entry);
        outputStream.write(testData, 0, testData.length);
        outputStream.closeArchiveEntry();

        ArchiveEntry archiveEntry = outputStream.getNextEntry();
        byte[] outputData = new ByteArrayOutputStream(testData.length).toByteArray();
        outputStream.read(outputData, 0, testData.length);

        assertEquals(testData, outputData);
        assertEquals(entryName, archiveEntry.getName());
        assertEquals(testData.length, archiveEntry.getSize());
    }

    @Test
    public void testWriteAndCloseArchiveEntryWithEmptyData() throws IOException {
        String entryName = "testEntry";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(0);

        outputStream.putArchiveEntry(entry);
        outputStream.write(new byte[0], 0, 0);
        outputStream.closeArchiveEntry();

        ArchiveEntry archiveEntry = outputStream.getNextEntry();
        assertEquals(entryName, archiveEntry.getName());
        assertEquals(0, archiveEntry.getSize());
    }

    @Test(expected = IOException.class)
    public void testWriteAndCloseArchiveEntryWithInvalidSize() throws IOException {
        String entryName = "testEntry";
        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(-testData.length);

        outputStream.putArchiveEntry(entry);
        outputStream.write(testData, 0, testData.length);
        outputStream.closeArchiveEntry();
    }

    @Test
    public void testFinish() throws IOException {
        ArchiveEntry archiveEntry = null;
        outputStream.finish();
        archiveEntry = outputStream.getNextEntry();
        assertEquals(CPIO_TRAILER, archiveEntry.getName());
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        String entryName = "testEntry";
        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(testData.length);

        outputStream.putArchiveEntry(entry);
        outputStream.write(testData, 0, testData.length);

        outputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        ArchiveEntry archiveEntry = null;
        outputStream.close();
        archiveEntry = outputStream.getNextEntry();
        assertFalse(archiveEntry.isDirectory());
        assertFalse(archiveEntry.isArchiveEntry());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File file = new File("testFile.txt");
        CpioArchiveEntry entry = outputStream.createArchiveEntry(file, "testEntry");

        assertEquals(file.getName(), entry.getName());
        assertEquals(file.length(), entry.getSize());
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveEntryWithNullFile() throws IOException {
        CpioArchiveEntry entry = outputStream.createArchiveEntry(null, "testEntry");
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveEntryWithEmptyName() throws IOException {
        CpioArchiveEntry entry = outputStream.createArchiveEntry(new File("testFile.txt"), "");
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveEntryWithInvalidName() throws IOException {
        CpioArchiveEntry entry = outputStream.createArchiveEntry(new File("testFile.txt"), null);
    }
}
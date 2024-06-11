import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link CpioArchiveOutputStream} class.
 */
public class CpioArchiveOutputStreamTest {

    private File testFile;
    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        testFile = new File("temp_test.cpio");
        outputStream = new FileOutputStream(testFile);
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        if (cpioArchiveOutputStream != null) {
            cpioArchiveOutputStream.close();
        }
        IOUtils.closeQuietly(outputStream);
        IOUtils.deleteQuietly(testFile);
    }

    /**
     * Tests writing a regular file entry with specified name and size.
     */
    @Test
    public void testWriteRegularFileEntry() throws IOException {
        String entryName = "test_regular_file.txt";
        byte[] testData = "Hello, World!".getBytes();
        long expectedSize = testData.length;

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setFormat(CpioConstants.FORMAT_NEW);
        entry.setFileSize(expectedSize);
        entry.setMode(CpioConstants.C_ISREG);

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(testData, 0, testData.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        assertEquals(testData.length, cpioArchiveOutputStream.getSize());
        assertEquals(expectedSize, cpioArchiveOutputStream.getNextEntry().getSize());
    }

    /**
     * Tests writing a directory entry with specified name and size.
     */
    @Test
    public void testWriteDirectoryEntry() throws IOException {
        String entryName = "test_directory/";
        long expectedSize = 0;

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setFormat(CpioConstants.FORMAT_NEW);
        entry.setFileSize(expectedSize);
        entry.setMode(CpioConstants.C_ISDIR);

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.closeArchiveEntry();

        assertEquals(expectedSize, cpioArchiveOutputStream.getNextEntry().getSize());
    }

    /**
     * Tests writing an empty entry with specified name and size.
     */
    @Test
    public void testWriteEmptyEntry() throws IOException {
        String entryName = "test_empty_file.txt";
        long expectedSize = 0;

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setFormat(CpioConstants.FORMAT_NEW);
        entry.setFileSize(expectedSize);

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.closeArchiveEntry();

        assertEquals(expectedSize, cpioArchiveOutputStream.getNextEntry().getSize());
    }

    /**
     * Tests writing a malformed entry with invalid size.
     */
    @Test
    public void testWriteMalformedEntryWithInvalidSize() {
        String entryName = "test_malformed_entry.txt";
        long expectedSize = -1;

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setFormat(CpioConstants.FORMAT_NEW);
        entry.setFileSize(expectedSize);

        assertThrows(IOException.class, () -> {
            cpioArchiveOutputStream.putArchiveEntry(entry);
        });
    }

    /**
     * Tests writing a new CPIO file entry with specified name, size, and mode
     * using {@link CpioArchiveOutputStream#putArchiveEntry(ArchiveEntry)}.
     */
    @Test
    public void testPutArchiveEntry() throws IOException {
        String entryName = "test_putArchiveEntry.txt";
        byte[] testData = "test_putArchiveEntry_data".getBytes();
        long expectedSize = testData.length;

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setFormat(CpioConstants.FORMAT_NEW);
        entry.setFileSize(expectedSize);
        entry.setMode(CpioConstants.C_ISREG);

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(testData, 0, testData.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        assertEquals(testData.length, cpioArchiveOutputStream.getSize());
        assertEquals(expectedSize, cpioArchiveOutputStream.getNextEntry().getSize());
    }

    /**
     * Tests writing a new CPIO file entry with specified name, size, and mode
     * using {@link CpioArchiveOutputStream#putArchiveEntry(File, String)}.
     */
    @Test
    public void testPutArchiveEntryWithFile() throws IOException {
        String entryName = "test_putArchiveEntryWithFile.txt";
        String testFileContent = "test_putArchiveEntryWithFile_content";
        File testFile = new File("test_putArchiveEntryWithFile_temp.txt");
        testFile.createNewFile();
        testFile.setEnabled(false);
        testFile.setReadable(false);
        testFile.setWritable(true);
        testFile.setExecutable(false);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setFormat(CpioConstants.FORMAT_NEW);
        entry.setFileSize(testFile.length());
        entry.setMode(CpioConstants.C_ISREG);

        cpioArchiveOutputStream.putArchiveEntry(entry, testFile);
        IOUtils.copy(testFile.length(), testFile.getInputStream(), cpioArchiveOutputStream);
        cpioArchiveOutputStream.closeArchiveEntry();

        testFile.delete();

        assertEquals(testFile.length(), cpioArchiveOutputStream.getSize());
        assertEquals(testFile.length(), cpioArchiveOutputStream.getNextEntry().getSize());
    }

    /**
     * Tests writing a new CPIO file entry with specified name, size, and mode
     * using {@link CpioArchiveOutputStream#createArchiveEntry(File, String)} and
     * {@link CpioArchiveOutputStream#putNextEntry(ArchiveEntry)} methods.
     */
    @Test
    public void testCreateArchiveEntryAndPutNextEntry() throws IOException {
        String entryName = "test_createArchiveEntryAndPutNextEntry.txt";
        String testFileContent = "test_createArchiveEntryAndPutNextEntry_content";
        File testFile = new File("test_createArchiveEntryAndPutNextEntry_temp.txt");
        testFile.createNewFile();
        testFile.setEnabled(false);
        testFile.setReadable(false);
        testFile.setWritable(true);
        testFile.setExecutable(false);

        CpioArchiveEntry entry = cpioArchiveOutputStream.createArchiveEntry(testFile, entryName);

        cpioArchiveOutputStream.putNextEntry(entry);
        IOUtils.copy(testFile.length(), testFile.getInputStream(), cpioArchiveOutputStream);
        cpioArchiveOutputStream.closeArchiveEntry();

        testFile.delete();

        assertEquals(testFile.length(), cpioArchiveOutputStream.getSize());
        assertEquals(testFile.length(), cpioArchiveOutputStream.getNextEntry().getSize());
    }

    /**
     * Tests writing a new CPIO file entry with specified name, size, and mode
     * using {@link CpioArchiveOutputStream#createArchiveEntry(File, String)} and
     * writing data directly to the output stream.
     */
    @Test
    public void testCreateArchiveEntryAndWriteData() throws IOException {
        String entryName = "test_createArchiveEntryAndWriteData.txt";
        String testFileContent = "test_createArchiveEntryAndWriteData_content";
        File testFile = new File("test_createArchiveEntryAndWriteData_temp.txt");
        testFile.createNewFile();
        testFile.setEnabled(false);
        testFile.setReadable(false);
        testFile.setWritable(true);
        testFile.setExecutable(false);

        CpioArchiveEntry entry = cpioArchiveOutputStream.createArchiveEntry(testFile, entryName);

        cpioArchiveOutputStream.putNextEntry(entry);
        byte[] testData = testFileContent.getBytes();
        cpioArchiveOutputStream.write(testData, 0, testData.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        testFile.delete();

        assertEquals(testData.length, cpioArchiveOutputStream.getSize());
        assertEquals(testData.length, cpioArchiveOutputStream.getNextEntry().getSize());
    }
}
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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

public class TarArchiveOutputStreamTest {

    private OutputStream os;
    private File tempFile;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        os = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(os);
        tempFile = File.createTempFile("junit-test-", null);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
        os.close();
        tempFile.delete();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        // Put a file entry
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        testEntry.setSize(42);
        tarArchiveOutputStream.putArchiveEntry(testEntry);

        // Write some data
        byte[] data = "Hello, TarArchiveOutputStream!".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);

        // Close the entry
        tarArchiveOutputStream.closeArchiveEntry();

        // Verify the output
        byte[] expectedOutput = generateExpectedOutput("test.txt", data);
        assertEquals(expectedOutput, os.toByteArray());
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        // Put a file entry with a long file name
        String longFileName = "a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/test.txt";
        TarArchiveEntry testEntry = new TarArchiveEntry(longFileName);
        testEntry.setSize(42);

        if (tarArchiveOutputStream.longFileMode == TarArchiveOutputStream.LONGFILE_TRUNCATE) {
            throw new RuntimeException("Long file names should not be truncated.");
        }

        tarArchiveOutputStream.putArchiveEntry(testEntry);

        // Write some data
        byte[] data = "Long file names are supported!".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);

        // Close the entry
        tarArchiveOutputStream.closeArchiveEntry();

        // Verify the output
        byte[] expectedOutput = generateExpectedOutput(longFileName, data);
        assertEquals(expectedOutput, os.toByteArray());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameAndLongFileTruncateMode() throws IOException {
        // Put a file entry with a long file name and long file truncate mode
        String longFileName = "a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/test.txt";
        TarArchiveEntry testEntry = new TarArchiveEntry(longFileName);
        testEntry.setSize(42);

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarArchiveOutputStream.putArchiveEntry(testEntry);

        // Write some data
        byte[] data = "Long file names should not be truncated!".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameAndLongFileGNUMode() throws IOException {
        // Put a file entry with a long file name and long file GNU mode
        String longFileName = "a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/test.txt";
        TarArchiveEntry testEntry = new TarArchiveEntry(longFileName);
        testEntry.setSize(42);

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(testEntry);

        // Write some data
        byte[] data = "Long file names can be handled with GNU extensions!".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);

        // Close the entry
        tarArchiveOutputStream.closeArchiveEntry();

        // Verify the output
        byte[] expectedOutput = generateExpectedOutputWithLongFileNameGNU(longFileName, data);
        assertEquals(expectedOutput, os.toByteArray());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDirectoryEntry() throws IOException {
        // Put a directory entry
        TarArchiveEntry testEntry = new TarArchiveEntry("test_dir/");
        tarArchiveOutputStream.putArchiveEntry(testEntry);

        // Verify an exception is thrown
        assertThrows(IOException.class, () -> tarArchiveOutputStream.write(new byte[0], 0, 0));
    }

    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryWithNonTarArchiveEntry() throws IOException {
        // Put a non-TarArchiveEntry
        File inputFile = new File("non_existent_file.txt");
        TarArchiveEntry testEntry = new File(inputFile);
        tarArchiveOutputStream.putArchiveEntry(testEntry);
    }

    @Test
    public void testFinish() throws IOException {
        // Write some entries and data
        TarArchiveEntry testEntry1 = new TarArchiveEntry("test1.txt");
        testEntry1.setSize(42);
        tarArchiveOutputStream.putArchiveEntry(testEntry1);
        tarArchiveOutputStream.write(new byte[0], 0, 0);
        tarArchiveOutputStream.closeArchiveEntry();

        TarArchiveEntry testEntry2 = new TarArchiveEntry("test2.txt");
        testEntry2.setSize(42);
        tarArchiveOutputStream.putArchiveEntry(testEntry2);
        tarArchiveOutputStream.write(new byte[0], 0, 0);
        tarArchiveOutputStream.closeArchiveEntry();

        // Finish the archive
        tarArchiveOutputStream.finish();

        // Verify the output
        byte[] expectedOutput = generateExpectedOutput("test1.tar");
        assertEquals(expectedOutput, os.toByteArray());

        // Write the contents of the archive to a file
        File outputFile = new File(tempFile.getAbsolutePath() + ".tar");
        OutputStream outputStream = new FileOutputStream(outputFile);
        IOUtils.copy(new ByteArrayInputStream(os.toByteArray()), outputStream);
        outputStream.close();

        // Verify the contents of the file
        byte[] fileOutput = IOUtils.toByteArray(new FileInputStream(outputFile));
        assertEquals(expectedOutput, fileOutput);
    }

    @Test
    public void testClose() throws IOException {
        // Write some entries and data
        TarArchiveEntry testEntry1 = new TarArchiveEntry("test1.txt");
        testEntry1.setSize(42);
        tarArchiveOutputStream.putArchiveEntry(testEntry1);
        tarArchiveOutputStream.write(new byte[0], 0, 0);
        tarArchiveOutputStream.closeArchiveEntry();

        TarArchiveEntry testEntry2 = new TarArchiveEntry("test2.txt");
        testEntry2.setSize(42);
        tarArchiveOutputStream.putArchiveEntry(testEntry2);
        tarArchiveOutputStream.write(new byte[0], 0, 0);
        tarArchiveOutputStream.closeArchiveEntry();

        // Close the stream
        tarArchiveOutputStream.close();

        // Verify that the stream is closed and no more entries can be added
        assertFalse(tarArchiveOutputStream.isOpen());
        assertThrows(IOException.class, () -> tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test3.txt")));
    }

    private byte[] generateExpectedOutput(String fileName, byte[] data) {
        byte[] header = new TarArchiveEntry(fileName).getHeaderBytes();
        byte[] record = new byte[TarArchiveOutputStream.TarConstants.HEADSIZE + data.length];
        System.arraycopy(header, 0, record, 0, TarArchiveOutputStream.TarConstants.HEADSIZE);
        System.arraycopy(data, 0, record, TarArchiveOutputStream.TarConstants.HEADSIZE, data.length);

        byte[] expectedOutput = new byte[os.size()];
        System.arraycopy(record, 0, expectedOutput, 0, record.length);
        System.arraycopy(os.toByteArray(), 0, expectedOutput, record.length, os.size() - record.length);

        return expectedOutput;
    }

    private byte[] generateExpectedOutputWithLongFileNameGNU(String fileName, byte[] data) {
        byte[] longLinkHeader = new TarArchiveEntry(fileName, TarArchiveEntry.LONGNAME_GNU).getHeaderBytes();
        byte[] record = new byte[TarArchiveEntry.NAME_LENGTH + TarArchiveOutputStream.TarConstants.HEADSIZE + data.length];
        System.arraycopy(longLinkHeader, 0, record, 0, TarArchiveEntry.NAME_LENGTH + TarArchiveOutputStream.TarConstants.HEADSIZE);
        System.arraycopy(data, 0, record, TarArchiveEntry.NAME_LENGTH + TarArchiveOutputStream.TarConstants.HEADSIZE, data.length);

        byte[] expectedOutput = new byte[os.size()];
        System.arraycopy(record, 0, expectedOutput, 0, record.length);
        System.arraycopy(os.toByteArray(), 0, expectedOutput, record.length, os.size() - record.length);

        return expectedOutput;
    }
}
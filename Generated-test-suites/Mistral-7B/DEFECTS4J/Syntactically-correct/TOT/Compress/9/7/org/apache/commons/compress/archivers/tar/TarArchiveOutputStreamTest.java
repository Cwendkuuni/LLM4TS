import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Test class for {@link TarArchiveOutputStream}.
 */
public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.close();
    }

    /**
     * Test writing a single file entry.
     */
    @Test
    public void testWriteFileEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        entry.setSize(5);
        tarOutputStream.putArchiveEntry(entry);
        byte[] data = "Hello World".getBytes();
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();

        Assert.assertArrayEquals(data, tarOutputStream.getBuffer().getData(0, 0, data.length));
        Assert.assertEquals(5, tarOutputStream.getSize());
    }

    /**
     * Test writing a directory entry.
     */
    @Test
    public void testWriteDirectoryEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testDir");
        entry.setSize(0);
        entry.setIsDirectory(true);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();

        Assert.assertEquals(0, tarOutputStream.getSize());
    }

    /**
     * Test writing multiple file entries.
     */
    @Test
    public void testWriteMultipleFileEntries() throws IOException {
        TarArchiveEntry file1 = new TarArchiveEntry("file1.txt");
        file1.setSize(5);
        TarArchiveEntry file2 = new TarArchiveEntry("file2.txt");
        file2.setSize(10);

        tarOutputStream.putArchiveEntry(file1);
        byte[] data1 = "Hello World".getBytes();
        tarOutputStream.write(data1, 0, data1.length);
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.putArchiveEntry(file2);
        byte[] data2 = "Hello TarArchiveOutputStreamTest".getBytes();
        tarOutputStream.write(data2, 0, data2.length);
        tarOutputStream.closeArchiveEntry();

        Assert.assertEquals(15, tarOutputStream.getSize());
    }

    /**
     * Test writing file entries with long names.
     */
    @Test
    public void testWriteLongFileNameEntries() throws IOException {
        String longFileName = "a/very/long/filename/with/many/directories.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        entry.setSize(5);

        tarOutputStream.putArchiveEntry(entry);
        byte[] data = "Long file name test".getBytes();
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();

        Assert.assertEquals(5 + longFileName.getBytes().length + 1, tarOutputStream.getSize());
    }

    /**
     * Test handling of long file names with truncation.
     */
    @Test(expected = RuntimeException.class)
    public void testWriteLongFileNameTruncation() throws IOException {
        String longFileName = "a/very/long/filename/with/many/directories.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        entry.setSize(5);

        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOutputStream.putArchiveEntry(entry);
        byte[] data = "Long file name test".getBytes();
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();
    }

    /**
     * Test handling of long file names with GNU extensions.
     */
    @Test
    public void testWriteLongFileNameGNU() throws IOException {
        String longFileName = "a/very/long/filename/with/many/directories.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName, TarConstants.GNU_LONGLINK);
        entry.setSize(5);

        tarOutputStream.putArchiveEntry(entry);
        byte[] data = "Long file name test".getBytes();
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.write(longFileName.getBytes());
        tarOutputStream.write(0);
        tarOutputStream.closeArchiveEntry();

        Assert.assertEquals(5 + longFileName.length() + 1, tarOutputStream.getSize());
    }
}
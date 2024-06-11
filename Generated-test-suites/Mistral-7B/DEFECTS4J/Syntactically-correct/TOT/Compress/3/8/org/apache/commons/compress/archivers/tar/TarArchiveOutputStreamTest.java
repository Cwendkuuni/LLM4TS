import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Test class for {@link TarArchiveOutputStream}.
 */
public class TarArchiveOutputStreamTest {

    private File tempFile;
    private OutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws Exception {
        tempFile = File.createTempFile("junit-", null);
        outputStream = new FileOutputStream(tempFile);
        tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
    }

    @After
    public void tearDown() throws Exception {
        IOUtils.closeQuietly(tarOutputStream);
        IOUtils.closeQuietly(outputStream);
        IOUtils.deleteQuietly(tempFile);
    }

    @Test
    public void testWriteSingleFile() throws IOException {
        // Write a test file
        File testFile = new File("test.txt");
        byte[] testContent = "Hello, world!".getBytes();
        ArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.write(testContent, 0, testContent.length);
        tarOutputStream.closeArchiveEntry();

        // Read the archive and verify the contents
        TarArchiveOutputStream testTarOutputStream = new TarArchiveOutputStream(new FileOutputStream(tempFile));
        testTarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        TarEntry testEntryFromArchive = testTarOutputStream.getNextEntry();
        Assert.assertEquals("test.txt", testEntryFromArchive.getName());
        byte[] readContent = new byte[testContent.length];
        int length = testTarOutputStream.read(readContent, 0, testContent.length);
        Assert.assertEquals(testContent.length, length);
        Assert.assertArrayEquals(testContent, readContent);
        testTarOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testWriteLongFileName() throws IOException {
        String longFileName = new String(new char[TarConstants.NAMELEN + 1]);
        ArchiveEntry longFileNameEntry = new TarArchiveEntry(longFileName);
        tarOutputStream.putArchiveEntry(longFileNameEntry);
    }

    @Test
    public void testWriteDirectory() throws IOException {
        // Create a test directory
        File testDir = new File("test_dir");
        testDir.mkdirs();

        // Write a test file into the directory
        File testFileInDir = new File(testDir, "test.txt");
        byte[] testContent = "Hello, world!".getBytes();
        ArchiveEntry testEntry = new TarArchiveEntry("test_dir/test.txt");
        testEntry.setSize(testContent.length);
        testEntry.setTimeStamp(System.currentTimeMillis());
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.write(testContent, 0, testContent.length);
        tarOutputStream.closeArchiveEntry();

        // Read the archive and verify the contents
        TarArchiveOutputStream testTarOutputStream = new TarArchiveOutputStream(new FileOutputStream(tempFile));
        testTarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        TarEntry testEntryFromArchive = testTarOutputStream.getNextEntry();
        Assert.assertEquals("test_dir/test.txt", testEntryFromArchive.getName());
        byte[] readContent = new byte[testContent.length];
        int length = testTarOutputStream.read(readContent, 0, testContent.length);
        Assert.assertEquals(testContent.length, length);
        Assert.assertArrayEquals(testContent, readContent);
        testTarOutputStream.close();

        // Verify the directory was written correctly
        TarArchiveInputStream testTarInputStream = new TarArchiveInputStream(new FileInputStream(tempFile));
        TarEntry testDirEntryFromArchive = testTarInputStream.getNextEntry();
        Assert.assertEquals("test_dir/", testDirEntryFromArchive.getName());
        Assert.assertTrue(testDirEntryFromArchive.isDirectory());
        TarEntry testEntryFromDir = testTarInputStream.getNextEntry();
        Assert.assertEquals("test_dir/test.txt", testEntryFromDir.getName());
        testTarInputStream.close();

        // Clean up
        File[] filesInDir = testDir.listFiles();
        for (File file : filesInDir) {
            file.delete();
        }
        testDir.delete();
    }

    @Test
    public void testWriteLongFile() throws IOException {
        String longFileName = new String(new char[TarConstants.NAMELEN + 1]);
        byte[] longFileContent = new byte[TarArchiveOutputStream.DEFAULT_BLKSIZE * 10];
        ArchiveEntry longFileNameEntry = new TarArchiveEntry(longFileName);
        tarOutputStream.putArchiveEntry(longFileNameEntry);
        tarOutputStream.write(longFileContent, 0, longFileContent.length);
        tarOutputStream.closeArchiveEntry();

        // Read the archive and verify the contents
        TarArchiveOutputStream testTarOutputStream = new TarArchiveOutputStream(new FileOutputStream(tempFile));
        testTarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        TarEntry testEntryFromArchive = testTarOutputStream.getNextEntry();
        Assert.assertEquals(longFileName, testEntryFromArchive.getName());
        byte[] readContent = new byte[longFileContent.length];
        int length = testTarOutputStream.read(readContent, 0, longFileContent.length);
        Assert.assertEquals(longFileContent.length, length);
        Assert.assertArrayEquals(longFileContent, readContent);
        testTarOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testWriteBeyondFileSize() throws IOException {
        // Set up a test file with a known size
        File testFile = new File("test.txt");
        byte[] testContent = "Hello, world!".getBytes();
        ArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.write(testContent, 0, testContent.length);
        tarOutputStream.closeArchiveEntry();

        // Attempt to write beyond the file size
        byte[] extraContent = "This content should not be written".getBytes();
        tarOutputStream.write(extraContent, 0, extraContent.length);
    }
}
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class TarArchiveOutputStreamTest {

    private File outputFile;
    private TarOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        outputFile = File.createTempFile("test", ".tar");
        tarOutputStream = new TarOutputStream(new FileOutputStream(outputFile));
    }

    @After
    public void tearDown() throws IOException {
        if (tarOutputStream != null) {
            tarOutputStream.finish();
            tarOutputStream.close();
        }
        FileUtils.deleteQuietly(outputFile);
    }

    @Test
    public void testPutEmptyArchive() throws IOException {
        tarOutputStream.finish();
        TarArchiveEntry entry = tarOutputStream.getNextEntry();
        assertEquals(null, entry);
    }

    @Test
    public void testPutSingleEntry() throws IOException {
        File testFile = new File("test.txt");
        byte[] testData = "Hello World!".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(testData.length);

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();

        TarArchiveEntry readEntry = tarOutputStream.getNextEntry();
        assertEquals("test.txt", readEntry.getName());
        assertEquals("test.txt", readEntry.getSize(), testData.length);

        byte[] readData = new byte[testData.length];
        tarOutputStream.read(readData, 0, readData.length);
        assertEquals(testData, readData);

        tarOutputStream.getNextEntry();
        assertEquals(null, tarOutputStream.getNextEntry());
    }

    @Test(expected = RuntimeException.class)
    public void testPutLongFileName() throws IOException {
        String longFileName = "a" + TarConstants.NAMELEN + "b";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testPutLongFileNameWithLongFileMode() throws IOException {
        String longFileName = "a" + TarConstants.NAMELEN + "b";
        tarOutputStream.setLongFileMode(TarOutputStream.LONGFILE_GNU);
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOutputStream.putArchiveEntry(entry);

        TarArchiveEntry readEntry = tarOutputStream.getNextEntry();
        assertEquals("a" + TarConstants.NAMELEN + "b", readEntry.getName());

        tarOutputStream.getNextEntry();
        assertEquals(null, tarOutputStream.getNextEntry());
    }

    @Test
    public void testPutDirectory() throws IOException {
        File dir = new File("test_dir");
        if (dir.exists()) {
            dir.delete();
        }
        dir.mkdir();

        File testFile1 = new File(dir, "test1.txt");
        File testFile2 = new File(dir, "test2.txt");

        TarArchiveEntry testEntry1 = new TarArchiveEntry(testFile1.getPath(), TarEntry.IS_DIRECTORY);
        TarArchiveEntry testEntry2 = new TarArchiveEntry(testFile2.getPath());

        tarOutputStream.putArchiveEntry(testEntry1);
        tarOutputStream.finish();
        tarOutputStream.putArchiveEntry(testEntry2);

        TarArchiveEntry entry1 = tarOutputStream.getNextEntry();
        TarArchiveEntry entry2 = tarOutputStream.getNextEntry();

        assertEquals("test_dir/", entry1.getName());
        assertEquals(TarEntry.IS_DIRECTORY, entry1.isDirectory());
        assertEquals(null, entry1.getSize());

        assertEquals("test1.txt", entry2.getName());
        assertEquals(0, entry2.getSize());

        tarOutputStream.finish();

        TarArchiveEntry rootEntry = tarOutputStream.getNextEntry();
        assertEquals(".", rootEntry.getName());
        assertEquals(TarEntry.IS_DIRECTORY, rootEntry.isDirectory());
        assertEquals(0, rootEntry.getSize());

        TarArchiveEntry testDirEntry = tarOutputStream.getNextEntry();
        assertEquals("test_dir/", testDirEntry.getName());
        assertEquals(TarEntry.IS_DIRECTORY, testDirEntry.isDirectory());
        assertEquals(0, testDirEntry.getSize());

        TarArchiveEntry testEntry1_1 = tarOutputStream.getNextEntry();
        assertEquals("test1.txt", testEntry1_1.getName());
        assertEquals(0, testEntry1_1.getSize());

        TarArchiveEntry testEntry2_1 = tarOutputStream.getNextEntry();
        assertEquals("test2.txt", testEntry2_1.getName());
        assertEquals(0, testEntry2_1.getSize());

        tarOutputStream.getNextEntry();
        assertEquals(null, tarOutputStream.getNextEntry());

        tarOutputStream.close();

        FileUtils.deleteQuietly(outputFile);
    }

    @Test(expected = IOException.class)
    public void testPutInvalidArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test");
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testWriteBeyondSize() throws IOException {
        byte[] testData = "Hello World!".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(testData.length);

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.write(testData, 0, testData.length + 1);

        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWritePartialData() throws IOException {
        byte[] testData = "Hello World!".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(testData.length);

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(testData, 0, testData.length - 1);
        tarOutputStream.closeArchiveEntry();

        TarArchiveEntry readEntry = tarOutputStream.getNextEntry();
        assertEquals("test.txt", readEntry.getName());
        assertEquals("test.txt", readEntry.getSize(), testData.length - 1);

        byte[] readData = new byte[testData.length - 1];
        tarOutputStream.read(readData, 0, readData.length);
        assertEquals(Arrays.substring(testData, 0, testData.length - 1), readData);

        tarOutputStream.getNextEntry();
        assertEquals(null, tarOutputStream.getNextEntry());
    }
}
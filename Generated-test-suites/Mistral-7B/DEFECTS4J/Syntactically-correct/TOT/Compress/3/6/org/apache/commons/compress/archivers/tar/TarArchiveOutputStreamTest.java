import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * TarArchiveOutputStreamTest
 */
public class TarArchiveOutputStreamTest {

    private File testFile;
    private TarOutputStream tarStream;

    @Before
    public void setUp() throws IOException {
        testFile = new File("test.tar");
        tarStream = new TarArchiveOutputStream(new FileOutputStream(testFile));
    }

    @After
    public void tearDown() throws IOException {
        tarStream.finish();
        tarStream.close();
        Files.deleteIfExists(Paths.get(testFile.getAbsolutePath()));
    }

    /**
     * Test putArchiveEntry method with a simple file entry
     */
    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] content = "Hello, World!".getBytes();

        tarStream.putNextEntry(entry);
        tarStream.write(content, 0, content.length);
        tarStream.closeArchiveEntry();

        File testFileContent = new File(testFile.getAbsolutePath() + "/test.txt");
        byte[] testFileContentBytes = Files.readAllBytes(Paths.get(testFileContent.getAbsolutePath()));

        assertArrayEquals(content, testFileContentBytes);
    }

    /**
     * Test putArchiveEntry method with a directory entry
     */
    @Test
    public void testPutArchiveEntryDirectory() throws IOException {
        File testDir = new File("testDir");
        testDir.mkdir();
        TarArchiveEntry entry = new TarArchiveEntry("testDir/");

        tarStream.putNextEntry(entry);
        tarStream.closeArchiveEntry();

        Files.delete(testFile.toPath());
        testFile.mkdir();
        Files.move(testDir.toPath(), testFile.toPath().resolve("testDir").toFile().getAbsoluteFile().toPath());

        TarArchiveEntry testEntry = (TarArchiveEntry) tarStream.getNextEntry();
        assertTrue(testEntry.isDirectory());

        Files.deleteRecursively(testFile.toPath());
    }

    /**
     * Test putArchiveEntry method with long file names
     */
    @Test
    public void testPutArchiveEntryLongFileName() throws IOException {
        String longFileName = "a/very/long/file/name.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        byte[] content = "Long file name test".getBytes();

        tarStream.setLongFileMode(TarOutputStream.LONGFILE_GNU);
        tarStream.putNextEntry(entry);
        tarStream.write(content, 0, content.length);
        tarStream.closeArchiveEntry();

        File testFileContent = new File(testFile.getAbsolutePath() + "/a/very/long/file/name.txt");
        byte[] testFileContentBytes = Files.readAllBytes(Paths.get(testFileContent.getAbsolutePath()));

        assertArrayEquals(content, testFileContentBytes);
    }

    /**
     * Test write method with a simple file entry
     */
    @Test
    public void testWriteSimpleFileEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] content = "Hello, World!".getBytes();

        tarStream.putNextEntry(entry);

        tarStream.write(content, 0, content.length);
        tarStream.closeArchiveEntry();

        File testFileContent = new File(testFile.getAbsolutePath() + "/test.txt");
        byte[] testFileContentBytes = Files.readAllBytes(Paths.get(testFileContent.getAbsolutePath()));

        assertArrayEquals(content, testFileContentBytes);
    }

    /**
     * Test write method with a directory entry
     */
    @Test
    public void testWriteDirectoryEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testDir/");

        tarStream.putNextEntry(entry);
        tarStream.closeArchiveEntry();

        Files.delete(testFile.toPath());
        testFile.mkdir();

        TarArchiveEntry testEntry = (TarArchiveEntry) tarStream.getNextEntry();
        assertTrue(testEntry.isDirectory());

        Files.deleteRecursively(testFile.toPath());
    }

    /**
     * Test write method with long file names
     */
    @Test
    public void testWriteLongFileName() throws IOException {
        String longFileName = "a/very/long/file/name.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        byte[] content = "Long file name test".getBytes();

        tarStream.setLongFileMode(TarOutputStream.LONGFILE_GNU);

        tarStream.putNextEntry(entry);

        tarStream.write(content, 0, content.length);
        tarStream.closeArchiveEntry();

        File testFileContent = new File(testFile.getAbsolutePath() + "/a/very/long/file/name.txt");
        byte[] testFileContentBytes = Files.readAllBytes(Paths.get(testFileContent.getAbsolutePath()));

        assertArrayEquals(content, testFileContentBytes);
    }
}
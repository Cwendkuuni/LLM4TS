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
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Test TarArchiveOutputStream class.
 */
public class TarArchiveOutputStreamTest {

    private Path tmpFile;
    private OutputStream output;
    private TarOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        tmpFile = Files.createTempFile("junit-", ".tar");
        output = new FileOutputStream(tmpFile.toFile());
        tarOutputStream = new TarArchiveOutputStream(output);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.close();
        output.close();
        Files.deleteIfExists(tmpFile);
    }

    /**
     * Test putArchiveEntry method with a regular file.
     */
    @Test
    public void testPutArchiveEntryWithRegularFile() throws IOException {
        // Create test file
        File testFile = new File("test.txt");
        Files.write(testFile.toPath(), "Hello, World!".getBytes());

        // Put archive entry
        TarArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(testFile, "test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);

        // Write test file content
        byte[] testFileContent = Files.readAllBytes(testFile.toPath());
        tarOutputStream.write(testFileContent, 0, testFileContent.length);

        // Close archive entry
        tarOutputStream.closeArchiveEntry();

        // Finish and close archive
        tarOutputStream.finish();
        tarOutputStream.close();

        // Test archive content
        File archiveFile = tmpFile.toFile();
        TarArchiveEntry archiveEntryRead = new TarArchiveEntry(archiveFile, "test.txt");
        Files.deleteIfExists(testFile.toPath());

        try (InputStream inputStream = new FileInputStream(archiveFile)) {
            byte[] readBytes = new byte[testFileContent.length];
            int bytesRead = inputStream.read(readBytes, 0, readBytes.length);
            assertEquals("Wrong test file content in archive", Arrays.toString(testFileContent),
                         Arrays.toString(readBytes));
            assertNotNull("Missing test file entry in archive", archiveEntryRead);
            assertEquals("Wrong file name in archive entry", "test.txt", archiveEntryRead.getName());
            assertEquals("Wrong file size in archive entry", testFile.length(), archiveEntryRead.getSize());
        }
    }

    /**
     * Test putArchiveEntry method with a directory.
     */
    @Test
    public void testPutArchiveEntryWithDirectory() throws IOException {
        // Create test directory and test file
        File testDir = new File("test");
        testDir.mkdir();
        File testFile = new File(testDir, "test.txt");
        Files.write(testFile.toPath(), "Hello, World!".getBytes());

        // Put archive entry for the directory
        TarArchiveEntry archiveEntryDir = tarOutputStream.createArchiveEntry(testDir, ".");
        tarOutputStream.putArchiveEntry(archiveEntryDir);

        // Write directory content
        tarOutputStream.writeDirectory(testDir);

        // Close archive entry and directory
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.closeDirectory();

        // Finish and close archive
        tarOutputStream.finish();
        tarOutputStream.close();

        // Test archive content
        File archiveFile = tmpFile.toFile();
        TarArchiveEntry archiveEntryDirRead = new TarArchiveEntry(archiveFile, "test/");
        assertNotNull("Missing test directory entry in archive", archiveEntryDirRead);
        assertEquals("Wrong directory name in archive entry", "test/", archiveEntryDirRead.getName());

        try (InputStream inputStream = new FileInputStream(archiveFile)) {
            TarArchiveEntry testFileEntry = null;
            byte[] readBytes = new byte[512];
            int bytesRead;

            // Read test file entry in archive
            while ((bytesRead = inputStream.read(readBytes, 0, readBytes.length)) != -1) {
                TarArchiveEntry entry = new TarArchiveEntry(null, readBytes);
                if (entry.isDirectory()) {
                    continue;
                }
                testFileEntry = entry;
                break;
            }
            assertNotNull("Missing test file entry in archive", testFileEntry);
            assertEquals("Wrong file name in archive entry", "test.txt", testFileEntry.getName());
            assertEquals("Wrong file size in archive entry", testFile.length(), testFileEntry.getSize());
        }

        // Delete test directory and file
        Files.deleteRecursively(testDir.toPath());
        Files.deleteIfExists(testFile.toPath());
    }

    /**
     * Test putArchiveEntry method with a long file name.
     */
    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        final int longFileNameLength = 256;
        String longFileName = "longFileName_";
        for (int i = 0; i < longFileNameLength; i++) {
            longFileName += "a";
        }

        // Create test file
        File testFile = new File("test.txt");
        Files.write(testFile.toPath(), "Hello, World!".getBytes());

        // Set long file mode to LONGFILE_GNU
        tarOutputStream.setLongFileMode(TarOutputStream.LONGFILE_GNU);

        // Put archive entry
        TarArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(testFile, longFileName);
        tarOutputStream.putArchiveEntry(archiveEntry);

        // Write test file content
        byte[] testFileContent = Files.readAllBytes(testFile.toPath());
        tarOutputStream.write(testFileContent, 0, testFileContent.length);

        // Close archive entry
        tarOutputStream.closeArchiveEntry();

        // Finish and close archive
        tarOutputStream.finish();
        tarOutputStream.close();

        // Test archive content
        File archiveFile = tmpFile.toFile();
        TarArchiveEntry archiveEntryRead = null;
        try (InputStream inputStream = new FileInputStream(archiveFile)) {
            byte[] readBytes = new byte[512];
            int bytesRead;

            // Read archive entry for the long file name
            while ((bytesRead = inputStream.read(readBytes, 0, readBytes.length)) != -1) {
                TarArchiveEntry entry = new TarArchiveEntry(null, readBytes);
                if (entry.getName().equals(longFileName)) {
                    archiveEntryRead = entry;
                    break;
                }
            }
            assertNotNull("Missing long file name entry in archive", archiveEntryRead);
            assertEquals("Wrong file name in archive entry", longFileName, archiveEntryRead.getName());
            assertEquals("Wrong file size in archive entry", testFile.length(), archiveEntryRead.getSize());
        }

        // Delete test file
        Files.deleteIfExists(testFile.toPath());
    }

    /**
     * Test setLongFileMode method with LONGFILE_TRUNCATE.
     */
    @Test
    public void testSetLongFileNameModeWithTruncate() throws IOException {
        final int longFileNameLength = 256;
        String longFileName = "longFileName_";
        for (int i = 0; i < longFileNameLength; i++) {
            longFileName += "a";
        }

        // Create test file
        File testFile = new File("test.txt");
        Files.write(testFile.toPath(), "Hello, World!".getBytes());

        // Set long file mode to LONGFILE_TRUNCATE
        tarOutputStream.setLongFileMode(TarOutputStream.LONGFILE_TRUNCATE);

        // Put archive entry
        TarArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(testFile, longFileName);
        tarOutputStream.putArchiveEntry(archiveEntry);

        try {
            // Write test file content
            byte[] testFileContent = Files.readAllBytes(testFile.toPath());
            tarOutputStream.write(testFileContent, 0, testFileContent.length);
            fail("Expected IllegalArgumentException for LONGFILE_TRUNCATE mode with long file names");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Close archive entry
        tarOutputStream.closeArchiveEntry();

        // Finish and close archive
        tarOutputStream.finish();
        tarOutputStream.close();

        // Test archive content
        File archiveFile = tmpFile.toFile();
        TarArchiveEntry archiveEntryRead = null;
        try (InputStream inputStream = new FileInputStream(archiveFile)) {
            byte[] readBytes = new byte[512];
            int bytesRead;

            // Read archive entry for the long file name
            while ((bytesRead = inputStream.read(readBytes, 0, readBytes.length)) != -1) {
                TarArchiveEntry entry = new TarArchiveEntry(null, readBytes);
                if (entry.getName().equals(longFileName)) {
                    archiveEntryRead = entry;
                    break;
                }
            }
            assertNull("Found long file name entry in archive", archiveEntryRead);
        }

        // Delete test file
        Files.deleteIfExists(testFile.toPath());
    }
}
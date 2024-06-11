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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TarArchiveOutputStreamTest {

    private File outputFile;
    private TarOutputStream tarOutputStream;

    @Before
    public void setUp() throws Exception {
        outputFile = Files.createTempFile("test", "tar").toFile();
        tarOutputStream = new TarOutputStream(new FileOutputStream(outputFile)) {
            @Override
            public void write(byte[] buffer, int offset, int length) throws IOException {
                super.write(buffer, offset, length);
                System.out.write(buffer, offset, length);
            }
        };
        tarOutputStream.setLongFileMode(TarOutputStream.LONGFILE_GNU);
    }

    @After
    public void tearDown() throws Exception {
        if (tarOutputStream != null) {
            tarOutputStream.finish();
            tarOutputStream.close();
        }
        if (outputFile != null) {
            outputFile.delete();
        }
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        // Test putArchiveEntry with a small file
        File testFile = new File("test.txt");
        File testDirectory = new File("test_directory");
        testDirectory.mkdirs();
        TarArchiveEntry entry = new TarArchiveEntry("test_directory/test_file.txt", "Test file content.");
        tarOutputStream.putNextEntry(entry);
        Files.write(Paths.get(testFile.getPath()), "Test file content.".getBytes());
        tarOutputStream.write(Files.readAllBytes(Paths.get(testFile.getPath())), 0, testFile.length());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        tarOutputStream.close();
        File outputFile = new File(outputFile.getParent(), outputFile.getName());
        TarArchiveEntry outputEntry = new TarArchiveEntry(outputFile, "test_directory/test_file.txt");
        TarArchiveEntry expectedEntry = new TarArchiveEntry(testFile, "test_file.txt");
        assertEquals(expectedEntry.getName(), outputEntry.getName());
        assertEquals(expectedEntry.getSize(), outputEntry.getSize());
        assertEquals(expectedEntry.getCrc(), outputEntry.getCrc());

        // Test putArchiveEntry with a directory
        testDirectory.mkdir();
        File testDirectoryEntry = new TarArchiveEntry("test_directory/", "");
        tarOutputStream.putNextEntry(testDirectoryEntry);
        tarOutputStream.finish();
        tarOutputStream.close();
        TarArchiveEntry outputDirectoryEntry = new TarArchiveEntry(outputFile, "test_directory/");
        TarArchiveEntry expectedDirectoryEntry = new TarArchiveEntry(testDirectory, "");
        assertEquals(expectedDirectoryEntry.getName(), outputDirectoryEntry.getName());
        assertEquals(TarArchiveEntry.STANDARD_FILE_TYPE, outputDirectoryEntry.getFileType());

        // Test putArchiveEntry with a long file name
        String longFileName = "test_directory/test_file_______long_name.txt";
        File longFileNameFile = new File(longFileName);
        File longFileNameDirectory = new File(longFileName.substring(0, longFileName.lastIndexOf('/')));
        longFileNameDirectory.mkdirs();
        entry = new TarArchiveEntry(longFileName, "Test file content.");
        tarOutputStream.putNextEntry(entry);
        Files.write(Paths.get(longFileNameFile.getPath()), "Test file content.".getBytes());
        tarOutputStream.write(Files.readAllBytes(Paths.get(longFileNameFile.getPath())), 0, longFileNameFile.length());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        tarOutputStream.close();
        outputFile = new File(outputFile.getParent(), outputFile.getName());
        outputEntry = new TarArchiveEntry(outputFile, longFileName);
        assertEquals(expectedEntry.getName(), outputEntry.getName());
        assertEquals(expectedEntry.getSize(), outputEntry.getSize());
        assertEquals(expectedEntry.getCrc(), outputEntry.getCrc());

        // Test putArchiveEntry with a long file name and invalid longFileMode
        tarOutputStream = new TarOutputStream(new FileOutputStream(outputFile)) {
            @Override
            public void write(byte[] buffer, int offset, int length) throws IOException {
                super.write(buffer, offset, length);
                System.out.write(buffer, offset, length);
            }
        };
        tarOutputStream.setLongFileMode(TarOutputStream.LONGFILE_ERROR);
        entry = new TarArchiveEntry("test_directory/test_file_______long_name.txt", "Test file content.");
        assertThrows(RuntimeException.class, () -> tarOutputStream.putNextEntry(entry));

        // Test putArchiveEntry with a long file name and truncation
        tarOutputStream = new TarOutputStream(new FileOutputStream(outputFile)) {
            @Override
            public void write(byte[] buffer, int offset, int length) throws IOException {
                super.write(buffer, offset, length);
                System.out.write(buffer, offset, length);
            }
        };
        tarOutputStream.setLongFileMode(TarOutputStream.LONGFILE_TRUNCATE);
        entry = new TarArchiveEntry("test_directory/test_file_______long_name.txt", "Test file content.");
        tarOutputStream.putNextEntry(entry);
        Files.write(Paths.get(longFileNameFile.getPath()), "Test file content.".getBytes());
        tarOutputStream.write(Files.readAllBytes(Paths.get(longFileNameFile.getPath())), 0, longFileNameFile.length());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        tarOutputStream.close();
        outputEntry = new TarArchiveEntry(outputFile, longFileName.substring(0, TarConstants.NAMELEN));
        assertEquals(expectedEntry.getName().substring(0, TarConstants.NAMELEN), outputEntry.getName());
        assertEquals(expectedEntry.getSize(), outputEntry.getSize());
        assertEquals(expectedEntry.getCrc(), outputEntry.getCrc());
    }
}
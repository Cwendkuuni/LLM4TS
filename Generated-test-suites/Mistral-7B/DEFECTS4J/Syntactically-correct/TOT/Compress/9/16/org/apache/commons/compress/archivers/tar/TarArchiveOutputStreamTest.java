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
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * TarArchiveOutputStreamTest class.
 */
public class TarArchiveOutputStreamTest {

    /**
     * The output stream for testing.
     */
    private TarArchiveOutputStream tarOutputStream;

    /**
     * The input stream for testing.
     */
    private ByteArrayOutputStream byteArrayOutputStream;

    /**
     * Sets up the test environment.
     * @throws IOException if an error occurs during setup
     */
    @Before
    public void setUp() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    /**
     * Tears down the test environment.
     * @throws IOException if an error occurs during teardown
     */
    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
        IOUtils.closeQuietly(byteArrayOutputStream.toInputStream());
        byteArrayOutputStream.reset();
    }

    /**
     * Test writing a single file to the archive.
     * @throws IOException if an error occurs during the test
     */
    @Test
    public void testWriteSingleFile() throws IOException {
        // Create an entry
        final String fileName = "testFile.txt";
        final String fileContent = "Hello, World!";
        TarArchiveEntry tarEntry = new TarArchiveEntry(fileName);
        tarEntry.setSize(fileContent.length());
        tarOutputStream.putArchiveEntry(tarEntry);

        // Write the file content
        tarOutputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();

        // Verify the archive content
        byte[] expectedArchiveContent = generateExpectedArchiveContent("singleFile.tar", fileName, fileContent);
        byte[] actualArchiveContent = byteArrayOutputStream.toByteArray();
        assertEquals(expectedArchiveContent, actualArchiveContent);
    }

    /**
     * Test writing multiple files to the archive.
     * @throws IOException if an error occurs during the test
     */
    @Test
    public void testWriteMultipleFiles() throws IOException {
        // Create entries and write files
        final String fileName1 = "file1.txt";
        final String fileContent1 = "File 1 content.";
        TarArchiveEntry tarEntry1 = new TarArchiveEntry(fileName1);
        tarEntry1.setSize(fileContent1.length());
        tarOutputStream.putArchiveEntry(tarEntry1);
        tarOutputStream.write(fileContent1.getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();

        final String fileName2 = "file2.txt";
        final String fileContent2 = "File 2 content.";
        TarArchiveEntry tarEntry2 = new TarArchiveEntry(fileName2);
        tarEntry2.setSize(fileContent2.length());
        tarOutputStream.putArchiveEntry(tarEntry2);
        tarOutputStream.write(fileContent2.getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();

        // Verify the archive content
        byte[] expectedArchiveContent = generateExpectedArchiveContent("multipleFiles.tar", fileName1, fileContent1, fileName2, fileContent2);
        byte[] actualArchiveContent = byteArrayOutputStream.toByteArray();
        assertEquals(expectedArchiveContent, actualArchiveContent);
    }

    /**
     * Test writing an empty file to the archive.
     * @throws IOException if an error occurs during the test
     */
    @Test
    public void testWriteEmptyFile() throws IOException {
        // Create an entry
        final String fileName = "emptyFile.txt";
        TarArchiveEntry tarEntry = new TarArchiveEntry(fileName);
        tarEntry.setSize(0);
        tarOutputStream.putArchiveEntry(tarEntry);
        tarOutputStream.closeArchiveEntry();

        // Verify the archive content
        byte[] expectedArchiveContent = generateExpectedArchiveContent("emptyFile.tar", fileName);
        byte[] actualArchiveContent = byteArrayOutputStream.toByteArray();
        assertEquals(expectedArchiveContent, actualArchiveContent);
    }

    /**
     * Test writing a directory to the archive.
     * @throws IOException if an error occurs during the test
     */
    @Test
    public void testWriteDirectory() throws IOException {
        // Create a directory entry
        final String dirName = "testDir";
        TarArchiveEntry tarEntry = new TarArchiveEntry(dirName, false);
        tarOutputStream.putArchiveEntry(tarEntry);
        tarOutputStream.closeArchiveEntry();

        // Create and write two files to the directory
        final String fileName1 = "file1.txt";
        final String fileContent1 = "File 1 content.";
        TarArchiveEntry tarEntry1 = new TarArchiveEntry(fileName1);
        tarEntry1.setSize(fileContent1.length());
        tarOutputStream.putNextEntry(tarEntry1);
        tarOutputStream.write(fileContent1.getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();

        final String fileName2 = "file2.txt";
        final String fileContent2 = "File 2 content.";
        TarArchiveEntry tarEntry2 = new TarArchiveEntry(fileName2);
        tarEntry2.setSize(fileContent2.length());
        tarOutputStream.putNextEntry(tarEntry2);
        tarOutputStream.write(fileContent2.getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();

        // Close the directory entry
        tarOutputStream.putArchiveEntry(tarEntry);
        tarOutputStream.closeArchiveEntry();

        // Verify the archive content
        byte[] expectedArchiveContent = generateExpectedArchiveContent("directory.tar", dirName, fileName1, fileContent1, fileName2, fileContent2);
        byte[] actualArchiveContent = byteArrayOutputStream.toByteArray();
        assertEquals(expectedArchiveContent, actualArchiveContent);
    }

    /**
     * Test writing an entry with a long file name.
     * @throws IOException if an error occurs during the test
     */
    @Test
    public void testWriteLongFileName() throws IOException {
        // Set long file name mode
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        // Create an entry
        final String longFileName = "testLongFileName/testLongFileName_subdir/testLongFileName_file.txt";
        TarArchiveEntry tarEntry = new TarArchiveEntry(longFileName);
        tarEntry.setSize(0);
        tarOutputStream.putArchiveEntry(tarEntry);

        // Close the entry
        tarOutputStream.closeArchiveEntry();

        // Verify the archive content
        byte[] expectedArchiveContent = generateExpectedArchiveContent("longFileName.tar", longFileName);
        byte[] actualArchiveContent = byteArrayOutputStream.toByteArray();
        assertEquals(expectedArchiveContent, actualArchiveContent);
    }

    /**
     * Test writing an entry with an error.
     * @throws IOException if an error occurs during the test
     */
    @Test(expected = IOException.class)
    public void testWriteError() throws IOException {
        // Create an entry
        final String fileName = "testErrorFile.txt";
        TarArchiveEntry tarEntry = new TarArchiveEntry(fileName);
        tarEntry.setSize(0);
        tarOutputStream.putArchiveEntry(tarEntry);

        // Write an error
        tarOutputStream.write(new byte[] { 0x01 }); // Write an invalid byte
    }

    /**
     * Test finishing the archive.
     * @throws IOException if an error occurs during the test
     */
    @Test
    public void testFinishArchive() throws IOException {
        // Write an empty file
        TarArchiveEntry tarEntry = new TarArchiveEntry("emptyFile.txt");
        tarEntry.setSize(0);
        tarOutputStream.putArchiveEntry(tarEntry);
        tarOutputStream.closeArchiveEntry();

        // Finish the archive
        tarOutputStream.finish();
        tarOutputStream.close();

        // Verify the archive content
        byte[] expectedArchiveContent = generateExpectedArchiveContent("emptyFile.tar", "emptyFile.txt");
        byte[] actualArchiveContent = IOUtils.toByteArray(new File("emptyFile.tar"));
        assertEquals(expectedArchiveContent, actualArchiveContent);
    }

    /**
     * Generates the expected archive content for a given test case.
     * @param archiveName the name of the archive file
     * @param fileName the name of the file in the archive
     * @param fileContent the content of the file
     * @return the expected archive content as a byte array
     */
    private byte[] generateExpectedArchiveContent(String archiveName, String... params) {
        StringBuilder expectedArchiveContent = new StringBuilder();
        expectedArchiveContent.append("00000000 10000000 41525241 10005353 00000001\n"); // Header
        expectedArchiveContent.append("50000000 60000054 00000001 20000000 03000000 name,size,mode,uid,gid,modtime,file_name\n");
        expectedArchiveContent.append(String.format("54%c%c%c%c%c%c%c%c%c%c%c%c 00000000 00000000 %d %d %d %d %d %d %d %d %d %d %d %d\n", (byte) 0x54,
                (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
                (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
                (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0)); // File header

        for (String param : params) {
            int fileNameLength = param.length();
            int nameLength = fileNameLength > TarConstants.NAMELEN ? fileNameLength + 2 : fileNameLength;
            expectedArchiveContent.append(String.format("%011d %08d%n", 55 + fileNameLength, nameLength * 2L)); // Name length and padding
            expectedArchiveContent.append(param + "\00"); // File name
            expectedArchiveContent.append(String.format("%07d %07d%n", 0, 0)); // Uid, gid, and padding
            expectedArchiveContent.append(String.format("%011d %011d%n", 0, 0)); // Mod time and padding
            expectedArchiveContent.append(String.format("%06d %06d%n", 0, 0)); // File size and padding
            expectedArchiveContent.append("000000000000\n"); // Magic number
        }

        expectedArchiveContent.append("50000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 eoa,eof,eof\n"); // EOF record
        expectedArchiveContent.append("50000000 50000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 eof,eof,eof\n"); // EOF record

        return expectedArchiveContent.toString().getBytes(StandardCharsets.UTF_8);
    }
}
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
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class TarArchiveOutputStreamTest {

    private File tempFile;
    private InputStream input;
    private ByteArrayOutputStream output;

    @Before
    public void setUp() throws IOException {
        tempFile = File.createTempFile("junit-test-", ".tar");
        output = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(output);
        IOUtils.deleteQuietly(tempFile);
    }

    @Test
    public void testWriteSingleFile() throws IOException {
        String fileName = "testFile.txt";
        String content = "Hello, World!";

        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(output, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        File fileToWrite = new File("src/test/resources/testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(fileToWrite, fileName);
        tarOutputStream.putArchiveEntry(entry);
        writeBytesToStream(tarOutputStream, content.getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        // Verify the output
        byte[] expectedTarHeader = getExpectedTarHeader(fileName, (int) fileToWrite.length(), "tar");
        byte[] expectedContent = content.getBytes(StandardCharsets.UTF_8);
        byte[] expectedTarArchive = merge(expectedTarHeader, expectedContent);
        assertEquals(expectedTarArchive, output.toByteArray());
    }

    @Test
    public void testWriteLongFileName() throws IOException {
        String longFileName = "a/very/very/long/filename.txt";
        String content = "Long file name test.";

        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(output, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        File fileToWrite = new File("src/test/resources/" + longFileName);
        TarArchiveEntry entry = new TarArchiveEntry(fileToWrite, longFileName);
        tarOutputStream.putArchiveEntry(entry);
        writeBytesToStream(tarOutputStream, content.getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        // Verify the output
        byte[] expectedTarHeader = getExpectedTarHeader(longFileName, (int) fileToWrite.length(), "tar");
        byte[] expectedContent = content.getBytes(StandardCharsets.UTF_8);
        byte[] expectedTarArchive = merge(expectedTarHeader, expectedContent);
        assertEquals(expectedTarArchive, output.toByteArray());
    }

    @Test(expected = RuntimeException.class)
    public void testWriteLongFileNameWithTruncate() throws IOException {
        String longFileName = "a/very/very/long/filename.txt";

        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(output, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        TarArchiveEntry entry = new TarArchiveEntry(new File(longFileName), longFileName);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();
    }

    @Test
    public void testWriteDirectory() throws IOException {
        String dirName = "testDir";
        String fileName1 = "file1.txt";
        String content1 = "Directory test.";
        String fileName2 = "file2.txt";
        String content2 = "Another file.";

        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(output, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        File dirToWrite = new File("src/test/resources/" + dirName);
        dirToWrite.mkdirs();

        File file1ToWrite = new File(dirToWrite, fileName1);
        writeBytesToFile(file1ToWrite, content1.getBytes(StandardCharsets.UTF_8));

        File file2ToWrite = new File(dirToWrite, fileName2);
        writeBytesToFile(file2ToWrite, content2.getBytes(StandardCharsets.UTF_8));

        TarArchiveEntry entry = new TarArchiveEntry(dirToWrite, dirName);
        entry.setDirectory(true);
        tarOutputStream.putArchiveEntry(entry);

        TarArchiveEntry file1Entry = new TarArchiveEntry(file1ToWrite, fileName1);
        tarOutputStream.putArchiveEntry(file1Entry);
        writeBytesToStream(tarOutputStream, content1.getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();

        TarArchiveEntry file2Entry = new TarArchiveEntry(file2ToWrite, fileName2);
        tarOutputStream.putArchiveEntry(file2Entry);
        writeBytesToStream(tarOutputStream, content2.getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        // Verify the output
        byte[] expectedDirectoryHeader = getExpectedDirectoryTarHeader(dirName, (int) dirToWrite.length(), "tar");
        byte[] expectedFile1Header = getExpectedFileTarHeader(fileName1, (int) file1ToWrite.length(), "tar");
        byte[] expectedFile2Header = getExpectedFileTarHeader(fileName2, (int) file2ToWrite.length(), "tar");
        byte[] expectedContent1 = content1.getBytes(StandardCharsets.UTF_8);
        byte[] expectedContent2 = content2.getBytes(StandardCharsets.UTF_8);
        byte[] expectedTarArchive = merge(merge(expectedDirectoryHeader, expectedFile1Header), expectedFile2Header, expectedContent1, expectedContent2);
        assertEquals(expectedTarArchive, output.toByteArray());
    }

    private byte[] merge(byte[]... parts) {
        int totalLength = 0;
        for (byte[] part : parts) {
            totalLength += part.length;
        }
        byte[] result = new byte[totalLength];
        int offset = 0;
        for (byte[] part : parts) {
            System.arraycopy(part, 0, result, offset, part.length);
            offset += part.length;
        }
        return result;
    }

    private byte[] getExpectedTarHeader(String fileName, int fileSize, String fileType) {
        // TODO: Complete the logic to generate a valid TarHeader
        return new byte[0];
    }

    private byte[] getExpectedDirectoryTarHeader(String dirName, int dirSize, String fileType) {
        // TODO: Complete the logic to generate a valid DirTarHeader
        return new byte[0];
    }

    private byte[] getExpectedFileTarHeader(String fileName, int fileSize, String fileType) {
        // TODO: Complete the logic to generate a valid FileTarHeader
        return new byte[0];
    }

    private void writeBytesToFile(File file, byte[] bytes) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(bytes);
        writer.close();
    }

    private void writeBytesToStream(OutputStream outputStream, byte[] bytes) throws IOException {
        outputStream.write(bytes);
    }
}
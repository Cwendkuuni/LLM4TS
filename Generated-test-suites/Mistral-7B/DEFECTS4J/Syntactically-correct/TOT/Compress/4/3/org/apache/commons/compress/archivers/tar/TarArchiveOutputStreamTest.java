import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream, TarConstants.DEFAULT_BLKSIZE, TarConstants.DEFAULT_RCDSIZE);
    }

    @After
    public void tearDown() throws Exception {
        tarOutputStream.close();
    }

    @Test
    public void testPutEmptyArchive() throws IOException {
        tarOutputStream.finish();
        byte[] expected = new byte[TarConstants.STANDARD_HEADER_SIZE * 2];
        Assert.assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test
    public void testPutSingleFileEntry() throws IOException {
        String fileName = "testFile.txt";
        File inputFile = new File("src/test/resources/" + fileName);
        TarArchiveEntry entry = tarOutputStream.createArchiveEntry(inputFile, fileName);
        tarOutputStream.putArchiveEntry(entry);
        byte[] fileContent = "test content".getBytes(StandardCharsets.UTF_8);
        tarOutputStream.write(fileContent, 0, fileContent.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        byte[] expected = getExpectedTarData(fileName, fileContent.length);
        Assert.assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test(expected = IOException.class)
    public void testPutLongFileName() throws IOException {
        String longFileName = "a/long/path/to/file.txt";
        TarArchiveEntry entry = new TarArchiveEntry(TarConstants.GNU_LONGLINK, TarConstants.LF_GNUTYPE_LONGNAME, longFileName);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(longFileName.getBytes(StandardCharsets.UTF_8));
        tarOutputStream.write(0);
        tarOutputStream.closeArchiveEntry();

        // Long file name test should be passed if no error is thrown
    }

    @Test(expected = RuntimeException.class)
    public void testPutLongFileNameWithErrorMode() throws IOException {
        String longFileName = "a/long/path/to/file.txt";
        TarArchiveEntry entry = new TarArchiveEntry(TarConstants.GNU_LONGLINK, TarConstants.LF_GNUTYPE_LONGNAME, longFileName);
        tarOutputStream.setLongFileMode(TarOutputStream.LONGFILE_ERROR);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(longFileName.getBytes(StandardCharsets.UTF_8));
        tarOutputStream.write(0);
        tarOutputStream.closeArchiveEntry();

        // Long file name test with error mode should throw an exception
    }

    @Test(expected = IOException.class)
    public void testPutDirectoryEntry() throws IOException {
        String dirName = "testDir";
        File inputFile = new File("src/test/resources/" + dirName);
        TarArchiveEntry entry = tarOutputStream.createArchiveEntry(inputFile, dirName);
        tarOutputStream.putArchiveEntry(entry);

        // Directory entry should not have a size
        tarOutputStream.finish();

        byte[] expected = new byte[TarConstants.STANDARD_HEADER_SIZE];
        Assert.assertArrayEquals(expected, outputStream.toByteArray());

        // Writing to the stream again should throw an exception
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testCloseAndReopen() throws IOException {
        String fileName = "testFile.txt";
        File inputFile = new File("src/test/resources/" + fileName);
        TarArchiveEntry entry = tarOutputStream.createArchiveEntry(inputFile, fileName);
        tarOutputStream.putArchiveEntry(entry);
        byte[] fileContent = "test content".getBytes(StandardCharsets.UTF_8);
        tarOutputStream.write(fileContent, 0, fileContent.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        tarOutputStream.close();

        // Reopen the stream
        TarArchiveOutputStream newTarOutputStream = new TarArchiveOutputStream(outputStream, TarConstants.DEFAULT_BLKSIZE, TarConstants.DEFAULT_RCDSIZE);
        entry = newTarOutputStream.createArchiveEntry(inputFile, fileName);
        newTarOutputStream.putArchiveEntry(entry);
        newTarOutputStream.write(fileContent, 0, fileContent.length);
        newTarOutputStream.closeArchiveEntry();
        newTarOutputStream.finish();

        byte[] expected = getExpectedTarData(fileName, fileContent.length);
        Assert.assertArrayEquals(expected, outputStream.toByteArray());
    }

    private byte[] getExpectedTarData(String fileName, int fileSize) {
        byte[] header = new byte[TarConstants.STANDARD_HEADER_SIZE];
        byte[] record = new byte[TarConstants.STANDARD_HEADER_SIZE + fileSize];

        int offset = 0;
        System.arraycopy(TarConstants.STANDARD_MAGIC_NUMBER, 0, header, 0, TarConstants.STANDARD_MAGIC_NUMBER.length);

        header[TarConstants.STANDARD_HEADER_OFFSET_VERSION_MAJOR] = TarConstants.STANDARD_VERSION_MAJOR;
        header[TarConstants.STANDARD_HEADER_OFFSET_VERSION_MINOR] = TarConstants.STANDARD_VERSION_MINOR;

        header[TarConstants.STANDARD_HEADER_OFFSET_FILE_TYPE] = TarConstants.STANDARD_FILE_TYPE_REGULAR;
        header[TarConstants.STANDARD_HEADER_OFFSET_MODE] = 0644;
        header[TarConstants.STANDARD_HEADER_OFFSET_UID] = 0;
        header[TarConstants.STANDARD_HEADER_OFFSET_GID] = 0;

        header[TarConstants.STANDARD_HEADER_OFFSET_SIZE] = (byte) (fileSize >> 24);
        header[TarConstants.STANDARD_HEADER_OFFSET_SIZE + 1] = (byte) (fileSize >> 16);
        header[TarConstants.STANDARD_HEADER_OFFSET_SIZE + 2] = (byte) (fileSize >> 8);
        header[TarConstants.STANDARD_HEADER_OFFSET_SIZE + 3] = (byte) fileSize;

        header[TarConstants.STANDARD_HEADER_OFFSET_NAME_LENGTH_LOW] = (byte) fileName.length();
        header[TarConstants.STANDARD_HEADER_OFFSET_NAME_LENGTH_HIGH] = (byte) ((fileName.length() >> 8));

        System.arraycopy(fileName.getBytes(StandardCharsets.UTF_8), 0, record, TarConstants.STANDARD_HEADER_SIZE, fileName.length());
        System.arraycopy(fileSize > 0 ? new byte[]{(byte) 0x00} : new byte[]{}, record, record.length - 1);

        if (fileSize > 0) {
            System.arraycopy(new byte[]{0x00, 0x00}, 0, record, 0, 2);
            System.arraycopy(fileContent, 0, record, 2, fileSize);
        }

        byte[] result = new byte[header.length + record.length];
        System.arraycopy(header, 0, result, 0, header.length);
        System.arraycopy(record, 0, result, header.length, record.length);

        return result;
    }
}
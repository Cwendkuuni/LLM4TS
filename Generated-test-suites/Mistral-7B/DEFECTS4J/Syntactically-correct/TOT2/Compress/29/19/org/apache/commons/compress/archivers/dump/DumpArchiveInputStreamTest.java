import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.compressors.Gzip60CompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class DumpArchiveInputStreamTest {

    private static final String TEST_ARCHIVE = "testData.dump";
    private static final String TEST_ARCHIVE_GZ = "testData.dump.gz";
    private static final int TEST_DATA_SIZE = 1024;

    private DumpArchiveInputStream inputStream;
    private DumpArchiveInputStream gzippedInputStream;
    private DumpArchiveSummary summary;

    @Before
    public void setUp() throws Exception {
        InputStream testData = new ByteArrayInputStream(getTestDataBytes());
        inputStream = new DumpArchiveInputStream(testData);

        InputStream gzippedTestData = new ByteArrayInputStream(getGzippedTestDataBytes());
        gzippedInputStream = new DumpArchiveInputStream(
                new Gzip60CompressorInputStream(gzippedTestData));

        summary = inputStream.getSummary();
    }

    @Test
    public void testGetCount() throws Exception {
        Assert.assertEquals(inputStream.getBytesRead(), summary.getTotalBytes());
    }

    @Test
    public void testGetBytesRead() throws Exception {
        Assert.assertEquals(inputStream.getBytesRead(), summary.getTotalBytes());
    }

    @Test
    public void testGetSummary() throws Exception {
        Assert.assertEquals("testData.dump", summary.getName());
        Assert.assertEquals("dumps", summary.getVolumeName());
        Assert.assertEquals(2, summary.getEntryCount());
        Assert.assertEquals(12345, summary.getMagic());
    }

    @Test
    public void testGetNextEntry() throws Exception {
        DumpArchiveEntry entry1 = inputStream.getNextEntry();
        DumpArchiveEntry entry2 = inputStream.getNextEntry();

        Assert.assertNotNull(entry1);
        Assert.assertNotNull(entry2);
        Assert.assertEquals("file1.txt", entry1.getName());
        Assert.assertEquals("dir1/file2.txt", entry2.getName());

        Assert.assertFalse(entry1.isDirectory());
        Assert.assertTrue(entry2.isDirectory());
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryAfterEndOfStream() throws Exception {
        inputStream.getNextEntry();
        inputStream.getNextEntry();
        inputStream.getNextEntry();
        inputStream.close();
        inputStream.getNextEntry();
    }

    @Test
    public void testReadFile() throws Exception {
        byte[] buffer = new byte[TEST_DATA_SIZE];

        int bytesRead = inputStream.read(buffer);
        Assert.assertEquals(TEST_DATA_SIZE, bytesRead);

        for (int i = 0; i < TEST_DATA_SIZE; i++) {
            Assert.assertEquals((byte) i, buffer[i]);
        }

        Assert.assertEquals(-1, inputStream.read(buffer));
    }

    @Test
    public void testReadFileGzipped() throws Exception {
        byte[] buffer = new byte[TEST_DATA_SIZE];

        int bytesRead = gzippedInputStream.read(buffer);
        Assert.assertEquals(TEST_DATA_SIZE, bytesRead);

        for (int i = 0; i < TEST_DATA_SIZE; i++) {
            Assert.assertEquals((byte) i, buffer[i]);
        }

        Assert.assertEquals(-1, gzippedInputStream.read(buffer));
    }

    @Test(expected = EOFException.class)
    public void testReadPastEndOfStream() throws Exception {
        byte[] buffer = new byte[TEST_DATA_SIZE * 2];
        int bytesRead = inputStream.read(buffer);
        Assert.assertEquals(-1, bytesRead);
    }

    @Test(expected = EOFException.class)
    public void testReadPastEndOfStreamGzipped() throws Exception {
        byte[] buffer = new byte[TEST_DATA_SIZE * 2];
        int bytesRead = gzippedInputStream.read(buffer);
        Assert.assertEquals(-1, bytesRead);
    }

    private byte[] getTestDataBytes() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(DumpArchiveConstants.MAGIC_NUMBER);
        outputStream.write(DumpArchiveConstants.VERSION);
        outputStream.write(DumpArchiveConstants.NFS_MAGIC);

        writeDirectoryEntry(outputStream, 1, ".", ".");
        writeDirectoryEntry(outputStream, 2, "dir1", null);

        writeFileEntry(outputStream, 3, "file1.txt", 10,
                TEST_DATA_SIZE, new byte[TEST_DATA_SIZE], 0, 0);

        writeFileEntry(outputStream, 4, "file2.txt", 10,
                TEST_DATA_SIZE, new byte[TEST_DATA_SIZE], 0, 1);

        outputStream.close();
        return outputStream.toByteArray();
    }

    private byte[] getGzippedTestDataBytes() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // write gzip header
        outputStream.write(0xF9);
        outputStream.write(0x37);
        outputStream.write(0xDB);
        outputStream.write(0x1E);
        outputStream.write(0x94);
        outputStream.write(0x12);
        outputStream.write(0x28);
        outputStream.write(0x00);
        outputStream.write(0x02);
        outputStream.write(0x00);
        outputStream.write(0x00);

        // write uncompressed data
        outputStream.write(getTestDataBytes());

        outputStream.close();
        return outputStream.toByteArray();
    }

    private void writeDirectoryEntry(ByteArrayOutputStream outputStream, int ino,
                                    String name, String parentName) throws IOException {
        if (parentName != null) {
            writeDirectoryEntry(outputStream, parentName.substring(0,
                    parentName.lastIndexOf('/')).hashCode(), parentName.substring(0,
                    parentName.lastIndexOf('/')), null);
        }

        byte[] nameBytes = DumpArchiveUtil.encode(ZipEncoding.ASCII_8BIT, name).getBytes();
        outputStream.write(DumpArchiveConstants.SEGMENT_TYPE_INODE);
        outputStream.write(DumpArchiveUtil.convert32(nameBytes, 0));
        outputStream.write(DumpArchiveUtil.convert16(nameBytes, 0));
        outputStream.write(DumpArchiveConstants.TYPE_DIRECTORY);
        outputStream.write(nameBytes, 0, nameBytes.length);
        outputStream.write((byte) 0);
    }

    private void writeFileEntry(ByteArrayOutputStream outputStream, int ino,
                               String name, int fileType, long fileSize, byte[] content,
                               long offset, int parentIno) throws IOException {
        byte[] nameBytes = DumpArchiveUtil.encode(ZipEncoding.ASCII_8BIT, name).getBytes();
        byte[] contentBytes = content;

        outputStream.write(DumpArchiveConstants.SEGMENT_TYPE_INODE);
        outputStream.write(DumpArchiveUtil.convert32(nameBytes, 0));
        outputStream.write(DumpArchiveUtil.convert16(nameBytes, 0));
        outputStream.write(fileType);
        outputStream.write(nameBytes, 0, nameBytes.length);
        outputStream.write(contentBytes, 0, contentBytes.length);
        outputStream.write(DumpArchiveConstants.TYPE_FILE);
        outputStream.write(Arrays.copyOfRange(nameBytes, 0, 6));
        outputStream.write(contentBytes, 0, 8);
        outputStream.write(DumpArchiveConstants.SEGMENT_TYPE_ADDRESS);
        outputStream.write(DumpArchiveUtil.convert32(Long.toString(offset).getBytes(), 0));
        outputStream.write(DumpArchiveUtil.convert32(Long.toString(fileSize).getBytes(), 0));
        outputStream.write(DumpArchiveConstants.TP_SIZE);
        outputStream.write(DumpArchiveConstants.TP_SIZE * (512 - 1) + 8);
        outputStream.write(contentBytes, 0, contentBytes.length);

        if (parentIno != 0) {
            outputStream.write(DumpArchiveConstants.SEGMENT_TYPE_INODE);
            outputStream.write(DumpArchiveUtil.convert32(Integer.toString(parentIno).getBytes(), 0));
            outputStream.write(DumpArchiveUtil.convert16(Integer.toString(ino).getBytes(), 0));
            outputStream.write(DumpArchiveConstants.TYPE_ADDRESS);
            outputStream.write(DumpArchiveUtil.convert32(Long.toString(offset + contentBytes.length).getBytes(), 0));
            outputStream.write(DumpArchiveUtil.convert32(Long.toString(offset + fileSize).getBytes(), 0));
            outputStream.write(DumpArchiveConstants.TP_SIZE);
            outputStream.write(DumpArchiveConstants.TP_SIZE * 512);
        }
    }
}
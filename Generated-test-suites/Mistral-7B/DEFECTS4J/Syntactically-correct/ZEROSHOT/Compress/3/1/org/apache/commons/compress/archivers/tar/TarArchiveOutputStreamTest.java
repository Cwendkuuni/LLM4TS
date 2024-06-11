import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * TarArchiveOutputStreamTest class to test all methods in the TarArchiveOutputStream class.
 */
public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeWithInvalidValue() {
        tarArchiveOutputStream.setLongFileMode(3);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.close();
    }

    @Test
    public void testGetRecordSize() {
        Assert.assertEquals(TarArchiveOutputStream.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithNullArchiveEntry() {
        tarArchiveOutputStream.putArchiveEntry(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithNonTarArchiveEntry() {
        ArchiveEntry nonTarArchiveEntry = new ArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(nonTarArchiveEntry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IllegalStateException.class)
    public void testCloseArchiveEntryWithoutPutArchiveEntry() throws IOException {
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "Test data".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] outputData = outputStream.toByteArray();
        int recordSize = TarArchiveOutputStream.DEFAULT_RCDSIZE;
        int headerSize = entry.getHeaderSize();
        int expectedDataPosition = headerSize + TarConstants.TAR_HEADER_SIZE;

        for (int i = 0; i < data.length; i++) {
            int index = expectedDataPosition + i;
            Assert.assertEquals(data[i], outputData[index]);
        }
    }

    @Test
    public void testWriteWithPartialRecord() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] data = "Test data".getBytes();
        byte[] firstChunk = new byte[TarArchiveOutputStream.DEFAULT_RCDSIZE / 2];
        System.arraycopy(data, 0, firstChunk, 0, firstChunk.length);

        tarArchiveOutputStream.write(firstChunk, 0, firstChunk.length);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.write(data, firstChunk.length, data.length - firstChunk.length);
        tarArchiveOutputStream.close();

        byte[] outputData = outputStream.toByteArray();
        int headerSize = entry.getHeaderSize();
        int recordSize = TarArchiveOutputStream.DEFAULT_RCDSIZE;
        int expectedDataPosition = headerSize + TarConstants.TAR_HEADER_SIZE;

        for (int i = 0; i < firstChunk.length; i++) {
            int index = expectedDataPosition + i;
            Assert.assertEquals(firstChunk[i], outputData[index]);
        }

        for (int i = firstChunk.length; i < data.length; i++) {
            int index = expectedDataPosition + firstChunk.length + i;
            Assert.assertEquals(data[i], outputData[index]);
        }
    }

    @Test
    public void testWriteWithBuffer() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] data = "Test data".getBytes();
        byte[] buffer = new byte[TarArchiveOutputStream.DEFAULT_RCDSIZE];

        int length = tarArchiveOutputStream.write(data, 0, data.length, buffer, 0);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] outputData = outputStream.toByteArray();
        int headerSize = entry.getHeaderSize();
        int recordSize = TarArchiveOutputStream.DEFAULT_RCDSIZE;
        int expectedDataPosition = headerSize + TarConstants.TAR_HEADER_SIZE;

        for (int i = 0; i < length; i++) {
            int index = expectedDataPosition + i;
            Assert.assertEquals(data[i], outputData[index]);
        }
    }

    @Test
    public void testFlush() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] data = "Test data".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.flush();

        byte[] outputData = outputStream.toByteArray();
        int headerSize = entry.getHeaderSize();
        int recordSize = TarArchiveOutputStream.DEFAULT_RCDSIZE;
        int expectedDataPosition = headerSize + TarConstants.TAR_HEADER_SIZE;

        for (int i = 0; i < data.length; i++) {
            int index = expectedDataPosition + i;
            Assert.assertEquals(data[i], outputData[index]);
        }
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File file = new File("test.txt");
        TarArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(file, "test.txt");
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertEquals(file.length(), entry.getSize());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateArchiveEntryWithNonExistentFile() throws IOException {
        File nonExistentFile = new File("nonexistentfile.txt");
        tarArchiveOutputStream.createArchiveEntry(nonExistentFile, "nonexistentfile.txt");
    }
}
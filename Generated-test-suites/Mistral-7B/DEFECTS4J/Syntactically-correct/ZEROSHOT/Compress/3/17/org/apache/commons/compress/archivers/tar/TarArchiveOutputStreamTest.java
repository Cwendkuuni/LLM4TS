import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * JUnit 4 test file for TarArchiveOutputStream class.
 */
public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new File("test.tar"));
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullOutputStream() {
        new TarArchiveOutputStream(null);
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testWriteEOFRecords() throws IOException {
        tarArchiveOutputStream.finish();
        byte[] eofRecord = new byte[TarConstants.STD_BLOCKSIZE];
        tarArchiveOutputStream.write(eofRecord, 0, eofRecord.length);
        Assert.assertEquals(2 * TarConstants.STD_BLOCKSIZE, outputStream.size());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        Assert.assertNull(tarArchiveOutputStream.getNextEntry());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(null);
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test/long/name/test.txt");
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(entry);
        Assert.assertNull(tarArchiveOutputStream.getNextEntry());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameAndErrorMode() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test/long/name/test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        Assert.assertNull(tarArchiveOutputStream.getNextEntry());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameAndTruncateMode() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test/long/name/test.txt");
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarArchiveOutputStream.putArchiveEntry(entry);
        Assert.assertNull(tarArchiveOutputStream.getNextEntry());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "Test data".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();
        Assert.assertNull(tarArchiveOutputStream.getNextEntry());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryBeforePuttingNextEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.write(new byte[1], 0, 1); // should fail
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "Test data".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] outputData = outputStream.toByteArray();
        Assert.assertEquals(data.length, outputData.length);
    }

    @Test
    public void testWriteWithRemainingBytes() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "Test data".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length - 1);
        tarArchiveOutputStream.write(new byte[]{0x0A}, 0, 1); // write LF
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] outputData = outputStream.toByteArray();
        Assert.assertEquals(data.length, outputData.length);
    }
}
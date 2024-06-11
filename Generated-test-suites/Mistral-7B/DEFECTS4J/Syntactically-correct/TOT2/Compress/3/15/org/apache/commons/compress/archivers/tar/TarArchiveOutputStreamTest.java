import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws Exception {
        tarArchiveOutputStream.close();
    }

    /**
     * Test putArchiveEntry method with valid ArchiveEntry.
     *
     * @throws IOException when writing to the output stream fails
     */
    @Test
    public void testPutArchiveEntryValidEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] record = entry.getHeaderRecord();
        tarArchiveOutputStream.write(record, 0, record.length);

        tarArchiveOutputStream.closeArchiveEntry();

        Assert.assertEquals(record.length, outputStream.size());
    }

    /**
     * Test putArchiveEntry method with long file name.
     *
     * @throws IOException when writing to the output stream fails
     */
    @Test
    public void testPutArchiveEntryLongFileName() throws IOException {
        String longFileName = "very/long/file/name.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarArchiveOutputStream.putArchiveEntry(entry);

        if (tarArchiveOutputStream.longFileMode == TarArchiveOutputStream.LONGFILE_ERROR) {
            Assert.assertThrows(RuntimeException.class, () -> tarArchiveOutputStream.putArchiveEntry(entry));
        } else {
            byte[] longLinkRecord = TarConstants.getGnuLongLinkRecord(longFileName.getBytes());
            byte[] record = entry.getHeaderRecord();

            tarArchiveOutputStream.write(longLinkRecord, 0, longLinkRecord.length);
            tarArchiveOutputStream.write(record, 0, record.length);

            tarArchiveOutputStream.closeArchiveEntry();

            Assert.assertEquals(record.length + longLinkRecord.length, outputStream.size());
        }
    }

    /**
     * Test putArchiveEntry method with null ArchiveEntry.
     *
     * @throws IOException when writing to the output stream fails
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryNullEntry() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(null);
    }

    /**
     * Test closeArchiveEntry method with no entry open.
     *
     * @throws IOException when writing to the output stream fails
     */
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryNoEntryOpen() throws IOException {
        tarArchiveOutputStream.closeArchiveEntry();
    }

    /**
     * Test write method with valid data.
     *
     * @throws IOException when writing to the output stream fails
     */
    @Test
    public void testWriteValidData() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] data = "Hello, TarArchiveOutputStream!".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);

        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expected = new byte[entry.getSize()];
        System.arraycopy(entry.getHeaderRecord(), 0, expected, 0, entry.getHeaderRecord().length);
        System.arraycopy(data, 0, expected, entry.getHeaderRecord().length, data.length);

        Assert.assertArrayEquals(expected, outputStream.toByteArray());
    }

    /**
     * Test write method with data longer than entry size.
     *
     * @throws IOException when writing to the output stream fails
     */
    @Test(expected = IOException.class)
    public void testWriteDataLongerThanEntrySize() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] data = "Hello, TarArchiveOutputStream!".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length + 1);
    }

    /**
     * Test finish method.
     *
     * @throws IOException when writing to the output stream fails
     */
    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] record = entry.getHeaderRecord();
        tarArchiveOutputStream.write(record, 0, record.length);

        tarArchiveOutputStream.finish();

        Assert.assertEquals(record.length * 2, outputStream.size());
    }
}
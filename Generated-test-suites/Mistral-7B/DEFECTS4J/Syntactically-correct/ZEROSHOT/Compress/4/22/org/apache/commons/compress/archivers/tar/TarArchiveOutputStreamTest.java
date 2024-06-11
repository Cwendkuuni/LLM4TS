import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * TarArchiveOutputStreamTest class to test all methods in TarArchiveOutputStream class.
 */
public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullOutputStream() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
    }

    @Test
    public void testSetLongFileMode() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        tarArchiveOutputStream.finish();
        byte[] expectedEOFRecord = new byte[TarConstants.STANDARD_HEADER_SIZE * 2];
        Assert.assertArrayEquals(expectedEOFRecord, byteArrayOutputStream.toByteArray());
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(new byte[512]); // writing some data, but not closing the entry
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(new byte[512]); // writing some data
        tarArchiveOutputStream.close();
        byte[] expectedEOFRecord = new byte[TarConstants.STANDARD_HEADER_SIZE * 2];
        Assert.assertArrayEquals(expectedEOFRecord, byteArrayOutputStream.toByteArray());
    }

    @Test(expected = IOException.class)
    public void testCloseWithUnclosedEntry() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(new byte[512]); // writing some data
        tarArchiveOutputStream.closeArchiveEntry(); // closing the entry, but not the stream
        tarArchiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testWriteWithUnclosedEntry() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(new byte[512]); // writing some data, but not closing the entry
        tarArchiveOutputStream.write(new byte[512]); // trying to write more data
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        Assert.assertEquals(TarConstants.STANDARD_HEADER_SIZE, byteArrayOutputStream.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryNullEntry() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        TarArchiveEntry entry = null;
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntrySameNameTwice() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(new byte[512]); // writing some data
        tarArchiveOutputStream.closeArchiveEntry(); // closing the entry
        Assert.assertEquals(TarConstants.STANDARD_HEADER_SIZE + 512, byteArrayOutputStream.size());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithUnclosedStream() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(new byte[512]); // writing some data
        tarArchiveOutputStream.close(); // closing the stream
        tarArchiveOutputStream.closeArchiveEntry(); // trying to close the entry
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] testData = new byte[512];
        tarArchiveOutputStream.write(testData);
        Assert.assertEquals(TarConstants.STANDARD_HEADER_SIZE + 512, byteArrayOutputStream.size());
    }

    @Test(expected = IOException.class)
    public void testWriteWithUnclosedEntry() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(new byte[512]); // writing some data
        tarArchiveOutputStream.write(new byte[512]); // trying to write more data without closing the entry
    }
}
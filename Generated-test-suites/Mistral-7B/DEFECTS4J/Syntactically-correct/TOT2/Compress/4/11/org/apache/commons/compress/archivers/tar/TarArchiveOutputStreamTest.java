import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);

        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        tarOS.putArchiveEntry(entry);

        // write some data
        byte[] data = "Hello World".getBytes();
        tarOS.write(data, 0, data.length);

        tarOS.closeArchiveEntry();
        tarOS.finish();

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        TarArchiveEntry readEntry = new TarArchiveEntry(is);
        byte[] readData = new byte[data.length];
        int length = is.read(readData, 0, readData.length);
        Assert.assertEquals("Data not equal.", data, readData, 0, length);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);

        TarArchiveEntry entry = new TarArchiveEntry("verylongfilenamethatshouldbe truncated.txt");
        tarOS.putArchiveEntry(entry);

        tarOS.finish();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameGNU() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);

        tarOS.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        TarArchiveEntry entry = new TarArchiveEntry("verylongfilenamethatshouldbehandledproperly.txt");
        tarOS.putArchiveEntry(entry);

        tarOS.finish();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);

        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        tarOS.putArchiveEntry(entry);

        // write some data
        byte[] data = "Hello World".getBytes();
        tarOS.write(data, 0, data.length);

        // no closeArchiveEntry() called
        tarOS.finish();
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);

        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOS.putArchiveEntry(entry);

        // write some data
        byte[] data = "Hello World".getBytes();
        tarOS.write(data, 0, data.length);

        tarOS.closeArchiveEntry();
        tarOS.finish();

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        TarArchiveEntry readEntry = new TarArchiveEntry(is);
        byte[] readData = new byte[data.length];
        int length = is.read(readData, 0, readData.length);
        Assert.assertEquals("Data not equal.", data, readData, 0, length);
    }

    @Test
    public void testWriteWithLongFileName() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);

        tarOS.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        TarArchiveEntry entry = new TarArchiveEntry("verylongfilenamethatshouldbehandledproperly.txt");
        tarOS.putArchiveEntry(entry);

        // write some data
        byte[] data = "Hello World".getBytes();
        tarOS.write(data, 0, data.length);

        tarOS.closeArchiveEntry();
        tarOS.finish();

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        TarArchiveEntry readEntry = new TarArchiveEntry(is);
        byte[] readData = new byte[data.length];
        int length = is.read(readData, 0, readData.length);
        Assert.assertEquals("Data not equal.", data, readData, 0, length);
    }

    @Test(expected = IOException.class)
    public void testWriteWithClosedEntry() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);

        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOS.putArchiveEntry(entry);

        tarOS.closeArchiveEntry();

        // write some data
        byte[] data = "Hello World".getBytes();
        tarOS.write(data, 0, data.length);
    }
}
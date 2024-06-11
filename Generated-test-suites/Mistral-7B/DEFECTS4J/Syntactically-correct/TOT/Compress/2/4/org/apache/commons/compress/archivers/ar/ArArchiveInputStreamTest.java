import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws Exception {
        byte[] header = ArArchiveInputStream.HEADER.getBytes();
        byte[] name = "test.txt".getBytes();
        byte[] length = "12345678901234567890".getBytes();
        byte[] trailer = ArArchiveInputStream.TRAILER.getBytes();

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(header);
        byteStream.write(name);
        byteStream.write(length);
        byteStream.write(trailer);

        InputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());
        ArArchiveInputStream test = new ArArchiveInputStream(inputStream);

        ArArchiveEntry entry = test.getNextArEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertEquals(1234567890123L, entry.getSize());

        entry = test.getNextArEntry();
        Assert.assertNull(entry);
    }

    @Test
    public void testGetNextArEntryWithInvalidHeader() throws Exception {
        byte[] header = new byte[ArArchiveInputStream.HEADER.length];
        byte[] name = "test.txt".getBytes();
        byte[] length = "12345678901234567890".getBytes();
        byte[] trailer = ArArchiveInputStream.TRAILER.getBytes();

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(header);
        byteStream.write(name);
        byteStream.write(length);

        InputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());
        ArArchiveInputStream test = new ArArchiveInputStream(inputStream);

        test.getNextArEntry(); // consume invalid header

        Assert.assertNull(test.getNextArEntry());
    }

    @Test
    public void testGetNextArEntryWithNoEntry() throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream test = new ArArchiveInputStream(inputStream);

        Assert.assertNull(test.getNextArEntry());
    }

    @Test
    public void testGetNextArEntryWithTruncatedEntry() throws Exception {
        byte[] header = ArArchiveInputStream.HEADER.getBytes();
        byte[] name = "test.txt".getBytes();
        byte[] length = "123456789012345".getBytes(); // truncated
        byte[] trailer = ArArchiveInputStream.TRAILER.getBytes();

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(header);
        byteStream.write(name);
        byteStream.write(length);
        byteStream.write(trailer);

        InputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());
        ArArchiveInputStream test = new ArArchiveInputStream(inputStream);

        Assert.assertNull(test.getNextArEntry());
    }
}
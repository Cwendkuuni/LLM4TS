import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        final byte[] header = new byte[]{0x3c, 0x21, 0x72, 0x61, 0x68, 0x63, 0x0a, 0x3e, 0x06, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x3c, 0x61, 0x72, 0x72, 0x6f, 0x63, 0x61, 0x6d, 0x65, 0x00, 0x00, 0x00, 0x0a, 0x43, 0x6f, 0x6d, 0x70, 0x65, 0x72, 0x61, 0x75, 0x74, 0x69, 0x6f, 0x6e, 0x00, 0x00, 0x00, 0x0a, 0x02, 0x00, 0x00, 0x00, 0x0a, 0x01, 0x00, 0x00, 0x00, 0x0a, 0x04, 0x00, 0x00, 0x00, 0x0a, 0x03, 0x00, 0x00, 0x00, 0x0a, 0x05, 0x00, 0x00, 0x00, 0x0a, 0x06, 0x00, 0x00, 0x00, 0x0a, 0x07, 0x00, 0x00, 0x00, 0x3c, 0x61, 0x72, 0x72, 0x6f, 0x63, 0x61, 0x6d, 0x65, 0x00, 0x00, 0x00, 0x0a, 0x44, 0x65, 0x6e, 0x64, 0x69, 0x6d, 0x65, 0x00, 0x00, 0x00, 0x0a, 0x02, 0x00, 0x00, 0x00, 0x0a, 0x01, 0x00, 0x00, 0x00, 0x0a, 0x04, 0x00, 0x00, 0x00, 0x0a, 0x03, 0x00, 0x00, 0x00, 0x0a, 0x05, 0x00, 0x00, 0x00, 0x0a, 0x06, 0x00, 0x00, 0x00, 0x0a, 0x07, 0x00, 0x00, 0x00, 0x3c, 0x61, 0x72, 0x72, 0x6f, 0x63, 0x61, 0x6d, 0x65, 0x00, 0x00, 0x00, 0x0a, 0x44, 0x65, 0x6e, 0x64, 0x69, 0x6d, 0x65, 0x00, 0x00, 0x00, 0x0a, 0x02, 0x00, 0x00, 0x00, 0x0a, 0x01, 0x00, 0x00, 0x00, 0x0a, 0x04, 0x00, 0x00, 0x00, 0x0a, 0x03, 0x00, 0x00, 0x00, 0x0a, 0x05, 0x00, 0x00, 0x00, 0x0a, 0x06, 0x00, 0x00, 0x00, 0x0a, 0x07, 0x00, 0x00, 0x00};

        InputStream inputStream = new ByteArrayInputStream(header);
        ArArchiveInputStream testStream = new ArArchiveInputStream(inputStream);

        ArArchiveEntry entry = testStream.getNextArEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertEquals(4L, entry.getSize());

        entry = testStream.getNextArEntry();
        Assert.assertEquals(null, entry);

        testStream.close();
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[16];
        Assert.assertFalse(ArArchiveInputStream.matches(signature, 16));

        System.arraycopy(ArArchiveInputStream.HEADER, 0, signature, 0, ArArchiveInputStream.HEADER.length);
        Assert.assertTrue(ArArchiveInputStream.matches(signature, 16));
    }

    @Test(expected = IOException.class)
    public void testInvalidHeader() throws IOException {
        byte[] header = new byte[17];
        System.arraycopy(ArArchiveInputStream.HEADER, 0, header, 0, ArArchiveInputStream.HEADER.length - 1);
        ArArchiveInputStream testStream = new ArArchiveInputStream(new ByteArrayInputStream(header));
        testStream.getNextArEntry();
    }

    @Test(expected = IOException.class)
    public void testInvalidTrailer() throws IOException {
        byte[] header = ArArchiveInputStream.HEADER;
        byte[] trailer = ArArchiveInputStream.TRAILER;
        byte[] data = new byte[10];
        byte[] input = new byte[header.length + trailer.length + data.length];
        System.arraycopy(header, 0, input, 0, header.length);
        System.arraycopy(trailer, 0, input, header.length, trailer.length);
        System.arraycopy(data, 0, input, header.length + trailer.length, data.length);

        ArArchiveInputStream testStream = new ArArchiveInputStream(new ByteArrayInputStream(input));
        testStream.getNextArEntry();
    }

    @Test(expected = IOException.class)
    public void testNoEntry() throws IOException {
        byte[] header = ArArchiveInputStream.HEADER;
        byte[] trailer = ArArchiveInputStream.TRAILER;
        byte[] input = new byte[header.length + trailer.length];
        System.arraycopy(header, 0, input, 0, header.length);
        System.arraycopy(trailer, 0, input, header.length, trailer.length);

        ArArchiveInputStream testStream = new ArArchiveInputStream(new ByteArrayInputStream(input));
        testStream.getNextArEntry();
        testStream.getNextArEntry();
    }
}
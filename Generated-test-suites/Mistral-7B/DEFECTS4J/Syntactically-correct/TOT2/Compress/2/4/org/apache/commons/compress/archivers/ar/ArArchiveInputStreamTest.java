import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStreamTest;
import org.apache.commons.compress.utils.ByteStreams;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

public class ArArchiveInputStreamTest {

    private InputStream inputStream;
    private ArArchiveInputStream archiveInputStream;

    @Before
    public void setUp() throws IOException {
        String testData = "TestData\n" +
                "12345678901234\n" +
                "user user\n" +
                "group group\n" +
                "0644\n" +
                "10\n" +
                "Test File\n" +
                "0123456789ABCDEF\n" +
                ArArchiveEntry.HEADER +
                "Test File\n" +
                "10\n" +
                "user user\n" +
                "group group\n" +
                "0644\n" +
                "0123456789ABCDEF\n" +
                ArArchiveEntry.TRAILER;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CRC32 crc = new CRC32();
        crc.update(testData.getBytes(StandardCharsets.UTF_8));
        byteArrayOutputStream.write(Long.toByteArray(crc.getValue()));
        byteArrayOutputStream.write(testData.getBytes(StandardCharsets.UTF_8));

        this.inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        this.archiveInputStream = new ArArchiveInputStream(this.inputStream);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry = this.archiveInputStream.getNextArEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("Test File", entry.getName());
        Assert.assertEquals(10L, entry.getSize());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArArchiveEntry entry = this.archiveInputStream.getNextEntry();
        Assert.assertSame(entry, this.archiveInputStream.getNextArEntry());
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryAfterEOF() throws IOException {
        this.archiveInputStream.getNextArEntry();
        this.archiveInputStream.getNextArEntry();
        this.archiveInputStream.getNextArEntry();
        this.archiveInputStream.getNextEntry(); // should throw an exception
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryWithInvalidHeader() throws IOException {
        ByteArrayInputStream invalidHeaderInputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08});
        ArArchiveInputStream invalidHeaderArchiveInputStream = new ArArchiveInputStream(invalidHeaderInputStream);
        invalidHeaderArchiveInputStream.getNextArEntry(); // should throw an exception
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryWithInvalidTrailer() throws IOException {
        ByteArrayInputStream invalidTrailerInputStream = new ByteArrayInputStream(new byte[]{0x3E, 0x0A, 0x68, 0x63, 0x72, 0x61, 0x68, 0x63});
        ArArchiveInputStream invalidTrailerArchiveInputStream = new ArArchiveInputStream(invalidTrailerInputStream);
        invalidTrailerArchiveInputStream.getNextArEntry(); // should throw an exception
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryWithUnexpectedEndOfStream() throws IOException {
        InputStream unexpectedEndOfStreamInputStream = new ByteArrayInputStream(new byte[]{0x3C, 0x21, 0x72, 0x61, 0x68, 0x63, 0x0A, 0x3E, 0x0A});
        ArArchiveInputStream unexpectedEndOfStreamArchiveInputStream = new ArArchiveInputStream(unexpectedEndOfStreamInputStream);
        unexpectedEndOfStreamArchiveInputStream.getNextArEntry(); // should throw an exception
    }

    @Test
    public void testClose() throws IOException {
        this.archiveInputStream.close();
        Assert.assertTrue(this.inputStream.available() > 0);
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[10];
        int read = this.archiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(10, read);
        for (int i = 0; i < buffer.length; i++) {
            Assert.assertEquals("Test File".charAt(i), buffer[i]);
        }
    }

    @Test
    public void testReadWithOffset() throws IOException {
        byte[] buffer = new byte[5];
        int offset = 5;
        int read = this.archiveInputStream.read(buffer, offset, buffer.length - offset);
        Assert.assertEquals(5, read);
        for (int i = 0; i < buffer.length; i++) {
            if (i >= offset) {
                Assert.assertEquals("Test File".charAt(i - offset), buffer[i]);
            }
        }
    }

    @Test(expected = IOException.class)
    public void testReadWithNegativeOffset() throws IOException {
        byte[] buffer = new byte[5];
        int offset = -5;
        this.archiveInputStream.read(buffer, offset, 5); // should throw an exception
    }
}
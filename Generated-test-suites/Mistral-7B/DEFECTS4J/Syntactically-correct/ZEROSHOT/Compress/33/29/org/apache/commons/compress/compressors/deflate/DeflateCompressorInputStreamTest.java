import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;

/**
 * Test class for DeflateCompressorInputStream.
 */
public class DeflateCompressorInputStreamTest {

    private InputStream inputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        byte[] data = "This is a test".getBytes();
        this.outputStream = new ByteArrayOutputStream();
        this.inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(data), new DeflateParameters());
    }

    @After
    public void tearDown() throws IOException {
        this.inputStream.close();
        this.outputStream.close();
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[1];
        int read;

        // Test reading single byte
        read = this.inputStream.read(buffer, 0, 1);
        assert (read == 1);
        assert (buffer[0] == (byte) 0x58); // First byte of deflate compressed data

        // Test reading multiple bytes
        read = this.inputStream.read(buffer, 0, buffer.length);
        assert (read == buffer.length);
        assertArrayEquals(new byte[]{(byte) 0x58, (byte) 0x31, (byte) 0x13, (byte) 0x50, (byte) 0x31, (byte) 0x16, (byte) 0x50, (byte) 0x33, (byte) 0x18, (byte) 0x50, (byte) 0x34, (byte) 0x19, (byte) 0x50, (byte) 0x35, (byte) 0x1a, (byte) 0x50, (byte) 0x36, (byte) 0x1b, (byte) 0x50, (byte) 0x37, (byte) 0x1c, (byte) 0x2c}, buffer);

        // Test end of stream
        read = this.inputStream.read(buffer, 0, buffer.length);
        assert (read == -1);
    }

    @Test
    public void testReadWithByteArray() throws IOException {
        byte[] buffer = new byte[10];

        int read = this.inputStream.read(buffer, 0, buffer.length);
        assert (read == buffer.length);
        assertArrayEquals(data, buffer);
    }

    @Test
    public void testReadWithOffset() throws IOException {
        byte[] buffer = new byte[10];

        int read = this.inputStream.read(buffer, 5, 5);
        assert (read == 5);
        assertArrayEquals(new byte[]{(byte) 0x50, (byte) 0x31, (byte) 0x16, (byte) 0x50, (byte) 0x33}, buffer, 5);
    }

    @Test
    public void testSkip() throws IOException {
        long skipped = this.inputStream.skip(7L);
        assert (skipped == 7L);

        int read = this.inputStream.read(buffer, 0, buffer.length);
        assert (read == buffer.length - 3);
        assertArrayEquals(new byte[]{(byte) 0x50, (byte) 0x34, (byte) 0x19, (byte) 0x50, (byte) 0x35, (byte) 0x1a, (byte) 0x50, (byte) 0x36, (byte) 0x1b, (byte) 0x50, (byte) 0x37}, buffer);
    }

    @Test
    public void testAvailable() throws IOException {
        int available = this.inputStream.available();
        assert (available == 100);

        byte[] buffer = new byte[10];
        int read = this.inputStream.read(buffer, 0, buffer.length);
        available = this.inputStream.available();
        assert (available == 0);

        // Test reset
        this.inputStream.reset();
        available = this.inputStream.available();
        assert (available == 100);
    }

    @Test
    public void testClose() throws IOException {
        byte[] buffer = new byte[10];
        int read = this.inputStream.read(buffer, 0, buffer.length);
        assert (read == buffer.length);
        assertArrayEquals(data, buffer);

        this.inputStream.close();

        read = this.inputStream.read(buffer, 0, buffer.length);
        assert (read == -1);
    }
}
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.DeflateParameters;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DeflateCompressorInputStreamTest {

    private InputStream inputStream;
    private DeflateCompressorInputStream compressorInputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        byte[] data = "Test data for DeflateCompressorInputStream test".getBytes();
        outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStreamToCompress = new ByteArrayInputStream(data);
        this.inputStream = new DeflateCompressorInputStream(inputStreamToCompress, new DeflateParameters());
        this.compressorInputStream = new DeflateCompressorInputStream(this.inputStream);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        int readByte = compressorInputStream.read();
        assertEquals(-1, readByte);

        byte[] buffer = new byte[1];
        readByte = compressorInputStream.read(buffer);
        assertEquals((byte) 0x41, buffer[0]); // first byte of the test data is 'A'
        assertEquals(1, readByte);
    }

    @Test
    public void testReadMultipleBytes() throws IOException {
        byte[] buffer = new byte[3];
        int readBytes = compressorInputStream.read(buffer);
        assertEquals(3, readBytes);
        String readData = new String(buffer);
        assertEquals("Test", readData);
    }

    @Test
    public void testSkip() throws IOException {
        long bytesSkipped = compressorInputStream.skip(5);
        assertEquals(5, bytesSkipped);
        int readByte = compressorInputStream.read();
        assertNull(readByte);
    }

    @Test
    public void testAvailable() throws IOException {
        int availableBytes = compressorInputStream.available();
        assertEquals(23, availableBytes);

        byte[] buffer = new byte[24];
        int readBytes = compressorInputStream.read(buffer);
        assertEquals(23, readBytes);
        int availableBytesAfterRead = compressorInputStream.available();
        assertEquals(0, availableBytesAfterRead);
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        compressorInputStream.close();
        int readByte = compressorInputStream.read();
    }

    @Test
    public void testClose() throws IOException {
        compressorInputStream.close();
        assertFalse(compressorInputStream.isOpen());

        byte[] buffer = new byte[1];
        int readByte = compressorInputStream.read(buffer);
        assertNull(readByte);
    }

    @Test
    public void testZLibHeader() throws IOException {
        byte[] zlibHeader = {0x78, 0x5E, 0x63, 0x61, 0x78, 0x4D, 0x65, 0x74, 0x68, 0x6F, 0x76};

        inputStream = new ByteArrayInputStream(new byte[][]{zlibHeader, new byte[0], "Test data for DeflateCompressorInputStream test".getBytes()});
        compressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());

        int readByte = compressorInputStream.read();
        assertEquals(-1, readByte); // zlib header byte

        byte[] buffer = new byte[1];
        readByte = compressorInputStream.read(buffer);
        assertEquals((byte) 0x41, buffer[0]); // first byte of the test data is 'A'
        assertEquals(1, readByte);
    }

    @Test
    public void testNoZLibHeader() throws IOException {
        inputStream = new ByteArrayInputStream("Test data for DeflateCompressorInputStream test".getBytes());
        compressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters(false));

        int readByte = compressorInputStream.read();
        assertEquals((byte) 0x41, readByte); // first byte of the test data is 'A'
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(outputStream);
        IOUtils.closeQuietly(compressorInputStream);
        IOUtils.closeQuietly(inputStream);
    }

}
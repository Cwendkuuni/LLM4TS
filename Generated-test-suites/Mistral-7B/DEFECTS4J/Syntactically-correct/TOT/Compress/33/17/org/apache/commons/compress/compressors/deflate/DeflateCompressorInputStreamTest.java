import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for DeflateCompressorInputStream.
 */
public class DeflateCompressorInputStreamTest {

    private InputStream inputStream;
    private DeflateCompressorInputStream decompressorInputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        byte[] sourceData = "This is a test message".getBytes();
        inputStream = new ByteArrayInputStream(sourceData);
        outputStream = new ByteArrayOutputStream();
        DeflateParameters deflateParameters = new DeflateParameters();
        decompressorInputStream = new DeflateCompressorInputStream(inputStream, deflateParameters);
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(decompressorInputStream);
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
    }

    /**
     * Test reading a single byte from the input stream.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testReadSingleByte() throws IOException {
        int readByte = decompressorInputStream.read();
        assertEquals((int) 'T', readByte);
    }

    /**
     * Test reading multiple bytes from the input stream.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testReadMultipleBytes() throws IOException {
        byte[] target = new byte[10];
        int bytesRead = decompressorInputStream.read(target, 0, 10);
        assertEquals(8, bytesRead);
        for (int i = 0; i < bytesRead; i++) {
            assertEquals((byte) 'T' + (byte) (i + 1), target[i]);
        }
    }

    /**
     * Test skip method.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testSkip() throws IOException {
        long skippedBytes = decompressorInputStream.skip(8);
        assertEquals(8, skippedBytes);
    }

    /**
     * Test reading available bytes.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testAvailableBytes() throws IOException {
        int availableBytes = decompressorInputStream.available();
        assertEquals(14, availableBytes);
    }

    /**
     * Test closing the input stream.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testClose() throws IOException {
        int readByte = decompressorInputStream.read();
        decompressorInputStream.close();
        assertNull(decompressorInputStream.in);
        int readByteAfterClose = decompressorInputStream.read();
        assertNull(readByteAfterClose);
    }

    /**
     * Test reading from the input stream with zlib headers.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testReadWithZlibHeader() throws IOException {
        byte[] sourceDataWithZlibHeader = new byte[]{31, 139, 80, 144, 145, 129, 1, 0, 0, 0, 0, 0, 0, 0};
        inputStream = new ByteArrayInputStream(sourceDataWithZlibHeader);
        decompressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        byte[] target = new byte[10];
        int bytesRead = decompressorInputStream.read(target, 0, 10);
        assertEquals(8, bytesRead);
        for (int i = 0; i < bytesRead; i++) {
            assertEquals((byte) 'T' + (byte) (i + 1), target[i]);
        }
    }
}
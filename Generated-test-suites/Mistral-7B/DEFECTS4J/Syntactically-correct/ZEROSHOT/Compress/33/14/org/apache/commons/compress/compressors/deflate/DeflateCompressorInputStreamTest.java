import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Inflater;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link DeflateCompressorInputStream}.
 */
public class DeflateCompressorInputStreamTest {

    private InputStream inputStream;
    private DeflateCompressorInputStream deflateCompressorInputStream;

    @Before
    public void setUp() {
        byte[] compressedData = new byte[]{(byte) 0x78, (byte) 0xDA, (byte) 0x10, (byte) 0x3C, (byte) 0x4C, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0xFF};
        inputStream = new ByteArrayInputStream(compressedData);
        deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
    }

    @After
    public void tearDown() throws IOException {
        if (deflateCompressorInputStream != null) {
            deflateCompressorInputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    @Test
    public void testInitWithInputStream() {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        assertNotNull(deflateCompressorInputStream);
        assertSame(inputStream, deflateCompressorInputStream.getDelegateStream());
    }

    @Test
    public void testInitWithInputStreamAndParameters() {
        DeflateParameters deflateParameters = new DeflateParameters();
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, deflateParameters);
        assertNotNull(deflateCompressorInputStream);
        assertSame(inputStream, deflateCompressorInputStream.getDelegateStream());
        assertFalse(deflateCompressorInputStream.isZlibCompressed());
    }

    @Test(expected = NullPointerException.class)
    public void testInitWithNullInputStream() {
        new DeflateCompressorInputStream(null);
    }

    @Test
    public void testReadByte() throws IOException {
        byte testByte = (byte) deflateCompressorInputStream.read();
        assertEquals((byte) 0x78, testByte);
        count(1);
    }

    @Test
    public void testReadByteEndOfStream() throws IOException {
        byte testByte = (byte) deflateCompressorInputStream.read();
        assertEquals((byte) 0xFF, testByte);
        count(1);
        testByte = (byte) deflateCompressorInputStream.read();
        assertNull(testByte);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] testBytes = new byte[10];
        int bytesRead = deflateCompressorInputStream.read(testBytes, 0, testBytes.length);
        assertEquals(testBytes.length, bytesRead);
        byte testByte = testBytes[0];
        assertEquals((byte) 0x78, testByte);
        count(testBytes.length);
        testByte = testBytes[testBytes.length - 1];
        assertEquals((byte) 0xFF, testByte);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayNegativeOffset() throws IOException {
        byte[] testBytes = new byte[10];
        deflateCompressorInputStream.read(testBytes, -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayNegativeLength() throws IOException {
        byte[] testBytes = new byte[10];
        deflateCompressorInputStream.read(testBytes, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayInvalidParameters() throws IOException {
        byte[] testBytes = new byte[10];
        deflateCompressorInputStream.read(testBytes, 1, 10 - 1);
    }

    @Test
    public void testSkip() throws IOException {
        long bytesToSkip = 5;
        long bytesSkipped = deflateCompressorInputStream.skip(bytesToSkip);
        assertEquals(bytesToSkip, bytesSkipped);
        count(bytesToSkip);
        testByte = (byte) deflateCompressorInputStream.read();
        assertEquals((byte) 0x7D, testByte); // next byte after skipping 5 bytes
    }

    @Test
    public void testAvailable() throws IOException {
        int availableBytes = deflateCompressorInputStream.available();
        assertEquals(5, availableBytes);
        testByte = (byte) deflateCompressorInputStream.read();
        assertEquals((byte) 0x7D, testByte);
        availableBytes = deflateCompressorInputStream.available();
        assertEquals(4, availableBytes);
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Inflater inflater = new Inflater();
        InflaterInputStream inflaterInputStream = new InflaterInputStream(deflateCompressorInputStream);
        byte[] buffer = new byte[10];
        int bytesRead;
        do {
            bytesRead = inflaterInputStream.read(buffer, 0, buffer.length);
            outputStream.write(buffer, 0, bytesRead);
        } while (bytesRead != -1);
        byte[] expectedData = outputStream.toByteArray();
        deflateCompressorInputStream.close();
        deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(expectedData));
        byte testByte = (byte) deflateCompressorInputStream.read();
        assertEquals((byte) 0x0, testByte);
        testByte = (byte) deflateCompressorInputStream.read();
        assertNull(testByte);
    }

    private void count(int count) {
        if (count > 0) {
            System.out.println("Read " + count + " bytes");
        }
    }

    private byte testByte;

}
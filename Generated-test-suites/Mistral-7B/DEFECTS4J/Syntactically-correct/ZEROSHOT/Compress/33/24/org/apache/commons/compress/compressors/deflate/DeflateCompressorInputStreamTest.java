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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DeflateCompressorInputStreamTest {

    private InputStream inputStream;
    private DeflateCompressorInputStream deflateCompressorInputStream;

    @Before
    public void setUp() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("Deflate\u0000\u0014\u0000".getBytes()); // zlib header
        outputStream.write("Lorem ipsum dolor sit amet".getBytes()); // data
        byte[] bytes = outputStream.toByteArray();
        inputStream = new ByteArrayInputStream(bytes);
        deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
    }

    @After
    public void tearDown() throws Exception {
        if (deflateCompressorInputStream != null) {
            deflateCompressorInputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    @Test
    public void testDeflateCompressorInputStreamConstructorWithInputStream() {
        assertNotNull(deflateCompressorInputStream);
    }

    @Test
    public void testDeflateCompressorInputStreamConstructorWithInputStreamAndParameters() {
        DeflateParameters deflateParameters = new DeflateParameters();
        deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, deflateParameters);
        assertFalse(deflateCompressorInputStream.getInput().markSupported());
        assertFalse(deflateCompressorInputStream.getInput().reset());
    }

    @Test(expected = IOException.class)
    public void testDeflateCompressorInputStreamConstructorWithNullInputStream() {
        new DeflateCompressorInputStream(null);
    }

    @Test
    public void testDeflateCompressorInputStreamRead() throws IOException {
        byte[] buffer = new byte[1];
        int read = deflateCompressorInputStream.read(buffer, 0, buffer.length);
        assertEquals(0x44, buffer[0]); // 'D'
        assertEquals(1, read);

        read = deflateCompressorInputStream.read(buffer, 0, buffer.length);
        assertEquals('e', buffer[0]);
        assertEquals(1, read);

        read = deflateCompressorInputStream.read();
        assertEquals('f', (byte) read);
        assertEquals(1, read);

        read = deflateCompressorInputStream.read();
        assertEquals('\u0000', (byte) read);
        assertEquals(-1, read);
    }

    @Test
    public void testDeflateCompressorInputStreamReadByteArray() throws IOException {
        byte[] buffer = new byte[10];
        int length = deflateCompressorInputStream.read(buffer, 0, buffer.length);
        assertEquals(10, length);

        assertEquals('D', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('f', buffer[2]);
        assertEquals('l', buffer[3]);
        assertEquals('o', buffer[4]);
        assertEquals('r', buffer[5]);
        assertEquals(' ', buffer[6]);
        assertEquals('i', buffer[7]);
        assertEquals('p', buffer[8]);
        assertEquals('s', buffer[9]);
    }

    @Test
    public void testDeflateCompressorInputStreamClose() throws IOException {
        deflateCompressorInputStream.close();
        assertNull(deflateCompressorInputStream.getInput());
    }

    @Test
    public void testDeflateCompressorInputStreamAvailable() throws IOException {
        assertEquals(15, deflateCompressorInputStream.available());
    }

    @Test
    public void testDeflateCompressorInputStreamCount() throws IOException {
        int read = deflateCompressorInputStream.read(new byte[1][], 0, 1);
        deflateCompressorInputStream.count(read);
        assertEquals(1, deflateCompressorInputStream.getBytesRead());

        deflateCompressorInputStream.count(123);
        assertEquals(124, deflateCompressorInputStream.getBytesRead());
    }

    @Test
    public void testDeflateCompressorInputStreamSkip() throws IOException {
        deflateCompressorInputStream.skip(2);
        assertEquals(8, deflateCompressorInputStream.available());
    }

    @Test(expected = IOException.class)
    public void testDeflateCompressorInputStreamMarkAndReset() throws IOException {
        deflateCompressorInputStream.mark(1);
        deflateCompressorInputStream.reset();
    }

    @Test
    public void testDeflateCompressorInputStreamGetInput() {
        InputStream input = deflateCompressorInputStream.getInput();
        assertNotNull(input);
        assertFalse(input.markSupported());
        assertFalse(input.reset());
    }

    @Test
    public void testDeflateCompressorInputStreamWithoutZlibHeader() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("Lorem ipsum dolor sit amet".getBytes()); // data
        byte[] bytes = outputStream.toByteArray();
        inputStream = new ByteArrayInputStream(bytes);
        deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters(false));

        byte[] buffer = new byte[1];
        int read = deflateCompressorInputStream.read(buffer, 0, buffer.length);
        assertEquals('L', buffer[0]);
        assertEquals(1, read);
    }
}
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for Base64InputStream.
 */
public class Base64InputStreamTest {

    /**
     * Test default constructor.
     */
    @Test
    public void testDefaultConstructor() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Hello, World!"
                .getBytes()});
        Base64InputStream stream = new Base64InputStream(input);
        byte[] expected = new byte[]{"SGVsbG8gV29ybGQh"
                .getBytes()};
        byte[] result = new byte[512];
        int bytesRead = 0;
        while (bytesRead != -1) {
            bytesRead = stream.read(result, 0, result.length);
            if (bytesRead > 0) {
                assertArrayEquals(expected, result, 0, bytesRead);
            }
        }
        assertFalse(stream.hasRemaining());
        stream.close();
    }

    /**
     * Test constructor with decoding.
     */
    @Test
    public void testConstructorWithDecoding() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"SGVsbG8gV29ybGQh"
                .getBytes()});
        Base64InputStream stream = new Base64InputStream(input, false);
        byte[] expected = new byte[]{"Hello, World!".getBytes()};
        byte[] result = new byte[expected.length];
        int bytesRead = 0;
        while (bytesRead != -1) {
            bytesRead = stream.read(result, 0, result.length);
            if (bytesRead > 0) {
                assertArrayEquals(expected, result, 0, bytesRead);
            }
        }
        assertFalse(stream.hasRemaining());
        stream.close();
    }

    /**
     * Test constructor with encoding.
     */
    @Test
    public void testConstructorWithEncoding() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Hello, World!"
                .getBytes()});
        Base64OutputStream output = new Base64OutputStream(new ByteArrayOutputStream(1024), false);
        Base64InputStream stream = new Base64InputStream(input, true, 76, new byte[]{0x13, 0x10, 0x11, 0x14});
        byte[] expected = new byte[]{"SGVsbG8gV29ybGQh"
                .getBytes()};
        byte[] result = new byte[expected.length];
        int bytesRead = 0;
        while (bytesRead != -1) {
            bytesRead = stream.read(result, 0, result.length);
            if (bytesRead > 0) {
                assertArrayEquals(expected, result, 0, bytesRead);
            }
        }
        assertFalse(stream.hasRemaining());
        stream.close();
        assertEquals(expected, output.toByteArray());
    }

    /**
     * Test reading a single byte.
     */
    @Test
    public void testReadSingleByte() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"A"
                .getBytes()});
        Base64InputStream stream = new Base64InputStream(input);
        byte expected = 'A';
        assertEquals(expected, (byte) stream.read());
        assertFalse(stream.hasRemaining());
        stream.close();
    }

    /**
     * Test reading multiple bytes.
     */
    @Test
    public void testReadMultipleBytes() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"ABC"
                .getBytes()});
        Base64InputStream stream = new Base64InputStream(input);
        byte[] expected = new byte[]{'A', 'B', 'C'};
        byte[] result = new byte[expected.length];
        int bytesRead = stream.read(result, 0, result.length);
        assertArrayEquals(expected, result, 0, bytesRead);
        assertFalse(stream.hasRemaining());
        stream.close();
    }

    /**
     * Test markSupported.
     */
    @Test
    public void testMarkSupported() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Hello, World!"
                .getBytes()});
        Base64InputStream stream = new Base64InputStream(input, false);
        assertFalse(stream.markSupported());
        stream.close();
    }

    /**
     * Test close.
     */
    @Test
    public void testClose() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Hello, World!"
                .getBytes()});
        Base64InputStream stream = new Base64InputStream(input, false);
        assertTrue(stream.hasRemaining());
        stream.close();
        assertNull(stream.in);
        assertFalse(stream.hasRemaining());
    }

    /**
     * Test reading past EOF.
     */
    @Test(expected = IOException.class)
    public void testReadPastEof() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Hello, World!"
                .getBytes()});
        Base64InputStream stream = new Base64InputStream(input, false);
        byte[] result = new byte[1];
        stream.read(result, 0, result.length);
        stream.read(result, 0, result.length);
        stream.read(result, 0, result.length);
    }
}
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Class to test {@link Base64InputStream} methods.
 */
public class Base64InputStreamTest {

    private static final byte[] TEST_DATA = "Test data".getBytes();

    /**
     * Test Base64InputStream with default decoding.
     *
     * @throws IOException when an I/O error occurs.
     */
    @Test
    public void testDefaultDecoding() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        int readBytes;
        byte[] buffer = new byte[1024];
        do {
            readBytes = base64InputStream.read(buffer, 0, buffer.length);
            base64OutputStream.write(buffer, 0, readBytes);
        } while (readBytes > 0);

        Assert.assertEquals(TEST_DATA.length, outputStream.size());
        Assert.assertArrayEquals(TEST_DATA, outputStream.toByteArray());
    }

    /**
     * Test Base64InputStream with custom encoding.
     *
     * @throws IOException when an I/O error occurs.
     */
    @Test
    public void testCustomEncoding() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64 base64 = new Base64(64, new byte[0]);
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, base64);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 64, new byte[0]);

        int readBytes;
        byte[] buffer = new byte[1024];
        do {
            readBytes = base64InputStream.read(buffer, 0, buffer.length);
            base64OutputStream.write(buffer, 0, readBytes);
        } while (readBytes > 0);

        Assert.assertEquals(TEST_DATA.length, outputStream.size());
        Assert.assertArrayEquals(TEST_DATA, outputStream.toByteArray());

        byte[] encoded = outputStream.toByteArray();
        ByteArrayInputStream encodedInputStream = new ByteArrayInputStream(encoded);
        ByteArrayOutputStream decodedOutputStream = new ByteArrayOutputStream();
        Base64InputStream decodedInputStream = new Base64InputStream(encodedInputStream, false, 64, new byte[0]);
        Base64OutputStream decodedOutputStream = new Base64OutputStream(decodedOutputStream);

        int decodedReadBytes;
        byte[] decodedBuffer = new byte[1024];
        do {
            decodedReadBytes = decodedInputStream.read(decodedBuffer, 0, decodedBuffer.length);
            decodedOutputStream.write(decodedBuffer, 0, decodedReadBytes);
        } while (decodedReadBytes > 0);

        Assert.assertArrayEquals(TEST_DATA, decodedOutputStream.toByteArray());
    }
}
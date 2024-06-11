import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for Base64InputStream class.
 */
public class Base64InputStreamTest {

    /**
     * Test constructor without encoding.
     */
    @Test
    public void testBase64InputStreamWithoutEncoding() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] expectedBytes = new byte[]{'T', 'e', 's', 't', ' ', 'd', 'a', 't', 'a', ' ', 'd', 'a', 't', 'a'};

        byte[] receivedBytes = new byte[expectedBytes.length];
        int readBytes = base64InputStream.read(receivedBytes, 0, expectedBytes.length);
        Assert.assertEquals(expectedBytes.length, readBytes);
        Assert.assertArrayEquals(expectedBytes, receivedBytes);
    }

    /**
     * Test constructor with decoding.
     */
    @Test
    public void testBase64InputStreamWithDecoding() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("SGVsbG8sIFdvcmxkIQ=".getBytes());
        outputStream.flush();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] expectedBytes = new byte[]{'T', 'e', 's', 't', ' ', 'd', 'a', 't', 'a', ' '};

        byte[] receivedBytes = new byte[expectedBytes.length];
        int readBytes = base64InputStream.read(receivedBytes, 0, expectedBytes.length);
        Assert.assertEquals(expectedBytes.length, readBytes);
        Assert.assertArrayEquals(expectedBytes, receivedBytes);
    }

    /**
     * Test constructor with encoding.
     */
    @Test
    public void testBase64InputStreamWithEncoding() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        Base64 base64 = new Base64(76, "\r\n".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76, "\r\n".getBytes());

        String encodedString = base64InputStream.toString();
        String expectedString = "VXNlcg==\r\nVXNlcg==\r\nVXNlcg==\r\nVXNlcg==\r\nVXNlcg==\r\nVXNlcg==\r\nVXNlcg==\r\nVXNlcg==\r\nVXNlcg==\r\nVXNlcg==\r\nVXNlcg==";
        Assert.assertEquals(expectedString, encodedString);
    }

    /**
     * Test read() method with empty input.
     */
    @Test
    public void testBase64InputStreamEmptyInput() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int readBytes = base64InputStream.read();
        Assert.assertEquals(-1, readBytes);
    }

    /**
     * Test read() method with null input.
     */
    @Test(expected = NullPointerException.class)
    public void testBase64InputStreamNullInput() {
        Base64InputStream base64InputStream = new Base64InputStream(null);
    }

    /**
     * Test read(byte[], int, int) method with empty input.
     */
    @Test
    public void testBase64InputStreamEmptyInputArray() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] result = new byte[0];
        int readBytes = base64InputStream.read(result, 0, 0);
        Assert.assertEquals(0, readBytes);
    }

    /**
     * Test read(byte[], int, int) method with invalid input.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testBase64InputStreamInvalidInput() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] result = new byte[5];
        int readBytes = base64InputStream.read(result, -1, 5);
    }

    /**
     * Test markSupported() method.
     */
    @Test
    public void testBase64InputStreamMarkSupported() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        Assert.assertFalse(base64InputStream.markSupported());
    }
}
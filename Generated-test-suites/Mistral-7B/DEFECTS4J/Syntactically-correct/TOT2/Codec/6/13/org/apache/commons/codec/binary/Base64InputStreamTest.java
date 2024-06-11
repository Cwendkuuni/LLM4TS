import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test cases for Base64InputStream class.
 */
public class Base64InputStreamTest {

    /**
     * Test constructor without encoding.
     * @throws IOException
     */
    @Test
    public void testBase64InputStreamWithoutEncoding() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x31, 0x32, 0x33});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] expectedOutput = new byte[]{0x31, 0x32, 0x33};

        // When
        byte[] actualOutput = new byte[5];
        int length = base64InputStream.read(actualOutput, 0, 5);

        // Then
        Assert.assertArrayEquals(expectedOutput, actualOutput, 0, length);

        // Clean up
        base64InputStream.close();
        inputStream.close();
    }

    /**
     * Test constructor with encoding.
     * @throws IOException
     */
    @Test
    public void testBase64InputStreamWithEncoding() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x31, 0x32, 0x33});
        Base64 base64 = new Base64(64);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, Base64.NO_LINE_BREAKS);
        base64OutputStream.write(0x31);
        base64OutputStream.write(0x32);
        base64OutputStream.write(0x33);
        base64OutputStream.close();
        byte[] encodedInput = outputStream.toByteArray();
        ByteArrayInputStream encodedInputStream = new ByteArrayInputStream(encodedInput);
        Base64InputStream base64InputStream = new Base64InputStream(encodedInputStream, true);
        byte[] expectedOutput = new byte[]{0x31, 0x32, 0x33};

        // When
        byte[] actualOutput = new byte[5];
        int length = base64InputStream.read(actualOutput, 0, 5);

        // Then
        Assert.assertArrayEquals(expectedOutput, actualOutput, 0, length);

        // Clean up
        base64InputStream.close();
        base64OutputStream.close();
        inputStream.close();
        outputStream.close();
    }

    /**
     * Test read method with empty input stream.
     * @throws IOException
     */
    @Test
    public void testBase64InputStreamWithEmptyInput() throws IOException {
        // Given
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0][]));
        byte[] expectedOutput = new byte[0];

        // When
        byte[] actualOutput = new byte[5];
        int length = base64InputStream.read(actualOutput, 0, 5);

        // Then
        Assert.assertArrayEquals(expectedOutput, actualOutput, 0, length);

        // Clean up
        base64InputStream.close();
    }

    /**
     * Test read method with invalid input.
     * @throws IOException
     */
    @Test(expected = IllegalStateException.class)
    public void testBase64InputStreamWithInvalidInput() throws IOException {
        // Given
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{0xFF}));

        // When
        base64InputStream.read();

        // Then
        // Exception should be thrown

        // Clean up
        base64InputStream.close();
    }

    /**
     * Test markSupported method.
     * @throws IOException
     */
    @Test
    public void testBase64InputStreamMarkSupported() throws IOException {
        // Given
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0][]));

        // When and Then
        Assert.assertFalse(base64InputStream.markSupported());

        // Clean up
        base64InputStream.close();
    }
}
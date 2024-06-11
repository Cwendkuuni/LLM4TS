package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;

import org.junit.Test;

/**
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

    /**
     * Test constructor Base64InputStream(InputStream in)
     */
    @Test
    public void testBase64InputStreamConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        assertTrue(base64InputStream instanceof Base64InputStream);

        Base64 decoder = new Base64();
        byte[] expectedResult = decoder.decode(new byte[] { 61 }); // 'A' is encoded as 'Q' in Base64
        byte[] result = new byte[base64InputStream.read(new byte[1], 0, 1)];
        assertArrayEquals(expectedResult, result);

        // Test EOF
        assertEquals(-1, base64InputStream.read(new byte[1], 0, 1));
        assertEquals(-1, base64InputStream.read(new byte[1], 0, 1));
        assertFalse(base64InputStream.hasNextByte());
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode)
     */
    @Test
    public void testBase64InputStreamConstructorEncode() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        assertTrue(base64InputStream instanceof Base64InputStream);

        Base64 encoder = new Base64();
        byte[] expectedResult = encoder.encode(new byte[] { 'A' });
        byte[] result = new byte[base64InputStream.read(new byte[1], 0, 1)];
        assertArrayEquals(expectedResult, result);

        // Test EOF
        assertEquals(-1, base64InputStream.read(new byte[1], 0, 1));
        assertEquals(-1, base64InputStream.read(new byte[1], 0, 1));
        assertFalse(base64InputStream.hasNextByte());
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator)
     */
    @Test
    public void testBase64InputStreamConstructorWithLineLengthAndLineSeparator() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 5, new byte[] { 13, 10 }); // CRLF
        assertTrue(base64InputStream instanceof Base64InputStream);

        Base64 decoder = new Base64();
        byte[] expectedResult = decoder.decode(new byte[] { 61, 13, 10 }); // 'A' is encoded as 'Q' in Base64 followed by CRLF
        byte[] result = new byte[base64InputStream.read(new byte[3], 0, 3)];
        assertArrayEquals(expectedResult, result);

        // Test EOF
        assertEquals(-1, base64InputStream.read(new byte[1], 0, 1));
        assertEquals(-1, base64InputStream.read(new byte[1], 0, 1));
        assertFalse(base64InputStream.hasNextByte());
    }

    /**
     * Test read method
     */
    @Test
    public void testBase64InputStreamReadMethod() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A', 'B' });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] expectedResult = new byte[] { 'A' };
        byte[] result = new byte[base64InputStream.read(new byte[1], 0, 1)];
        assertArrayEquals(expectedResult, result);

        expectedResult = new byte[] { 'B' };
        result = new byte[base64InputStream.read(new byte[1], 0, 1)];
        assertArrayEquals(expectedResult, result);

        // Test EOF
        assertEquals(-1, base64InputStream.read(new byte[1], 0, 1));
        assertFalse(base64InputStream.hasNextByte());

        // Test encoding
        inputStream = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });
        base64InputStream = new Base64InputStream(inputStream, true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = base64InputStream.read(buffer, 0, buffer.length)) > 0) {
            outputStream.write(buffer, 0, bytesRead);
        }
        byte[] resultBytes = outputStream.toByteArray();
        String expectedBase64 = "AQIDBA==";
        String resultBase64 = new String(resultBytes);
        assertEquals(expectedBase64, resultBase64);
    }

    /**
     * Test read method with large input
     */
    @Test
    public void testBase64InputStreamReadMethodWithLargeInput() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[1024 * 1024]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = base64InputStream.read(buffer, 0, buffer.length)) > 0) {
            outputStream.write(buffer, 0, bytesRead);
        }
        byte[] resultBytes = outputStream.toByteArray();
        byte[] expectedBytes = new byte[1024 * 1024];
        // The actual encoded size depends on the input
        byte[] expectedBase64 = Arrays.copyOfRange(Base64.encode(expectedBytes), 0, Base64.encode(expectedBytes).length);
        String resultBase64 = new String(resultBytes);
        assertEquals(new String(expectedBase64), resultBase64);
    }
}
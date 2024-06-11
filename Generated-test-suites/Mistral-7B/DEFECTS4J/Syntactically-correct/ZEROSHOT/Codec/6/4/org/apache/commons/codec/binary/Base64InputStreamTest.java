package org.apache.commons.codec.binary;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for Base64InputStream.
 */
public class Base64InputStreamTest {

    /**
     * Test instance creation.
     */
    @Test
    public void testInstanceCreation() {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);
        Base64InputStream base64InputStreamDecoding = new Base64InputStream(input, false);
        Base64InputStream base64InputStreamEncoding = new Base64InputStream(input, true);

        Base64InputStream base64InputStreamLineLength = new Base64InputStream(input, false, 5, new byte[]{'\r', '\n'});
        Base64InputStream base64InputStreamLineLengthEncoding = new Base64InputStream(input, true, 5, new byte[]{'\r', '\n'});
    }

    /**
     * Test read method.
     */
    @Test
    public void testRead() throws IOException {
        String testString = "Test";
        byte[] testBytes = testString.getBytes();

        InputStream input = new ByteArrayInputStream(testBytes);
        Base64InputStream base64InputStream = new Base64InputStream(input);
        Base64InputStream base64InputStreamDecoding = new Base64InputStream(input, false);

        byte[] result = new byte[testBytes.length];
        int bytesRead = base64InputStreamDecoding.read(result);
        Assert.assertEquals(testBytes.length, bytesRead);
        Assert.assertArrayEquals(testBytes, result);

        int readByte = base64InputStreamDecoding.read();
        Assert.assertEquals(-1, readByte);

        // Test read with custom Base64InputStream
        Base64 base64 = new Base64();
        byte[] base64EncodedBytes = base64.encode(testBytes);
        InputStream base64InputStreamEncoding = new ByteArrayInputStream(base64EncodedBytes);
        Base64InputStream base64InputStreamDecodingEncoded = new Base64InputStream(base64InputStreamEncoding, false);

        result = new byte[testBytes.length];
        bytesRead = base64InputStreamDecodingEncoded.read(result);
        Assert.assertEquals(testBytes.length, bytesRead);
        Assert.assertArrayEquals(testBytes, result);

        readByte = base64InputStreamDecodingEncoded.read();
        Assert.assertEquals(-1, readByte);
    }

    /**
     * Test read(byte[], int, int) method.
     */
    @Test
    public void testReadByteArray() throws IOException {
        String testString = "Test";
        byte[] testBytes = testString.getBytes();

        InputStream input = new ByteArrayInputStream(testBytes);
        Base64InputStream base64InputStream = new Base64InputStream(input);
        Base64InputStream base64InputStreamDecoding = new Base64InputStream(input, false);

        byte[] result = new byte[testBytes.length];
        int bytesRead = base64InputStreamDecoding.read(result);
        Assert.assertEquals(testBytes.length, bytesRead);
        Assert.assertArrayEquals(testBytes, result);

        InputStream inputEncoded = new ByteArrayInputStream(Base64.encode(testBytes));
        Base64InputStream base64InputStreamEncoding = new Base64InputStream(inputEncoded, true);
        Base64InputStream base64InputStreamDecodingEncoded = new Base64InputStream(inputEncoded, false);

        result = new byte[testBytes.length];
        bytesRead = base64InputStreamDecodingEncoded.read(result);
        Assert.assertEquals(testBytes.length, bytesRead);
        Assert.assertArrayEquals(testBytes, result);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(testBytes);
        byte[] base64Encoded = Base64.encode(output.toByteArray());
        InputStream inputEncoded2 = new ByteArrayInputStream(base64Encoded);
        Base64InputStream base64InputStreamDecodingEncoded2 = new Base64InputStream(inputEncoded2, false);

        result = new byte[testBytes.length];
        bytesRead = base64InputStreamDecodingEncoded2.read(result);
        Assert.assertEquals(testBytes.length, bytesRead);
        Assert.assertArrayEquals(testBytes, result);
    }

    /**
     * Test markSupported method.
     */
    @Test
    public void testMarkSupported() {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);
        Assert.assertFalse(base64InputStream.markSupported());
    }

    /**
     * Test custom lineLength and lineSeparator.
     */
    @Test
    public void testCustomLineLengthAndLineSeparator() throws IOException {
        String testString = "TestTestTestTestTestTest";
        byte[] testBytes = testString.getBytes();

        InputStream input = new ByteArrayInputStream(testBytes);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(testBytes);
        byte[] base64Encoded = Base64.encode(output.toByteArray(), 75, new byte[]{'\r', '\n'});
        InputStream inputEncoded = new ByteArrayInputStream(base64Encoded);

        Base64InputStream base64InputStream = new Base64InputStream(inputEncoded, true, 75, new byte[]{'\r', '\n'});
        ByteArrayOutputStream output2 = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int bytesRead;

        while ((bytesRead = base64InputStream.read(buffer)) != -1) {
            output2.write(buffer, 0, bytesRead);
        }

        Assert.assertEquals(testString, output2.toString());

        InputStream decodedInput = new ByteArrayInputStream(Base64.decode(output2.toByteArray()));
        Base64InputStream base64InputStreamDecoding = new Base64InputStream(decodedInput, false, 75, new byte[]{'\r', '\n'});
        byte[] result = new byte[testBytes.length];
        int bytesReadDecoded = base64InputStreamDecoding.read(result);
        Assert.assertEquals(testBytes.length, bytesReadDecoded);
        Assert.assertArrayEquals(testBytes, result);
    }
}
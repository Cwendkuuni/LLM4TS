package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

    @Test
    public void testBase64InputStream_DefaultConstructor() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("RnJvbSBvZiBEYXkgWzBdLg==".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] resultBytes = new byte[1024];
        int bytesRead;

        do {
            bytesRead = base64InputStream.read(resultBytes, 0, resultBytes.length);
            outputStream.write(resultBytes, 0, bytesRead);
        } while (bytesRead != -1);

        Assert.assertEquals("Decoded data is not equal to the original data.", "Hello World!".getBytes(), outputStream.toByteArray());
    }

    @Test
    public void testBase64InputStream_EncodeConstructor() throws IOException {
        String originalData = "Hello World!";
        InputStream inputStream = new ByteArrayInputStream(originalData.getBytes());

        ByteArrayOutputStream encodedOutputStream = new ByteArrayOutputStream();
        Base64 base64Encoder = new Base64();

        byte[] bytesToEncode = new byte[1024];
        int bytesEncoded;

        do {
            bytesEncoded = inputStream.read(bytesToEncode, 0, bytesToEncode.length);
            if (bytesEncoded > 0) {
                byte[] encodedBytes = base64Encoder.encode(bytesToEncode, 0, bytesEncoded);
                encodedOutputStream.write(encodedBytes, 0, encodedBytes.length);
            }
        } while (bytesEncoded != -1);

        String encodedData = new String(encodedOutputStream.toByteArray());

        InputStream encodedInputStream = new ByteArrayInputStream(encodedData.getBytes());

        ByteArrayOutputStream decodedOutputStream = new ByteArrayOutputStream();
        byte[] decodedBytes = new byte[1024];

        Base64InputStream base64InputStream = new Base64InputStream(encodedInputStream, true);

        do {
            int bytesDecoded = base64InputStream.read(decodedBytes, 0, decodedBytes.length);
            decodedOutputStream.write(decodedBytes, 0, bytesDecoded);
        } while (bytesDecoded != -1);

        Assert.assertEquals("Encoded and decoded data are not equal.", originalData, new String(decodedOutputStream.toByteArray()));
    }

    @Test
    public void testBase64InputStream_CustomConstructor() throws IOException {
        String originalData = "Hello World!";
        InputStream inputStream = new ByteArrayInputStream(originalData.getBytes());

        ByteArrayOutputStream encodedOutputStream = new ByteArrayOutputStream();
        Base64 base64Encoder = new Base64(72, "\r\n".getBytes());

        byte[] bytesToEncode = new byte[1024];
        int bytesEncoded;

        do {
            bytesEncoded = inputStream.read(bytesToEncode, 0, bytesToEncode.length);
            if (bytesEncoded > 0) {
                byte[] encodedBytes = base64Encoder.encode(bytesToEncode, 0, bytesEncoded);
                encodedOutputStream.write(encodedBytes, 0, encodedBytes.length);
            }
        } while (bytesEncoded != -1);

        String encodedData = new String(encodedOutputStream.toByteArray());

        InputStream encodedInputStream = new ByteArrayInputStream(encodedData.getBytes());

        ByteArrayOutputStream decodedOutputStream = new ByteArrayOutputStream();
        byte[] decodedBytes = new byte[1024];

        Base64InputStream base64InputStream = new Base64InputStream(encodedInputStream, true, 72, "\r\n".getBytes());

        do {
            int bytesDecoded = base64InputStream.read(decodedBytes, 0, decodedBytes.length);
            decodedOutputStream.write(decodedBytes, 0, bytesDecoded);
        } while (bytesDecoded != -1);

        Assert.assertEquals("Encoded and decoded data are not equal.", originalData, new String(decodedOutputStream.toByteArray()));
    }

    @Test(expected = IOException.class)
    public void testBase64InputStream_Exception_NegativeLineLength() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("RnJvbSBvZiBEYXkgWzBdLg==".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, -1, null);
    }

    @Test(expected = IOException.class)
    public void testBase64InputStream_Exception_NullLineSeparator() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("RnJvbSBvZiBEYXkgWzBdLg==".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 72, null);
    }

    @Test(expected = IOException.class)
    public void testBase64InputStream_Exception_NegativeLineLengthAndNullLineSeparator() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("RnJvbSBvZiBEYXkgWzBdLg==".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, -1, null);
    }
}
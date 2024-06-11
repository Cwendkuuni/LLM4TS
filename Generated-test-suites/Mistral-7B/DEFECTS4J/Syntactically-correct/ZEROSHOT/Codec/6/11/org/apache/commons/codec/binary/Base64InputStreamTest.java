package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;

import org.junit.Test;

/**
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

    private static byte[] inputData = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a };

    /**
     * Test constructor without encoding or decoding
     *
     * @throws IOException
     */
    @Test
    public void testConstructorWithoutEncodingOrDecoding() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(inputData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        // do nothing
        // verify that the instance is created correctly
        assertEquals(inputStream, base64InputStream.in);
        assertFalse(base64InputStream.doEncode);
    }

    /**
     * Test constructor for decoding
     *
     * @throws IOException
     */
    @Test
    public void testConstructorForDecoding() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(inputData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        // verify decoding
        byte[] decodedData = new byte[inputData.length];
        int read = base64InputStream.read(decodedData, 0, decodedData.length);
        assertArrayEquals(inputData, decodedData);
        assertEquals(inputData.length, read);
    }

    /**
     * Test constructor for encoding
     *
     * @throws IOException
     */
    @Test
    public void testConstructorForEncoding() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(inputData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        // verify encoding
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] encodedData = new byte[inputData.length * 4 / 3 + 4];
        int encodedLength = base64InputStream.read(encodedData, 0, encodedData.length);
        assertArrayEquals(encodedData, 0, encodedLength);
        Base64 base64 = new Base64();
        byte[] expectedEncodedData = base64.encode(inputData);
        assertArrayEquals(expectedEncodedData, encodedData, 0, encodedLength);
    }

    /**
     * Test read method with decoding
     *
     * @throws IOException
     */
    @Test
    public void testReadWithDecoding() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(inputData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] decodedData = new byte[inputData.length];
        int read = base64InputStream.read(decodedData, 0, decodedData.length);
        assertArrayEquals(inputData, decodedData);
        assertEquals(inputData.length, read);
    }

    /**
     * Test read method with encoding
     *
     * @throws IOException
     */
    @Test
    public void testReadWithEncoding() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(inputData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] encodedData = new byte[inputData.length * 4 / 3 + 4];
        int read = base64InputStream.read(encodedData, 0, encodedData.length);
        assertArrayEquals(encodedData, 0, read);
        Base64 base64 = new Base64();
        byte[] expectedEncodedData = base64.encode(inputData);
        assertArrayEquals(expectedEncodedData, encodedData, 0, read);
        assertEquals(read, encodedData.length);
    }

    /**
     * Test markSupported method
     *
     * @throws IOException
     */
    @Test
    public void testMarkSupported() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(inputData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        assertFalse(base64InputStream.markSupported());
    }
}
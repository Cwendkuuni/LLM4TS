package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for Base64InputStream class
 */
public class Base64InputStreamTest {

    private InputStream inputStream;
    private Base64InputStream base64InputStream;

    @Before
    public void setUp() throws IOException {
        String inputString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/";
        ByteArrayInputStream input = new ByteArrayInputStream(inputString.getBytes());
        base64InputStream = new Base64InputStream(input, false);
        inputStream = base64InputStream;
    }

    @Test
    public void testReadOneByte() throws IOException {
        byte expected = 'a';
        byte actual = (byte) base64InputStream.read();
        assertEquals(expected, actual);
    }

    @Test
    public void testReadOneByteEOF() throws IOException {
        byte expected = -1;
        byte actual = (byte) base64InputStream.read();
        assertEquals(expected, actual);
    }

    @Test
    public void testReadMultipleBytes() throws IOException {
        byte[] expected = { 'a', 'b' };
        byte[] actual = new byte[2];
        int read = base64InputStream.read(actual);
        assertEquals(expected.length, read);
        assertSame(expected, actual);
    }

    @Test
    public void testReadMultipleBytesEOF() throws IOException {
        byte[] expected = { 'a', 'b' };
        byte[] actual = new byte[3];
        int read = base64InputStream.read(actual);
        assertEquals(expected.length, read);
        assertSame(expected, actual, 0, 2);
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testMarkSupported() throws IOException {
        assertFalse(base64InputStream.markSupported());
    }

    @Test(expected = IOException.class)
    public void testCloseStreamAndRead() throws IOException {
        int read = base64InputStream.read();
        base64InputStream.close();
        read = base64InputStream.read();
    }

    @Test
    public void testEncodeDecode() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64 base64Encoder = new Base64();

        byte[] inputData = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/".getBytes();
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(inputData), true);
        byte[] outputData = new byte[inputData.length];
        int len = base64InputStream.read(outputData);

        byte[] decodedData = base64Encoder.decode(base64InputStream.getBase64().getBuffer());
        assertEquals(len, decodedData.length);
        assertSame(inputData, decodedData);
    }
}
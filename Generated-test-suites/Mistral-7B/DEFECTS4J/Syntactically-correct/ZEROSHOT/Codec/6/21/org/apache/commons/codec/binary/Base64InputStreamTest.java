package org.apache.commons.codec.binary;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;

/**
 * Test file for Base64InputStream class.
 */
public class Base64InputStreamTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        InputStream inputStream = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        assertNotNull(base64InputStream);
        assertEquals(false, base64InputStream.doEncode);
        assertEquals(Base64.DEFAULT, base64InputStream.base64);
    }

    /**
     * Test constructor with encode flag.
     */
    @org.junit.Test
    public void testConstructorWithEncodeFlag() {
        InputStream inputStream = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        assertNotNull(base64InputStream);
        assertEquals(true, base64InputStream.doEncode);
        assertEquals(Base64.DEFAULT, base64InputStream.base64);
    }

    /**
     * Test constructor with encode flag, lineLength and lineSeparator.
     */
    @org.junit.Test
    public void testConstructorWithEncodeFlagLineLengthLineSeparator() {
        InputStream inputStream = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 50, new byte[] { 13, 10 });
        assertNotNull(base64InputStream);
        assertEquals(true, base64InputStream.doEncode);
        assertEquals(new Base64(50, new byte[] { 13, 10 }), base64InputStream.base64);
    }

    /**
     * Test read method.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testRead() throws IOException {
        String testData = "TestData";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int read;
        byte[] buffer = new byte[1];
        while ((read = base64InputStream.read(buffer, 0, 1)) > 0) {
            base64OutputStream.write(buffer, 0, 1);
        }
        base64OutputStream.finish();
        String decodedData = new String(Base64.decodeBase64(outputStream.toByteArray()));
        assertEquals(testData, decodedData);
    }

    /**
     * Test read method with encode flag.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testReadWithEncodeFlag() throws IOException {
        String testData = "TestData";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        int read;
        byte[] buffer = new byte[1];
        while ((read = base64InputStream.read(buffer, 0, 1)) > 0) {
            outputStream.write(buffer, 0, 1);
        }
        String encodedData = new String(outputStream.toByteArray());
        String decodedData = new String(Base64.decodeBase64(Base64.encodeBase64(testData.getBytes())));
        assertEquals(encodedData, decodedData);
    }

    /**
     * Test markSupported method.
     */
    @org.junit.Test(expected = IOException.class)
    public void testMarkSupported() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.markSupported();
    }
}
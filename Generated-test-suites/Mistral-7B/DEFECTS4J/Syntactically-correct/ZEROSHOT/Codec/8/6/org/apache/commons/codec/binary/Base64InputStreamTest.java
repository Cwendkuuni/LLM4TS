package org.apache.commons.codec.binary;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

/**
 * Test class for Base64InputStream.
 */
public class Base64InputStreamTest {

    private InputStream inputStream;
    private InputStream base64InputStream;
    private ByteArrayOutputStream outputStream;

    /**
     * Set up the test environment.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Before
    public void setUp() throws IOException {
        String plainText = "Hello, World!";
        byte[] plainTextBytes = plainText.getBytes();
        outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream plainTextInputStream = new ByteArrayInputStream(plainTextBytes);
        inputStream = new Base64InputStream(plainTextInputStream, false);
        base64InputStream = new Base64InputStream(inputStream);
    }

    /**
     * Tear down the test environment.
     */
    @After
    public void tearDown() {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            // Ignore.
        }
    }

    /**
     * Test the Base64InputStream reading a single byte from a plain text InputStream.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testReadByte() throws IOException {
        int actual = base64InputStream.read();
        int expected = plainText.charAt(0);
        assertEquals(expected, actual);
    }

    /**
     * Test the Base64InputStream reading multiple bytes from a plain text InputStream.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testReadBytes() throws IOException {
        byte[] expected = plainText.getBytes();
        byte[] actual = new byte[expected.length];
        int read = base64InputStream.read(actual, 0, expected.length);
        assertArrayEquals(expected, actual);
        assertEquals(expected.length, read);
    }

    /**
     * Test the Base64InputStream decoding multiple lines from a plain text InputStream.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testDecodeMultipleLines() throws IOException {
        String plainText = "Hello,\r\nWorld!";
        byte[] plainTextBytes = plainText.getBytes();
        outputStream.write(plainText.getBytes());
        outputStream.write("\r\n".getBytes());
        byte[] input = outputStream.toByteArray();
        inputStream = new ByteArrayInputStream(input);
        base64InputStream = new Base64InputStream(inputStream);

        byte[] actual = new byte[plainText.length() + 2]; // for \r\n
        int read = base64InputStream.read(actual, 0, actual.length);
        assertArrayEquals(plainText.getBytes(), new ArraySection(actual, 0, plainText.length()));
        assertEquals('\r', actual[plainText.length()]);
        assertEquals('\n', actual[plainText.length() + 1]);
        assertEquals(read, plainText.length() + 2);
    }

    /**
     * Test the Base64InputStream encoding a single byte from a plain text InputStream.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testEncodeByte() throws IOException {
        byte[] expected = Base64.encodeBase64(new byte[]{0x48});
        byte[] actual = new byte[expected.length];
        int read = base64InputStream.read(actual, 0, expected.length);
        assertArrayEquals(expected, actual);
        assertEquals(expected.length, read);
    }

    /**
     * Test the Base64InputStream encoding multiple bytes from a plain text InputStream.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testEncodeBytes() throws IOException {
        String plainText = "Hello, World!";
        byte[] plainTextBytes = plainText.getBytes();
        byte[] expected = Base64.encodeBase64(plainTextBytes);
        byte[] actual = new byte[expected.length];
        int read = base64InputStream.read(actual, 0, expected.length);
        assertArrayEquals(expected, actual);
        assertEquals(expected.length, read);
    }
}

class ArraySection {

    private byte[] array;
    private int offset;
    private int length;

    public ArraySection(byte[] array, int offset, int length) {
        this.array = array;
        this.offset = offset;
        this.length = length;
    }

    public byte[] getArray() {
        return array;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }

    public byte get(int index) {
        if (index < 0 || index > offset + length) {
            throw new IndexOutOfBoundsException();
        }
        return array[offset + index];
    }
}
package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;

/**
 * Test class for {@link Base64InputStream}. This class uses JUnit 4.
 */
public class Base64InputStreamTest {

    /**
     * Tests the default constructor and the read method.
     * @throws IOException if an I/O error occurs.
     */
    @org.junit.Test
    public void testDefaultConstructorAndRead() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("TestData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // Test reading a single byte
        int testByte = base64InputStream.read();
        assertEquals(-1, testByte); // EOF should be reached after reading all bytes

        // Test reading multiple bytes
        byte[] testBytes = new byte[5];
        int bytesRead = base64InputStream.read(testBytes, 0, testBytes.length);
        assertNull(bytesRead); // EOF should be reached after reading all bytes

        // Test reading with a larger buffer
        byte[] largerBuffer = new byte[10];
        bytesRead = base64InputStream.read(largerBuffer, 0, largerBuffer.length);
        assertNull(bytesRead); // EOF should be reached after reading all bytes
    }

    /**
     * Tests the constructor with a doEncode parameter and the read method.
     * @throws IOException if an I/O error occurs.
     */
    @org.junit.Test
    public void testEncodeConstructorAndRead() throws IOException {
        String testData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdWwvYWNoIGNsYXNzLCB3aGJzIGlzIG5vdCBmb3IgeWVsbG8gZmlyc3Qgb2YgdGVjaG5vbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIu";
        byte[] encodedTestData = Base64.decodeBase64(testData);

        InputStream inputStream = new ByteArrayInputStream(encodedTestData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        // Test reading a single byte
        int testByte = base64InputStream.read();
        assertEquals(-1, testByte); // EOF should be reached after reading all bytes

        // Test reading multiple bytes
        byte[] testBytes = new byte[5];
        int bytesRead = base64InputStream.read(testBytes, 0, testBytes.length);
        assertNull(bytesRead); // EOF should be reached after reading all bytes

        // Test reading with a larger buffer
        byte[] largerBuffer = new byte[10];
        bytesRead = base64InputStream.read(largerBuffer, 0, largerBuffer.length);
        assertNull(bytesRead); // EOF should be reached after reading all bytes

        // Test encoding and decoding
        byte[] decodedTestData = new byte[encodedTestData.length];
        bytesRead = base64InputStream.read(decodedTestData, 0, decodedTestData.length);
        assertTrue(Base64.isBase64(decodedTestData));
        assertEquals(testData, Base64.encodeBase64String(decodedTestData));
    }

    /**
     * Tests the constructor with a doEncode parameter, a lineLength, and a lineSeparator, and the read method.
     * @throws IOException if an I/O error occurs.
     */
    @org.junit.Test
    public void testEncodeConstructorWithLineLengthAndLineSeparatorAndRead() throws IOException {
        String testData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdWwvYWNoIGNsYXNzLCB3aGJzIHdpdGg9IjE1MSIgYW5kIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdWwvYWNoIGNsYXNzLCB3aGJzIHdpdGg9IjE2MSIgYW5kIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdWwvYWNoIGNsYXNzLCB3aGJzIHdpdGg9IjE3MSIgYW5kIHlvdSBuZWVkIGEgY29tcHV0ZXIu";
        byte[] encodedTestData = Base64.decodeBase64(testData);

        InputStream inputStream = new ByteArrayInputStream(encodedTestData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76, "\r\n".getBytes());

        // Test reading a single byte
        int testByte = base64InputStream.read();
        assertEquals(-1, testByte); // EOF should be reached after reading all bytes

        // Test reading multiple bytes
        byte[] testBytes = new byte[5];
        int bytesRead = base64InputStream.read(testBytes, 0, testBytes.length);
        assertNull(bytesRead); // EOF should be reached after reading all bytes

        // Test reading with a larger buffer
        byte[] largerBuffer = new byte[10];
        bytesRead = base64InputStream.read(largerBuffer, 0, largerBuffer.length);
        assertNull(bytesRead); // EOF should be reached after reading all bytes

        // Test encoding and decoding
        byte[] decodedTestData = new byte[encodedTestData.length];
        bytesRead = base64InputStream.read(decodedTestData, 0, decodedTestData.length);
        assertTrue(Base64.isBase64(decodedTestData));
        assertEquals(testData, new String(decodedTestData, "US-ASCII"));
    }

    /**
     * Tests the constructor with a doEncode parameter and the read method with an empty InputStream.
     * @throws IOException if an I/O error occurs.
     */
    @org.junit.Test
    public void testEmptyInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        // Test reading a single byte
        int testByte = base64InputStream.read();
        assertEquals(-1, testByte); // EOF should be reached
    }

    /**
     * Tests the constructor with a doEncode parameter and the read method with an InputStream that throws an IOException.
     * @throws IOException if an I/O error occurs.
     */
    @org.junit.Test(expected = IOException.class)
    public void testIOException() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01 }) {
            @Override
            public int read() throws IOException {
                throw new IOException("Test IOException");
            }
        };
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        // Test reading a single byte
        int testByte = base64InputStream.read();
    }

    /**
     * Tests the constructor with a doEncode parameter and the read method with a null InputStream.
     * @throws IOException if an I/O error occurs.
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testNullInputStream() {
        Base64InputStream base64InputStream = new Base64InputStream(null, false);
    }

    /**
     * Tests the constructor with a doEncode parameter and the read method with an InputStream that has no data.
     * @throws IOException if an I/O error occurs.
     */
    @org.junit.Test
    public void testInputStreamWithNoData() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        // Test reading a single byte
        int testByte = base64InputStream.read();
        assertEquals(-1, testByte); // EOF should be reached after reading all bytes
    }

    /**
     * Tests the constructor with a doEncode parameter and the read method with an InputStream that has invalid data.
     * @throws IOException if an I/O error occurs.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidData() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43, 0x44 }); // The data must not be base64 encoded!
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
    }
}
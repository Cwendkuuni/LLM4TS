package org.apache.commons.codec.binary;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;

/**
 * Tests for {@link Base64InputStream}.
 */
public class Base64InputStreamTest {

    /**
     * Test decoding a Base64-encoded byte array.
     *
     * @throws IOException
     *             if the test fails due to an I/O error.
     */
    @Test
    public void testDecodeBase64() throws IOException {
        String base64Data = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdWwgYW4gVGVzdCB3aGF0IHlvdSBuZWVkIG5vdCB0byB0aGUgc2VwYXJhdG9yLCBidXQgdG8gZW5jb2RlZCBleHBhbmQgdG8gZnV0dXJlIG1hcmtlZCBzbyBkYXRhIGtleXBhbmQgdGhhdCB0aGUgZm9yIG5vdCBsb25nIHRoZSB5b3UgZ29vZ2xldXNlcmNvbnRlbnQuCiAgICAtLVBhdWwgYW4gVGVzdCB3aGF0IHlvdSBuZWVkIG5vdCB0byB0aGUgc2VwYXJhdG9yLCBidXQgdG8gZW5jb2RlZCBleHBhbmQgdG8gZnV0dXJlIG1hcmtlZCBzbyBkYXRhIGtleXBhbmQgdGhhdCB0aGUgZm9yIG5vdCBsb25nIHRoZSB5b3UgZ29vZ2xldXNlcmNvbnRlbnQuCiAgICAtLVBhdWwgYW4gVGVzdCB3aGF0IHlvdSBuZWVkIG5vdCB0byB0aGUgc2VwYXJhdG9yLCBidXQgdG8gZW5jb2RlZCBleHBhbmQgdG8gZnV0dXJlIG1hcmtlZCBzbyBkYXRhIGtleXBhbmQgdGhhdCB0aGUgZm9yIG5vdCBsb25nIHRoZSB5b3UgZ29vZ2xldXNlcmNvbnRlbnQuCiAgICAtLVBhdWwgYW4gVGVzdCB3aGF0IHlvdSBuZWVkIG5vdCB0byB0aGUgc2VwYXJhdG9yLCBidXQgdG8gZW5jb2RlZCBleHBhbmQgdG8gZnV0dXJlIG1hcmtlZCBzbyBkYXRhIGtleXBhbmQgdGhhdCB0aGUgZm9yIG5vdCBsb25nIHRoZSB5b3UgZ29vZ2xldXNlcmNvbnRlbnQuCiAgICAtLVBhdWwgYW4gVGVzdCB3aGF0IHlvdSBuZWVkIG5vdCB0byB0aGUgc2VwYXJhdG9yLCBidXQgdG8gZW5jb2RlZCBleHBhbmQgdG8gZnV0dXJlIG1hcmtlZCBzbyBkYXRhIGtleXBhbmQgdGhhdCB0aGUgZm9yIG5vdCBsb25nIHRoZSB5b3UgZ29vZ2xldXNlcmNvbnRlbnQuCiAgICAtLVBhdWwgYW4gVGVzdCB3aGF0IHlvdSBuZWVkIG5vdCB0byB0aGUgc2VwYXJhdG9yLCBidXQgdG8gZW5jb2RlZCBleHBhbmQgdG8gZnV0dXJlIG1hcmtlZCBzbyBkYXRhIGtleXBhbmQgdGhhdCB0aGUgZm9yIG5vdCBsb25nIHRoZSB5b3UgZ29vZ2xldXNlcmNvbnRlbnQuCiAgICAtLVBhdWwgYW4gVGVzdCB3aGF0IHlvdSBuZWVkIG5vdCB0byB0aGUgc2VwYXJhdG9yLCBidXQgdG8gZW5jb2RlZCBleHBhbmQgdG8gZnV0dXJlIG1hcmtlZCBzbyBkYXRhIGtleXBhbmQgdGhhdCB0aGUgZm9yIG5vdCBsb25nIHRoZSB5b3UgZ29vZ2xldXNlcmNvbnRlbnQuCnByZW1lYWJsZS4=";

        // Create a Base64InputStream from the base64-encoded byte array.
        InputStream inputStream = new ByteArrayInputStream(base64Data.getBytes());
        // Create a Base64InputStream that decodes data.
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // Create a byte array to store the decoded data.
        byte[] decodedData = new byte[base64Data.length() / 4 * 3];

        // Read the decoded data from the Base64InputStream.
        int read = base64InputStream.read(decodedData, 0, decodedData.length);
        // Check that the expected amount of data was read.
        assertEquals("Unexpected amount of data read.", decodedData.length, read);

        // Create a Base64 decoder and decode the decoded data to a string.
        Base64 decoder = new Base64();
        String decodedString = decoder.decodeToString(decodedData);

        // Check that the decoded string matches the original base64-encoded string.
        assertEquals("Decoded data does not match original base64 data.", base64Data, decodedString);
    }

    /**
     * Test encoding a byte array as Base64.
     *
     * @throws IOException
     *             if the test fails due to an I/O error.
     */
    @Test
    public void testEncodeBase64() throws IOException {
        byte[] inputData = { 0x41, 0x42, 0x43 };

        // Create a ByteArrayOutputStream to store the encoded data.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // Create a Base64OutputStream that encodes data.
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);

        // Write the input data to the Base64OutputStream.
        base64OutputStream.write(inputData, 0, inputData.length);

        // Flush the Base64OutputStream and close it.
        base64OutputStream.flush();
        base64OutputStream.close();

        // Check that the encoded data matches the expected value.
        byte[] encodedData = outputStream.toByteArray();
        assertEquals("Encoded data does not match expected value.", "RkVGBXKgVwopAW4=", new String(encodedData));
    }

    /**
     * Test decoding an empty Base64-encoded byte array.
     *
     * @throws IOException
     *             if the test fails due to an I/O error.
     */
    @Test
    public void testDecodeEmptyBase64() throws IOException {
        // Create an empty Base64InputStream.
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        // Create a Base64InputStream that decodes data.
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // Create a byte array to store the decoded data.
        byte[] decodedData = new byte[0];

        // Read the decoded data from the Base64InputStream.
        int read = base64InputStream.read(decodedData, 0, decodedData.length);

        // Check that no data was read.
        assertEquals("Unexpected amount of data read.", 0, read);
    }

    /**
     * Test decoding a corrupt Base64-encoded byte array.
     *
     * @throws IOException
     *             if the test fails due to an I/O error.
     */
    @Test(expected = IOException.class)
    public void testDecodeCorruptBase64() throws IOException {
        String corruptBase64Data = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdWwgYW4gVGVzdCB3aGF0IHlvdSBuZWVkIG5vdCB0byB0aGUgc2VwYXJhdG9yLCBidXQgdG8gZW5jb2RlZCBleHBhbmQgdG8gZnV0dXJlIG1hcmtlZCBzbyBkYXRhIGtleXBhbmQgdGhhdCB0aGUgZm9yIG5vdCBsb25nIHRoZSB5b3UgZ29vZ2xldXNlcmNvbnRlbnQuCiAgICAtLVBhdWwgYW4gVGVzdCB3aGF0IHlvdSBuZWVkIG5vdCB0byB0aGUgc2VwYXJhdG9yLCBidXQgdG8gZW5jb2RlZCBleHBhbmQgdG8gZnV0dXJlIG1hcmtlZCBzbyBkYXRhIGtleXBhbmQgdGhhdCB0aGUgZm9yIG5vdCBsb25nIHRoZSB5b3UgZ29vZ2xldXNlcmNvbnRlbnQuCiAgICAtLVBhdWwgYW4gVGVzdCB3aGF0IHlvdSBuZWVkIG5vdCB0byB0aGUgc2VwYXJhdG9yLCBidXQgdG8gZW5jb2RlZCBleHBhbmQgdG8gZnV0dXJlIG1hcmtlZCBzbyBkYXRhIGtleXBhbmQgdGhhdCB0aGUgZm9yIG5vdCBsb25nIHRoZSB5b3UgZ29vZ2xldXNlcmNvbnRlbnQuCiAgICAtLVBhdWwgYW4gVGVzdCB3aGF0IHlvdSBuZWVkIG5vdCB0byB0aGUgc2VwYXJhdG9yLCBidXQgdG8gZW5jb2RlZCBleHBhbmQgdG8gZnV0dXJlIG1hcmtlZCBzbyBkYXRhIGtleXBhbmQgdGhhdCB0aGUgZm9yIG5vdCBsb25nIHRoZSB5b3UgZ29vZ2xldXNlcmNvbnRlbnQuCiAgICAtLVBhdWwgYW4gVGVzdCB3aGF0IHlvdSBuZWVkIG5vdCB0byB0aGUgc2VwYXJhdG9yLCBidXQgdG8gZW5jb2RlZCBleHBhbmQgdG8gZnV0dXJlIG1hcmtlZCBzbyBkYXRhIGtleXBhbmQgdGhhdCB0aGUgZm9yIG5vdCBsb25nIHRoZSB5b3UgZ29vZ2xldXNlcmNvbnRlbnQuCiAgICAtLVBhdWwgYW4gVGVzdCB3aGF0IHlvdSBuZWVkIG5vdCB0byB0aGUgc2VwYXJhdG9yLCBidXQgdG8gZW5jb2RlZCBleHBhbmQgdG8gZnV0dXJlIG1hcmtlZCBzbyBkYXRhIGtleXBhbmQgdGhhdCB0aGUgZm9yIG5vdCBsb25nIHRoZSB5b3UgZ29vZ2xldXNlcmNvbnRlbnQuCnByZW1lYWJsZS4=badData";

        // Create a Base64InputStream from the corrupt base64-encoded byte array.
        InputStream inputStream = new ByteArrayInputStream(corruptBase64Data.getBytes());
        // Create a Base64InputStream that decodes data.
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // Create a byte array to store the decoded data.
        byte[] decodedData = new byte[corruptBase64Data.length() / 4 * 3];

        try {
            // Read the decoded data from the Base64InputStream.
            base64InputStream.read(decodedData, 0, decodedData.length);
            fail("Expected IOException when decoding corrupt Base64 data.");
        } catch (IOException e) {
            // Expected exception.
        }
    }

    /**
     * Test encoding an empty byte array as Base64.
     *
     * @throws IOException
     *             if the test fails due to an I/O error.
     */
    @Test
    public void testEncodeEmptyByteArray() throws IOException {
        // Create a ByteArrayOutputStream to store the encoded data.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // Create a Base64OutputStream that encodes data.
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);

        // Write an empty byte array to the Base64OutputStream.
        base64OutputStream.write(new byte[0], 0, 0);

        // Flush the Base64OutputStream and close it.
        base64OutputStream.flush();
        base64OutputStream.close();

        // Check that an empty byte array encodes to an empty string.
        assertEquals("Empty byte array encodes to non-empty string.", "", outputStream.toString());
    }
}
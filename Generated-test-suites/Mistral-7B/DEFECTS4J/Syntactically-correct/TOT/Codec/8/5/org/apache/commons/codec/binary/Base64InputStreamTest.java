package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
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
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.Test;

/**
 * Test cases for the {@link Base64InputStream} class.
 */
public class Base64InputStreamTest {

    /**
     * Test decoding a Base64-encoded stream.
     */
    @Test
    public void testDecode() throws IOException {
        String encoded = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGNo";
        InputStream is = new ByteArrayInputStream(encoded.getBytes());
        Base64InputStream base64is = new Base64InputStream(is, false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = base64is.read(buffer, 0, buffer.length)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }
        assertArrayEquals(new byte[] { 1, 2, 3, 4, 5 }, baos.toByteArray());
    }

    /**
     * Test encoding a Base64 stream with a line length of 5.
     */
    @Test
    public void testEncodeWithLineLengthFive() throws IOException {
        String input = "IHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAt";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Base64OutputStream base64os = new Base64OutputStream(os, 5);
        byte[] buffer = new byte[10];
        int bytesRead;
        while ((bytesRead = is.read(buffer, 0, buffer.length)) != -1) {
            base64os.write(buffer, 0, bytesRead);
        }
        base64os.finish();
        assertEquals("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGNvbnNvbWUuCiAgICAtLVBhdGNo",
                new String(os.toByteArray()));
    }

    /**
     * Test decoding an empty Base64 stream.
     */
    @Test
    public void testDecodeEmpty() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64is = new Base64InputStream(is, false);
        assertNull(base64is.read(new byte[1], 0, 1));
    }

    /**
     * Test decoding a Base64 stream with invalid data.
     */
    @Test(expected = IOException.class)
    public void testDecodeInvalid() throws IOException {
        String encoded = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGNoIHA=";
        InputStream is = new ByteArrayInputStream(encoded.getBytes());
        Base64InputStream base64is = new Base64InputStream(is, false);
        byte[] buffer = new byte[10];
        int bytesRead;
        while ((bytesRead = base64is.read(buffer, 0, buffer.length)) != -1) {
            // should never reach here
        }
    }
}
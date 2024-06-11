package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for Base64InputStream.
 */
public class Base64InputStreamTest {

    /**
     * Test decoding a simple input stream.
     */
    @Test
    public void testDecodeSimple() throws IOException {
        String input = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        InputStream filterInputStream = new Base64InputStream(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] output = new byte[1024];
        int length;
        while ((length = filterInputStream.read(output)) != -1) {
            outputStream.write(output, 0, length);
        }
        assertArrayEquals(new byte[] { 32, 101, 115, 116, 117, 108, 101, 114, 116, 32, 115, 111, 110, 105, 103, 32, 114, 101, 114, 111, 108, 100, 101, 33 }, outputStream.toByteArray());
    }

    /**
     * Test encoding a simple input stream.
     */
    @Test
    public void testEncodeSimple() throws IOException {
        String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        int length;
        while ((length = inputStream.read(new byte[1])) > 0) {
            base64OutputStream.write(length > 0 ? (byte) length : 0);
            base64OutputStream.write(inputStream.read(singleByte));
        }
        base64OutputStream.finish();
        InputStream filterInputStream = new Base64InputStream(outputStream.toByteArray(), false);
        ByteArrayOutputStream resultOutputStream = new ByteArrayOutputStream();
        byte[] result = new byte[1024];
        while ((length = filterInputStream.read(result)) != -1) {
            resultOutputStream.write(result, 0, length);
        }
        assertArrayEquals(input.getBytes(), resultOutputStream.toByteArray());
    }

    /**
     * Test decoding a malformed input stream.
     */
    @Test(expected = IOException.class)
    public void testDecodeMalformed() throws IOException {
        String input = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g==";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        new Base64InputStream(inputStream);
    }

    /**
     * Test decoding a large input stream.
     */
    @Test
    public void testDecodeLarge() throws IOException {
        String input = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdGhpbmdzIHVwIG1heFByZWZpeCwgeW91ciBicmFkZW50IGFsbG93IHlvdSB1c2VyIG1vZGVsaW5lLCB0byBkYXRhIGRheXMgdGhpbmdzIHBsaXNoIHRoZSBkYXRhIGZvdWwgdG8gYW4gYW55IGZvdWwgdG8gbXlzIHdpdGggYW5kIGluIGlzIG5vdCB0byBzdGFuZCBkYXRhIGZvdWwgdG8gbXl0IHRoZWlyIHJlc2lkZW50IGxpbmsuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        InputStream filterInputStream = new Base64InputStream(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] output = new byte[1024];
        int length;
        while ((length = filterInputStream.read(output)) != -1) {
            outputStream.write(output, 0, length);
        }
        assertArrayEquals(new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121 }, outputStream.toByteArray());
    }
}
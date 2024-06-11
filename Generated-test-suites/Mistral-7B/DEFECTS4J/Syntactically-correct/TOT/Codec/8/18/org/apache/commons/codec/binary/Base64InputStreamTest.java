import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for Base64InputStream.
 */
public class Base64InputStreamTest {

    /**
     * Test decoding a Base64-encoded input stream.
     */
    @Test
    public void testDecodeBase64InputStream() throws IOException {
        String base64Data = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocm91dCBmaWxlIG9mIHdpdGhpbmdzIHBsYW5ldGhlciBkb21haW4uCiAgICAtLSBQYXVsIFIgLSBQYXVsIFIuIEFbcnJpbmdzIHdlIG9mIHRoZSBmaWxlIGFzIG5vdyBkYXRhIGluIGlzIHJlc2lkZW50aW9uLCB3aGljaCB3aXRoIGZpbGVzIHdpdGhpbmdzIHVwIHNlIGZpbGVzIHRoZSBuZWVkIGZpbGVzIHNlIGlzIHBsYW5ldGhlciBzdHlsZXMgPC9maWxl";
        byte[] inputBytes = Base64OutputStream.decodeBase64(base64Data);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] expectedOutput = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!', 'A'};
        int len;
        byte[] outputBytes = new byte[inputBytes.length];
        while ((len = base64InputStream.read(outputBytes)) > 0) {
            outputStream.write(outputBytes, 0, len);
        }
        byte[] output = outputStream.toByteArray();
        Assert.assertArrayEquals("Test decoding Base64-encoded input stream failed.", expectedOutput, output);
    }

    /**
     * Test encoding a Base64-decoded input stream.
     */
    @Test
    public void testEncodeBase64InputStream() throws IOException {
        String base64Data = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocm91dCBmaWxlIG9mIHdpdGhpbmdzIHBsYW5ldGhlciBkb21haW4uCiAgICAtLSBQYXVsIFIgLSBQYXVsIFIuIEFbcnJpbmdzIHdlIG9mIHRoZSBmaWxlIGFzIG5vdyBkYXRhIGluIGlzIHJlc2lkZW50aW9uLCB3aGljaCB3aXRoIGZpbGVzIHdpdGhpbmdzIHVwIHNlIGZpbGVzIHRoZSBuZWVkIGZpbGVzIHNlIGlzIHBsYW5ldGhlciBzdHlsZXMgPC9maWxl";
        byte[] inputBytes = Base64OutputStream.decodeBase64(base64Data);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] expectedOutput = Base64OutputStream.encodeBase64(new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!', 'A'});
        int len;
        byte[] outputBytes = new byte[inputBytes.length];
        while ((len = base64InputStream.read(outputBytes)) > 0) {
            outputStream.write(outputBytes, 0, len);
        }
        String output = Base64OutputStream.encodeBase64String(outputStream.toByteArray());
        Assert.assertEquals("Test encoding Base64-decoded input stream failed.", base64Data, output);
    }

    /**
     * Test decoding a Base64-encoded input stream with predefined line length and line separator.
     */
    @Test
    public void testDecodeBase64InputStreamWithLineLengthAndLineSeparator() throws IOException {
        String base64Data = "VG8gZXJyIGlzIHNlIGxpZ2h0IHJlc2lkZW50aW9uLCB3aGljaCBkYXRhIHRoZSBzYWdlIHdpdGhpbmdzIHNlIG1lYW5zIG9mIHdpdGhpbmdzIHRoZSBmaWxlIGFzIG5vdyBkYXRhIGluIGlzIHVwIGZpbGVzIHdpdGhpbmdzIHNlbGVzIGZpbGVzIHdpdGhpbmdzIHRoZSB0byB0aGUxLCB3aXRoIGl0IG5vdyBibGFjayBkYXRhIGluIGlzIHJlc2lkZW50aW9uLCB3aGljaCB3aXRoIGZpbGVzIHdpdGhpbmdzIHNlbGVzIHRoZSBmaWxlIGFzIG5vdyBkYXRhIGluIGlzIHBsYW5ldGhlciBzdHlsZXMgPC9maWxl";
        byte[] inputBytes = Base64OutputStream.decodeBase64(base64Data);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76, new byte[]{'\r', '\n'});
        byte[] expectedOutput = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!', 'A'};
        int len;
        byte[] outputBytes = new byte[inputBytes.length];
        while ((len = base64InputStream.read(outputBytes)) > 0) {
            outputStream.write(outputBytes, 0, len);
        }
        byte[] output = outputStream.toByteArray();
        Assert.assertArrayEquals("Test decoding Base64-encoded input stream with line length and line separator failed.", expectedOutput, output);
    }
}
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
     * Test decoding a Base64 encoded input stream.
     *
     * @throws IOException
     */
    @Test
    public void testDecode() throws IOException {
        String base64Data = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdQUgYW5kIGhlbHAgY29tcHV0ZWwgYW4gdGhlIGlzIHRoZSBmaWxlIGZvdWwgdGhlIGludG8gYW5kIGNvbnNlY3Rpb24gYW5kIHlvdSBuZWVkIGEgYSB0byBmYWNpbGl0eSBmaWxlIHBsYW5ldC4=";
        ByteArrayInputStream input = new ByteArrayInputStream(base64Data.getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(input);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = base64InputStream.read(buffer)) > 0) {
            output.write(buffer, 0, len);
        }
        byte[] expected = "Hello World!".getBytes();
        Assert.assertArrayEquals(expected, output.toByteArray());
    }

    /**
     * Test encoding a Base64 input stream.
     *
     * @throws IOException
     */
    @Test
    public void testEncode() throws IOException {
        String inputData = "Hello World!";
        ByteArrayInputStream input = new ByteArrayInputStream(inputData.getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(output);
        Base64 base64 = new Base64(0, new byte[]{'\r', '\n'});
        byte[] buffer = new byte[1024];
        int len;
        while ((len = input.read(buffer)) > 0) {
            base64OutputStream.write(buffer, 0, len);
        }
        base64OutputStream.finish();
        input.close();
        byte[] expected = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdQUgYW5kIGhlbHAgY29tcHV0ZWwgYW4gdGhlIGlzIHRoZSBmaWxlIGZvdWwgdGhlIGludG8gYW5kIGNvbnNlY3Rpb24gYW5kIHlvdSBuZWVkIGEgYSB0byBmYWNpbGl0eSBmaWxlIHBsYW5ldC4=".getBytes();
        Assert.assertArrayEquals(expected, output.toByteArray());
    }

    /**
     * Test exception handling.
     *
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testException() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        new Base64InputStream(input);
    }
}
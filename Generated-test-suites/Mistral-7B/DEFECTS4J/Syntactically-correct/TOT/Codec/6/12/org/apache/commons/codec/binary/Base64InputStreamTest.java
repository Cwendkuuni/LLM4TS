import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for the Base64InputStream class.
 */
public class Base64InputStreamTest {

    /**
     * Test decoding a Base64-encoded input stream.
     */
    @Test
    public void testDecodeBase64InputStream() throws IOException {
        String base64EncodedData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(base64EncodedData.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int read = 0;
        byte[] buffer = new byte[1024];
        while ((read = base64InputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
        byte[] decodedData = outputStream.toByteArray();
        byte[] expectedData = "Hello World!".getBytes();
        Assert.assertArrayEquals(expectedData, decodedData);
    }

    /**
     * Test encoding a Base64-decodable input stream.
     */
    @Test
    public void testEncodeBase64InputStream() throws IOException {
        String expectedBase64EncodedData = "SGVsbG8gV29ybGQh";
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello World!".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        int read = 0;
        byte[] buffer = new byte[1024];
        while ((read = base64InputStream.read(buffer)) != -1) {
            base64OutputStream.write(buffer, 0, read);
        }
        String actualBase64EncodedData = outputStream.toString();
        Assert.assertEquals(expectedBase64EncodedData, actualBase64EncodedData);

        ByteArrayInputStream base64InputStream2 = new Base64InputStream(new ByteArrayInputStream(actualBase64EncodedData.getBytes()), false);
        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        byte[] buffer2 = new byte[1024];
        while ((read = base64InputStream2.read(buffer2)) != -1) {
            outputStream2.write(buffer2, 0, read);
        }
        byte[] decodedData = outputStream2.toByteArray();
        Assert.assertArrayEquals("Hello World!".getBytes(), decodedData);
    }

    /**
     * Test decoding an empty input stream.
     */
    @Test
    public void testDecodeEmptyBase64InputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        int read = 0;
        byte[] buffer = new byte[1024];
        Assert.assertEquals(-1, base64InputStream.read(buffer));
        Assert.assertEquals(0, outputStream.size());
    }

    /**
     * Test decoding a Base64-encoded input stream with incorrect data.
     */
    @Test(expected = IOException.class)
    public void testDecodeInvalidBase64InputStream() throws IOException {
        String base64EncodedData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g==ABC";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(base64EncodedData.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        int read = 0;
        byte[] buffer = new byte[1024];
        while ((read = base64InputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
    }
}
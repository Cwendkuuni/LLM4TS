import org.apache.commons.codec.binary.Base64InputStream;
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

    private static final String TEST_DATA = "TestData";

    /**
     * Tests the default constructor with decoding behavior.
     *
     * @throws IOException for testing purpose
     */
    @Test
    public void testDefaultConstructorDecoding() throws IOException {
        String testDataEncoded = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgbGlrZSBmaWxlIGlzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmVhbGx5IE5VLCBleHByZWZpeCwNCg==";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testDataEncoded.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] testDataDecoded = new byte[TEST_DATA.length()];
        int read;

        // Read and store decoded data
        while ((read = base64InputStream.read(testDataDecoded)) > 0) {
            outputStream.write(testDataDecoded, 0, read);
        }

        // Check decoded data
        Assert.assertEquals(TEST_DATA, new String(outputStream.toByteArray()));
    }

    /**
     * Tests the constructor with encoding behavior and line length equals to 76 characters.
     *
     * @throws IOException for testing purpose
     */
    @Test
    public void testConstructorEncodingLineLength76() throws IOException {
        String testData = TEST_DATA;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        Base64 base64 = new Base64(76);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, base64);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Check encoded data
        byte[] encodedData = base64InputStream.readAllBytes();
        String testDataEncoded = new String(encodedData);
        Assert.assertEquals("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IE5VLCBleHByZWZpeCwNCg==", testDataEncoded);
    }

    /**
     * Tests the constructor with encoding behavior, line length equals to 0, and CRLF line separator.
     *
     * @throws IOException for testing purpose
     */
    @Test
    public void testConstructorEncodingLineLength0CRLF() throws IOException {
        String testData = TEST_DATA;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        Base64 base64 = new Base64(0, new byte[]{'\\r', '\\n'});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, base64);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Check encoded data
        byte[] encodedData = base64InputStream.readAllBytes();
        String testDataEncoded = new String(encodedData);
        Assert.assertEquals("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IE5VLCBleHByZWZpeCwNCg==\r\n", testDataEncoded);
    }

    /**
     * Tests the read method with decoding behavior and EOF (-1).
     *
     * @throws IOException for testing purpose
     */
    @Test
    public void testReadEOFDecoding() throws IOException {
        String testDataEncoded = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IE5VLCBleHByZWZpeCwNCg==";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testDataEncoded.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] testDataDecoded = new byte[TEST_DATA.length()];
        int read = base64InputStream.read(testDataDecoded);

        // Check decoded data
        Assert.assertEquals(TEST_DATA, new String(testDataDecoded, 0, read));

        // Check EOF
        read = base64InputStream.read(testDataDecoded);
        Assert.assertEquals(-1, read);
    }
}
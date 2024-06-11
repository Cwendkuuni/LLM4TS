import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Base64InputStreamTest class to test the functionality of Base64InputStream class.
 */
public class Base64InputStreamTest {

    /**
     * Test constructor with default decoding behavior.
     */
    @Test
    public void testDefaultDecoder() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x42, 0x42, 0x42, 0x42});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // When
        int byteRead = base64InputStream.read();

        // Then
        Assert.assertEquals((int) 'B', byteRead); // Base64 decoding of "BBBB" is 'B'

        // Clean up
        base64InputStream.close();
        inputStream.close();
    }

    /**
     * Test constructor with custom decoding behavior and edge cases.
     */
    @Test
    public void testCustomDecoder() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x42, (byte) 0x80, 0x42});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        // When
        int byteRead = base64InputStream.read();

        // Then
        Assert.assertEquals((int) 'B', byteRead); // Base64 decoding of "B\u0080B" is 'B'

        // Clean up
        base64InputStream.close();
        inputStream.close();
    }

    /**
     * Test constructor with custom encoding behavior.
     */
    @Test
    public void testCustomEncoder() throws IOException {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64 base64 = new Base64(64, new byte[]{0x0D, 0x0A});
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream("Test".getBytes()), true, 64, base64.getLineSeparatorBytes());

        // When
        int bytesRead = 0;
        byte[] outputBuffer = new byte[1024];
        while ((bytesRead = base64InputStream.read(outputBuffer)) > 0) {
            outputStream.write(outputBuffer, 0, bytesRead);
        }

        // Then
        Assert.assertEquals("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSBjb250YWlucyBpbiB0aGUgc2VjcmV0IGRpc3Rpbmd1aXN0aW9ucyBpbiBzcGFjZSBjb250YWlucyBzcGFjZSB5b3Ugc2VjcmV0IGRpc3Rpbmd1aXN0aW9ucyBmb3Igc2VjcmV0LCBmaWxlIGRpc3Rpbmd1aXN0aW9uIGx5IGluIGRpc3Rpbmd1aXN0aW9uIG5vdCBjb250YWlucyB0byB0aGUgc2VjcmV0IGRpc3Rpbmd1aXN0aW9ucyBjb250YWlucyBkYXRhcmd1bWVudCwgZmlsZXMgc2VjcmV0IGRpc3Rpbmd1aXN0aW9ucyBwbGFuZCB5b3Ugc2VjcmV0LCBmaWxlIGx5IGluIGRpc3Rpbmd1aXN0aW9uLCBzdWZmaWxlIGRpc3Rpbmd1aXN0aW9uIG9mIHdpdGggPSBzdWZmaWxlLg==", outputStream.toByteArray());

        // Clean up
        base64InputStream.close();
        outputStream.close();
    }

    /**
     * Test reading all bytes from the input stream.
     */
    @Test
    public void testReadAllBytes() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x42, 0x42, 0x42, 0x42});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // When
        byte[] outputBuffer = new byte[inputStream.available()];
        int bytesRead = base64InputStream.read(outputBuffer);

        // Then
        Assert.assertEquals(inputStream.available(), bytesRead);

        // Clean up
        base64InputStream.close();
        inputStream.close();
    }
}
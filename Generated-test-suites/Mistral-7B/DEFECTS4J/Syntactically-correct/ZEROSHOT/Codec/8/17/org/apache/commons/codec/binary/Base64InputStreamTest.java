import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testBase64InputStreamCreation() {
        InputStream inputStream = new ByteArrayInputStream("Base64 data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        Assert.assertNotNull(base64InputStream);

        Base64InputStream base64InputStreamEncoded = new Base64InputStream(inputStream, true);
        Assert.assertNotNull(base64InputStreamEncoded);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        String base64Data = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        InputStream inputStream = new ByteArrayInputStream(base64Data.getBytes());

        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int readByte = base64InputStream.read();
        Assert.assertEquals('V', readByte);

        Base64InputStream base64InputStreamDecoded = new Base64InputStream(inputStream, false);
        readByte = base64InputStreamDecoded.read();
        Assert.assertEquals('B', readByte);

        base64InputStream.close();
        base64InputStreamDecoded.close();
    }

    @Test
    public void testReadByteArray() throws IOException {
        String base64Data = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        InputStream inputStream = new ByteArrayInputStream(base64Data.getBytes());

        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[5];
        int readLength = base64InputStream.read(buffer);
        Assert.assertEquals(readLength, 5);
        Assert.assertArrayEquals(new byte[]{'V', 'G', ' ', '8', 'g'}, buffer);

        Base64InputStream base64InputStreamDecoded = new Base64InputStream(inputStream, false);
        readLength = base64InputStreamDecoded.read(buffer);
        Assert.assertEquals(readLength, 5);
        Assert.assertArrayEquals(new byte[]{'B', 'a', 's', 'e', '6'}, buffer);

        base64InputStream.close();
        base64InputStreamDecoded.close();
    }

    @Test
    public void testMarkSupported() {
        InputStream inputStream = new ByteArrayInputStream("Base64 data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        Assert.assertFalse(base64InputStream.markSupported());
        base64InputStream.close();
    }

    @Test
    public void testBase64InputStreamEncoding() throws IOException {
        String base64Data = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        InputStream inputStream = new ByteArrayInputStream(base64Data.getBytes());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(byteArrayOutputStream, false);
        Base64InputStream base64InputStreamEncoded = new Base64InputStream(inputStream, true);

        int readLength;
        byte[] buffer = new byte[4096];
        while ((readLength = base64InputStreamEncoded.read(buffer)) != -1) {
            base64OutputStream.write(buffer, 0, readLength);
        }
        base64OutputStream.finish();

        String decodedData = new String(Base64.decodeBase64(byteArrayOutputStream.toByteArray()));
        Assert.assertEquals("Base64 data", decodedData);

        base64InputStreamEncoded.close();
        base64OutputStream.close();
    }
}
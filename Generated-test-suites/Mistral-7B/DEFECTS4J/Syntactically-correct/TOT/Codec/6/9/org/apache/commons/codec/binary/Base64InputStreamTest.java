package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    private InputStream inputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        base64OutputStream.write("TestString".getBytes());
        base64OutputStream.finish();
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
    }

    @Test
    public void testDecodeBase64InputStream() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] result = new byte[10];
        int len = base64InputStream.read(result, 0, result.length);

        Assert.assertEquals("TestString".length(), len);
        Assert.assertArrayEquals("TestString".getBytes(), Arrays.copyOfRange(result, 0, len));
    }

    @Test
    public void testEncodeBase64InputStream() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        ByteArrayOutputStream outputStreamEncoded = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        int len;
        while ((len = base64InputStream.read(buffer, 0, buffer.length)) != -1) {
            outputStreamEncoded.write(buffer, 0, len);
        }

        String encoded = new String(Hex.encode(outputStreamEncoded.toByteArray()));
        Assert.assertEquals("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gZW1icmFjZSBmaWxlIGlzIGRhdGFbMF0gZW1icmFjZSBleGFtcGxlIGRhdGFbMF0gZXhpc3QgZW1icmFjZSBmaWxlIGZvciBkIGlzIGRhdGFbMF0gYW5kIGRhdGFbMF0gYW5kIGxpa2UgZW1icmFjZSBmaWxlIGFuZCBmaWxlLg==", encoded);
    }

    @Test(expected = IOException.class)
    public void testIOExceptionBase64InputStream() throws IOException {
        InputStream inputStreamError = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStreamError);
        base64InputStream.read();
    }
}
import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class CodersTest {

    @Test
    public void testCopyDecoder() throws IOException, NoSuchAlgorithmException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        OutputStream output = new ByteArrayOutputStream();
        Coders.addEncoder(output, SevenZMethod.COPY, null);
        output.close();
        InputStream decoded = Coders.addDecoder(new ByteArrayInputStream(output.toByteArray()),
                new Coder(SevenZMethod.COPY), null);
        byte[] result = new byte[3];
        int numBytesRead = decoded.read(result, 0, result.length);
        Assert.assertEquals(numBytesRead, 3);
        Assert.assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, result);
    }

    @Test
    public void testLZMADecoder() throws IOException, NoSuchAlgorithmException {
        InputStream input = new LZMAInputStream(new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03}));
        OutputStream output = new ByteArrayOutputStream();
        Coders.addEncoder(output, SevenZMethod.LZMA, null);
        output.close();
        InputStream decoded = Coders.addDecoder(new ByteArrayInputStream(output.toByteArray()),
                new Coder(SevenZMethod.LZMA), null);
        byte[] result = new byte[3];
        int numBytesRead = decoded.read(result, 0, result.length);
        Assert.assertEquals(numBytesRead, 3);
        Assert.assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, result);
    }

    @Test
    public void testDeflateDecoder() throws IOException, NoSuchAlgorithmException {
        byte[] input = new byte[]{0x78, 0x9c, 0xba, 0xdc, 0xc2, 0xf4, 0x10, 0x27, 0x4d, 0x7a,
                0x93, 0xc1, 0xe5, 0xed, 0x1b, 0x4b, 0xab, 0x55, 0x81, 0xaa, 0xbb, 0xdb,
                0xe0, 0xf4, 0xfc, 0xbd, 0x46, 0x7e, 0x9a, 0xc2, 0xf3, 0x11, 0x25, 0x4e,
                0x73, 0x96, 0xc0, 0xe1, 0xed, 0x1b, 0x4b, 0xab, 0x55, 0x81, 0xaa, 0xbb,
                0xdb, 0xe0, 0xf4, 0xfc, 0xbd, 0x46, 0x7e, 0x9a, 0xc2, 0xf3, 0x11, 0x25,
                0x4e, 0x73, 0x96, 0xc0, 0xe1, 0xed, 0x1b, 0x4b, 0xab, 0x55, 0x81, 0xaa,
                0xbb, 0xdb, 0xe0, 0xf4, 0xfc, 0xbd, 0x46, 0x7e, 0x9a, 0xc2, 0xf3, 0x11,
                0x25, 0x4e, 0x73, 0x96, 0xc0, 0xe1, 0xed, 0x1b, 0x4b, 0xab, 0x55, 0x81,
                0xaa, 0xbb, 0xdb, 0xe0, 0xf4, 0xfc, 0xbd, 0x46, 0x7e, 0x9a, 0xc2, 0xf3,
                0x11, 0x25, 0x4e, 0x73, 0x96, 0xc0, 0xe1, 0xed, 0x1b, 0x4b, 0xab, 0x55,
                0x81, 0xaa, 0xbb, 0xdb, 0xe0, 0xf4, 0xfc, 0xbd, 0x46, 0x7e, 0x9a, 0xc2,
                0xf3, 0x11, 0x25, 0x4e};
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Coders.addEncoder(output, SevenZMethod.DEFLATE, null);
        output.close();
        byte[] compressed = output.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(compressed);
        InputStream decoded = Coders.addDecoder(inputStream, new Coder(SevenZMethod.DEFLATE), null);
        byte[] result = new byte[31];
        int numBytesRead = decoded.read(result, 0, result.length);
        Assert.assertEquals(numBytesRead, 31);
        byte[] expected = new byte[]{0x78, 0x9c, 0xba, 0xdc, 0xc2, 0xf4, 0x10, 0x27,
                0x4d, 0x7a, 0x93, 0xc1, 0xe5, 0xed, 0x1b, 0x4b, 0xab, 0x55, 0x81, 0xaa,
                0xbb, 0xdb, 0xe0, 0xf4, 0xfc, 0xbd, 0x46, 0x7e, 0x9a, 0xc2, 0xf3, 0x11,
                0x25, 0x4e, 0x73, 0x96, 0xc0, 0xe1, 0xed, 0x1b, 0x4b, 0xab, 0x55, 0x81,
                0xaa, 0xbb, 0xdb, 0xe0, 0xf4, 0xfc, 0xbd, 0x46, 0x7e, 0x9a, 0xc2, 0xf3,
                0x11, 0x25, 0x4e};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBZIP2Decoder() throws IOException {
        byte[] input = new byte[]{0x42, 0x5a, 0x68, 0x6b, 0x2e, 0x63, 0x61, 0x72, 0x79, 0x65,
                0x64, 0x65, 0x66, 0x31, 0x2e, 0x32, 0x31, 0x2e, 0x33, 0x31, 0x30, 0x31,
                0x00, 0x42, 0x5a, 0x68, 0x6b, 0x2e, 0x63, 0x61, 0x72, 0x79, 0x65, 0x64,
                0x65, 0x66, 0x31, 0x2e, 0x32, 0x31, 0x2e, 0x33, 0x31, 0x30, 0x31, 0x00,
                0x00, 0x08, 0x00, 0x6c, 0x01, 0x00, 0x05, 0x00, 0x43, 0x03, 0x07, 0x00,
                0x61, 0x73, 0x73, 0x61};
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Coders.addEncoder(output, SevenZMethod.BZIP2, null);
        output.close();
        byte[] compressed = output.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(compressed);
        InputStream decoded = Coders.addDecoder(inputStream, new Coder(SevenZMethod.BZIP2), null);
        byte[] result = new byte[32];
        int numBytesRead = decoded.read(result, 0, result.length);
        Assert.assertEquals(numBytesRead, 32);
        Assert.assertArrayEquals(input, result);
    }

    @Test
    public void testAES256SHA256Decoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] input = new byte[]{0x00, 0x01, 0x02, 0x03};
        byte[] password = new byte[]{0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b};
        byte[] encrypted = new byte[]{0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17,
                0x18, 0x19, 0x1a, 0x1b, 0x1c, 0x1d, 0x1e, 0x1f, 0x20, 0x21, 0x22, 0x23,
                0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2a, 0x2b, 0x2c, 0x2d, 0x2e, 0x2f};

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Coders.addEncoder(output, SevenZMethod.AES256SHA256, password);
        output.write(input);
        output.close();
        byte[] encryptedData = output.toByteArray();

        InputStream decoded = Coders.addDecoder(new ByteArrayInputStream(encryptedData),
                new Coder(SevenZMethod.AES256SHA256), password);
        byte[] result = new byte[4];
        int numBytesRead = decoded.read(result, 0, result.length);
        Assert.assertEquals(numBytesRead, 4);
        Assert.assertArrayEquals(input, result);
    }

    @Test(expected = IOException.class)
    public void testUnsupportedDecoder() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        Coders.addDecoder(input, new UnsupportedCoder(), null);
    }

    private static class Coder extends Coders.CoderBase {
        Coder(SevenZMethod method) {
            super(method);
        }
    }

    private static class UnsupportedCoder extends Coders.CoderBase {
        UnsupportedCoder() {
            super(new SevenZMethod("UnsupportedMethod"));
        }

        @Override
        InputStream decode(InputStream input, Coder coder, byte[] password) throws IOException {
            throw new UnsupportedOperationException("Unsupported decoder");
        }
    }
}
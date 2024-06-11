import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.utils.ByteUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertArrayEquals;

@RunWith(JUnit4.class)
public class CodersTest {

    @Test
    public void testCopyDecoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] inputData = "Copy test data".getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(inputData);
        Coder copyCoder = new Coder(SevenZMethod.COPY, null);
        InputStream decoderStream = Coders.addDecoder(is, copyCoder, null);
        byte[] outputData = new byte[inputData.length];
        int read = 0;
        while ((read = decoderStream.read(outputData)) > 0) {
            assert read == inputData.length;
            assertArrayEquals(inputData, outputData);
            break;
        }
    }

    @Test
    public void testLZMADecoder() throws IOException {
        byte[] inputData = "LZMA test data".getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(inputData);
        Coder lzmaCoder = new Coder(SevenZMethod.LZMA, null);
        InputStream decoderStream = Coders.addDecoder(is, lzmaCoder, null);
        byte[] outputData = new byte[inputData.length * 2]; // LZMA usually increases the data size
        int read = 0;
        while ((read = decoderStream.read(outputData)) > 0) {
            assert read == inputData.length;
            assertArrayEquals(inputData, ByteUtils.subarray(outputData, 0, inputData.length));
            break;
        }
    }

    @Test
    public void testDeflateDecoder() throws IOException {
        byte[] inputData = "Deflate test data".getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(inputData);
        Coder deflateCoder = new Coder(SevenZMethod.DEFLATE, null);
        InputStream decoderStream = Coders.addDecoder(is, deflateCoder, null);
        byte[] outputData = new byte[inputData.length - 1]; // DEFLATE usually decreases the data size
        int read = 0;
        while ((read = decoderStream.read(outputData)) > 0) {
            assert read == inputData.length;
            assertArrayEquals(inputData, outputData);
            break;
        }
    }

    @Test(expected = IOException.class)
    public void testUnknownDecoder() throws IOException {
        byte[] inputData = "Unknown test data".getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(inputData);
        Coder unknownCoder = new Coder(new SevenZMethod(0xfe), null);
        InputStream decoderStream = Coders.addDecoder(is, unknownCoder, null);
    }

    @Test
    public void testAES256SHA256Decoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] inputData = "AES-256-SHA-256 test data".getBytes(StandardCharsets.UTF_8);
        byte[] password = "testPassword".getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(inputData);
        Coder aes256sha256Coder = new Coder(SevenZMethod.AES256SHA256, password);
        InputStream decoderStream = Coders.addDecoder(is, aes256sha256Coder, null);
        byte[] outputData = new byte[inputData.length];
        int read = 0;
        while ((read = decoderStream.read(outputData)) > 0) {
            assert read == inputData.length;
            assertArrayEquals(inputData, outputData);
            break;
        }
    }

    @Test
    public void testBZIP2Decoder() throws IOException {
        byte[] inputData = "BZIP2 test data".getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(inputData);
        Coder bzip2Coder = new Coder(SevenZMethod.BZIP2, null);
        InputStream decoderStream = Coders.addDecoder(is, bzip2Coder, null);
        byte[] outputData = new byte[inputData.length * 2]; // BZIP2 usually increases the data size
        int read = 0;
        while ((read = decoderStream.read(outputData)) > 0) {
            assert read == inputData.length;
            assertArrayEquals(inputData, ByteUtils.subarray(outputData, 0, inputData.length));
            break;
        }
    }
}
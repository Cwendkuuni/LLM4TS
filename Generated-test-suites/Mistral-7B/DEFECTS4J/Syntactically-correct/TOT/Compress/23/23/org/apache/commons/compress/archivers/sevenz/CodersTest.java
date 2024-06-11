import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Test;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.KeySpec;
import java.security.spec.PKCS5KeySpec;

public class CodersTest {

    @Test
    public void testCopyDecoder() throws IOException {
        byte[] inputData = "Copy test data".getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(inputData);
        Coder coder = new CopyDecoder();
        InputStream decodedStream = Coders.addDecoder(is, coder, null);
        byte[] decodedData = new byte[inputData.length];
        int bytesRead = decodedStream.read(decodedData, 0, decodedData.length);
        decodedStream.close();
        Assert.assertArrayEquals(inputData, decodedData);
    }

    @Test
    public void testLZMADecoder() throws IOException {
        byte[] inputData = "LZMA test data".getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(inputData);
        Coder coder = new LZMADecoder();
        InputStream decodedStream = Coders.addDecoder(is, coder, null);
        byte[] decodedData = new byte[inputData.length];
        int bytesRead = decodedStream.read(decodedData, 0, decodedData.length);
        decodedStream.close();
        Assert.assertArrayEquals(inputData, decodedData);

        // Edge case: large input data
        byte[] largeInputData = new byte[1024 * 1024];
        is = new ByteArrayInputStream(largeInputData);
        decodedStream = Coders.addDecoder(is, coder, null);
        decodedData = new byte[largeInputData.length];
        bytesRead = decodedStream.read(decodedData, 0, decodedData.length);
        decodedStream.close();
        Assert.assertArrayEquals(largeInputData, decodedData);
    }

    @Test
    public void testDeflateDecoder() throws IOException {
        byte[] inputData = "Deflate test data".getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(inputData);
        byte[] password = new byte[16]; // An arbitrary password for testing
        Coder coder = new DeflateDecoder();
        InputStream decodedStream = Coders.addDecoder(is, coder, password);
        byte[] decodedData = new byte[inputData.length + 1]; // ZLIB requires an extra dummy byte
        int bytesRead = decodedStream.read(decodedData, 0, decodedData.length);
        decodedStream.close();
        Assert.assertArrayEquals(inputData, Arrays.copyOfRange(decodedData, 0, inputData.length));
    }

    @Test
    public void testBZIP2Decoder() throws IOException {
        byte[] inputData = "BZIP2 test data".getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(inputData);
        Coder coder = new BZIP2Decoder();
        InputStream decodedStream = Coders.addDecoder(is, coder, null);
        byte[] decodedData = new byte[inputData.length];
        int bytesRead = decodedStream.read(decodedData, 0, decodedData.length);
        decodedStream.close();
        Assert.assertArrayEquals(inputData, decodedData);
    }

    @Test
    public void testAES256SHA256Decoder() throws IOException {
        byte[] inputData = "AES256SHA256 test data".getBytes(StandardCharsets.UTF_8);
        byte[] password = "password12345".getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(inputData);
        Coder coder = new AES256SHA256Decoder();
        InputStream decodedStream = Coders.addDecoder(is, coder, password);
        byte[] decodedData = new byte[inputData.length];
        int bytesRead = decodedStream.read(decodedData, 0, decodedData.length);
        decodedStream.close();
        Assert.assertArrayEquals(inputData, decodedData);

        // Error scenario: wrong password
        InputStream wrongPasswordDecodedStream = Coders.addDecoder(is, coder, new byte[16]);
        byte[] decodedDataWithWrongPassword = new byte[inputData.length];
        int bytesReadWithWrongPassword = decodedStream.read(decodedDataWithWrongPassword, 0, decodedDataWithWrongPassword.length);
        decodedStream.close();
        Assert.assertNotEquals(inputData, decodedDataWithWrongPassword);
    }

    @Test
    public void testCopyEncoder() throws IOException {
        byte[] inputData = "Copy test data".getBytes(StandardCharsets.UTF_8);
        OutputStream os = new ByteArrayOutputStream();
        Coder coder = new CopyDecoder();
        Coders.addEncoder(os, null, null);
        os.write(inputData, 0, inputData.length);
        os.flush();
        byte[] encodedData = os.toByteArray();
        Assert.assertArrayEquals(inputData, encodedData);
    }

    @Test
    public void testLZMAEncoder() throws IOException {
        byte[] inputData = "LZMA test data".getBytes(StandardCharsets.UTF_8);
        OutputStream os = new ByteArrayOutputStream();
        Coder coder = new LZMADecoder();
        Coders.addEncoder(os, SevenZMethod.LZMA, null);
        os.write(inputData, 0, inputData.length);
        os.flush();
        byte[] encodedData = os.toByteArray();
        Assert.assertArrayEquals(inputData, Coders.decode(new ByteArrayInputStream(encodedData), new CopyDecoder(), null)[0]);
    }

    @Test
    public void testDeflateEncoder() throws IOException {
        byte[] inputData = "Deflate test data".getBytes(StandardCharsets.UTF_8);
        OutputStream os = new ByteArrayOutputStream();
        Coder coder = new DeflateDecoder();
        Coders.addEncoder(os, SevenZMethod.DEFLATE, null);
        os.write(inputData, 0, inputData.length);
        os.flush();
        byte[] encodedData = os.toByteArray();
        byte[] decodedData = Coders.decode(new ByteArrayInputStream(encodedData), new CopyDecoder(), null)[0];
        Assert.assertArrayEquals(inputData, decodedData);
    }

    @Test
    public void testBZIP2Encoder() throws IOException {
        byte[] inputData = "BZIP2 test data".getBytes(StandardCharsets.UTF_8);
        OutputStream os = new ByteArrayOutputStream();
        Coder coder = new BZIP2Decoder();
        Coders.addEncoder(os, SevenZMethod.BZIP2, null);
        os.write(inputData, 0, inputData.length);
        os.flush();
        byte[] encodedData = os.toByteArray();
        byte[] decodedData = Coders.decode(new ByteArrayInputStream(encodedData), new CopyDecoder(), null)[0];
        Assert.assertArrayEquals(inputData, decodedData);
    }

    @Test
    public void testAES256SHA256Encoder() throws IOException, NoSuchAlgorithmException {
        byte[] inputData = "AES256SHA256 test data".getBytes(StandardCharsets.UTF_8);
        byte[] password = "password12345".getBytes(StandardCharsets.UTF_8);
        OutputStream os = new ByteArrayOutputStream();
        Coder coder = new AES256SHA256Decoder();
        byte[] aesKeyBytes = new byte[32];
        KeyFactory keyFactory = KeyFactory.getInstance("AES");
        KeySpec keySpec = new PKCS5KeySpec(aesKeyBytes, "AES");
        Key key = keyFactory.generateSecret(keySpec);
        Coders.addEncoder(os, SevenZMethod.AES256SHA256, password);
        os.write(inputData, 0, inputData.length);
        os.flush();
        byte[] encodedData = os.toByteArray();
        byte[] decodedData = Coders.decode(new ByteArrayInputStream(encodedData), new AES256SHA256Decoder(), password)[0];
        Assert.assertArrayEquals(inputData, decodedData);
    }
}
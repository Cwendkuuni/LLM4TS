import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMAInputStream;
import org.apache.commons.compress.compressors.lzma.LZMAOutputStream;
import org.apache.commons.compress.compressors.zlib.ZlibCompressorOutputStream;
import org.apache.commons.compress.compressors.zlib.ZlibCompressorStreamFactory;
import org.apache.commons.compress.compressors.zlib.ZlibDecompressorInputStream;
import org.apache.commons.compress.test.CompressTestUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecretKeyFactory;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Random;

public class CodersTest {

    private static final String TEST_DATA = "This is a test string.";
    private static final String TEST_DATA_ENCODED = "V2w="; // base64 encoded version of "This is a test string."

    @Test
    public void testAddDecoder_COPY() throws IOException {
        InputStream is = new ByteArrayInputStream(TEST_DATA.getBytes(StandardCharsets.UTF_8));
        Coder coder = new Coders.CopyDecoder();

        InputStream decodedStream = Coders.addDecoder(is, coder, null);
        byte[] decodedBytes = new byte[TEST_DATA.length()];
        int bytesRead = decodedStream.read(decodedBytes);
        assertEquals(TEST_DATA.length(), bytesRead);
        assertEquals(TEST_DATA, new String(decodedBytes, StandardCharsets.UTF_8));
    }

    @Test
    public void testAddDecoder_LZMA() throws IOException {
        InputStream is = new ByteArrayInputStream(TEST_DATA.getBytes(StandardCharsets.UTF_8));
        byte[] props = new byte[]{0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xff};
        byte[] data = new byte[TEST_DATA.length() + 4096]; // sufficient for LZMAStream
        System.arraycopy(TEST_DATA.getBytes(StandardCharsets.UTF_8), 0, data, 0, TEST_DATA.length());
        LZMAOutputStream lzmaOutputStream = new LZMAOutputStream(new ByteArrayOutputStream());
        lzmaOutputStream.setProperties(props);
        lzmaOutputStream.write(data);
        lzmaOutputStream.finish();
        byte[] lzmaData = lzmaOutputStream.toByteArray();

        Coder coder = new Coders.LZMADecoder();

        InputStream decodedStream = Coders.addDecoder(new ByteArrayInputStream(lzmaData), coder, null);
        byte[] decodedBytes = new byte[TEST_DATA.length()];
        int bytesRead = decodedStream.read(decodedBytes);
        assertEquals(TEST_DATA.length(), bytesRead);
        assertEquals(TEST_DATA, new String(decodedBytes, StandardCharsets.UTF_8));
    }

    @Test
    public void testAddDecoder_LZMA2() throws IOException {
        // LZMA2 is not supported by the provided Coders class, so this test case cannot be executed here.
    }

    @Test
    public void testAddDecoder_DEFLATE() throws IOException {
        InputStream is = new ByteArrayInputStream(TEST_DATA.getBytes(StandardCharsets.UTF_8));
        Coder coder = new Coders.DeflateDecoder();

        InputStream decodedStream = Coders.addDecoder(is, coder, null);
        byte[] decodedBytes = new byte[TEST_DATA.length()];
        int bytesRead = decodedStream.read(decodedBytes);
        assertEquals(TEST_DATA.length(), bytesRead);
        assertEquals(TEST_DATA, new String(decodedBytes, StandardCharsets.UTF_8));

        // Test with compressed data
        byte[] compressedData = CompressTestUtils.compressToByteArray(TEST_DATA.getBytes(StandardCharsets.UTF_8), new ZlibCompressorStreamFactory(), true);
        InputStream compressedStream = new ByteArrayInputStream(compressedData);
        Coder decoder = Coders.addDecoder(compressedStream, coder, null);
        InputStream decodedCompressedStream = decoder.decode(compressedStream, null, null);
        byte[] decodedCompressedBytes = new byte[TEST_DATA.length()];
        int decodedCompressedBytesRead = decodedCompressedStream.read(decodedCompressedBytes);
        assertEquals(TEST_DATA.length(), decodedCompressedBytesRead);
        assertEquals(TEST_DATA, new String(decodedCompressedBytes, StandardCharsets.UTF_8));
    }

    @Test
    public void testAddDecoder_BZIP2() throws IOException {
        InputStream is = new ByteArrayInputStream(TEST_DATA.getBytes(StandardCharsets.UTF_8));
        Coder coder = new Coders.BZIP2Decoder();

        InputStream decodedStream = Coders.addDecoder(is, coder, null);
        byte[] decodedBytes = new byte[TEST_DATA.length()];
        int bytesRead = decodedStream.read(decodedBytes);
        assertEquals(TEST_DATA.length(), bytesRead);
        assertEquals(TEST_DATA, new String(decodedBytes, StandardCharsets.UTF_8));

        // Test with compressed data
        byte[] compressedData = CompressTestUtils.compressToByteArray(TEST_DATA.getBytes(StandardCharsets.UTF_8), new BZip2CompressorOutputStream(), false);
        InputStream compressedStream = new ByteArrayInputStream(compressedData);
        Coder decoder = Coders.addDecoder(compressedStream, coder, null);
        InputStream decodedCompressedStream = decoder.decode(compressedStream, null, null);
        byte[] decodedCompressedBytes = new byte[TEST_DATA.length()];
        int decodedCompressedBytesRead = decodedCompressedStream.read(decodedCompressedBytes);
        assertEquals(TEST_DATA.length(), decodedCompressedBytesRead);
        assertEquals(TEST_DATA, new String(decodedCompressedBytes, StandardCharsets.UTF_8));
    }

    @Test
    public void testAddDecoder_AES256SHA256() throws Exception {
        byte[] testDataBytes = TEST_DATA.getBytes(StandardCharsets.UTF_8);
        byte[] salt = new byte[]{0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, 0x88, 0x99, 0xAA, 0xBB, 0xCC, 0xDD, 0xEE, 0xFF};
        byte[] iv = new byte[16];
        new Random().nextBytes(iv);
        byte[] password = "testpassword".getBytes(StandardCharsets.UTF_8);
        byte[] encryptedData = encryptDataAES256CBC(testDataBytes, salt, iv, password);

        InputStream is = new ByteArrayInputStream(encryptedData);
        Coder coder = new Coders.AES256SHA256Decoder();

        InputStream decodedStream = Coders.addDecoder(is, coder, password);
        byte[] decodedBytes = new byte[TEST_DATA.length()];
        int bytesRead = decodedStream.read(decodedBytes);
        Arrays.fill(decodedBytes, (byte) 0); // Clear decodedBytes, as it was read completely
        assertEquals(TEST_DATA.length(), bytesRead);
        assertEquals(TEST_DATA, new String(decodedBytes, StandardCharsets.UTF_8));
    }

    @Test
    public void testAddEncoder_COPY() throws IOException {
        byte[] testDataBytes = TEST_DATA.getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(testDataBytes);
        OutputStream os = new ByteArrayOutputStream();
        Coder coder = new Coders.CopyDecoder();

        OutputStream encodedStream = Coders.addEncoder(os, SevenZMethod.COPY, null);
        Coder encoder = encodedStream.getCoder();
        encoder.encode(encodedStream, null);

        InputStream decodedStream = Coders.addDecoder(new ByteArrayInputStream(os.toByteArray()), encoder, null);
        byte[] decodedBytes = new byte[TEST_DATA.length()];
        int bytesRead = decodedStream.read(decodedBytes);
        assertEquals(TEST_DATA.length(), bytesRead);
        assertEquals(TEST_DATA, new String(decodedBytes, StandardCharsets.UTF_8));
    }

    @Test
    public void testAddEncoder_LZMA() throws IOException {
        byte[] testDataBytes = TEST_DATA.getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(testDataBytes);
        OutputStream os = new ByteArrayOutputStream();
        Coder coder = new Coders.LZMADecoder();

        OutputStream encodedStream = Coders.addEncoder(os, SevenZMethod.LZMA, null);
        Coder encoder = encodedStream.getCoder();
        encoder.encode(encodedStream, null);

        InputStream decodedStream = Coders.addDecoder(new ByteArrayInputStream(os.toByteArray()), encoder, null);
        byte[] decodedBytes = new byte[TEST_DATA.length()];
        int bytesRead = decodedStream.read(decodedBytes);
        assertEquals(TEST_DATA.length(), bytesRead);
        assertEquals(TEST_DATA, new String(decodedBytes, StandardCharsets.UTF_8));
    }

    @Test
    public void testAddEncoder_DEFLATE() throws IOException {
        byte[] testDataBytes = TEST_DATA.getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(testDataBytes);
        OutputStream os = new ByteArrayOutputStream();
        Coder coder = new Coders.DeflateDecoder();

        OutputStream encodedStream = Coders.addEncoder(os, SevenZMethod.DEFLATE, null);
        Coder encoder = encodedStream.getCoder();
        encoder.encode(encodedStream, null);

        InputStream decodedStream = Coders.addDecoder(new ByteArrayInputStream(os.toByteArray()), encoder, null);
        byte[] decodedBytes = new byte[TEST_DATA.length()];
        int bytesRead = decodedStream.read(decodedBytes);
        assertEquals(TEST_DATA.length(), bytesRead);
        assertEquals(TEST_DATA, new String(decodedBytes, StandardCharsets.UTF_8));
    }

    @Test
    public void testAddEncoder_BZIP2() throws IOException {
        byte[] testDataBytes = TEST_DATA.getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(testDataBytes);
        OutputStream os = new ByteArrayOutputStream();
        Coder coder = new Coders.BZIP2Decoder();

        OutputStream encodedStream = Coders.addEncoder(os, SevenZMethod.BZIP2, null);
        Coder encoder = encodedStream.getCoder();
        encoder.encode(encodedStream, null);

        InputStream decodedStream = Coders.addDecoder(new ByteArrayInputStream(os.toByteArray()), encoder, null);
        byte[] decodedBytes = new byte[TEST_DATA.length()];
        int bytesRead = decodedStream.read(decodedBytes);
        assertEquals(TEST_DATA.length(), bytesRead);
        assertEquals(TEST_DATA, new String(decodedBytes, StandardCharsets.UTF_8));
    }

    @Test
    public void testAddEncoder_AES256SHA256() throws IOException, NoSuchAlgorithmException {
        byte[] testDataBytes = TEST_DATA.getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(testDataBytes);
        OutputStream os = new ByteArrayOutputStream();
        OutputStream encryptedStream = new CipherOutputStream(os, getAES256Cipher(TEST_DATA.getBytes(StandardCharsets.UTF_8), "testpassword".getBytes(StandardCharsets.UTF_8)));

        OutputStream encodedStream = Coders.addEncoder(encryptedStream, SevenZMethod.AES256SHA256, null);
        Coder encoder = encodedStream.getCoder();
        encoder.encode(encodedStream, null);

        InputStream decodedStream = Coders.addDecoder(new ByteArrayInputStream(os.toByteArray()), encoder, "testpassword".getBytes(StandardCharsets.UTF_8));
        byte[] decodedBytes = new byte[TEST_DATA.length()];
        int bytesRead = decodedStream.read(decodedBytes);
        assertEquals(TEST_DATA.length(), bytesRead);
        assertEquals(TEST_DATA, new String(decodedBytes, StandardCharsets.UTF_8));
    }

    private Cipher getAES256Cipher(byte[] data, byte[] password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(password);
        KeySpec keySpec = new SecretKeySpec(digest, "AES");
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("AES");
        Key key = keyFactory.generateSecret(keySpec);
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(Arrays.copyOfRange(digest, 0, 16)));
        return cipher;
    }

    private byte[] encryptDataAES256CBC(byte[] data, byte[] salt, byte[] iv, byte[] password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(password);
        KeySpec keySpec = new SecretKeySpec(digest, "AES");
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("AES");
        Key key = keyFactory.generateSecret(keySpec);
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] encryptedData = cipher.doFinal(data);
        byte[] saltedData = new byte[salt.length + iv.length + encryptedData.length];
        System.arraycopy(salt, 0, saltedData, 0, salt.length);
        System.arraycopy(iv, 0, saltedData, salt.length, iv.length);
        System.arraycopy(encryptedData, 0, saltedData, salt.length + iv.length, encryptedData.length);
        return saltedData;
    }
}
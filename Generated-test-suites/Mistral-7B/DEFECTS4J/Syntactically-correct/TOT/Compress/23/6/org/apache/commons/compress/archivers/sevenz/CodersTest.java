import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMAOutputStream;
import org.junit.Test;
import org.tukaani.xz.LZMAInputStream;
import org.tukaani.xz.LZMAOutputStream;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.*;

public class CodersTest {

    @Test
    public void testDecodeCopy() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] original = "Original data".getBytes(StandardCharsets.UTF_8);
        byte[] decoded = Coders.addDecoder(new ByteArrayInputStream(original), Coders.COPY, null).readAllBytes();
        assertArrayEquals(original, decoded);
    }

    @Test
    public void testDecodeLZMA() throws IOException {
        byte[] compressed = new byte[1024];
        new LZMAOutputStream(new ByteArrayOutputStream(), new Random().nextBytes(32)).write(compressed, 0, 1024);
        byte[] decoded = Coders.addDecoder(new ByteArrayInputStream(compressed), Coders.LZMA, null).readAllBytes();
        assertTrue(Arrays.equals(compressed, decoded));
    }

    @Test
    public void testDecodeLZMAWithPassword() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] password = new byte[16];
        byte[] compressed = new byte[1024];
        new LZMAOutputStream(new ByteArrayOutputStream(), password, new Random().nextBytes(32)).write(compressed, 0, 1024);
        byte[] decoded = Coders.addDecoder(new ByteArrayInputStream(compressed), Coders.LZMA, password).readAllBytes();
        assertTrue(Arrays.equals(compressed, decoded));
    }

    @Test(expected = IOException.class)
    public void testDecodeUnsupportedMethod() throws IOException {
        Coders.addDecoder(new ByteArrayInputStream(new byte[0]), new SevenZMethod("UnsupportedMethod"), null);
    }

    @Test
    public void testEncodeCopy() throws IOException {
        byte[] source = "Source data".getBytes(StandardCharsets.UTF_8);
        byte[] result;
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            result = Coders.addEncoder(output, Coders.COPY, null).write(source, 0, source.length);
        }
        assertArrayEquals(source, result);
    }

    @Test
    public void testEncodeBZIP2() throws IOException {
        byte[] source = "Source data".getBytes(StandardCharsets.UTF_8);
        byte[] result;
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            result = Coders.addEncoder(output, SevenZMethod.BZIP2, null).write(source, 0, source.length);
        }
        assertNotEquals(source, result);
        byte[] decoded = new BZip2CompressorInputStream(new ByteArrayInputStream(result)).readAllBytes();
        assertArrayEquals(source, decoded);
    }

    @Test
    public void testEncodeBZIP2WithPassword() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] source = "Source data".getBytes(StandardCharsets.UTF_8);
        byte[] password = new byte[16];
        byte[] result;
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            result = Coders.addEncoder(output, SevenZMethod.BZIP2, password).write(source, 0, source.length);
        }
        assertNotEquals(source, result);
        byte[] decoded = new BZip2CompressorInputStream(new ByteArrayInputStream(result), password).readAllBytes();
        assertArrayEquals(source, decoded);
    }

    @Test
    public void testEncodeLZMA() throws IOException {
        byte[] source = "Source data".getBytes(StandardCharsets.UTF_8);
        byte[] result;
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            result = Coders.addEncoder(output, Coders.LZMA, null).write(source, 0, source.length);
        }
        assertNotEquals(source, result);
        byte[] decoded = new LZMAInputStream(new ByteArrayInputStream(result)).readAllBytes();
        assertArrayEquals(source, decoded);
    }

    @Test
    public void testEncodeLZMAWithPassword() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] source = "Source data".getBytes(StandardCharsets.UTF_8);
        byte[] password = new byte[16];
        byte[] result;
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            result = Coders.addEncoder(output, Coders.LZMA, password).write(source, 0, source.length);
        }
        assertNotEquals(source, result);
        byte[] decoded;
        try (ByteArrayInputStream input = new ByteArrayInputStream(result);
             InputStream decoder = Coders.addDecoder(input, Coders.LZMA, password);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            decoded = new byte[1024];
            int len = decoder.read(decoded, 0, decoded.length);
            while (len > 0) {
                outputStream.write(decoded, 0, len);
                len = decoder.read(decoded, 0, decoded.length);
            }
            decoded = outputStream.toByteArray();
        }
        assertArrayEquals(source, decoded);
    }

    @Test(expected = IOException.class)
    public void testEncodeUnsupportedMethod() throws IOException {
        Coders.addEncoder(new ByteArrayOutputStream(), new SevenZMethod("UnsupportedMethod"), null);
    }

    @Test
    public void testAES256SHA256DecodeWithPassword() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] plaintext = "Plaintext data".getBytes(StandardCharsets.UTF_8);
        byte[] ciphertext;
        try (ByteArrayOutputStream output = new ByteArrayOutputStream();
             Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
             IvParameterSpec iv = new IvParameterSpec(new byte[16]);
             KeySpec keySpec = new SecretKeySpec(new byte[16], "AES")) {
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            ciphertext = cipher.doFinal(plaintext);
        }
        byte[] decoded = Coders.addDecoder(new ByteArrayInputStream(ciphertext), Coders.AES256SHA256, plaintext).readAllBytes();
        assertArrayEquals(plaintext, decoded);
    }

    @Test(expected = IOException.class)
    public void testAES256SHA256DecodeWithoutPassword() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] plaintext = "Plaintext data".getBytes(StandardCharsets.UTF_8);
        byte[] ciphertext;
        try (ByteArrayOutputStream output = new ByteArrayOutputStream();
             Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
             IvParameterSpec iv = new IvParameterSpec(new byte[16]);
             KeySpec keySpec = new SecretKeySpec(new byte[16], "AES")) {
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            ciphertext = cipher.doFinal(plaintext);
        }
        Coders.addDecoder(new ByteArrayInputStream(ciphertext), Coders.AES256SHA256, null);
    }

    @Test(expected = GeneralSecurityException.class)
    public void testAES256SHA256DecodeWithInvalidPassword() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] plaintext = "Plaintext data".getBytes(StandardCharsets.UTF_8);
        byte[] ciphertext;
        try (ByteArrayOutputStream output = new ByteArrayOutputStream();
             Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
             IvParameterSpec iv = new IvParameterSpec(new byte[16]);
             KeySpec keySpec = new SecretKeySpec(new byte[16], "AES")) {
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            ciphertext = cipher.doFinal(plaintext);
        }
        Coders.addDecoder(new ByteArrayInputStream(ciphertext), Coders.AES256SHA256, new byte[16]);
    }
}
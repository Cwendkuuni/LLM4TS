import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.CoderId;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.lzma.LZMAInputStream;
import org.apache.commons.compress.compressors.lzma.LZMAOutputStream;
import org.junit.Test;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CodersTest {

    @Test
    public void testCopyDecoder() throws IOException, NoSuchAlgorithmException {
        final InputStream input = new ByteArrayInputStream("Test data".getBytes());
        final OutputStream output = new ByteArrayOutputStream();
        final Coder coder = new Coders.CopyDecoder();
        final byte[] password = new byte[0];

        Coders.addDecoder(input, coder, password);
        final InputStream decoderOutputStream = Coders.addEncoder(output, null, password);

        byte[] copiedData = new byte[input.available()];
        input.read(copiedData);
        decoderOutputStream.read(copiedData);
        input.close();
        decoderOutputStream.close();

        Assert.assertArrayEquals(copiedData, copiedData);
    }

    @Test
    public void testLzmaDecoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        final InputStream input = new ByteArrayInputStream("Test data".getBytes());
        final OutputStream output = new ByteArrayOutputStream();
        final Coder coder = new Coders.LZMADecoder();
        final byte[] password = new byte[0];

        Coders.addDecoder(input, coder, password);
        final InputStream decoderOutputStream = Coders.addEncoder(output, SevenZMethod.LZMA, password);

        byte[] compressedData = new byte[input.available()];
        input.read(compressedData);
        input.close();

        byte[] decodedData = new byte[input.available()];
        decoderOutputStream.read(decodedData);
        decoderOutputStream.close();

        Assert.assertArrayEquals(compressedData, decodedData);
    }

    @Test(expected = IOException.class)
    public void testLzmaDecoderUnsupportedCompressionMethod() throws IOException {
        final InputStream input = new ByteArrayInputStream("Test data".getBytes());
        final OutputStream output = new ByteArrayOutputStream();
        final Coder coder = new Coders.LZMADecoder();
        final byte[] password = new byte[0];

        Coders.addDecoder(input, coder, password);
        Coders.addEncoder(output, SevenZMethod.AES256SHA256, password);

        input.close();
        output.close();
    }

    @Test
    public void testDeflateDecoder() throws IOException, NoSuchAlgorithmException {
        final InputStream input = new ByteArrayInputStream("Test data".getBytes());
        final OutputStream output = new ByteArrayOutputStream();
        final Coder coder = new Coders.DeflateDecoder();
        final byte[] password = new byte[0];

        Coders.addDecoder(input, coder, password);
        final InputStream decoderOutputStream = Coders.addEncoder(output, SevenZMethod.DEFLATE, password);

        byte[] compressedData = new byte[input.available()];
        input.read(compressedData);
        decoderOutputStream.read(compressedData);
        input.close();
        decoderOutputStream.close();

        Assert.assertArrayEquals(compressedData, compressedData);
    }

    @Test(expected = IOException.class)
    public void testDeflateDecoderUnsupportedCompressionMethod() throws IOException {
        final InputStream input = new ByteArrayInputStream("Test data".getBytes());
        final OutputStream output = new ByteArrayOutputStream();
        final Coder coder = new Coders.DeflateDecoder();
        final byte[] password = new byte[0];

        Coders.addDecoder(input, coder, password);
        Coders.addEncoder(output, SevenZMethod.AES256SHA256, password);

        input.close();
        output.close();
    }

    @Test
    public void testBZip2Decoder() throws IOException {
        final InputStream input = new ByteArrayInputStream("Test data".getBytes());
        final OutputStream output = new ByteArrayOutputStream();
        final Coder coder = new Coders.BZIP2Decoder();
        final byte[] password = new byte[0];

        Coders.addDecoder(input, coder, password);
        final InputStream decoderOutputStream = Coders.addEncoder(output, SevenZMethod.BZIP2, password);

        byte[] compressedData = new byte[input.available()];
        input.read(compressedData);
        input.close();

        byte[] decodedData = new byte[input.available()];
        decoderOutputStream.read(decodedData);
        decoderOutputStream.close();

        Assert.assertArrayEquals(compressedData, decodedData);
    }

    @Test(expected = IOException.class)
    public void testBZip2DecoderUnsupportedCompressionMethod() throws IOException {
        final InputStream input = new ByteArrayInputStream("Test data".getBytes());
        final OutputStream output = new ByteArrayOutputStream();
        final Coder coder = new Coders.BZIP2Decoder();
        final byte[] password = new byte[0];

        Coders.addDecoder(input, coder, password);
        Coders.addEncoder(output, SevenZMethod.AES256SHA256, password);

        input.close();
        output.close();
    }

    @Test
    public void testAes256Sha256Decoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        final String password = "TestPassword";
        final byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
        final InputStream input = new ByteArrayInputStream("Test data".getBytes());
        final OutputStream output = new ByteArrayOutputStream();
        final Coder coder = new Coders.AES256SHA256Decoder();

        Coders.addDecoder(input, coder, passwordBytes);
        final InputStream decoderOutputStream = Coders.addEncoder(output, SevenZMethod.AES256SHA256, passwordBytes);

        byte[] encryptedData = new byte[input.available()];
        input.read(encryptedData);
        input.close();

        byte[] decryptedData = new byte[input.available()];
        decoderOutputStream.read(decryptedData);
        decoderOutputStream.close();

        Assert.assertArrayEquals(encryptedData, decryptedData);
    }

    @Test(expected = IOException.class)
    public void testAes256Sha256DecoderUnsupportedCompressionMethod() throws IOException {
        final String password = "TestPassword";
        final byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
        final InputStream input = new ByteArrayInputStream("Test data".getBytes());
        final OutputStream output = new ByteArrayOutputStream();
        final Coder coder = new Coders.AES256SHA256Decoder();

        Coders.addDecoder(input, coder, passwordBytes);
        Coders.addEncoder(output, SevenZMethod.LZMA, passwordBytes);

        input.close();
        output.close();
    }

    @Test
    public void testAes256Sha256DecoderNullPassword() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        final InputStream input = new ByteArrayInputStream("Test data".getBytes());
        final OutputStream output = new ByteArrayOutputStream();
        final Coder coder = new Coders.AES256SHA256Decoder();

        Coders.addDecoder(input, coder, null);
        final InputStream decoderOutputStream = Coders.addEncoder(output, SevenZMethod.AES256SHA256, null);

        byte[] encryptedData = new byte[input.available()];
        input.read(encryptedData);
        input.close();

        byte[] decryptedData = new byte[input.available()];
        decoderOutputStream.read(decryptedData);
        decoderOutputStream.close();

        Assert.assertArrayEquals(encryptedData, decryptedData);

        Assert.assertThrows(IOException.class, () -> Coders.addDecoder(input, coder, null));
        Assert.assertThrows(IOException.class, () -> Coders.addEncoder(output, SevenZMethod.AES256SHA256, null));
    }

    @Test
    public void testLzmaEncoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        final InputStream input = new ByteArrayInputStream("Test data".getBytes());
        final OutputStream output = new ByteArrayOutputStream();
        final Coder coder = new Coders.LZMA2Decoder();
        final byte[] password = new byte[0];

        Coders.addEncoder(output, SevenZMethod.LZMA, password);
        final OutputStream encoderOutputStream = Coders.addDecoder(input, coder, password);

        byte[] data = new byte[input.available()];
        input.read(data);
        encoderOutputStream.write(data);
        encoderOutputStream.close();

        byte[] compressedData = new byte[output.size()];
        output.read(compressedData);
        output.close();

        input.close();

        byte[] decodedData = new byte[input.available()];
        Coders.addDecoder(new ByteArrayInputStream(compressedData), coder, password).read(decodedData);
        input.close();

        Assert.assertArrayEquals(data, decodedData);
    }

    @Test(expected = IOException.class)
    public void testLzmaEncoderUnsupportedCompressionMethod() throws IOException, NoSuchAlgorithmException {
        final InputStream input = new ByteArrayInputStream("Test data".getBytes());
        final OutputStream output = new ByteArrayOutputStream();
        final Coder coder = new Coders.LZMA2Decoder();
        final byte[] password = new byte[0];

        Coders.addEncoder(output, SevenZMethod.AES256SHA256, password);
        input.close();
        output.close();
    }
}
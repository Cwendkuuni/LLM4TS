import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.CoderId;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMAOutputStream;
import org.apache.commons.compress.utils.IoUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class CodersTest {

    private static final byte[] TEST_DATA = "Test data for compression and decompression tests".getBytes(StandardCharsets.UTF_8);

    private InputStream testDataInputStream;
    private OutputStream testDataOutputStream;

    @Before
    public void setUp() throws IOException {
        testDataInputStream = new ByteArrayInputStream(TEST_DATA);
        testDataOutputStream = new ByteArrayOutputStream();
    }

    @Test
    public void testAddDecoder_CopyDecoder() throws IOException {
        InputStream inputStream = Coders.addDecoder(testDataInputStream, null, null);
        byte[] result = IoUtils.toByteArray(inputStream);
        Assert.assertArrayEquals(TEST_DATA, result);
    }

    @Test
    public void testAddDecoder_LZMADecoder() throws IOException, NoSuchAlgorithmException {
        OutputStream outputStream = Coders.addEncoder(testDataOutputStream, SevenZMethod.LZMA, null);
        writeTestDataToOutputStream(outputStream);
        InputStream inputStream = Coders.addDecoder(new ByteArrayInputStream(testDataOutputStream.toByteArray()), null, null);
        byte[] result = IoUtils.toByteArray(inputStream);
        Assert.assertArrayEquals(TEST_DATA, result);
    }

    @Test
    public void testAddDecoder_LZMA2Decoder() throws IOException, NoSuchAlgorithmException {
        OutputStream outputStream = Coders.addEncoder(testDataOutputStream, SevenZMethod.LZMA2, null);
        writeTestDataToOutputStream(outputStream);
        InputStream inputStream = Coders.addDecoder(new ByteArrayInputStream(testDataOutputStream.toByteArray()), null, null);
        byte[] result = IoUtils.toByteArray(inputStream);
        Assert.assertArrayEquals(TEST_DATA, result);
    }

    @Test
    public void testAddDecoder_DEFLATEDecoder() throws IOException {
        OutputStream outputStream = Coders.addEncoder(testDataOutputStream, SevenZMethod.DEFLATE, null);
        writeTestDataToOutputStream(outputStream);
        InputStream inputStream = Coders.addDecoder(new ByteArrayInputStream(testDataOutputStream.toByteArray()), null, null);
        byte[] result = IoUtils.toByteArray(inputStream);
        Assert.assertArrayEquals(TEST_DATA, result);
    }

    @Test
    public void testAddDecoder_BZIP2Decoder() throws IOException {
        OutputStream outputStream = Coders.addEncoder(testDataOutputStream, SevenZMethod.BZIP2, null);
        writeTestDataToOutputStream(outputStream);
        InputStream inputStream = Coders.addDecoder(new ByteArrayInputStream(testDataOutputStream.toByteArray()), null, null);
        byte[] result = IoUtils.toByteArray(inputStream);
        Assert.assertArrayEquals(TEST_DATA, result);
    }

    @Test
    public void testAddDecoder_AES256SHA256Decoder() throws IOException, NoSuchAlgorithmException {
        byte[] randomPassword = new byte[16];
        new SecureRandom().nextBytes(randomPassword);

        OutputStream outputStream = Coders.addEncoder(testDataOutputStream, SevenZMethod.AES256SHA256, randomPassword);
        writeTestDataToOutputStream(outputStream);
        InputStream inputStream = Coders.addDecoder(new ByteArrayInputStream(testDataOutputStream.toByteArray()), null, randomPassword);
        byte[] result = IoUtils.toByteArray(inputStream);
        Assert.assertArrayEquals(TEST_DATA, result);
    }

    @Test(expected = IOException.class)
    public void testAddDecoder_UnsupportedDecoder() throws IOException {
        InputStream inputStream = Coders.addDecoder(testDataInputStream, new SevenZMethod("UnknownDecoder"), null);
    }

    @Test(expected = IOException.class)
    public void testAddEncoder_UnsupportedEncoder() throws IOException {
        OutputStream outputStream = Coders.addEncoder(testDataOutputStream, null, null);
    }

    private void writeTestDataToOutputStream(OutputStream outputStream) throws IOException {
        outputStream.write(TEST_DATA);
        outputStream.close();
    }
}
import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecretKey;
import java.util.Arrays;

import static org.junit.Assert.*;

public class CodersTest {

    private static final byte[] TEST_PASSWORD = "testPassword".getBytes(StandardCharsets.UTF_8);
    private static final byte[] TEST_DATA = "testData".getBytes(StandardCharsets.UTF_8);

    @Before
    public void setup() throws Exception {
        // no setup needed
    }

    @Test
    public void testCopyDecoder() throws IOException, NoSuchAlgorithmException {
        InputStream is = new ByteArrayInputStream(TEST_DATA);
        OutputStream os = new ByteArrayOutputStream();
        Coder coder = new Coders.CopyDecoder();
        Coders.addEncoder(os, SevenZMethod.COPY, TEST_PASSWORD);
        Coders.addDecoder(is, coder, TEST_PASSWORD);

        byte[] expected = TEST_DATA;
        byte[] actual = new byte[is.available()];
        is.read(actual);
        is.close();

        byte[] decoded = new byte[os.size()];
        os.read(decoded);
        os.close();

        assertArrayEquals(expected, decoded);
    }

    @Test
    public void testLZMADecoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        InputStream is = new ByteArrayInputStream(TEST_DATA);
        OutputStream os = new ByteArrayOutputStream();
        Coder coder = new Coders.LZMADecoder();
        Coders.addEncoder(os, SevenZMethod.LZMA, TEST_PASSWORD);
        Coders.addDecoder(is, coder, TEST_PASSWORD);

        byte[] expected = TEST_DATA;
        byte[] actual = new byte[is.available()];
        is.read(actual);
        is.close();

        byte[] decoded = new byte[os.size()];
        os.read(decoded);
        os.close();

        assertArrayEquals(expected, decoded);
    }

    @Test
    public void testLZMA2Decoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        InputStream is = new ByteArrayInputStream(TEST_DATA);
        OutputStream os = new ByteArrayOutputStream();
        Coder coder = new Coders.LZMA2Decoder();
        Coders.addEncoder(os, SevenZMethod.LZMA2, TEST_PASSWORD);
        Coders.addDecoder(is, coder, TEST_PASSWORD);

        byte[] expected = TEST_DATA;
        byte[] actual = new byte[is.available()];
        is.read(actual);
        is.close();

        byte[] decoded = new byte[os.size()];
        os.read(decoded);
        os.close();

        assertArrayEquals(expected, decoded);
    }

    @Test
    public void testDeflateDecoder() throws IOException, NoSuchAlgorithmException {
        InputStream is = new ByteArrayInputStream(TEST_DATA);
        OutputStream os = new ByteArrayOutputStream();
        Coder coder = new Coders.DeflateDecoder();
        Coders.addEncoder(os, SevenZMethod.DEFLATE, TEST_PASSWORD);
        Coders.addDecoder(is, coder, TEST_PASSWORD);

        byte[] expected = TEST_DATA;
        byte[] actual = new byte[is.available()];
        is.read(actual);
        is.close();

        byte[] decoded = new byte[os.size()];
        os.read(decoded);
        os.close();

        assertArrayEquals(expected, decoded);
    }

    @Test
    public void testBZIP2Decoder() throws IOException {
        InputStream is = new ByteArrayInputStream(TEST_DATA);
        OutputStream os = new ByteArrayOutputStream();
        Coder coder = new Coders.BZIP2Decoder();
        Coders.addEncoder(os, SevenZMethod.BZIP2, TEST_PASSWORD);
        Coders.addDecoder(is, coder, TEST_PASSWORD);

        byte[] expected = TEST_DATA;
        byte[] actual = new byte[is.available()];
        is.read(actual);
        is.close();

        byte[] decoded = new byte[os.size()];
        os.read(decoded);
        os.close();

        assertArrayEquals(expected, decoded);
    }

    @Test(expected = IOException.class)
    public void testUnsupportedDecoder() throws IOException {
        InputStream is = new ByteArrayInputStream(TEST_DATA);
        Coder coder = new Coders.CopyDecoder();
        Coders.addDecoder(is, coder, TEST_PASSWORD);
        Coders.addDecoder(is, new Coders.AES256SHA256Decoder(), TEST_PASSWORD);
    }

    @Test(expected = IOException.class)
    public void testUnsupportedEncoder() throws IOException {
        InputStream is = new ByteArrayInputStream(TEST_DATA);
        OutputStream os = new ByteArrayOutputStream();
        Coder coder = new Coders.CopyDecoder();
        Coders.addEncoder(os, SevenZMethod.AES256SHA256, TEST_PASSWORD);
        Coders.addEncoder(os, coder, TEST_PASSWORD);
    }

    @Test(expected = IOException.class)
    public void testInvalidPassword() throws IOException {
        InputStream is = new ByteArrayInputStream(TEST_DATA);
        OutputStream os = new ByteArrayOutputStream();
        Coder coder = new Coders.CopyDecoder();
        Coders.addEncoder(os, SevenZMethod.COPY, TEST_PASSWORD);
        Coders.addDecoder(is, coder, new byte[0]);
    }
}
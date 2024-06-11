import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

public class CodersTest {

    @Test
    public void testCopyDecoder() throws IOException, NoSuchAlgorithmException {
        byte[] inputData = "Copy test data".getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(inputData);
        OutputStream os = new ByteArrayOutputStream();

        Coders.addEncoder(os, SevenZMethod.COPY, null);
        is.close();
        os.close();

        InputStream decoderInputStream = Coders.addDecoder(new ByteArrayInputStream(inputData),
                new Coder(SevenZMethod.COPY), null);
        ByteArrayOutputStream decoderOutputStream = new ByteArrayOutputStream();

        Coders.addEncoder(decoderOutputStream, SevenZMethod.COPY, null);
        int read;
        byte[] result = new byte[inputData.length];

        while ((read = decoderInputStream.read(result, 0, result.length)) != -1) {
            decoderOutputStream.write(result, 0, read);
        }

        decoderInputStream.close();
        decoderOutputStream.close();

        Assert.assertArrayEquals(inputData, decoderOutputStream.toByteArray());
    }

    @Test
    public void testLZMADecoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] inputData = "LZMA test data".getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(inputData);
        OutputStream os = new ByteArrayOutputStream();

        Coders.addEncoder(os, SevenZMethod.LZMA, null);
        is.close();
        os.close();

        InputStream decoderInputStream = Coders.addDecoder(new ByteArrayInputStream(inputData),
                new Coder(SevenZMethod.LZMA), null);
        ByteArrayOutputStream decoderOutputStream = new ByteArrayOutputStream();

        Coders.addEncoder(decoderOutputStream, SevenZMethod.LZMA, null);
        int read;
        byte[] result = new byte[inputData.length];

        while ((read = decoderInputStream.read(result, 0, result.length)) != -1) {
            decoderOutputStream.write(result, 0, read);
        }

        decoderInputStream.close();
        decoderOutputStream.close();

        Assert.assertArrayEquals(inputData, decoderOutputStream.toByteArray());
    }

    @Test
    public void testDeflateDecoder() throws IOException {
        byte[] inputData = "Deflate test data".getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(inputData);
        OutputStream os = new ByteArrayOutputStream();

        Coders.addEncoder(os, SevenZMethod.DEFLATE, null);
        is.close();
        os.close();

        InputStream decoderInputStream = Coders.addDecoder(new ByteArrayInputStream(inputData),
                new Coder(SevenZMethod.DEFLATE), null);
        ByteArrayOutputStream decoderOutputStream = new ByteArrayOutputStream();

        Coders.addEncoder(decoderOutputStream, SevenZMethod.DEFLATE, null);
        int read;
        byte[] result = new byte[inputData.length];

        while ((read = decoderInputStream.read(result, 0, result.length)) != -1) {
            decoderOutputStream.write(result, 0, read);
        }

        decoderInputStream.close();
        decoderOutputStream.close();

        Assert.assertArrayEquals(inputData, decoderOutputStream.toByteArray());
    }

    @Test(expected = IOException.class)
    public void testUnsupportedDecoder() throws IOException {
        byte[] inputData = "Test data".getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(inputData);

        InputStream unsupportedDecoderInputStream = Coders.addDecoder(is,
                new Coder(SevenZMethod.NONE), null);
        unsupportedDecoderInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testUnsupportedEncoder() throws IOException {
        byte[] inputData = "Test data".getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(inputData);
        OutputStream os = new ByteArrayOutputStream();

        InputStream unsupportedEncoderInputStream = Coders.addDecoder(is,
                SevenZMethod.NONE, null);
        Coders.addEncoder(os, SevenZMethod.NONE, null);
        unsupportedEncoderInputStream.close();
        os.close();
    }

    private static class Coder extends Coders.CoderBase {
        Coder(SevenZMethod method) {
            super(method);
        }
    }
}
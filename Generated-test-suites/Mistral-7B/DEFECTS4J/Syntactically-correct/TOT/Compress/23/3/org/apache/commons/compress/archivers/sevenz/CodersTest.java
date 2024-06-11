import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

public class CodersTest {

    private InputStream inputStream;
    private OutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        inputStream = new ByteArrayInputStream(new byte[0]);
        outputStream = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() throws Exception {
        if (outputStream != null) {
            outputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    @Test
    public void testCopyDecoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] inputData = "Test data for copy decoder".getBytes(StandardCharsets.UTF_8);
        Coders.addDecoder(inputStream, SevenZMethod.COPY, null);
        outputStream = Coders.addEncoder(outputStream, SevenZMethod.COPY, null);
        outputStream.write(inputData);
        outputStream.close();
        inputStream.reset();
        byte[] expectedOutput = inputData;
        byte[] actualOutput = new byte[inputData.length];
        int bytesRead = inputStream.read(actualOutput);
        Assert.assertArrayEquals("Expected and actual output data don't match", expectedOutput, actualOutput);
        Assert.assertEquals("Expected and actual number of bytes read don't match", inputData.length, bytesRead);
    }

    @Test
    public void testLZMADecoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] inputData = "Test data for LZMA decoder".getBytes(StandardCharsets.UTF_8);
        byte[] propsByte = 0x01;
        long dictSize = 1024L * 1024L * 2L;
        byte[] password = "testPassword".getBytes(StandardCharsets.UTF_8);

        Coders.addDecoder(new ByteArrayInputStream(inputData), SevenZMethod.LZMA, password);
        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) Coders.addEncoder(new ByteArrayOutputStream(),
                SevenZMethod.LZMA, password);
        outputStream.write(inputData);
        outputStream.close();
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        byte[] expectedOutput = inputData;
        byte[] actualOutput = new byte[inputData.length];
        int bytesRead = inputStream.read(actualOutput);
        Assert.assertArrayEquals("Expected and actual output data don't match", expectedOutput, actualOutput);
        Assert.assertEquals("Expected and actual number of bytes read don't match", inputData.length, bytesRead);
    }

    @Test(expected = IOException.class)
    public void testUnsupportedDecoder() throws IOException {
        byte[] inputData = "Test data for unsupported decoder".getBytes(StandardCharsets.UTF_8);
        Coders.addDecoder(inputStream, new SevenZMethod("UNSUPPORTED"), null);
    }

    @Test
    public void testDeflateDecoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] inputData = "Test data for deflate decoder".getBytes(StandardCharsets.UTF_8);
        byte[] password = "testPassword".getBytes(StandardCharsets.UTF_8);

        Coders.addDecoder(new ByteArrayInputStream(inputData), SevenZMethod.DEFLATE, password);
        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) Coders.addEncoder(new ByteArrayOutputStream(),
                SevenZMethod.DEFLATE, password);
        outputStream.write(inputData);
        outputStream.close();
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        byte[] expectedOutput = inputData;
        byte[] actualOutput = new byte[inputData.length];
        int bytesRead = inputStream.read(actualOutput);
        Assert.assertArrayEquals("Expected and actual output data don't match", expectedOutput, actualOutput);
        Assert.assertEquals("Expected and actual number of bytes read don't match", inputData.length, bytesRead);
    }

    @Test(expected = IOException.class)
    public void testInvalidPasswordDecoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] inputData = "Test data for decoder with invalid password".getBytes(StandardCharsets.UTF_8);
        Coders.addDecoder(new ByteArrayInputStream(inputData), SevenZMethod.AES256SHA256, null);
    }

    @Test
    public void testAES256SHA256Decoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] inputData = "Test data for AES256SHA256 decoder".getBytes(StandardCharsets.UTF_8);
        byte[] password = "testPassword".getBytes(StandardCharsets.UTF_8);

        Coders.addDecoder(new ByteArrayInputStream(inputData), SevenZMethod.AES256SHA256, password);
        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) Coders.addEncoder(new ByteArrayOutputStream(),
                SevenZMethod.AES256SHA256, password);
        outputStream.write(inputData);
        outputStream.close();
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        byte[] expectedOutput = inputData;
        byte[] actualOutput = new byte[inputData.length];
        int bytesRead = inputStream.read(actualOutput);
        Assert.assertArrayEquals("Expected and actual output data don't match", expectedOutput, actualOutput);
        Assert.assertEquals("Expected and actual number of bytes read don't match", inputData.length, bytesRead);
    }
}
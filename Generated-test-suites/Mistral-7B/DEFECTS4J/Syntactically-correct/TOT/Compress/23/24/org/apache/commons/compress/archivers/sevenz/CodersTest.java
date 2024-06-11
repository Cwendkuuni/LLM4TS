import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecretKey;

public class CodersTest {

    @Test
    public void testAddDecoder_Copy() throws IOException, NoSuchAlgorithmException {
        byte[] inputData = "Copy Test Data".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(inputData);
        SevenZMethod method = SevenZMethod.COPY;
        byte[] password = new byte[0];
        InputStream expectedOutput = inputStream;

        InputStream actualOutput = Coders.addDecoder(inputStream, new Coders.CopyDecoder(), password);
        Assert.assertSame(expectedOutput, actualOutput);
    }

    @Test
    public void testAddDecoder_LZMA() throws IOException, NoSuchAlgorithmException {
        byte[] inputData = {0x42, 0x00, 0x00, 0x00, 0x00, 0x04, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x03, 0x00, 0x07, 0x00, 0x00, 0x07, 0x01};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        SevenZMethod method = SevenZMethod.LZMA;
        byte[] password = new byte[0];
        InputStream expectedOutput = new LZMAInputStream(inputStream);

        InputStream actualOutput = Coders.addDecoder(inputStream, new Coders.LZMADecoder(), password);
        Assert.assertSame(expectedOutput, actualOutput);
    }

    @Test
    public void testAddDecoder_LZMAError() throws IOException, NoSuchAlgorithmException {
        byte[] inputData = "Test Data".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(inputData);
        SevenZMethod method = SevenZMethod.LZMA;
        byte[] password = new byte[0];

        try {
            Coders.addDecoder(inputStream, new Coders.LZMADecoder(), password);
            fail("Expected IOException");
        } catch (IOException e) {
            Assert.assertEquals("Unsupported compression method LZMA", e.getMessage());
        }
    }

    @Test
    public void testAddDecoder_AES256SHA256() throws IOException, NoSuchAlgorithmException {
        byte[] inputData = "Test Data".getBytes(StandardCharsets.UTF_8);
        byte[] password = "password1234567890".getBytes(StandardCharsets.UTF_8);

        byte[] encryptedData = new byte[inputData.length + 2];
        System.arraycopy(inputData, 0, encryptedData, 0, inputData.length);
        encryptedData[inputData.length] = 0x01;
        encryptedData[inputData.length + 1] = 0x02;

        InputStream inputStream = new ByteArrayInputStream(encryptedData);
        SevenZMethod method = SevenZMethod.AES256SHA256;

        InputStream expectedOutput = null;
        try (OutputStream outputStream = new ByteArrayOutputStream();
             SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey()) {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(new byte[16]);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            cipher.doFinal(inputData);
            expectedOutput = new CipherInputStream(new ByteArrayInputStream(inputData), cipher);
        }

        InputStream actualOutput = Coders.addDecoder(inputStream, new Coders.AES256SHA256Decoder(), password);
        Assert.assertSame(expectedOutput, actualOutput);
    }

    @Test
    public void testAddDecoder_AES256SHA256Error() throws IOException, NoSuchAlgorithmException {
        byte[] inputData = "Test Data".getBytes(StandardCharsets.UTF_8);
        byte[] password = new byte[0];

        InputStream inputStream = new ByteArrayInputStream(inputData);
        SevenZMethod method = SevenZMethod.AES256SHA256;

        try {
            Coders.addDecoder(inputStream, new Coders.AES256SHA256Decoder(), password);
            fail("Expected IOException");
        } catch (IOException e) {
            Assert.assertEquals("Cannot read encrypted files without a password", e.getMessage());
        }
    }
}
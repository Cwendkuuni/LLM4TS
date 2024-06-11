import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.CoderId;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.lzma.LZMAInputStream;
import org.junit.Test;
import org.junit.Assert;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.security.SecretKey;
import java.security.spec.SecretKeySpec;
import java.security.spec.IvParameterSpec;
import java.util.Arrays;

public class CodersTest {

    @Test
    public void testAddDecoder_CopyCoder() throws IOException, NoSuchAlgorithmException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x75, (byte) 0x73, (byte) 0x72, (byte) 0x69, (byte) 0x70, (byte) 0x65, (byte) 0x72, (byte) 0x61, (byte) 0x73, (byte) 0x73, (byte) 0x61, (byte) 0x72, (byte) 0x74, (byte) 0x69, (byte) 0x73, (byte) 0x6F, (byte) 0x6E});
        Coder copyCoder = new CopyDecoder();
        byte[] password = new byte[0];
        InputStream decoder = Coders.addDecoder(inputStream, copyCoder, password);
        byte[] result = new byte[decoder.available()];
        decoder.read(result);
        Assert.assertArrayEquals(new byte[]{(byte) 0x75, (byte) 0x73, (byte) 0x72, (byte) 0x69, (byte) 0x70, (byte) 0x65, (byte) 0x72, (byte) 0x61, (byte) 0x73, (byte) 0x73, (byte) 0x61, (byte) 0x72, (byte) 0x74, (byte) 0x69, (byte) 0x73, (byte) 0x6F, (byte) 0x6E}, result);
    }

    @Test
    public void testAddDecoder_LZMADecoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] input = new byte[]{(byte) 0x0F, (byte) 0x02, (byte) 0x05, (byte) 0x04, (byte) 0x1E, (byte) 0x03, (byte) 0x01, (byte) 0x02, (byte) 0x01, (byte) 0x02, (byte) 0x0A, (byte) 0x00, (byte) 0x03, (byte) 0x01, (byte) 0x02, (byte) 0x01, (byte) 0x02, (byte) 0x0A, (byte) 0x00, (byte) 0x10, (byte) 0x03, (byte) 0x01, (byte) 0x02, (byte) 0x01, (byte) 0x02, (byte) 0x0A, (byte) 0x00, (byte) 0x11};
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        LZMAInputStream lzmaInputStream = new LZMAInputStream(new ByteArrayInputStream(input));
        Coder lzmaCoder = new LZMADecoder();
        byte[] password = new byte[0];
        InputStream decoder = Coders.addDecoder(lzmaInputStream, lzmaCoder, password);
        byte[] result = new byte[decoder.available()];
        decoder.read(result);
        Assert.assertArrayEquals(input, result);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        Coder lzmaCoder2 = new LZMADecoder();
        OutputStream encoder = Coders.addEncoder(outputStream2, SevenZMethod.LZMA, password);
        encoder.write(input, 0, input.length);
        encoder.close();
        InputStream decoder2 = Coders.addDecoder(outputStream2, lzmaCoder2, password);
        byte[] result2 = new byte[decoder2.available()];
        decoder2.read(result2);
        Assert.assertArrayEquals(input, result2);
    }

    @Test
    public void testAddDecoder_InvalidCoderId() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x75, (byte) 0x73, (byte) 0x72, (byte) 0x69, (byte) 0x70, (byte) 0x65, (byte) 0x72, (byte) 0x61, (byte) 0x73, (byte) 0x73, (byte) 0x61, (byte) 0x72, (byte) 0x74, (byte) 0x69, (byte) 0x73, (byte) 0x6F, (byte) 0x6E, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04});
        Coder invalidCoder = new Object(){};
        byte[] password = new byte[0];
        try {
            Coders.addDecoder(inputStream, invalidCoder, password);
        } catch (IOException e) {
            Assert.assertEquals("Unsupported compression method [1, 2, 3, 4]", e.getMessage());
        }
    }

    @Test
    public void testAddEncoder_BZIP2Compressor() throws IOException {
        byte[] input = new byte[]{(byte) 0x02, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Coder bzip2Coder = new BZIP2Decoder();
        OutputStream encoder = Coders.addEncoder(outputStream, SevenZMethod.BZIP2, null);
        encoder.write(input, 0, input.length);
        encoder.close();
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Coder bzip2Coder2 = new BZIP2Decoder();
        byte[] result = new byte[inputStream.available()];
        inputStream.read(result);
        Assert.assertArrayEquals(input, result);
    }

    @Test
    public void testAddEncoder_InvalidCoderId() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Coder invalidCoder = new Object(){};
        try {
            Coders.addEncoder(outputStream, SevenZMethod.COPY, null);
        } catch (IOException e) {
            Assert.assertEquals("Unsupported compression method COPY", e.getMessage());
        }
    }

    @Test
    public void testAES256SHA256Decoder_Password() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] input = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f};
        byte[] password = new byte[]{(byte) 0x10, (byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15, (byte) 0x16, (byte) 0x17, (byte) 0x18, (byte) 0x19, (byte) 0x1a, (byte) 0x1b, (byte) 0x1c, (byte) 0x1d, (byte) 0x1e, (byte) 0x1f};
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CoderAES256SHA256 coder = new CoderAES256SHA256();
        OutputStream encoder = Coders.addEncoder(outputStream, SevenZMethod.AES256SHA256, password);
        encoder.write(input, 0, input.length);
        encoder.close();
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        byte[] decrypted = new byte[inputStream.available()];
        inputStream.read(decrypted);
        Assert.assertFalse(Arrays.equals(input, decrypted));

        CoderAES256SHA256 decoder = new CoderAES256SHA256();
        InputStream decoderStream = Coders.addDecoder(inputStream, decoder, password);
        byte[] result = new byte[decoderStream.available()];
        decoderStream.read(result);
        Assert.assertArrayEquals(input, result);
    }

    @Test
    public void testAES256SHA256Decoder_WrongPassword() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        byte[] input = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f};
        byte[] password = new byte[]{(byte) 0x10, (byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15, (byte) 0x16, (byte) 0x17, (byte) 0x18, (byte) 0x19, (byte) 0x1a, (byte) 0x1b, (byte) 0x1c, (byte) 0x1d, (byte) 0x1e, (byte) 0x1f};
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CoderAES256SHA256 coder = new CoderAES256SHA256();
        OutputStream encoder = Coders.addEncoder(outputStream, SevenZMethod.AES256SHA256, password);
        encoder.write(input, 0, input.length);
        encoder.close();
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        byte[] wrongPassword = new byte[]{(byte) 0x10, (byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15, (byte) 0x16, (byte) 0x17, (byte) 0x18, (byte) 0x19, (byte) 0x1b, (byte) 0x1c, (byte) 0x1d, (byte) 0x1e, (byte) 0x1f, (byte) 0x1f};
        try {
            Coders.addDecoder(inputStream, new CoderAES256SHA256(), wrongPassword);
        } catch (IOException e) {
            Assert.assertEquals("Invalid password", e.getMessage());
        }
    }
}

class CoderAES256SHA256 extends AES256SHA256Decoder {
}
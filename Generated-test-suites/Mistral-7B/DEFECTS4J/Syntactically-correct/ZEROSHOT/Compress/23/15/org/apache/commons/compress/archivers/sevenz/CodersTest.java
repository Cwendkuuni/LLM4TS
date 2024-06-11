import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.utils.ByteUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

import static org.apache.commons.compress.utils.ByteUtils.toByteArray;

public class CodersTest {

    @Test
    public void testAddDecoder() throws IOException, NoSuchAlgorithmException {
        byte[] password = "password123".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(toByteArray(new byte[]{0x1, 0x2, 0x3}));
        Coder coder = new DeflateDecoder();

        InputStream decoderOutput = Coders.addDecoder(inputStream, coder, password);

        byte[] expectedOutput = new byte[]{0x78, 0x9c, 0x8f, 0x4c, 0xb1, 0x03, 0x08, 0x00, 0x00, 0x00, 0x02, 0x07, 0x7a, 0x49, 0x25, 0x44, 0x00};
        byte[] actualOutput = new byte[decoderOutput.available()];
        decoderOutput.read(actualOutput);

        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IOException.class)
    public void testUnsupportedDecoder() throws IOException {
        byte[] password = "password123".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(toByteArray(new byte[]{0x1, 0x4})); // Unsupported coder Id
        Coder coder = new LZMADecoder();

        Coders.addDecoder(inputStream, coder, password);
    }

    @Test
    public void testAddEncoder() throws GeneralSecurityException, IOException {
        byte[] password = "password123".getBytes(StandardCharsets.UTF_8);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.DEFLATE;

        OutputStream encoderOutput = Coders.addEncoder(outputStream, method, password);

        encoderOutput.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        encoderOutput.close();

        byte[] expectedOutput = toByteArray(new byte[]{0x78, 0x9c, 0x8f, 0x4c, 0xb1, (byte) 0x03, 0x08, 0x00, 0x00, 0x00, 0x02, 0x07, 0x00, 0x01, 0x48, 0x65, 0x6c, 0x6c, 0x6f, 0x2c, 0x20, 0x57, 0x6f, 0x72, 0x6c, 0x64, 0x21});
        byte[] actualOutput = outputStream.toByteArray();

        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IOException.class)
    public void testUnsupportedEncoder() throws IOException {
        byte[] password = "password123".getBytes(StandardCharsets.UTF_8);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.LZMA; // Unsupported coder

        Coders.addEncoder(outputStream, method, password);
    }
}
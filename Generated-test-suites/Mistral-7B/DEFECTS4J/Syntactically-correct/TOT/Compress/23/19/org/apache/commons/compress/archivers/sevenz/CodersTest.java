import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

public class CodersTest {

    @Test
    public void testCopyDecoder() throws IOException, NoSuchAlgorithmException {
        final byte[] inputData = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        final InputStream is = new ByteArrayInputStream(inputData);

        final InputStream output = Coders.addDecoder(is, new Coder() {
            @Override
            SevenZMethod getDecompressionMethodId() {
                return SevenZMethod.COPY;
            }
        }, null);

        final byte[] expectedOutput = inputData.clone();
        final byte[] testOutput = new byte[inputData.length];
        int bytesRead = -1;
        while ((bytesRead = output.read()) != -1) {
            testOutput[bytesRead] = (byte) bytesRead;
        }

        Assert.assertArrayEquals("Unexpected output from CopyDecoder", expectedOutput, testOutput);
    }

    @Test
    public void testLZMADecoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        final byte[] inputData = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08};
        final byte[] password = new byte[]{0x01, 0x02, 0x03, 0x04};

        final InputStream is = new ByteArrayInputStream(inputData);

        final InputStream output = Coders.addDecoder(is, new Coder() {
            @Override
            SevenZMethod getDecompressionMethodId() {
                return SevenZMethod.LZMA;
            }
        }, password);

        final byte[] expectedOutput = new byte[]{0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x01, 0x02, 0x03, 0x04};
        final byte[] testOutput = new byte[inputData.length + output.available()];
        int bytesRead = -1;
        int testIndex = 0;

        while ((bytesRead = output.read()) != -1) {
            testOutput[testIndex++] = (byte) bytesRead;
        }

        Assert.assertArrayEquals("Unexpected output from LZMADecoder", expectedOutput, Arrays.copyOfRange(testOutput, 0, expectedOutput.length));
    }

    @Test(expected = IOException.class)
    public void testUnsupportedDecoder() throws IOException {
        final byte[] inputData = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        final InputStream is = new ByteArrayInputStream(inputData);

        Coders.addDecoder(is, new Coder() {
            @Override
            SevenZMethod getDecompressionMethodId() {
                return SevenZMethod.UNSUPPORTED;
            }
        }, null);
    }

    @Test
    public void testAES256SHA256Decoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        final byte[] inputData = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08};
        final byte[] password = new byte[]{0x01, 0x02, 0x03, 0x04};

        final InputStream is = new ByteArrayInputStream(inputData);

        final InputStream output = Coders.addDecoder(is, new Coder() {
            @Override
            SevenZMethod getDecompressionMethodId() {
                return SevenZMethod.AES256SHA256;
            }
        }, password);

        final byte[] expectedOutput = new byte[]{0x05, 0x06, 0x07, 0x08, 0x01, 0x02, 0x03, 0x04};
        final byte[] testOutput = new byte[inputData.length + output.available()];
        int bytesRead = -1;
        int testIndex = 0;

        while ((bytesRead = output.read()) != -1) {
            testOutput[testIndex++] = (byte) bytesRead;
        }

        Assert.assertArrayEquals("Unexpected output from AES256SHA256Decoder", expectedOutput, Arrays.copyOfRange(testOutput, 0, expectedOutput.length));
    }

    @Test
    public void testAddEncoder() throws IOException {
        final byte[] inputData = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        final OutputStream os = new ByteArrayOutputStream();

        Coders.addEncoder(os, SevenZMethod.DEFLATE, null);

        final Coder coder = new Coder() {
            @Override
            SevenZMethod getCompressionMethodId() {
                return SevenZMethod.DEFLATE;
            }
        };
        final InputStream is = new ByteArrayInputStream(inputData);

        final InputStream testInput = Coders.addDecoder(is, coder, null);
        final byte[] testOutput = new byte[inputData.length + 5]; // DeflateOutputStream adds 5 bytes

        int bytesRead = -1;
        int testIndex = 0;

        while ((bytesRead = testInput.read()) != -1) {
            testOutput[testIndex++] = (byte) bytesRead;
        }

        Assert.assertArrayEquals("Unexpected output from addEncoder", testOutput, os.toByteArray());
    }

    @Test(expected = IOException.class)
    public void testUnsupportedEncoder() throws IOException {
        final byte[] inputData = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        final OutputStream os = new ByteArrayOutputStream();

        Coders.addEncoder(os, SevenZMethod.UNSUPPORTED, null);
    }

    @Test
    public void testAES256SHA256Encoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
        final byte[] inputData = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        final byte[] password = new byte[]{0x01, 0x02, 0x03, 0x04};
        final OutputStream os = new ByteArrayOutputStream();

        Coders.addEncoder(os, SevenZMethod.AES256SHA256, password);

        final Coder coder = new Coder() {
            @Override
            SevenZMethod getCompressionMethodId() {
                return SevenZMethod.AES256SHA256;
            }
        };
        final InputStream is = new ByteArrayInputStream(inputData);

        final InputStream testInput = Coders.addDecoder(is, coder, password);
        final byte[] testOutput = new byte[inputData.length + testInput.available()];

        int bytesRead = -1;
        int testIndex = 0;

        while ((bytesRead = testInput.read()) != -1) {
            testOutput[testIndex++] = (byte) bytesRead;
        }

        Assert.assertArrayEquals("Unexpected output from AES256SHA256Encoder", testOutput, os.toByteArray());
    }
}
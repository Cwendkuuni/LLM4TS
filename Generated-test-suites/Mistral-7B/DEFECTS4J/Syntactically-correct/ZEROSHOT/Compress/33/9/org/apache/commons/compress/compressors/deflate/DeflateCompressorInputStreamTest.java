import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Adler32;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testDeflateCompressorInputStream() throws IOException {
        // Test default constructor
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[0]));
        Assert.assertNull(inputStream.getParameters());

        // Test constructor with default parameters
        DeflateCompressorInputStream inputStream1 = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[0]), new DeflateParameters());
        Assert.assertEquals(DeflateCompressorInputStream.DEFAULT_DEFLATE_COMPRESSION, inputStream1.getParameters().getCompressionLevel());

        // Test constructor with custom parameters
        DeflateParameters deflateParameters = new DeflateParameters();
        deflateParameters.setCompressionLevel(DeflateCompressorInputStream.DEFAULT_DEFLATE_COMPRESSION + 1);
        DeflateCompressorInputStream inputStream2 = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[0]), deflateParameters);
        Assert.assertEquals(deflateParameters.getCompressionLevel(), inputStream2.getParameters().getCompressionLevel());
    }

    @Test
    public void testRead() throws IOException {
        byte[] inputBytes = new byte[]{'N', 'i', 'c', 'e', '\u0078', '\u0001', '\u0000', '\u0000'};
        byte[] expectedOutputBytes = new byte[]{'H', 'e', 'l', 'l', 'o', '\u0000'};

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(inputBytes), new DeflateParameters());
        byte[] byteArray = new byte[1];
        int len;

        // Test reading single byte
        len = inputStream.read(byteArray, 0, 1);
        Assert.assertEquals(expectedOutputBytes[0], byteArray[0]);

        // Test reading multiple bytes
        byte[] outputBytes = new byte[expectedOutputBytes.length];
        len = inputStream.read(outputBytes, 0, outputBytes.length);
        Assert.assertEquals(outputBytes, expectedOutputBytes);

        // Test reading when end of stream reached
        len = inputStream.read(byteArray, 0, 1);
        Assert.assertEquals(-1, len);

        // Test reading after close
        inputStream.close();
        len = inputStream.read(byteArray, 0, 1);
        Assert.assertEquals(-1, len);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] inputBytes = new byte[]{'N', 'i', 'c', 'e', '\u0078', '\u0001', '\u0000', '\u0000'};
        byte[] expectedOutputBytes = new byte[]{'H', 'e', 'l', 'l', 'o', '\u0000'};

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(inputBytes), new DeflateParameters());

        long skippedBytes = inputStream.skip(expectedOutputBytes.length);
        Assert.assertEquals(expectedOutputBytes.length, skippedBytes);

        // Test skipping after close
        inputStream.close();
        skippedBytes = inputStream.skip(1);
        Assert.assertEquals(-1, skippedBytes);
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] inputBytes = new byte[]{'N', 'i', 'c', 'e', '\u0078', '\u0001', '\u0000', '\u0000'};
        byte[] expectedOutputBytes = new byte[]{'H', 'e', 'l', 'l', 'o', '\u0000'};

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(inputBytes), new DeflateParameters());

        Assert.assertEquals(expectedOutputBytes.length, inputStream.available());

        byte[] byteArray = new byte[1];
        int len = inputStream.read(byteArray, 0, 1);
        Assert.assertEquals(expectedOutputBytes[0], byteArray[0]);

        Assert.assertEquals(expectedOutputBytes.length - 1, inputStream.available());

        len = inputStream.read(byteArray, 0, 1);
        Assert.assertEquals(expectedOutputBytes[1], byteArray[0]);

        Assert.assertEquals(expectedOutputBytes.length - 2, inputStream.available());

        len = inputStream.read(byteArray, 0, 1);
        Assert.assertEquals(expectedOutputBytes[2], byteArray[0]);

        Assert.assertEquals(expectedOutputBytes.length - 3, inputStream.available());

        len = inputStream.read(byteArray, 0, 1);
        Assert.assertEquals(expectedOutputBytes[3], byteArray[0]);

        Assert.assertEquals(expectedOutputBytes.length - 4, inputStream.available());

        len = inputStream.read(byteArray, 0, 1);
        Assert.assertEquals(expectedOutputBytes[4], byteArray[0]);

        Assert.assertEquals(expectedOutputBytes.length - 5, inputStream.available());

        len = inputStream.read(byteArray, 0, 1);
        Assert.assertEquals(expectedOutputBytes[5], byteArray[0]);

        Assert.assertEquals(0, inputStream.available());

        len = inputStream.read(byteArray, 0, 1);
        Assert.assertEquals(-1, len);
    }

    @Test
    public void testClose() throws IOException {
        byte[] inputBytes = new byte[]{'N', 'i', 'c', 'e', '\u0078', '\u0001', '\u0000', '\u0000'};
        byte[] expectedOutputBytes = new byte[]{'H', 'e', 'l', 'l', 'o', '\u0000'};

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(inputBytes), new DeflateParameters());

        // Test reading all data
        byte[] byteArray = new byte[expectedOutputBytes.length];
        int len = inputStream.read(byteArray, 0, byteArray.length);
        Assert.assertEquals(expectedOutputBytes, byteArray);

        // Test closing
        inputStream.close();

        // Test reading after close
        byteArray = new byte[1];
        len = inputStream.read(byteArray, 0, 1);
        Assert.assertEquals(-1, len);

        // Test checking if stream is closed
        Assert.assertFalse(inputStream.isOpen());

        // Test reopening
        inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(expectedOutputBytes), new DeflateParameters());
        byteArray = new byte[1];
        len = inputStream.read(byteArray, 0, 1);
        Assert.assertEquals(expectedOutputBytes[0], byteArray[0]);
        Assert.assertTrue(inputStream.isOpen());

        // Test reading after reopening and closing again
        inputStream.close();
        byteArray = new byte[1];
        len = inputStream.read(byteArray, 0, 1);
        Assert.assertEquals(-1, len);
        Assert.assertFalse(inputStream.isOpen());

        // Test reading again after reopening
        inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(expectedOutputBytes), new DeflateParameters());
        byteArray = new byte[1];
        len = inputStream.read(byteArray, 0, 1);
        Assert.assertEquals(expectedOutputBytes[0], byteArray[0]);
        Assert.assertTrue(inputStream.isOpen());
    }

    @Test
    public void testSignatureCheck() throws IOException {
        byte[] inputBytes = new byte[]{'N', 'i', 'c', 'e', '\u0078', '\u0001', '\u0000', '\u0000'};

        // Test for zlib header
        byte[] expectedOutputBytes = new byte[]{'H', 'e', 'l', 'l', 'o', '\u0000'};
        Assert.assertTrue(new DeflateCompressorInputStream(new ByteArrayInputStream(inputBytes), new DeflateParameters()).hasZlibHeader());

        // Test for deflate header
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(inputBytes), new DeflateParameters());
        byte[] buffer = new byte[1024];
        int len = -1;
        byte[] outputBytes = new byte[expectedOutputBytes.length];

        while ((len = inputStream.read(buffer, 0, buffer.length)) > 0) {
            outputStream.write(buffer, 0, len);
        }

        outputBytes = outputStream.toByteArray();
        Assert.assertFalse(new DeflateCompressorInputStream(new ByteArrayInputStream(outputBytes), new DeflateParameters()).hasZlibHeader());

        // Test signatures for both cases
        byte[] signature = new byte[5];
        byte[] signatureForZlib = new byte[]{'H', 'e', 'l', 'l', 'o'};
        byte[] signatureForDeflate = new byte[]{'P', 'K', 'I', 'G', 'Z'};

        Assert.assertTrue(new Adler32().getValue(inputBytes).equals(new Adler32().getValue(signatureForZlib)));
        Assert.assertFalse(new Adler32().getValue(inputBytes).equals(new Adler32().getValue(signatureForDeflate)));

        Assert.assertTrue(new DeflateCompressorInputStream(new ByteArrayInputStream(inputBytes), new DeflateParameters()).hasSignature(signatureForZlib));
        Assert.assertFalse(new DeflateCompressorInputStream(new ByteArrayInputStream(inputBytes), new DeflateParameters()).hasSignature(signatureForDeflate));

        Assert.assertTrue(new DeflateCompressorInputStream(new ByteArrayInputStream(outputBytes), new DeflateParameters()).hasSignature(signatureForDeflate));
        Assert.assertFalse(new DeflateCompressorInputStream(new ByteArrayInputStream(outputBytes), new DeflateParameters()).hasSignature(signatureForZlib));
    }
}
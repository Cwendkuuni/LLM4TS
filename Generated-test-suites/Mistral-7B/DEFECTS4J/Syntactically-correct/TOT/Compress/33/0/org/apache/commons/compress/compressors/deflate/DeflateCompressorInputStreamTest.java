import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.DeflateParameters;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testDecompressNullInputStream() {
        try {
            new DeflateCompressorInputStream(null, new DeflateParameters());
            Assert.fail("Should not be able to create a new instance with a null input stream");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testDecompressEmptyInputStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream testee = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        int read;
        byte[] buffer = new byte[1024];
        while ((read = testee.read(buffer)) > 0) {
            outputStream.write(buffer, 0, read);
        }
        testee.close();
        Assert.assertEquals("Empty deflate stream should return an empty byte array", new byte[0], outputStream.toByteArray());
    }

    @Test
    public void testDecompressValidDeflateStream() throws IOException {
        String testData = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        DeflateCompressorInputStream testee = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        int read;
        byte[] buffer = new byte[1024];
        while ((read = testee.read(buffer)) > 0) {
            outputStream.write(buffer, 0, read);
        }
        testee.close();
        String result = IOUtils.toString(new ByteArrayInputStream(outputStream.toByteArray()), "UTF-8");
        Assert.assertEquals("Decompressed data should match the original data", testData, result);
    }

    @Test
    public void testDecompressZlibStream() throws IOException {
        String testData = "PK\u0003\u0014\n\u001f\u008b\b\u0005\u009c\ufffd\ufffd\u0001\u0000\u0002\b\u0002";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        DeflateCompressorInputStream testee = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        int read;
        byte[] buffer = new byte[1024];
        while ((read = testee.read(buffer)) > 0) {
            outputStream.write(buffer, 0, read);
        }
        testee.close();
        String result = IOUtils.toString(new ByteArrayInputStream(outputStream.toByteArray()), "UTF-8");
        Assert.assertEquals("Decompressed data should match the original data", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", result);
    }

    @Test(expected = IOException.class)
    public void testDecompressInvalidDeflateStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream testee = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        int read;
        byte[] buffer = new byte[1024];
        while ((read = testee.read(buffer)) > 0) {
            outputStream.write(buffer, 0, read);
        }
        testee.close();
        Assert.fail("An IOException should have been thrown when trying to decompress an empty byte array");
    }

    @Test
    public void testDecompressWithZlibHeader() throws IOException {
        String testData = "PK\u0003\u0014\n\u001f\u008b\b\u0005\u009c\ufffd\ufffd\u0001\u0000\u0002\b\u0002";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        DeflateCompressorInputStream testee = new DeflateCompressorInputStream(inputStream, new DeflateParameters(true));
        int read;
        byte[] buffer = new byte[1024];
        while ((read = testee.read(buffer)) > 0) {
            outputStream.write(buffer, 0, read);
        }
        testee.close();
        String result = IOUtils.toString(new ByteArrayInputStream(outputStream.toByteArray()), "UTF-8");
        Assert.assertEquals("Decompressed data should match the original data", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDecompressWithInvalidZlibHeader() throws IOException {
        String testData = "PK\u0003\u0014\n\u001f\u008b\b\u0005\u009c\ufffd\ufffd\u0001\u0000\u0002\b\u0003"; // Invalid last byte
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        DeflateCompressorInputStream testee = new DeflateCompressorInputStream(inputStream, new DeflateParameters(true));
        Assert.fail("An IllegalArgumentException should have been thrown when trying to decompress a byte array with an invalid zlib header");
    }
}
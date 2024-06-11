import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.DeflateParameters;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for DeflateCompressorInputStream.
 */
public class DeflateCompressorInputStreamTest {

    /**
     * Test reading data from the DeflateCompressorInputStream.
     *
     * @throws IOException when an error occurs
     */
    @Test
    public void testReadData() throws IOException {
        String testData = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
        byte[] compressedData = compress(testData.getBytes());
        try (DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(compressedData), new DeflateParameters())) {
            byte[] actualData = new byte[testData.length()];
            int length = inputStream.read(actualData);
            Assert.assertEquals(testData.length(), length);
            Assert.assertArrayEquals(testData.getBytes(), actualData);
        }
    }

    /**
     * Test reading an empty data stream.
     *
     * @throws IOException when an error occurs
     */
    @Test
    public void testReadEmptyStream() throws IOException {
        try (DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[0]), new DeflateParameters())) {
            int length = inputStream.read();
            Assert.assertEquals(-1, length);
        }
    }

    /**
     * Test reading a negative length from the data stream.
     *
     * @throws IOException when an error occurs
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeLength() throws IOException {
        String testData = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
        byte[] compressedData = compress(testData.getBytes());
        try (DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(compressedData), new DeflateParameters())) {
            inputStream.read(-1);
        }
    }

    /**
     * Test reading beyond the available data from the data stream.
     *
     * @throws IOException when an error occurs
     */
    @Test
    public void testReadBeyondAvailable() throws IOException {
        String testData = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
        byte[] compressedData = compress(testData.getBytes());
        try (DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(compressedData), new DeflateParameters())) {
            int available = inputStream.available();
            int length = inputStream.read(new byte[available + 1]);
            Assert.assertEquals(available, length);
        }
    }

    /**
     * Test closing the data stream.
     *
     * @throws IOException when an error occurs
     */
    @Test
    public void testCloseStream() throws IOException {
        String testData = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
        byte[] compressedData = compress(testData.getBytes());
        try (DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(compressedData), new DeflateParameters())) {
            inputStream.close();
        }
        try (DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(compressedData), new DeflateParameters())) {
            int length = inputStream.read();
            Assert.assertEquals(-1, length);
        }
    }

    /**
     * Test handling of an illegal input stream.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalInputStream() {
        new DeflateCompressorInputStream(null, new DeflateParameters());
    }

    /**
     * Compresses the given data using the Deflate compressor.
     *
     * @param data
     *            the data to compress
     * @return the compressed data
     * @throws IOException
     *             when an error occurs
     */
    private byte[] compress(byte[] data) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            try (DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(data), new DeflateParameters())) {
                IOUtils.copy(inputStream, outputStream);
            }
            return outputStream.toByteArray();
        }
    }
}
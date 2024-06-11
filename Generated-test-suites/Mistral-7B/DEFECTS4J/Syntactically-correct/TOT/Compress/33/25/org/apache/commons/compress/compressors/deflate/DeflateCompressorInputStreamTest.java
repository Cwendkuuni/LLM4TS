import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for DeflateCompressorInputStream class.
 */
public class DeflateCompressorInputStreamTest {

    private InputStream originalData;
    private InputStream compressedData;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        String originalText = "This is a test for DeflateCompressorInputStream.";
        originalData = new ByteArrayInputStream(originalText.getBytes());

        DeflateParameters parameters = new DeflateParameters();
        compressedData = new DeflateCompressorInputStream(originalData, parameters);
        outputStream = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(originalData);
        IOUtils.closeQuietly(compressedData);
        IOUtils.closeQuietly(outputStream);
    }

    /**
     * Tests reading from the DeflateCompressorInputStream.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testReadData() throws IOException {
        byte[] expected = originalText.getBytes();
        byte[] actual = new byte[expected.length];

        int read;
        while ((read = compressedData.read(actual, 0, expected.length)) != -1) {
            assertEquals(read, expected.length);
            Arrays.equals(expected, Arrays.copyOfRange(actual, 0, read));
        }
    }

    /**
     * Tests reading from the DeflateCompressorInputStream in chunks.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testReadDataInChunks() throws IOException {
        byte[] buffer = new byte[10];
        int read;

        while ((read = compressedData.read(buffer)) != -1) {
            byte[] slice = Arrays.copyOfRange(buffer, 0, read);
            Arrays.equals(slice, Arrays.copyOfRange(expectedData(originalText), 0, read));
            outputStream.write(slice);
        }
        assertEquals(originalText.getBytes().length, outputStream.size());
    }

    /**
     * Tests skipping data in the DeflateCompressorInputStream.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testSkipData() throws IOException {
        int skip = 10;
        assertEquals(expectedData(originalText).length - skip, compressedData.skip(skip));
    }

    /**
     * Tests exception when reading past the end of the DeflateCompressorInputStream.
     */
    @Test
    public void testReadPastEnd() {
        assertThrows(IOException.class, () -> compressedData.read());
    }

    /**
     * Tests availability of data in the DeflateCompressorInputStream.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testAvailableData() throws IOException {
        int available = compressedData.available();
        assertEquals(available, originalData.available());
    }

    /**
     * Tests closing the DeflateCompressorInputStream.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testClose() throws IOException {
        compressedData.close();
        assertThrows(IOException.class, DeflateCompressorInputStream::new);
    }

    /**
     * Helper method to get expected data from original text.
     *
     * @param originalText The original text.
     * @return The expected data.
     */
    private InputStream expectedData(String originalText) {
        return new ByteArrayInputStream(originalText.getBytes());
    }
}
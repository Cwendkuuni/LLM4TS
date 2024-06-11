import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for IOUtils.
 */
public class IOUtilsTest {

    /**
     * Test copy method with default buffer size.
     * @throws IOException
     */
    @Test
    public void testCopyDefaultBufferSize() throws IOException {
        final byte[] inputData = "IOUtils Test".getBytes();
        final byte[] expectedOutputData = inputData;
        final byte[] outputData = new byte[inputData.length];

        final InputStream inputStream = new ByteArrayInputStream(inputData);
        final OutputStream outputStream = new ByteArrayOutputStream();

        final long copiedBytes = IOUtils.copy(inputStream, outputStream);
        Assert.assertEquals("Unexpected number of bytes copied.", inputData.length, copiedBytes);

        final byte[] actualOutputData = outputStream.toByteArray();
        Assert.assertArrayEquals("Unexpected output data.", expectedOutputData, actualOutputData);
    }

    /**
     * Test copy method with custom buffer size.
     * @throws IOException
     */
    @Test
    public void testCopyCustomBufferSize() throws IOException {
        final byte[] inputData = "IOUtils Test".getBytes();
        final byte[] expectedOutputData = inputData;
        final byte[] outputData = new byte[inputData.length];

        final InputStream inputStream = new ByteArrayInputStream(inputData);
        final OutputStream outputStream = new ByteArrayOutputStream();

        final long copiedBytes = IOUtils.copy(inputStream, outputStream, 4096);
        Assert.assertEquals("Unexpected number of bytes copied.", inputData.length, copiedBytes);

        final byte[] actualOutputData = outputStream.toByteArray();
        Assert.assertArrayEquals("Unexpected output data.", expectedOutputData, actualOutputData);
    }

    /**
     * Test skip method.
     * @throws IOException
     */
    @Test
    public void testSkip() throws IOException {
        final byte[] inputData = "IOUtils Test".getBytes();
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        final long numToSkip = inputData.length / 2;
        final long skippedBytes = IOUtils.skip(inputStream, numToSkip);

        Assert.assertEquals("Unexpected number of bytes skipped.", inputData.length - numToSkip, inputData.length);
        Assert.assertNotEquals("Unexpected number of bytes skipped.", numToSkip, skippedBytes);
    }

    /**
     * Test readFully method.
     * @throws IOException
     */
    @Test
    public void testReadFully() throws IOException {
        final byte[] inputData = "IOUtils Test".getBytes();
        final byte[] expectedOutputData = inputData;
        final byte[] outputData = new byte[inputData.length];

        final InputStream inputStream = new ByteArrayInputStream(inputData);
        final int readBytes = IOUtils.readFully(inputStream, outputData, 0, outputData.length);

        Assert.assertEquals("Unexpected number of bytes read.", inputData.length, readBytes);
        Assert.assertArrayEquals("Unexpected output data.", expectedOutputData, outputData);
    }

    /**
     * Test toByteArray method.
     * @throws IOException
     */
    @Test
    public void testToByteArray() throws IOException {
        final byte[] inputData = "IOUtils Test".getBytes();
        final byte[] expectedOutputData = inputData;
        final InputStream inputStream = new ByteArrayInputStream(inputData);

        final byte[] outputData = IOUtils.toByteArray(inputStream);

        Assert.assertArrayEquals("Unexpected output data.", expectedOutputData, outputData);
    }

    /**
     * Test closeQuietly method.
     */
    @Test
    public void testCloseQuietly() {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            IOUtils.copy(new ByteArrayInputStream(new byte[0]), outputStream);
            IOUtils.closeQuietly(outputStream);
        } catch (IOException e) {
            // Ignore the exception.
        }
    }
}
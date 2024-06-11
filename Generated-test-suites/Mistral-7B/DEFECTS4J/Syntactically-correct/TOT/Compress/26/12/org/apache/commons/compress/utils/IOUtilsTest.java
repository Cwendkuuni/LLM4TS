import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test class for IOUtils
 */
public final class IOUtilsTest {

    /**
     * Test copy method with correct input and output stream
     */
    @Test
    public void testCopy() throws IOException {
        final byte[] sourceData = {0x01, 0x02, 0x03, 0x04, 0x05};
        final ByteArrayOutputStream target = new ByteArrayOutputStream();

        IOUtils.copy(new ByteArrayInputStream(sourceData), target);

        final byte[] expected = {0x01, 0x02, 0x03, 0x04, 0x05};
        final byte[] actual = target.toByteArray();

        Assert.assertArrayEquals("Unexpected data in the output stream", expected, actual);
    }

    /**
     * Test copy method with incorrect buffer size
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCopyWithNegativeBufferSize() throws IOException {
        IOUtils.copy(new ByteArrayInputStream(new byte[0]), new ByteArrayOutputStream(1), -1);
    }

    /**
     * Test skip method with correct input stream and number of bytes to skip
     */
    @Test
    public void testSkip() throws IOException {
        final byte[] data = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final InputStream input = new ByteArrayInputStream(data);

        IOUtils.copy(input, output);
        IOUtils.skip(output, 3L);

        final byte[] expected = {0x05, 0x06, 0x07, 0x08};
        final byte[] actual = output.toByteArray();

        Assert.assertArrayEquals("Unexpected data in the output stream", expected, actual);
    }

    /**
     * Test skip method with empty input stream
     */
    @Test
    public void testSkipWithEmptyInputStream() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[0]);

        IOUtils.skip(input, 10L);
    }

    /**
     * Test readFully method with correct input stream and byte array
     */
    @Test
    public void testReadFully() throws IOException {
        final byte[] expected = {0x01, 0x02, 0x03, 0x04, 0x05};
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final InputStream input = new ByteArrayInputStream(expected);

        IOUtils.copy(input, output);
        final byte[] actual = new byte[expected.length];
        IOUtils.readFully(input, actual);

        Assert.assertArrayEquals("Unexpected data in the output byte array", expected, actual);
    }

    /**
     * Test toByteArray method with correct input stream
     */
    @Test
    public void testToByteArray() throws IOException {
        final byte[] expected = {0x01, 0x02, 0x03, 0x04, 0x05};
        final InputStream input = new ByteArrayInputStream(expected);

        final byte[] actual = IOUtils.toByteArray(input);

        Assert.assertArrayEquals("Unexpected data in the output byte array", expected, actual);
    }

    /**
     * Test closeQuietly method with null Closeable
     */
    @Test
    public void testCloseQuietlyWithNullCloseable() {
        IOUtils.closeQuietly(null);
    }
}
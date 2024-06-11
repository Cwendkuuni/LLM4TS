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
public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test IOUtils copy method".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long actual = IOUtils.copy(input, output);
        long expected = input.available();
        Assert.assertEquals(expected, actual);

        byte[] actualBytes = output.toByteArray();
        byte[] expectedBytes = input.readAllBytes();
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test IOUtils copy method with buffer size".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long actual = IOUtils.copy(input, output, 10);
        long expected = input.available();
        Assert.assertEquals(expected, actual);

        byte[] actualBytes = output.toByteArray();
        byte[] expectedBytes = input.readAllBytes();
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test IOUtils skip method".getBytes());
        long numToSkip = 5;
        long skipped = IOUtils.skip(input, numToSkip);

        Assert.assertEquals(expectedValue(numToSkip, input.available()), skipped);
    }

    @Test
    public void testReadFully() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test IOUtils readFully method".getBytes());
        byte[] buffer = new byte[input.available()];

        int actual = IOUtils.readFully(input, buffer);
        int expected = input.available();
        Assert.assertEquals(expected, actual);

        byte[] actualBytes = input.readAllBytes();
        Assert.assertArrayEquals(actualBytes, buffer);
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test IOUtils readFully method with offset and length".getBytes());
        byte[] buffer = new byte[input.available()];

        int actual = IOUtils.readFully(input, buffer, 5, 5);
        int expected = 5;
        Assert.assertEquals(expected, actual);

        byte[] actualBytes = new byte[5];
        System.arraycopy(buffer, 5, actualBytes, 0, expected);
        byte[] expectedBytes = "Test IOUtils readFully method with offset and length".substring(0, 5).getBytes();
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testToByteArray() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test IOUtils toByteArray method".getBytes());
        byte[] actualBytes = IOUtils.toByteArray(input);
        byte[] expectedBytes = input.readAllBytes();
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testCloseQuietly() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test IOUtils closeQuietly method".getBytes());
        IOUtils.closeQuietly(input);
        Assert.assertFalse(input.isOpen());
    }

    private long expectedValue(long numToSkip, long available) {
        return Math.min(numToSkip, available);
    }
}
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Hello World!".getBytes());

        long copiedBytes = IOUtils.copy(input, output);
        Assert.assertEquals(13, copiedBytes);
        byte[] expected = "Hello World!".getBytes();
        byte[] actual = output.toByteArray();
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Hello World!".getBytes());

        long copiedBytes = IOUtils.copy(input, output, 5);
        Assert.assertEquals(13, copiedBytes);
        byte[] expected = "Hello World!".getBytes();
        byte[] actual = output.toByteArray();
        Assert.assertArrayEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCopyWithNullInputStream() {
        OutputStream output = new ByteArrayOutputStream();
        InputStream input = null;

        IOUtils.copy(input, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCopyWithNullOutputStream() {
        InputStream input = new ByteArrayInputStream("Hello World!".getBytes());
        OutputStream output = null;

        IOUtils.copy(input, output);
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Hello World!".getBytes());

        long skipped = IOUtils.skip(input, 7);
        Assert.assertEquals(6, skipped);
        long remaining = 13 - 6;
        int skippedBytes = (int) IOUtils.skip(input, remaining);
        Assert.assertEquals(0, skippedBytes);
    }

    @Test
    public void testReadFully() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Hello World!".getBytes());
        byte[] expected = "Hello World!".getBytes();

        byte[] buffer = new byte[10];
        int bytesRead = IOUtils.readFully(input, buffer);
        Assert.assertEquals(expected.length, bytesRead);
        Assert.assertArrayEquals(expected, buffer);
    }

    @Test
    public void testReadFullyWithNegativeLength() {
        InputStream input = new ByteArrayInputStream("Hello World!".getBytes());

        byte[] buffer = new byte[10];
        try {
            IOUtils.readFully(input, buffer);
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testReadFullyWithNullInputStream() {
        byte[] buffer = new byte[10];

        try {
            IOUtils.readFully(null, buffer);
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World!".getBytes());
        byte[] expected = "Hello World!".getBytes();

        byte[] result = IOUtils.toByteArray(input);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testToByteArrayWithNullInputStream() {
        try {
            IOUtils.toByteArray(null);
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testCloseQuietlyWithNullCloseable() {
        Closeable closeable = null;

        IOUtils.closeQuietly(closeable);
    }
}
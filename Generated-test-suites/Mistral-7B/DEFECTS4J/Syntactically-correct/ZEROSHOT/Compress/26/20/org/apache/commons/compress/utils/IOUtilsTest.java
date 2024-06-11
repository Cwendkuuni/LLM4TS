import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        long copiedBytes = IOUtils.copy(input, output);
        Assert.assertEquals(13, copiedBytes);
        byte[] expected = "Hello World".getBytes();
        byte[] actual = output.toByteArray();
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        long copiedBytes = IOUtils.copy(input, output, 5);
        Assert.assertEquals(1, copiedBytes);
        byte[] expected = "Hello World".getBytes();
        byte[] actual = output.toByteArray();
        Assert.assertArrayEquals(expected, actual);

        output = new ByteArrayOutputStream();
        input = new ByteArrayInputStream("Hello World".getBytes());
        copiedBytes = IOUtils.copy(input, output, 14);
        Assert.assertEquals(13, copiedBytes);
        expected = "Hello World".getBytes();
        actual = output.toByteArray();
        Assert.assertArrayEquals(expected, actual);

        output = new ByteArrayOutputStream();
        input = new ByteArrayInputStream("Hello World".getBytes());
        copiedBytes = IOUtils.copy(input, output, 8024);
        Assert.assertEquals(13, copiedBytes);
        expected = "Hello World".getBytes();
        actual = output.toByteArray();
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream("Hello World".getBytes());
        long skipped = IOUtils.skip(input, 7);
        Assert.assertEquals(7, skipped);
        byte[] expected = "World".getBytes();
        byte[] actual = output.toByteArray();
        Assert.assertArrayEquals(expected, actual);

        skipped = IOUtils.skip(input, 5);
        Assert.assertEquals(5, skipped);
        expected = new byte[0];
        actual = output.toByteArray();
        Assert.assertArrayEquals(expected, actual);

        skipped = IOUtils.skip(input, 12);
        Assert.assertEquals(0, skipped);
        expected = "Hello World".getBytes();
        actual = output.toByteArray();
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testReadFully() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        IOUtils.readFully(input, output.toByteArray());
        byte[] expected = "Hello World".getBytes();
        byte[] actual = output.toByteArray();
        Assert.assertArrayEquals(expected, actual);

        output = new ByteArrayOutputStream();
        input = new ByteArrayInputStream("Hello World".getBytes());
        byte[] buffer = new byte[10];
        IOUtils.readFully(input, buffer, 0, 5);
        byte[] expectedSubArray = new byte[]{"H", 'e', 'l', 'l', 'o'};
        byte[] actualSubArray = new byte[5];
        System.arraycopy(buffer, 0, actualSubArray, 0, 5);
        Assert.assertArrayEquals(expectedSubArray, actualSubArray);

        IOUtils.readFully(input, buffer, 5, 5);
        byte[] expectedSubArray2 = new byte[]{'W', 'o', 'r', 'l', 'd'};
        actualSubArray = new byte[5];
        System.arraycopy(buffer, 5, actualSubArray, 0, 5);
        Assert.assertArrayEquals(expectedSubArray2, actualSubArray);

        IOUtils.readFully(input, buffer, 10, 3);
        byte[] expectedSubArray3 = new byte[]{'l', 'd', '.'};
        actualSubArray = new byte[3];
        System.arraycopy(buffer, 10, actualSubArray, 0, 3);
        Assert.assertArrayEquals(expectedSubArray3, actualSubArray);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            IOUtils.closeQuietly((Closeable) output);
            Assert.assertTrue(output.size() > 0);
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                // do nothing
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void testCloseQuietlyNull() {
        IOUtils.closeQuietly(null);
    }
}
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
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        long copiedBytes = IOUtils.copy(inputStream, outputStream);
        Assert.assertEquals("Incorrect number of bytes copied", 13, copiedBytes);
        Assert.assertEquals("Copied data not equal to original", "Hello World", outputStream.toString());
    }

    @Test
    public void testCopyBufferSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        long copiedBytes = IOUtils.copy(inputStream, outputStream, 5);
        Assert.assertTrue("Incorrect number of bytes copied with small buffer size", copiedBytes < 13);
        Assert.assertEquals("Copied data not equal to original", "Hello World", outputStream.toString());
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello World\nHello Again".getBytes());
        long skippedBytes = IOUtils.skip(inputStream, 3);
        Assert.assertEquals("Incorrect number of bytes skipped", 3, skippedBytes);
        Assert.assertEquals("Data after skip should be 'World\nHello Again'", "World\nHello Again", inputStream.toString());
    }

    @Test
    public void testSkipFail() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        long skippedBytes = IOUtils.skip(inputStream, 4);
        Assert.assertEquals("More than requested number of bytes skipped", 13, inputStream.available());
        Assert.assertEquals("Data after skip should be 'Hello World'", "Hello World", inputStream.toString());
        Assert.assertEquals("Incorrect number of bytes skipped", 0, skippedBytes);
    }

    @Test
    public void testReadFully() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        IOUtils.readFully(inputStream, outputStream.toByteArray(), 0, outputStream.size());
        Assert.assertEquals("Data not equal to original", "Hello World", new String(outputStream.toByteArray()));
    }

    @Test
    public void testReadFullyPartialFill() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        IOUtils.readFully(inputStream, outputStream.toByteArray(), 0, 5);
        Assert.assertEquals("Data not equal to original (partially filled)", "Hello", new String(outputStream.toByteArray()));
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        byte[] byteArray = IOUtils.toByteArray(inputStream);
        Assert.assertEquals("Data not equal to original", "Hello World", new String(byteArray));
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        IOUtils.closeQuietly(inputStream);
        Assert.assertFalse("Input stream closed", inputStream.isOpen());
    }
}
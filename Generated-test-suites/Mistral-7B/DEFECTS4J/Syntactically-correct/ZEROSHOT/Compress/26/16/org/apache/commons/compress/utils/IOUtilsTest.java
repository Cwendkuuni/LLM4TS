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
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());

        long bytesCopied = IOUtils.copy(inputStream, outputStream);
        Assert.assertEquals(13, bytesCopied);

        byte[] outputBytes = outputStream.toByteArray();
        Assert.assertArrayEquals("Hello World".getBytes(), outputBytes);
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());

        long bytesSkipped = IOUtils.skip(inputStream, 5);
        Assert.assertEquals(5, bytesSkipped);

        byte[] outputBytes = outputStream.toByteArray();
        Assert.assertArrayEquals("Hello World".getBytes(), outputBytes);
    }

    @Test
    public void testReadFully() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());

        byte[] buffer = new byte[10];
        int bytesRead = IOUtils.readFully(inputStream, buffer);
        Assert.assertEquals(13, bytesRead);
        Assert.assertArrayEquals("Hello World".getBytes(), buffer);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());

        byte[] outputBytes = IOUtils.toByteArray(inputStream);
        Assert.assertArrayEquals("Hello World".getBytes(), outputBytes);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());

        IOUtils.closeQuietly(inputStream);
        Assert.assertFalse(inputStream.isOpen());
    }
}
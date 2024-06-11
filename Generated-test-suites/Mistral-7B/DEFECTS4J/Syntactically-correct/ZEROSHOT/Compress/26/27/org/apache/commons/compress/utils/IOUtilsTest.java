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
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});

        long copiedBytes = IOUtils.copy(inputStream, outputStream);
        Assert.assertEquals(5L, copiedBytes);

        byte[] outputBytes = outputStream.toByteArray();
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, outputBytes);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});

        long copiedBytes = IOUtils.copy(inputStream, outputStream, 2);
        Assert.assertEquals(5L, copiedBytes);

        byte[] outputBytes = outputStream.toByteArray();
        Assert.assertArrayEquals(new byte[]{1, 2}, outputBytes, 0, 2);
        Assert.assertArrayEquals(new byte[]{3, 4, 5}, outputBytes, 2, 3);
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9});

        long skippedBytes = IOUtils.skip(inputStream, 5L);
        Assert.assertEquals(5L, skippedBytes);

        Assert.assertArrayEquals(new byte[]{6, 7, 8, 9}, inputStream.readAllBytes());
    }

    @Test
    public void testReadFully() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});

        byte[] buffer = new byte[5];
        int bytesRead = IOUtils.readFully(inputStream, buffer);

        Assert.assertEquals(5, bytesRead);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, buffer);
    }

    @Test
    public void testToByteArray() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});

        byte[] outputBytes = IOUtils.toByteArray(inputStream);

        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, outputBytes);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);

        IOUtils.closeQuietly(inputStream);
        Assert.assertFalse(inputStream.isOpen());
    }
}
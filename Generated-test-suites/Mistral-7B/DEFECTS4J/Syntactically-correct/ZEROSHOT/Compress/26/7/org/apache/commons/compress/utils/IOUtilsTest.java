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
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        long copiedBytes = IOUtils.copy(inputStream, outputStream);
        Assert.assertEquals(5, copiedBytes);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, outputStream.toByteArray());
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        long copiedBytes = IOUtils.copy(inputStream, outputStream, 3);
        Assert.assertEquals(5, copiedBytes);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, outputStream.toByteArray());
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        long skippedBytes = IOUtils.skip(inputStream, 3);
        Assert.assertEquals(3, skippedBytes);
        Assert.assertArrayEquals(new byte[]{3, 4, 5}, inputStream.readAllBytes());
    }

    @Test
    public void testReadFully() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        int readBytes = IOUtils.readFully(inputStream, new byte[5]);
        Assert.assertEquals(5, readBytes);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, outputStream.toByteArray());
    }

    @Test
    public void testReadFullyWithBufferSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        int readBytes = IOUtils.readFully(inputStream, new byte[5], 0, 5);
        Assert.assertEquals(5, readBytes);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, outputStream.toByteArray());
    }

    @Test
    public void testToByteArray() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        byte[] expectedByteArray = new byte[]{1, 2, 3, 4, 5};
        byte[] byteArray = IOUtils.toByteArray(inputStream);
        Assert.assertArrayEquals(expectedByteArray, byteArray);
    }

    @Test
    public void testCloseQuietly() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        IOUtils.closeQuietly(inputStream);
        Assert.assertNull(inputStream.available());
    }
}
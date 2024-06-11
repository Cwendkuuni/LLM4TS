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
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't'});

        long copiedBytes = IOUtils.copy(input, output);
        Assert.assertEquals(4, copiedBytes);

        byte[] expected = new byte[]{'T', 'e', 's', 't'};
        byte[] actual = output.toByteArray();
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't'});

        long copiedBytes = IOUtils.copy(input, output, 2);
        Assert.assertEquals(4, copiedBytes);

        byte[] expected = new byte[]{'T', 'e', 's', 't'};
        byte[] actual = output.toByteArray();
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testSkip() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't'});

        long skipped = IOUtils.skip(input, 3);
        Assert.assertEquals(1, skipped);

        byte[] expected = new byte[]{'s'};
        byte[] actual = output.toByteArray();
        Assert.assertArrayEquals(expected, actual);

        skipped = IOUtils.skip(input, 1);
        Assert.assertEquals(0, skipped);
    }

    @Test
    public void testReadFully() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't'});

        byte[] buffer = new byte[4];
        int bytesRead = IOUtils.readFully(input, buffer);

        Assert.assertEquals(4, bytesRead);
        byte[] expected = new byte[]{'T', 'e', 's', 't'};
        byte[] actual = new byte[4];
        System.arraycopy(buffer, 0, actual, 0, bytesRead);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testToByteArray() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't'});

        byte[] expected = new byte[]{'T', 'e', 's', 't'};
        byte[] actual = IOUtils.toByteArray(input);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        IOUtils.closeQuietly(output);
    }

    @Test(expected = NullPointerException.class)
    public void testCloseQuietlyNull() {
        IOUtils.closeQuietly(null);
    }
}
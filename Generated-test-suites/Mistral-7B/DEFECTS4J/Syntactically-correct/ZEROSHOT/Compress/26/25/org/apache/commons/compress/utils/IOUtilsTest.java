import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test class for IOUtils.
 */
public class IOUtilsTest {

    /**
     * Test copy method.
     */
    @Test
    public void testCopy() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"test".getBytes()});
        OutputStream output = new ByteArrayOutputStream();
        long actual = IOUtils.copy(input, output);
        Assert.assertEquals(4L, actual);
        Assert.assertEquals("test".getBytes(), output.toByteArray());
    }

    /**
     * Test copy method with custom buffer size.
     */
    @Test
    public void testCopyWithCustomBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"test".getBytes()});
        OutputStream output = new ByteArrayOutputStream();
        long actual = IOUtils.copy(input, output, 10);
        Assert.assertEquals(4L, actual);
        Assert.assertEquals("test".getBytes(), output.toByteArray());
    }

    /**
     * Test skip method.
     */
    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"test".getBytes()});
        long actual = IOUtils.skip(input, 3);
        Assert.assertEquals(3L, actual);
        Assert.assertEquals("est".getBytes(), IOUtils.toByteArray(input));
    }

    /**
     * Test readFully method.
     */
    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"test".getBytes()});
        byte[] expected = new byte[]{'t', 'e', 's', 't'};
        byte[] actual = IOUtils.toByteArray(input);
        Assert.assertArrayEquals(expected, actual);
        Assert.assertEquals(4, IOUtils.readFully(input, new byte[4]));
    }

    /**
     * Test readFully method with partial read.
     */
    @Test
    public void testReadFullyWithPartialRead() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{'t', 'e', 's', 't', 'a', ' ', ' ', 't'});
        byte[] expected = new byte[]{'t', 'e', 's'};
        byte[] actual = IOUtils.toByteArray(input);
        Assert.assertEquals(3, IOUtils.readFully(input, new byte[3]));
        Assert.assertArrayEquals(expected, IOUtils.toByteArray(new ByteArrayInputStream(input)));
    }

    /**
     * Test toByteArray method.
     */
    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{'t', 'e', 's'.getBytes()});
        byte[] expected = new byte[]{'t', 'e', 's'};
        byte[] actual = IOUtils.toByteArray(input);
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test closeQuietly method.
     */
    @Test
    public void testCloseQuietly() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.closeQuietly(outputStream);
    }
}
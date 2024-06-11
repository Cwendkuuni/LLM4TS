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
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'A', 'B', 'C'});
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long actual = IOUtils.copy(input, output);
        Assert.assertEquals(3, actual);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'A', 'B', 'C'});
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long actual = IOUtils.copy(input, output, 2);
        Assert.assertEquals(3, actual);

        byte[] outputBytes = output.toByteArray();
        Assert.assertArrayEquals(new byte[]{'A', 'B', 'C'}, outputBytes);
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'A', 'B', 'C'});

        long actual = IOUtils.skip(input, 2L);
        Assert.assertEquals(1L, actual);

        byte[] inputBytes = new byte[3];
        int read = input.read(inputBytes, 0, 3);
        Assert.assertArrayEquals(new byte[]{'C'}, inputBytes);
    }

    @Test
    public void testReadFully() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'A', 'B', 'C'});
        byte[] expected = new byte[]{'A', 'B', 'C'};

        byte[] output = IOUtils.toByteArray(input);
        Assert.assertArrayEquals(expected, output);
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'A', 'B', 'C'});
        byte[] expected = new byte[]{'B', 'C'};

        byte[] output = new byte[3];
        int len = IOUtils.readFully(input, output, 1, 2);
        Assert.assertEquals(2, len);
        Assert.assertArrayEquals(expected, new byte[]{'B', 'C'});
    }

    @Test
    public void testToByteArray() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'A', 'B', 'C'});

        byte[] actual = IOUtils.toByteArray(input);
        Assert.assertArrayEquals(new byte[]{'A', 'B', 'C'}, actual);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        IOUtils.closeQuietly(input);
    }
}
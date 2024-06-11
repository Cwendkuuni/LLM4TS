import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtilsTest {

    private InputStream input;
    private OutputStream output;
    private byte[] inputData = new byte[]{1, 2, 3, 4, 5};
    private byte[] outputData = new byte[]{1, 2, 3, 4, 5};

    @Before
    public void setUp() {
        input = new ByteArrayInputStream(inputData);
        output = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(input);
        IOUtils.closeQuietly(output);
    }

    @Test
    public void testCopy() throws IOException {
        long bytesCopied = IOUtils.copy(input, output);
        byte[] outputBytes = output.toByteArray();

        Assert.assertEquals(inputData.length, bytesCopied);
        Assert.assertArrayEquals(inputData, outputBytes);
    }

    @Test
    public void testSkip() throws IOException {
        long numToSkip = 3;
        long skipped = IOUtils.skip(input, numToSkip);

        byte[] remainingData = new byte[inputData.length - (int) numToSkip];
        System.arraycopy(inputData, 3, remainingData, 0, remainingData.length);

        byte[] skippedBytes = new byte[3];
        System.arraycopy(inputData, 0, skippedBytes, 0, 3);

        Assert.assertArrayEquals(skippedBytes, IOUtils.toByteArray(input));
        Assert.assertArrayEquals(remainingData, IOUtils.toByteArray(input));
        Assert.assertEquals(numToSkip + inputData.length - 3, input.available());
        Assert.assertEquals(numToSkip, skipped);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] buffer = new byte[inputData.length];
        int bytesRead = IOUtils.readFully(input, buffer, 0, buffer.length);

        Assert.assertEquals(inputData.length, bytesRead);
        Assert.assertArrayEquals(inputData, buffer);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream inputWithBuffer = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream outputWithBuffer = new ByteArrayOutputStream();

        long bytesCopied = IOUtils.copy(inputWithBuffer, outputWithBuffer, 1024);

        byte[] outputBytesWithBuffer = outputWithBuffer.toByteArray();
        Assert.assertEquals(inputData.length, bytesCopied);
        Assert.assertArrayEquals(inputData, outputBytesWithBuffer);
    }
}
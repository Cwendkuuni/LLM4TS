import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test class for IOUtils class
 */
public class IOUtilsTest {

    /**
     * Test copy method
     */
    @Test
    public void testCopy() throws IOException {
        byte[] inputData = new byte[]{'T', 'e', 's', 't', ' ', 'C', 'o', 'p', 'y'};
        byte[] expectedOutputData = new byte[]{'T', 'e', 's', 't', ' ', 'C', 'o', 'p', 'y'};

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(inputData);

        long actualCopiedBytes = IOUtils.copy(inputStream, outputStream);
        byte[] outputData = outputStream.toByteArray();

        Assert.assertEquals("Incorrect number of bytes copied", expectedOutputData.length, actualCopiedBytes);
        Assert.assertArrayEquals("Output data is not as expected", expectedOutputData, outputData);
    }

    /**
     * Test copy method with custom buffer size
     */
    @Test
    public void testCopyWithCustomBufferSize() throws IOException {
        byte[] inputData = new byte[]{'T', 'e', 's', 't', ' ', 'C', 'o', 'p', 'y'};
        byte[] expectedOutputData = new byte[]{'T', 'e', 's', 't', ' ', 'C', 'o', 'p', 'y'};

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(inputData);

        long actualCopiedBytes = IOUtils.copy(inputStream, outputStream, 5);

        byte[] outputData = outputStream.toByteArray();

        Assert.assertEquals("Incorrect number of bytes copied", expectedOutputData.length, actualCopiedBytes);
        Assert.assertArrayEquals("Output data is not as expected", expectedOutputData, outputData);
    }

    /**
     * Test skip method
     */
    @Test
    public void testSkip() throws IOException {
        byte[] inputData = new byte[]{'T', 'e', 's', 't', ' ', 'C', 'o', 'p', 'y', ' ', 'I', 'O', 'U', 'T', 'S'};
        InputStream inputStream = new ByteArrayInputStream(inputData);

        long expectedSkippedBytes = 7;
        long actualSkippedBytes = IOUtils.skip(inputStream, expectedSkippedBytes);

        byte[] outputData = new byte[inputData.length - (int) expectedSkippedBytes];
        System.arraycopy(inputData, 0, outputData, 0, outputData.length);

        byte[] actualOutputData = new byte[outputData.length];
        InputStream actualOutputStream = new ByteArrayInputStream(outputData);

        IOUtils.readFully(actualOutputStream, actualOutputData);

        Assert.assertArrayEquals("Output data is not as expected", actualOutputData, inputData, false);
    }

    /**
     * Test readFully method
     */
    @Test
    public void testReadFully() throws IOException {
        byte[] expectedInputData = new byte[]{'T', 'e', 's', 't', ' ', 'C', 'o', 'p', 'y'};
        byte[] actualOutputData = new byte[expectedInputData.length];

        InputStream inputStream = new ByteArrayInputStream(expectedInputData);

        IOUtils.readFully(inputStream, actualOutputData);

        Assert.assertArrayEquals("Output data is not as expected", expectedInputData, actualOutputData);
    }

    /**
     * Test toByteArray method
     */
    @Test
    public void testToByteArray() throws IOException {
        byte[] inputData = new byte[]{'T', 'e', 's', 't', ' ', 'C', 'o', 'p', 'y'};
        byte[] expectedOutputData = new byte[]{'T', 'e', 's', 't', ' ', 'C', 'o', 'p', 'y'};

        InputStream inputStream = new ByteArrayInputStream(inputData);
        byte[] actualOutputData = IOUtils.toByteArray(inputStream);

        Assert.assertArrayEquals("Output data is not as expected", expectedOutputData, actualOutputData);
    }
}
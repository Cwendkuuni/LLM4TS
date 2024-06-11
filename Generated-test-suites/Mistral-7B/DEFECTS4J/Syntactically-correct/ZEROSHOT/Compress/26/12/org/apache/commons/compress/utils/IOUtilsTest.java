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
        byte[] inputData = "IOUtils is a utility class".getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        long copiedBytes = IOUtils.copy(new ByteArrayInputStream(inputData), outputStream);
        byte[] expectedOutput = "IOUtils is a utility class".getBytes();
        byte[] actualOutput = outputStream.toByteArray();

        Assert.assertEquals("Unexpected number of bytes copied", expectedOutput.length, copiedBytes);
        Assert.assertArrayEquals("Unexpected output from copy method", expectedOutput, actualOutput);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] inputData = "IOUtils is a utility class".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);

        long skippedBytes = IOUtils.skip(inputStream, 7);
        byte[] actualOutput = new byte[inputData.length - 7];
        System.arraycopy(inputData, 0, actualOutput, 0, actualOutput.length);

        Assert.assertEquals("Unexpected number of bytes skipped", actualOutput.length, skippedBytes);
        Assert.assertArrayEquals("Unexpected output after skipping bytes", actualOutput, IOUtils.toByteArray(inputStream));
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] inputData = "IOUtils is a utility class".getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[10];

        IOUtils.write(outputStream, "IOUtils is a utility class".getBytes());
        byte[] expectedOutput = outputStream.toByteArray();

        int bytesRead = IOUtils.readFully(new ByteArrayInputStream(expectedOutput), buffer);

        Assert.assertEquals("Unexpected number of bytes read", expectedOutput.length, bytesRead);
        Assert.assertArrayEquals("Unexpected output from readFully method", expectedOutput, buffer);
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] inputData = "IOUtils is a utility class".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);

        byte[] expectedOutput = new byte[inputData.length];
        byte[] actualOutput = IOUtils.toByteArray(inputStream);

        Assert.assertArrayEquals("Unexpected output from toByteArray method", expectedOutput, actualOutput);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("IOUtils is a utility class".getBytes());

        IOUtils.closeQuietly(inputStream);

        Assert.assertNull("Expected inputStream to be null after closeQuietly", inputStream);
    }
}
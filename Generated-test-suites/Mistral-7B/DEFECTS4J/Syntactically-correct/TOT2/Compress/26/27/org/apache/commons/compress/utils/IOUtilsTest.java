import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        byte[] inputData = new byte[] {1, 2, 3, 4, 5};
        byte[] expectedOutput = new byte[] {1, 2, 3, 4, 5};

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(new ByteArrayInputStream(inputData), outputStream);

        byte[] outputData = outputStream.toByteArray();

        Assert.assertArrayEquals("Output data doesn't match expected data", expectedOutput, outputData);
    }

    // Add edge cases and exception handling test cases here
}
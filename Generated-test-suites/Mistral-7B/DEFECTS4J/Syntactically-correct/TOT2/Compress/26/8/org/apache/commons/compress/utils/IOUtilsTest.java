import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class IOUtilsTest {

    @Test
    public void testCopyMethod() throws IOException {
        byte[] inputData = "Copy Test".getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        long bytesCopied = IOUtils.copy(new ByteArrayInputStream(inputData), outputStream);
        byte[] expectedOutput = "Copy Test".getBytes();
        byte[] actualOutput = outputStream.toByteArray();

        assertEquals("Incorrect number of bytes copied", expectedOutput.length, bytesCopied);
        assertArrayEquals("Data was not copied correctly", expectedOutput, actualOutput);
    }
}
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class IOUtilsTest {

    byte[] inputData = {1, 2, 3, 4, 5};

    @Test
    public void testCopy() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);

        long bytesCopied = IOUtils.copy(inputStream, outputStream);
        byte[] outputData = outputStream.toByteArray();

        assertArrayEquals(inputData, outputData);
        assertEquals(inputData.length, bytesCopied);
    }
}
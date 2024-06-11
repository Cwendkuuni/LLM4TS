import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class IOUtilsTest {

    @Test
    public void testCopy() throws Exception {
        ByteArrayInputStream input = new ByteArrayInputStream("Hello World".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        int expected = "Hello World".length();
        long actual = IOUtils.copy(input, output);
        byte[] outputBytes = output.toByteArray();

        assertEquals(expected, actual);
        assertEquals("Hello World", new String(outputBytes));
    }
}
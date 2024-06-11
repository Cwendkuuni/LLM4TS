import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "aGVsbG8=";
        byte[] expectedOutput = {97, 112, 101, 115, 116, 101};
        byte[] output = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expectedOutput, output);
    }
}
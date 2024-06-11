import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        byte[] expectedOutput = new byte[] { 0, -119, -121, -116, -112, -112, -112, -112, -112, -112, -112, -112, -112, -112, -112, -112, -112, -112, -112, -112, -112, -112, -112, -112, -112, -112, -112, -112, -112, -112, -112, -112, -112, -112, -112, -112, -112, -112, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        byte[] output = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expectedOutput, output);
    }
}
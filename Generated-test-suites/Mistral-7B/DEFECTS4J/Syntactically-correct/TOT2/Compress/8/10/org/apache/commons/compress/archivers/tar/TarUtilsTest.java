import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // Test case 1: Valid octal string
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(01234567L, result);

        // Test case 2: Invalid byte in the middle of the string
        byte[] invalidBuffer = new byte[]{'0', '1', '2', '3', '4', '8'}; // '8' instead of '7'
        try {
            TarUtils.parseOctal(invalidBuffer, 0, invalidBuffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test case 3: Empty buffer
        byte[] emptyBuffer = new byte[0];
        try {
            TarUtils.parseOctal(emptyBuffer, 0, emptyBuffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test case 4: Buffer shorter than required length
        byte[] shorterBuffer = new byte[]{'0', '1'};
        try {
            TarUtils.parseOctal(shorterBuffer, 0, shorterBuffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Add test cases for other methods here
}
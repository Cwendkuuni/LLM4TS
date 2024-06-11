import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.compress.archivers.tar.TarUtils;

public class TarUtilsTest {

    @Test
    public void testParseOctal_ValidInput() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0'};
        int offset = 0;
        int length = 5;
        long expectedResult = 0175550L;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidInput() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '8'}; // Should throw an exception
        int offset = 0;
        int length = 5;
        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test
    public void testParseName_ValidInput() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'i', 'n', 'p', 'u', 't', '.', 't', 'x', 't', ' ', '0', '1', '0'};
        int offset = 0;
        int length = 14;
        String expectedResult = "tar.input.txt";
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatNameBytes_ValidInput() {
        String name = "tar.output.txt";
        byte[] buffer = new byte[11];
        int offset = 0;
        int length = 11;
        int result = TarUtils.formatNameBytes(name, buffer, offset, length);
        //Check the format is correct
        for (int i = 0; i < length; i++) {
            System.out.println("Byte " + i + " = " + (int) buffer[i]);
        }
        assertTrue(result == offset + length);
    }

    
    // Add more test cases here, ensuring coverage of all methods and edge cases
    
}
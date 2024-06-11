import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '5', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0123456750L, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '8', '5', '0'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
        fail("Expected IllegalArgumentException not thrown");
    }

    @Test
    public void testParseOctalMissingTrailingSpace() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '5'};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            assertTrue(message.startsWith("Missing trailing space/NUL"));
        }
    }

    @Test
    public void testParseOctalAllNUL() {
        byte[] buffer = new byte[0];
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0L, result);
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer1 = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7'};
        long result1 = TarUtils.parseOctalOrBinary(buffer1, 0, buffer1.length);
        assertEquals(01234567L, result1);

        byte[] buffer2 = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '1'};
        long result2 = TarUtils.parseOctalOrBinary(buffer2, 0, buffer2.length);
        assertEquals(101234567L, result2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryInvalidByte() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '8'};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        fail("Expected IllegalArgumentException not thrown");
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer1 = new byte[]{1};
        boolean result1 = TarUtils.parseBoolean(buffer1, 0);
        assertTrue(result1);

        byte[] buffer2 = new byte[]{0};
        boolean result2 = TarUtils.parseBoolean(buffer2, 0);
        assertFalse(result2);

        byte[] buffer3 = new byte[]{0, 0};
        try {
            TarUtils.parseBoolean(buffer3, 0);
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            assertTrue(message.startsWith("Invalid byte"));
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'m', 'y', ' ','f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p'};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("my file.tar.gz", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "my file.tar.gz";
        byte[] buffer = new byte[TarConstants.NAME_LENGTH];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        byte[] expected = new byte[]{
            'm', 'y', (byte) 0x20, 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g',
            'z', 'i', 'p', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        };
        assertArrayEquals(expected, buffer, 0, buffer.length);
        assertEquals(offset, buffer.length);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[12];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{
            '0', '0', '0', '0', '1', '2', '3', '4', '5', '0', '0', '0'
        };
        assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{
            '0', '0', '0', '0', '1', '2', '3', '4', '5', '0', ' ', '0'
        };
        assertArrayEquals(expected, buffer, 0, buffer.length);
        assertEquals(offset, buffer.length);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 123456789012345L;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'
        };
        assertArrayEquals(expected, buffer, 0, buffer.length);
        assertEquals(offset, buffer.length);
    }

}
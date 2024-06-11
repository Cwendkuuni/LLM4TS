import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '7', '5', '5', '0'};
        assertEquals(017557755L, TarUtils.parseOctal(buffer, 0, buffer.length));

        byte[] buffer2 = new byte[]{'0', '1', '0', '7', '5', '5', '7', '7', '5', '5', '0'};
        assertEquals(0101017557755L, TarUtils.parseOctal(buffer2, 0, buffer2.length));

        byte[] buffer3 = new byte[]{'0', '7', '5', '5', '0'};
        assertEquals(01755L, TarUtils.parseOctal(buffer3, 0, buffer3.length));

        byte[] buffer4 = new byte[]{'0', '7', '5', '5'};
        assertEquals(01755L, TarUtils.parseOctal(buffer4, 0, buffer4.length));

        byte[] buffer5 = new byte[]{'0'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer5, 0, buffer5.length));

        byte[] buffer6 = new byte[]{'0', '7'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer6, 0, buffer6.length));

        byte[] buffer7 = new byte[]{'0', '7', '5', '6'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer7, 0, buffer7.length));

        byte[] buffer8 = new byte[]{'0', '7', '5', '5', '1'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer8, 0, buffer8.length));

        byte[] buffer9 = new byte[]{'0', '7', '5', '5', '7', '8'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer9, 0, buffer9.length));

        byte[] buffer10 = new byte[]{'0', '7', '5', '5', '7', '7', '5', '5', '0', '1'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer10, 0, buffer10.length));
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = new byte[]{'0', '1', '0', '1'};
        assertEquals(101L, TarUtils.parseOctalOrBinary(buffer, 0, buffer.length));

        byte[] buffer2 = new byte[]{'0', '1', '0', '1', '0'};
        assertEquals(10101L, TarUtils.parseOctalOrBinary(buffer2, 0, buffer2.length));

        byte[] buffer3 = new byte[]{'0', '1', '1', '1'};
        assertEquals(1111L, TarUtils.parseOctalOrBinary(buffer3, 0, buffer3.length));

        byte[] buffer4 = new byte[]{'0', '1', '1', '1', '0'};
        assertEquals(11110L, TarUtils.parseOctalOrBinary(buffer4, 0, buffer4.length));

        byte[] buffer5 = new byte[]{'0', '1', '1', '1', '1'};
        assertEquals(11111L, TarUtils.parseOctalOrBinary(buffer5, 0, buffer5.length));

        byte[] buffer6 = new byte[]{'0', '1', '1', '1', '1', '1'};
        assertEquals(111111111L, TarUtils.parseOctalOrBinary(buffer6, 0, buffer6.length));

        byte[] buffer7 = new byte[]{'0', '1', '1', '1', '1', '1', '0'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(buffer7, 0, buffer7.length));

        byte[] buffer8 = new byte[]{'0', '1', '1', '1', '1', '1', '1', '1'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(buffer8, 0, buffer8.length));
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer = new byte[]{'1'};
        assertTrue(TarUtils.parseBoolean(buffer, 0));

        byte[] buffer2 = new byte[]{'0'};
        assertFalse(TarUtils.parseBoolean(buffer2, 0));

        byte[] buffer3 = new byte[]{'1', '1'};
        assertTrue(TarUtils.parseBoolean(buffer3, 0));

        byte[] buffer4 = new byte[]{'0', '0'};
        assertFalse(TarUtils.parseBoolean(buffer4, 0));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'};
        assertEquals("abcdefgih", TarUtils.parseName(buffer, 0, buffer.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abcdefghij";
        byte[] buffer = new byte[TarConstants.NAME_LENGTH];

        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals("abcdefghij", new String(buffer, 0, offset));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 1234567890L;
        byte[] buffer = new byte[TarConstants.UIDLEN];

        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals("0123567145", new String(buffer));
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 1234567890L;
        byte[] buffer = new byte[TarConstants.UIDLEN + 1];

        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals("0123567145 ", new String(buffer, 0, offset));
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456789012345L;
        byte[] buffer = new byte[TarConstants.UIDLEN + 1];

        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals("0123567145712345 ", new String(buffer, 0, offset));
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 123456789012345L;
        byte[] buffer = new byte[TarConstants.UIDLEN + 1];

        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        assertEquals("0123567145712345", new String(buffer, 0, offset));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0x75, 0x72, 0x6e, 0x63, 0x68, 0x65, 0x78, 0x74, 0x5f, 0x5c, 0x41, 0x53, 0x53, 0x55};
        assertEquals(0x153e981d, TarUtils.computeCheckSum(buffer));
    }
}
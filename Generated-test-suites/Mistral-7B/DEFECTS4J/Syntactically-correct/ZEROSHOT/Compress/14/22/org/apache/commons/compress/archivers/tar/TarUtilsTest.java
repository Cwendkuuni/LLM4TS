package org.apache.commons.compress.archivers.tar;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TarUtilsTest {

    private static final byte[] TEST_BUFFER = new byte[] {
            0x00, 'o', 'c', 't', 'a', 'l', 0x00, 0x03, ' ', '7', '5', '5', ' ', '7', '7', '7', '7', '5', '5', ' ', '1', '2',
            0x00, (byte) 0x80, ' ', '7', '5', '5', ' ', '7', '7', '7', '7', '5', '5'
    };

    @Test
    public void testParseOctal() {
        assertEquals(0L, TarUtils.parseOctal(new byte[0], 0, 0));
        assertEquals(0L, TarUtils.parseOctal(TEST_BUFFER, 0, 2));
        assertEquals(1755L, TarUtils.parseOctal(TEST_BUFFER, 0, 7));
        assertEquals(277755L, TarUtils.parseOctal(TEST_BUFFER, 0, 11));
        assertEquals(3777755L, TarUtils.parseOctal(TEST_BUFFER, 0, 15));

        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(TEST_BUFFER, 0, 1));
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(TEST_BUFFER, 0, 8));
    }

    @Test
    public void testParseOctalOrBinary() {
        assertEquals(0L, TarUtils.parseOctalOrBinary(new byte[0], 0, 0));
        assertEquals(1755L, TarUtils.parseOctalOrBinary(TEST_BUFFER, 0, 2));
        assertEquals(277755L, TarUtils.parseOctalOrBinary(TEST_BUFFER, 0, 7));
        assertEquals(3777755L, TarUtils.parseOctalOrBinary(TEST_BUFFER, 0, 11));
        assertEquals(12345678912345L, TarUtils.parseOctalOrBinary(new byte[] {0x00, 0x81, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
                0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f}, 0, 18));

        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(TEST_BUFFER, 0, 1));
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(TEST_BUFFER, 0, 8));
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(new byte[] {0x00, 0x80, 0x01}, 0, 3));
    }

    @Test
    public void testParseBoolean() {
        assertEquals(true, TarUtils.parseBoolean(TEST_BUFFER, 5));
        assertEquals(true, TarUtils.parseBoolean(TEST_BUFFER, 13));

        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseBoolean(TEST_BUFFER, 2));
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseBoolean(TEST_BUFFER, 14));
    }

    @Test
    public void testParseName() {
        assertEquals("test.txt", TarUtils.parseName(TEST_BUFFER, 0, 11));
        assertEquals("test.tar", TarUtils.parseName(TEST_BUFFER, 0, 7));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test.txt";
        byte[] buffer = new byte[TarConstants.NAMELEN];

        TarUtils.formatNameBytes(name, buffer, 0, TarConstants.NAMELEN);
        assertEquals("test.txt", new String(buffer));

        name = "test.tar";
        TarUtils.formatNameBytes(name, buffer, 0, TarConstants.NAMELEN);
        assertEquals("test.tar", new String(buffer));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 1755;
        byte[] buffer = new byte[TarConstants.UIDLEN];

        TarUtils.formatUnsignedOctalString(value, buffer, 0, TarConstants.UIDLEN);
        assertEquals("07555", new String(buffer));

        value = 277755;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, TarConstants.UIDLEN);
        assertEquals("0777555", new String(buffer));

        value = 3777755;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, TarConstants.UIDLEN);
        assertEquals("07777555", new String(buffer));
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 1755;
        byte[] buffer = new byte[TarConstants.UIDLEN + 2];

        TarUtils.formatOctalBytes(value, buffer, 0, TarConstants.UIDLEN + 2);
        assertEquals("07555 " + (char) 0x00, new String(buffer));

        value = 277755;
        TarUtils.formatOctalBytes(value, buffer, 0, TarConstants.UIDLEN + 2);
        assertEquals("0777555 " + (char) 0x00, new String(buffer));

        value = 3777755;
        TarUtils.formatOctalBytes(value, buffer, 0, TarConstants.UIDLEN + 2);
        assertEquals("07777555 " + (char) 0x00, new String(buffer));
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 12345678912345L;
        byte[] buffer = new byte[TarConstants.LONGUIDLEN + 2];

        TarUtils.formatLongOctalBytes(value, buffer, 0, TarConstants.LONGUIDLEN + 2);
        assertEquals("01234567755 " + (char) 0x00, new String(buffer));
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 12345678912345L;
        byte[] buffer = new byte[TarConstants.UIDLEN];

        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, TarConstants.UIDLEN);
        assertEquals("12345677755", new String(buffer));

        value = 12345678912345L;
        buffer = new byte[TarConstants.UIDLEN + 2];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, TarConstants.UIDLEN + 2);
        assertEquals("12345677755 " + (char) 0x00, new String(buffer));

        value = Long.MAX_VALUE;
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, TarConstants.UIDLEN));
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, TarConstants.UIDLEN + 2));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] testBuffer = new byte[] {
                0x00, 'o', 'c', 't', 'a', 'l', 0x00, 0x03, ' ', '7', '5', '5', ' ', '7', '7', '7', '7', '5', '5', ' ', '1', '2',
                0x00, (byte) 0x80, ' ', '7', '5', '5', ' ', '7', '7', '7', '7', '5', '5'
        };
        assertEquals(6611521603630553349L, TarUtils.computeCheckSum(testBuffer));

        byte[] testBuffer2 = new byte[] {
                0x00, 'o', 'c', 't', 'a', 'l', 0x00, 0x03, ' ', '7', '5', '5', ' ', '7', '7', '7', '7', '5', '5', ' ', '1', '2'
        };
        assertEquals(6611521603630553348L, TarUtils.computeCheckSum(testBuffer2));
    }
}
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', ' ', '0'};
        long expected = 0123456L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);

        byte[] bufferTrailingNull = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '0'};
        long actualTrailingNull = TarUtils.parseOctal(bufferTrailingNull, 0, bufferTrailingNull.length);
        assertEquals(expected, actualTrailingNull);

        byte[] bufferTrailingSpace = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', ' '};
        long actualTrailingSpace = TarUtils.parseOctal(bufferTrailingSpace, 0, bufferTrailingSpace.length);
        assertEquals(expected, actualTrailingSpace);

        byte[] bufferMissingTrailing = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7'};
        try {
            TarUtils.parseOctal(bufferMissingTrailing, 0, bufferMissingTrailing.length);
            assert false;
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] bufferInvalidByte = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8'};
        try {
            TarUtils.parseOctal(bufferInvalidByte, 0, bufferInvalidByte.length);
            assert false;
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] bufferLeadingNUL = new byte[]{'0', 0, '1', '2', '3', '4', '5', '6', '7', ' ', '0'};
        long actualLeadingNUL = TarUtils.parseOctal(bufferLeadingNUL, 0, bufferLeadingNUL.length);
        assertEquals(0, actualLeadingNUL);

        byte[] bufferLeadingNUL2 = new byte[]{0, '1', '2', '3', '4', '5', '6', '7', ' ', '0'};
        long actualLeadingNUL2 = TarUtils.parseOctal(bufferLeadingNUL2, 0, bufferLeadingNUL2.length);
        assertEquals(0, actualLeadingNUL2);

        byte[] bufferLeadingNUL3 = new byte[]{'0', 0, '1', '2', '3', '4', '5', '6', '7', ' ', '0'};
        long actualLeadingNUL3 = TarUtils.parseOctal(bufferLeadingNUL3, 0, bufferLeadingNUL3.length);
        assertEquals(0, actualLeadingNUL3);
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] bufferOctal = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7'};
        long expectedOctal = 0123456L;
        long actualOctal = TarUtils.parseOctalOrBinary(bufferOctal, 0, bufferOctal.length);
        assertEquals(expectedOctal, actualOctal);

        byte[] bufferBinary = new byte[]{0, 0, 0, 1, 2, 3, 4, 5, 6};
        long expectedBinary = 0x0000010203040506L;
        long actualBinary = TarUtils.parseOctalOrBinary(bufferBinary, 0, bufferBinary.length);
        assertEquals(expectedBinary, actualBinary);

        byte[] bufferInvalidBinaryLength = new byte[]{0, 0, 0, 1, 2, 3, 4, 5, 6, 7};
        try {
            TarUtils.parseOctalOrBinary(bufferInvalidBinaryLength, 0, bufferInvalidBinaryLength.length);
            assert false;
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] bufferInvalidBinaryByte = new byte[]{0, 0, 0, 1, 2, 3, 4, 5, 6, 8};
        try {
            TarUtils.parseOctalOrBinary(bufferInvalidBinaryByte, 0, bufferInvalidBinaryByte.length);
            assert false;
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseBoolean() {
        byte[] bufferTrue = new byte[]{1};
        boolean expectedTrue = true;
        boolean actualTrue = TarUtils.parseBoolean(bufferTrue, 0);
        assertEquals(expectedTrue, actualTrue);

        byte[] bufferFalse = new byte[]{0};
        boolean expectedFalse = false;
        boolean actualFalse = TarUtils.parseBoolean(bufferFalse, 0);
        assertEquals(expectedFalse, actualFalse);

        byte[] bufferOther = new byte[]{2};
        try {
            TarUtils.parseBoolean(bufferOther, 0);
            assert false;
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'x', 't'};
        String expected = "tar.txt";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, actual);

        byte[] bufferTrailingNUL = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'x', 't', 0};
        String actualTrailingNUL = TarUtils.parseName(bufferTrailingNUL, 0, bufferTrailingNUL.length);
        assertEquals(expected, actualTrailingNUL);

        byte[] bufferTrailingSpace = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'x', 't', ' '};
        String actualTrailingSpace = TarUtils.parseName(bufferTrailingSpace, 0, bufferTrailingSpace.length);
        assertEquals(expected, actualTrailingSpace);

        byte[] bufferMissingTrailing = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'x', 't'};
        try {
            TarUtils.parseName(bufferMissingTrailing, 0, bufferMissingTrailing.length);
            assert false;
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatNameBytes() {
        String name = "tar.txt";
        byte[] expected = new byte[]{'t', 'a', 'r', '.', 't', 'x', 't'};
        byte[] buffer = new byte[7];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, 7);
        byte[] actual = new byte[offset];
        System.arraycopy(buffer, 0, actual, 0, offset);
        assertArrayEquals(expected, actual);

        String name2 = "tar.txt";
        byte[] buffer2 = new byte[12];
        int offset2 = TarUtils.formatNameBytes(name2, buffer2, 0, 12);
        byte[] actual2 = new byte[offset2];
        System.arraycopy(buffer2, 0, actual2, 0, offset2);
        assertArrayEquals(expected, actual2);

        String name3 = "abcdefghijklmnopqrstuvwxyz";
        byte[] buffer3 = new byte[31];
        int offset3 = TarUtils.formatNameBytes(name3, buffer3, 0, 31);
        byte[] actual3 = new byte[offset3];
        System.arraycopy(buffer3, 0, actual3, 0, offset3);
        assertArrayEquals(name3.getBytes(), actual3);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123456;
        byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '6'};
        byte[] buffer = new byte[7];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, 7);
        byte[] actual = new byte[7];
        System.arraycopy(buffer, 0, actual, 0, 7);
        assertArrayEquals(expected, actual);

        long value2 = Long.MIN_VALUE;
        byte[] buffer2 = new byte[TarConstants.MAXID + 1];
        try {
            TarUtils.formatUnsignedOctalString(value2, buffer2, 0, TarConstants.MAXID + 1);
            assert false;
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123456;
        byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '6', ' ', '0'};
        byte[] buffer = new byte[9];
        TarUtils.formatOctalBytes(value, buffer, 0, 9);
        byte[] actual = new byte[9];
        System.arraycopy(buffer, 0, actual, 0, 9);
        assertArrayEquals(expected, actual);

        long value2 = Long.MIN_VALUE;
        byte[] buffer2 = new byte[TarConstants.MAXID + 3];
        try {
            TarUtils.formatOctalBytes(value2, buffer2, 0, TarConstants.MAXID + 3);
            assert false;
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456;
        byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '6', ' ', '0'};
        byte[] buffer = new byte[9];
        TarUtils.formatLongOctalBytes(value, buffer, 0, 9);
        byte[] actual = new byte[9];
        System.arraycopy(buffer, 0, actual, 0, 9);
        assertArrayEquals(expected, actual);

        long value2 = Long.MIN_VALUE;
        byte[] buffer2 = new byte[TarConstants.MAXID + 3];
        try {
            TarUtils.formatLongOctalBytes(value2, buffer2, 0, TarConstants.MAXID + 3);
            assert false;
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 123456;
        byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '6'};
        byte[] buffer = new byte[7];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, 7);
        byte[] actual = new byte[7];
        System.arraycopy(buffer, 0, actual, 0, 7);
        assertArrayEquals(expected, actual);

        long value2 = Long.MIN_VALUE;
        byte[] buffer2 = new byte[8];
        try {
            TarUtils.formatLongOctalOrBinaryBytes(value2, buffer2, 0, 8);
            assert false;
        } catch (IllegalArgumentException e) {
            // expected
        }

        long value3 = Long.MAX_VALUE;
        byte[] buffer3 = new byte[TarConstants.MAXID + 1];
        try {
            TarUtils.formatLongOctalOrBinaryBytes(value3, buffer3, 0, TarConstants.MAXID + 1);
            assert false;
        } catch (IllegalArgumentException e) {
            // expected
        }

        long value4 = 123456789012345L;
        byte[] buffer4 = new byte[TarConstants.MAXID + 1];
        try {
            TarUtils.formatLongOctalOrBinaryBytes(value4, buffer4, 0, TarConstants.MAXID + 1);
            assert false;
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'x', 't'};
        long expected = TarUtils.computeCheckSum(buffer);
        long actual = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, actual);
    }
}
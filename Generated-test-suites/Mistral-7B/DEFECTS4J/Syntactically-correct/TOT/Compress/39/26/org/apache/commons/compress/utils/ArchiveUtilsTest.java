import org.apache.commons.compress.archivers.test.TestUtils;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = TestUtils.createTestFileEntry("/test.txt", "Hello World".getBytes());
        String result = ArchiveUtils.toString(entry);
        assertEquals("- 10         test.txt", result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "Hello World";
        byte[] buffer = "Hello World".getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));

        byte[] wrongBuffer = "Hello Java".getBytes();
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, wrongBuffer, 0, wrongBuffer.length));

        byte[] emptyBuffer = new byte[0];
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, emptyBuffer, 0, emptyBuffer.length));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "Hello World";
        byte[] expected = inputString.getBytes(CharsetNames.US_ASCII);
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        assertNotNull(result);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiString() {
        String inputString = "Hello World";
        String expected = inputString;
        String result = ArchiveUtils.toAsciiString(inputString.getBytes(CharsetNames.US_ASCII));
        assertEquals(expected, result);

        String wrongInputString = new String(new byte[]{1, 2, 3}, CharsetNames.US_ASCII);
        assertNull(ArchiveUtils.toAsciiString(new byte[]{}));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = "Hello World".getBytes();
        byte[] buffer2 = "Hello World".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));

        byte[] buffer3 = "Hello Java".getBytes();
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, false));

        byte[] buffer4 = new byte[10];
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer4, 0, buffer4.length, false));

        byte[] buffer5 = new byte[buffer1.length + 5];
        Arrays.fill(buffer5, (byte) 0);
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer5, 0, buffer5.length, false));

        byte[] buffer6 = new byte[buffer1.length + 5];
        Arrays.fill(buffer6, (byte) 0);
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer6, 0, buffer1.length, true));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[5];
        Arrays.fill(buffer1, (byte) 0);
        byte[] buffer2 = new byte[10];
        Arrays.fill(buffer2, (byte) 0);
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsArrayZero() {
        byte[] buffer = new byte[0];
        assertTrue(ArchiveUtils.isArrayZero(buffer, 0));

        byte[] buffer1 = new byte[5];
        Arrays.fill(buffer1, (byte) 0);
        assertTrue(ArchiveUtils.isArrayZero(buffer1, 0));

        byte[] buffer2 = new byte[5];
        Arrays.fill(buffer2, (byte) 1);
        assertFalse(ArchiveUtils.isArrayZero(buffer2, 0));
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testToAsciiStringThrowsException() {
        String inputString = "\uD800\uDC00"; // surrogate pair
        ArchiveUtils.toAsciiString(inputString.getBytes());
    }
}
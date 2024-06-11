
import org.apache.commons.compress.archivers.test.TestArchiveEntry;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new TestArchiveEntry("main.c", true, 2000L);
        String result = ArchiveUtils.toString(entry);
        String expected = "- 2000 main.c";
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToStringNullEntry() {
        ArchiveEntry entry = null;
        String result = ArchiveUtils.toString(entry);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "Hello World";
        byte[] buffer = "Hello World".getBytes(CharsetNames.US_ASCII);
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length);
        assertTrue(result);
    }

    @Test
    public void testMatchAsciiBufferDifferentEncodings() {
        String expected = "Hello World";
        byte[] buffer = "Hello World".getBytes(CharsetNames.UTF_8);
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length);
        assertFalse(result);
    }

    @Test
    public void testMatchAsciiBufferNegativeOffset() {
        String expected = "Hello World";
        byte[] buffer = "Hello World".getBytes(CharsetNames.US_ASCII);
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, -1, buffer.length);
        assertFalse(result);
    }

    @Test
    public void testMatchAsciiBufferNonAsciiBuffer() {
        String expected = "Hello World";
        byte[] buffer = new byte[]{0x68, 0x65, 0x6C, 0x6C, 0x6F, 0x20, 0x57, 0x6F, 0x72, 0x6C, 0x64, 0x21};
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length);
        assertFalse(result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x00};
        byte[] buffer2 = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x00};
        byte[] buffer3 = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
        assertTrue(result);

        result = ArchiveUtils.isEqual(buffer1, buffer3, true);
        assertFalse(result);
    }

    @Test
    public void testIsEqualDifferentLengths() {
        byte[] buffer1 = new byte[]{0x01, 0x02, 0x03};
        byte[] buffer2 = new byte[]{0x01, 0x02, 0x03, 0x04};
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
        assertFalse(result);
    }

    @Test
    public void testIsEqualDifferentContents() {
        byte[] buffer1 = new byte[]{0x01, 0x02, 0x03};
        byte[] buffer2 = new byte[]{0x02, 0x03, 0x01};
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
        assertFalse(result);
    }

    @Test
    public void testIsEqualIgnoreTrailingNulls() {
        byte[] buffer1 = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x00};
        byte[] buffer2 = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x00};
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsEqualNullBuffer1() {
        byte[] buffer1 = null;
        byte[] buffer2 = new byte[]{0x01, 0x02, 0x03};
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsEqualNullBuffer2() {
        byte[] buffer1 = new byte[]{0x01, 0x02, 0x03};
        byte[] buffer2 = null;
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
        fail("Expected IllegalArgumentException");
    }
}


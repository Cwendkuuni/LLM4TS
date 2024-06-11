import org.apache.commons.compress.utils.ArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        String result = ArchiveUtils.toString(entry);
        String expected = "- 0 test.txt";
        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = toAsciiBytes(expected);
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertTrue(result);
    }

    @Test
    public void testMatchAsciiBuffer_False() {
        String expected = "test";
        byte[] buffer = new byte[5];
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertFalse(result);
    }

    @Test
    public void testToStringEdgeCase() {
        ArchiveEntry entry = new ArchiveEntry("test\u0000.txt");
        String result = ArchiveUtils.toString(entry);
        String expected = "- 0 test\u0000.txt";
        assertEquals(expected, result);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testToAsciiBytes_Exception() {
        String inputString = "test\u0000";
        ArchiveUtils.toAsciiBytes(inputString);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };
        byte[] buffer2 = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        assertTrue(result);
    }

    @Test
    public void testIsEqual_False() {
        byte[] buffer1 = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };
        byte[] buffer2 = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x06 };
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        assertFalse(result);
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x00 };
        byte[] buffer2 = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x00 };
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithNull_False() {
        byte[] buffer1 = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x00 };
        byte[] buffer2 = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x01 };
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        assertFalse(result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[5];
        boolean result = ArchiveUtils.isArrayZero(a, 5);
        assertTrue(result);
    }

    @Test
    public void testIsArrayZero_False() {
        byte[] a = new byte[] { 1, 2, 3, 4, 5 };
        boolean result = ArchiveUtils.isArrayZero(a, 5);
        assertFalse(result);
    }

    @Test
    public void testSanitize() {
        String s = "test\u0000abc\u001fdef";
        String result = ArchiveUtils.sanitize(s);
        String expected = "test??....abc??def";
        assertEquals(expected, result);
    }

    @Test
    public void testSanitizeEdgeCase() {
        String s = "\u0000test\u001fabc\u001fdef";
        String result = ArchiveUtils.sanitize(s);
        String expected = "??test??abc??def";
        assertEquals(expected, result);
    }
}
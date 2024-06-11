
import org.apache.commons.compress.archivers.test.TestArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArchiveUtilsTest {

    @Test
    void testToString() {
        ArchiveEntry entry = new TestArchiveEntry("testfile.txt", false, 10);
        String result = ArchiveUtils.toString(entry);
        assertEquals("-      10 testfile.txt", result);
    }

    @Test
    void testToStringWithEmptyEntryName() {
        ArchiveEntry entry = new TestArchiveEntry("", false, 10);
        String result = ArchiveUtils.toString(entry);
        assertEquals("", result);
    }

    @Test
    void testToStringWithNullEntry() {
        String result = ArchiveUtils.toString(null);
        assertNull(result);
    }

    @Test
    void testToStringWithEmptyStringForEntryName() {
        ArchiveEntry entry = new TestArchiveEntry(null, false, 10);
        String result = ArchiveUtils.toString(entry);
        assertEquals("", result);
    }

    @Test
    void testToStringWithException() {
        ArchiveEntry entry = new TestArchiveEntry("testfile.txt", false, 10);
        assertThrows(RuntimeException.class, () -> ArchiveUtils.toString(null));
    }

    @Test
    void testMatchAsciiBufferWithExpectedNull() {
        byte[] buffer = new byte[10];
        boolean result = ArchiveUtils.matchAsciiBuffer(null, buffer, 0, buffer.length);
        assertFalse(result);
    }

    @Test
    void testMatchAsciiBufferWithExpectedEmptyString() {
        byte[] buffer = new byte[10];
        boolean result = ArchiveUtils.matchAsciiBuffer("", buffer, 0, buffer.length);
        assertFalse(result);
    }

    @Test
    void testMatchAsciiBufferWithBufferNull() {
        String expected = "test";
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, null, 0, 0);
        assertFalse(result);
    }

    @Test
    void testMatchAsciiBufferWithBufferEmpty() {
        String expected = "test";
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, new byte[0][], 0, 0);
        assertFalse(result);
    }

    @Test
    void testMatchAsciiBufferWithOffsetLessThanZero() {
        String expected = "test";
        byte[] buffer = new byte[10];
        int offset = -1;
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, offset, buffer.length);
        assertFalse(result);
    }

    @Test
    void testMatchAsciiBufferWithLengthLessThanZero() {
        String expected = "test";
        byte[] buffer = new byte[10];
        int length = -1;
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, length);
        assertFalse(result);
    }

    @Test
    void testMatchAsciiBufferWithOffsetPlusLengthGreaterThanBufferLength() {
        String expected = "test";
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 6;
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, offset, length);
        assertFalse(result);
    }

    // Add test cases for the other methods here
}

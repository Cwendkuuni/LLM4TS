import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;
import org.junit.Assert.*;

public class ArchiveUtilsTest {
    // Test case for toString method
    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("main.c");
        String actual = ArchiveUtils.toString(entry);
        String expected = "- 2000 main.c";
        assertEquals(expected, actual);

        entry.setDirectory(true);
        expected = "d 100 testfiles";
        assertEquals(expected, ArchiveUtils.toString(entry));
    }

    // Test case for matchAsciiBuffer method
    @Test
    public void testMatchAsciiBuffer() {
        String expected = "Hello World";
        byte[] buffer = "Hello World".getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));

        // Edge case: empty strings
        assertEquals(true, ArchiveUtils.matchAsciiBuffer("", new byte[0]));
        assertEquals(true, ArchiveUtils.matchAsciiBuffer(new String(), new byte[0]));

        // Exception handling: wrong charset
        try {
            ArchiveUtils.matchAsciiBuffer("abc", new byte[] {'a', 'b', 'c'}, 0, 3, "UTF-8");
            fail();
        } catch (RuntimeException e) {
            // expected
        }
    }

    // Test case for toAsciiBytes method
    @Test
    public void testToAsciiBytes() {
        String inputString = "Hello World";
        byte[] expected = "Hello World".getBytes(CharsetNames.US_ASCII);
        byte[] actual = ArchiveUtils.toAsciiBytes(inputString);
        assertArrayEquals(expected, actual);
    }

    // Test case for isEqual method
    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[] {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] buffer2 = new byte[] {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));

        byte[] buffer3 = new byte[] {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', 0x01};
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3));

        // Edge case: empty byte arrays
        assertTrue(ArchiveUtils.isEqual(new byte[0], new byte[0]));

        // Exception handling: negative offset or length
        try {
            ArchiveUtils.isEqual(new byte[1], new byte[0], -1, new byte[0], 0, 1, false);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}
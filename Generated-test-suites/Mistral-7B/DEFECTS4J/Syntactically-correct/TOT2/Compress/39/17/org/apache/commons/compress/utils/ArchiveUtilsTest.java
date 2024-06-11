import org.apache.commons.compress.archivers.ArchiveEntry;
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
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{1, 2, 3};
        byte[] buffer2 = new byte[]{1, 2, 3};
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[]{1, 2, 3, 0, 0};
        byte[] buffer2 = new byte[]{1, 2, 3, 0, 0};
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, buffer2);
        assertTrue(result);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testMatchAsciiBufferWithUnsupportedEncoding() {
        String expected = "test";
        byte[] buffer = new byte[10];
        ArchiveUtils.matchAsciiBuffer(expected, buffer);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = new byte[]{0x74, 0x65, 0x73, 0x74};
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertTrue(result);
    }
}
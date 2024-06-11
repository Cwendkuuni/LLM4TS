import org.apache.commons.compress.utils.ArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {

    @Test
    public void testToStringMethod() {
        ArchiveEntry testEntry = new ArchiveEntry("testfile.txt");
        String result = ArchiveUtils.toString(testEntry);
        String expected = "- 0 testfile.txt";
        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBufferMethod() {
        String expected = "teststring";
        byte[] buffer = toAsciiBytes(expected);
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertTrue(result);
    }

    @Test
    public void testIsEqualMethod() {
        byte[] buffer1 = {0x01, 0x02, 0x03};
        byte[] buffer2 = {0x01, 0x02, 0x03};
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        assertTrue(result);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testMatchAsciiBufferWithInvalidEncodingMethod() {
        String expected = "teststring";
        byte[] buffer = toAsciiBytes(expected);
        ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length); // intentionally passing incorrect arguments
    }
}
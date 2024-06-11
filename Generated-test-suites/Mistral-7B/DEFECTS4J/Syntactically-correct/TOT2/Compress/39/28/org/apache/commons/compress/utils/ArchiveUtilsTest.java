import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

public class ArchiveUtilsTest {
    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("main.c");
        String result = ArchiveUtils.toString(entry);
        String expected = "- 2000 main.c";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = toAsciiBytes(expected);
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{1, 2, 3};
        byte[] buffer2 = new byte[]{1, 2, 3};
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        Assert.assertTrue(result);

        byte[] buffer3 = new byte[]{1, 2, 3, 4};
        Assert.assertFalse(ArchiveUtils.isEqual(buffer1, buffer3));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[]{1, 2, 0};
        byte[] buffer2 = new byte[]{1, 2, 3, 0};
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, buffer2, 3, buffer2, 4, 1);
        Assert.assertTrue(result);
    }

    @Test
    public void testSanitize() {
        String input = "invalid_filename: with spaces and other characters!";
        String result = ArchiveUtils.sanitize(input);
        String expected = "invalid_filename:...............";
        Assert.assertEquals(expected, result);
    }
}
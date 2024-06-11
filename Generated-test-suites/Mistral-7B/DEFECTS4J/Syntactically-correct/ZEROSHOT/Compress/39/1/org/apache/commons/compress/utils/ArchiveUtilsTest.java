import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new MockArchiveEntry("testfiles", "testfiles", false, 100L);
        String expected = "- 100 testfiles";
        String result = ArchiveUtils.toString(entry);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "expectedString";
        byte[] buffer = toAsciiBytes(expected);
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertTrue(result);
    }

    @Test
    public void testMatchAsciiBufferFalse() {
        String expected = "expectedString";
        byte[] buffer = new byte[]{1, 2, 3};
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{'A', 'B', 'C'};
        byte[] buffer2 = new byte[]{'A', 'B', 'C'};
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEqualFalse() {
        byte[] buffer1 = new byte[]{'A', 'B', 'C'};
        byte[] buffer2 = new byte[]{'A', 'B', 'D'};
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEqualIgnoreTrailingNulls() {
        byte[] buffer1 = new byte[]{'A', 'B', 'C', 0, 0};
        byte[] buffer2 = new byte[]{'A', 'B', 'C'};
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEqualIgnoreTrailingNullsFalse() {
        byte[] buffer1 = new byte[]{'A', 'B', 'C', 0, 0};
        byte[] buffer2 = new byte[]{'A', 'B', 'D'};
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[5];
        boolean result = ArchiveUtils.isArrayZero(a, 5);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsArrayZeroFalse() {
        byte[] a = new byte[]{1, 2, 3, 4, 5};
        boolean result = ArchiveUtils.isArrayZero(a, 5);
        Assert.assertFalse(result);
    }

    @Test
    public void testSanitize() {
        String input = "abçdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%&*()_+-=[]{}|;:,.<>?/\\";
        String expected = "ab?bc?de?f?g?h?i?j?k?l?m?n?o?p?q?r?s?t?u?v?w?x?y?z?AB?CD?EF?GH?IJ?KL?MN?OP?QR?ST?UV?WX?YZ?0?1?2?3?4?5?6?7?8?9!?@?#?$?%?&?*?(\\)?_+-?=?[?]?{?}?|?;?<>?/?";
        String result = ArchiveUtils.sanitize(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testSanitizeEmptyString() {
        String input = "";
        String expected = "";
        String result = ArchiveUtils.sanitize(input);
        Assert.assertEquals(expected, result);
    }
}

class MockArchiveEntry extends ArchiveEntry {

    public MockArchiveEntry(String name, String fileName, boolean isDirectory, long size) {
        super(name, fileName, isDirectory, size);
    }

}
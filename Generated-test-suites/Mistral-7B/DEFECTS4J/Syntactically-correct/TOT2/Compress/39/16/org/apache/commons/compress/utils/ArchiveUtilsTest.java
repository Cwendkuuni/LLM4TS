import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

public class ArchiveUtilsTest {

    // Test case for toString method
    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        String result = ArchiveUtils.toString(entry);
        String expected = "- 0 test.txt";
        Assert.assertEquals(expected, result);
    }

    // Test case for matchAsciiBuffer method with basic usage
    @Test
    public void testMatchAsciiBufferBasicUsage() {
        String expected = "test";
        byte[] buffer = toAsciiBytes(expected);
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertTrue(result);
    }

    // Test case for matchAsciiBuffer method with edge cases (empty string and null)
    @Test
    public void testMatchAsciiBufferEdgeCases() {
        String emptyString = "";
        byte[] buffer = new byte[0];
        boolean result1 = ArchiveUtils.matchAsciiBuffer(emptyString, buffer);
        Assert.assertFalse(result1);

        String nullString = null;
        boolean result2 = ArchiveUtils.matchAsciiBuffer(nullString, buffer);
        Assert.assertFalse(result2);
    }

    // Test case for matchAsciiBuffer method with exception handling (UnsupportedEncodingException)
    @Test(expected = UnsupportedEncodingException.class)
    public void testMatchAsciiBufferExceptionHandling() {
        String expected = "test";
        byte[] buffer = new byte[10]; // buffer should be larger than the expected string
        ArchiveUtils.matchAsciiBuffer("invalidEncoding", buffer);
    }

    // Test case for sanitize method with basic usage
    @Test
    public void testSanitizeBasicUsage() {
        String input = "test string";
        String result = ArchiveUtils.sanitize(input);
        String expected = "test ???????????????????? string";
        Assert.assertEquals(expected, result);
    }

    // Test case for sanitize method with edge cases (empty string and null)
    @Test
    public void testSanitizeEdgeCases() {
        String emptyString = "";
        String result1 = ArchiveUtils.sanitize(emptyString);
        String expected1 = "";
        Assert.assertEquals(expected1, result1);

        String nullString = null;
        String result2 = ArchiveUtils.sanitize(nullString);
        String expected2 = "";
        Assert.assertEquals(expected2, result2);
    }
}
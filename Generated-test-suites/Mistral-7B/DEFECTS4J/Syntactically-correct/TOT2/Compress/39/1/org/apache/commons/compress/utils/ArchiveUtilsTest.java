import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToStringMethod() {
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        String expectedToString = "- 0 test.txt";
        String actualToString = ArchiveUtils.toString(entry);
        Assert.assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testMatchAsciiBufferWithExpectedStringAndBuffer() {
        String expected = "test";
        byte[] buffer = toAsciiBytes(expected);
        boolean actual = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertTrue(actual);
    }

    @Test
    public void testMatchAsciiBufferWithExpectedStringAndWrongBufferLength() {
        String expected = "test";
        byte[] buffer = new byte[5]; // buffer length should be equal to expected string length
        boolean actual = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertFalse(actual);
    }

    @Test
    public void testToStringMethodWithDirectoryEntry() {
        ArchiveEntry directoryEntry = new ArchiveEntry("test/");
        String expectedToString = "d 0 test/";
        String actualToString = ArchiveUtils.toString(directoryEntry);
        Assert.assertEquals(expectedToString, actualToString);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testToAsciiBytesWithInvalidEncoding() {
        String inputString = "test";
        ArchiveUtils.toAsciiBytes("invalidEncoding"); // UnsupportedEncodingException should be thrown
    }

    @Test
    public void testToStringMethodWithEmptyString() {
        ArchiveEntry emptyEntry = new ArchiveEntry("");
        String expectedToString = "- 0";
        String actualToString = ArchiveUtils.toString(emptyEntry);
        Assert.assertEquals(expectedToString, actualToString);
    }
}
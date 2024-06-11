import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static org.apache.commons.compress.utils.ArchiveUtils.*;

/**
 * Test class for ArchiveUtils
 */
public class ArchiveUtilsTest {

    /**
     * Test toString method
     */
    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("main.c");
        entry.setSize(2000L);
        entry.setDirectory(false);

        String expectedOutput = "- 2000 main.c";
        String result = toString(entry);

        Assert.assertEquals(expectedOutput, result);
    }

    /**
     * Test matchAsciiBuffer method with correct data
     */
    @Test
    public void testMatchAsciiBufferCorrect() {
        String expected = "expectedString";
        byte[] buffer = toAsciiBytes(expected);

        boolean result = matchAsciiBuffer(expected, buffer);

        Assert.assertTrue(result);
    }

    /**
     * Test matchAsciiBuffer method with incorrect data
     */
    @Test
    public void testMatchAsciiBufferIncorrect() {
        String expected = "expectedString";
        byte[] buffer = new byte[5];

        boolean result = matchAsciiBuffer(expected, buffer);

        Assert.assertFalse(result);
    }

    /**
     * Test toString method with edge case: empty entry
     */
    @Test
    public void testToStringEdgeCaseEmptyEntry() {
        ArchiveEntry entry = new ArchiveEntry("");
        entry.setSize(0L);
        entry.setDirectory(false);

        String expectedOutput = "d 0";
        String result = toString(entry);

        Assert.assertEquals(expectedOutput, result);
    }

    /**
     * Test toString method with error case: null entry
     */
    @Test(expected = NullPointerException.class)
    public void testToStringErrorCaseNullEntry() {
        ArchiveEntry entry = null;

        toString(entry);
    }

    /**
     * Test isEqual method with correct data
     */
    @Test
    public void testIsEqualCorrect() {
        byte[] buffer1 = new byte[]{1, 2, 3, 4, 5};
        byte[] buffer2 = new byte[]{1, 2, 3, 4, 5};

        boolean result = isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);

        Assert.assertTrue(result);
    }

    /**
     * Test isEqual method with incorrect data
     */
    @Test
    public void testIsEqualIncorrect() {
        byte[] buffer1 = new byte[]{1, 2, 3, 4, 5};
        byte[] buffer2 = new byte[]{1, 2, 3, 4, 6};

        boolean result = isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);

        Assert.assertFalse(result);
    }

    /**
     * Test isEqual method with edge case: different lengths
     */
    @Test
    public void testIsEqualEdgeCaseDifferentLengths() {
        byte[] buffer1 = new byte[]{1, 2, 3};
        byte[] buffer2 = new byte[]{1, 2, 3, 4};

        boolean result = isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);

        Assert.assertFalse(result);
    }

    /**
     * Test isEqual method with error case: null buffers
     */
    @Test(expected = NullPointerException.class)
    public void testIsEqualErrorCaseNullBuffers() {
        byte[] buffer1 = null;
        byte[] buffer2 = new byte[5];

        isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
    }

    /**
     * Test isEqual method with error case: negative lengths
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testIsEqualErrorCaseNegativeLengths() {
        byte[] buffer1 = new byte[]{1, 2, 3};
        byte[] buffer2 = new byte[5];

        isEqual(buffer1, -1, buffer1.length, buffer2, 0, buffer2.length, false);
    }

    /**
     * Test isEqualWithNull method with correct data
     */
    @Test
    public void testIsEqualWithNullCorrect() {
        byte[] buffer1 = new byte[]{1, 2, 3, 0, 4, 0};
        byte[] buffer2 = new byte[]{1, 2, 3, 0, 4, 0};

        boolean result = isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);

        Assert.assertTrue(result);
    }

    /**
     * Test isEqualWithNull method with incorrect data
     */
    @Test
    public void testIsEqualWithNullIncorrect() {
        byte[] buffer1 = new byte[]{1, 2, 3, 0, 4, 0};
        byte[] buffer2 = new byte[]{1, 2, 3, 0, 4, 5};

        boolean result = isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);

        Assert.assertFalse(result);
    }

    /**
     * Test isEqualWithNull method with edge cases: empty arrays and arrays with only nulls
     */
    @Test
    public void testIsEqualWithNullEdgeCases() {
        byte[] buffer1 = new byte[0];
        byte[] buffer2 = new byte[0];

        boolean result = isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);

        Assert.assertTrue(result);

        buffer1 = new byte[5];
        Arrays.fill(buffer1, (byte)0);
        buffer2 = new byte[5];
        Arrays.fill(buffer2, (byte)0);

        result = isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);

        Assert.assertTrue(result);
    }

    /**
     * Test isEqualWithNull method with error cases: null buffers and different lengths
     */
    @Test(expected = NullPointerException.class)
    public void testIsEqualWithNullErrorCases() {
        byte[] buffer1 = new byte[]{1, 2, 3, 0, 4, 0};
        byte[] buffer2 = null;

        isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);

        buffer1 = null;
        buffer2 = new byte[]{1, 2, 3, 0, 4, 0};

        isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);

        buffer1 = new byte[5];
        buffer2 = new byte[6];

        isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
    }
}
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.compress.archivers.test.TestArchiveEntry;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new TestArchiveEntry("testfile.txt", "test content".getBytes());
        String expectedOutput = "-       10 testfile.txt";
        String result = ArchiveUtils.toString(entry);
        Assert.assertEquals(expectedOutput, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "expected string";
        byte[] buffer = toAsciiBytes(expected);
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertTrue(result);
    }

    @Test
    public void testMatchAsciiBuffer_Mismatch() {
        String expected = "expected string";
        byte[] buffer = new byte[]{'a', 'b', 'c'};
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertFalse(result);
    }

    @Test
    public void testMatchAsciiBuffer_Offset() {
        String expected = "expected string";
        byte[] buffer = new byte[10];
        Arrays.fill(buffer, 5, 10, (byte) 'a');
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, 5, 5);
        Assert.assertFalse(result);

        result = ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, 10);
        Assert.assertFalse(result);

        result = ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, 5);
        Assert.assertTrue(result);
    }

    @Test
    public void testToString_NullEntry() {
        ArchiveEntry entry = null;
        String result = ArchiveUtils.toString(entry);
        Assert.assertNull(result);
    }

    @Test
    public void testToAsciiBytes() {
        String expected = "expected string";
        byte[] result = ArchiveUtils.toAsciiBytes(expected);
        Assert.assertArrayEquals(expected.getBytes(CharsetNames.US_ASCII), result);
    }

    @Test
    public void testToAsciiString() {
        byte[] bytes = new byte[]{1, 2, 3, 4};
        String expected = "abcd";
        String result = ArchiveUtils.toAsciiString(bytes);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{1, 2, 3, 1, 2, 3};
        byte[] buffer2 = new byte[]{1, 2, 3, 1, 2, 3};
        byte[] buffer3 = new byte[]{1, 2, 3, 4, 5, 6};
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        Assert.assertTrue(result);

        result = ArchiveUtils.isEqual(buffer1, buffer3);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEqual_Null() {
        byte[] buffer1 = null;
        byte[] buffer2 = new byte[]{1, 2, 3};
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        Assert.assertFalse(result);

        result = ArchiveUtils.isEqual(buffer2, buffer1);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEqual_IgnoreTrailingNulls() {
        byte[] buffer1 = new byte[]{1, 2, 3, 0};
        byte[] buffer2 = new byte[]{1, 2, 3};
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEqual_IgnoreTrailingNulls_Null() {
        byte[] buffer1 = null;
        byte[] buffer2 = new byte[]{1, 2, 3, 0};
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
        Assert.assertFalse(result);

        result = ArchiveUtils.isEqual(buffer2, buffer1, true);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEqual_IgnoreTrailingNulls_LongerBuffer() {
        byte[] buffer1 = new byte[]{1, 2, 3, 0};
        byte[] buffer2 = new byte[]{1, 2, 3};
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEqual_IgnoreTrailingNulls_ShorterBuffer() {
        byte[] buffer1 = new byte[]{1, 2, 3};
        byte[] buffer2 = new byte[]{1, 2, 3, 0};
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEqual_LongerBuffer() {
        byte[] buffer1 = new byte[]{1, 2, 3, 0};
        byte[] buffer2 = new byte[]{1, 2, 3};
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEqual_ShorterBuffer() {
        byte[] buffer1 = new byte[]{1, 2, 3};
        byte[] buffer2 = new byte[]{1, 2, 3, 0};
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[]{1, 2, 3, 0};
        byte[] buffer2 = new byte[]{1, 2, 3};
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, buffer2);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEqualWithNull_Null() {
        byte[] buffer1 = null;
        byte[] buffer2 = new byte[]{1, 2, 3};
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, buffer2);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEqualWithNull_Null2() {
        byte[] buffer1 = new byte[]{1, 2, 3};
        byte[] buffer2 = null;
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, buffer2);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[0];
        boolean result = ArchiveUtils.isArrayZero(array, 0);
        Assert.assertTrue(result);

        array = new byte[]{0};
        result = ArchiveUtils.isArrayZero(array, 0);
        Assert.assertTrue(result);

        array = new byte[]{0, 0};
        result = ArchiveUtils.isArrayZero(array, 0);
        Assert.assertTrue(result);

        array = new byte[]{0, 1};
        result = ArchiveUtils.isArrayZero(array, 0);
        Assert.assertFalse(result);
    }

    @Test
    public void testSanitize() {
        String input = "test string with  spécial characters: \u00E9\u00E4\u2122";
        String expectedOutput = "test string with ? ? ?: ?? ";
        String result = ArchiveUtils.sanitize(input);
        Assert.assertEquals(expectedOutput, result);
    }
}
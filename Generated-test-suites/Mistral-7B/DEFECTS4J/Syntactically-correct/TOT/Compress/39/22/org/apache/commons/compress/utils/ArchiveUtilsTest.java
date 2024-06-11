import org.apache.commons.compress.archivers.test.TestArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new TestArchiveEntry("entry.txt", true, new byte[0]);
        String expected = "-       0 entry.txt";
        String result = ArchiveUtils.toString(entry);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "expected";
        byte[] buffer = toAsciiBytes(expected);
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertEquals(true, result);
    }

    @Test
    public void testMatchAsciiBufferWithDifferentLengths() {
        String expected = "expected";
        byte[] buffer = new byte[10];
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testMatchAsciiBufferWithMismatch() {
        String expected = "expected";
        byte[] buffer = toAsciiBytes("different".getBytes(StandardCharsets.UTF_8));
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testToStringIgnoreTrailingNulls() {
        ArchiveEntry entry = new TestArchiveEntry("entry.txt", true, new byte[]{'a'.getValue(), 'b'.getValue(), '\u0000'});
        String expected = "-       2 ab";
        String result = ArchiveUtils.toString(entry);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{'a'.getValue(), 'b'.getValue(), '\u0000'};
        byte[] buffer2 = new byte[]{'a'.getValue(), 'b'.getValue(), '\u0000'};
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
        Assert.assertEquals(true, result);
    }

    @Test
    public void testIsEqualWithNulls() {
        byte[] buffer1 = new byte[]{'a'.getValue(), 'b'.getValue(), '\u0000'};
        byte[] buffer2 = new byte[]{'a'.getValue(), 'b'.getValue()};
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertEquals(true, result);
    }

    @Test
    public void testIsEqualWithDifferentLengths() {
        byte[] buffer1 = new byte[]{'a'.getValue(), 'b'.getValue(), '\u0000'};
        byte[] buffer2 = new byte[]{'a'.getValue(), 'b'.getValue(), 'c'.getValue()};
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testIsEqualWithMismatch() {
        byte[] buffer1 = new byte[]{'a'.getValue(), 'b'.getValue(), '\u0000'};
        byte[] buffer2 = new byte[]{'a'.getValue(), 'c'.getValue(), '\u0000'};
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testIsEqualStreams() throws IOException {
        byte[] buffer1 = new byte[]{'a'.getValue(), 'b'.getValue(), '\u0000'};
        byte[] buffer2 = new byte[]{'a'.getValue(), 'b'.getValue(), '\u0000'};
        InputStream inputStream1 = new ByteArrayInputStream(buffer1);
        InputStream inputStream2 = new ByteArrayInputStream(buffer2);
        boolean result = ArchiveUtils.isEqual(inputStream1, 0, buffer1.length, inputStream2, 0, buffer2.length, false);
        Assert.assertEquals(true, result);
    }

    @Test
    public void testIsEqualStreamsWithDifferentLengths() throws IOException {
        byte[] buffer1 = new byte[]{'a'.getValue(), 'b'.getValue(), '\u0000'};
        byte[] buffer2 = new byte[]{'a'.getValue(), 'b'.getValue(), 'c'.getValue()};
        InputStream inputStream1 = new ByteArrayInputStream(buffer1);
        InputStream inputStream2 = new ByteArrayInputStream(buffer2);
        boolean result = ArchiveUtils.isEqual(inputStream1, 0, buffer1.length, inputStream2, 0, buffer2.length, false);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testIsEqualStreamsWithMismatch() throws IOException {
        byte[] buffer1 = new byte[]{'a'.getValue(), 'b'.getValue(), '\u0000'};
        byte[] buffer2 = new byte[]{'a'.getValue(), 'c'.getValue(), '\u0000'};
        InputStream inputStream1 = new ByteArrayInputStream(buffer1);
        InputStream inputStream2 = new ByteArrayInputStream(buffer2);
        boolean result = ArchiveUtils.isEqual(inputStream1, 0, buffer1.length, inputStream2, 0, buffer2.length, false);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testIsEqualStreamsWithTrailingNulls() throws IOException {
        byte[] buffer1 = new byte[]{'a'.getValue(), 'b'.getValue()};
        byte[] buffer2 = new byte[]{'a'.getValue(), 'b'.getValue(), '\u0000'};
        InputStream inputStream1 = new ByteArrayInputStream(buffer1);
        InputStream inputStream2 = new ByteArrayInputStream(buffer2);
        boolean result = ArchiveUtils.isEqual(inputStream1, 0, buffer1.length, inputStream2, 0, buffer2.length, true);
        Assert.assertEquals(true, result);
    }

    @Test
    public void testIsEqualStreamsWithNullTrailingBytes() throws IOException {
        byte[] buffer1 = new byte[]{'a'.getValue(), 'b'.getValue(), '\u0000'};
        byte[] buffer2 = new byte[]{'a'.getValue(), 'b'.getValue()};
        InputStream inputStream1 = new ByteArrayInputStream(buffer1);
        InputStream inputStream2 = new ByteArrayInputStream(buffer2);
        boolean result = ArchiveUtils.isEqual(inputStream1, 0, buffer1.length, inputStream2, 0, buffer2.length, true);
        Assert.assertEquals(true, result);
    }

    @Test
    public void testIsEqualStreamsWithTrailingNullsAndMismatch() throws IOException {
        byte[] buffer1 = new byte[]{'a'.getValue(), 'b'.getValue(), '\u0000'};
        byte[] buffer2 = new byte[]{'a'.getValue(), 'c'.getValue(), '\u0000'};
        InputStream inputStream1 = new ByteArrayInputStream(buffer1);
        InputStream inputStream2 = new ByteArrayInputStream(buffer2);
        boolean result = ArchiveUtils.isEqual(inputStream1, 0, buffer1.length, inputStream2, 0, buffer2.length, true);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testisArrayZero() {
        byte[] a = new byte[0];
        boolean result = ArchiveUtils.isArrayZero(a, a.length);
        Assert.assertEquals(true, result);
    }

    @Test
    public void testisArrayZeroWithNonZero() {
        byte[] a = new byte[]{1};
        boolean result = ArchiveUtils.isArrayZero(a, a.length);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testSanitize() {
        String input = "This is a test string with special characters:  Formula: √(x+y)² = (x+y) * (x+y)";
        String expected = "This is a test string with special characters:  Formula:  ?   ?   (x+y)² = (x+y) * (x+y)";
        String result = ArchiveUtils.sanitize(input);
        Assert.assertEquals(expected, result);
    }
}
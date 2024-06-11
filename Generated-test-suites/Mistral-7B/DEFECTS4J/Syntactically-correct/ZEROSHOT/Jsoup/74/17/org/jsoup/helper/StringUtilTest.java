import org.junit.Test;
import org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.net.MalformedURLException;
import java.net.URL;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> list = Arrays.asList("hello", "world", "junit");
        String expected = "hello world junit";
        String result = StringUtil.join(list.iterator(), " ");
        assertEquals(expected, result);
    }

    @Test
    public void testJoinArray() {
        String[] array = new String[]{"hello", "world", "junit"};
        String expected = "hello world junit";
        String result = StringUtil.join(array, " ");
        assertEquals(expected, result);
    }

    @Test
    public void testJoinEmptyCollection() {
        List<String> list = Collections.emptyList();
        String result = StringUtil.join(list.iterator(), " ");
        assertEquals("", result);
    }

    @Test
    public void testJoinNullCollection() {
        List<String> list = null;
        String result = StringUtil.join(list.iterator(), " ");
        assertEquals("", result);
    }

    @Test
    public void testPadding() {
        String result = StringUtil.padding(5);
        assertEquals("     ", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegativeWidth() {
        StringUtil.padding(-5);
    }

    @Test
    public void testIsBlank() {
        String result = StringUtil.isBlank(null);
        assertTrue(result);

        String result1 = StringUtil.isBlank("");
        assertTrue(result1);

        String result2 = StringUtil.isBlank(" ");
        assertTrue(result2);

        String result3 = StringUtil.isBlank("   ");
        assertTrue(result3);

        String result4 = StringUtil.isBlank("\n");
        assertTrue(result4);

        String result5 = StringUtil.isBlank("\r");
        assertTrue(result5);

        String result6 = StringUtil.isBlank("\f");
        assertTrue(result6);

        String result7 = StringUtil.isBlank("hello");;
        assertFalse(result7);

        String result8 = StringUtil.isBlank("hello world");;
        assertFalse(result8);

        String result9 = StringUtil.isBlank("   hello   ");
        assertTrue(result9);
    }

    @Test
    public void testIsNumeric() {
        String result1 = StringUtil.isNumeric("123");
        assertTrue(result1);

        String result2 = StringUtil.isNumeric("456");
        assertTrue(result2);

        String result3 = StringUtil.isNumeric("789");
        assertTrue(result3);

        String result4 = StringUtil.isNumeric("1234");
        assertTrue(result4);

        String result5 = StringUtil.isNumeric("0");
        assertTrue(result5);

        String result6 = StringUtil.isNumeric(" ");
        assertFalse(result6);

        String result7 = StringUtil.isNumeric("abc");
        assertFalse(result7);

        String result8 = StringUtil.isNumeric("123a");
        assertFalse(result8);
    }

    @Test
    public void testIsWhitespace() {
        String result1 = StringUtil.isWhitespace(" ");
        assertTrue(result1);

        String result2 = StringUtil.isWhitespace("\t");
        assertTrue(result2);

        String result3 = StringUtil.isWhitespace("\n");
        assertTrue(result3);

        String result4 = StringUtil.isWhitespace("\f");
        assertTrue(result4);

        String result5 = StringUtil.isWhitespace("\r");
        assertTrue(result5);

        String result6 = StringUtil.isWhitespace("abc");
        assertFalse(result6);
    }

    @Test
    public void testIsActuallyWhitespace() {
        String result1 = StringUtil.isActuallyWhitespace(" ");
        assertTrue(result1);

        String result2 = StringUtil.isActuallyWhitespace("\t");
        assertTrue(result2);

        String result3 = StringUtil.isActuallyWhitespace("\n");
        assertTrue(result3);

        String result4 = StringUtil.isActuallyWhitespace("\f");
        assertTrue(result4);

        String result5 = StringUtil.isActuallyWhitespace("\r");
        assertTrue(result5);

        String result6 = StringUtil.isActuallyWhitespace("\u00A0"); // non-breaking space
        assertTrue(result6);
    }

    @Test
    public void testNormaliseWhitespace() {
        String result1 = StringUtil.normaliseWhitespace(" ");
        assertEquals(" ", result1);

        String result2 = StringUtil.normaliseWhitespace("\t");
        assertEquals(" ", result2);

        String result3 = StringUtil.normaliseWhitespace("\n");
        assertEquals(" ", result3);

        String result4 = StringUtil.normaliseWhitespace("   ");
        assertEquals(" ", result4);

        String result5 = StringUtil.normaliseWhitespace("hello\tworld");
        assertEquals("hello world", result5);

        String result6 = StringUtil.normaliseWhitespace("hello\nworld");
        assertEquals("hello world", result6);

        String result7 = StringUtil.normaliseWhitespace("hello\t world");
        assertEquals("hello world", result7);

        String result8 = StringUtil.normaliseWhitespace("hello\n world");
        assertEquals("hello world", result8);

        String result9 = StringUtil.normaliseWhitespace("hello\t world\n");
        assertEquals("hello world", result9);
    }

    @Test
    public void testIn() {
        String[] array = new String[]{"hello", "world", "junit"};
        String needle = "world";
        boolean expected = true;
        boolean result = StringUtil.in(needle, array);
        assertEquals(expected, result);
    }

    @Test
    public void testInSorted() {
        String[] array = new String[]{"hello", "world", "junit"};
        String needle = "world";
        boolean expected = true;
        boolean result = StringUtil.inSorted(needle, array);
        assertEquals(expected, result);
    }

    @Test
    public void testInEmptyArray() {
        String[] array = new String[0];
        String needle = "world";
        boolean result = StringUtil.in(needle, array);
        assertFalse(result);
    }

    @Test
    public void testInNullNeedle() {
        String[] array = new String[]{"hello", "world", "junit"};
        String needle = null;
        boolean result = StringUtil.in(needle, array);
        assertFalse(result);
    }

    @Test
    public void testJoinNullIterator() {
        Iterator<String> iterator = null;
        String sep = " ";
        String result = StringUtil.join(iterator, sep);
        assertEquals("", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJoinNullCollection() {
        Collection<String> collection = null;
        String sep = " ";
        String result = StringUtil.join(collection, sep);
        fail("Should have thrown IllegalArgumentException");
    }

    @Test
    public void testResolve() throws MalformedURLException {
        String base = "https://example.com/";
        String rel = "./about";
        String expected = "https://example.com/about";
        String result = StringUtil.resolve(base, rel);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveNullBase() {
        String rel = "./about";
        String expected = "";
        String result = StringUtil.resolve(null, rel);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveNullRel() {
        String base = "https://example.com/";
        String expected = "";
        String result = StringUtil.resolve(base, null);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveMalformedBase() throws MalformedURLException {
        String base = "https://example.com.";
        String rel = "./about";
        String result = StringUtil.resolve(base, rel);
        assertEquals("", result);
    }

    @Test
    public void testResolveMalformedRel() throws MalformedURLException {
        String base = "https://example.com/";
        String rel = "//another.com/about";
        String expected = "https://another.com/about";
        String result = StringUtil.resolve(base, rel);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveRelativeToAnotherDomain() throws MalformedURLException {
        String base = "https://example.com/";
        String rel = "http://another.com/about";
        String expected = "http://another.com/about";
        String result = StringUtil.resolve(base, rel);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveRelativeWithFragment() throws MalformedURLException {
        String base = "https://example.com/";
        String rel = "#fragment";
        String expected = base;
        String result = StringUtil.resolve(base, rel);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveAbsolute() throws MalformedURLException {
        String base = "https://example.com/";
        String rel = "https://example.com/about";
        String expected = rel;
        String result = StringUtil.resolve(base, rel);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveEmptyBase() throws MalformedURLException {
        String base = "";
        String rel = "./about";
        String result = StringUtil.resolve(base, rel);
        assertEquals("", result);
    }

    @Test
    public void testResolveEmptyRel() throws MalformedURLException {
        String base = "https://example.com/";
        String rel = "";
        String result = StringUtil.resolve(base, rel);
        assertEquals("", result);
    }

    @Test
    public void testResolveWithBaseTrailingSlash() throws MalformedURLException {
        String base = "https://example.com/";
        String rel = "./about";
        String expected = "https://example.com/about";
        String result = StringUtil.resolve(base, rel);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveWithBaseTrailingSlash2() throws MalformedURLException {
        String base = "https://example.com/";
        String rel = "/about";
        String expected = "https://example.com/about";
        String result = StringUtil.resolve(base, rel);
        assertEquals(expected, result);
    }
}
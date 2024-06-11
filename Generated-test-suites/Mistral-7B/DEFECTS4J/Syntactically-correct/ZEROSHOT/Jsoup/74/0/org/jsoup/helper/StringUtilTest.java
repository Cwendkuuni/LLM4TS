package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Tests for the StringUtil class.
 */
public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> list = Arrays.asList("test1", "test2", "test3");
        String expected = "test1 test2 test3";
        String result = StringUtil.join(list.iterator(), " ");
        assertEquals(expected, result);
    }

    @Test
    public void testJoinArray() {
        String[] array = new String[]{"test1", "test2", "test3"};
        String expected = "test1 test2 test3";
        String result = StringUtil.join(array, " ");
        assertEquals(expected, result);
    }

    @Test
    public void testJoinEmptyCollection() {
        String result = StringUtil.join(Collections.emptyList(), " ");
        assertEquals("", result);
    }

    @Test
    public void testJoinIteratorEmpty() {
        Iterator<String> iterator = Collections.emptyIterator();
        String result = StringUtil.join(iterator, " ");
        assertEquals("", result);
    }

    @Test
    public void testJoinIteratorSingle() {
        Iterator<String> iterator = Arrays.asList("test").iterator();
        String result = StringUtil.join(iterator, " ");
        assertEquals("test", result);
    }

    @Test
    public void testPadding() {
        for (int i = 0; i <= 21; i++) {
            String result = StringUtil.padding(i);
            assertEquals(StringUtil.padding(i).length(), result.length());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegative() {
        StringUtil.padding(-1);
    }

    @Test
    public void testIsBlankNull() {
        assertTrue(StringUtil.isBlank(null));
    }

    @Test
    public void testIsBlankEmpty() {
        assertTrue(StringUtil.isBlank(""));
    }

    @Test
    public void testIsBlankWhitespace() {
        String whitespace = "\t\n\r  ";
        assertTrue(StringUtil.isBlank(whitespace));
    }

    @Test
    public void testIsBlankNonWhitespace() {
        String nonWhitespace = "test";
        assertFalse(StringUtil.isBlank(nonWhitespace));
    }

    @Test
    public void testIsNumeric() {
        String numeric = "123";
        assertTrue(StringUtil.isNumeric(numeric));
    }

    @Test
    public void testIsNumericNonNumeric() {
        String nonNumeric = "test";
        assertFalse(StringUtil.isNumeric(nonNumeric));
    }

    @Test
    public void testIsWhitespaceSpace() {
        assertTrue(StringUtil.isWhitespace(' '));
    }

    @Test
    public void testIsWhitespaceTab() {
        assertTrue(StringUtil.isWhitespace('\t'));
    }

    @Test
    public void testIsWhitespaceNewline() {
        assertTrue(StringUtil.isWhitespace('\n'));
    }

    @Test
    public void testIsWhitespaceReturn() {
        assertTrue(StringUtil.isWhitespace('\r'));
    }

    @Test
    public void testIsActuallyWhitespaceNewline() {
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
    }

    @Test
    public void testIsActuallyWhitespaceTab() {
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
    }

    @Test
    public void testIsActuallyWhitespaceSpace() {
        assertTrue(StringUtil.isActuallyWhitespace(' '));
    }

    @Test
    public void testIsActuallyWhitespaceNbsp() {
        assertTrue(StringUtil.isActuallyWhitespace((int) 160));
    }

    @Test
    public void testNormaliseWhitespace() {
        String input = "\t test \t\n  \r\f foo\t bar\r\n\t baz\t  \n";
        String expected = " test foo bar baz ";
        String result = StringUtil.normaliseWhitespace(input);
        assertEquals(expected, result);
    }

    @Test
    public void testIn() {
        String haystack1 = "test1 test2 test3";
        assertTrue(StringUtil.in(
            "test2", "test1", "test2", "test3"
        ));

        assertFalse(StringUtil.in("test4", "test1", "test2", "test3"));

        String haystack2 = "test1,test2,test3";
        assertTrue(StringUtil.in("test2", "test1,test2,test3"));

        assertFalse(StringUtil.in("test4", "test1,test2,test3"));

        String haystack3 = "";
        assertFalse(StringUtil.in("test1", ""));
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"test2", "test1", "test3"};
        assertTrue(StringUtil.inSorted("test2", "test1", "test2", "test3"));

        assertFalse(StringUtil.inSorted("test4", "test1", "test2", "test3"));

        String[] haystack2 = {};
        assertFalse(StringUtil.inSorted("test1", ""));
    }

    @Test
    public void testResolveEmptyBaseUrl() {
        String relUrl = "/test";
        String expected = "";
        String result = StringUtil.resolve(null, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveEmptyBaseUrlRelativeToAbsolute() {
        String baseUrl = "https://example.com/";
        String relUrl = "/test";
        String expected = "https://example.com/test";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveAbsoluteUrl() {
        String baseUrl = "https://example.com/";
        String relUrl = "https://example.com/test";
        String expected = "https://example.com/test";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveAbsoluteUrlWithTrailingSlash() {
        String baseUrl = "https://example.com/";
        String relUrl = "/test";
        String expected = "https://example.com/test";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveBaseUrlWithTrailingSlash() {
        String baseUrl = "https://example.com/";
        String relUrl = "test";
        String expected = "https://example.com/test";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveBaseUrlWithoutTrailingSlash() {
        String baseUrl = "https://example.com";
        String relUrl = "test";
        String expected = "https://example.com/test";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveRelativeUrlWithTrailingSlash() {
        String baseUrl = "https://example.com/";
        String relUrl = "/test/";
        String expected = "https://example.com/test/";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveRelativeUrlWithoutTrailingSlash() {
        String baseUrl = "https://example.com/";
        String relUrl = "/test";
        String expected = "https://example.com/test";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveMalformedUrl() {
        String baseUrl = "https://example.com/";
        String relUrl = "test:/";
        String result = StringUtil.resolve(baseUrl, relUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveMalformedUrlBaseUrl() {
        String baseUrl = "https://example.com/";
        String relUrl = "test://example.com/";
        String result = StringUtil.resolve(baseUrl, relUrl);
    }
}
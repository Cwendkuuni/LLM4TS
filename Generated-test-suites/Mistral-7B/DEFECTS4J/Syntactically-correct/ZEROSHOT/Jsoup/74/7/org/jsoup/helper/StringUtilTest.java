package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * Test class for StringUtil.
 */
public class StringUtilTest {

    /**
     * Test for join method with Collection.
     */
    @Test
    public void testJoin_Collection() {
        List<String> strings = Arrays.asList("hello", "world", "!");
        String result = StringUtil.join(strings, " ");
        assertEquals("hello world !", result);

        strings = Collections.emptyList();
        result = StringUtil.join(strings, " ");
        assertEquals("", result);

        strings = null;
        result = StringUtil.join(strings, " ");
        assertNull(result);
    }

    /**
     * Test for join method with Iterator.
     */
    @Test
    public void testJoin_Iterator() {
        String[] strings = {"hello", "world", "!"};
        Iterator<String> iterator = Arrays.asList(strings).iterator();
        String result = StringUtil.join(iterator, " ");
        assertEquals("hello world !", result);

        iterator = Collections.emptyIterator();
        result = StringUtil.join(iterator, " ");
        assertEquals("", result);

        iterator = null;
        result = StringUtil.join(iterator, " ");
        assertNull(result);
    }

    /**
     * Test for join method with array.
     */
    @Test
    public void testJoin_Array() {
        String[] strings = {"hello", "world", "!"};
        String result = StringUtil.join(strings, " ");
        assertEquals("hello world !", result);

        strings = new String[0];
        result = StringUtil.join(strings, " ");
        assertEquals("", result);

        strings = null;
        result = StringUtil.join(strings, " ");
        assertNull(result);
    }

    /**
     * Test for padding method.
     */
    @Test
    public void testPadding() {
        for (int i = 1; i <= 21; i++) {
            String result = StringUtil.padding(i);
            assertEquals(padding[i], result);
        }

        String result = StringUtil.padding(-1);
        assertNull(result);
    }

    /**
     * Test for isBlank method.
     */
    @Test
    public void testIsBlank() {
        String result = StringUtil.isBlank(null);
        assertTrue(result);

        result = StringUtil.isBlank("");
        assertTrue(result);

        result = StringUtil.isBlank(" ");
        assertTrue(result);

        result = StringUtil.isBlank("\n");
        assertTrue(result);

        result = StringUtil.isBlank("\t");
        assertTrue(result);

        result = StringUtil.isBlank("  \n");
        assertTrue(result);

        result = StringUtil.isBlank("hello");
        assertFalse(result);

        result = StringUtil.isBlank("hello world!");
        assertFalse(result);
    }

    /**
     * Test for isNumeric method.
     */
    @Test
    public void testIsNumeric() {
        String result = StringUtil.isNumeric(null);
        assertFalse(result);

        result = StringUtil.isNumeric("");
        assertFalse(result);

        result = StringUtil.isNumeric("123");
        assertTrue(result);

        result = StringUtil.isNumeric("0");
        assertTrue(result);

        result = StringUtil.isNumeric("12345");
        assertTrue(result);

        result = StringUtil.isNumeric("123abc");
        assertFalse(result);

        result = StringUtil.isNumeric("abc123");
        assertFalse(result);

        result = StringUtil.isNumeric("123.45");
        assertTrue(result);
    }

    /**
     * Test for isWhitespace method (int).
     */
    @Test
    public void testIsWhitespace_Int() {
        for (int i = 0; i <= 127; i++) {
            boolean result = StringUtil.isWhitespace(i);
            if (isWhitespace(i)) {
                assertTrue(result);
            } else {
                assertFalse(result);
            }
        }
    }

    /**
     * Test for isActuallyWhitespace method (int).
     */
    @Test
    public void testIsActuallyWhitespace_Int() {
        for (int i = 0; i <= 127; i++) {
            boolean result = StringUtil.isActuallyWhitespace(i);
            if (isActuallyWhitespace(i)) {
                assertTrue(result);
            } else {
                assertFalse(result);
            }
        }

        // test non-breaking space
        int nbsp = 160;
        assertTrue(StringUtil.isActuallyWhitespace(nbsp));
    }

    /**
     * Test for normaliseWhitespace method.
     */
    @Test
    public void testNormaliseWhitespace() {
        String result = StringUtil.normaliseWhitespace("   hello  world!  ");
        assertEquals("hello world!", result);

        result = StringUtil.normaliseWhitespace("\n\t hello \r world!");
        assertEquals("hello world", result);

        result = StringUtil.normaliseWhitespace("  \n\t  \r world!");
        assertEquals("", result);

        result = StringUtil.normaliseWhitespace("   \n\t  \r world!   ");
        assertEquals("world", result);
    }

    /**
     * Test for in method.
     */
    @Test
    public void testIn() {
        String[] haystack = {"hello", "world", "!"};
        assertTrue(StringUtil.in(null, haystack));
        assertTrue(StringUtil.in("", haystack));
        assertTrue(StringUtil.in("hello", haystack));
        assertTrue(StringUtil.in("world", haystack));
        assertTrue(StringUtil.in("!", haystack));
        assertFalse(StringUtil.in("foo", haystack));

        String[] haystack2 = new String[0];
        assertFalse(StringUtil.in("foo", haystack2));

        String[] haystack3 = null;
        assertFalse(StringUtil.in("foo", haystack3));
    }

    /**
     * Test for inSorted method.
     */
    @Test
    public void testInSorted() {
        String[] haystack = {"hello", "world", "!"};
        assertTrue(StringUtil.inSorted("hello", haystack));
        assertTrue(StringUtil.inSorted("world", haystack));
        assertTrue(StringUtil.inSorted("!", haystack));
        assertFalse(StringUtil.inSorted("foo", haystack));

        String[] haystack2 = new String[0];
        assertFalse(StringUtil.inSorted("foo", haystack2));

        String[] haystack3 = null;
        assertFalse(StringUtil.inSorted("foo", haystack3));
    }

    /**
     * Test for resolve method with URL.
     */
    @Test
    public void testResolve_URL() throws MalformedURLException {
        String baseUrl = "http://example.com/path/to/file.html";
        String relUrl = "relative.html";
        URL expected = new URL("http://example.com/path/to/file.html/relative.html");

        URL result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected.toExternalForm(), result.toExternalForm());

        baseUrl = null;
        relUrl = "relative.html";
        expected = new URL("relative.html");

        result = StringUtil.resolve(null, relUrl);
        assertEquals(expected.toExternalForm(), result.toExternalForm());

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "";
        expected = new URL("http://example.com/path/to/file.html");

        result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected.toExternalForm(), result.toExternalForm());

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = ".";
        expected = new URL("http://example.com/path/to/file.html");

        result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected.toExternalForm(), result.toExternalForm());

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "..";
        expected = new URL("http://example.com/path/to/");

        result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected.toExternalForm(), result.toExternalForm());

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "../relative.html";
        expected = new URL("http://example.com/path/to/relative.html");

        result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected.toExternalForm(), result.toExternalForm());

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "http://example.com/another/path/to/file.html";
        expected = new URL("http://example.com/another/path/to/file.html");

        result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected.toExternalForm(), result.toExternalForm());
    }

    /**
     * Test for resolve method with String.
     */
    @Test
    public void testResolve_String() {
        String baseUrl = "http://example.com/path/to/file.html";
        String relUrl = "relative.html";
        String expected = "http://example.com/path/to/file.html/relative.html";

        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = null;
        relUrl = "relative.html";
        expected = "relative.html";

        result = StringUtil.resolve(null, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "";
        expected = "http://example.com/path/to/file.html";

        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = ".";
        expected = "http://example.com/path/to/file.html";

        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "..";
        expected = "http://example.com/path/to/";

        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "../relative.html";
        expected = "http://example.com/path/to/relative.html";

        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "http://example.com/another/path/to/file.html";
        expected = "http://example.com/another/path/to/file.html";

        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    /**
     * Test for stringBuilder method.
     */
    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        assertNotNull(sb);

        sb.append("hello");
        assertEquals("hello", sb.toString());

        sb.append(" world!");
        assertEquals("hello world!", sb.toString());

        sb.delete(0, sb.length());
        assertNotNull(sb);
        assertTrue(sb.length() == 0);
    }
}
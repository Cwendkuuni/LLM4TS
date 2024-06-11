package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Test class for the StringUtil class
 */
public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("test1", "test2", "test3");
        String expected = "test1,test2,test3";
        String result = StringUtil.join(strings, ", ");
        assertEquals(expected, result);

        strings = Collections.singletonList(null);
        result = StringUtil.join(strings, ", ");
        assertEquals("", result);

        strings = Collections.emptyList();
        result = StringUtil.join(strings, ", ");
        assertEquals("", result);
    }

    @Test
    public void testJoinIterator() {
        String[] strings = {"test1", "test2", "test3"};
        String expected = "test1 test2 test3";
        String result = StringUtil.join(Arrays.asList(strings).iterator(), " ");
        assertEquals(expected, result);

        Iterator<String> emptyIterator = Collections.emptyIterator();
        result = StringUtil.join(emptyIterator, " ");
        assertEquals("", result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"test1", "test2", "test3"};
        String expected = "test1 test2 test3";
        String result = StringUtil.join(strings, " ");
        assertEquals(expected, result);

        strings = new String[0];
        result = StringUtil.join(strings, " ");
        assertEquals("", result);
    }

    @Test
    public void testPadding() {
        for (int i = 0; i <= 21; i++) {
            String expected = StringUtil.padding(i);
            String result = "";
            if (i > 0)
                result += repeatString(" ", i);
            assertEquals(expected, result);
        }
    }

    @Test
    public void testIsBlank() {
        String[] strings = {"", "  ", " test", " test ", "test ", " test ", " test ", " test ", " test ", " test", "test123"};
        for (String string : strings) {
            boolean expected = StringUtil.isBlank(string);
            boolean result = false;
            if (string.isEmpty() || string.equals(" "))
                result = true;
            assertEquals(expected, result);
        }
    }

    @Test
    public void testIsNumeric() {
        String[] strings = {"123", "1A3", "test123", "test123abcd", "test123", "test123", "test123"};
        for (String string : strings) {
            boolean expected = StringUtil.isNumeric(string);
            boolean result = false;
            if (string.matches("^[0-9]+$"))
                result = true;
            assertEquals(expected, result);
        }
    }

    @Test
    public void testIsWhitespace() {
        char[] chars = {' ', '\t', '\n', '\r', '\f'};
        for (char c : chars) {
            boolean expected = StringUtil.isWhitespace(c);
            boolean result = false;
            if (Character.isWhitespace(c))
                result = true;
            assertEquals(expected, result);
        }
    }

    @Test
    public void testIsActuallyWhitespace() {
        char[] chars = {' ', '\t', '\n', '\r', '\f', '\u00A0'};
        for (char c : chars) {
            boolean expected = StringUtil.isActuallyWhitespace(c);
            boolean result = false;
            if (Character.isWhitespace(c) || c == '\u00A0')
                result = true;
            assertEquals(expected, result);
        }
    }

    @Test
    public void testNormaliseWhitespace() {
        String[] strings = {"test  \r\n \t test", "test", "test\t\r\n test\n", "test"};
        String expected = "testtest";
        for (String string : strings) {
            String result = StringUtil.normaliseWhitespace(string);
            assertEquals(expected, result);
        }
    }

    @Test
    public void testIn() {
        String[] strings = {"test1", "test2", "test3"};

        String needle = "test2";
        boolean expected = true;
        boolean result = StringUtil.in(needle, strings);
        assertEquals(expected, result);

        needle = "test4";
        result = StringUtil.in(needle, strings);
        assertEquals(false, result);
    }

    @Test
    public void testInSorted() {
        String[] strings = {"test1", "test2", "test3"};

        String needle = "test2";
        boolean expected = true;
        boolean result = StringUtil.inSorted(needle, strings);
        assertEquals(expected, result);

        needle = "test4";
        result = StringUtil.inSorted(needle, strings);
        assertEquals(false, result);
    }

    @Test
    public void testResolve() throws MalformedURLException {
        String baseUrl = "http://example.com/base";
        String relUrl = "/path/file.html";
        URL expected = new URL("http://example.com/path/file.html");
        URL result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected.toString(), result.toString());

        baseUrl = "http://example.com/base";
        relUrl = "file.html";
        expected = new URL("http://example.com/file.html");
        result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected.toString(), result.toString());

        baseUrl = "http://example.com/base";
        relUrl = "//anotherdomain.com/file.html";
        expected = new URL("http://anotherdomain.com/file.html");
        result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected.toString(), result.toString());

        baseUrl = "http://example.com/base";
        relUrl = "./file.html";
        expected = new URL("http://example.com/file.html");
        result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected.toString(), result.toString());

        baseUrl = "http://example.com/base";
        relUrl = "../file.html";
        expected = new URL("http://example.com/file.html");
        result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected.toString(), result.toString());

        baseUrl = "http://example.com/base";
        relUrl = "../../file.html";
        expected = new URL("http://example.com/file.html");
        result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected.toString(), result.toString());

        baseUrl = "http://example.com/base";
        relUrl = "file:/path/file.html";
        result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals("", result.toString());

        baseUrl = "http://example.com/base";
        relUrl = "";
        result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals("", result.toString());

        baseUrl = "http://example.com/base";
        relUrl = null;
        result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals("", result.toString());
    }

    @Test
    public void testResolveEmptyBase() {
        String baseUrl = "";
        String relUrl = "/path/file.html";
        String expected = "";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "";
        relUrl = "file.html";
        expected = "";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "";
        relUrl = "//anotherdomain.com/file.html";
        expected = "";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "";
        relUrl = "./file.html";
        expected = "";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "";
        relUrl = "../file.html";
        expected = "";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "";
        relUrl = "../../file.html";
        expected = "";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "";
        relUrl = "file:/path/file.html";
        expected = "";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "";
        relUrl = "";
        expected = "";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "";
        relUrl = null;
        expected = "";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveNullBase() {
        String baseUrl = null;
        String relUrl = "/path/file.html";
        String expected = "";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = null;
        relUrl = "file.html";
        expected = "";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = null;
        relUrl = "//anotherdomain.com/file.html";
        expected = "";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = null;
        relUrl = "./file.html";
        expected = "";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = null;
        relUrl = "../file.html";
        expected = "";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = null;
        relUrl = "../../file.html";
        expected = "";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = null;
        relUrl = "file:/path/file.html";
        expected = "";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = null;
        relUrl = "";
        expected = "";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = null;
        relUrl = null;
        expected = "";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        sb.append("test");
        String expected = "test";
        String result = sb.toString();
        assertEquals(expected, result);

        sb.setLength(0);
        sb.append("test1");
        sb.append("test2");
        expected = "test1test2";
        result = sb.toString();
        assertEquals(expected, result);

        sb.setLength(0);
        sb.append("test1");
        sb.append("test2");
        sb.append("test3");
        expected = "test1test2test3";
        result = sb.toString();
        assertEquals(expected, result);

        sb.setLength(0);
        sb.append("test1");
        sb.append("test2");
        sb.append("test3");
        sb.append("test4");
        expected = "test1test2test3test4";
        result = sb.toString();
        assertEquals(expected, result);

        sb.setLength(0);
        sb.append("test1");
        sb.append("test2");
        sb.append("test3");
        sb.append("test4");
        sb.append("test5");
        expected = "test1test2test3test4test5";
        result = sb.toString();
        assertEquals(expected, result);
    }

    private String repeatString(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(s);
        }
        return sb.toString();
    }

}
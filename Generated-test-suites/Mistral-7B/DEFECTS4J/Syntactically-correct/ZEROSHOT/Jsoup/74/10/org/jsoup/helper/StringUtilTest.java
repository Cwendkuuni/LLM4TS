package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class StringUtilTest {

    @Test
    public void testJoin() {
        List<String> list = Arrays.asList("hello", "world", "!");
        String expected = "helloworld!";
        String result = StringUtil.join(list, "");
        assertEquals(expected, result);

        list = Collections.singletonList("hello");
        expected = "hello";
        result = StringUtil.join(list, "");
        assertEquals(expected, result);

        list = Collections.emptyList();
        expected = "";
        result = StringUtil.join(list, "");
        assertEquals(expected, result);

        list = Arrays.asList("", "hello", "world", "!");
        expected = "helloworld!";
        result = StringUtil.join(list, "");
        assertEquals(expected, result);

        list = Arrays.asList("", null);
        expected = null;
        result = StringUtil.join(list, "");
        assertNull(result);

        list = Arrays.asList("hello", null);
        expected = "hello";
        result = StringUtil.join(list, "");
        assertEquals(expected, result);
    }

    @Test
    public void testJoinWithSeparator() {
        List<String> list = Arrays.asList("hello", "world", "!");
        String expected = "hello, world, !";
        String result = StringUtil.join(list, ", ");
        assertEquals(expected, result);

        list = Arrays.asList("hello", "world");
        expected = "hello, world";
        result = StringUtil.join(list, ", ");
        assertEquals(expected, result);

        list = Collections.singletonList("hello");
        expected = "hello";
        result = StringUtil.join(list, ", ");
        assertEquals(expected, result);

        list = Collections.emptyList();
        expected = "";
        result = StringUtil.join(list, ", ");
        assertEquals(expected, result);

        list = Arrays.asList("", "hello", "world", "!");
        expected = "hello, world, !";
        result = StringUtil.join(list, ", ");
        assertEquals(expected, result);

        list = Arrays.asList("", null);
        expected = null;
        result = StringUtil.join(list, ", ");
        assertNull(result);

        list = Arrays.asList("hello", null);
        expected = "hello";
        result = StringUtil.join(list, ", ");
        assertEquals(expected, result);
    }

    @Test
    public void testPadding() {
        for (int i = 0; i < padding.length; i++) {
            String expected = padding[i];
            String result = StringUtil.padding(i);
            assertEquals(expected, result);
        }

        try {
            String result = StringUtil.padding(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIsBlank() {
        String[] strings = new String[]{"", null, " ", "hello", "   hello   ", "hello world", " hello world ", "hello\nworld"};

        for (String string : strings) {
            boolean expected = StringUtil.isBlank(string);
            boolean result = false;
            if (string == null) {
                result = true;
            } else {
                int length = string.length();
                for (int i = 0; i < length; i++) {
                    if (!Character.isWhitespace(string.charAt(i))) {
                        result = false;
                        break;
                    }
                }
            }
            assertEquals(expected, result);
        }
    }

    @Test
    public void testIsNumeric() {
        String[] strings = new String[]{"1234", "1a2b3c", "123", "1A2B3C", "123abcd", "123", "123 "};

        for (String string : strings) {
            boolean expected = StringUtil.isNumeric(string);
            boolean result = false;
            int length = string.length();
            for (int i = 0; i < length; i++) {
                char c = string.charAt(i);
                if (Character.isDigit(c)) {
                    result = true;
                    break;
                }
            }
            assertEquals(expected, result);
        }
    }

    @Test
    public void testIsWhitespace() {
        char[] chars = new char[]{' ', '\t', '\n', '\f', '\r'};
        for (char c : chars) {
            boolean expected = StringUtil.isWhitespace(c);
            boolean result = true;
            assertEquals(expected, result);
        }

        for (char c = 0; c <= 127; c++) {
            if (c != ' ' && c != '\t' && c != '\n' && c != '\f' && c != '\r') {
                boolean expected = StringUtil.isWhitespace(c);
                boolean result = false;
                assertEquals(expected, result);
            }
        }
    }

    @Test
    public void testIsActuallyWhitespace() {
        char[] chars = new char[]{' ', '\t', '\n', '\f', '\r', 160};
        for (char c : chars) {
            boolean expected = StringUtil.isActuallyWhitespace(c);
            boolean result = true;
            assertEquals(expected, result);
        }

        for (char c = 0; c <= 127; c++) {
            if (c != ' ' && c != '\t' && c != '\n' && c != '\f' && c != '\r' && c != 160) {
                boolean expected = StringUtil.isActuallyWhitespace(c);
                boolean result = false;
                assertEquals(expected, result);
            }
        }
    }

    @Test
    public void testNormaliseWhitespace() {
        String[] strings = new String[]{"   hello world   ", "   hello    world   ", "hello\nworld", "   hello\nworld   ", "   \t\n   hello world   ", "   \n   hello world   ", "   \n   hello\nworld   ", "   hello\n   world   ", "   hello\nworld   ", "   \t\n   hello world  \t", "   \n   hello world  \n", "   hello\nworld  \t\n", "   hello\n   world  \t\n"};
        String[] expected = new String[]{"hello world", "hello world", "hello\nworld", "hello\nworld", "hello world", "hello world", "hello world", "hello world", "hello world", "hello world", "hello world", "hello world"};

        for (int i = 0; i < strings.length; i++) {
            String result = StringUtil.normaliseWhitespace(strings[i]);
            assertEquals(expected[i], result);
        }

        String s = "   hello   world   ";
        StringBuilder sb = StringUtil.stringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, s, false);
        String result = sb.toString();
        assertEquals("hello world", result);

        sb.setLength(0);
        StringUtil.appendNormalisedWhitespace(sb, s, true);
        result = sb.toString();
        assertEquals("hello world", result);
    }

    @Test
    public void testIn() {
        String[] strings = new String[]{"apple", "banana", "cherry", "apple"};
        boolean expected = true;
        boolean result = StringUtil.in(Strings.Singleton("apple"), strings);
        assertEquals(expected, result);

        expected = false;
        result = StringUtil.in(Strings.Singleton("orange"), strings);
        assertEquals(expected, result);

        strings = new String[]{Strings.Empty, null};
        expected = false;
        result = StringUtil.in(Strings.Singleton("hello"), strings);
        assertEquals(expected, result);

        strings = new String[]{Strings.Empty, null, "hello"};
        expected = true;
        result = StringUtil.in(Strings.Singleton("hello"), strings);
        assertEquals(expected, result);

        strings = new String[]{"apple", "banana", "cherry", null, "apple"};
        expected = true;
        result = StringUtil.in(Strings.Singleton("apple"), strings);
        assertEquals(expected, result);

        strings = new String[]{Strings.Empty, "apple", null, "banana", "cherry", "apple"};
        expected = true;
        result = StringUtil.in(Strings.Singleton("apple"), strings);
        assertEquals(expected, result);

        strings = new String[]{"apple", "banana", "cherry", "apple", "orange"};
        expected = false;
        result = StringUtil.in(Strings.Singleton("orange"), strings);
        assertEquals(expected, result);

        strings = new String[]{"apple", "banana", "cherry", "apple", "orange"};
        expected = true;
        result = StringUtil.in(Arrays.asList("apple", "orange"), strings);
        assertEquals(expected, result);

        strings = new String[]{"apple", "banana", "cherry", "apple", "orange"};
        expected = true;
        result = StringUtil.inSorted(Strings.Singleton("apple"), strings);
        assertEquals(expected, result);

        strings = new String[]{"apple", "banana", "cherry", "apple", "orange"};
        expected = false;
        result = StringUtil.inSorted(Strings.Singleton("banana"), strings);
        assertEquals(expected, result);
    }

    @Test
    public void testResolve() {
        String baseUrl = "http://example.com/";
        String relUrl = "/foo";
        String expected = "http://example.com/foo";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/foo";
        relUrl = "/bar";
        expected = "http://example.com/bar";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/foo/";
        relUrl = "bar";
        expected = "http://example.com/foo/bar";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/foo/";
        relUrl = "../bar";
        expected = "http://example.com/bar";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/foo/";
        relUrl = "../../bar";
        expected = "http://example.com/bar";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/foo/";
        relUrl = "..";
        expected = "http://example.com/";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/foo/";
        relUrl = ".";
        expected = "http://example.com/foo/";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = null;
        relUrl = "/foo";
        expected = "/foo";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/foo/";
        relUrl = null;
        expected = baseUrl;
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveWithMalformedBaseUrl() {
        String baseUrl = "http:/example.com/foo/";
        String relUrl = "/bar";
        String expected = "";
        String result = "";

        try {
            result = StringUtil.resolve(baseUrl, relUrl);
        } catch (MalformedURLException e) {
            expected = "";
        }

        assertEquals(expected, result);
    }

    @Test
    public void testResolveWithMalformedRelUrl() {
        String baseUrl = "http://example.com/foo/";
        String relUrl = "//example.com/bar";
        String expected = "";
        String result = "";

        try {
            result = StringUtil.resolve(baseUrl, relUrl);
        } catch (MalformedURLException e) {
            expected = "";
        }

        assertEquals(expected, result);
    }

    @Test
    public void testResolveWithAbsoluteRelUrl() {
        String baseUrl = "http://example.com/foo/";
        String relUrl = "http://example.com/bar";
        String expected = "http://example.com/bar";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();
        StringBuilder sb3 = StringUtil.stringBuilder();

        assertSame(sb1, sb2);
        assertSame(sb1, sb3);

        sb1.append("hello");
        assertNotSame(sb1, sb2);
        assertNotSame(sb1, sb3);

        sb2.append("world");
        assertNotSame(sb1, sb2);
        assertNotSame(sb1, sb3);

        sb3.append("world");
        assertNotSame(sb1, sb3);
        assertSame(sb2, sb3);
    }

}
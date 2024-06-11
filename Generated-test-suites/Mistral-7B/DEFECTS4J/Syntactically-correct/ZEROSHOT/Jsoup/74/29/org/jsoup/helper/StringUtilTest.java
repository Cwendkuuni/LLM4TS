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
 * Test for StringUtil class.
 */
public class StringUtilTest {

    @Test
    public void testJoin() {
        List<String> strings = Arrays.asList("Hello", "World", "!", "Java", "Testing");
        String expected = "HelloWorld!JavaTesting";
        String result = StringUtil.join(strings, "");
        assertEquals(expected, result);

        List<String> emptyList = Collections.emptyList();
        String emptyResult = "";
        result = StringUtil.join(emptyList, "");
        assertEquals(emptyResult, result);
    }

    @Test
    public void testJoinIterator() {
        List<String> strings = Arrays.asList("Hello", "World", "!", "Java", "Testing");
        Iterator<String> iterator = strings.iterator();
        String expected = "HelloWorld!JavaTesting";
        String result = StringUtil.join(iterator, "");
        assertEquals(expected, result);

        Iterator<String> emptyIterator = Collections.emptyIterator();
        String emptyResult = "";
        result = StringUtil.join(emptyIterator, "");
        assertEquals(emptyResult, result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = new String[]{"Hello", "World", "!", "Java", "Testing"};
        String expected = "HelloWorld!JavaTesting";
        String result = StringUtil.join(strings, "");
        assertEquals(expected, result);

        String[] emptyArray = {};
        String emptyResult = "";
        result = StringUtil.join(emptyArray, "");
        assertEquals(emptyResult, result);
    }

    @Test
    public void testPadding() {
        for (int i = 0; i < 22; i++) {
            String result = StringUtil.padding(i);
            String expected = StringUtil.padding(21 - i);
            assertEquals(expected, result);
        }
    }

    @Test
    public void testIsBlank() {
        String emptyString = "";
        assertTrue(StringUtil.isBlank(emptyString));
        assertFalse(StringUtil.isBlank("Hello World"));
        assertTrue(StringUtil.isBlank("   "));
        assertFalse(StringUtil.isBlank("Hello"));
    }

    @Test
    public void testIsNumeric() {
        String numberString = "12345";
        assertTrue(StringUtil.isNumeric(numberString));

        String emptyString = "";
        assertFalse(StringUtil.isNumeric(emptyString));

        String nonNumericString = "Hello";
        assertFalse(StringUtil.isNumeric(nonNumericString));

        String stringWithDigitAndLetter = "A1";
        assertFalse(StringUtil.isNumeric(stringWithDigitAndLetter));
    }

    @Test
    public void testIsWhitespace() {
        int whitespaceCodePoints = '\u0020', tabCodePoint = '\t', newLineCodePoint = '\n', formFeedCodePoint = '\f',
                carriageReturnCodePoint = '\r';
        assertTrue(StringUtil.isWhitespace(whitespaceCodePoints));
        assertTrue(StringUtil.isWhitespace(tabCodePoint));
        assertTrue(StringUtil.isWhitespace(newLineCodePoint));
        assertTrue(StringUtil.isWhitespace(formFeedCodePoint));
        assertTrue(StringUtil.isWhitespace(carriageReturnCodePoint));

        int nonWhitespaceCodePoint = 'A';
        assertFalse(StringUtil.isWhitespace(nonWhitespaceCodePoint));
    }

    @Test
    public void testIsActuallyWhitespace() {
        int spaceCodePoint = '\u0020';
        int tabCodePoint = '\t';
        int newLineCodePoint = '\n';
        int formFeedCodePoint = '\f';
        int carriageReturnCodePoint = '\r';
        int nonBreakingSpaceCodePoint = '\u00A0';

        assertTrue(StringUtil.isActuallyWhitespace(spaceCodePoint));
        assertTrue(StringUtil.isActuallyWhitespace(tabCodePoint));
        assertTrue(StringUtil.isActuallyWhitespace(newLineCodePoint));
        assertTrue(StringUtil.isActuallyWhitespace(formFeedCodePoint));
        assertTrue(StringUtil.isActuallyWhitespace(carriageReturnCodePoint));
        assertTrue(StringUtil.isActuallyWhitespace(nonBreakingSpaceCodePoint));

        int nonWhitespaceCodePoint = 'A';
        assertFalse(StringUtil.isActuallyWhitespace(nonWhitespaceCodePoint));
    }

    @Test
    public void testNormaliseWhitespace() {
        String input = "  Hello     World!\n\t12345\n  ";
        String expected = "Hello World!12345";
        String result = StringUtil.normaliseWhitespace(input);
        assertEquals(expected, result);
    }

    @Test
    public void testIn() {
        String needle = "World";
        String[] haystack = {"Hello", "World", "!", "Java", "Testing", "World"};
        assertTrue(StringUtil.in(needle, haystack));

        needle = "Java";
        assertFalse(StringUtil.in(needle, haystack));
    }

    @Test
    public void testInSorted() {
        String needle = "World";
        String[] haystack = {"Hello", "Testing", "World", "Java"};
        assertTrue(StringUtil.inSorted(needle, haystack));

        needle = "Java";
        assertFalse(StringUtil.inSorted(needle, haystack));

        needle = "Testing";
        assertTrue(StringUtil.inSorted(needle, haystack));
    }

    @Test
    public void testResolve() throws MalformedURLException {
        String baseUrl = "http://example.com";
        String relUrl = "/path/to/file.html";
        String expected = "http://example.com/path/to/file.html";
        URL result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected, result.toExternalForm());

        baseUrl = "http://example.com/page.html";
        relUrl = "../index.html";
        expected = "http://example.com/index.html";
        result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected, result.toExternalForm());

        baseUrl = "http://example.com/page.html";
        relUrl = "//example.org/index.html";
        expected = "http://example.org/index.html";
        result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected, result.toExternalForm());

        baseUrl = "http://example.com/page.html";
        relUrl = "./index.html";
        expected = "http://example.com/index.html";
        result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected, result.toExternalForm());

        baseUrl = "http://example.com/page.html";
        relUrl = "path/to/file.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(relUrl, result);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveException() throws MalformedURLException {
        String baseUrl = "http://example.com";
        String relUrl = "";
        StringUtil.resolve(new URL(baseUrl), relUrl);
    }
}
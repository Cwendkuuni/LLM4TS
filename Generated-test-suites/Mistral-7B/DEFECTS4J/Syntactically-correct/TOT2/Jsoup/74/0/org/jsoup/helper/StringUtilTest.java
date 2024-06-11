package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringUtilTest {

    @Test
    public void testJoin() {
        List<String> strings = Arrays.asList("apple", "banana", "cherry");
        String expected = "applebananacherry";
        String result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinEmptyCollection() {
        List<String> strings = Collections.emptyList();
        String result = StringUtil.join(strings, "");
        Assert.assertEquals("", result);
    }

    @Test
    public void testJoinNullIterator() {
        String result = StringUtil.join((Iterator<String>) null, "");
        Assert.assertEquals("", result);
    }

    @Test
    public void testJoinArrayNullElement() {
        String[] strings = new String[]{"apple", null, "cherry"};
        String expected = "applecherry";
        String result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinArrayEmptyElement() {
        String[] strings = new String[]{"apple", "", "cherry"};
        String expected = "applecherry";
        String result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPaddingNegativeWidth() {
        try {
            String result = StringUtil.padding(-1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testPaddingZeroWidth() {
        String result = StringUtil.padding(0);
        Assert.assertEquals("", result);
    }

    @Test
    public void testIsBlankNullString() {
        boolean result = StringUtil.isBlank(null);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsBlankEmptyString() {
        boolean result = StringUtil.isBlank("");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsBlankWhitespaceString() {
        String string = "   ";
        boolean result = StringUtil.isBlank(string);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsBlankNonWhitespaceString() {
        String string = "hello";
        boolean result = StringUtil.isBlank(string);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsNumericNullString() {
        boolean result = StringUtil.isNumeric(null);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsNumericEmptyString() {
        boolean result = StringUtil.isNumeric("");
        Assert.assertFalse(result);
    }

    @Test
    public void testIsNumericAllDigits() {
        String string = "12345";
        boolean result = StringUtil.isNumeric(string);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsNumericLetter() {
        String string = "1a2";
        boolean result = StringUtil.isNumeric(string);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsWhitespaceSpace() {
        boolean result = StringUtil.isWhitespace(' ');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsWhitespaceTab() {
        boolean result = StringUtil.isWhitespace('\t');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsWhitespaceNewline() {
        boolean result = StringUtil.isWhitespace('\n');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsWhitespaceFormFeed() {
        boolean result = StringUtil.isWhitespace('\f');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsWhitespaceCarriageReturn() {
        boolean result = StringUtil.isWhitespace('\r');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceSpace() {
        boolean result = StringUtil.isActuallyWhitespace(' ');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceTab() {
        boolean result = StringUtil.isActuallyWhitespace('\t');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceNewline() {
        boolean result = StringUtil.isActuallyWhitespace('\n');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceFormFeed() {
        boolean result = StringUtil.isActuallyWhitespace('\f');
        Assert.assertTrue(result);
    }

    @Test   
    public void testIsActuallyWhitespaceCarriageReturn() {
        boolean result = StringUtil.isActuallyWhitespace('\r');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsActuallyWhitespaceNonBreakingSpace() {
        boolean result = StringUtil.isActuallyWhitespace(160);
        Assert.assertTrue(result);
    }

    @Test
    public void testNormaliseWhitespaceSingleSpace() {
        String input = " a ";
        String expected = " a ";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceLeadingSpace() {
        String input = " a  ";
        String expected = " a ";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceTrailingSpace() {
        String input = " a ";
        String expected = " a ";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceMultipleSpaces() {
        String input = " a   b   c ";
        String expected = " a b c ";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceLeadingAndTrailingSpaces() {
        String input = "   a   ";
        String expected = " a ";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceNonBreakingSpace() {
        String input = " a &nbsp; b ";
        String expected = " a b ";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testInNullNeedle() {
        String[] haystack = new String[]{"apple", "banana", "cherry"};
        boolean result = StringUtil.in(null, haystack);
        Assert.assertFalse(result);
    }

    @Test
    public void testInEmptyNeedle() {
        String[] haystack = new String[]{"apple", "banana", "cherry"};
        boolean result = StringUtil.in("", haystack);
        Assert.assertFalse(result);
    }

    @Test
    public void testInEmptyHaystack() {
        String needle = "apple";
        boolean result = StringUtil.in(needle, (String[]) null);
        Assert.assertFalse(result);
    }

    @Test
    public void testInMatchingString() {
        String needle = "apple";
        String[] haystack = new String[]{"apple", "banana", "cherry"};
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertTrue(result);
    }

    @Test
    public void testInNonMatchingString() {
        String needle = "banana";
        String[] haystack = new String[]{"apple", "banana", "cherry"};
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertFalse(result);
    }

    @Test
    public void testInSortedMatchingString() {
        String needle = "apple";
        String[] haystack = new String[]{"apple", "banana", "cherry"};
        boolean result = StringUtil.inSorted(needle, haystack);
        Assert.assertTrue(result);
    }

    @Test
    public void testInSortedNonMatchingString() {
        String needle = "banana";
        String[] haystack = new String[]{"apple", "banana", "cherry"};
        boolean result = StringUtil.inSorted(needle, haystack);
        Assert.assertFalse(result);
    }

    @Test
    public void testResolveNullBaseURL() {
        String relUrl = "/test.html";
        String result = StringUtil.resolve(null, relUrl);
        Assert.assertEquals("", result);
    }

    @Test
    public void testResolveEmptyBaseURL() {
        String relUrl = "/test.html";
        String result = StringUtil.resolve("", relUrl);
        Assert.assertEquals("/test.html", result);
    }

    @Test
    public void testResolveAbsoluteBaseURL() {
        String relUrl = "/test.html";
        String baseUrl = "http://example.com/";
        String result = StringUtil.resolve(baseUrl, relUrl);
        String expected = "http://example.com/test.html";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveRelativeBaseURL() {
        String relUrl = "test.html";
        String baseUrl = "http://example.com/path/to/";
        String result = StringUtil.resolve(baseUrl, relUrl);
        String expected = "http://example.com/path/to/test.html";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveAbsoluteRelURL() {
        String relUrl = "/test.html";
        String baseUrl = "http://example.com/path/to/test.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(baseUrl, result);
    }

    @Test
    public void testResolveMalformedURLBase() {
        String relUrl = "/test.html";
        String baseUrl = "http://example.com";
        try {
            String result = StringUtil.resolve(baseUrl, relUrl);
            Assert.fail();
        } catch (MalformedURLException e) {
            // expected
        }
    }

    @Test
    public void testResolveMalformedURLRel() {
        String baseUrl = "http://example.com/path/to/test.html";
        String relUrl = "//example.com/test2.html";
        try {
            String result = StringUtil.resolve(baseUrl, relUrl);
            Assert.fail();
        } catch (MalformedURLException e) {
            // expected
        }
    }

    @Test
    public void testResolveInvalidSchemeBase() {
        String relUrl = "/test.html";
        String baseUrl = "data:example.com/path/to/test.html";
        try {
            String result = StringUtil.resolve(baseUrl, relUrl);
            Assert.fail();
        } catch (MalformedURLException e) {
            // expected
        }
    }

    @Test
    public void testStringBuilderCached() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();
        StringBuilder sb3 = StringUtil.stringBuilder();
        StringBuilder sb4 = StringUtil.stringBuilder();

        sb1.append("test1");
        sb2.append("test2");
        sb3.append("test3");

        Assert.assertEquals("test1", sb1.toString());
        Assert.assertEquals("test2", sb2.toString());
        Assert.assertEquals("test3", sb3.toString());

        Assert.assertEquals("test1test2test3", sb1.append(sb2).append(sb3).toString());
    }
}
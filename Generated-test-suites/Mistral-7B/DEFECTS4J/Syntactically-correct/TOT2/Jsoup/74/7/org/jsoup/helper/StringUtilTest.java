package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;

public class StringUtilTest {

 // Testing join method with Collection as an argument
 @Test
 public void testJoinCollection() {
 Collection<String> strings = Arrays.asList("Hello", "World", "Java");
 String expected = "HelloWorldJava";
 String result = StringUtil.join(strings, "");
 assertEquals(expected, result);
 }

 // Testing join method with Iterator as an argument
 @Test
 public void testJoinIterator() {
 Iterator<String> iterator = Arrays.asList("Hello", "World", "Java").iterator();
 String expected = "HelloWorldJava";
 String result = StringUtil.join(iterator, "");
 assertEquals(expected, result);
 }

 // Testing join method with Array as an argument
 @Test
 public void testJoinArray() {
 String[] strings = {"Hello", "World", "Java"};
 String expected = "HelloWorldJava";
 String result = StringUtil.join(strings, "");
 assertEquals(expected, result);
 }

 // Testing padding method with positive width
 @Test
 public void testPaddingPositive() {
 String result = StringUtil.padding(5);
 assertEquals("   ", result);
 }

 // Testing padding method with negative width
 @Test(expected = IllegalArgumentException.class)
 public void testPaddingNegative() {
 StringUtil.padding(-5);
 }

 // Testing isBlank method with null string
 @Test
 public void testIsBlankNull() {
 assertTrue(StringUtil.isBlank(null));
 }

 // Testing isBlank method with empty string
 @Test
 public void testIsBlankEmpty() {
 assertTrue(StringUtil.isBlank(""));
 }

 // Testing isBlank method with whitespace string
 @Test
 public void testIsBlankWhitespace() {
 String string = "   ";
 assertTrue(StringUtil.isBlank(string));
 }

 // Testing isNumeric method with numeric string
 @Test
 public void testIsNumericNumeric() {
 String string = "12345";
 assertTrue(StringUtil.isNumeric(string));
 }

 // Testing isNumeric method with non-numeric string
 @Test
 public void testIsNumericNonNumeric() {
 String string = "abcd";
 assertFalse(StringUtil.isNumeric(string));
 }

 // Testing isWhitespace method with whitespace code point
 @Test
 public void testIsWhitespaceWhitespace() {
 int codePoint = ' ';
 assertTrue(StringUtil.isWhitespace(codePoint));
 }

 // Testing isWhitespace method with non-whitespace code point
 @Test
 public void testIsWhitespaceNonWhitespace() {
 int codePoint = 'a';
 assertFalse(StringUtil.isWhitespace(codePoint));
 }

 // Testing isActuallyWhitespace method with whitespace code point
 @Test
 public void testIsActuallyWhitespaceWhitespace() {
 int codePoint = ' ';
 assertTrue(StringUtil.isActuallyWhitespace(codePoint));
 }

 // Testing isActuallyWhitespace method with non-whitespace code point
 @Test
 public void testIsActuallyWhitespaceNonWhitespace() {
 int codePoint = 'a';
 assertFalse(StringUtil.isActuallyWhitespace(codePoint));
 }

 // Testing normaliseWhitespace method with empty string
 @Test
 public void testNormaliseWhitespaceEmpty() {
 String result = StringUtil.normaliseWhitespace("");
 assertEquals("", result);
 }

 // Testing normaliseWhitespace method with whitespace string
 @Test
 public void testNormaliseWhitespaceWhitespace() {
 String string = "   Hello World   ";
 String expected = " Hello World ";
 String result = StringUtil.normaliseWhitespace(string);
 assertEquals(expected, result);
 }

 // Testing in method
 @Test
 public void testIn() {
 String needle = "Hello";
 String[] haystack = {"Hello World", "Java", "Hello"};
 assertTrue(StringUtil.in(needle, haystack));
 }

 // Testing in method with empty array
 @Test
 public void testInEmptyArray() {
 String needle = "Hello";
 String[] haystack = {};
 assertFalse(StringUtil.in(needle, haystack));
 }

 // Testing in method with null needle or haystack
 @Test
 public void testInNull() {
 String needle = null;
 String[] haystack = {"Hello World", "Java", "Hello"};
 assertFalse(StringUtil.in(needle, haystack));
 StringUtil.in(null, haystack);
 assertFalse(StringUtil.in(needle, null));
 }

 // Testing inSorted method
 @Test
 public void testInSorted() {
 String needle = "Hello";
 String[] haystack = {"Java", "Hello World", "Hello"};
 assertTrue(StringUtil.inSorted(needle, haystack));
 }

 // Testing inSorted method with empty array
 @Test
 public void testInSortedEmptyArray() {
 String needle = "Hello";
 String[] haystack = {};
 assertFalse(StringUtil.inSorted(needle, haystack));
 }

 // Testing normaliseWhitespace and in method
 @Test
 public void testNormaliseWhitespaceAndIn() {
 String string = "   Hello World   ";
 String needle = "Hello";
 String[] haystack = {"Java", "Hello World", "Hello"};
 String normalisedString = StringUtil.normaliseWhitespace(string);
 String expectedNeedle = StringUtil.normaliseWhitespace(needle);
 assertTrue(StringUtil.inSorted(expectedNeedle, haystack));
 assertTrue(StringUtil.in(needle, haystack));
 assertTrue(StringUtil.inSorted(normalisedString, haystack));
 assertTrue(StringUtil.in(normalisedString, haystack));
 }

 // Testing resolve method with absolute base URL and relative URL
 @Test
 public void testResolveAbsoluteBaseAndRelative() {
 String baseUrl = "http://example.com/";
 String relUrl = "/test.html";
 String expected = "http://example.com/test.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);
 }

 // Testing resolve method with absolute base URL and relative URL containing query string
 @Test
 public void testResolveAbsoluteBaseAndRelativeWithQueryString() {
 String baseUrl = "http://example.com/";
 String relUrl = "/test.html?foo=bar";
 String expected = "http://example.com/test.html?foo=bar";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);
 }

 // Testing resolve method with absolute base URL and absolute relative URL
 @Test
 public void testResolveAbsoluteBaseAndAbsoluteRelative() {
 String baseUrl = "http://example.com/";
 String relUrl = "http://example.com/test.html";
 String expected = "http://example.com/test.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);
 }

 // Testing resolve method with absolute base URL and empty relative URL
 @Test
 public void testResolveAbsoluteBaseAndEmptyRelative() {
 String baseUrl = "http://example.com/";
 String relUrl = "";
 String expected = "http://example.com/";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);
 }

 // Testing resolve method with absolute base URL and null relative URL
 @Test
 public void testResolveAbsoluteBaseAndNullRelative() {
 String baseUrl = "http://example.com/";
 String relUrl = null;
 String expected = "";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);
 }

 // Testing resolve method with absolute base URL and invalid relative URL
 @Test(expected = MalformedURLException.class)
 public void testResolveAbsoluteBaseAndInvalidRelative() {
 String baseUrl = "http://example.com/";
 String relUrl = "/test/../../test.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 }

 // Testing resolve method with base URL as empty string and relative URL
 @Test(expected = MalformedURLException.class)
 public void testResolveEmptyBaseAndRelative() {
 String baseUrl = "";
 String relUrl = "/test.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 }

 // Testing resolve method with base URL as empty string and empty relative URL
 @Test
 public void testResolveEmptyBaseAndEmptyRelative() {
 String baseUrl = "";
 String relUrl = "";
 String expected = "";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);
 }

 // Testing resolve method with base URL as null and relative URL
 @Test(expected = MalformedURLException.class)
 public void testResolveNullBaseAndRelative() {
 String baseUrl = null;
 String relUrl = "/test.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 }

 // Testing resolve method with base URL as null and empty relative URL
 @Test
 public void testResolveNullBaseAndEmptyRelative() {
 String baseUrl = null;
 String relUrl = "";
 String expected = "";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);
 }

 // Testing resolve method with base URL as null and null relative URL
 @Test
 public void testResolveNullBaseAndNullRelative() {
 String baseUrl = null;
 String relUrl = null;
 String expected = "";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);
 }

 // Testing resolve method with base URL as an invalid URL
 @Test(expected = MalformedURLException.class)
 public void testResolveInvalidBaseAndRelative() {
 String baseUrl = "javascript:alert('Hello World')";
 String relUrl = "/test.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 }

 // Testing resolve method with base URL as an invalid URL and empty relative URL
 @Test
 public void testResolveInvalidBaseAndEmptyRelative() {
 String baseUrl = "javascript:alert('Hello World')";
 String relUrl = "";
 String expected = "";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);
 }

 // Testing resolve method with base URL as an invalid URL and null relative URL
 @Test
 public void testResolveInvalidBaseAndNullRelative() {
 String baseUrl = "javascript:alert('Hello World')";
 String relUrl = null;
 String expected = "";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, result);
 }

 // Testing resolve method with base URL as an invalid URL and invalid relative URL
 @Test(expected = MalformedURLException.class)
 public void testResolveInvalidBaseAndInvalidRelative() {
 String baseUrl = "javascript:alert('Hello World')";
 String relUrl = "/test/../../test.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 }

}
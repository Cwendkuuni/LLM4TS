package org.jsoup.helper.test;

import org.junit.Test;
import org.jsoup.helper.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class StringUtilTest {

 @Test
 public void testJoinCollection() {
 String sep = ",";
 List<String> strings = Arrays.asList("hello", "world", "!");
 String result = StringUtil.join(strings, sep);
 assertEquals("hello,world,!", result);
 }

 @Test
 public void testJoinEmptyCollection() {
 String sep = "";
 List<String> strings = Collections.emptyList();
 String result = StringUtil.join(strings, sep);
 assertEquals("", result);
 }

 @Test
 public void testJoinNullCollection() {
 String sep = "";
 List<String> strings = null;
 String result = StringUtil.join(strings, sep);
 assertNull(result);
 }

 @Test
 public void testJoinIterator() {
 String sep = "|";
 Iterator<String> strings = Arrays.asList("hello", "world", "!").iterator();
 String result = StringUtil.join(strings, sep);
 assertEquals("hello|world|!", result);
 }

 @Test
 public void testJoinArray() {
 String sep = "-";
 String[] strings = {"hello", "world", "!"};
 String result = StringUtil.join(strings, sep);
 assertEquals("hello-world-!", result);
 }

 @Test
 public void testPadding() {
 int width = 10;
 String result = StringUtil.padding(width);
 assertEquals("         ", result);
 }

 @Test
 public void testIsBlankNull() {
 String string = null;
 boolean result = StringUtil.isBlank(string);
 assertTrue(result);
 }

 @Test
 public void testIsBlankEmpty() {
 String string = "";
 boolean result = StringUtil.isBlank(string);
 assertTrue(result);
 }

 @Test
 public void testIsBlankWhitespace() {
 String string = "   ";
 boolean result = StringUtil.isBlank(string);
 assertTrue(result);
 }

 @Test
 public void testIsBlankNonWhitespace() {
 String string = "hello";
 boolean result = StringUtil.isBlank(string);
 assertFalse(result);
 }

 @Test
 public void testIsNumericNull() {
 String string = null;
 boolean result = StringUtil.isNumeric(string);
 assertFalse(result);
 }

 @Test
 public void testIsNumericEmpty() {
 String string = "";
 boolean result = StringUtil.isNumeric(string);
 assertFalse(result);
 }

 @Test
 public void testIsNumericDigits() {
 String string = "12345";
 boolean result = StringUtil.isNumeric(string);
 assertTrue(result);
 }

 @Test
 public void testIsNumericDigitsAndNonDigits() {
 String string = "123a45";
 boolean result = StringUtil.isNumeric(string);
 assertFalse(result);
 }

 @Test
 public void testIsWhitespaceSpace() {
 int codePoint = ' ';
 boolean result = StringUtil.isWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testIsWhitespaceTab() {
 int codePoint = '\t';
 boolean result = StringUtil.isWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testIsWhitespaceNewLine() {
 int codePoint = '\n';
 boolean result = StringUtil.isWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testIsWhitespaceFormFeed() {
 int codePoint = '\f';
 boolean result = StringUtil.isWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testIsWhitespaceCarriageReturn() {
 int codePoint = '\r';
 boolean result = StringUtil.isWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testIsActuallyWhitespaceSpace() {
 int codePoint = ' ';
 boolean result = StringUtil.isActuallyWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testIsActuallyWhitespaceTab() {
 int codePoint = '\t';
 boolean result = StringUtil.isActuallyWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testIsActuallyWhitespaceNewLine() {
 int codePoint = '\n';
 boolean result = StringUtil.isActuallyWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testIsActuallyWhitespaceFormFeed() {
 int codePoint = '\f';
 boolean result = StringUtil.isActuallyWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testIsActuallyWhitespaceCarriageReturn() {
 int codePoint = '\r';
 boolean result = StringUtil.isActuallyWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testIsActuallyWhitespaceNonBreakingSpace() {
 int codePoint = '\u00A0';
 boolean result = StringUtil.isActuallyWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testNormaliseWhitespace() {
 String string = " hello \t world\n";
 String result = StringUtil.normaliseWhitespace(string);
 assertEquals("hello world", result);
 }

 @Test
 public void testInNull() {
 String needle = null;
 String[] haystack = {"hello", "world"};
 boolean result = StringUtil.in(needle, haystack);
 assertFalse(result);
 }

 @Test
 public void testInEmptyArray() {
 String needle = "hello";
 String[] haystack = {};
 boolean result = StringUtil.in(needle, haystack);
 assertFalse(result);
 }

 @Test
 public void testInSingleElementArray() {
 String needle = "hello";
 String[] haystack = {"hello"};
 boolean result = StringUtil.in(needle, haystack);
 assertTrue(result);
 }

 @Test
 public void testInMultipleElementsArray() {
 String needle = "hello";
 String[] haystack = {"world", "hello", "goodbye"};
 boolean result = StringUtil.in(needle, haystack);
 assertTrue(result);
 }

 @Test
 public void testInSortedNull() {
 String needle = null;
 String[] haystack = {"hello", "world"};
 boolean result = StringUtil.inSorted(needle, haystack);
 assertFalse(result);
 }

 @Test
 public void testInSortedEmpty() {
 String needle = "hello";
 String[] haystack = {};
 boolean result = StringUtil.inSorted(needle, haystack);
 assertFalse(result);
 }

 @Test
 public void testInSortedSingleElement() {
 String needle = "hello";
 String[] haystack = {"hello"};
 boolean result = StringUtil.inSorted(needle, haystack);
 assertTrue(result);
 }

 @Test
 public void testInSortedMultipleElements() {
 String needle = "hello";
 String[] haystack = {"world", "hello", "goodbye"};
 boolean result = StringUtil.inSorted(needle, haystack);
 assertTrue(result);
 }

 @Test
 public void testResolveBaseUrlAbsoluteRelUrl() throws MalformedURLException {
 String baseUrl = "http://example.com/path/to/file.html";
 String relUrl = "/newfile.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("http://example.com/newfile.html", result);
 }

 @Test
 public void testResolveBaseUrlRelativeUrl() throws MalformedURLException {
 String baseUrl = "http://example.com/path/to/file.html";
 String relUrl = "../newfile.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("http://example.com/newfile.html", result);
 }

 @Test
 public void testResolveNullBaseUrl() throws MalformedURLException {
 String relUrl = "http://example.com/newfile.html";
 String result = StringUtil.resolve(null, relUrl);
 assertEquals("", result);
 }

 @Test
 public void testResolveNullRelUrl() throws MalformedURLException {
 String baseUrl = "http://example.com/path/to/file.html";
 String result = StringUtil.resolve(baseUrl, null);
 assertEquals("", result);
 }

 @Test
 public void testResolveMalformedBaseUrl() throws MalformedURLException {
 String baseUrl = "http://example.com//path/to/file.html";
 String relUrl = "/newfile.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("", result);
 }

 @Test
 public void testResolveMalformedRelUrl() throws MalformedURLException {
 String baseUrl = "http://example.com/path/to/file.html";
 String relUrl = "//newfile.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("", result);
 }

 @Test
 public void testResolveRelativeUrlStartsWithSlash() throws MalformedURLException {
 String baseUrl = "http://example.com/path/to/file.html";
 String relUrl = "/newfile.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("http://example.com/newfile.html", result);
 }

 @Test
 public void testResolveRelativeUrlEndsWithSlash() throws MalformedURLException {
 String baseUrl = "http://example.com/path/to/file.html";
 String relUrl = "/newfile.html/";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("http://example.com/newfile.html/", result);
 }

 @Test
 public void testResolveRelativeUrlEndsWithQueryParam() throws MalformedURLException {
 String baseUrl = "http://example.com/path/to/file.html?foo=bar";
 String relUrl = "/newfile.html?baz=qux";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("http://example.com/newfile.html?baz=qux", result);
 }

 @Test
 public void testResolveRelativeUrlEndsWithFragment() throws MalformedURLException {
 String baseUrl = "http://example.com/path/to/file.html#fragment";
 String relUrl = "/newfile.html#fragment2";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("http://example.com/newfile.html#fragment2", result);
 }

 @Test
 public void testResolveRelativeUrlStartsWithQuestionMark() throws MalformedURLException {
 String baseUrl = "http://example.com/path/to/file.html";
 String relUrl = "?newfile.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("http://example.com/newfile.html", result);
 }

 @Test
 public void testResolveRelativeUrlStartsWithHash() throws MalformedURLException {
 String baseUrl = "http://example.com/path/to/file.html";
 String relUrl = "#newfile.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("http://example.com/#newfile.html", result);
 }

 @Test
 public void testResolveRelativeUrlContainsQuestionMark() throws MalformedURLException {
 String baseUrl = "http://example.com/path/to/file.html?foo=bar";
 String relUrl = "/newfile.html?baz=qux";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("http://example.com/newfile.html?baz=qux", result);
 }

 @Test
 public void testResolveRelativeUrlContainsHash() throws MalformedURLException {
 String baseUrl = "http://example.com/path/to/file.html#fragment";
 String relUrl = "/newfile.html#fragment2";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("http://example.com/newfile.html#fragment2", result);
 }

 @Test
 public void testResolveRelativeUrlDifferentProtocol() throws MalformedURLException {
 String baseUrl = "http://example.com/path/to/file.html";
 String relUrl = "https://example.com/newfile.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("", result);
 }

 @Test
 public void testResolveRelativeUrlDifferentDomain() throws MalformedURLException {
 String baseUrl = "http://example.com/path/to/file.html";
 String relUrl = "http://anotherexample.com/newfile.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("", result);
 }

 @Test
 public void testResolveRelativeUrlRelativeToAnotherBase() throws MalformedURLException {
 String baseUrl = "http://example.com/path/to/file.html";
 String base2Url = "http://example.com/another/path/to/file.html";
 String relUrl = "/newfile.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("http://example.com/newfile.html", result);
 String result2 = StringUtil.resolve(base2Url, relUrl);
 assertEquals("http://example.com/newfile.html", result2);
 }

 @Test
 public void testStringBuilder() {
 StringBuilder stringBuilder = StringUtil.stringBuilder();
 stringBuilder.append("hello");
 assertEquals("hello", stringBuilder.toString());
 }

 @Test
 public void testStringBuilderCapacity() {
 StringBuilder stringBuilder = StringUtil.stringBuilder();
 stringBuilder.append("hello");
 stringBuilder.append("world");
 assertEquals("helloworld", stringBuilder.toString());
 }

 @Test
 public void testStringBuilderClear() {
 StringBuilder stringBuilder = StringUtil.stringBuilder();
 stringBuilder.append("hello");
 stringBuilder.clear();
 assertEquals("", stringBuilder.toString());
 }

 @Test
 public void testStringBuilderLength() {
 StringBuilder stringBuilder = StringUtil.stringBuilder();
 stringBuilder.append("hello");
 stringBuilder.append("world");
 assertEquals(11, stringBuilder.length());
 }

 @Test
 public void testStringBuilderCapacityLimit() {
 StringBuilder stringBuilder = StringUtil.stringBuilder();
 stringBuilder.append("a".repeat(MaxCachedBuilderSize + 1));
 assertEquals(MaxCachedBuilderSize, stringBuilder.capacity());
 }

 @Test
 public void testStringBuilderLargeCapacityLimit() {
 StringBuilder stringBuilder = StringUtil.stringBuilder();
 stringBuilder.append("a".repeat(MaxCachedBuilderSize * 2));
 assertEquals(MaxCachedBuilderSize * 2, stringBuilder.capacity());
 }
}
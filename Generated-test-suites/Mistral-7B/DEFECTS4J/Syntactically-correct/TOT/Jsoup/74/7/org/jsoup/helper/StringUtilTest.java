package org.jsoup.helper;

import org.junit.Test;
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

public class StringUtilTest {

 @Test
 public void testJoinCollection() {
 Collection<String> strings = Arrays.asList("Hello", "World", "!");
 String result = StringUtil.join(strings, ";");
 assertEquals("Hello;World;!", result);
 }

 @Test
 public void testJoinCollectionEmpty() {
 Collection<String> strings = Collections.emptyList();
 String result = StringUtil.join(strings, ";");
 assertEquals("", result);
 }

 @Test
 public void testJoinIteratorNullStrings() {
 Iterator<String> strings = null;
 String result = StringUtil.join(strings, ";");
 assertNull(result);
 }

 @Test
 public void testJoinIteratorEmpty() {
 Iterator<String> strings = Collections.emptyIterator();
 String result = StringUtil.join(strings, ";");
 assertEquals("", result);
 }

 @Test
 public void testJoinIteratorSingleString() {
 Iterator<String> strings = Arrays.asList("Hello").iterator();
 String result = StringUtil.join(strings, ";");
 assertEquals("Hello", result);
 }

 @Test
 public void testJoinIteratorMultipleStrings() {
 Iterator<String> strings = Arrays.asList("Hello", "World", "!").iterator();
 String result = StringUtil.join(strings, ";");
 assertEquals("Hello;World;!", result);
 }

 @Test
 public void testJoinArray() {
 String[] strings = new String[]{"Hello", "World", "!"};
 String result = StringUtil.join(strings, ";");
 assertEquals("Hello;World;!", result);
 }

 @Test
 public void testJoinArrayEmpty() {
 String[] strings = new String[0];
 String result = StringUtil.join(strings, ";");
 assertEquals("", result);
 }

 @Test
 public void testJoinArraySingleString() {
 String[] strings = new String[]{"Hello"};
 String result = StringUtil.join(strings, ";");
 assertEquals("Hello", result);
 }

 @Test
 public void testIsBlankNullString() {
 String string = null;
 boolean result = StringUtil.isBlank(string);
 assertTrue(result);
 }

 @Test
 public void testIsBlankEmptyString() {
 String string = "";
 boolean result = StringUtil.isBlank(string);
 assertTrue(result);
 }

 @Test
 public void testIsBlankWhitespace() {
 String string = " \t\r\n ";
 boolean result = StringUtil.isBlank(string);
 assertTrue(result);
 }

 @Test
 public void testIsBlankNonBlank() {
 String string = "abc123";
 boolean result = StringUtil.isBlank(string);
 assertFalse(result);
 }

 @Test
 public void testIsNumericTrue() {
 String string = "123";
 boolean result = StringUtil.isNumeric(string);
 assertTrue(result);
 }

 @Test
 public void testIsNumericFalse() {
 String string = "abc";
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
 public void testIsWhitespaceTrue() {
 char codePoint = ' ';
 boolean result = StringUtil.isWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testIsWhitespaceFalse() {
 char codePoint = 'A';
 boolean result = StringUtil.isWhitespace(codePoint);
 assertFalse(result);
 }

 @Test
 public void testIsActuallyWhitespaceTrue() {
 char codePoint = ' ';
 boolean result = StringUtil.isActuallyWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testIsActuallyWhitespaceFalse() {
 char codePoint = 'A';
 boolean result = StringUtil.isActuallyWhitespace(codePoint);
 assertFalse(result);
 }

 @Test
 public void testIsActuallyWhitespaceSpecial() {
 char codePoint = '\u00A0'; // non-breaking space
 boolean result = StringUtil.isActuallyWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testNormaliseWhitespaceEmpty() {
 String string = "";
 StringBuilder sb = StringUtil.stringBuilder();
 StringUtil.appendNormalisedWhitespace(sb, string, false);
 assertEquals("", sb.toString());
 }

 @Test
 public void testNormaliseWhitespaceSingleWhitespace() {
 String string = " ";
 StringBuilder sb = StringUtil.stringBuilder();
 StringUtil.appendNormalisedWhitespace(sb, string, false);
 assertEquals(" ", sb.toString());
 }

 @Test
 public void testNormaliseWhitespaceMultipleWhitespace() {
 String string = " \t\r\n ";
 StringBuilder sb = StringUtil.stringBuilder();
 StringUtil.appendNormalisedWhitespace(sb, string, false);
 assertEquals(" ", sb.toString());
 }

 @Test
 public void testNormaliseWhitespaceWhitespaceAndText() {
 String string = " Hello World    ";
 StringBuilder sb = StringUtil.stringBuilder();
 StringUtil.appendNormalisedWhitespace(sb, string, false);
 assertEquals(" Hello World ", sb.toString());
 }

 @Test
 public void testInSingleElement() {
 String needle = "test";
 String[] haystack = new String[]{"test"};
 boolean result = StringUtil.in(needle, haystack);
 assertTrue(result);
 }

 @Test
 public void testInMultipleElements() {
 String needle = "test";
 String[] haystack = new String[]{"test1", "test2", "test"};
 boolean result = StringUtil.in(needle, haystack);
 assertTrue(result);
 }

 @Test
 public void testInEmptyArray() {
 String needle = "test";
 String[] haystack = new String[0];
 boolean result = StringUtil.in(needle, haystack);
 assertFalse(result);
 }

 @Test
 public void testInNullArray() {
 String needle = "test";
 String[] haystack = null;
 boolean result = StringUtil.in(needle, haystack);
 assertFalse(result);
 }

 @Test
 public void testInSorted() {
 String needle = "test";
 String[] haystack = new String[]{"test1", "test2", "test"};
 boolean result = StringUtil.inSorted(needle, haystack);
 assertTrue(result);
 }

 @Test
 public void testInSortedNullNeedle() {
 String[] haystack = new String[]{"test1", "test2", "test"};
 boolean result = StringUtil.inSorted(null, haystack);
 assertFalse(result);
 }

 @Test
 public void testInSortedEmptyArray() {
 String needle = "test";
 boolean result = StringUtil.inSorted(needle, new String[0]);
 assertFalse(result);
 }

 @Test
 public void testInSortedNullArray() {
 String needle = "test";
 boolean result = StringUtil.inSorted(needle, null);
 assertFalse(result);
 }

 @Test
 public void testResolveAbsoluteNullBase() {
 String relUrl = "//example.com";
 String result = StringUtil.resolve(null, relUrl);
 assertNull(result);
 }

 @Test
 public void testResolveAbsoluteEmptyBase() {
 String relUrl = "//example.com";
 String result = StringUtil.resolve("", relUrl);
 assertNotNull(result);
 assertEquals("//example.com", result);
 }

 @Test
 public void testResolveAbsoluteEmptyRel() {
 String baseUrl = "http://example.com/";
 String result = StringUtil.resolve(baseUrl, "");
 assertNotNull(result);
 assertEquals(baseUrl, result);
 }

 @Test
 public void testResolveAbsoluteSelfRel() {
 String baseUrl = "http://example.com/";
 String relUrl = ".";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertNotNull(result);
 assertEquals(baseUrl, result);
 }

 @Test
 public void testResolveAbsoluteParentRel() {
 String baseUrl = "http://example.com/path/";
 String relUrl = "../../../";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("http://example.com", result);
 }

 @Test
 public void testResolveAbsoluteRelative() {
 String baseUrl = "http://example.com/path/";
 String relUrl = "relative.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("http://example.com/relative.html", result);
 }

 @Test
 public void testResolveAbsoluteAbsolute() {
 String baseUrl = "http://example.com/path/";
 String relUrl = "http://example.com/absolute.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(relUrl, result);
 }

 @Test(expected = MalformedURLException.class)
 public void testResolveMalformedBase() {
 String baseUrl = "//example.com";
 String relUrl = "relative.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 }

 @Test(expected = MalformedURLException.class)
 public void testResolveMalformedRel() {
 String baseUrl = "http://example.com/path/";
 String relUrl = "//example.com/absolute.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 }

 @Test
 public void testResolveRelativeWithQuery() {
 String baseUrl = "http://example.com/path/";
 String relUrl = "relative.html?query";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("http://example.com/relative.html?query", result);
 }

 @Test
 public void testResolveAbsoluteWithQuery() {
 String baseUrl = "http://example.com/path/";
 String relUrl = "absolute.html?query";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("http://example.com/absolute.html?query", result);
 }
}
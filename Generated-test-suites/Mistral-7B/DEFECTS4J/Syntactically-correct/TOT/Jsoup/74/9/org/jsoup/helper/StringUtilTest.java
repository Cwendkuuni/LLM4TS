package org.jsoup.helper;

import org.junit.Test;
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

public class StringUtilTest {

 @Test
 public void testJoin_EmptyCollection() {
 String result = StringUtil.join(Collections.emptyList(), "");
 assertEquals("", result);
 }

 @Test
 public void testJoin_OneElement() {
 String element = "Hello";
 String result = StringUtil.join(Arrays.asList(element), "");
 assertEquals(element, result);
 }

 @Test
 public void testJoin_MultipleElements() {
 String element1 = "Hello";
 String element2 = "World";
 String separator = "-";
 List<String> list = Arrays.asList(element1, element2);
 String result = StringUtil.join(list, separator);
 assertEquals(element1 + separator + element2, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJoin_NegativeWidth() {
 String result = StringUtil.padding(-1);
 assertNull(result);
 }

 @Test
 public void testJoin_ZeroWidth() {
 String result = StringUtil.padding(0);
 assertEquals("", result);
 }

 @Test
 public void testJoin_SmallWidth() {
 int width = 5;
 String result = StringUtil.padding(width);
 assertEquals("     ", result);
 }

 @Test
 public void testJoin_LargeWidth() {
 int width = 21;
 String result = StringUtil.padding(width);
 assertEquals(padding[width], result);
 }

 @Test
 public void testIsBlank_NullString() {
 String string = null;
 boolean result = StringUtil.isBlank(string);
 assertTrue(result);
 }

 @Test
 public void testIsBlank_EmptyString() {
 String string = "";
 boolean result = StringUtil.isBlank(string);
 assertTrue(result);
 }

 @Test
 public void testIsBlank_WhitespaceString() {
 String string = "   ";
 boolean result = StringUtil.isBlank(string);
 assertTrue(result);
 }

 @Test
 public void testIsBlank_NonBlankString() {
 String string = "HelloWorld";
 boolean result = StringUtil.isBlank(string);
 assertFalse(result);
 }

 @Test
 public void testIsNumeric_EmptyString() {
 String string = "";
 boolean result = StringUtil.isNumeric(string);
 assertFalse(result);
 }

 @Test
 public void testIsNumeric_NonNumericString() {
 String string = "abc";
 boolean result = StringUtil.isNumeric(string);
 assertFalse(result);
 }

 @Test
 public void testIsNumeric_NumericString() {
 String string = "123";
 boolean result = StringUtil.isNumeric(string);
 assertTrue(result);
 }

 @Test
 public void testIsWhitespace_Space() {
 int codePoint = ' ';
 boolean result = StringUtil.isWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testIsWhitespace_Tab() {
 int codePoint = '\t';
 boolean result = StringUtil.isWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testIsWhitespace_Newline() {
 int codePoint = '\n';
 boolean result = StringUtil.isWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testIsWhitespace_NonWhitespace() {
 int codePoint = 'a';
 boolean result = StringUtil.isWhitespace(codePoint);
 assertFalse(result);
 }

 @Test
 public void testIsActuallyWhitespace_Space() {
 int codePoint = ' ';
 boolean result = StringUtil.isActuallyWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testIsActuallyWhitespace_Tab() {
 int codePoint = '\t';
 boolean result = StringUtil.isActuallyWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testIsActuallyWhitespace_Newline() {
 int codePoint = '\n';
 boolean result = StringUtil.isActuallyWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testIsActuallyWhitespace_NonWhitespace() {
 int codePoint = 'a';
 boolean result = StringUtil.isActuallyWhitespace(codePoint);
 assertFalse(result);
 }

 @Test
 public void testIsActuallyWhitespace_NonBreakingSpace() {
 int codePoint = 160;
 boolean result = StringUtil.isActuallyWhitespace(codePoint);
 assertTrue(result);
 }

 @Test
 public void testNormaliseWhitespace_EmptyString() {
 String string = "";
 StringBuilder accum = StringUtil.stringBuilder();
 StringUtil.appendNormalisedWhitespace(accum, string, false);
 assertEquals("", accum.toString());
 }

 @Test
 public void testNormaliseWhitespace_SingleWhitespace() {
 String string = " ";
 StringBuilder accum = StringUtil.stringBuilder();
 StringUtil.appendNormalisedWhitespace(accum, string, false);
 assertEquals(" ", accum.toString());
 }

 @Test
 public void testNormaliseWhitespace_MultipleWhitespace() {
 String string = " \t\n\r\f";
 StringBuilder accum = StringUtil.stringBuilder();
 StringUtil.appendNormalisedWhitespace(accum, string, false);
 assertEquals(" ", accum.toString());
 }

 @Test
 public void testNormaliseWhitespace_MixedWhitespace() {
 String string = " Hello\tWorld\n";
 StringBuilder accum = StringUtil.stringBuilder();
 StringUtil.appendNormalisedWhitespace(accum, string, false);
 assertEquals("Hello World", accum.toString());
 }

 @Test
 public void testNormaliseWhitespace_LeadingWhitespace() {
 String string = "   HelloWorld";
 StringBuilder accum = StringUtil.stringBuilder();
 StringUtil.appendNormalisedWhitespace(accum, string, true);
 assertEquals("HelloWorld", accum.toString());
 }

 @Test
 public void testIn_NeedleIsInHaystack() {
 String haystack = "HelloWorld";
 String needle = "World";
 boolean result = StringUtil.in(needle, new String[]{haystack});
 assertTrue(result);
 }

 @Test
 public void testIn_NeedleIsNotInHaystack() {
 String haystack = "HelloWorld";
 String needle = "Hello";
 boolean result = StringUtil.in(needle, new String[]{haystack});
 assertFalse(result);
 }

 @Test
 public void testInSorted_NeedleIsInSortedHaystack() {
 String haystack = "apple banana cherry";
 String needle = "cherry";
 boolean result = StringUtil.inSorted(needle, new String[]{haystack.split(" ")});
 assertTrue(result);
 }

 @Test
 public void testInSorted_NeedleIsNotInSortedHaystack() {
 String haystack = "apple banana cherry";
 String needle = "orange";
 boolean result = StringUtil.inSorted(needle, new String[]{haystack.split(" ")});
 assertFalse(result);
 }

 @Test
 public void testResolve_BaseUrlIsAbsolute() throws MalformedURLException {
 String relUrl = "../path/file.html";
 String baseUrl = "http://example.com/index.html";
 String resolvedUrl = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("http://example.com/path/file.html", resolvedUrl);
 }

 @Test
 public void testResolve_BaseUrlIsRelative() throws MalformedURLException {
 String relUrl = "path/file.html";
 String baseUrl = "";
 String resolvedUrl = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("path/file.html", resolvedUrl);
 }

 @Test
 public void testResolve_RelativeUrlIsAbsolute() throws MalformedURLException {
 String relUrl = "http://example.com/file.html";
 String baseUrl = "http://example.com";
 String resolvedUrl = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("http://example.com/file.html", resolvedUrl);
 }

 @Test(expected = MalformedURLException.class)
 public void testResolve_MalformedBaseUrl() throws MalformedURLException {
 String relUrl = "http://example.com/file.html";
 String baseUrl = "example.com";
 String resolvedUrl = StringUtil.resolve(baseUrl, relUrl);
 }

 @Test(expected = MalformedURLException.class)
 public void testResolve_MalformedRelativeUrl() throws MalformedURLException {
 String baseUrl = "http://example.com/index.html";
 String relUrl = "../path/file.html";
 String resolvedUrl = StringUtil.resolve(baseUrl, relUrl);
 }

 @Test
 public void testResolve_BaseUrlAndRelativeUrlBothMalformed() throws MalformedURLException {
 String relUrl = "//example.com/file.html";
 String baseUrl = "example.com";
 String resolvedUrl = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("", resolvedUrl);
 }

 @Test
 public void testStringBuilder() {
 StringBuilder sb = StringUtil.stringBuilder();
 StringUtil.appendNormalisedWhitespace(sb, " test string ", false);
 StringBuilder expected = new StringBuilder(" test string ");
 StringUtil.appendNormalisedWhitespace(expected, " test string ", false);
 assertEquals(expected.toString(), sb.toString());
 }

}
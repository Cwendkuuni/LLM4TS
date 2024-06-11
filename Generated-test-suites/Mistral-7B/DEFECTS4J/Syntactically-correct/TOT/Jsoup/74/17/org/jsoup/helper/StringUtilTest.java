package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringUtilTest {

 @Test
 public void testJoinEmptyCollection() {
 String result = StringUtil.join(Collections.emptyList(), "");
 assertEquals("", result);
 }

 @Test
 public void testJoinIterableNull() {
 Iterator<String> iterator = null;
 String result = StringUtil.join(iterator, "");
 assertEquals("", result);
 }

 @Test
 public void testJoinNullString() {
 Iterator<String> iterator = Arrays.asList("Hello", "World").iterator();
 String result = StringUtil.join(iterator, null);
 assertEquals("", result);
 }

 @Test
 public void testJoinIterableEmpty() {
 Iterator<String> iterator = Arrays.asList().iterator();
 String result = StringUtil.join(iterator, "");
 assertEquals("", result);
 }

 @Test
 public void testJoinSingleString() {
 Iterator<String> iterator = Arrays.asList("Hello").iterator();
 String result = StringUtil.join(iterator, "");
 assertEquals("Hello", result);
 }

 @Test
 public void testJoinMultipleStrings() {
 Iterator<String> iterator = Arrays.asList("Hello", "World").iterator();
 String result = StringUtil.join(iterator, ":");
 assertEquals("Hello:World", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJoinNegativeWidth() {
 String result = StringUtil.padding(-1);
 fails("Should throw IllegalArgumentException for negative width");
 }

 @Test
 public void testPaddingZero() {
 String result = StringUtil.padding(0);
 assertEquals("", result);
 }

 @Test
 public void testPaddingOne() {
 String result = StringUtil.padding(1);
 assertEquals(" ", result);
 }

 @Test
 public void testPaddingTen() {
 String result = StringUtil.padding(10);
 assertEquals("         ", result);
 }

 @Test
 public void testIsBlankEmptyString() {
 boolean result = StringUtil.isBlank("");
 assertTrue(result);
 }

 @Test
 public void testIsBlankNullString() {
 boolean result = StringUtil.isBlank(null);
 assertTrue(result);
 }

 @Test
 public void testIsBlankWhitespace() {
 String text = "   ";
 boolean result = StringUtil.isBlank(text);
 assertTrue(result);
 }

 @Test
 public void testIsBlankOneCharacter() {
 String text = "a";
 boolean result = StringUtil.isBlank(text);
 assertFalse(result);
 }

 @Test
 public void testIsNumericEmptyString() {
 boolean result = StringUtil.isNumeric("");
 assertFalse(result);
 }

 @Test
 public void testIsNumericNullString() {
 boolean result = StringUtil.isNumeric(null);
 assertFalse(result);
 }

 @Test
 public void testIsNumericDigitsOnly() {
 String text = "1234567890";
 boolean result = StringUtil.isNumeric(text);
 assertTrue(result);
 }

 @Test
 public void testIsNumericLettersOnly() {
 String text = "abcdefghijklmnopqrstuvwxyz";
 boolean result = StringUtil.isNumeric(text);
 assertFalse(result);
 }

 @Test
 public void testIsNumericDigitsAndLetters() {
 String text = "1a2b3c4d5e";
 boolean result = StringUtil.isNumeric(text);
 assertFalse(result);
 }

 @Test
 public void testIsWhitespaceSpace() {
 boolean result = StringUtil.isWhitespace(' ');
 assertTrue(result);
 }

 @Test
 public void testIsWhitespaceTab() {
 boolean result = StringUtil.isWhitespace('\t');
 assertTrue(result);
 }

 @Test
 public void testIsWhitespaceNewline() {
 boolean result = StringUtil.isWhitespace('\n');
 assertTrue(result);
 }

 @Test
 public void testIsWhitespaceReturn() {
 boolean result = StringUtil.isWhitespace('\r');
 assertTrue(result);
 }

 @Test
 public void testIsWhitespaceNonWhitespace() {
 boolean result = StringUtil.isWhitespace('a');
 assertFalse(result);
 }

 @Test
 public void testIsActuallyWhitespaceSpace() {
 boolean result = StringUtil.isActuallyWhitespace(' ');
 assertTrue(result);
 }

 @Test
 public void testIsActuallyWhitespaceTab() {
 boolean result = StringUtil.isActuallyWhitespace('\t');
 assertTrue(result);
 }

 @Test
 public void testIsActuallyWhitespaceNewline() {
 boolean result = StringUtil.isActuallyWhitespace('\n');
 assertTrue(result);
 }

 @Test
 public void testIsActuallyWhitespaceReturn() {
 boolean result = StringUtil.isActuallyWhitespace('\r');
 assertTrue(result);
 }

 @Test
 public void testIsActuallyWhitespaceNonWhitespace() {
 boolean result = StringUtil.isActuallyWhitespace('a');
 assertFalse(result);
 }

 @Test
 public void testIsActuallyWhitespaceNbsp() {
 boolean result = StringUtil.isActuallyWhitespace('\u00A0');
 assertTrue(result);
 }

 @Test
 public void testNormaliseWhitespaceNoWhitespace() {
 String input = "";
 String result = StringUtil.normaliseWhitespace(input);
 assertEquals("", result);
 }

 @Test
 public void testNormaliseWhitespaceSingleSpace() {
 String input = " ";
 String result = StringUtil.normaliseWhitespace(input);
 assertEquals(" ", result);
 }

 @Test
 public void testNormaliseWhitespaceMultipleSpaces() {
 String input = "   ";
 String result = StringUtil.normaliseWhitespace(input);
 assertEquals(" ", result);
 }

 @Test
 public void testNormaliseWhitespaceTab() {
 String input = "\t";
 String result = StringUtil.normaliseWhitespace(input);
 assertEquals(" ", result);
 }

 @Test
 public void testNormaliseWhitespaceNewline() {
 String input = "\n";
 String result = StringUtil.normaliseWhitespace(input);
 assertEquals(" ", result);
 }

 @Test
 public void testNormaliseWhitespaceReturn() {
 String input = "\r";
 String result = StringUtil.normaliseWhitespace(input);
 assertEquals(" ", result);
 }

 @Test
 public void testNormaliseWhitespaceSpaceTabNewline() {
 String input = " \t\n";
 String result = StringUtil.normaliseWhitespace(input);
 assertEquals(" ", result);
 }

 @Test
 public void testNormaliseWhitespaceSpaceTabNewlineAndNbsp() {
 String input = " \t\n\u00A0";
 String result = StringUtil.normaliseWhitespace(input);
 assertEquals(" ", result);
 }

 @Test
 public void testResolveNullBaseUrl() {
 String relUrl = "/path/to/file.html";
 String result = StringUtil.resolve(null, relUrl);
 assertEquals("", result);
 }

 @Test
 public void testResolveEmptyBaseUrl() {
 String relUrl = "/path/to/file.html";
 String result = StringUtil.resolve("", relUrl);
 assertEquals("", result);
 }

 @Test
 public void testResolveRelativeUrlToAbsoluteUrl() {
 String baseUrl = "http://example.com";
 String relUrl = "/path/to/file.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("http://example.com/path/to/file.html", result);
 }

 @Test
 public void testResolveAbsoluteUrlToAbsoluteUrl() {
 String baseUrl = "http://example.com/";
 String relUrl = "/path/to/file.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("http://example.com/path/to/file.html", result);
 }

 @Test
 public void testResolveAbsoluteUrlToRelativeUrl() {
 String baseUrl = "http://example.com/path/to/";
 String relUrl = "file.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("file.html", result);
 }

 @Test
 public void testResolveRelativeUrlToRelativeUrl() {
 String baseUrl = "http://example.com/path/to/";
 String relUrl = "../file.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("../../file.html", result);
 }

 @Test
 public void testResolveRelativeUrlToNonExistingFile() {
 String baseUrl = "http://example.com/path/to/";
 String relUrl = "../file.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("", result);
 }

 @Test
 public void testResolveRelativeUrlToSameFile() {
 String baseUrl = "http://example.com/path/to/file.html";
 String relUrl = "file.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("file.html", result);
 }

 @Test
 public void testResolveMalformedUrl() {
 String baseUrl = "http://example.com/path/to/";
 String relUrl = "//file.html";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("", result);
 }

 @Test
 public void testResolveRelativeUrlWithHash() {
 String baseUrl = "http://example.com/path/to/";
 String relUrl = "file.html#fragment";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("file.html#fragment", result);
 }

 @Test
 public void testResolveAbsoluteUrlWithHash() {
 String baseUrl = "http://example.com/path/to/file.html";
 String relUrl = "/";
 String result = StringUtil.resolve(baseUrl, relUrl);
 assertEquals("#", result.substring(baseUrl.length()));
 assertEquals(baseUrl.substring(0, baseUrl.length() - 1), result.substring(0, baseUrl.length() - 1));
 }

}
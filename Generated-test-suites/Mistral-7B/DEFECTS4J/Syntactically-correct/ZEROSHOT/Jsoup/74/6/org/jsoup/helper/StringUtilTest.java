package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringUtilTest {

 @Test
 public void testJoin() {
 String[] strings = new String[]{"hello", "world", "!"};
 String sep = ",";
 String expected = "hello" + sep + "world" + sep + "!";
 String actual = StringUtil.join(strings, sep);
 assertEquals(expected, actual);

 strings = new String[]{};
 expected = "";
 actual = StringUtil.join(strings, sep);
 assertEquals(expected, actual);

 strings = new String[]{"", " "};
 expected = "";
 actual = StringUtil.join(strings, sep);
 assertEquals(expected, actual);

 strings = new String[]{"", "a"};
 expected = "a";
 actual = StringUtil.join(strings, sep);
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinCollection() {
 List<String> strings = Arrays.asList("hello", "world", "!");
 String sep = ",";
 String expected = "hello" + sep + "world" + sep + "!";
 String actual = StringUtil.join(strings, sep);
 assertEquals(expected, actual);

 strings = Collections.emptyList();
 expected = "";
 actual = StringUtil.join(strings, sep);
 assertEquals(expected, actual);
 }

 @Test
 public void testJoinIterator() {
 List<String> strings = Arrays.asList("hello", "world", "!");
 Iterator<String> iter = strings.iterator();
 String sep = ",";
 String expected = "hello" + sep + "world" + sep + "!";
 String actual = StringUtil.join(iter, sep);
 assertEquals(expected, actual);

 strings = Collections.emptyList().iterator();
 expected = "";
 actual = StringUtil.join(strings, sep);
 assertEquals(expected, actual);
 }

 @Test
 public void testPadding() {
 for (int i = 0; i < padding.length; i++) {
 String actual = StringUtil.padding(i);
 String expected = "";
 for (int j = 0; j < i; j++) {
 expected += " ";
 }
 assertEquals(expected, actual);
 }

 int width = 30;
 String actual = StringUtil.padding(width);
 String expected = "";
 for (int i = 0; i < width; i++) {
 expected += " ";
 }
 assertEquals(expected, actual);
 }

 @Test
 public void testIsBlank() {
 String[] strings = new String[]{null, "", " ", " \t", "\n", "\r", "\f", " \r\n\t", "\n\r", "\r\n", "hello", "world", "!", "  \tworld", "hello \r\n", " \nhello", " \t\r", " \n\t", " \r\t", " \f\t"};
 String[] expected = new String[]{true, true, true, true, true, true, true, true, true, true, true, false, false, false, false, false, false, false, false, false, false};
 for (int i = 0; i < strings.length; i++) {
 String actual = StringUtil.isBlank(strings[i]);
 assertEquals(expected[i], actual);
 }
 }

 @Test
 public void testIsNumeric() {
 String[] strings = new String[]{"123", "1A2", "123abc", "123", "1.2", "123e3", "1_2", "123 456", "123.456", "123.456e3", "123 456.789", "123.456e+03"};
 String[] expected = new String[]{true, false, false, true, true, true, false, false, false, false, false, false, false, false};
 for (int i = 0; i < strings.length; i++) {
 String actual = StringUtil.isNumeric(strings[i]);
 assertEquals(expected[i], actual);
 }
 }

 @Test
 public void testIsWhitespace() {
 int[] codes = new int[]{0x0020, 0x0009, 0x000A, 0x000B, 0x000C, 0x000D, 0x0085, 0x00A0, 0x1680, 0x200B, 0x200C, 0x200D, 0x202F, 0x205F, 0x3000};
 for (int code : codes) {
 boolean actual = StringUtil.isWhitespace(code);
 boolean expected = Arrays.binarySearch(whitespaceCodes, code) >= 0;
 assertEquals(expected, actual);
 }
 }

 @Test
 public void testIsActuallyWhitespace() {
 char[] chars = new char[]{' ', '\t', '\n', '\r', '\f', '\u00A0', '\u1680', '\u200B', '\u200C', '\u200D', '\u202F', '\u205F', '\u3000'};
 for (char c : chars) {
 boolean actual = StringUtil.isActuallyWhitespace(c);
 boolean expected = true;
 assertEquals(expected, actual);
 }

 char[] nonWhitespace = new char[]{'a', 'b', 'c', 'd', 'e', 'f'};
 for (char c : nonWhitespace) {
 boolean actual = StringUtil.isActuallyWhitespace(c);
 boolean expected = false;
 assertEquals(expected, actual);
 }
 }

 @Test
 public void testNormaliseWhitespace() {
 String[] inputs = new String[]{"", " ", "\t", "\n", "\r", "\f", "hello     world!\n\t\r", "hello\t world!\n\t\r"};
 String[] expected = new String[]{"", " ", "\t", "\n", "\r", "\f", "hello world!", "hello world!"};
 for (int i = 0; i < inputs.length; i++) {
 String actual = StringUtil.normaliseWhitespace(inputs[i]);
 assertEquals(expected[i], actual);
 }
 }

 @Test
 public void testIn() {
 String needle = "hello";
 String[] haystack1 = new String[]{"hello world", "hello", "world", "java"};
 assertTrue(StringUtil.in(needle, haystack1));

 String[] haystack2 = new String[]{"world", "hello world", "java", "hello"};
 assertFalse(StringUtil.in(needle, haystack2));
 }

 @Test
 public void testInSorted() {
 String needle = "hello";
 String[] haystack = new String[]{"java", "world", "hello world", "hello"};
 assertTrue(StringUtil.inSorted(needle, haystack));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsBlankNegativeWidth() {
 String input = StringUtil.padding(-1);
 fail();
 }

 @Test
 public void testResolve() throws MalformedURLException {
 String baseUrl = "https://www.example.com/index.html";
 String relUrl1 = "about.html";
 String relUrl2 = "../contact.html";
 String relUrl3 = "..//relative.html";
 String relUrl4 = "./";

 String expectedBaseUrl = "https://www.example.com/index.html";
 String expectedRelUrl1 = "https://www.example.com/about.html";
 String expectedRelUrl2 = "https://www.example.com/contact.html";
 String expectedRelUrl3 = "";
 String expectedRelUrl4 = "";

 String actualBaseUrl = StringUtil.resolve(new URL(expectedBaseUrl), "");
 assertEquals(expectedBaseUrl, actualBaseUrl.toExternalForm());

 String actualRelUrl1 = StringUtil.resolve(expectedBaseUrl, relUrl1);
 assertEquals(expectedRelUrl1, actualRelUrl1.toExternalForm());

 String actualRelUrl2 = StringUtil.resolve(expectedBaseUrl, relUrl2);
 assertEquals(expectedRelUrl2, actualRelUrl2.toExternalForm());

 String actualRelUrl3 = StringUtil.resolve(expectedBaseUrl, relUrl3);
 assertEquals(expectedRelUrl3, actualRelUrl3);

 String actualRelUrl4 = StringUtil.resolve(expectedBaseUrl, relUrl4);
 assertEquals(expectedRelUrl4, actualRelUrl4.toExternalForm());
 }

 @Test
 public void testResolveMalformedBaseUrl() throws MalformedURLException {
 String relUrl = "about.html";

 String expectedRelUrl = "about.html";
 String actualRelUrl = StringUtil.resolve(null, relUrl);
 assertEquals(expectedRelUrl, actualRelUrl);
 }

 @Test
 public void testResolveMalformedRelUrl() throws MalformedURLException {
 String baseUrl = "https://www.example.com/index.html";

 String expected = "";
 String actual = StringUtil.resolve(baseUrl, "");
 assertEquals(expected, actual);

 String relUrl1 = "//path/file.html";
 String actualRelUrl1 = StringUtil.resolve(baseUrl, relUrl1);
 assertEquals(expected, actualRelUrl1);

 String relUrl2 = "//path/file.html?foo";
 String actualRelUrl2 = StringUtil.resolve(baseUrl, relUrl2);
 assertEquals(expected, actualRelUrl2);

 String relUrl3 = "http://www.example.com/";
 String actualRelUrl3 = StringUtil.resolve(baseUrl, relUrl3);
 assertEquals(expected, actualRelUrl3);
 }

 @Test
 public void testResolveMalformedBaseUrlAndRelUrl() throws MalformedURLException {
 String baseUrl = "https://www.example.com/index.html";
 String relUrl = "//path/file.html";

 String expected = "";
 String actual = StringUtil.resolve(new URL(baseUrl), relUrl);
 assertEquals(expected, actual);
 }

 @Test
 public void testStringBuilder() {
 StringBuilder sb = StringUtil.stringBuilder();
 String expected = "";
 assertEquals(expected, sb.toString());

 sb.append("hello");
 assertEquals("hello", sb.toString());

 sb.setLength(0);
 assertEquals(expected, sb.toString());
 }
}
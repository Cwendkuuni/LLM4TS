package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class StringUtilTest {

 // Test for join method with Collection
 @Test
 public void testJoinCollection() {
 Collection<String> strings = Arrays.asList("Hello", "World", "Java");
 String expected = "HelloWorldJava";
 String actual = StringUtil.join(strings, "");
 assertEquals(expected, actual);
 }

 // Test for join method with Iterator
 @Test
 public void testJoinIterator() {
 Iterator<String> strings = Arrays.asList("Hello", "World", "Java").iterator();
 String expected = "HelloWorldJava";
 String actual = StringUtil.join(strings, "");
 assertEquals(expected, actual);
 }

 // Test for join method with Array
 @Test
 public void testJoinArray() {
 String[] strings = new String[]{"Hello", "World", "Java"};
 String expected = "HelloWorldJava";
 String actual = StringUtil.join(strings, "");
 assertEquals(expected, actual);
 }

 // Test for padding method with valid input
 @Test
 public void testPaddingValid() {
 String actual = StringUtil.padding(5);
 String expected = "     ";
 assertEquals(expected, actual);
 }

 // Test for padding method with negative input
 @Test(expected = IllegalArgumentException.class)
 public void testPaddingNegative() {
 StringUtil.padding(-2);
 }

 // Test for isBlank method with null input
 @Test
 public void testIsBlankNull() {
 boolean actual = StringUtil.isBlank(null);
 boolean expected = true;
 assertEquals(expected, actual);
 }

 // Test for isBlank method with empty string input
 @Test
 public void testIsBlankEmpty() {
 boolean actual = StringUtil.isBlank("");
 boolean expected = true;
 assertEquals(expected, actual);
 }

 // Test for isBlank method with whitespace string input
 @Test
 public void testIsBlankWhitespace() {
 String string = "   ";
 boolean actual = StringUtil.isBlank(string);
 boolean expected = true;
 assertEquals(expected, actual);
 }

 // Test for isBlank method with non-blank string input
 @Test
 public void testIsBlankNonBlank() {
 String string = "HelloWorld";
 boolean actual = StringUtil.isBlank(string);
 boolean expected = false;
 assertEquals(expected, actual);
 }

 // Test for isNumeric method with numeric string input
 @Test
 public void testIsNumericNumeric() {
 String string = "12345";
 boolean actual = StringUtil.isNumeric(string);
 boolean expected = true;
 assertEquals(expected, actual);
 }

 // Test for isNumeric method with non-numeric string input
 @Test
 public void testIsNumericNonNumeric() {
 String string = "abcdef";
 boolean actual = StringUtil.isNumeric(string);
 boolean expected = false;
 assertEquals(expected, actual);
 }

 // Test for isWhitespace method with whitespace character input
 @Test
 public void testIsWhitespaceWhitespace() {
 int actual = StringUtil.isWhitespace(' ');
 int expected = 1;
 assertEquals(expected, actual);
 }

 // Test for isWhitespace method with non-whitespace character input
 @Test
 public void testIsWhitespaceNonWhitespace() {
 int actual = StringUtil.isWhitespace('a');
 int expected = 0;
 assertEquals(expected, actual);
 }

 // Test for normaliseWhitespace method with single whitespace input
 @Test
 public void testNormaliseWhitespaceSingle() {
 String actual = StringUtil.normaliseWhitespace(" ");
 String expected = " ";
 assertEquals(expected, actual);
 }

 // Test for normaliseWhitespace method with multiple whitespace input
 @Test
 public void testNormaliseWhitespaceMultiple() {
 String actual = StringUtil.normaliseWhitespace(" Hello\tWorld ");
 String expected = " Hello World ";
 assertEquals(expected, actual);
 }

 // Test for normaliseWhitespace method with mixed whitespace input
 @Test
 public void testNormaliseWhitespaceMixed() {
 String actual = StringUtil.normaliseWhitespace(" Hello\tWorld \r\n Java ");
 String expected = " Hello World Java ";
 assertEquals(expected, actual);
 }

 // Test for in method with list and needle
 @Test
 public void testInList() {
 List<String> strings = Arrays.asList("Hello", "World", "Java");
 String needle = "World";
 boolean actual = StringUtil.in(needle, strings.toArray(new String[0]));
 boolean expected = true;
 assertEquals(expected, actual);
 }

 // Test for in method with array and needle
 @Test
 public void testInArray() {
 String[] strings = new String[]{"Hello", "World", "Java"};
 String needle = "World";
 boolean actual = StringUtil.in(needle, strings);
 boolean expected = true;
 assertEquals(expected, actual);
 }

 // Test for in method with needle not in list or array
 @Test
 public void testInNotFound() {
 List<String> strings = Arrays.asList("Hello", "World", "Java");
 String needle = "Python";
 boolean actual = StringUtil.in(needle, strings.toArray(new String[0]));
 boolean expected = false;
 assertEquals(expected, actual);
 }

 // Test for inSorted method with list and needle
 @Test
 public void testInSortedList() {
 List<String> strings = Arrays.asList("Java", "World", "Hello");
 String needle = "World";
 boolean actual = StringUtil.inSorted(needle, strings.toArray(new String[0]));
 boolean expected = true;
 assertEquals(expected, actual);
 }

 // Test for inSorted method with list and needle not in sorted list
 @Test
 public void testInSortedNotFound() {
 List<String> strings = Arrays.asList("Java", "World", "Hello");
 String needle = "Python";
 boolean actual = StringUtil.inSorted(needle, strings.toArray(new String[0]));
 boolean expected = false;
 assertEquals(expected, actual);
 }

 // Test for resolve method with absolute base and absolute rel URL
 @Test
 public void testResolveAbsoluteAbsolute() throws MalformedURLException {
 String baseUrl = "http://example.com/";
 String relUrl = "http://example.com/test.html";
 String expected = "http://example.com/test.html";
 String actual = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, actual);
 }

 // Test for resolve method with absolute base and relative rel URL
 @Test
 public void testResolveAbsoluteRelative() throws MalformedURLException {
 String baseUrl = "http://example.com/";
 String relUrl = "/test.html";
 String expected = "http://example.com/test.html";
 String actual = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, actual);
 }

 // Test for resolve method with relative base and absolute rel URL
 @Test
 public void testResolveRelativeAbsolute() throws MalformedURLException {
 String baseUrl = "http://example.com/";
 String relUrl = "http://example.com/test.html";
 String expected = "http://example.com/test.html";
 String actual = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, actual);
 }

 // Test for resolve method with relative base and relative rel URL
 @Test
 public void testResolveRelativeRelative() throws MalformedURLException {
 String baseUrl = "http://example.com/";
 String relUrl = "/test.html";
 String expected = "http://example.com/test.html";
 String actual = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, actual);
 }

 // Test for resolve method with empty base URL
 @Test
 public void testResolveEmptyBase() throws MalformedURLException {
 String baseUrl = "";
 String relUrl = "http://example.com/test.html";
 String expected = "";
 String actual = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, actual);
 }

 // Test for resolve method with null base URL
 @Test(expected = NullPointerException.class)
 public void testResolveNullBase() throws MalformedURLException {
 String baseUrl = null;
 String relUrl = "http://example.com/test.html";
 String actual = StringUtil.resolve(baseUrl, relUrl);
 }

 // Test for resolve method with null rel URL
 @Test(expected = NullPointerException.class)
 public void testResolveNullRel() throws MalformedURLException {
 String baseUrl = "http://example.com/";
 String relUrl = null;
 String actual = StringUtil.resolve(baseUrl, relUrl);
 }

 // Test for resolve method with malformed rel URL
 @Test(expected = MalformedURLException.class)
 public void testResolveMalformedRel() throws MalformedURLException {
 String baseUrl = "http://example.com/";
 String relUrl = "//example.com/test.html";
 String actual = StringUtil.resolve(baseUrl, relUrl);
 }

 // Test for resolve method with malformed base URL
 @Test(expected = MalformedURLException.class)
 public void testResolveMalformedBase() throws MalformedURLException {
 String baseUrl = "http://example.com/";
 String relUrl = "/test.html";
 String actual = StringUtil.resolve(baseUrl, relUrl);
 baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
 String expected = "";
 assertEquals(expected, actual);
 }

 // Test for resolve method with empty rel URL
 @Test
 public void testResolveEmptyRel() throws MalformedURLException {
 String baseUrl = "http://example.com/";
 String relUrl = "";
 String expected = "";
 String actual = StringUtil.resolve(baseUrl, relUrl);
 assertEquals(expected, actual);
 }

 // Test for stringBuilder method with normal usage
 @Test
 public void testStringBuilderNormalUsage() {
 StringBuilder expected = new StringBuilder("Hello");
 StringBuilder actual = StringUtil.stringBuilder();
 actual.append("Hello");
 assertTrue(expected.toString().equals(actual.toString()));
 }

 // Test for stringBuilder method with size limit
 @Test
 public void testStringBuilderSizeLimit() {
 StringBuilder expected = new StringBuilder("Hello");
 StringBuilder actual = StringUtil.stringBuilder();
 for (int i = 0; i < 10 * 1024; i++) {
 actual.append("X");
 }
 StringBuilder expectedLimited = new StringBuilder(10 * 1024 * 10);
 expectedLimited.append("X".repeat(10 * 1024));
 assertTrue(expectedLimited.toString().equals(actual.toString()));
 }
}
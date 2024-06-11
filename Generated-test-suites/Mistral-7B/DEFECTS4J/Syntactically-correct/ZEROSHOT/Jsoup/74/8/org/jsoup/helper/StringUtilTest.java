package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class StringUtilTest {

 @Test
 public void testJoin() {
 String[] strings = {"apple", "banana", "cherry"};
 String expected = "applebananacherry";
 assertEquals(expected, StringUtil.join(strings, ""));

 expected = "apple, banana, cherry";
 assertEquals(expected, StringUtil.join(strings, ", "));

 expected = "apple banana cherry";
 assertEquals(expected, StringUtil.join(strings, " "));
 }

 @Test
 public void testJoinCollection() {
 List<String> stringList = Arrays.asList("apple", "banana", "cherry");
 String expected = "applebananacherry";
 assertEquals(expected, StringUtil.join(stringList, ""));

 expected = "apple, banana, cherry";
 assertEquals(expected, StringUtil.join(stringList, ", "));

 expected = "apple banana cherry";
 assertEquals(expected, StringUtil.join(stringList, " "));
 }

 @Test
 public void testJoinIterator() {
 List<String> stringList = Arrays.asList("apple", "banana", "cherry");
 Iterator<String> iterator = stringList.iterator();
 String expected = "applebananacherry";
 assertEquals(expected, StringUtil.join(iterator, ""));

 expected = "apple, banana, cherry";
 assertEquals(expected, StringUtil.join(iterator, ", "));

 expected = "apple banana cherry";
 assertEquals(expected, StringUtil.join(iterator, " "));
 }

 @Test(expected = RuntimeException.class)
 public void testJoinNullCollection() {
 StringUtil.join(null, "");
 }

 @Test
 public void testJoinEmptyCollection() {
 String expected = "";
 assertEquals(expected, StringUtil.join(Collections.emptyList(), ""));
 }

 @Test
 public void testPadding() {
 for (int i = 0; i < padding.length; i++) {
 String expected = StringUtil.padding(i);
 String actual = StringUtil.padding(i.length());
 assertEquals(expected, actual);
 }
 }

 @Test
 public void testIsBlank() {
 String blank = "";
 String notBlank1 = "foo";
 String notBlank2 = " bar ";
 String notBlank3 = " 123";
 boolean expected = false;
 assertEquals(expected, StringUtil.isBlank(blank));
 assertEquals(expected, StringUtil.isBlank(notBlank1));
 assertEquals(expected, StringUtil.isBlank(notBlank2));
 assertEquals(expected, StringUtil.isBlank(notBlank3));
 }

 @Test
 public void testIsNumeric() {
 String numeric = "123";
 String nonNumeric1 = "foo";
 String nonNumeric2 = "bar123";
 boolean expected = true;
 assertEquals(expected, StringUtil.isNumeric(numeric));
 assertEquals(expected, StringUtil.isNumeric(nonNumeric1));
 assertFalse(StringUtil.isNumeric(nonNumeric2));
 }

 @Test
 public void testIsWhitespace() {
 char space = ' ';
 char tab = '\t';
 char newline = '\n';
 char formFeed = '\f';
 char carriageReturn = '\r';
 boolean expected = true;
 assertTrue(StringUtil.isWhitespace(space));
 assertTrue(StringUtil.isWhitespace(tab));
 assertTrue(StringUtil.isWhitespace(newline));
 assertTrue(StringUtil.isWhitespace(formFeed));
 assertTrue(StringUtil.isWhitespace(carriageReturn));
 }

 @Test
 public void testIsActuallyWhitespace() {
 char space = ' ';
 char tab = '\t';
 char newline = '\n';
 char formFeed = '\f';
 char carriageReturn = '\r';
 char nonBreakingSpace = '\u00A0';
 boolean expected = true;
 assertTrue(StringUtil.isActuallyWhitespace(space));
 assertTrue(StringUtil.isActuallyWhitespace(tab));
 assertTrue(StringUtil.isActuallyWhitespace(newline));
 assertTrue(StringUtil.isActuallyWhitespace(formFeed));
 assertTrue(StringUtil.isActuallyWhitespace(carriageReturn));
 assertTrue(StringUtil.isActuallyWhitespace(nonBreakingSpace));
 }

 @Test
 public void testNormaliseWhitespace() {
 String input1 = "  foo     ";
 String expected1 = "foo";
 assertEquals(expected1, StringUtil.normaliseWhitespace(input1));

 String input2 = "  foo \t bar\n baz  ";
 String expected2 = "foo bar baz";
 assertEquals(expected2, StringUtil.normaliseWhitespace(input2));
 }

 @Test
 public void testIn() {
 String haystack1 = "apple,banana,cherry";
 String needle1 = "banana";
 boolean expected1 = true;
 assertTrue(StringUtil.in(needle1, haystack1.split(",")));

 String haystack2 = "apple,banana,cherry";
 String needle2 = "orange";
 boolean expected2 = false;
 assertFalse(StringUtil.in(needle2, haystack2.split(",")));
 }

 @Test
 public void testInSorted() {
 String[] haystack = {"apple", "banana", "cherry"};
 String needle = "banana";
 boolean expected = true;
 assertTrue(StringUtil.inSorted(needle, haystack));
 }

 @Test
 public void testResolve() throws MalformedURLException {
 String baseUrl = "https://example.com";
 String relUrl1 = "path/file.html";
 String expected1 = "https://example.com/path/file.html";
 assertEquals(expected1, StringUtil.resolve(new URL(baseUrl), relUrl1));

 String relUrl2 = "../index.html";
 String expected2 = "https://example.com/index.html";
 assertEquals(expected2, StringUtil.resolve(new URL(baseUrl), relUrl2));

 String relUrl3 = "../../index.html";
 String expected3 = "https://example.com/index.html";
 assertEquals(expected3, StringUtil.resolve(new URL(baseUrl), relUrl3));

 String relUrl4 = "http://example.org/path/file.html";
 String expected4 = "http://example.org/path/file.html";
 assertEquals(expected4, StringUtil.resolve(new URL(baseUrl), relUrl4));
 }

 @Test
 public void testResolveEmptyString() throws MalformedURLException {
 String baseUrl = "https://example.com";
 String relUrl = "";
 String expected = "";
 assertEquals(expected, StringUtil.resolve(new URL(baseUrl), relUrl));
 }

 @Test
 public void testResolveNullBaseUrl() throws MalformedURLException {
 String relUrl = "path/file.html";
 String expected = "";
 assertEquals(expected, StringUtil.resolve(null, relUrl));
 }

 @Test
 public void testResolveNullRelUrl() throws MalformedURLException {
 String baseUrl = "https://example.com";
 String expected = "";
 assertEquals(expected, StringUtil.resolve(new URL(baseUrl), null));
 }

 @Test
 public void testResolveInvalidRelUrl() throws MalformedURLException {
 String baseUrl = "https://example.com";
 String relUrl = "data:text/plain,Hello%20World";
 String expected = "";
 assertEquals(expected, StringUtil.resolve(new URL(baseUrl), relUrl));
 }
}
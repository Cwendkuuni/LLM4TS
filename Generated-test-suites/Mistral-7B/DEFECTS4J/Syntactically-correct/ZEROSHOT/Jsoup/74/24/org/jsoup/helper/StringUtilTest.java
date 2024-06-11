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
 String[] strings = {"one", "two", "three"};
 String expected = "onetwothree";
 String result = StringUtil.join(strings, "");
 assertEquals(expected, result);

 expected = "one | two | three";
 result = StringUtil.join(strings, " | ");
 assertEquals(expected, result);
 }

 @Test
 public void testJoinCollection() {
 List<String> strings = Arrays.asList("one", "two", "three");
 String expected = "onetwothree";
 String result = StringUtil.join(strings, "");
 assertEquals(expected, result);

 expected = "one | two | three";
 result = StringUtil.join(strings, " | ");
 assertEquals(expected, result);
 }

 @Test
 public void testJoinIterator() {
 List<String> strings = Arrays.asList("one", "two", "three");
 Iterator<String> stringIterator = strings.iterator();
 String expected = "onetwothree";
 String result = StringUtil.join(stringIterator, "");
 assertEquals(expected, result);

 expected = "one | two | three";
 result = StringUtil.join(stringIterator, " | ");
 assertEquals(expected, result);
 }

 @Test
 public void testJoinEmpty() {
 String result = StringUtil.join(Collections.emptyList(), "");
 assertEquals("", result);
 }

 @Test
 public void testPadding() {
 for (int i = 0; i < padding.length; i++) {
 String result = StringUtil.padding(i);
 assertEquals(padding[i], result);
 }
 }

 @Test
 public void testIsBlank() {
 String[] strings = {"", " ", "one", " one", "one two", "one two "};
 for (String string : strings) {
 boolean expected = string.isEmpty();
 boolean result = StringUtil.isBlank(string);
 assertEquals(expected, result);
 }
 }

 @Test
 public void testIsNumeric() {
 String[] strings = {"", "1", "123", "abcd", "123.4", "123.45", "12345"};
 for (String string : strings) {
 boolean expected = string.matches("\\d+(\\.\\d+)?") || string.isEmpty();
 boolean result = StringUtil.isNumeric(string);
 assertEquals(expected, result);
 }
 }

 @Test
 public void testIsWhitespace() {
 for (int i = 0; i < Character.MAX_CODE_POINT; i++) {
 boolean expected = Character.isWhitespace(i) || i == 160;
 boolean result = StringUtil.isWhitespace(i);
 assertEquals(expected, result);
 }
 }

 @Test
 public void testIsActuallyWhitespace() {
 for (int i = 0; i < Character.MAX_CODE_POINT; i++) {
 boolean expected = Character.isWhitespace(i) || i == 160;
 boolean result = StringUtil.isActuallyWhitespace(i);
 assertEquals(expected, result);
 }
 }

 @Test
 public void testNormaliseWhitespace() {
 String[] strings = {" one  two   three  ", " one\ttwo\r\n three", "one two three", "", " one\ttwo\r\n three "};
 for (String string : strings) {
 String expected = "onetwothree";
 String result = StringUtil.normaliseWhitespace(string);
 assertEquals(expected, result);
 }
 }

 @Test
 public void testIn() {
 String needle = "foo";
 String[] haystack = {"bar", "foo", "baz"};
 boolean expected = true;
 boolean result = StringUtil.in(needle, haystack);
 assertEquals(expected, result);

 needle = "quux";
 expected = false;
 result = StringUtil.in(needle, haystack);
 assertEquals(expected, result);
 }

 @Test
 public void testInSorted() {
 String needle = "foo";
 String[] haystack = {"bar", "foo", "baz"};
 boolean expected = true;
 boolean result = StringUtil.inSorted(needle, haystack);
 assertEquals(expected, result);

 needle = "quux";
 expected = false;
 result = StringUtil.inSorted(needle, haystack);
 assertEquals(expected, result);
 }

 @Test
 public void testResolve() throws MalformedURLException {
 String baseUrl = "http://example.com/index.html";
 String relUrl = "/about.html";
 URL expected = new URL("http://example.com/about.html");
 URL result = StringUtil.resolve(new URL(baseUrl), relUrl);
 assertEquals(expected.toString(), result.toString());

 baseUrl = "http://example.com/index.html";
 relUrl = "/../contact.html";
 expected = new URL("http://example.com/contact.html");
 result = StringUtil.resolve(new URL(baseUrl), relUrl);
 assertEquals(expected.toString(), result.toString());

 baseUrl = "http://example.com/index.html";
 relUrl = "../contact.html";
 expected = new URL("http://example.com/contact.html");
 result = StringUtil.resolve(new URL(baseUrl), relUrl);
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testResolveEmptyBaseUrl() throws MalformedURLException {
 String relUrl = "/about.html";
 String expected = "/about.html";
 String result = StringUtil.resolve("", relUrl);
 assertEquals(expected, result);
 }

 @Test
 public void testResolveRelativeUrlIsAlreadyAbsolute() throws MalformedURLException {
 String relUrl = "http://example.com/about.html";
 String expected = relUrl;
 String result = StringUtil.resolve("", relUrl);
 assertEquals(expected, result);
 }
}
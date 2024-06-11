package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {

 @Test
 public void testFilterNullInput() {
 assertEquals(null, HTMLFilter.filter(null));
 }

 @Test
 public void testFilterEmptyInput() {
 assertEquals("", HTMLFilter.filter(""));
 }

 @Test
 public void testFilterSingleCharacter() {
 char[] message = {'A'};
 char[] expected = {'A'};
 char[] result = new char[message.length];
 HTMLFilter.filter(message, 0, message.length, result, 0);
 assertEquals(new String(expected), new String(result));
 }

 @Test
 public void testFilterWithSpecialCharacters() {
 String message = "<>&\"'";
 String expected = "&lt;&gt;&amp; &quot; &apos;";
 String result = HTMLFilter.filter(message);
 assertEquals(expected, result);
 }

 @Test
 public void testFilterWithMultipleSpecialCharacters() {
 String message = "<>&\"'<>";
 String expected = "&lt;&gt;&amp; &quot; &apos; &lt;";
 String result = HTMLFilter.filter(message);
 assertEquals(expected, result);
 }

 @Test
 public void testFilterWithLongString() {
 String message = "<p><b>Hello</b> World! &quot;<i>Test</i>&apos; <a href=\"http://example.com\">Link</a></p>";
 String expected = "&lt;p&gt;&amp;lt;b&gt;Hello&amp;lt;/b&gt; World! &amp;quot;&lt;i&gt;Test&lt;/i&gt;&amp;apos; &amp;lt;a href=&quot;http://example.com&quot;&gt;Link&lt;/a&gt;&amp;lt;/p&gt;";
 String result = HTMLFilter.filter(message);
 assertEquals(expected, result);
 }

 @Test
 public void testFilterWithLongInputArray() {
 char[] message = {'<', 'p', '<', 'b', 'H', 'e', 'l', 'l', 'o', '>', 'W', 'o', 'r', 'l', 'd', '!', ' ', '&', 'q', 'u', 'o', 't', ';', '<', 'i', ' ', 'T', 'e', 's', 't', ' ', '&', 'a', 'p', 'o', 's', ';', ' ', '<', 'a', ' ', 'h', 'r', 'e', 'f', '=', '"', 'h', 't', 't', 'p', ':', '/', 'e', 'x', 'a', 'm', 'p', 'l', '/', 'I', 'n', 'd', 'e', 'x', '.', 'c', 'o', 'm', ' '};
 char[] expected = {'&lt;', 'p', '&lt;', 'b', 'H', 'e', 'l', 'l', 'o', '&gt;', 'W', 'o', 'r', 'l', 'd', '!', ' ', '&', 'a', 'm', 'p', ';', '&lt;', 'i', ' ', 'T', 'e', 's', 't', ' ', '&', 'a', 'p', 'o', 's', ';', ' ', '&lt;', 'a', ' ', 'h', 'r', 'e', 'f', '=', '&quot;', 'h', 't', 't', 'p', ':', '/', 'e', 'x', 'a', 'm', 'p', '/', 'I', 'n', 'd', 'e', 'x', '.', 'c', 'o', 'm', ' '};
 char[] result = new char[message.length];
 HTMLFilter.filter(message, 0, message.length, result, 0);
 assertEquals(new String(expected), new String(result));
 }
}
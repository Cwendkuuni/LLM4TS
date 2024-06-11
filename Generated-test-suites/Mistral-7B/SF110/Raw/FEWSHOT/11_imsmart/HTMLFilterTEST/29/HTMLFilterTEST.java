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
 public void testFilterSimpleInput() {
 char[] messageChars = new char[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
 String expected = "H&lt;e&lt;l&lt;l&lt;o&gt; &amp;W&lt;o&lt;l&lt;d";
 String message = new String(messageChars);
 String result = HTMLFilter.filter(message);
 assertEquals(expected, result);
 }

 @Test
 public void testFilterInputWithSpecialCharacters() {
 char[] messageChars = new char[]{'<', 'h', 'e', 'l', 'l', 'o', '>', ' ', '&', 'a', 'm', 'p;', '&', 'q', 'u', 'o', 't', '>', ' '};
 String expected = "&lt;h&lt;e&lt;l&lt;l&lt;o&gt;&gt; &amp;a&amp;m&amp;p;&amp;q&gt; &quot;";
 String message = new String(messageChars);
 String result = HTMLFilter.filter(message);
 assertEquals(expected, result);
 }

 @Test
 public void testFilterLongInput() {
 String longMessage = "This is a very long message that contains many special characters: <h1>Hello <b>World!</b></h1><p>This &amp; that &quot;is&quot; a test &lt;a href=\"http://www.example.com\"&gt;link&lt;/a&gt;.";
 String expected = "This is a very long message that contains many special characters: &lt;h1&gt;Hello &lt;b&gt;World!&lt;/b&gt;</h1>&lt;p&gt;This &amp;amp; that &quot;is&quot; a test &lt;a href=&quot;http://www.example.com&quot;&gt;link&lt;/a&gt;.";
 String result = HTMLFilter.filter(longMessage);
 assertEquals(expected, result);
 }

 @Test
 public void testFilterInputWithNullCharacter() {
 char[] messageChars = new char[]{'H', 'e', 'l', 'l', 'o', ' ', '<', null, '>', ' '};
 String message = new String(messageChars);
 String result = HTMLFilter.filter(message);
 assertNull(result);
 }
}
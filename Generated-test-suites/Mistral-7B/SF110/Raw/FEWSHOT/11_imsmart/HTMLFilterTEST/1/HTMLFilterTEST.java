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
 String message = new String(messageChars);
 String expected = "H&lt;e&lt;l&lt;l&lt;o &gt;W&gt;o&gt;r&lt;l&gt;d";
 assertEquals(expected, HTMLFilter.filter(message));
 }

 @Test
 public void testFilterInputWithSpecialCharacters() {
 char[] messageChars = new char[]{'<', 'h', 'e', 'l', 'l', 'o', '>', '&', ' ', 'W', 'o', 'r', 'l', 'd', '>', '<', 's', 'c', 'r', 'i', 'p', 't', '>', ' ', '&', 'a', 'm', 'p;', ' ', '&', 'l', 't;'};
 String message = new String(messageChars);
 String expected = "&lt;h&lt;e&lt;l&lt;l&lt;o &gt;&gt;W&gt;o&gt;r&lt;l&gt;d&gt; &amp; &lt;s&gt;c&gt;r&gt;i&gt;p&gt; &gt; &amp; &lt;a&gt;m&gt;p;&gt; &gt;";
 assertEquals(expected, HTMLFilter.filter(message));
 }

 @Test
 public void testFilterInputWithMultipleSpecialCharacters() {
 char[] messageChars = new char[]{'<', 'h', 'e', 'l', 'l', 'o', '>', '&', '&', 'a', 'm', 'p', ';', ' ', '&', 'l', 't;', '&', 'g', 't;', ' ', '&', 'q', 'u', 'o', 't;', ' ', '&', 'a', 'm', 'p;', ' ', '&', 'l', 't;'};
 String message = new String(messageChars);
 String expected = "&lt;h&lt;e&lt;l&lt;l&lt;o &gt;&gt; &amp; &amp;a&gt;m&gt;p;&gt; &gt; &gt; &gt; &gt; &gt; &amp; &lt;a&gt;m&gt;p;&gt; &gt; &gt;";
 assertEquals(expected, HTMLFilter.filter(message));
 }
}
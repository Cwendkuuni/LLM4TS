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
 String expected = "H&lt;e&lt;l&lt;l&lt;o &gt;W&gt;o&gt;r&gt;l&gt;d";
 String message = new String(messageChars);
 String result = HTMLFilter.filter(message);
 assertEquals(expected, result);
 }

 @Test
 public void testFilterInputWithSpecialCharacters() {
 char[] messageChars = new char[]{'<', 'h', 'e', 'l', 'l', 'o', '>', '&', ' ', 'W', 'o', 'r', 'l', 'd', '&', '<', 's', 'c', 'r', 'i', 'p', 't', '>', ' ', '>', '>'};
 String expected = "&lt;h&lt;e&lt;l&lt;l&lt;o &gt;&amp; &gt;W&gt;o&gt;r&gt;l&gt;d&amp; &lt;s&lt;c&lt;r&lt;i&lt;p&gt;&gt; &gt;&gt;&gt;";
 String message = new String(messageChars);
 String result = HTMLFilter.filter(message);
 assertEquals(expected, result);
 }

 @Test
 public void testFilterInputWithMultipleSpecialCharacters() {
 char[] messageChars = new char[]{'<', 'h', 'e', 'l', 'l', 'o', '>', '&', '&', 'a', 'm', 'p', ';', '<', 's', 'c', 'r', 'i', 'p', 't', '>', ' ', '>', '>'};
 String expected = "&lt;h&lt;e&lt;l&lt;l&lt;o &gt;&amp; &amp;a&gt;m&gt;p&gt;&gt; &lt;s&lt;c&lt;r&lt;i&lt;p&gt;&gt; &gt;&gt;&gt;";
 String message = new String(messageChars);
 String result = HTMLFilter.filter(message);
 assertEquals(expected, result);
 }

 @Test
 public void testFilterInputWithSingleQuotes() {
 char[] messageChars = new char[]{'H', "'", 'e', 'l', 'l', 'o', "'", ' ', 'W', 'o', 'r', 'l', 'd'};
 String expected = "H&lt;'&gt;e&lt;l&lt;l&lt;o &gt;W&gt;o&gt;r&gt;l&gt;d";
 String message = new String(messageChars);
 String result = HTMLFilter.filter(message);
 assertEquals(expected, result);
 }

 @Test
 public void testFilterInputWithSingleQuotesAndSpecialCharacters() {
 char[] messageChars = new char[]{'<', 'h', "'", 'e', 'l', 'l', 'o', '>', '&', ' ', 'W', 'o', 'r', 'l', 'd', "'", '&', '<', 's', 'c', 'r', 'i', 'p', 't', '>', ' ', '>', '>'};
 String expected = "&lt;h&lt;&gt;e&lt;l&lt;l&lt;o &gt;&amp; &gt;W&gt;o&gt;r&gt;l&gt;d&gt;&amp; &lt;s&lt;c&lt;r&lt;i&lt;p&gt;&gt; &gt;&gt;&gt;";
 String message = new String(messageChars);
 String result = HTMLFilter.filter(message);
 assertEquals(expected, result);
 }
}
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
 String expected = "H&nbsp;e&nbsp;l&nbsp;l&nbsp;o&nbsp; &nbsp;W&nbsp;o&nbsp;r&nbsp;l&nbsp;d";
 assertEquals(expected, HTMLFilter.filter(message));
 }

 @Test
 public void testFilterInputWithSpecialCharacters() {
 char[] messageChars = new char[]{'H', 'e', 'l', 'l', 'o', ' ', '<', 'b', '>', ' ', 'W', 'o', 'r', 'l', 'd'};
 String message = new String(messageChars);
 String expected = "H&nbsp;e&nbsp;l&nbsp;l&nbsp;o&nbsp; &nbsp;&amp;lt;b&gt; &nbsp;W&nbsp;o&nbsp;r&nbsp;l&nbsp;d";
 assertEquals(expected, HTMLFilter.filter(message));
 }

 @Test
 public void testFilterInputWithMultipleSpecialCharacters() {
 char[] messageChars = new char[]{'H', 'e', 'l', 'l', 'o', ' ', '<', 'b', '>', '<', 'i', '>', ' ', 'W', 'o', 'r', 'l', 'd'};
 String message = new String(messageChars);
 String expected = "H&nbsp;e&nbsp;l&nbsp;l&nbsp;o&nbsp; &nbsp;&amp;lt;b&gt; &nbsp;&amp;lt;i&gt; &nbsp;W&nbsp;o&nbsp;r&nbsp;l&nbsp;d";
 assertEquals(expected, HTMLFilter.filter(message));
 }

 @Test
 public void testFilterInputWithOnlySpecialCharacters() {
 char[] messageChars = new char[]{'<', 'b', '>', '<', 'i', '>', '<', 'p', '>', '&', 'a', 'm', 'p;'};
 String message = new String(messageChars);
 String expected = "&amp;lt;b&gt; &nbsp;&amp;lt;i&gt; &nbsp;&amp;lt;p&gt; &nbsp;&amp;amp; &nbsp;a&nbsp; &nbsp;m&nbsp; &nbsp;p&nbsp;&#8230;";
 assertEquals(expected, HTMLFilter.filter(message));
 }
}
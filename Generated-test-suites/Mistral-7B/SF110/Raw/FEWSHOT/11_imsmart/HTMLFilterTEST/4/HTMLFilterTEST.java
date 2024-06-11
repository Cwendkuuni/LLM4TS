package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {

 @Test
 public void testFilterNullInput() {
 assertNull(HTMLFilter.filter(null));
 }

 @Test
 public void testFilterEmptyInput() {
 assertEquals("", HTMLFilter.filter(""));
 }

 @Test
 public void testFilterSimpleInput() {
 char[] inputChars = new char[]{'H', 'e', 'l', 'l', 'o', '!', '<', 'b', 'o', 'o', 't', ' ', 't', 'e', 's', 't', '.', '>'};
 String expectedOutput = "Hello! &lt;boot&gt; test.";
 StringBuilder result = new StringBuilder();
 HTMLFilter.filter(new String(inputChars, 0, inputChars.length).toString(), result);
 assertEquals(expectedOutput, result.toString());
 }

 @Test
 public void testFilterComplexInput() {
 char[] inputChars = new char[]{'<', 's', 'c', 'r', 'i', 'p', 't', '>', '&', 'l', 't;', '>', '<', '/', 's', 'c', 'r', 'i', 'p', 't', '>', ' ', 'H', 'e', 'l', 'l', 'o', ' ', '&', 'a', 'm', 'p;', ' ', '&', 'l', 't;', ' ', '&', 'q', 'u', 'o', 't;', ' ', '&', 'g', 't;', ' ', '<', '/', 's', 'c', 'r', 'i', 'p', 't', '>', ' '};
 String expectedOutput = "&lt;script&gt;&amp;lt;amp;lt;lt;&gt;&amp;amp;lt;lt;script&gt; Hello &amp;amp;amp;amp;amp;lt;qout&gt; &amp;amp;amp;amp;amp;gt; &amp;amp;amp;amp;amp;gt; &amp;amp;amp;amp;amp;lt;gt;&amp;gt; &amp;gt; &amp;gt; &amp;gt; &amp;gt;";
 StringBuilder result = new StringBuilder();
 HTMLFilter.filter(new String(inputChars, 0, inputChars.length).toString(), result);
 assertEquals(expectedOutput, result.toString());
 }
}
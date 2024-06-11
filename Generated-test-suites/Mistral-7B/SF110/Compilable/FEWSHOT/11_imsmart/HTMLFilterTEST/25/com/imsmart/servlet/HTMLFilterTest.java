package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

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
 public void testFilterSingleCharacterInput() {
 char[] input = new char[]{'A'};
 String expected = String.copyValueOf(input);
 String result = HTMLFilter.filter(new String(input));
 assertEquals(expected, result);
 }

 @Test
 public void testFilterSingleSpecialCharacterLessThan() {
 char[] input = new char[]{'<'};
 String expected = "&lt;";
 String result = HTMLFilter.filter(new String(input));
 assertEquals(expected, result);
 }

 @Test
 public void testFilterSingleSpecialCharacterGreaterThan() {
 char[] input = new char[]{'>'};
 String expected = "&gt;";
 String result = HTMLFilter.filter(new String(input));
 assertEquals(expected, result);
 }

 @Test
 public void testFilterSingleSpecialCharacterAmpersand() {
 char[] input = new char[]{'&'};
 String expected = "&amp;";
 String result = HTMLFilter.filter(new String(input));
 assertEquals(expected, result);
 }

 @Test
 public void testFilterSingleSpecialCharacterQuote() {
 char[] input = new char[]{'\"'};
 String expected = "&quot;";
 String result = HTMLFilter.filter(new String(input));
 assertEquals(expected, result);
 }

 @Test
 public void testFilterSpecialCharactersTogether() {
 char[] input = new char[]{'<', '&', '>'};
 String expected = "&lt; &amp; &gt;";
 String result = HTMLFilter.filter(new String(input));
 assertEquals(expected, result);
 }

 @Test
 public void testFilterMultipleSpecialAndRegularCharacters() {
 char[] input = new char[]{'<', 'a', '&', 'b', '>', 'c'};
 String expected = "&lt;a &amp; &gt;c";
 String result = HTMLFilter.filter(new String(input));
 assertEquals(expected, result);
 }

 @Test
 public void testFilterLongInput() {
 String input = "This is a <test> string with some & special & characters, like <html> & <head> & <body> & <p>.";
 String expected = "This is a &lt;test&gt; string with some &amp; &lt;special&gt; &amp; characters, like &lt;html&gt; &amp; &lt;head&gt; &amp; &lt;body&gt; &amp; &lt;p&gt;.";
 String result = HTMLFilter.filter(input);
 assertEquals(expected, result);
 }

 @Test
 public void testFilterInputWithTrailingQuotes() {
 String input = "This is a <test> string with some & special & characters, like <html> & <head> & <body> & <p>\"";
 String expected = "This is a &lt;test&gt; string with some &amp; &lt;special&gt; &amp; characters, like &lt;html&gt; &amp; &lt;head&gt; &amp; &lt;body&gt; &amp; &lt;p&gt;\"";
 String result = HTMLFilter.filter(input);
 assertEquals(expected, result);
 }

 @Test
 public void testFilterInputWithLeadingQuotes() {
 String input = "\"This is a <test> string with some & special & characters, like <html> & <head> & <body> & <p>";
 String expected = "&quot;This is a &lt;test&gt; string with some &amp; &lt;special&gt; &amp; characters, like &lt;html&gt; &amp; &lt;head&gt; &amp; &lt;body&gt; &amp; &lt;p&gt;";
 String result = HTMLFilter.filter(input);
 assertEquals(expected, result);
 }

 @Test
 public void testFilterInputWithLeadingAndTrailingQuotes() {
 String input = "\"This is a <test> string with some & special & characters, like <html> & <head> & <body> & <p>\"";
 String expected = "&quot;This is a &lt;test&gt; string with some &amp; &lt;special&gt; &amp; characters, like &lt;html&gt; &amp; &lt;head&gt; &amp; &lt;body&gt; &amp; &lt;p&gt;\"";
 String result = HTMLFilter.filter(input);
 assertEquals(expected, result);
 }

 @Test
 public void testFilterInputWithMixedLeadingAndTrailingQuotes() {
 String input = "\"This is a <test> string with some & special & characters,\" like <html> & <head> & <body> & <p>&quot;";
 String expected = "&quot;This is a &lt;test&gt; string with some &amp; &lt;special&gt; &amp; characters,&\" &lt;p&gt; &quot; like &lt;html&gt; &amp; &lt;head&gt; &amp; &lt;body&gt; &amp;";
 String result = HTMLFilter.filter(input);
 assertEquals(expected, result);
 }
}
package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

 // Test case for filter method with null input
 @Test
 public void testFilterNullInput() {
 String expected = null;
 String actual = HTMLFilter.filter(null);
 assertEquals(expected, actual);
 }

 // Test case for filter method with empty string input
 @Test
 public void testFilterEmptyInput() {
 String expected = "";
 String actual = HTMLFilter.filter("");
 assertEquals(expected, actual);
 }

 // Test case for filter method with valid input
 @Test
 public void testFilterValidInput() {
 String message = "<html><body><h1>Hello World</h1></body></html>";
 String expected = "&lt;html&gt;&lt;body&gt;&lt;h1&gt;Hello World&lt;/h1&gt;&lt;/body&gt;&lt;/html&gt;";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 // Test case for filter method with input containing '&' character
 @Test
 public void testFilterInputWithAmpersand() {
 String message = "This is a &amp; test.";
 String expected = "This is a &amp;amp; test.";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 // Test case for filter method with input containing '>' character
 @Test
 public void testFilterInputWithGreaterThanSign() {
 String message = "This message > contains > a greater than sign.";
 String expected = "This message &gt; contains &gt; a greater than sign.";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 // Test case for filter method with input containing '<' character
 @Test
 public void testFilterInputWithLessThanSign() {
 String message = "This message contains < a less than sign.";
 String expected = "This message contains &lt; a less than sign.";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 // Test case for filter method with input containing double quote character
 @Test
 public void testFilterInputWithDoubleQuote() {
 String message = "This message contains \" a double quote character.";
 String expected = "This message contains &quot; a double quote character.";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 // Test case for filter method with input containing multiple special characters
 @Test
 public void testFilterInputWithMultipleSpecialCharacters() {
 String message = "<h1>&quot;Hello World&quot; < &amp; &gt; World&gt;</h1>";
 String expected = "&lt;h1&gt;&amp;quot;Hello World&amp;quot; &lt; &amp; &gt; World&gt;&lt;/h1&gt;";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 // Test case for filter method with input containing only whitespaces
 @Test
 public void testFilterInputWithWhitespaces() {
 String message = "   ";
 String expected = "";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 // Test case for filter method with input containing control characters
 @Test
 public void testFilterInputWithControlCharacters() {
 String message = "\u0001<html>\u0002<body>\u0003<h1>\u0004Hello World\u0005</h1>\u0006</body>\u0007</html>\u0008";
 String expected = "&\u0001;&lt;html&gt;&amp;\u0002;&lt;body&gt;&amp;\u0003;&lt;h1&gt;&amp;\u0004Hello World&amp;\u0005&lt;/h1&gt;&amp;\u0006&lt;/body&gt;&amp;\u0007&lt;/html&gt;&amp;\u0008";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }
}
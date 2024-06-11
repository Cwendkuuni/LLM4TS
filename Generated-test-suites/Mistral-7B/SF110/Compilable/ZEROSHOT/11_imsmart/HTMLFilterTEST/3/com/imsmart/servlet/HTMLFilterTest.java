package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {

 @Test
 public void testFilterNullMessage() {
 String input = null;
 String expectedOutput = null;
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterEmptyMessage() {
 String input = "";
 String expectedOutput = "";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterSimpleMessage() {
 String input = "Hello World!";
 String expectedOutput = "Hello World!";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMessageWithHtmlTags() {
 String input = "<html><head><title>Test Page</title></head><body><h1>Welcome to Test Page</h1></body></html>";
 String expectedOutput = "&lt;html&gt;&lt;head&gt;&lt;title&gt;Test Page&lt;/title&gt;&lt;/head&gt;&lt;body&gt;&lt;h1&gt;Welcome to Test Page&lt;/h1&gt;&lt;/body&gt;&lt;/html&gt;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMessageWithSpecialCharacters() {
 String input = "The quick brown fox jumps over the &lt;strong&gt;lazy&lt;/strong&gt; dog.";
 String expectedOutput = "The quick brown fox jumps over the &amp;lt;strong&amp;gt;lazy&amp;lt;/strong&amp;gt; dog.";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMessageWithQuotationMarks() {
 String input = "The quick brown fox jumps over the \"lazy\" dog.";
 String expectedOutput = "The quick brown fox jumps over the &quot;lazy&quot; dog.";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMessageWithAmpersand() {
 String input = "The quick brown fox jumps over the &amp; dog.";
 String expectedOutput = "The quick brown fox jumps over the &amp;amp; dog.";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMessageWithMultipleSpecialCharacters() {
 String input = "The quick brown fox jumps over the &lt;strong&gt;&amp;lazy&lt;/strong&gt; dog.";
 String expectedOutput = "The quick brown fox jumps over the &amp;lt;strong&amp;gt;&amp;amp;lazy&amp;lt;/strong&amp;gt; dog.";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }
}
package com.imsmart.servlet.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HTMLFilterTest {

 private final HTMLFilter filter = new HTMLFilter();

 @Test
 public void testFilterNullMessage() {
 assertEquals("", filter.filter(null));
 }

 @Test
 public void testFilterEmptyMessage() {
 assertEquals("", filter.filter(""));
 }

 @Test
 public void testFilterSimpleMessage() {
 final String message = "Hello <World>!";
 final String expected = "Hello &lt;World&gt;!";
 assertEquals(expected, filter.filter(message));
 }

 @Test
 public void testFilterMessageWithMultipleSpecialCharacters() {
 final String message = "Hello <World>! This is a &quot;test&quot; with <script>alert('Hello World')</script>. ";
 final String expected = "Hello &lt;World&gt;! This is a &quot;test&quot; with &lt;script&gt;alert('Hello World')&lt;/script&gt;. ";
 assertEquals(expected, filter.filter(message));
 }

 @Test(expected = NullPointerException.class)
 public void testFilterNullCharArray() {
 filter.filter(null);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testFilterEmptyCharArray() {
 char[] content = new char[0];
 filter.filter(content);
 }

 @Test
 public void testFilterMessageWithOnlySpecialCharacters() {
 final String message = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><head><title></title></head><body><script>alert('Hello World')</script></body></html>";
 final String expected = "&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;&lt;html&gt;&lt;head&gt;&lt;title&gt;&lt;/title&gt;&lt;/head&gt;&lt;body&gt;&lt;script&gt;alert('Hello World')&lt;/script&gt;&lt;/body&gt;&lt;/html&gt;";
 assertEquals(expected, filter.filter(message));
 }

 @Test
 public void testFilterMessageWithSpecialCharacterAtStart() {
 final String message = "<html>";
 final String expected = "&lt;html&gt;";
 assertEquals(expected, filter.filter(message));
 }

 @Test
 public void testFilterMessageWithSpecialCharacterAtEnd() {
 final String message = "Hello <World>";
 final String expected = "Hello &lt;World&gt;";
 assertEquals(expected, filter.filter(message));
 }

 @Test
 public void testFilterMessageWithMultipleSpecialCharactersAtEnd() {
 final String message = "Hello <World>&amp;<script>alert('Hello World')</script>&gt;!";
 final String expected = "Hello &lt;World&gt;&amp; &lt;script&gt;alert('Hello World')&lt;/script&gt;&gt;!";
 assertEquals(expected, filter.filter(message));
 }
}
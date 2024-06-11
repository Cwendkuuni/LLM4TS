package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {

 @Test
 public void testFilterNullMessage() {
 assertNull(HTMLFilter.filter(null));
 }

 @Test
 public void testFilterEmptyMessage() {
 assertEquals("", HTMLFilter.filter(""));
 }

 @Test
 public void testFilterMessageWithSpecialCharacters() {
 char[] messageChars = new char[]{'<', '>', '&', '"'};
 String expected = new String(messageChars) + new String(new char[messageChars.length]);
 String result = HTMLFilter.filter(new String(messageChars));
 assertEquals(expected, result);
 }

 @Test
 public void testFilterMessageWithHTMLTags() {
 String message = "<html><body><h1>Hello World</h1></body></html>";
 String expected = "&lt;html&gt;&lt;body&gt;&lt;h1&gt;Hello World&lt;/h1&gt;&lt;/body&gt;&lt;/html&gt;";
 String result = HTMLFilter.filter(message);
 assertEquals(expected, result);
 }

 @Test
 public void testFilterMessageWithHTMLAndSpecialCharacters() {
 String message = "<html><body>&quot;<h1>Hello &amp; World</h1>&gt;</body></html>";
 String expected = "&lt;html&gt;&lt;body&gt;&amp;quot;&lt;h1&gt;Hello &amp;amp; World&lt;/h1&gt;&gt;&lt;/body&gt;&lt;/html&gt;";
 String result = HTMLFilter.filter(message);
 assertEquals(expected, result);
 }

 @Test
 public void testFilterMessageWithComplexHTML() {
 String message = "<html><body><script>alert('Hello World')</script><h1>Hello &amp; World</h1></body></html>";
 String expected = "&lt;html&gt;&lt;body&gt;&lt;script&gt;&amp;lt;alert&amp;gt;&amp;amp;quot;Hello World&amp;amp;lt;/alert&amp;gt;&lt;/script&gt;&lt;h1&gt;Hello &amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;quot; &amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;lt; &amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;lt;amp&gt;&amp;lt;amp&gt; &amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;gt;&amp;gt; &lt;/h1&gt;&lt;/body&gt;&lt;/html&gt;";
 String result = HTMLFilter.filter(message);
 assertEquals(expected, result);
 }
}
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
 public void testFilterSingleCharacter() {
 char[] message = {'A'};
 char[] expected = {'A'};
 char[] content = new char[message.length];
 System.arraycopy(message, 0, content, 0, message.length);
 StringBuffer result = new StringBuffer(content.length + 50);
 HTMLFilter.filter(message, result, 0, message.length);
 char[] resultContent = new char[result.length()];
 result.getChars(0, result.length(), resultContent, 0);
 assertEquals(new String(resultContent), new String(expected));
 }

 @Test
 public void testFilterWithSpecialCharacters() {
 String message = "<html><head><title>&Hello & World&</title></head><body><h1>Hello & World</h1></body></html>";
 String expected = "&lt;html&gt;&lt;head&gt;&lt;title&gt;&amp;Hello &amp; World&amp; &lt;/title&gt;&lt;/head&gt;&lt;body&gt;&lt;h1&gt;Hello &amp; World&lt;/h1&gt;&lt;/body&gt;&lt;/html&gt;";
 StringBuffer result = new StringBuffer(message.length() + 50);
 HTMLFilter.filter(message, result, 0, message.length());
 assertEquals(expected, result.toString());
 }

 @Test
 public void testFilterWithMultipleSpecialCharacters() {
 String message = "This is a <test> message with &amp; special & symbols: '<', '>', '&', '\"' and '>'. ";
 String expected = "This is a &lt;test&gt; message with &amp;amp; special &amp; &amp; symbols: &lt;, &gt;, &amp;, &quot; and &gt;. ";
 StringBuffer result = new StringBuffer(message.length() + 50);
 HTMLFilter.filter(message, result, 0, message.length());
 assertEquals(expected, result.toString());
 }

 @Test
 public void testFilterWithEmptyStringInside() {
 String message = "This is a <test> message with &amp; special & <empty> symbols: '<', '>', '&', '\"' and '>'. ";
 String expected = "This is a &lt;test&gt; message with &amp;amp; special & &lt;empty&gt; symbols: &lt;, &gt;, &amp;, &quot; and &gt;. ";
 StringBuffer result = new StringBuffer(message.length() + 50);
 HTMLFilter.filter(message, result, 0, message.length());
 assertEquals(expected, result.toString());
 }
}
package com.imsmart.servlet.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {

 private final HTMLFilter htmlFilter = new HTMLFilter();

 // Test for filter method with null input
 @Test
 public void testFilterNullInput() {
 assertNull(htmlFilter.filter(null));
 }

 // Test for filter method with empty string input
 @Test
 public void testFilterEmptyInput() {
 assertEquals("", htmlFilter.filter(""));
 }

 // Test for filter method with valid input
 @Test
 public void testFilterValidInput() {
 final String message = "<html><body><h1>Hello World</h1></body></html>";
 final String expected = "&lt;html&gt;&lt;body&gt;&lt;h1&gt;Hello World&lt;/h1&gt;&lt;/body&gt;&lt;/html&gt;";
 assertEquals(expected, htmlFilter.filter(message));
 }

 // Test for filter method with special characters - '<', '>', '&' and '\"'
 @Test
 public void testFilterSpecialCharacters() {
 final String message = "This is a <test> message with & special & characters and a \"quote\".";
 final String expected = "This is a &lt;test&gt; message with &amp; special &amp; characters and a &quot;quote&quot;.";
 assertEquals(expected, htmlFilter.filter(message));
 }

 // Test for filter method with long input
 @Test
 public void testFilterLongInput() {
 final String message = "This is a long message that will test the filter method with a large input.";
 final int lengthBefore = message.length();
 final String filteredMessage = htmlFilter.filter(message);
 final int lengthAfter = filteredMessage.length();
 assertEquals(lengthBefore, lengthAfter);
 }

 // Test for filter method with input containing multiple special characters
 @Test
 public void testFilterMultipleSpecialCharacters() {
 final String message = "<html><body><h1>&amp;<test&gt; &quot;Hello World&quot;</h1></body></html>";
 final String expected = "&lt;html&gt;&lt;body&gt;&lt;h1&gt;&amp;amp;<test&gt; &quot;Hello World&quot;&lt;/h1&gt;&lt;/body&gt;&lt;/html&gt;";
 assertEquals(expected, htmlFilter.filter(message));
 }

 // Test for filter method with input containing script tag
 @Test(expected = IllegalStateException.class)
 public void testFilterScriptTag() {
 final String message = "<html><body><script>alert('Hello World');</script></body></html>";
 }
}
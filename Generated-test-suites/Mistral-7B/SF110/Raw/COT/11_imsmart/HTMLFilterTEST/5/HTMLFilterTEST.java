package com.imsmart.servlet.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HTMLFilterTest {

 private static final HTMLFilter FILTER = new HTMLFilter();

 @Test
 public void testFilterNullMessage() {
 // Test case for filter method with null message
 assertEquals("", FILTER.filter(null));
 }

 @Test
 public void testFilterEmptyMessage() {
 // Test case for filter method with empty message
 assertEquals("", FILTER.filter(""));
 }

 @Test
 public void testFilterSimpleMessage() {
 // Test case for filter method with simple message
 String message = "Hello <world>!";
 String expected = "Hello &lt;world&gt;!";
 assertEquals(expected, FILTER.filter(message));
 }

 @Test
 public void testFilterMessageWithMultipleTags() {
 // Test case for filter method with message containing multiple tags
 String message = "<html><body><h1>Hello <world>!</h1></body></html>";
 String expected = "&lt;html&gt;&lt;body&gt;&lt;h1&gt;Hello &lt;world&gt;!&lt;/h1&gt;&lt;/body&gt;&lt;/html&gt;";
 assertEquals(expected, FILTER.filter(message));
 }

 @Test(expected = NullPointerException.class)
 public void testFilterNullContent() {
 // Test case for filter method with null content
 char[] content = null;
 StringBuffer result = new StringBuffer(content.length + 50);
 FILTER.filter(content, result); // Should throw NullPointerException
 }

 @Test
 public void testFilterMessageWithOnlyWhiteSpaces() {
 // Test case for filter method with message containing only white spaces
 String message = "   ";
 String expected = "   ";
 assertEquals(expected, FILTER.filter(message));
 }

 @Test
 public void testFilterMessageWithSpecialCharacter() {
 // Test case for filter method with message containing special character
 String message = "Hello <special& character>!";
 String expected = "Hello &lt;special&amp; character&gt;!";
 assertEquals(expected, FILTER.filter(message));
 }
}
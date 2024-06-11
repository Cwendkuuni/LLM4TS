package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

 // Test case 1: Filter a valid message with no special characters
 @Test
 public void testFilterValidMessage() {
 String message = "This is a valid message.";
 String expected = "This is a valid message.";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 // Test case 2: Filter a message with special characters - "<"
 @Test
 public void testFilterLessThan() {
 String message = "<html><head></head><body>";
 String expected = "&lt;html&gt;&lt;head&gt;&lt;body&gt;";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 // Test case 3: Filter a message with special characters - ">"
 @Test
 public void testFilterGreaterThan() {
 String message = "This message contains a &gt; symbol.";
 String expected = "This message contains a &gt; symbol.";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 // Test case 4: Filter a message with special characters - "&"
 @Test
 public void testFilterAmpersand() {
 String message = "This message contains an &amp; symbol.";
 String expected = "This message contains an &amp;amp; symbol.";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 // Test case 5: Filter a message with special characters - "\"
 @Test
 public void testFilterQuote() {
 String message = "This message contains a \" symbol.";
 String expected = "This message contains a &quot; symbol.";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 // Test case 6: Filter a null message
 @Test (expected = NullPointerException.class)
 public void testFilterNullMessage() {
 String message = null;
 HTMLFilter.filter(message);
 }

 // Test case 7: Filter an empty message
 @Test
 public void testFilterEmptyMessage() {
 String message = "";
 String expected = "";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 // Test case 8: Filter a long message
 @Test
 public void testFilterLongMessage() {
 String message = "This is a very long message that should not cause any issues.";
 String expected = "This is a very long message that should not cause any issues.";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }
}
package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

 @Test
 public void testFilterNullMessage() {
 String expected = null;
 String actual = HTMLFilter.filter(null);
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterEmptyMessage() {
 String expected = "";
 String actual = HTMLFilter.filter("");
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterSimpleMessage() {
 String expected = "&lt;html&gt;&amp;this&quot; is a simple message&gt;";
 String message = "<html>this is a simple message</html>";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterMessageWithSpecialChars() {
 String expected = "&lt;p&gt;&lt;a href=&quot;http://www.example.com&quot;&gt;Link&lt;/a&gt;&lt;/p&gt;";
 String message = "<p><a href=\"http://www.example.com\">Link</a></p>";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterMessageWithMultipleSpecialChars() {
 String expected = "&lt;p&gt;&lt;a href=&quot;<http://www.example.com>&quot;&gt;Link &lt; with &amp &quot;quotes&quot;&lt;/a&gt;&lt;/p&gt;";
 String message = "<p><a href=\"<http://www.example.com>\">Link < with & amp; \"quotes\"</a></p>";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }
}
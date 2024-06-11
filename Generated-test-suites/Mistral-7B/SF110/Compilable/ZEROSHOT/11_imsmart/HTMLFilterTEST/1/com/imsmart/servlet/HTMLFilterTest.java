package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

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
 String message = "Hello World!";
 String expected = "Hello World!";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterMessageWithSpecialCharacters() {
 String message = "<Hello & World! >";
 String expected = "&lt;Hello &amp; &quot;amp;&quot; &amp; &quot;World! &gt;";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterMessageWithMultipleSpecialCharacters() {
 String message = "<>&\"'<>";
 String expected = "&lt;&gt;&amp;&quot;&apos;&lt;";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterMessageWithLongContent() {
 String message = "This is a long message that contains special characters: <>&\"'<>";
 String expected = "This is a long message that contains special characters: &lt;&gt;&amp;&quot;&apos;&lt;";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }
}
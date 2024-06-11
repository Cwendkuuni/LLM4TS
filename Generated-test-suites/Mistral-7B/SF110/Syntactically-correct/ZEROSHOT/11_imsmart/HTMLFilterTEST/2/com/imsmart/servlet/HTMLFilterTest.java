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
 String expected = "&lt;test&gt;";
 String message = "<test>";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterMessageWithMultipleSpecialCharacters() {
 String expected = "&lt;p&gt;&amp;quot;hello&amp;quot; world!&gt;";
 String message = "<p&quot;hello&quot; world!>";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterMessageWithSpecialCharacterAtBeginning() {
 String expected = "&gt;&lt;test&gt;";
 String message = ">&lt;test>";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterMessageWithSpecialCharacterAtEnd() {
 String expected = "&lt;test&gt;";
 String message = "<test>&gt;";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterMessageWithSpecialCharacterInside() {
 String expected = "&lt;p&gt;&amp;quot;hello&amp;quot; &lt;world&gt;!&gt;";
 String message = "<p&quot;hello &lt;world&gt;!>";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }
}
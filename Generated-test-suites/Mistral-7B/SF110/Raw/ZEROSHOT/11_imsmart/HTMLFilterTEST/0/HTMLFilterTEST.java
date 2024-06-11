package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

 @Test
 public void testNullInput() {
 String expected = null;
 String actual = HTMLFilter.filter(null);
 assertEquals(expected, actual);
 }

 @Test
 public void testEmptyInput() {
 String expected = "";
 String actual = HTMLFilter.filter("");
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterSingleCharacter() {
 char[] inputCharArray = {'a'};
 String expected = "a";
 String actual = HTMLFilter.filter(new String(inputCharArray));
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterSpecialCharacters() {
 char[] inputCharArray = {'<', '>', '&', '"'};
 String expected = "&lt;>&gt;&amp;&quot;";
 String actual = HTMLFilter.filter(new String(inputCharArray));
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterMultipleSpecialCharacters() {
 char[] inputCharArray = {'<', '>', '&', '&', '>', '"', '<', '"', '&'};
 String expected = "&lt;>&gt;&amp;&amp;>&gt;&quot; &quot; &amp;";
 String actual = HTMLFilter.filter(new String(inputCharArray));
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterLongString() {
 String input = "<html><body><h1>Hello & World!</h1></body></html>";
 String expected = "&lt;html&gt;&lt;body&gt;&lt;h1&gt;Hello &amp; World!&lt;/h1&gt;&lt;/body&gt;&lt;/html&gt;";
 String actual = HTMLFilter.filter(input);
 assertEquals(expected, actual);
 }
}
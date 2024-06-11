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
 public void testFilterSingleCharacterMessage() {
 char character = 'A';
 String expected = Character.toString(character);
 String actual = HTMLFilter.filter(Character.toString(character));
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterMessageWithSpecialCharacters() {
 String message = "<>&\"";
 String expected = "&lt;&gt;&amp;&quot;";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterMessageWithMultipleSpecialCharacters() {
 String message = "<>&\"<>&";
 String expected = "&lt;&gt;&amp;&quot; &lt; &gt; &amp; &quot;";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }
}
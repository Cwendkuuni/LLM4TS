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
 public void testFilterMessageContainingLT() {
 String expected = "&lt;";
 String message = "<html>";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual.substring(0, 1));
 }

 @Test
 public void testFilterMessageContainingGT() {
 String expected = "&gt;";
 String message = "><html>";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual.substring(message.length() - 1));
 }

 @Test
 public void testFilterMessageContainingAmpersand() {
 String expected = "&amp;";
 String message = "&hello world&";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual.substring(message.indexOf("&") + 1, message.indexOf("&") + 3));
 }

 @Test
 public void testFilterMessageContainingDoubleQuotes() {
 String expected = "&quot;";
 String message = "\"hello world\"";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual.substring(message.indexOf('"') + 1, message.indexOf('"') + 3));
 }

 @Test
 public void testFilterMessageContainingMixedSpecialCharacters() {
 String expected = "<p>&quot;This is a test with &amp; < &gt; special characters&quot;</p>";
 String message = "<p>&quot;This is a test with &amp; < &gt; special characters&quot;</p>";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterMessageContainingMultipleSpecialCharactersAtOnce() {
 String expected = "&amp;lt;p&amp;gt;&quot;This is a test with &amp;amp; &amp;lt; &amp;gt; special characters&quot;&amp;lt;/p&amp;gt;</p>";
 String message = "&lt;p&gt;&quot;This is a test with & &lt; &gt; special characters&quot;</p>";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

}
package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

 @Test
 public void testNullMessage() {
 String expected = null;
 String actual = HTMLFilter.filter(null);
 assertEquals(expected, actual);
 }

 @Test
 public void testEmptyMessage() {
 String expected = "";
 String actual = HTMLFilter.filter("");
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterSingleCharacter() {
 char message = 'A';
 String expected = "A";
 String actual = HTMLFilter.filter(Character.toString(message));
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterSpecialCharacterLessThan() {
 char message = '<';
 String expected = "&lt;";
 String actual = HTMLFilter.filter(Character.toString(message));
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterSpecialCharacterGreaterThan() {
 char message = '>';
 String expected = "&gt;";
 String actual = HTMLFilter.filter(Character.toString(message));
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterSpecialCharacterAmpersand() {
 char message = '&';
 String expected = "&amp;";
 String actual = HTMLFilter.filter(Character.toString(message));
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterSpecialCharacterDoubleQuote() {
 char message = '\"';
 String expected = "&quot;";
 String actual = HTMLFilter.filter(Character.toString(message));
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterMultipleSpecialCharacters() {
 String message = "<&> & \"";
 String expected = "&lt;&gt; & &quot;";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterMessageWithHTML() {
 String message = "<html><head></head><body><h1>Hello <world></h1></body></html>";
 String expected = "&lt;html&gt;&lt;head&gt;&lt;/head&gt;&lt;body&gt;&lt;h1&gt;Hello &lt;world&gt;&lt;/h1&gt;&lt;/body&gt;&lt;/html&gt;";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }
}
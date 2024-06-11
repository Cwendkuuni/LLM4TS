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
 public void testFilterSingleLetter() {
 char letter = 'A';
 String expected = Character.toString(letter);
 String actual = HTMLFilter.filter(Character.toString(letter));
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterSingleLessThan() {
 char lessThan = '<';
 String expected = "&lt;";
 String actual = HTMLFilter.filter(Character.toString(lessThan));
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterSingleGreaterThan() {
 char greaterThan = '>';
 String expected = "&gt;";
 String actual = HTMLFilter.filter(Character.toString(greaterThan));
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterSingleAmpersand() {
 char ampersand = '&';
 String expected = "&amp;";
 String actual = HTMLFilter.filter(Character.toString(ampersand));
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterSingleDoubleQuote() {
 char doubleQuote = '\"';
 String expected = "&quot;";
 String actual = HTMLFilter.filter(Character.toString(doubleQuote));
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterMultipleSpecialCharacters() {
 String input = "<&>&\"";
 String expected = "&lt;&amp;&gt;--&quot;";
 String actual = HTMLFilter.filter(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterMultipleLetters() {
 String input = "abcdef";
 String expected = "abcdef";
 String actual = HTMLFilter.filter(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterMessageWithSpecialCharactersAndLetters() {
 String input = "<abcd>&efg&\"hij>";
 String expected = "&lt;abcd--&amp;--&gt;--efg--&amp;----&quot;--hij";
 String actual = HTMLFilter.filter(input);
 assertEquals(expected, actual);
 }
}
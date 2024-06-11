package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {

 @Test
 public void testFilterNullInput() {
 String input = null;
 String expectedOutput = null;
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterEmptyInput() {
 String input = "";
 String expectedOutput = "";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterSimpleInput() {
 String input = "Hello World!";
 String expectedOutput = "Hello World!";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterInputWithLessThanSymbol() {
 String input = "<html>";
 String expectedOutput = "&lt;html&gt;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterInputWithGreaterThanSymbol() {
 String input = ">Hello World!";
 String expectedOutput = "&gt;Hello World!";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterInputWithAmpersandSymbol() {
 String input = "& Hello World!";
 String expectedOutput = "&amp; &hellip; World!";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterInputWithDoubleQuotes() {
 String input = "\"Hello World!\"";
 String expectedOutput = "&quot;Hello World!&quot;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterInputWithMixedSpecialCharacters() {
 String input = "<& \"Hello World!\" >";
 String expectedOutput = "&lt;&amp; &quot;Hello World!&quot; &gt;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test(expected = NullPointerException.class)
 public void testFilterInputWithNullCharacterArray() {
 char[] input = null;
 String expectedOutput = null;
 String output = new String(HTMLFilter.filter(input));
 assertEquals(expectedOutput, output);
 }
}
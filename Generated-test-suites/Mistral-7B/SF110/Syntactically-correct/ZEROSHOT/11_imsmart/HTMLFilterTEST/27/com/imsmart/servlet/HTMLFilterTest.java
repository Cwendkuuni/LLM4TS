package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {

 @Test
 public void testNullInput() {
 String input = null;
 String expectedOutput = null;
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEmptyInput() {
 String input = "";
 String expectedOutput = "";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterSimpleCharacters() {
 String input = "Hello World!";
 String expectedOutput = "Hello World!";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterSingleLessThanSymbol() {
 String input = "<";
 String expectedOutput = "&lt;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterSingleGreaterThanSymbol() {
 String input = ">";
 String expectedOutput = "&gt;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterSingleAmpersandSymbol() {
 String input = "&";
 String expectedOutput = "&amp;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterSingleDoubleQuoteSymbol() {
 String input = "\"";
 String expectedOutput = "&quot;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMultipleSpecialCharacters() {
 String input = "<&>";
 String expectedOutput = "&lt; &amp;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMultipleSpecialCharactersWithText() {
 String input = "<h1>&quot;Hello World&quot;<&gt;";
 String expectedOutput = "&lt;h1&gt; &amp;quot;Hello World&amp;quot;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }
}
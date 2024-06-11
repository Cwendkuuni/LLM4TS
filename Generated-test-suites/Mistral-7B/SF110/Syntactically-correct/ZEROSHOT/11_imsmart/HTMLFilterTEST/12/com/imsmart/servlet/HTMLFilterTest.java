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
 public void testFilterSingleLessThan() {
 String input = "<";
 String expectedOutput = "&lt;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterSingleGreaterThan() {
 String input = ">";
 String expectedOutput = "&gt;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterSingleAmpersand() {
 String input = "&";
 String expectedOutput = "&amp;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterSingleQuote() {
 String input = "\"";
 String expectedOutput = "&quot;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMultipleSpecialCharacters() {
 String input = "&><\"";
 String expectedOutput = "&amp; &lt; &gt; &quot;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMultipleSpecialAndNormalCharacters() {
 String input = "Hello <World>! This is a test.";
 String expectedOutput = "Hello &amp;lt;World&amp;gt;! This is a test.";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

}
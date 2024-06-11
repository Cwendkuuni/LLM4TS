package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

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
 String input = "<&>";
 String expectedOutput = "&lt;&amp;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMultipleSpecialAndNormalCharacters() {
 String input = "<h1>Hello & World!</h1>";
 String expectedOutput = "&lt;h1&gt;Hello &amp; World!&lt;/h1&gt;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test(expected = NullPointerException.class)
 public void testFilterNullCharacterArray() {
 char[] input = null;
 String output = HTMLFilter.filter(input);
 fail("Expected NullPointerException, but got: " + output);
 }

 @Test
 public void testFilterLongInput() {
 String input = "This is a long string that we want to filter, let's see if our filter can handle it.";
 String expectedOutput = "This is a long string that we want to filter, let's see if our filter can handle it.";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }
}
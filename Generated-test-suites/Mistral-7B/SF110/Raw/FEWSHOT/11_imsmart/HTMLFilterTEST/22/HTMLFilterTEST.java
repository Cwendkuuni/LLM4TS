package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {

 @Test
 public void testFilterNullMessage() {
 assertNull(HTMLFilter.filter(null));
 }

 @Test
 public void testFilterEmptyMessage() {
 assertEquals("", HTMLFilter.filter(""));
 }

 @Test
 public void testFilterMessageWithNoSpecialChars() {
 final String input = "This is a test.";
 final String expectedOutput = "This is a test.";
 assertEquals(expectedOutput, HTMLFilter.filter(input));
 }

 @Test
 public void testFilterMessageWithSingleLessThanSymbol() {
 final String input = "<hello>";
 final String expectedOutput = "&lt;hello&gt;";
 assertEquals(expectedOutput, HTMLFilter.filter(input));
 }

 @Test
 public void testFilterMessageWithSingleGreaterThanSymbol() {
 final String input = "hello>";
 final String expectedOutput = "hello&gt;";
 assertEquals(expectedOutput, HTMLFilter.filter(input));
 }

 @Test
 public void testFilterMessageWithSingleAmpersandSymbol() {
 final String input = "&world";
 final String expectedOutput = "&amp;world";
 assertEquals(expectedOutput, HTMLFilter.filter(input));
 }

 @Test
 public void testFilterMessageWithSingleQuotationMark() {
 final String input = "'hello'";
 final String expectedOutput = "&quot;'hello'&quot;";
 assertEquals(expectedOutput, HTMLFilter.filter(input));
 }

 @Test
 public void testFilterMessageWithMultipleSpecialChars() {
 final String input = "<&>hello&world>";
 final String expectedOutput = "&lt;&amp;gt;hello&amp;amp;amp;amp;world";
 assertEquals(expectedOutput, HTMLFilter.filter(input));
 }
}
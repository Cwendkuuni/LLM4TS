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
 public void testFilterSimpleText() {
 String input = "This is a simple text.";
 String expectedOutput = "This is a simple text.";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterTextWithLessThanSign() {
 String input = "This text contains < a tag.";
 String expectedOutput = "This text contains &lt; a tag.";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterTextWithGreaterThanSign() {
 String input = "This text contains > a tag.";
 String expectedOutput = "This text contains &gt; a tag.";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterTextWithAmpersand() {
 String input = "This text contains & a tag.";
 String expectedOutput = "This text contains &amp; a tag.";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterTextWithDoubleQuotes() {
 String input = "This text contains \" a tag.";
 String expectedOutput = "This text contains &quot; a tag.";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterTextWithMixedSpecialChars() {
 String input = "This text contains <a & \"tag>.";
 String expectedOutput = "This text contains &lt;a &amp; &quot;tag>.";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test(expected = NullPointerException.class)
 public void testFilterNullCharacterArray() {
 char[] input = null;
 String expectedOutput = null;
 String output = new String(HTMLFilter.filter(input));
 assertEquals(expectedOutput, output);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testFilterEmptyCharacterArray() {
 char[] input = new char[0];
 String expectedOutput = "";
 String output = new String(HTMLFilter.filter(input));
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterLongText() {
 String input = "This is a long text that needs to be filtered."
 + " It contains several special characters like <, >, &, \" and '.";
 String expectedOutput = "This is a long text that needs to be filtered."
 + " It contains several special characters like &lt;, &gt;, &amp;, &quot; and &apos;.";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }
}
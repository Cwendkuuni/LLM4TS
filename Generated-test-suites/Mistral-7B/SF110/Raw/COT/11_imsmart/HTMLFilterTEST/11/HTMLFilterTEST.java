package com.imsmart.servlet.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HTMLFilterTest {

 private final static String NEW_LINE = "\n";

 @Test
 public void testFilterNullInput() {
 expectedNull(null);
 }

 @Test
 public void testFilterEmptyInput() {
 String input = "";
 String expectedOutput = "";
 testFilter(input, expectedOutput);
 }

 @Test
 public void testFilterSimpleInput() {
 String input = "Hello <World>";
 String expectedOutput = "Hello &lt;World&gt;";
 testFilter(input, expectedOutput);
 }

 @Test
 public void testFilterInputWithNewLine() {
 String input = "Hello <World>\n";
 String expectedOutput = "Hello &lt;World&gt;" + NEW_LINE;
 testFilter(input, expectedOutput);
 }

 @Test
 public void testFilterInputWithMultipleSpecialChars() {
 String input = "Hello <World> &This is & a &test&>";
 String expectedOutput = "Hello &lt;World&gt; &amp;This is &amp; a &amp;test&gt;";
 testFilter(input, expectedOutput);
 }

 @Test(expected = NullPointerException.class)
 public void testFilterInputWithNullCharacterArray() {
 char[] content = null;
 String input = new String(content);
 String expectedOutput = null;
 testFilter(input, expectedOutput);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testFilterInputWithEmptyCharacterArray() {
 char[] content = new char[0];
 String input = new String(content);
 String expectedOutput = "";
 testFilter(input, expectedOutput);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFilterInputWithLongMessage() {
 String longMessage = new String(new char[Integer.MAX_VALUE]);
 String expectedOutput = null;
 testFilter(longMessage, expectedOutput);
 }

 private void testFilter(String input, String expectedOutput) {
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 private void expectedNull(Object object) {
 if (object == null) {
 assertEquals(null, object);
 } else {
 fail("Expected null, but received: " + object);
 }
 }

}
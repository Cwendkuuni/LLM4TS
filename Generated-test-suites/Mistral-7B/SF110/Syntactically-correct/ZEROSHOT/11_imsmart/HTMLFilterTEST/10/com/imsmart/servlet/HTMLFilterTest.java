package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {

 @Test
 public void testNullMessage() {
 String input = null;
 String expectedOutput = null;
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEmptyMessage() {
 String input = "";
 String expectedOutput = "";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterSingleCharacter() {
 char character = 'A';
 String input = Character.toString(character);
 String expectedOutput = Character.toString(character);
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterLessThanSymbol() {
 String input = "<";
 String expectedOutput = "&lt;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterGreaterThanSymbol() {
 String input = ">";
 String expectedOutput = "&gt;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterAmpersandSymbol() {
 String input = "&";
 String expectedOutput = "&amp;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterDoubleQuotationSymbol() {
 String input = "\"";
 String expectedOutput = "&quot;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterSpecialCharacter() {
 char character = '&';
 String input = Character.toString(character) + "<script>alert('XSS')</script>";
 String expectedOutput = Character.toString(character) + "&lt;script&gt;alert('XSS')&lt;/script&gt;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterComplexMessage() {
 String input = "This is a <strong>test</strong> message with & special <script>JavaScript</script> and <a href='http://www.google.com'>links</a>.";
 String expectedOutput = "This is a &lt;strong&gt;test&lt;/strong&gt; message with &amp; special &lt;script&gt;JavaScript&lt;/script&gt; and &lt;a href='http://www.google.com'&gt;links&lt;/a&gt;.";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test(expected = NullPointerException.class)
 public void testFilterNullCharacterArray() {
 char[] input = null;
 String expectedOutput = null;
 String output = new String(HTMLFilter.filter(input)); // This line causes the NullPointerException
 assertEquals(expectedOutput, output);
 }
}
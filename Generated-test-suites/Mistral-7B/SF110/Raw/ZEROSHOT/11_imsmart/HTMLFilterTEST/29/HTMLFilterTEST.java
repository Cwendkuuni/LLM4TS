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
 public void testFilterLessThan() {
 String input = "<";
 String expectedOutput = "&lt;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterGreaterThan() {
 String input = ">";
 String expectedOutput = "&gt;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterAmpersand() {
 String input = "&";
 String expectedOutput = "&amp;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterDoubleQuote() {
 String input = "\"";
 String expectedOutput = "&quot;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterSpecialCharacterSequence() {
 String input = "<tag name=\"attribute\" value=\"&quot;Hello World&quot; &amp; &lt; &gt; &amp; &gt;\" />";
 String expectedOutput = "&lt;tag name=&quot;attribute&quot; value=&quot; &amp; &amp; &lt; &amp; &gt; &amp; &amp;gt;&quot; &gt;/>";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterLongMessage() {
 String longMessage = "This is a long message that needs to be filtered to ensure that all special characters are properly encoded.";
 String expectedOutput = "This is a long message that needs to be filtered to ensure that all special characters are properly encoded.";
 String output = HTMLFilter.filter(longMessage);
 assertEquals(expectedOutput, output);
 }

}
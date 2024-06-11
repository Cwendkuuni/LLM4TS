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
 public void testFilterSingleCharacter() {
 String input = "A";
 String expectedOutput = "A";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMultipleSymbols() {
 String input = "<>&\"";
 String expectedOutput = "&lt; &amp; &quot;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMultipleSymbolsWithText() {
 String input = "This is a <test> message with <special> characters.";
 String expectedOutput = "This is a &lt;test&gt; message with &lt;special&gt; characters.";
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

 @Test
 public void testFilterLongMessage() {
 String input = "This is a very very very very very very very long message that needs to be filtered.";
 String expectedOutput = "This is a very very very very very very very long message that needs to be filtered.";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }
}
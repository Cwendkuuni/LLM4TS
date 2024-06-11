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
 public void testFilterSingleCharacter() {
 char[] message = {'a'};
 String expectedOutput = "a";
 String output = HTMLFilter.filter(new String(message));
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterSingleCharacterWithSpecialSymbol() {
 char[] message = {'<'};
 String expectedOutput = "&lt;";
 String output = HTMLFilter.filter(new String(message));
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMultipleCharacters() {
 char[] message = {'<', 'h', 'e', 'l', 'l', 'o', '>', ' ', 'w', 'o', 'r', 'l', 'd', '!'};
 String expectedOutput = "&lt;hello&gt; world!";
 String output = HTMLFilter.filter(new String(message));
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMultipleSpecialSymbols() {
 char[] message = {'&', '&', '<', '>', '&'};
 String expectedOutput = "&amp;&amp;&lt;&gt;&amp;";
 String output = HTMLFilter.filter(new String(message));
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMultipleQuotes() {
 char[] message = {'"', '"', '"'};
 String expectedOutput = "&quot;&quot;&quot;";
 String output = HTMLFilter.filter(new String(message));
 assertEquals(expectedOutput, output);
 }

 @Test(expected = NullPointerException.class)
 public void testFilterNullCharacterArray() {
 char[] message = null;
 String output = HTMLFilter.filter(new String(message));
 // JUnit assertion is not needed here as we expect an exception
 }
}
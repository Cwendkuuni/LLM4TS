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
 char c = 'A';
 String input = Character.toString(c);
 String expectedOutput = Character.toString(c);
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterSpecialCharacters() {
 String input = "<>&\"";
 String expectedOutput = "&lt;&gt;&amp;&quot;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterLongString() {
 String input = "<h1>Hello <world> & this is a <test> string</h1>";
 String expectedOutput = "&lt;h1&gt;Hello &lt;world&gt; &amp; this is a &lt;test&gt; string&lt;/h1&gt;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }
}
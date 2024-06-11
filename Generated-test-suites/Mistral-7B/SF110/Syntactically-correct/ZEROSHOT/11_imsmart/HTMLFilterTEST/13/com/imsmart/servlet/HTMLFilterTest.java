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
 String input = new String(message);
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterAscii() {
 String input = "This is a <test> message.";
 String expectedOutput = "This is a &lt;test&gt; message.";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterSpecialCharacters() {
 String input = "<html><body><h1>&quot;Hello, World!&quot;</h1></body></html>";
 String expectedOutput = "&lt;html&gt;&lt;body&gt;&lt;h1&gt;&amp;quot;Hello, World!&amp;quot;&lt;/h1&gt;&lt;/body&gt;&lt;/html&gt;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMultipleSpecialCharacters() {
 String input = "This is a <test &quot;message&quot; with <b>HTML</b> &amp; &lt; tags!";
 String expectedOutput = "This is a &lt;test &amp;quot;message&amp;quot; with &lt;b&gt;HTML&lt;/b&gt; &amp; &lt;&gt; tags!";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }
}
package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {

 @Test
 public void testFilterNullInput() {
 String input = null;
 String expectedOutput = null;
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterEmptyInput() {
 String input = "";
 String expectedOutput = "";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterSimpleMessageWithSpecialCharacters() {
 String input = "<>&\"";
 String expectedOutput = "&lt;&gt;&amp;&quot;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMessageWithMultipleSpecialCharacters() {
 String input = "<abcd>&efgh>ijk<lmnop&qrst>";
 String expectedOutput = "&lt;abcd&gt;&amp;amp;&gt;efgh&amp;gt;&lt;ijk&gt;&amp;amp;&lt;lmnop&amp;amp;&gt;qrst";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMessageWithMultipleSpecialCharactersAndText() {
 String input = "The message contains <special> characters: &amp; <tag> and &quot;quoted text&quot;.";
 String expectedOutput = "The message contains &amp;amp; special&gt;&gt; characters: &amp; &amp; &lt;tag&gt; and &amp;quot;quoted text&amp;quot;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMessageWithLongInput() {
 String input = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.";
 String expectedOutput = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

}
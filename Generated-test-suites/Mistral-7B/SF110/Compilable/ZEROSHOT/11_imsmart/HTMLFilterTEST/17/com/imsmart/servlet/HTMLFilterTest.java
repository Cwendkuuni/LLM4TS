package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

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
 public void testFilterSimpleMessage() {
 String input = "Hello <World>!";
 String expectedOutput = "Hello &lt;World&gt;!";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMessageWithMultipleSpecialChars() {
 String input = "<HTML><HEAD><TITLE>&Quoted Title&</TITLE></HEAD><BODY>";
 String expectedOutput = "&lt;HTML&gt;&lt;HEAD&gt;&lt;TITLE&gt;&amp;Quoted Title;&lt;/TITLE&gt;&lt;/HEAD&gt;&lt;BODY&gt;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMessageWithSpecialCharsAtStartAndEnd() {
 String input = "&lt;HTML&gt;&lt;HEAD&gt;&lt;TITLE&gt;&amp;Quoted Title&lt;/TITLE&gt;&lt;/HEAD&gt;&lt;BODY&gt;";
 String expectedOutput = "&amp;lt;HTML&amp;gt;&amp;lt;HEAD&amp;gt;&amp;lt;TITLE&amp;gt;&amp;amp;Quoted Title;&amp;lt;/TITLE&amp;gt;&amp;lt;/HEAD&amp;gt;&amp;lt;BODY&amp;gt;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMessageWithSpecialCharsInTheMiddle() {
 String input = "This is a &test& message.";
 String expectedOutput = "This is a &amp;test&amp; message.";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMessageWithSpecialCharsInsideSpecialChars() {
 String input = "This is a <test of &amp; &gt; filter &lt; &gt;.";
 String expectedOutput = "This is a &lt;test of &amp; &amp;&gt; &amp;filter&amp; &lt;&gt;.";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

}
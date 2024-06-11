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
 String input = "This is a <test> message with a &quot;special&quot; <char>.";
 String expectedOutput = "This is a &lt;test&gt; message with a &quot;special&quot; &lt;char&gt;.";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMessageWithSpecialCharAtFirstPosition() {
 String input = "<html>This is a test message.";
 String expectedOutput = "&lt;html&gt;This is a test message.";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMessageWithSpecialCharAtLastPosition() {
 String input = "This is a test message.<html>";
 String expectedOutput = "This is a test message.<html>&gt;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMessageWithMultipleSpecialCharsAtSamePosition() {
 String input = "This is a <test><message> with <special> characters.";
 String expectedOutput = "This is a &lt;test&gt;&lt;message&gt; with &lt;special&gt; characters.";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMessageWithMultipleSpecialCharsAtDifferentPositions() {
 String input = "This is a <test> message with &amp; &quot;special&quot; <char>.";
 String expectedOutput = "This is a &lt;test&gt; message with &amp; &quot;special&quot; &lt;char&gt;.";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }
}
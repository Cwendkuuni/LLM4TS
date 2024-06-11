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
 String input = "The <quick> brown <fox> jumps over the &amp;quot;lazy&amp;quot; dog.";
 String expectedOutput = "The &lt;quick&gt; &lt;brown&gt; jumps over the &amp;quot;lazy&amp;quot; dog.";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMessageWithMultipleSpecialCharsInASingleChar() {
 String input = "The &amp;&amp; message.";
 String expectedOutput = "The &amp;amp; message.";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMessageWithSpecialCharsAtBeginningAndEnd() {
 String input = "<html><body>&lt;p&gt;Hello, &amp; World! &gt;</body></html>";
 String expectedOutput = "&lt;html&gt;&lt;body&gt;&amp;lt;p&gt;Hello, &amp; World! &amp;gt;</body&gt;&lt;/html&gt;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMessageWithSpecialCharsInTheMiddle() {
 String input = "1<sup>st</sup> &ndash; 2<sup>nd</sup>";
 String expectedOutput = "1<sup>st</sup> &ndash; 2<sup>nd</sup>";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }
}
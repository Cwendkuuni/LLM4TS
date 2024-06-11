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
 public void testFilterSingleCharacter() {
 char[] message = {'A'};
 String expectedOutput = "A";
 String output = HTMLFilter.filter(new String(message));
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
 public void testFilterLongInput() {
 String input = "<html><head><title>Test Title</title></head><body><p>Test &amp; Special &lt;characters&gt;.</p></body></html>";
 String expectedOutput = "&lt;html&gt;&lt;head&gt;&lt;title&gt;Test Title&lt;/title&gt;&lt;body&gt;&lt;p&gt;Test &amp; Special &lt;characters&gt;&lt;/p&gt;&lt;/body&gt;&lt;/html&gt;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }
}
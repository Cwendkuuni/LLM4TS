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
 public void testFilterSimpleString() {
 String input = "Hello World!";
 String expectedOutput = "Hello World!";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterStringWithSpecialCharacters() {
 String input = "<html><head><title>Test</title></head><body><p>This is a &quot;test&quot;.</p></body></html>";
 String expectedOutput = "&lt;html&gt;&lt;head&gt;&lt;title&gt;Test&lt;/title&gt;&lt;body&gt;&lt;p&gt;This is a &amp;quot;test&amp;quot;.&lt;/p&gt;&lt;/body&gt;&lt;/html&gt;";
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test(expected = NullPointerException.class)
 public void testFilterNullCharacterArray() {
 char[] input = null;
 String expectedOutput = null;
 String output = new String(HTMLFilter.filter(input)); // This line will throw NullPointerException
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterCharacterArray() {
 char[] input = {'<', 'h', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!', '>'};
 char[] expectedOutput = {'&', 'l', 't', ';', 'h', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!', ';'};
 char[] output = HTMLFilter.filter(input);
 assert ArrayEquals(expectedOutput, output);
 }
}
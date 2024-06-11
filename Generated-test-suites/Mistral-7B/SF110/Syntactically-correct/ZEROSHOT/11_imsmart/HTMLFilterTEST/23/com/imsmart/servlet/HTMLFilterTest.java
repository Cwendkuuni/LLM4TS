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
 public void testFilterSingleCharacters() {
 char[] message = new char[]{'h', 'e', 'l', 'l', 'o'};
 String expectedOutput = "hello";
 String input = new String(message);
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterSpecialChars() {
 char[] message = new char[]{'<', '>', '&', '\"'};
 String expectedOutput = "&lt;&gt;&amp;&quot;";
 String input = new String(message);
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMixedChars() {
 char[] message = new char[]{'h', 'e', 'l', 'l', 'o', '<', 'p', '>', '&', ' ', 'q', 'u', 'o', 't', '\"'};
 String expectedOutput = "hello&lt;p&gt;&amp; &qout;";
 String input = new String(message);
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }
}
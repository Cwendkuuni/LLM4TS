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
 public void testFilterSingleChar() {
 char[] message = new char[]{'a'};
 String expectedOutput = "a";
 String input = new String(message);
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterSingleHTMLTag() {
 char[] message = new char[]{'<', 'h', 'e', 'l', 'l', 'o'};
 String expectedOutput = "&lt;h&lt;e&lt;l&lt;l&lt;o";
 String input = new String(message);
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterSingleHTMLAttribute() {
 char[] message = new char[]{'<', 'i', 'm', 'g', ' ', 's', 'o', 'r', 'c', 'e', '=', ' ', 'h', 't', 't', 'p', ':', '/', '/', 'a', '/', 'p', 'p', 'l', 'e', '/'};
 String expectedOutput = "&lt;img src=&quot;appllle&quot;/&gt;";
 String input = new String(message);
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMultipleHTMLTags() {
 char[] message = new char[]{'<', 'h', '1', ' ', '<', 'h', '2', ' ', 'h', '3', '>', 'h', '4', ' ', 'h', '5', '>', 'h', '6', '>'};
 String expectedOutput = "&lt;h1&gt;&lt;h2&gt;&lt;h3&gt;&gt;&lt;h4&gt;&lt;h5&gt;&gt;&gt;";
 String input = new String(message);
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testFilterMultipleHTMLTagsWithAttributes() {
 char[] message = new char[]{'<', 'a', ' ', 'h', '1', ' ', 'c', 'l', 'a', 's', 's', '=', '"', 'H', 'e', 'l', 'l', 'o', ' ', 'h', '2', ' ', 'i', 'm', 'g', ' ', 's', 'o', 'r', 'c', 'e', '=', '"', 'a', '/', 'p', 'p', 'l', 'e', '/', 'a', '/', 'h', '1', '>', '&lt;h2&gt;', 'i', 'm', ' ', 'g', 'o', 'o', 'd', ' ', 'f', 'o', 'o', 'd', ' ', '&gt;'};
 String expectedOutput = "&lt;a class=&quot;Hello&quot; href=&quot;appllle/apple&quot;&gt;&lt;h1&gt;&lt;h2&gt;img good food&lt;/h2&gt;&gt;";
 String input = new String(message);
 String output = HTMLFilter.filter(input);
 assertEquals(expectedOutput, output);
 }

}
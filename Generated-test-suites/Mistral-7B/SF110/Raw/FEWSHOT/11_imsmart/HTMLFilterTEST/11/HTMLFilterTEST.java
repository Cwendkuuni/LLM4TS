package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {

 @Test
 public void testFilterNullInput() {
 assertNull(HTMLFilter.filter(null));
 }

 @Test
 public void testFilterEmptyInput() {
 assertEquals("", HTMLFilter.filter(""));
 }

 @Test
 public void testFilterSimpleInput() {
 final String input = "Hello World!";
 final String output = "Hello World!";
 assertEquals(output, HTMLFilter.filter(input));
 }

 @Test
 public void testFilterInputWithSpecialCharacters() {
 final String input = "<html><head><title>My Title</title></head><body><p>Hello & World! &quot;<tag></tag>&amp; some content</p></body></html>";
 final String output = "&lt;html&gt;&lt;head&gt;&lt;title&gt;My Title&lt;/title&gt;&lt;body&gt;&lt;p&gt;Hello &amp; World! &quot;&lt;tag&gt;&amp; some content&lt;/p&gt;&lt;/body&gt;&lt;/html&gt;";
 assertEquals(output, HTMLFilter.filter(input));
 }

 @Test
 public void testFilterInputWithMultipleSpecialCharacters() {
 final String input = "&Hello <World!> &\"<tag></tag>&amp; some &content&";
 final String output = "&amp;&amp;Hello &amp;amp; &amp;Amp; &lt;Hello &gt; &amp; &quot; &lt;&lt;tag&gt;&gt; &amp;amp; some &amp;amp; &amp;content&amp;";
 assertEquals(output, HTMLFilter.filter(input));
 }

 @Test
 public void testFilterInputWithOnlySpecialCharacters() {
 final String input = "&";
 final String output = "&amp;";
 assertEquals(output, HTMLFilter.filter(input));
 }
}
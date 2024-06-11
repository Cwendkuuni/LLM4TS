package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {

 @Test
 public void testFilterNullInput() {
 assertEquals(null, HTMLFilter.filter(null));
 }

 @Test
 public void testFilterEmptyInput() {
 assertEquals("", HTMLFilter.filter(""));
 }

 @Test
 public void testFilterSingleCharacter() {
 char[] message = new char[]{'A'};
 char[] expected = new char[]{'A'};
 assertEquals(String.valueOf(expected), HTMLFilter.filter(new String(message)));
 }

 @Test
 public void testFilterWithSpecialCharacters() {
 char[] message = new char[]{'<', '>', '&', '>', '"', '<', '&', '>'};
 char[] expected = new char[]{'&lt;', '&gt;', '&amp;', '&gt;', '&quot;', '&lt;', '&amp;', '&gt;'};
 assertEquals(String.valueOf(expected), HTMLFilter.filter(new String(message)));
 }

 @Test
 public void testFilterLongInput() {
 String message = "This is a <test> message with & special & characters: <abcd> and <efgh> and &amp; &quot;quotes&quot;.";
 String expected = "This is a &lt;test&gt; message with &amp; &amp;special&amp; &amp; characters: &lt;abcd&gt; and &lt;efgh&gt; and &amp; &amp;quot;quotes&amp;quot;.";
 assertEquals(expected, HTMLFilter.filter(message));
 }
}
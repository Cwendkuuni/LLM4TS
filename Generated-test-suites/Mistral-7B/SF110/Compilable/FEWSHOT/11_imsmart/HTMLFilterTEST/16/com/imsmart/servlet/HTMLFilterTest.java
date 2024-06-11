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
 public void testFilterSimpleInput() {
 char[] content = new char[]{'H', 'e', 'l', 'l', 'o'};
 StringBuffer result = new StringBuffer();
 for (int i = 0; i < content.length; ++i) {
 result.append(content[i]);
 }
 String message = new String(content);
 assertEquals(HTMLFilter.filter(message), result.toString());
 }

 @Test
 public void testFilterInputWithSpecialChars() {
 char[] content = new char[]{'<', 'h', 'e', 'l', 'l', 'o', '>', ' ', '&', '\"'};
 StringBuffer result = new StringBuffer();
 result.append("&lt;h&amp;llo&gt; &amp; &quot;");
 String message = new String(content);
 assertEquals(HTMLFilter.filter(message), result.toString());
 }

 @Test
 public void testFilterInputWithMultipleSpecialChars() {
 char[] content = new char[]{'<', 'h', 'e', 'l', 'l', 'o', '>', '&', '&', '&', '>'};
 StringBuffer result = new StringBuffer();
 result.append("&lt;h&amp;llo&gt; &amp; &amp; &amp; &gt;");
 String message = new String(content);
 assertEquals(HTMLFilter.filter(message), result.toString());
 }
}
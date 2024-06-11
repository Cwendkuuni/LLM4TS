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
 public void testFilterEmptyString() {
 assertEquals("", HTMLFilter.filter(""));
 }

 @Test
 public void testFilterSingleCharacter() {
 char[] message = {'a'};
 char[] expected = {'a'};
 char[] content = new char[message.length];
 System.arraycopy(message, 0, content, 0, message.length);
 StringBuffer result = new StringBuffer(content.length + 50);
 for (int i = 0; i < content.length; ++i) {
 result.append(content[i]);
 }
 assertEquals(new String(expected), HTMLFilter.filter(new String(message)));
 }

 @Test
 public void testFilterSingleSpecialCharacter() {
 char[] message = {'<'};
 char[] expected = {'&lt;'};
 char[] content = new char[message.length];
 System.arraycopy(message, 0, content, 0, message.length);
 StringBuffer result = new StringBuffer(content.length + 50);
 for (int i = 0; i < content.length; ++i) {
 result.append(content[i]);
 }
 assertEquals(new String(expected), HTMLFilter.filter(new String(message)));
 }

 @Test
 public void testFilterMultipleSpecialCharacters() {
 char[] message = {'<', '&', '>', '>', '&', '"'};
 char[] expected = {'&lt;', '&amp;', '&gt;', '&gt;', '&amp;', '&quot;'};
 char[] content = new char[message.length];
 System.arraycopy(message, 0, content, 0, message.length);
 StringBuffer result = new StringBuffer(content.length + 50);
 for (int i = 0; i < content.length; ++i) {
 result.append(content[i]);
 }
 assertEquals(new String(expected), HTMLFilter.filter(new String(message)));
 }

 @Test
 public void testFilterComplexString() {
 String message = "<p>&quot;Hello, <b>world!</b>&quot;</p>";
 String expected = "&lt;p&gt;&amp;quot;Hello, &lt;b&gt;world!&lt;/b&gt;&amp;quot;";
 assertEquals(expected, HTMLFilter.filter(message));
 }
}
package com.imsmart.servlet;

import static org.junit.Assert.*;

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
 public void testFilterNoSpecialChars() {
 assertEquals("foo", HTMLFilter.filter("foo"));
 }

 @Test
 public void testFilterSingleLessThan() {
 assertEquals("&lt;", HTMLFilter.filter("<"));
 }

 @Test
 public void testFilterSingleGreaterThan() {
 assertEquals("&gt;", HTMLFilter.filter(">"));
 }

 @Test
 public void testFilterSingleAmpersand() {
 assertEquals("&amp;", HTMLFilter.filter("&"));
 }

 @Test
 public void testFilterSingleQuotes() {
 assertEquals("&quot;", HTMLFilter.filter("\""));
 }

 @Test
 public void testFilterMultipleSpecialChars() {
 assertEquals("<foo>&bar</foo>", HTMLFilter.filter("<foo>&bar</foo>"));
 }

 @Test
 public void testFilterMultipleSpecialCharsInARow() {
 assertEquals("&amp;lt;a&gt; &lt;b&gt; &amp;gt;c&amp;quot;", HTMLFilter.filter("&lt;a&gt; &lt;b&gt; &gt;c&quot;"));
 }

 @Test
 public void testFilterMixedSpecialAndOtherChars() {
 assertEquals("&lt;foo&gt;bar&amp;quot;baz&lt;baz&gt;", HTMLFilter.filter("<foo>bar&quot;baz<baz>"));
 }

}
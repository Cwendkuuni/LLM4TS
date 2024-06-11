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
 public void testFilterNoSpecialCharacters() {
 assertEquals("foo", HTMLFilter.filter("foo"));
 }

 @Test
 public void testFilterSingleLessThanSymbol() {
 assertEquals("&lt;", HTMLFilter.filter("<"));
 }

 @Test
 public void testFilterSingleGreaterThanSymbol() {
 assertEquals("&gt;", HTMLFilter.filter(">"));
 }

 @Test
 public void testFilterSingleAmpersandSymbol() {
 assertEquals("&amp;", HTMLFilter.filter("&"));
 }

 @Test
 public void testFilterSingleDoubleQuoteSymbol() {
 assertEquals("&quot;", HTMLFilter.filter("\""));
 }

 @Test
 public void testFilterMultipleSpecialCharacters() {
 assertEquals("&lt;foo&gt;&amp;&quot;bar&quot;<b&gt;baz&lt;/b&gt;", HTMLFilter.filter("<foo>& &\"bar<b>baz</b>"));
 }

 @Test
 public void testFilterStringWithLeadingTrailingSpaces() {
 assertEquals("&lt;p&gt; foo &gt;&lt;/p&gt;", HTMLFilter.filter(" <p> foo >"));
 }
}
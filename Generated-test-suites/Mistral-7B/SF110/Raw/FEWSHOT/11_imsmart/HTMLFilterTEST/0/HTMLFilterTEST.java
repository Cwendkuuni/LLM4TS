package com.imsmart.servlet;

import static org.junit. Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HTMLFilterTest {

 @Test
 public void testFilter_NullInput() {
 assertNull(HTMLFilter.filter(null));
 }

 @Test
 public void testFilter_EmptyInput() {
 assertEquals("", HTMLFilter.filter(""));
 }

 @Test
 public void testFilter_NoSpecialChars() {
 assertEquals("abcdef", HTMLFilter.filter("abcdef"));
 }

 @Test
 public void testFilter_SingleLessThanSign() {
 assertEquals("&lt;", HTMLFilter.filter("<"));
 }

 @Test
 public void testFilter_SingleGreaterThanSign() {
 assertEquals("&gt;", HTMLFilter.filter(">"));
 }

 @Test
 public void testFilter_SingleAmpersand() {
 assertEquals("&amp;", HTMLFilter.filter("&"));
 }

 @Test
 public void testFilter_SingleDoubleQuote() {
 assertEquals("&quot;", HTMLFilter.filter("\""));
 }

 @Test
 public void testFilter_MultipleSpecialChars() {
 assertEquals("&lt;abc&amp;def&gt;", HTMLFilter.filter("<abc&def>"));
 }

 @Test
 public void testFilter_MultipleSpecialCharsInsideString() {
 assertEquals("&lt;p&gt;Hello &amp; World!&lt;/p&gt;", HTMLFilter.filter("<p>Hello & World!</p>"));
 }

 @Test
 public void testFilter_MultipleSpecialCharsAtBeginningAndEnd() {
 assertEquals("&gt;&lt;p&gt;Hello &amp; World!&lt;/p&gt;&gt;", HTMLFilter.filter(">&<p>Hello & World!</p>&>"));
 }
}
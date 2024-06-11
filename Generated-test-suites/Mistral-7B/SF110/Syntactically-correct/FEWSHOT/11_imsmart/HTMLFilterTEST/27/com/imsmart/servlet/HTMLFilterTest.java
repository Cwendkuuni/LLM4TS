package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {

 @Test
 public void testFilterNullMessage() {
 assertEquals(null, HTMLFilter.filter(null));
 }

 @Test
 public void testFilterEmptyMessage() {
 assertEquals("", HTMLFilter.filter(""));
 }

 @Test
 public void testFilterMessageWithoutSpecialCharacters() {
 assertEquals("foo", HTMLFilter.filter("foo"));
 }

 @Test
 public void testFilterMessageWithLessThanSign() {
 assertEquals("&lt;", HTMLFilter.filter("<"));
 assertEquals("&lt;foo&gt;", HTMLFilter.filter("<foo>"));
 }

 @Test
 public void testFilterMessageWithGreaterThanSign() {
 assertEquals("&gt;", HTMLFilter.filter(">"));
 assertEquals("foo&gt;bar", HTMLFilter.filter("foo>bar"));
 }

 @Test
 public void testFilterMessageWithAmpersand() {
 assertEquals("&amp;", HTMLFilter.filter("&"));
 assertEquals("foo&amp;bar", HTMLFilter.filter("foo&bar"));
 }

 @Test
 public void testFilterMessageWithDoubleQuotes() {
 assertEquals("&quot;", HTMLFilter.filter("\""));
 assertEquals("foo&quot;bar", HTMLFilter.filter("foo\"bar"));
 }

 @Test
 public void testFilterMessageWithMixedSpecialCharacters() {
 assertEquals("&lt;foo&amp;bar&gt;", HTMLFilter.filter("<foo&bar>"));
 }
}
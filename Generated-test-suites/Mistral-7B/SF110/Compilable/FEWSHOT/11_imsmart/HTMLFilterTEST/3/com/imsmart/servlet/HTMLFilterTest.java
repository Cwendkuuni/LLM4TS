package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {

 @Test
 public void testFilter_NullInput() {
 assertEquals(null, HTMLFilter.filter(null));
 }

 @Test
 public void testFilter_EmptyInput() {
 assertEquals("", HTMLFilter.filter(""));
 }

 @Test
 public void testFilter_NoSpecialCharacters() {
 assertEquals("abcdef", HTMLFilter.filter("abcdef"));
 }

 @Test
 public void testFilter_LessThanSign() {
 assertEquals("&lt;", HTMLFilter.filter("<"));
 }

 @Test
 public void testFilter_GreaterThanSign() {
 assertEquals("&gt;", HTMLFilter.filter(">"));
 }

 @Test
 public void testFilter_Ampersand() {
 assertEquals("&amp;", HTMLFilter.filter("&"));
 }

 @Test
 public void testFilter_DoubleLessThanSign() {
 assertEquals("&lt;&lt;", HTMLFilter.filter("<<"));
 }

 @Test
 public void testFilter_DoubleGreaterThanSign() {
 assertEquals("&gt;&gt;", HTMLFilter.filter(">>"));
 }

 @Test
 public void testFilter_QuotationMark() {
 assertEquals("&quot;", HTMLFilter.filter("\""));
 }

 @Test
 public void testFilter_MultipleSpecialCharacters() {
 assertEquals("&lt;abc&amp;def&gt;", HTMLFilter.filter("<abc&def>"));
 }

 @Test
 public void testFilter_SpecialCharacterInMiddle() {
 assertEquals("ab&amp;cd", HTMLFilter.filter("ab&cd"));
 }

 @Test
 public void testFilter_SpecialCharacterAtBeginning() {
 assertEquals("&lt;abcdef", HTMLFilter.filter("<abcdef"));
 }

 @Test
 public void testFilter_SpecialCharacterAtEnd() {
 assertEquals("abcdef&gt;", HTMLFilter.filter("abcdef>"));
 }
}
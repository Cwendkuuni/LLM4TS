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
 public void testFilterSingleLetter() {
 assertEquals("A", HTMLFilter.filter("A"));
 }

 @Test
 public void testFilterSingleQuote() {
 assertEquals("&quot;", HTMLFilter.filter("\""));
 }

 @Test
 public void testFilterDoubleQuote() {
 assertEquals("&quot;", HTMLFilter.filter("\"\""));
 }

 @Test
 public void testFilterAmpersand() {
 assertEquals("&amp;", HTMLFilter.filter("&"));
 }

 @Test
 public void testFilterLessThan() {
 assertEquals("&lt;", HTMLFilter.filter("<"));
 }

 @Test
 public void testFilterGreaterThan() {
 assertEquals("&gt;", HTMLFilter.filter(">"));
 }

 @Test
 public void testFilterLessThanAndGreaterThan() {
 assertEquals("&lt; &gt;", HTMLFilter.filter("< >"));
 }

 @Test
 public void testFilterLessThanLessThan() {
 assertEquals("&lt; &lt;", HTMLFilter.filter("<<"));
 }

 @Test
 public void testFilterGreaterThanGreaterThan() {
 assertEquals("&gt; &gt;", HTMLFilter.filter(">>"));
 }

 @Test
 public void testFilterSingleCharacterEntity() {
 assertEquals("&nbsp;", HTMLFilter.filter("&nbsp;"));
 }

 @Test
 public void testFilterMultipleCharacterEntity() {
 assertEquals("&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;", HTMLFilter.filter("---------------"));
 }

 @Test
 public void testFilterSpecialCharacter() {
 assertEquals("&euro;", HTMLFilter.filter("\u20AC"));
 }

 @Test
 public void testFilterMultipleSpecialCharacters() {
 assertEquals("&lt; &euro; &gt;", HTMLFilter.filter("< \u20AC >"));
 }
}
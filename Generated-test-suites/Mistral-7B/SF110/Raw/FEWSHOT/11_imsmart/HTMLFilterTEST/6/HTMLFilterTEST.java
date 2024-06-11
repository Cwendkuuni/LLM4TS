package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

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
 public void testFilterSingleCharacter() {
 char[] message = new char[]{'A'};
 assertEquals("A", HTMLFilter.filter(new String(message)));
 }

 @Test
 public void testFilterSingleHTMLTagLessThan() {
 assertEquals("&lt;", HTMLFilter.filter("<"));
 }

 @Test
 public void testFilterSingleHTMLTagGreaterThan() {
 assertEquals("&gt;", HTMLFilter.filter(">"));
 }

 @Test
 public void testFilterSingleHTMLTagAmpersand() {
 assertEquals("&amp;", HTMLFilter.filter("&"));
 }

 @Test
 public void testFilterSingleHTMLTagQuotationMark() {
 assertEquals("&quot;", HTMLFilter.filter("\""));
 }

 @Test
 public void testFilterMultipleHTMLTagsLessThan() {
 assertEquals("&lt;&lt;", HTMLFilter.filter("<<"));
 }

 @Test
 public void testFilterMultipleHTMLTagsGreaterThan() {
 assertEquals("&gt;&gt;", HTMLFilter.filter(">>"));
 }

 @Test
 public void testFilterMultipleHTMLTagsAmpersand() {
 assertEquals("&amp;amp;", HTMLFilter.filter("&amp;"));
 }

 @Test
 public void testFilterMultipleHTMLTagsQuotationMark() {
 assertEquals("&quot;&quot;", HTMLFilter.filter("\"\""));
 }

 @Test
 public void testFilterMultipleHTMLTagsLessThanAndGreaterThan() {
 assertEquals("&lt;&gt;", HTMLFilter.filter("<>"));
 }

 @Test
 public void testFilterMultipleHTMLTagsLessThanQuotationMark() {
 assertEquals("&lt;&quot;", HTMLFilter.filter("<\""));
 }

 @Test
 public void testFilterMultipleHTMLTagsQuotationMarkGreaterThan() {
 assertEquals("&quot;&gt;", HTMLFilter.filter("\">"));
 }

 @Test
 public void testFilterMultipleHTMLTagsLessThanQuotationMarkAndAmpersand() {
 assertEquals("&lt;&quot; &amp; ", HTMLFilter.filter("<\" &"));
 }

 @Test
 public void testFilterMultipleHTMLTagsAmpersandLessThanGreaterThan() {
 assertEquals("&amp; &lt;&gt;", HTMLFilter.filter("& &<<"));
 }

 @Test
 public void testFilterMultipleHTMLTagsQuotationMarkLessThanGreaterThan() {
 assertEquals("&quot; &lt;&gt;", HTMLFilter.filter("\" < >"));
 }

 @Test
 public void testFilterMultipleHTMLTagsAndText() {
 char[] message = new char[]{'<', 'h', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!', ' ', 'T', 'h', 'i', 's', ' ', 'I', 's', ' ', 'G', 'o', 'o', 'd', ' ', '!', '>'};
 assertEquals("&lt;h&gt;ello&nbsp;World!&nbsp;This&nbsp;Is&nbsp;Good&nbsp;!&gt;", new String(HTMLFilter.filter(new String(message))));
 }
}
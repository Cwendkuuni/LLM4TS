package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

 @Test
 public void testFilterNullMessage() {
 String expected = null;
 String actual = HTMLFilter.filter(null);
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterEmptyMessage() {
 String expected = "";
 String actual = HTMLFilter.filter("");
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterMessageWithNoSpecialCharacters() {
 String message = "Hello, World!";
 String expected = "Hello, World!";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterMessageWithLessThanSign() {
 String message = "<html>";
 String expected = "&lt;html&gt;";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterMessageWithGreaterThanSign() {
 String message = ">abcdef";
 String expected = "&gt;abcdef";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterMessageWithAmpersand() {
 String message = "&example";
 String expected = "&amp;example";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterMessageWithDoubleQuotes() {
 String message = "\"quote\"";
 String expected = "&quot;&quot;";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterMessageWithMixedSpecialCharacters() {
 String message = "<abc&def>&quot;>";
 String expected = "&lt;abc&amp;def&gt;&quot;&gt;";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

}
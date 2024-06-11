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
 public void testFilterOnlySpace() {
 assertEquals("", HTMLFilter.filter("   "));
 }

 @Test
 public void testFilterSimpleHTMLTags() {
 assertEquals("&lt;p&gt;Hello&lt;world&lt;/p&gt;", HTMLFilter.filter("<p>Hello<world></p>"));
 assertEquals("&lt;a href=\"https://www.google.com\"&gt;Google&lt;/a&gt;", HTMLFilter.filter("<a href=\"https://www.google.com\">Google</a>"));
 }

 @Test
 public void testFilterComplexHTMLTags() {
 String input = "<html>\n<head>\n<title>Test Page</title>\n</head>\n<body>\n<p>\n<strong>Test</strong>\n<em>Text</em>\n</p>\n</body>\n</html>";
 String expectedOutput = "&lt;html&gt;&#xA0;&#xA0;--&gt;\n&lt;head&gt;\n&lt;title&gt;Test Page&lt;/title&gt;\n&lt;/head&gt;\n&lt;body&gt;\n&lt;p&gt;\n&lt;strong&gt;Test&lt;/strong&gt;\n&lt;em&gt;Text&lt;/em&gt;\n&lt;/p&gt;\n&lt;/body&gt;\n&lt;/html&gt;";
 assertEquals(expectedOutput, HTMLFilter.filter(input));
 }

 @Test
 public void testFilterHTMLTagsWithAttributes() {
 String input = "<a href=\"https://www.imsmart.com\" target=\"_blank\">Visit imsmart.com</a>";
 String expectedOutput = "&lt;a href=\"https://www.imsmart.com\" target=\"_blank\"&gt;Visit imsmart.com&lt;/a&gt;";
 assertEquals(expectedOutput, HTMLFilter.filter(input));
 }

 @Test
 public void testFilterHTMLTagsWithScripts() {
 String input = "<script src=\"script.js\"></script>";
 String expectedOutput = "&lt;script src=\"script.js\"&gt;&#xA0;&#xA0;--&gt;";
 assertEquals(expectedOutput, HTMLFilter.filter(input));
 }

 @Test
 public void testFilterHTMLTagsWithStyles() {
 String input = "<style type=\"text/css\">body { background-color: #f8f9fa; }</style>";
 String expectedOutput = "&lt;style type=\"text/css\"&gt;body { background-color: #f8f9fa; }&lt;/style&gt;";
 assertEquals(expectedOutput, HTMLFilter.filter(input));
 }
}
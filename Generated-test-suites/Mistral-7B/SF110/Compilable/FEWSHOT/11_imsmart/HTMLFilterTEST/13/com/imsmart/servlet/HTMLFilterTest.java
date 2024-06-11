package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {

 @Test
 public void testFilterNullMessage() {
 assertNull(HTMLFilter.filter(null));
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
 public void testFilterMessageWithSingleSpecialCharacter() {
 assertEquals("&lt;foo&gt;", HTMLFilter.filter("<foo>"));
 assertEquals("&gt;foo&lt;", HTMLFilter.filter(">foo<"));
 assertEquals("&amp;foo&quot;", HTMLFilter.filter("&foo\""));
 }

 @Test
 public void testFilterMessageWithMultipleSpecialCharacters() {
 assertEquals("&lt;foo&amp;bar&gt;", HTMLFilter.filter("<foo&bar>"));
 assertEquals("&gt;foo&quot;<bar&gt;", HTMLFilter.filter(">foo&quot;<bar>"));
 assertEquals("&amp;foo&quot;<bar&amp;baz>&gt;", HTMLFilter.filter("&foo&quot;<bar&baz>"));
 }

 @Test
 public void testFilterMessageWithComplexSpecialCharacters() {
 assertEquals("&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt; &lt;html&gt; &lt;head&gt; &lt;title&gt;Test&lt;/title&gt; &lt;body&gt; &lt;p&gt; &lt;a href=&quot;http://example.com&quot;&gt;Link&lt;/a&gt; &lt;/p&gt; &lt;/body&gt; &lt;/html&gt;",
 HTMLFilter.filter("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n <html>\n <head>\n <title>Test</title>\n <body>\n <p>\n <a href=\"http://example.com\">Link</a>\n </p>\n </body>\n </html>"));
 }
}
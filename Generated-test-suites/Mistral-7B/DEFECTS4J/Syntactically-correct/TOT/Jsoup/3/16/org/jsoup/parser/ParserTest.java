package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

import java.io.IOException;

/**
 * Test cases for the Parser class.
 */
public class ParserTest {

 private Document doc;

 @Before
 public void setUp() {
 doc = Jsoup.parse("<html><head></head><body></body></html>", "http://example.com");
 }

 @After
 public void tearDown() {
 doc = null;
 }

 /**
 * Test parsing an empty HTML document.
 */
 @Test
 public void testParseEmptyHtml() {
 assertEquals("", doc.body().text());
 }

 /**
 * Test parsing a simple HTML document with text and tags.
 */
 @Test
 public void testParseSimpleHtml() {
 doc = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>", "http://example.com");
 assertEquals("Hello, World!", doc.body().select("h1").text());
 assertEquals("Test", doc.title().text());
 }

 /**
 * Test parsing an HTML document with an embedded script tag.
 */
 @Test
 public void testParseScriptTag() {
 doc = Jsoup.parse("<html><head><title>Test</title></head><body><script type=\"text/javascript\">alert(\"Hello\");</script></body></html>", "http://example.com");
 assertFalse(doc.body().children().isEmpty());
 }

 /**
 * Test parsing an HTML document with an embedded style tag.
 */
 @Test
 public void testParseStyleTag() {
 doc = Jsoup.parse("<html><head><title>Test</title><style>body { background-color: red; }</style></head><body></body></html>", "http://example.com");
 assertEquals(StringUtils.repeat(" ", 20), doc.body().text()); // Ensure body text is empty due to background color
 }

 /**
 * Test parsing an HTML document with an embedded comment.
 */
 @Test
 public void testParseComment() {
 doc = Jsoup.parse("<html><!-- This is a comment --><body></body></html>", "http://example.com");
 assertTrue(doc.body().children().isEmpty());
 }

 /**
 * Test parsing an HTML document with an embedded CDATA section.
 */
 @Test
 public void testParseCdata() {
 doc = Jsoup.parse("<html><body><![CDATA[ <h1>Hello, World!</h1> ]]></body></html>", "http://example.com");
 assertEquals("Hello, World!", doc.body().select("h1").text());
 }

 /**
 * Test parsing an HTML document with a base tag.
 */
 @Test
 public void testParseBaseTag() throws IOException {
 doc = Jsoup.parse("<html><head><base href=\"/foo\"><title>Test</title></head><body><a href=\"bar/baz.html\"></a></body></html>", "http://example.com");
 assertEquals("http://example.com/foo/bar/baz.html", doc.select("a[href]").attr("abs:href"));
 }

 /**
 * Test parsing an invalid HTML document.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidHtml() {
 doc = Jsoup.parse("<html><head><title>Test</title></head><body><h1<script>alert(\"Hello\");</script></body></html>", "http://example.com");
 }
}
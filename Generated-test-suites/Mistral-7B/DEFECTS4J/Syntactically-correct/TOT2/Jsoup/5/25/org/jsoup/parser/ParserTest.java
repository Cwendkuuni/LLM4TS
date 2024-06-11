package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test class for Parser.java
 */
public class ParserTest {

 @Test
 public void testParse() {
 Parser parser = new Parser("<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>", "http://test.com");
 Document doc = parser.parse();

 Element html = doc.elementSibling(0);
 assertEquals("html", html.tagName());

 Element head = doc.elementSibling(1);
 assertEquals("head", head.tagName());
 Element title = head.child(0);
 assertEquals("title", title.tagName());
 assertEquals("Test Title", title.text());

 Element body = doc.elementSibling(2);
 assertEquals("body", body.tagName());
 Element h1 = body.child(0);
 assertEquals("h1", h1.tagName());
 assertEquals("Test Heading", h1.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullHtml() {
 Parser parser = new Parser(null, "http://test.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 Parser parser = new Parser("<html></html>", null);
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<body><h1>Test Heading</h1></body>", "http://test.com");
 Document doc = parser.parse();

 Element body = doc.elementSibling(0);
 assertEquals("body", body.tagName());
 Element h1 = body.child(0);
 assertEquals("h1", h1.tagName());
 assertEquals("Test Heading", h1.text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Parser parser = new Parser("<body><h1>Test Heading</h1>", "http://test.com");
 parser.relaxed = true;
 Document doc = parser.parse();

 Element body = doc.elementSibling(0);
 assertEquals("body", body.tagName());
 Element h1 = body.child(0);
 assertEquals("h1", h1.tagName());
 assertEquals("Test Heading", h1.text());
 }

 @Test
 public void testParseEmptyBodyFragment() {
 Parser parser = new Parser("<body></body>", "http://test.com");
 Document doc = parser.parse();

 Element body = doc.elementSibling(0);
 assertEquals("body", body.tagName());
 assertFalse(body.childNodes().hasSize());
 }

 @Test
 public void testParseEmptyDocument() {
 Parser parser = new Parser("", "http://test.com");
 Document doc = parser.parse();

 Element html = doc.elementSibling(0);
 assertEquals("html", html.tagName());
 assertTrue(html.childNodes().hasSize());
 assertTrue(html.childNodes().get(0).tagName().equals("head"));
 assertTrue(html.childNodes().get(1).tagName().equals("body"));
 assertFalse(html.childNodes().get(1).childNodes().hasSize());
 }

 @Test
 public void testParseXmlDecl() {
 Parser parser = new Parser("<!DOCTYPE html SYSTEM \"about:legacy-compat\">", "http://test.com");
 Document doc = parser.parse();
 Element xmlDecl = doc.child(0);
 assertEquals("xml-declaration", xmlDecl.tagName());
 }

 @Test
 public void testParseComment() {
 Parser parser = new Parser("<!DOCTYPE html> <!-- This is a comment -->", "http://test.com");
 Document doc = parser.parse();
 Element comment = doc.child(1);
 assertEquals("comment", comment.tagName());
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseEndTag() {
 Parser parser = new Parser("<div><p>Test<p></div>", "http://test.com");
 Document doc = parser.parse();
 Element div = doc.elementSibling(0);
 assertEquals("div", div.tagName());
 assertEquals(2, div.childNodes().size());
 Element p1 = div.child(0);
 assertEquals("p", p1.tagName());
 assertEquals("Test", p1.text());
 Element p2 = div.child(1);
 assertEquals("p", p2.tagName());
 assertTrue(p2.childNodes().isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithEmptyEndTag() {
 Parser parser = new Parser("<div><p>Test</p></p>", "http://test.com");
 }

 @Test
 public void testParseWithNestedEndTags() {
 Parser parser = new Parser("<div><p>Test<p><p>Nested</p></p></p></div>", "http://test.com");
 Document doc = parser.parse();
 Element div = doc.elementSibling(0);
 assertEquals("div", div.tagName());
 assertEquals(2, div.childNodes().size());
 Element p1 = div.child(0);
 assertEquals("p", p1.tagName());
 assertEquals("Test", p1.text());
 Element p2 = div.child(1);
 assertEquals("p", p2.tagName());
 assertEquals(1, p2.childNodes().size());
 Element nested = p2.child(0);
 assertEquals("p", nested.tagName());
 assertEquals("Nested", nested.text());
 }

 @Test
 public void testParseDataTag() {
 Parser parser = new Parser("<script type='text/javascript' src='script.js'></script>", "http://test.com");
 Document doc = parser.parse();
 Element script = doc.elementSibling(0);
 assertEquals("script", script.tagName());
 assertEquals("text/javascript", script.attr("type"));
 assertTrue(script.childNodes().isEmpty());
 }

 @Test
 public void testParseTextNode() {
 Parser parser = new Parser("<div>Hello <there></div>", "http://test.com");
 Document doc = parser.parse();
 Element div = doc.elementSibling(0);
 assertEquals("div", div.tagName());
 assertEquals(1, div.childNodes().size());
 Element text = div.child(0);
 assertEquals("text", text.tagName());
 assertEquals("Hello <there>", text.text());
 }

 @Test
 public void testParseCdata() {
 Parser parser = new Parser("<![CDATA[Hello <there>]]>", "http://test.com");
 Document doc = parser.parse();
 TextNode text = (TextNode) doc.childNodes().get(0);
 assertEquals("CDATA[Hello <there>]", text.text());
 }

 @Test
 public void testParseCommentAndCdata() {
 Parser parser = new Parser("<!DOCTYPE html> <!-- This is a comment --><![CDATA[Hello <there>]]>", "http://test.com");
 Document doc = parser.parse();
 Element comment = doc.child(1);
 assertEquals("comment", comment.tagName());
 assertEquals("This is a comment", comment.text());
 TextNode cdata = (TextNode) doc.child(2);
 assertEquals("CDATA[Hello <there>]", cdata.text());
 }
}
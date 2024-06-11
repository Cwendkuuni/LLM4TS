package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setUp() {
 String html = "<html><head><title>Test Page</title></head>" +
         "<body><h1>Test Heading</h1>" +
         "<p><a href=\"http://example.com\">Link</a></p>" +
         "</body></html>";
 parser = new Parser(html, "http://example.com", false);
 doc = parser.parse();
 }

 @Test
 public void testParseDocument() {
 Validate.notNull(parser);
 Validate.notNull(doc);

 assertEquals("Test Page", doc.title());
 assertEquals(5, doc.childNodeSize());

 Element body = doc.body();
 assertEquals(1, body.childNodeSize());

 Element h1 = body.child(0);
 assertEquals("Test Heading", h1.text());

 Element link = body.getElementsByTag("a").first();
 assertEquals("Link", link.text());
 assertEquals("http://example.com", link.attr("href"));
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<p><a href=\"http://example.com\">Link</a></p>";
 Document bodyDoc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

 Validate.notNull(bodyDoc);
 assertEquals(1, bodyDoc.childNodeSize());

 Element bodyElement = bodyDoc.body();
 assertEquals(1, bodyElement.childNodeSize());

 Element linkElement = bodyElement.getElementsByTag("a").first();
 assertEquals("Link", linkElement.text());
 assertEquals("http://example.com", linkElement.attr("href"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullHtml() {
 parser = new Parser(null, "http://example.com", false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 parser = new Parser("<html></html>", null, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyBodyFragment() {
 parser = new Parser("", "http://example.com", true);
 }

 @Test
 public void testParseComment() {
 String html = "<html><!-- This is a comment --></html>";
 parser = new Parser(html, "http://example.com", false);
 doc = parser.parse();

 Element commentElement = doc.body().child(0);
 assertTrue(commentElement instanceof Comment);
 assertEquals("This is a comment", commentElement.text());
 }

 @Test
 public void testParseXmlDecl() {
 String html = "<html xmlns:my=\"http://example.com/\">";
 parser = new Parser(html, "http://example.com", false);
 doc = parser.parse();

 assertEquals("http://example.com/", doc.xmlNamespaceURI("my"));
 }

 @Test
 public void testParseEndTag() {
 String html = "<html><body><p><div><p></div></p></body></html>";
 parser = new Parser(html, "http://example.com", false);
 doc = parser.parse();

 assertEquals("div", doc.body().child(1).tagName());
 assertEquals(3, doc.body().child(1).childNodeSize());
 }

 @Test
 public void testParseStartTag() {
 String html = "<html><body><p><a href=\"http://example.com\">Link</a></p></body></html>";
 parser = new Parser(html, "http://example.com", false);
 doc = parser.parse();

 assertEquals(2, doc.body().childNodeSize());
 assertEquals("Link", doc.body().child(1).child(0).text());
 assertEquals("http://example.com", doc.body().child(1).child(0).attr("href"));
 }

 @Test
 public void testParseAttribute() {
 String html = "<p class=\"test class2\"></p>";
 parser = new Parser(html, "http://example.com", false);
 doc = parser.parse();

 Element p = doc.body().child(0);
 List<Attribute> attributes = p.attributes();

 assertEquals(2, attributes.size());
 assertEquals("class", attributes.get(0).key());
 assertEquals("test class2", attributes.get(0).value());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnknownTagName() {
 String html = "<html><body><p><x>Test</x></p></body></html>";
 parser = new Parser(html, "http://example.com", false);
 }

 @Test
 public void testParseTextNode() {
 String html = "<html><body><p>Test</p></body></html>";
 parser = new Parser(html, "http://example.com", false);
 doc = parser.parse();

 assertEquals("Test", doc.body().child(0).child(0).text());
 }

 @Test
 public void testParseCdata() {
 String html = "<html><body><p><![CDATA[Test]]></p></body></html>";
 parser = new Parser(html, "http://example.com", false);
 doc = parser.parse();

 assertEquals("Test", doc.body().child(0).child(0).text());
 }

 @Test
 public void testParseBaseTag() {
 String html = "<html><head><base href=\"/test/\">";
 parser = new Parser(html, "http://example.com", false);
 doc = parser.parse();

 assertEquals("/test/", doc.baseUri());
 assertEquals("/test/", doc.head().child(0).attr("href"));
 }

 @Test
 public void testEmptyElement() {
 String html = "<html><body><img src=\"\"/></body></html>";
 parser = new Parser(html, "http://example.com", false);
 doc = parser.parse();

 assertEquals("img", doc.body().child(0).tagName());
 assertTrue(doc.body().child(0).childNodeSize() == 0);
 }
}
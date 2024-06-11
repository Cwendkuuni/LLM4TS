package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setup() {
 String html = "<html><head><title>Test Page</title></head>" +
         "<body><h1>Test Heading</h1>" +
         "<p>Test Paragraph</p>" +
         "</body></html>";

 parser = new Parser(html, "http://test.com", false);
 doc = parser.parse();
 }

 @Test
 public void testParse() {
 // Check parsed document structure
 assertEquals(htmlTag, doc.tagName());
 assertNotNull(doc.head());
 assertNotNull(doc.title());
 assertNotNull(doc.body());
 assertNotNull(doc.head().children());
 assertNotNull(doc.head().children().get(0));
 assertEquals("Test Page", doc.title().text());

 // Check body contents
 Element body = doc.body();
 assertEquals(1, body.childElementCount());
 assertTrue(body.child(0) instanceof Element);
 assertEquals("h1", body.child(0).tagName());
 assertEquals("Test Heading", body.child(0).text());

 // Check head contents
 assertEquals(1, doc.head().childElementCount());
 assertTrue(doc.head().child(0) instanceof Element);
 assertEquals("title", doc.head().child(0).tagName());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<p>Test Paragraph</p>";

 parser = new Parser(bodyHtml, "http://test.com", true);
 doc = parser.parse();

 // Check parsed document structure
 assertEquals(htmlTag, doc.tagName());
 assertNotNull(doc.body());
 assertNull(doc.head());

 // Check body contents
 assertEquals(1, doc.body().childElementCount());
 assertTrue(doc.body().child(0) instanceof Element);
 assertEquals("p", doc.body().child(0).tagName());
 assertEquals("Test Paragraph", doc.body().child(0).text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullHtml() {
 parser = new Parser(null, "http://test.com", false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() {
 parser = new Parser("<html></html>", null, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithEmptyBaseUri() {
 parser = new Parser("<html></html>", "", false);
 }

 @Test
 public void testParseWithBaseUri() {
 String html = "<img src=\"image.jpg\" alt=\"Test Image\">";

 parser = new Parser(html, "http://test.com", false);
 doc = parser.parse();
 String imageUrl = doc.select("img").absUrl("src");
 assertEquals("http://test.com/image.jpg", imageUrl);
 }

 @Test
 public void testParseWithEmptyDocument() {
 String html = "";
 parser = new Parser(html, "http://test.com", false);
 doc = parser.parse();
 assertNull(doc.body());
 }

 @Test
 public void testParseWithComment() {
 String html = "<!-- This is a comment -->";
 parser = new Parser(html, "http://test.com", false);
 doc = parser.parse();
 Element comment = doc.select("comment").first();
 assertNotNull(comment);
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseXmlDecl() {
 String xmlDecl = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
 String html = "<html>" + xmlDecl + "</html>";
 parser = new Parser(html, "http://test.com", false);
 doc = parser.parse();
 XmlDeclaration xmlDeclaration = (XmlDeclaration) doc.select("xml-declaration").first();
 assertNotNull(xmlDeclaration);
 assertNotNull(xmlDeclaration.getVersion());
 assertEquals("1.0", xmlDeclaration.getVersion());
 assertNotNull(xmlDeclaration.getEncoding());
 assertEquals("UTF-8", xmlDeclaration.getEncoding());
 assertFalse(xmlDeclaration.isStandalone());
 }

 @Test
 public void testParseEndTag() {
 String html = "<p><b>Test</b></p>";
 parser = new Parser(html, "http://test.com", false);
 doc = parser.parse();
 Element b = doc.select("b").first();
 assertEquals(1, b.childElementCount());
 assertTrue(b.child(0) instanceof TextNode);
 assertEquals("Test", b.child(0).text());
 }

 @Test
 public void testParseStartTag() {
 String html = "<h1>Test Heading</h1>";
 parser = new Parser(html, "http://test.com", false);
 doc = parser.parse();
 Element h1 = doc.select("h1").first();
 assertEquals("Test Heading", h1.text());
 }

 @Test
 public void testParseAttribute() {
 String html = "<a href=\"http://test.com/page1.html\" title=\"Test Title\">Link</a>";
 parser = new Parser(html, "http://test.com", false);
 doc = parser.parse();
 Element link = doc.select("a").first();
 List<Attribute> attributes = link.attributes();
 assertEquals(2, attributes.size());
 assertTrue(attributes.get(0).key().equals("href"));
 assertEquals("http://test.com/page1.html", attributes.get(0).value());
 assertTrue(attributes.get(1).key().equals("title"));
 assertEquals("Test Title", attributes.get(1).value());
 }

 @Test
 public void testParseTextNode() {
 String html = "<p>Test Paragraph</p>";
 parser = new Parser(html, "http://test.com", false);
 doc = parser.parse();
 Element p = doc.select("p").first();
 assertTrue(p.childElementCount() > 0);
 assertTrue(p.child(0) instanceof TextNode);
 assertEquals("Test Paragraph", p.child(0).text());
 }

 @Test
 public void testParseCdata() {
 String html = "<script><![CDATA[alert('Hello World');]]></script>";
 parser = new Parser(html, "http://test.com", false);
 doc = parser.parse();
 Element script = doc.select("script").first();
 assertTrue(script.childElementCount() > 0);
 assertTrue(script.child(0) instanceof TextNode);
 assertEquals("alert('Hello World');", script.child(0).text());
 }

 @Test
 public void testParseTagWithInvalidEndTag() {
 String html = "<p><b>Test</b></p><p>";
 parser = new Parser(html, "http://test.com", false);
 doc = parser.parse();
 assertEquals(2, doc.body().childElementCount());
 Element secondP = doc.body().child(1);
 assertTrue(secondP.tagName().equals("p"));
 assertTrue(secondP.childElementCount() == 0);
 }

 @Test
 public void testParseTagWithSelfClosingEndTag() {
 String html = "<p><img src=\"image.jpg\" alt=\"Test Image\" /></p>";
 parser = new Parser(html, "http://test.com", false);
 doc = parser.parse();
 Element p = doc.select("p").first();
 assertEquals(1, p.childElementCount());
 assertTrue(p.child(0) instanceof Element);
 assertEquals("img", p.child(0).tagName());
 }

 @Test
 public void testParseImplicitParentForTextarea() {
 String html = "<html><head></head><body>" +
         "<textarea name=\"test\" rows=\"2\" cols=\"20\">Test Text</textarea>" +
         "</body></html>";
 parser = new Parser(html, "http://test.com", false);
 doc = parser.parse();
 Element textarea = doc.select("textarea").first();
 Element implicitParent = textarea.parent();
 assertEquals("body", implicitParent.tagName());
 }

}
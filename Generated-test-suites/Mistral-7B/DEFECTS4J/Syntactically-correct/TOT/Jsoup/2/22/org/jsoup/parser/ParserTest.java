package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setUp() {
 String html = "<html><head><title>Test Title</title></head>" +
 "<body>" +
 "<p>This is a <strong>test</strong>.</p>" +
 "</body>" +
 "</html>";
 doc = Jsoup.parse(html);
 parser = new Parser(html, "http://example.com", false);
 }

 @Test
 public void testParse() {
 Document parsedDoc = parser.parse();
 assertEquals(doc, parsedDoc);
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<p>This is a <strong>test</strong></p>";
 Document parsedDoc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 assertEquals(doc.body(), parsedDoc.body());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullHtml() {
 Parser parser = new Parser(null, "http://example.com", false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 Parser parser = new Parser("", null, false);
 }

 @Test
 public void testParseComment() {
 parser = new Parser("<!-- This is a comment -->", "http://example.com", false);
 Document parsedDoc = parser.parse();
 Element comment = parsedDoc.body().child(0);
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseXmlDecl() {
 parser = new Parser("<xml version=\"1.0\" encoding=\"UTF-8\"?>", "http://example.com", false);
 Document parsedDoc = parser.parse();
 XmlDeclaration xmlDecl = parsedDoc.head().child(0);
 assertEquals("UTF-8", xmlDecl.encoding());
 }

 @Test
 public void testParseEndTag() {
 parser = new Parser("<p><p>", "http://example.com", false);
 Document parsedDoc = parser.parse();
 assertEquals(0, parsedDoc.body().child(0).childSize());
 }

 @Test
 public void testParseStartTag() {
 parser = new Parser("<p><strong>Test</strong></p>", "http://example.com", false);
 Document parsedDoc = parser.parse();
 Element p = parsedDoc.body().child(0);
 Element strong = p.child(0);
 assertEquals("Test", strong.text());
 assertEquals("p", p.tagName());
 assertEquals("strong", strong.tagName());
 }

 @Test
 public void testParseAttribute() {
 parser = new Parser("<a href=\"http://example.com/test\" class=\"testClass\">Link</a>", "http://example.com", false);
 Document parsedDoc = parser.parse();
 Element link = parsedDoc.body().child(0).child(0);
 assertEquals("http://example.com/test", link.absUrl("href"));
 assertEquals("testClass", link.className());
 }

 @Test
 public void testParseTextNode() {
 parser = new Parser("<p>This is a <strong>test</strong>.</p>", "http://example.com", false);
 Document parsedDoc = parser.parse();
 Element p = parsedDoc.body().child(0);
 assertEquals("This is a test.", p.text());
 }

 @Test
 public void testParseCdata() {
 parser = new Parser("<script><![CDATA[function test() { alert('Hello World'); }]]></script>", "http://example.com", false);
 Document parsedDoc = parser.parse();
 Element script = parsedDoc.body().child(0);
 assertEquals("function test() { alert('Hello World'); }", script.text());
 }

 @Test
 public void testParseBaseTag() {
 parser = new Parser("<base href=\"/new-base/\">", "http://example.com", false);
 Document parsedDoc = parser.parse();
 assertEquals("/new-base/", parsedDoc.baseUri());
 }
}
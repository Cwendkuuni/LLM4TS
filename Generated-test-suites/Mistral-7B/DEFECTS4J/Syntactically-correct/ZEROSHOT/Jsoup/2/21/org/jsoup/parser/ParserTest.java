package org.jsoup.parser;

import org.apache.commons.lang3.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParse() {
 Parser parser = new Parser("<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>", "http://test.com");
 Document doc = parser.parse();

 Element html = doc.elementSibling(0);
 Element head = html.child(0);
 Element title = head.child(0);
 Element body = html.child(1);
 Element h1 = body.child(0);

 assertEquals("Test Title", title.text());
 assertEquals("Test Heading", h1.text());
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<body><h1>Body Heading</h1></body>", "http://test.com");
 Document doc = parser.parse();

 Element html = doc; // by default doc has html tag
 Element body = html.child(0);
 Element h1 = body.child(0);

 assertEquals("Body Heading", h1.text());
 }

 @Test
 public void testParseComment() {
 Parser parser = new Parser("<!-- This is a comment -->", "http://test.com");
 Document doc = parser.parse();

 Element html = doc;
 Comment comment = html.child(0).child(0);

 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseXmlDecl() {
 Parser parser = new Parser("<xml version=\"1.0\" encoding=\"UTF-8\"></xml>", "http://test.com");
 Document doc = parser.parse();

 XmlDeclaration xmlDeclaration = doc.child(0);
 assertEquals("UTF-8", xmlDeclaration.encoding());
 }

 @Test
 public void testParseEndTag() {
 Parser parser = new Parser("<html><body><p>Test text</p></body></html>", "http://test.com");
 Document doc = parser.parse();

 Element html = doc;
 Element body = html.child(1);
 Element p = body.child(0);
 Element emptyP = body.child(1);

 assertEquals(1, body.childCount());
 assertNull(emptyP);
 }

 @Test
 public void testParseStartTag() {
 Parser parser = new Parser("<html><head><title>Test Title</title><link rel=\"stylesheet\" href=\"styles.css\"/></head><body><h1>Test Heading</h1></body></html>", "http://test.com");
 Document doc = parser.parse();

 Element html = doc;
 Element head = html.child(0);
 Element title = head.child(0);
 Element link = head.child(1);
 Element body = html.child(1);
 Element h1 = body.child(0);

 assertEquals("Test Title", title.text());
 assertEquals("styles.css", link.attr("href"));
 assertEquals("Test Heading", h1.text());
 }

 @Test
 public void testParseAttribute() {
 Parser parser = new Parser("<img src=\"image.jpg\" alt=\"Test Image\" title=\"Test Title\" class=\"test-class\"/>", "http://test.com");
 Document doc = parser.parse();

 Element img = doc.selectFirst("img");

 assertEquals("image.jpg", img.attr("src"));
 assertEquals("Test Image", img.attr("alt"));
 assertEquals("Test Title", img.attr("title"));
 assertEquals("test-class", img.attr("class"));
 }

 @Test
 public void testParseTextNode() {
 Parser parser = new Parser("<p>Test text</p>", "http://test.com");
 Document doc = parser.parse();

 Element p = doc.selectFirst("p");

 assertEquals("Test text", p.text());
 }

 @Test
 public void testParseCdata() {
 Parser parser = new Parser("<script><![CDATA[alert(\"Hello World\");]]></script>", "http://test.com");
 Document doc = parser.parse();

 Element script = doc.selectFirst("script");

 assertEquals("alert(\"Hello World\");", script.text());
 }

 @Test
 public void testParseInvalidHtml() {
 Parser parser = new Parser("</p>", "http://test.com");
 try {
 Document doc = parser.parse();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }
}
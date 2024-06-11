package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

/**
 * Test class for Parser.java
 */
public class ParserTest {

 @Test
 public void testParser() {
 Parser parser = new Parser("", "", false); // Test empty constructor
 assertNull(parser.doc);
 assertNotNull(parser.stack);
 assertNotNull(parser.tq);

 parser = new Parser("<html><head></head><body></body></html>", "", false); // Test valid constructor
 Document doc = parser.parse();
 assertEquals(htmlTag, doc.tagName());
 assertTrue(doc.body().tagName().equals("body"));

 parser = new Parser("<html><head></head><body><div></div></body></html>", "", false); // Test valid constructor with child element
 Document docWithChild = parser.parse();
 assertEquals(htmlTag, docWithChild.tagName());
 assertTrue(docWithChild.body().tagName().equals("body"));
 assertTrue(docWithChild.body().child(0).tagName().equals("div"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParserNullHtml() {
 Parser parser = new Parser(null, "", false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParserNullBaseUri() {
 Parser parser = new Parser("", null, false);
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<body><div>Hello World</div></body>", "", true);
 Document doc = parser.parse();
 assertTrue(doc.head() == null);
 assertTrue(doc.tagName().equals("document-fragment"));
 assertTrue(doc.body().tagName().equals("body"));
 assertTrue(doc.body().child(0).tagName().equals("div"));
 assertTrue(doc.body().child(0).child(0).nodeValue().equals("Hello World"));
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Parser parser = new Parser("<body><div>Hello World<div>Hello Again</div></div></body>", "", true);
 Document doc = parser.parse();
 assertTrue(doc.head() == null);
 assertTrue(doc.tagName().equals("document-fragment"));
 assertTrue(doc.body().tagName().equals("body"));
 assertTrue(doc.body().child(0).tagName().equals("div"));
 assertTrue(doc.body().child(0).child(0).nodeValue().equals("Hello World"));
 assertTrue(doc.body().child(0).child(1).tagName().equals("div"));
 assertTrue(doc.body().child(0).child(1).child(0).nodeValue().equals("Hello Again"));
 }

 @Test
 public void testParseXmlDecl() {
 Parser parser = new Parser("<xml version='1.0' encoding='UTF-8' standalone='yes'></xml>", "", false);
 Document doc = parser.parse();
 XmlDeclaration xmlDeclaration = doc.xml();
 assertEquals("1.0", xmlDeclaration.version());
 assertEquals("UTF-8", xmlDeclaration.encoding());
 assertTrue(xmlDeclaration.standalone());
 }

 @Test
 public void testParseComment() {
 Parser parser = new Parser("<html><!-- This is a comment --></html>", "", false);
 Document doc = parser.parse();
 Comment comment = doc.body().child(0).child(0);
 assertEquals("This is a comment", comment.data());
 }

 @Test
 public void testParseEndTag() {
 Parser parser = new Parser("<html><body><div><p></p></div></body></html>", "", false);
 Document doc = parser.parse();
 assertEquals("body", doc.tagName());
 Element div = doc.body().child(0);
 assertEquals("div", div.tagName());
 Element p = div.child(0);
 assertEquals("p", p.tagName());
 assertEquals(0, p.childNodes().size());
 }

 @Test
 public void testParseStartTag() {
 Parser parser = new Parser("<html><body><div><p><a href='test.html'>Link</a></p></div></body></html>", "", false);
 Document doc = parser.parse();
 assertEquals("html", doc.tagName());
 Element body = doc.body();
 Element div = body.child(0);
 Element p = div.child(0);
 Element a = p.child(0);
 assertEquals("div", div.tagName());
 assertEquals("p", p.tagName());
 assertEquals("a", a.tagName());
 assertEquals("Link", a.attr("href"));
 }

 @Test
 public void testParseAttribute() {
 Parser parser = new Parser("<input type='text' name='textfield' value='test' class='classname' id='textfield'>", "", false);
 Document doc = parser.parse();
 Element input = doc.body().child(0);
 Attribute typeAttribute = input.attr("type");
 Attribute nameAttribute = input.attr("name");
 Attribute valueAttribute = input.attr("value");
 Attribute classAttribute = input.attr("class");
 Attribute idAttribute = input.attr("id");
 assertEquals("text", typeAttribute.getValue());
 assertEquals("textfield", nameAttribute.getValue());
 assertEquals("test", valueAttribute.getValue());
 assertEquals("classname", classAttribute.getValue());
 assertEquals("textfield", idAttribute.getValue());
 }

 @Test
 public void testParseTextNode() {
 Parser parser = new Parser("<html><body><p>Test</p></body></html>", "", false);
 Document doc = parser.parse();
 TextNode textNode = (TextNode) doc.body().child(0).child(0);
 assertEquals("Test", textNode.nodeValue());
 }

 @Test
 public void testParseCdata() {
 Parser parser = new Parser("<html><body><![CDATA[Test]]></body></html>", "", false);
 Document doc = parser.parse();
 TextNode textNode = (TextNode) doc.body().child(0).child(0);
 assertEquals("Test", textNode.nodeValue());
 }
}
package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedList;

/**
 * Test class for Parser.java
 */
public class ParserTest {

 @Test
 public void testParseHtml() {
 Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "http://example.com");
 Document doc = parser.parse();
 assertEquals("Test", doc.title().text());
 assertEquals("Hello World", doc.body().text());
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<body><h1>Hello World</h1></body>", "http://example.com");
 Document doc = parser.parse();
 assertEquals("Hello World", doc.body().text());
 }

 @Test
 public void testParseComment() {
 Parser parser = new Parser("<html><!-- This is a comment --></html>", "http://example.com");
 Document doc = parser.parse();
 assertTrue(doc.body().hasText());
 assertTrue(doc.body().child(0) instanceof Comment);
 assertEquals("This is a comment", ((Comment) doc.body().child(0)).text());
 }

 @Test
 public void testParseCdata() {
 Parser parser = new Parser("<html><![CDATA[This is some CDATA data]]><body></body></html>", "http://example.com");
 Document doc = parser.parse();
 assertTrue(doc.body().hasText());
 assertTrue(doc.body().child(0) instanceof TextNode);
 assertEquals("This is some CDATA data", doc.body().child(0).text());
 }

 @Test
 public void testParseXmlDecl() {
 Parser parser = new Parser("<html xmlns=\"http://example.com/\"><head><title>Test</title></head><body></body></html>", "http://example.com");
 Document doc = parser.parse();
 assertEquals("http://example.com/", doc.baseUri());
 }

 @Test
 public void testParseEndTag() {
 Parser parser = new Parser("<html><body><p>Hello World<p></p></body></html>", "http://example.com");
 Document doc = parser.parse();
 assertEquals(1, doc.body().child(0).childCount());
 assertEquals("Hello World", doc.body().child(0).child(0).text());
 }

 @Test
 public void testParseStartTag() {
 Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "http://example.com");
 Document doc = parser.parse();
 assertEquals("Test", doc.title().text());
 Element h1 = doc.body().child(0);
 assertEquals(1, h1.childCount());
 assertEquals("Hello World", h1.child(0).text());
 }

 @Test
 public void testParseAttribute() {
 Parser parser = new Parser("<html><body><img src=\"image.png\" alt=\"Image\"></body></html>", "http://example.com");
 Document doc = parser.parse();
 Element img = doc.body().child(0);
 assertEquals("image.png", img.absUrl("src"));
 assertEquals("Image", img.attr("alt"));
 }

 @Test
 public void testParseTextNode() {
 Parser parser = new Parser("<html><body><p>This is a test text node</p></body></html>", "http://example.com");
 Document doc = parser.parse();
 Element p = doc.body().child(0);
 assertEquals(1, p.childCount());
 assertEquals("This is a test text node", p.child(0).text());
 }
}
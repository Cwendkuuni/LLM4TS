package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.junit.Test;

/**
 * Test class for Parser.java
 *
 * @author [Your Name]
 */
public class ParserTest {

 @Test
 public void testParse() {
 Parser parser = new Parser("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", "http://example.com");
 Document doc = parser.parse();

 Element html = doc.elementSiblings().get(0);
 Element head = html.child(0);
 Element title = head.child(0);
 Element body = html.child(1);
 Element p = body.child(0);

 assertEquals("html", html.tagName());
 assertEquals("head", head.tagName());
 assertEquals("title", title.tagName());
 assertEquals("body", body.tagName());
 assertEquals("p", p.tagName());
 assertEquals("Hello World", p.text());
 assertEquals("http://example.com", doc.baseUri());
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<p>Hello World</p>", "http://example.com");
 Document doc = parser.parseBodyFragment("", "http://example.com");

 Element body = doc.body();
 Element p = body.child(0);

 assertEquals("p", p.tagName());
 assertEquals("Hello World", p.text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Parser parser = new Parser("<p>Hello World</p>", "http://example.com");
 Document doc = parser.parseBodyFragmentRelaxed("", "http://example.com");

 Element body = doc.body();
 Element p = body.child(0);

 assertEquals("p", p.tagName());
 assertEquals("Hello World", p.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullHtml() {
 Parser parser = new Parser(null, "http://example.com", false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 Parser parser = new Parser("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", null, false);
 }

 @Test
 public void testParseWithBaseTag() {
 Parser parser = new Parser("<html><base href=\"/test/\"><head><title>Test</title></head><body><p>Hello World</p></body></html>", "http://example.com");
 Document doc = parser.parse();

 assertEquals("/test/", doc.baseUri());
 }

 @Test
 public void testParseWithEmptyBodyFragment() {
 Parser parser = new Parser("", "http://example.com");
 Document doc = parser.parseBodyFragment("", "http://example.com");

 assertEquals("", doc.body().text());
 }
}
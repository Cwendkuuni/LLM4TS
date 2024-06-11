package org.jsoup.parser;

import org.apache.commons.lang3.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Test for Parser class
 */
public class ParserTest {

 @Test
 public void testParse() {
 Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "http://example.com");
 Document doc = parser.parse();

 assertEquals("Test", doc.title());
 assertEquals("Hello World", doc.body().text());
 assertEquals("http://example.com", doc.baseUri());
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<h1>Hello World</h1>", "http://example.com");
 Document doc = parser.parse();

 assertEquals("http://example.com", doc.baseUri());
 assertNotNull(doc.head());
 assertEquals(0, doc.head().childElementCount());
 assertEquals("Hello World", doc.body().text());
 }

 @Test
 public void testParseInvalidHtml() {
 Parser parser = new Parser("<html><body</body>", "http://example.com");
 try {
 parser.parse();
 fail("Expected ParserException");
 } catch (ParserException e) {
 // expected
 }

 assertEquals("http://example.com", parser.getBaseUri());
 assertNull(parser.getDocument());
 }

 @Test
 public void testStack() {
 Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "http://example.com");
 Document doc = parser.parse();

 Element lastElement = parser.last();
 Element html = lastElement.previousElementSibling();

 assertEquals("html", html.tagName());
 assertEquals("head", lastElement.tagName());
 assertEquals("title", lastElement.child(0).tagName());
 assertEquals("h1", lastElement.nextSibling().tagName());
 }

 @Test
 public void testParseAttributes() {
 Parser parser = new Parser("<div id='test' class='class1 class2' title='Test Title'></div>", "http://example.com");
 Document doc = parser.parse();
 Element div = doc.body().firstChild();

 assertEquals("test", div.attr("id"));
 List<String> classes = div.classes();
 assertEquals(2, classes.size());
 assertTrue(classes.contains("class1"));
 assertTrue(classes.contains("class2"));
 assertEquals("Test Title", div.attr("title"));
 }

 @Test
 public void testParseTextNode() {
 Parser parser = new Parser("<p>Test text</p>", "http://example.com");
 Document doc = parser.parse();
 Element p = doc.body().firstChild();

 assertEquals("Test text", p.text());
 }

 @Test
 public void testParseTextArea() {
 Parser parser = new Parser("<textarea>Test text</textarea>", "http://example.com");
 Document doc = parser.parse();
 Element textarea = doc.body().firstChild();

 assertEquals("Test text", textarea.text());
 }

 @Test
 public void testParseComment() {
 Parser parser = new Parser("<!-- Test comment -->", "http://example.com");
 Document doc = parser.parse();

 assertEquals(1, doc.childElementCount());
 Element comment = doc.child(0);
 assertEquals("Test comment", comment.text());
 assertEquals("comment", comment.tagName());
 }

 @Test
 public void testParseXmlDeclaration() {
 Parser parser = new Parser("<!DOCTYPE html SYSTEM \"about:legacy-compat\">", "http://example.com");
 Document doc = parser.parse();

 assertEquals(1, doc.childElementCount());
 assertTrue(doc.child(0) instanceof XmlDeclaration);
 }

 @Test
 public void testParseEndTag() {
 Parser parser = new Parser("<div><p>Test text</p></div>", "http://example.com");
 Document doc = parser.parse();
 Element div = doc.body().firstChild();

 assertEquals(2, div.childElementCount());
 Element p = div.child(0);
 assertEquals("p", p.tagName());
 assertTrue(p.nextSibling() instanceof TextNode);
 }

 @Test
 public void testParseStartTag() {
 Parser parser = new Parser("<div><p>Test text</p><p>Test text 2</p></div>", "http://example.com");
 Document doc = parser.parse();
 Element div = doc.body().firstChild();

 assertEquals(3, div.childElementCount());
 Element p1 = div.child(0);
 Element p2 = div.child(1);

 assertEquals("p", p1.tagName());
 assertEquals("p", p2.tagName());
 }

 @Test
 public void testParseEmptyTag() {
 Parser parser = new Parser("<img src='image.png'/>", "http://example.com");
 Document doc = parser.parse();
 Element img = doc.body().firstChild();

 assertEquals("img", img.tagName());
 assertEquals(1, img.childElementCount());
 assertTrue(img.child(0) instanceof TextNode);
 assertEquals("image.png", img.attr("src"));
 }

 @Test
 public void testParseScriptTag() {
 Parser parser = new Parser("<script type='text/javascript'>console.log(\"Hello World\");</script>", "http://example.com");
 Document doc = parser.parse();
 Element script = doc.body().firstChild();

 assertEquals("script", script.tagName());
 assertEquals(1, script.childElementCount());
 assertTrue(script.child(0) instanceof TextNode);
 assertEquals("console.log(\"Hello World\");", script.text());
 }

 @Test
 public void testParseBaseTag() {
 Parser parser = new Parser("<base href='/newbase'><html><head></head><body></body></html>", "http://example.com");
 Document doc = parser.parse();

 assertEquals("/newbase", doc.baseUri());
 assertEquals("http://example.com/newbase", doc.absoluteUrl("/"));
 }

 @Test
 public void testParseBodyTag() {
 Parser parser = new Parser("<body><h1>Test</h1></body>", "http://example.com");
 Document doc = parser.parse();

 assertEquals("body", doc.tagName());
 assertEquals("Test", doc.body().text());
 }

 @Test
 public void testParseHtmlTag() {
 Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Test</h1></body></html>", "http://example.com");
 Document doc = parser.parse();

 assertEquals("html", doc.tagName());
 assertEquals("Test", doc.title().text());
 assertEquals("Test", doc.body().text());
 }

 @Test
 public void testParseBodyFragmentWithBaseTag() {
 Parser parser = new Parser("<body><base href='/newbase'><h1>Test</h1></body>", "http://example.com");
 Document doc = parser.parse();

 assertEquals("body", doc.tagName());
 assertEquals("/newbase", doc.baseUri());
 assertEquals("Test", doc.body().text());
 }
}
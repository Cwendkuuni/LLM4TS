package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

public class ParserTest {

 @Test
 public void testParseEmptyHtml() {
 Parser parser = new Parser("<html></html>", "http://example.com");
 Document doc = parser.parse();
 Element html = doc.getElementById("html");
 Assert.assertEquals(htmlTag, html.tag());
 Assert.assertEquals(1, html.childElementCount());
 }

 @Test
 public void testParseWithTitle() {
 Parser parser = new Parser("<html><head><title>Test Page</title></head><body></body></html>", "http://example.com");
 Document doc = parser.parse();
 Element title = doc.getTitle();
 Assert.assertEquals("Test Page", title.text());
 }

 @Test
 public void testParseWithTextNode() {
 Parser parser = new Parser("<html><body><p>Hello World</p></body></html>", "http://example.com");
 Document doc = parser.parse();
 Element p = doc.getElementsByTag("p").first();
 TextNode textNode = (TextNode) p.child(0);
 Assert.assertEquals("Hello World", textNode.text());
 }

 @Test
 public void testParseWithAttributes() {
 Parser parser = new Parser("<html><body><div id='myDiv' class='myClass' title='Hello'><p>Hello World</p></div></body></html>", "http://example.com");
 Document doc = parser.parse();
 Element div = doc.getElementById("myDiv");
 Assert.assertEquals("myDiv", div.id());
 Attribute classAttr = div.classAttr();
 Assert.assertEquals("myClass", classAttr.getValue());
 Attribute titleAttr = div.attr("title");
 Assert.assertEquals("Hello", titleAttr.getValue());
 }

 @Test
 public void testParseWithErrors() {
 Parser parser = new Parser("<html><body><script>alert('Hello')</script></body></html>", "http://example.com");
 try {
 parser.parse();
 Assert.fail();
 } catch (RuntimeException e) {
 // Expected
 }
 }

 @Test
 public void testParseWithImplicitParents() {
 Parser parser = new Parser("<html><body><div class='myClass'><span>Hello</span></div></body></html>", "http://example.com");
 Document doc = parser.parse();
 Element div = doc.getElementsByClass("myClass").first();
 Element span = div.child(0);
 Assert.assertEquals("span", span.tag().name());
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<p>Hello World</p>", "http://example.com");
 Document doc = Parser.parseBodyFragment(parser.html(), "http://example.com");
 Element p = doc.getElementsByTag("p").first();
 TextNode textNode = (TextNode) p.child(0);
 Assert.assertEquals("Hello World", textNode.text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Parser parser = new Parser("<p>Hello World</p>", "http://example.com");
 Document doc = Parser.parseBodyFragmentRelaxed(parser.html(), "http://example.com");
 Element p = doc.getElementsByTag("p").first();
 TextNode textNode = (TextNode) p.child(0);
 Assert.assertEquals("Hello World", textNode.text());
 }
}
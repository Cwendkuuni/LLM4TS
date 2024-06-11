package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.Attributes;
import org.jsoup.nodes.attributes.AttributeSet;
import org.jsoup.parser.Parser;

public class ParserTest {

 @Test
 public void testParseEmptyHTML() {
 String html = "<html></html>";
 Document doc = Parser.parse(html, "http://example.com");
 assertNotNull(doc);
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 }

 @Test
 public void testParseHTMLWithText() {
 String html = "<html><body><p>Hello World</p></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 Element body = doc.body();
 TextNode textNode = (TextNode) body.getElementsByTag("p").get(0).child(0);
 assertEquals("Hello World", textNode.text());
 }

 @Test
 public void testParseSimpleTag() {
 String html = "<html><body><h1>Test</h1></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 Element h1 = doc.body().getElementsByTag("h1").get(0);
 assertEquals("Test", h1.text());
 }

 @Test
 public void testParseTagWithAttributes() {
 String html = "<html><body><a href=\"http://example.com/test\" target=\"_blank\">Link</a></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 Element link = doc.body().getElementsByTag("a").get(0);
 assertEquals("http://example.com/test", link.attr("href"));
 assertEquals("_blank", link.attr("target"));
 }

 @Test
 public void testParseTagWithNestedTags() {
 String html = "<html><body><ul><li>Item 1<ul><li>Subitem 1</li><li>Subitem 2</li></ul></li></ul></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 Element ul = doc.body().getElementsByTag("ul").get(0);
 Element li1 = ul.getElementsByTag("li").get(0);
 Element subUl = li1.getElementsByTag("ul").get(0);
 List<Element> subLis = subUl.getAllElements();
 assertEquals(2, subLis.size());
 assertEquals("Subitem 1", subLis.get(0).text());
 assertEquals("Subitem 2", subLis.get(1).text());
 }

 @Test
 public void testParseSelfClosingTag() {
 String html = "<html><body><img src=\"image.jpg\"/></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 Element img = doc.body().getElementsByTag("img").get(0);
 assertNotNull(img);
 assertTrue(img.tag().isSelfClosing());
 }

 @Test
 public void testParseDataTags() {
 String html = "<html><body><script type=\"text/javascript\">alert('Hello World');</script></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 TextNode scriptText = (TextNode) doc.body().getElementsByTag("script").get(0).child(0);
 assertEquals("alert('Hello World');", scriptText.text());
 }

 @Test
 public void testParseEmptyBodyFragment() {
 String html = "<script type=\"text/javascript\">//<![CDATA[document.write('');//]]></script>";
 Document doc = Parser.parseBodyFragment(html, "http://example.com");
 assertNotNull(doc);
 assertNotNull(doc.head());
 assertNull(doc.body());
 }

 @Test
 public void testParseBodyFragmentWithText() {
 String html = "<script type=\"text/javascript\">//<![CDATA[document.write('<p>Hello World</p>');//]]></script>";
 Document doc = Parser.parseBodyFragment(html, "http://example.com");
 Element body = doc.body();
 TextNode textNode = (TextNode) body.getElementsByTag("p").get(0).child(0);
 assertEquals("Hello World", textNode.text());
 }

 @Test
 public void testParseBodyFragmentWithTag() {
 String html = "<script type=\"text/javascript\">//<![CDATA[document.write('<ul><li>Item 1</li></ul>');//]]></script>";
 Document doc = Parser.parseBodyFragment(html, "http://example.com");
 Element ul = doc.body().getElementsByTag("ul").get(0);
 List<Element> liList = ul.getAllElements();
 assertEquals(1, liList.size());
 assertEquals("Item 1", liList.get(0).text());
 }

 @Test (expected = IllegalArgumentException.class)
 public void testParseInvalidInput() {
 String html = "";
 Document doc = null;
 try {
 doc = Parser.parse(html, "http://example.com");
 } catch (IllegalArgumentException e) {
 // expected
 assertEquals("Error parsing input: Unexpected end of input", e.getMessage());
 }
 assertNull(doc);
 }
}
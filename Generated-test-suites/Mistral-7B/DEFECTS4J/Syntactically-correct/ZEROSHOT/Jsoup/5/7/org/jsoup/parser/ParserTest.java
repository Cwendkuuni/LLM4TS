package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;

import java.util.LinkedList;

import org.junit.Test;

public class ParserTest {

 @Test
 public void testParse() {
 Parser parser = new Parser("", "", false); // empty string should throw exception
 try {
 parser.parse();
 fail("Expected IllegalArgumentException for empty string input");
 } catch (IllegalArgumentException e) {
 // expected
 }

 String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parse(html, "");

 Element htmlNode = doc;
 assertEquals(htmlTag, htmlNode.tag());
 assertEquals(1, htmlNode.childNodes().size());

 Element headNode = htmlNode.childNodes().get(0);
 assertEquals(headTag, headNode.tag());
 assertEquals(1, headNode.childNodes().size());

 Element titleNode = headNode.childNodes().get(0);
 assertEquals(titleTag, titleNode.tag());
 assertEquals("Test Page", titleNode.text());
 assertEquals("", titleNode.baseUri());

 Element bodyNode = htmlNode.childNodes().get(1);
 assertEquals(bodyTag, bodyNode.tag());
 assertEquals(1, bodyNode.childNodes().size());

 Element h1Node = bodyNode.childNodes().get(0);
 assertEquals("h1", h1Node.tagName());
 assertEquals("Hello World", h1Node.text());
 assertEquals("", h1Node.baseUri());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><p>Hello World</p></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "");

 Element bodyNode = doc;
 assertEquals(bodyTag, bodyNode.tag());
 assertEquals(1, bodyNode.childNodes().size());

 Element pNode = bodyNode.childNodes().get(0);
 assertEquals("p", pNode.tagName());
 assertEquals("Hello World", pNode.text());
 assertEquals("", pNode.baseUri());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<body><p>Hello World<a href=\"test.html\">Link</a></p>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "");

 Element bodyNode = doc;
 assertEquals(bodyTag, bodyNode.tag());
 assertEquals(1, bodyNode.childNodes().size());

 Element pNode = bodyNode.childNodes().get(0);
 assertEquals("p", pNode.tagName());
 assertEquals("Hello World", pNode.text());
 assertEquals("", pNode.baseUri());

 Element aNode = pNode.childNodes().get(0);
 assertEquals("a", aNode.tagName());
 assertEquals("Link", aNode.text());
 assertEquals("test.html", aNode.absUrl("href"));
 }

 @Test
 public void testParseComment() {
 String html = "<html><!-- This is a comment --></html>";
 Document doc = Parser.parse(html, "");
 Element commentNode = doc.body().childNodes().get(0);
 assertEquals(Comment.class, commentNode.getClass());
 assertEquals("This is a comment", commentNode.text());
 }

 @Test
 public void testParseXmlDecl() {
 String html = "<html xmlns=\"http://www.w3.org/1999/xhtml\">";
 Document doc = Parser.parse(html, "");
 XmlDeclaration xmlDeclNode = doc.head().childNodes().get(0);
 assertEquals("<xml version=\"1.0\" xmlns=\"http://www.w3.org/1999/xhtml\">", xmlDeclNode.toString());
 }

 @Test
 public void testParseEndTag() {
 String html = "<html><body><p>Hello World</p></body></html>";
 Document doc = Parser.parse(html, "");
 Element pNode = doc.body().childNodes().get(0);
 assertEquals(1, pNode.childNodes().size());
 assertEquals(bodyTag, pNode.tag());
 assertEquals("", pNode.baseUri());
 }

 @Test
 public void testParseEmptyEndTag() {
 String html = "<html><body><p></p></body></html>";
 Document doc = Parser.parse(html, "");
 Element pNode = doc.body().childNodes().get(0);
 assertEquals(1, pNode.childNodes().size());
 assertEquals(bodyTag, pNode.tag());
 assertEquals("", pNode.baseUri());
 assertEquals(0, pNode.textNodes().size());
 }

 @Test
 public void testParseStartTag() {
 String html = "<html><body><p><strong>Hello World</strong></p></body></html>";
 Document doc = Parser.parse(html, "");
 Element pNode = doc.body().childNodes().get(0);
 assertEquals(2, pNode.childNodes().size());
 assertEquals(bodyTag, pNode.tag());
 assertEquals("", pNode.baseUri());

 Element strongNode = pNode.childNodes().get(0);
 assertEquals(1, strongNode.childNodes().size());
 assertEquals("strong", strongNode.tagName());
 assertEquals("Hello World", strongNode.text());
 assertEquals("", strongNode.baseUri());
 }

 @Test
 public void testParseStartTagWithAttributes() {
 String html = "<html><body><img src=\"test.jpg\" alt=\"Test Image\" title=\"Test Title\"/></body></html>";
 Document doc = Parser.parse(html, "");
 Element imgNode = doc.body().childNodes().get(0);
 assertEquals(3, imgNode.attributeCount());

 Attribute srcAttr = imgNode.attribute("src");
 assertEquals("test.jpg", srcAttr.getValue());

 Attribute altAttr = imgNode.attribute("alt");
 assertEquals("Test Image", altAttr.getValue());

 Attribute titleAttr = imgNode.attribute("title");
 assertEquals("Test Title", titleAttr.getValue());
 }

 @Test
 public void testParseStartTagWithEmptyAttributes() {
 String html = "<html><body><img src=\"test.jpg\" /></body></html>";
 Document doc = Parser.parse(html, "");
 Element imgNode = doc.body().childNodes().get(0);
 assertEquals(1, imgNode.attributeCount());
 Attribute srcAttr = imgNode.attribute("src");
 assertEquals("test.jpg", srcAttr.getValue());
 assertEquals(0, imgNode.childNodes().size());
 }

 @Test
 public void testParseTextNode() {
 String html = "<html><body><p>Hello World</p></body></html>";
 Document doc = Parser.parse(html, "");
 Element pNode = doc.body().childNodes().get(0);
 assertEquals(1, pNode.childNodes().size());
 assertEquals(bodyTag, pNode.tag());
 assertEquals("", pNode.baseUri());

 TextNode textNode = (TextNode) pNode.childNodes().get(0);
 assertEquals("Hello World", textNode.text());
 assertEquals("", textNode.baseUri());
 }

 @Test
 public void testParseCdata() {
 String html = "<html><body><![CDATA[This is some CDATA]]></body></html>";
 Document doc = Parser.parse(html, "");
 Element cdataNode = doc.body().childNodes().get(0);
 assertEquals(1, cdataNode.childNodes().size());
 assertEquals(TextNode.class, cdataNode.childNodes().get(0).getClass());
 assertEquals("This is some CDATA", ((TextNode) cdataNode.childNodes().get(0)).text());
 }

 @Test
 public void testParseUnknownTag() {
 String html = "<html><body><unknown></body></html>";
 Document doc = Parser.parse(html, "");
 Element unknownNode = doc.body().childNodes().get(0);
 assertEquals(1, unknownNode.childNodes().size());
 assertEquals(bodyTag, unknownNode.tag());
 assertEquals("", unknownNode.baseUri());

 TextNode textNode = (TextNode) unknownNode.childNodes().get(0);
 assertEquals("", textNode.text());
 assertEquals("", textNode.baseUri());
 }

 @Test
 public void testParseStartTagWithMixedAttributes() {
 String html = "<html><body><img src=\"test.jpg\" alt=\"Test Image\" title=\"Test Title\" class=\"testClass\"/></body></html>";
 Document doc = Parser.parse(html, "");
 Element imgNode = doc.body().childNodes().get(0);
 assertEquals(4, imgNode.attributeCount());

 Attribute srcAttr = imgNode.attribute("src");
 assertEquals("test.jpg", srcAttr.getValue());

 Attribute altAttr = imgNode.attribute("alt");
 assertEquals("Test Image", altAttr.getValue());

 Attribute titleAttr = imgNode.attribute("title");
 assertEquals("Test Title", titleAttr.getValue());

 Attribute classAttr = imgNode.attribute("class");
 assertEquals("testClass", classAttr.getValue());
 }

 @Test
 public void testParseStartTagWithMultipleAttributesWithSameName() {
 String html = "<html><body><img src=\"test.jpg\" alt=\"Test Image\" alt=\"Another Test Image\"/></body></html>";
 Document doc = Parser.parse(html, "");
 Element imgNode = doc.body().childNodes().get(0);
 assertEquals(3, imgNode.attributeCount());

 Attribute srcAttr = imgNode.attribute("src");
 assertEquals("test.jpg", srcAttr.getValue());

 Attribute altAttr1 = imgNode.attribute("alt");
 assertEquals("Test Image", altAttr1.getValue());

 Attribute altAttr2 = imgNode.attribute("alt");
 assertEquals("Another Test Image", altAttr2.getValue());
 }

 @Test
 public void testParseStartTagWithInvalidAttribute() {
 String html = "<html><body><p data-test=\"invalid-data-test\"></p></body></html>";
 Document doc = Parser.parse(html, "");
 Element pNode = doc.body().childNodes().get(0);
 assertEquals(1, pNode.attributeCount());
 assertNull(pNode.attribute("data-test"));
 }

 @Test
 public void testParseStartTagWithInvalidAttributeValue() {
 String html = "<html><body><img src=\"test.jpg\" alt=\"<script>alert('Hello World');</script>\"/></body></html>";
 Document doc = Parser.parse(html, "");
 Element imgNode = doc.body().childNodes().get(0);
 assertEquals(1, imgNode.attributeCount());
 assertEquals("src", imgNode.attribute("src").getName());
 assertEquals("test.jpg", imgNode.attribute("src").getValue());
 assertNull(imgNode.attribute("alt"));
 }

 @Test
 public void testParseStartTagWithInvalidAttributeValue2() {
 String html = "<html><body><img src=\"test.jpg\" alt=\"&lt;script&gt;alert('Hello World');&lt;/script&gt;\"/></body></html>";
 Document doc = Parser.parse(html, "");
 Element imgNode = doc.body().childNodes().get(0);
 assertEquals(1, imgNode.attributeCount());
 assertEquals("src", imgNode.attribute("src").getName());
 assertEquals("test.jpg", imgNode.attribute("src").getValue());
 assertNull(imgNode.attribute("alt"));
 }

 @Test
 public void testParseStartTagWithInvalidAttributeValue3() {
 String html = "<html><body><img src=\"test.jpg\" alt=\"\"><!--<script>alert('Hello World');</script>--></body></html>";
 Document doc = Parser.parse(html, "");
 Element imgNode = doc.body().childNodes().get(0);
 assertEquals(2, imgNode.attributeCount());
 assertEquals("src", imgNode.attribute("src").getName());
 assertEquals("test.jpg", imgNode.attribute("src").getValue());
 Attribute altAttr = imgNode.attribute("alt");
 assertEquals("", altAttr.getValue());
 assertEquals(0, altAttr.getValue().length());
 assertNull(imgNode.attribute("alt"));
 }

 @Test
 public void testParseStartTagWithInvalidAttributeValue4() {
 String html = "<html><body><img src=\"test.jpg\" alt=\"<script>alert('Hello World');</script>\"/></body></html>";
 Document doc = Parser.parse(html, "");
 Element imgNode = doc.body().childNodes().get(0);
 assertEquals(1, imgNode.attributeCount());
 assertEquals("src", imgNode.attribute("src").getName());
 assertEquals("test.jpg", imgNode.attribute("src").getValue());
 assertNull(imgNode.attribute("alt"));
 }

 @Test
 public void testParseStartTagWithInvalidAttributeName() {
 String html = "<html><body><img data-test=\"test-data\" x:test=\"test-test\"/></body></html>";
 Document doc = Parser.parse(html, "");
 Element imgNode = doc.body().childNodes().get(0);
 assertEquals(2, imgNode.attributeCount());
 assertEquals("src", imgNode.attribute("data-test").getName());
 assertEquals("test-data", imgNode.attribute("data-test").getValue());
 assertNull(imgNode.attribute("x:test"));
 }
}
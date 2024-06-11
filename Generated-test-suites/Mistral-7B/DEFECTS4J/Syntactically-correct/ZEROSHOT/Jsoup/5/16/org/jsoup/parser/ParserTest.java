package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BaseElement;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import org.junit.Test;

public class ParserTest {

 @Test
 public void testParse() throws IOException {
 String html = "<html><head><title>Test</title></head>" +
         "<body><h1>Hello World</h1>" +
         "<p>This is a test <base href=\"/test\">" +
         "<a href=\"foo.html\">link</a></p>" +
         "</body></html>";

 Document doc = Parser.parse(html, "http://www.example.com/test.html");
 assertNotNull(doc);

 Element htmlNode = doc.rootElement();
 assertEquals(htmlTag, htmlNode.tag());

 Element headNode = htmlNode.getFirstChild();
 assertEquals(headTag, headNode.tag());

 Element titleNode = headNode.getFirstChild();
 assertEquals(titleTag, titleNode.tag());
 assertEquals("Test", titleNode.text());

 Element bodyNode = htmlNode.getFirstChild();
 assertEquals(bodyTag, bodyNode.tag());

 Element linkNode = bodyNode.getChild(1);
 assertEquals(1, linkNode.childNodes().size());
 assertEquals("link", linkNode.tagName());
 assertEquals("/test/foo.html", linkNode.attr("href"));

 assertEquals(2, doc.elementCount());
 assertEquals(3, doc.childNodes().size());
 }

 @Test
 public void testParseBodyFragment() throws IOException {
 String html = "<body><h1>Hello World</h1></body>";

 Document doc = Parser.parseBodyFragment(html, "http://www.example.com/test.html");
 assertNotNull(doc);

 Element htmlNode = doc.rootElement();
 assertEquals(bodyTag, htmlNode.tag());

 Element h1Node = htmlNode.getFirstChild();
 assertEquals(h1Tag, h1Node.tag());
 assertEquals("Hello World", h1Node.text());

 assertEquals(1, doc.childNodes().size());
 }

 @Test
 public void testParseBodyFragmentRelaxed() throws IOException {
 String html = "<body><div><h1>Hello World</h1>";

 Document doc = Parser.parseBodyFragmentRelaxed(html, "http://www.example.com/test.html");
 assertNotNull(doc);

 Element htmlNode = doc.rootElement();
 assertEquals(bodyTag, htmlNode.tag());

 Element divNode = htmlNode.getFirstChild();
 assertEquals(divTag, divNode.tag());

 Element h1Node = divNode.getFirstChild();
 assertEquals(h1Tag, h1Node.tag());
 assertEquals("Hello World", h1Node.text());

 assertEquals(2, doc.childNodes().size());
 }

 @Test
 public void testParseInvalidHtml() throws IOException {
 String html = "<!--  <script>alert('Hello World');</script> -->";

 Document doc = Parser.parse(html, "http://www.example.com/test.html");
 assertNotNull(doc);

 assertEquals(0, doc.elementCount());
 assertEquals(1, doc.childNodes().size());

 Element commentNode = doc.child(0);
 assertEquals(Comment.class, commentNode.getClass());
 assertEquals("", commentNode.text());
 }

 @Test
 public void testParseCdata() throws IOException {
 String html = "<html><body><![CDATA[ <p>This is some <script>JavaScript</script> in a CDATA section.</p> ]]>";

 Document doc = Parser.parse(html, "http://www.example.com/test.html");
 assertNotNull(doc);

 Element pNode = doc.getElementsByTag("p").get(0);
 assertEquals(1, pNode.childNodes().size());
 assertEquals("This is some <script>JavaScript</script> in a CDATA section.", pNode.text());
 }

 @Test
 public void testParseXmlDecl() throws IOException {
 String html = "<html xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">";

 Document doc = Parser.parse(html, "http://www.example.com/test.html");
 assertNotNull(doc);

 XmlDeclaration xmlDecl = doc.getXmlDecl();
 assertNotNull(xmlDecl);
 assertEquals("xmlns:xhtml http://www.w3.org/1999/xhtml", xmlDecl.data());
 }

 @Test
 public void testParseComment() throws IOException {
 String html = "<html><!-- This is a comment --></html>";

 Document doc = Parser.parse(html, "http://www.example.com/test.html");
 assertNotNull(doc);

 Element commentNode = doc.getFirstChild();
 assertEquals(Comment.class, commentNode.getClass());
 assertEquals("This is a comment", commentNode.text());
 }

 @Test
 public void testParseAttribute() throws IOException {
 String html = "<p id=\"foo\" class=\"bar\" title=\"test\">This is a test</p>";

 Document doc = Parser.parse(html, "http://www.example.com/test.html");
 assertNotNull(doc);

 Element pNode = doc.getElementsByTag("p").get(0);
 List<Attribute> attributes = pNode.attributes();
 assertEquals(3, attributes.size());

 Attribute idAttr = attributes.get(0);
 assertEquals("id", idAttr.key());
 assertEquals("foo", idAttr.value());

 Attribute classAttr = attributes.get(1);
 assertEquals("class", classAttr.key());
 assertEquals("bar", classAttr.value());

 Attribute titleAttr = attributes.get(2);
 assertEquals("title", titleAttr.key());
 assertEquals("test", titleAttr.value());
 }

 @Test
 public void testParseTextNode() throws IOException {
 String html = "<p>This is a test</p>";

 Document doc = Parser.parse(html, "http://www.example.com/test.html");
 assertNotNull(doc);

 TextNode textNode = (TextNode) doc.getElementsByTag("p").get(0).childNodes().get(0);
 assertEquals("This is a test", textNode.text());
 }
}
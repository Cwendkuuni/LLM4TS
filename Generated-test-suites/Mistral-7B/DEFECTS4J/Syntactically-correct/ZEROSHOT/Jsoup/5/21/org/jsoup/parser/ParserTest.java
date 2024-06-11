package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;

import org.junit.Test;

public class ParserTest {

 @Test
 public void testParse() {
 Parser parser = new Parser("", "", false);
 Document doc = parser.parse();
 assertEquals("", doc.body().html());
 }

 @Test
 public void testParse_nullBaseUri() {
 assertThrows(NullPointerException.class, () -> Parser.parse(null, ""));
 }

 @Test
 public void testParse_emptyHtml() {
 Document doc = Parser.parse("", "");
 assertEquals("", doc.body().html());
 }

 @Test
 public void testParse_htmlWithBodyFragment() {
 Parser parser = new Parser("<html><body><p>Hello</p></body></html>", "", true);
 Document doc = parser.parse();
 assertEquals("<p>Hello</p>", doc.body().html());
 }

 @Test
 public void testParse_htmlWithBodyFragment_relaxed() {
 Parser parser = new Parser("<html><body><p>Hello</p></body></html>", "", true);
 parser.relaxed = true;
 Document doc = parser.parse();
 assertEquals("<p>Hello</p>", doc.body().html());
 }

 @Test
 public void testParse_fragmentWithScriptTag() {
 Parser parser = new Parser("<script type=\"text/javascript\">alert('Hello')</script>", "", true);
 Document doc = parser.parse();
 assertNotNull(doc.body());
 List<Node> scripts = doc.body().select("script");
 assertEquals(1, scripts.size());
 DataNode scriptNode = (DataNode) scripts.get(0);
 assertEquals("alert('Hello')", scriptNode.data());
 }

 @Test
 public void testParse_fragmentWithTitleTag() {
 Parser parser = new Parser("<title>Test</title>", "", true);
 Document doc = parser.parse();
 assertEquals("Test", doc.title().text());
 }

 @Test
 public void testParse_fragmentWithCommentTag() {
 Parser parser = new Parser("<!-- Hello -->", "", true);
 Document doc = parser.parse();
 assertEquals("", doc.body().html());
 Comment comment = doc.body().child(0);
 assertEquals("Hello", comment.text());
 }

 @Test
 public void testParse_fragmentWithCdataTag() {
 Parser parser = new Parser("<![CDATA[Hello]]>", "", true);
 Document doc = parser.parse();
 assertEquals("", doc.body().html());
 DataNode cdataNode = (DataNode) doc.body().child(0);
 assertEquals("Hello", cdataNode.data());
 }

 @Test
 public void testParse_fragmentWithAttributes() {
 Parser parser = new Parser("<div id=\"test\" class=\"test\" title=\"Test Title\"></div>", "", true);
 Document doc = parser.parse();
 Element div = doc.body().selectFirst("div");
 assertEquals("test", div.id());
 List<Attribute> attributes = div.attributes();
 assertEquals(3, attributes.size());
 Attribute idAttribute = attributes.get(0);
 assertEquals("id", idAttribute.key());
 assertEquals("test", idAttribute.value());
 Attribute classAttribute = attributes.get(1);
 assertEquals("class", classAttribute.key());
 assertEquals("test", classAttribute.value());
 Attribute titleAttribute = attributes.get(2);
 assertEquals("title", titleAttribute.key());
 assertEquals("Test Title", titleAttribute.value());
 }

 @Test
 public void testParse_fragmentWithNestedElements() {
 Parser parser = new Parser("<p><b>Hello</b></p>", "", true);
 Document doc = parser.parse();
 Element p = doc.body().selectFirst("p");
 Element b = p.child(0);
 assertEquals("Hello", b.text());
 assertEquals("p", p.tagName());
 assertEquals("b", b.tagName());
 }

 @Test
 public void testParse_fragmentWithImplicitParent() {
 Parser parser = new Parser("<img src=\"test.jpg\" />", "", true);
 Document doc = parser.parse();
 assertEquals("img", doc.body().child(0).tagName());
 Element img = doc.body().child(0);
 assertEquals("test.jpg", img.absUrl("src"));
 Element div = new Element("div");
 Element parent = doc.body().appendChild(div);
 parent.appendChild(img);
 assertEquals("div", img.parent().tagName());
 }

 @Test
 public void testParse_fragmentWithInvalidParent() {
 Parser parser = new Parser("<li><button></button></ul>", "", true); // ul cannot contain button
 Document doc = parser.parse();
 assertEquals(0, doc.body().select("button").size());
 }

 @Test
 public void testParse_fragmentWithInvalidParentRelaxed() {
 Parser parser = new Parser("<li><button></button></ul>", "", true); // ul cannot contain button, but relaxed mode allows it
 parser.relaxed = true;
 Document doc = parser.parse();
 assertEquals(1, doc.body().select("button").size());
 }

 @Test
 public void testParse_fragmentWithEmptyElement() {
 Parser parser = new Parser("<input type=\"text\" />", "", true);
 Document doc = parser.parse();
 Element input = doc.body().selectFirst("input");
 assertTrue(input.isEmpty());
 assertEquals("input", input.tagName());
 }

 @Test
 public void testParse_fragmentWithEmptyElementRelaxed() {
 Parser parser = new Parser("<input type=\"text\" />", "", true);
 parser.relaxed = true;
 Document doc = parser.parse();
 Element input = doc.body().selectFirst("input");
 assertTrue(input.isEmpty());
 assertEquals("input", input.tagName());
 }

 @Test
 public void testParse_fragmentWithEmptyElementSelfClosed() {
 Parser parser = new Parser("<input type=\"text\"/>", "", true);
 Document doc = parser.parse();
 Element input = doc.body().selectFirst("input");
 assertTrue(input.isEmpty());
 assertEquals("input", input.tagName());
 }

 @Test
 public void testParse_fragmentWithEmptyElementSelfClosedRelaxed() {
 Parser parser = new Parser("<input type=\"text\"/>", "", true);
 parser.relaxed = true;
 Document doc = parser.parse();
 Element input = doc.body().selectFirst("input");
 assertTrue(input.isEmpty());
 assertEquals("input", input.tagName());
 }
}
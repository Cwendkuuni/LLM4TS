package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.helper.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;

/**
 * Tests for {@link XmlTreeBuilder} class.
 */
public class XmlTreeBuilderTest {

 @Test
 public void testParseXmlFile() {
 String xmlString = "<root><child element=\"test\">1</child></root>";
 Document xmlDoc = Jsoup.parse(xmlString, Parser.xmlParser());

 assertEquals("root", xmlDoc.tagName());
 Element root = xmlDoc.rootElement();
 assertEquals("child", root.child(0).tagName());
 assertEquals("test", root.child(0).attr("element"));
 assertEquals("1", root.child(0).text());
 }

 @Test
 public void testParseXmlFragment() {
 String xmlFragment = "<child element=\"test\">1</child>";
 List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase).get(0).childNodes();

 assertEquals(2, nodes.size());
 Node child = nodes.get(0);
 assertEquals("child", child.tagName());
 assertEquals("test", child.attr("element"));
 assertEquals("1", child.text());
 }

 @Test(expected = IllegalStateException.class)
 public void testParseInvalidXml() {
 String xmlString = "<root><child<tag error=\"test\">1</child>";
 try {
 Document xmlDoc = Jsoup.parse(xmlString, Parser.xmlParser());
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testProcessStartTag() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Token startTag = new Token.StartTag("child", null, null, null, null);
 Element result = builder.insert(startTag);
 assertEquals("child", result.tagName());
 }

 @Test
 public void testProcessEndTag() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Token endTag = new Token.EndTag("child", null);
 builder.popStackToClose(endTag);
 assertTrue(builder.stack.isEmpty());
 }

 @Test
 public void testProcessComment() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Token comment = new Token.Comment("<!-- test comment -->");
 builder.insert(comment);
 List<Node> nodes = builder.doc.childNodes();
 assertEquals(1, nodes.size());
 Node node = nodes.get(0);
 assertTrue(node instanceof Comment);
 assertEquals("test comment", node.nodeName());
 }

 @Test
 public void testProcessCharacter() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Token character = new Token.Character('A');
 builder.insert(character);
 List<Node> nodes = builder.doc.childNodes();
 assertEquals(1, nodes.size());
 Node node = nodes.get(0);
 assertTrue(node instanceof TextNode);
 assertEquals("A", node.text());
 }

 @Test
 public void testProcessDocumentType() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Token doctype = new Token.Doctype("html", null, null);
 builder.insert(doctype);
 List<Node> nodes = builder.doc.childNodes();
 assertEquals(1, nodes.size());
 Node node = nodes.get(0);
 assertTrue(node instanceof DocumentType);
 assertEquals("html", node.tagName());
 }

 @Test
 public void testProcessEOF() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.runParser();
 assertTrue(builder.stack.isEmpty());
 }
}
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 @Test
 public void testDefaultSettings() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 assertEquals(ParseSettings.preserveCase, builder.defaultSettings());
 }

 @Test
 public void testInitialiseParse() {
 String xml = "<root><child><subchild/></child></root>";
 Document doc = xmlTreeBuilder.parse(new ByteArrayInputStream(xml.getBytes()), "");
 assertEquals("<root><child><subchild/></child></root>", doc.html());
 assertEquals("<root><child><subchild/></child></root>", doc.outerHtml());
 assertEquals("<root><child><subchild/></child></root>", doc.body().html());
 }

 @Test
 public void testProcessStartTag() {
 String xml = "<root><child><subchild/></child></root>";
 Document doc = xmlTreeBuilder.parse(new ByteArrayInputStream(xml.getBytes()), "");
 List<Node> rootChildren = doc.childNodes();
 assertEquals(1, rootChildren.size());
 Element root = (Element) rootChildren.get(0);
 List<Node> childChildren = root.childNodes();
 assertEquals(1, childChildren.size());
 Element child = (Element) childChildren.get(0);
 assertFalse(child.hasText());
 assertEquals("child", child.tagName());
 }

 @Test
 public void testProcessEndTag() {
 String xml = "<root><child></child></root>";
 Document doc = xmlTreeBuilder.parse(new ByteArrayInputStream(xml.getBytes()), "");
 List<Node> rootChildren = doc.childNodes();
 assertEquals(1, rootChildren.size());
 Element root = (Element) rootChildren.get(0);
 List<Node> childChildren = root.childNodes();
 assertEquals(0, childChildren.size());
 }

 @Test
 public void testProcessComment() {
 String xml = "<!-- This is a comment -->";
 Document doc = xmlTreeBuilder.parse(new ByteArrayInputStream(xml.getBytes()), "");
 List<Node> nodes = doc.childNodes();
 assertEquals(1, nodes.size());
 Node node = nodes.get(0);
 assertTrue(node instanceof Comment);
 assertEquals("This is a comment", node.text());
 }

 @Test
 public void testProcessCharacter() {
 String xml = "<root>abc</root>";
 Document doc = xmlTreeBuilder.parse(new ByteArrayInputStream(xml.getBytes()), "");
 List<Node> nodes = doc.childNodes();
 assertEquals(1, nodes.size());
 Element root = (Element) nodes.get(0);
 List<Node> children = root.childNodes();
 assertEquals(1, children.size());
 TextNode textNode = (TextNode) children.get(0);
 assertEquals("abc", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 String xml = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
 Document doc = xmlTreeBuilder.parse(new ByteArrayInputStream(xml.getBytes()), "");
 List<Node> nodes = doc.childNodes();
 assertEquals(1, nodes.size());
 DocumentType doctype = (DocumentType) nodes.get(0);
 assertEquals("html", doctype.tagName());
 assertNull(doctype.data());
 assertNull(doctype.entities());
 assertNull(doctype.notations());
 }

 @Test
 public void testProcessCharacter_CDATA() {
 String xml = "<![CDATA[ <root><child><subchild/></child></root> ]]>";
 Document doc = xmlTreeBuilder.parse(new ByteArrayInputStream(xml.getBytes()), "");
 List<Node> nodes = doc.childNodes();
 assertEquals(1, nodes.size());
 Element root = (Element) nodes.get(0);
 List<Node> children = root.childNodes();
 assertEquals(1, children.size());
 assertFalse(children.get(0) instanceof TextNode);
 assertTrue(children.get(0) instanceof CDataNode);
 }

 @Test
 public void testPopStackToClose() {
 String xml = "<root><child><child></child></child></root>";
 Document doc = xmlTreeBuilder.parse(new ByteArrayInputStream(xml.getBytes()), "");
 List<Node> rootChildren = doc.childNodes();
 assertEquals(1, rootChildren.size());
 Element root = (Element) rootChildren.get(0);
 List<Node> childChildren = root.childNodes();
 assertEquals(2, childChildren.size());
 Element child1 = (Element) childChildren.get(0);
 Element child2 = (Element) childChildren.get(1);
 xmlTreeBuilder.popStackToClose((EndTag) child1.tag());
 assertEquals(1, childChildren.size());
 assertEquals(child2, childChildren.get(0));
 }

 @Test
 public void testParseFragment() {
 String xml = "<root><child><subchild/></child></root>";
 List<Node> fragment = xmlTreeBuilder.parseFragment(xml, "", "");
 assertEquals(3, fragment.size());
 Element root = (Element) fragment.get(0);
 Element child = (Element) fragment.get(1);
 TextNode textNode = (TextNode) fragment.get(2);
 assertEquals("root", root.tagName());
 assertEquals("child", child.tagName());
 assertEquals("subchild", child.childNodes().get(0).tagName());
 assertEquals("", textNode.text());
 }

 @Test
 public void testParseFragment_Context() {
 String xml = "<root><child><subchild/></child></root>";
 Document context = Jsoup.parse("<root/>", "", xmlTreeBuilder);
 List<Node> fragment = xmlTreeBuilder.parseFragment(xml, context, "", xmlTreeBuilder);
 assertEquals(3, fragment.size());
 Element root = (Element) fragment.get(0);
 Element child = (Element) fragment.get(1);
 TextNode textNode = (TextNode) fragment.get(2);
 assertEquals("root", root.tagName());
 assertEquals("child", child.tagName());
 assertEquals("subchild", child.childNodes().get(0).tagName());
 assertEquals("", textNode.text());
 }

 @Test
 public void testParseFragment_InvalidInput() {
 String xml = "abc";
 try {
 xmlTreeBuilder.parseFragment(xml, "", "");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}
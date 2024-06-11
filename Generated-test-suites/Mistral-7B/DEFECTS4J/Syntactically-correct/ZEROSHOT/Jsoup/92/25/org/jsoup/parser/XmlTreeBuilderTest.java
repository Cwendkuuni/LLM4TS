package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class XmlTreeBuilderTest {

 @Mock
 private Parser parser;

 @Captor
 private ArgumentCaptor<Node> nodeCaptor;

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParse() throws Exception {
 String xmlInput = "<root><child>Hello</child></root>";
 Document parsedXml = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://baseurl.com");

 assertEquals("root", parsedXml.tagName());
 Element root = parsedXml.element(0);
 assertEquals("child", root.tagName());
 assertEquals("Hello", root.child(0).text());
 }

 @Test
 public void testParseFragment() throws Exception {
 String xmlFragment = "<child>Fragment</child>";
 List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlFragment, null, "http://baseurl.com", parser);

 assertEquals(1, parsedNodes.size());
 Node node = parsedNodes.get(0);
 assertEquals("child", node.tagName());
 assertEquals("Fragment", node.text());
 }

 @Test
 public void testParseDoctype() throws Exception {
 String xmlInput = "<!DOCTYPE root [ <!ENTITY myEntity SYSTEM \"file://myfile.dtd\"> ]>";
 Document parsedXml = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://baseurl.com");
 DocumentType doctypeNode = parsedXml.doctype();
 assertNotNull(doctypeNode);
 assertEquals("root", doctypeNode.name());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() throws Exception {
 xmlTreeBuilder.parse(new StringReader("Invalid XML"), "http://baseurl.com");
 }

 @Test
 public void testParseComment() throws Exception {
 String xmlInput = "<!-- This is a comment -->";
 Document parsedXml = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://baseurl.com");
 Comment commentNode = (Comment) parsedXml.child(0);
 assertEquals("This is a comment", commentNode.data());
 }

 @Test
 public void testParseCDATA() throws Exception {
 String xmlInput = "<![CDATA[This is CDATA]]>";
 Document parsedXml = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://baseurl.com");
 CDataNode cdataNode = (CDataNode) parsedXml.child(0);
 assertEquals("This is CDATA", cdataNode.data());
 }

 @Test
 public void testParseSelfClosingTag() throws Exception {
 String xmlInput = "<img src=\"image.jpg\" />";
 Document parsedXml = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://baseurl.com");
 Element imgNode = parsedXml.element(0);
 assertEquals("img", imgNode.tagName());
 assertNotNull(imgNode.attributes());
 assertEquals(0, imgNode.childNodes().size());
 }

 @Test
 public void testParseUnknownTag() throws Exception {
 String xmlInput = "<zoo tag=\"attribute\" />";
 Document parsedXml = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://baseurl.com");
 Element zooNode = parsedXml.element(0);
 assertEquals("zoo", zooNode.tagName());
 assertNotNull(zooNode.attributes());
 assertEquals("tag", zooNode.attribute("tag").toString());
 assertNotNull(zooNode.childNodes());
 assertEquals(0, zooNode.childNodes().size());
 }

 @Test
 public void testParseWithContext() throws Exception {
 String xmlInput = "<root><child><grandchild>Hello</grandchild></child></root>";
 Element context = new Element("root");
 Document parsedXml = xmlTreeBuilder.parseFragment(xmlInput, context, "http://baseurl.com", parser);
 Element grandchildNode = (Element) parsedXml.get(0).get(0);
 assertEquals("grandchild", grandchildNode.tagName());
 assertEquals("Hello", grandchildNode.text());
 assertEquals("root", context.tagName());
 }

 @Test
 public void testProcessStartTag() throws Exception {
 Element root = new Element("root");
 xmlTreeBuilder.insert(Mockito.mock(Token.StartTag.class));
 xmlTreeBuilder.currentElement().appendChild(root);
 verify(parser).parseStartTag("root", Mockito.eq(null), Mockito.isNull());
 }

 @Test
 public void testProcessEndTag() throws Exception {
 Element root = new Element("root");
 Element child = new Element("child");
 stack.add(root);
 stack.add(child);
 xmlTreeBuilder.popStackToClose(Mockito.mock(Token.EndTag.class));
 verify(parser).parseEndTag("child");
 }
}
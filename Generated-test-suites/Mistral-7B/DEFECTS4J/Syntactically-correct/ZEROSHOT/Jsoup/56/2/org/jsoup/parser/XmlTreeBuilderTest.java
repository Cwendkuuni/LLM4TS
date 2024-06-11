package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

 @Test
 public void testDefaultSettings() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.class, defaultSettings.getClass());
 }

 @Test
 public void testParse() throws Exception {
 String xmlInput = "<xml version=\"1.0\" encoding=\"UTF-8\"><root><child>Hello</child></root></xml>";
 Document xmlDoc = new XmlTreeBuilder().parse(xmlInput, null);
 Element root = xmlDoc.rootElement();
 assertEquals("root", root.tagName());
 List<Node> childNodes = root.childNodes();
 assertEquals(1, childNodes.size());
 assertEquals("child", childNodes.get(0).tagName());
 assertEquals("Hello", childNodes.get(0).text());
 }

 @Test
 public void testProcessStartTag() throws Exception {
 String xmlInput = "<xml version=\"1.0\" encoding=\"UTF-8\"><root><child></child></root></xml>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, null);
 Element root = xmlDoc.rootElement();
 Element child = root.childElements().get(0);
 assertEquals("child", child.tagName());
 assertTrue(child.hasAttrs());
 assertEquals(0, child.attr("id").length());
 }

 @Test
 public void testProcessEndTag() throws Exception {
 String xmlInput = "<xml version=\"1.0\" encoding=\"UTF-8\"><root><child><subChild/></child></root></xml>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, null);
 Element root = xmlDoc.rootElement();
 Element child = root.childElements().get(0);
 assertEquals(1, child.childNodes().size());
 child.remove();
 assertEquals(0, root.childNodes().size());
 }

 @Test
 public void testProcessComment() throws Exception {
 String xmlInput = "<xml version=\"1.0\" encoding=\"UTF-8\"><!-- This is a comment --></xml>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, null);
 List<Node> commentNodes = xmlDoc.childNodes();
 assertEquals(1, commentNodes.size());
 assertTrue(commentNodes.get(0) instanceof Comment);
 }

 @Test
 public void testProcessCharacter() throws Exception {
 String xmlInput = "<xml version=\"1.0\" encoding=\"UTF-8\">Text</xml>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, null);
 List<Node> textNodes = xmlDoc.childNodes();
 assertEquals(1, textNodes.size());
 assertTrue(textNodes.get(0) instanceof TextNode);
 assertEquals("Text", textNodes.get(0).text());
 }

 @Test
 public void testProcessDocType() throws Exception {
 String xmlInput = "<!DOCTYPE xml PUBLIC \"-//W3C//DTD XML 1.0//EN\" \"xml.dtd\">";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, null);
 DocumentType doctypeNode = (DocumentType) xmlDoc.childNodes().get(0);
 assertEquals("xml", doctypeNode.nodeName());
 assertNull(doctypeNode.publicId());
 assertNull(doctypeNode.systemId());
 }

 @Test
 public void testPopStackToClose() throws Exception {
 String xmlInput = "<xml version=\"1.0\" encoding=\"UTF-8\"><root><child><subChild/></child></root></xml>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, null);
 Element root = xmlDoc.rootElement();
 Element child = root.childElements().get(0);
 Element subChild = child.childElements().get(0);
 assertEquals(2, stack.size());
 xmlTreeBuilder.popStackToClose(subChild.tag());
 assertEquals(1, stack.size());
 assertEquals(child.tagName(), stack.peek().tagName());
 }

 @Test
 public void testParseFragment() throws Exception {
 String xmlFragment = "<child><subChild/></child>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 List<Node> fragmentNodes = xmlTreeBuilder.parseFragment(xmlFragment, null, null, ParseSettings.preserveCase);
 assertEquals(1, fragmentNodes.size());
 Element fragmentRoot = (Element) fragmentNodes.get(0);
 assertEquals("child", fragmentRoot.tagName());
 List<Node> fragmentChildNodes = fragmentRoot.childNodes();
 assertEquals(1, fragmentChildNodes.size());
 assertEquals("subChild", fragmentChildNodes.get(0).tagName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValidateFail() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Validate.fail("Unexpected token type: 123");
 }
}
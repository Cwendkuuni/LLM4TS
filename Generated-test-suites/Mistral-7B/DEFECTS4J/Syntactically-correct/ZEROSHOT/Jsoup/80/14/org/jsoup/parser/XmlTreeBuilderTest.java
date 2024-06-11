package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.List;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.XmlTreeBuilder.ParseSettings;

/**
 * Tests for {@link XmlTreeBuilder} class.
 */
public class XmlTreeBuilderTest {

 private static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
 private static final String INPUT_XML = "<root><element attr=\"value\">Content<![CDATA[Some content]]></element></root>";

 @Test
 public void testParse() {
 Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(INPUT_XML), "base-uri");

 assertEquals("root", xmlDoc.tagName());
 Element root = xmlDoc.rootElement();
 assertEquals(1, root.childElementCount());
 Element element = root.child(0);
 assertEquals("element", element.tagName());
 assertEquals("value", element.attr("attr"));
 assertEquals("Content", element.text());

 List<Node> fragNodes = new XmlTreeBuilder().parseFragment(INPUT_XML, "base-uri", ParseErrorList.noTracking(), ParseSettings.preserveCase).childNodes();
 assertEquals(2, fragNodes.size());
 assertNotNull(fragNodes.get(0));
 assertTrue(fragNodes.get(0) instanceof Element);
 assertNotNull(fragNodes.get(1));
 assertTrue(fragNodes.get(1) instanceof TextNode);
 }

 @Test
 public void testParseWithDeclaration() {
 Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(XML_DECLARATION + INPUT_XML), "base-uri");

 assertEquals("root", xmlDoc.tagName());
 Element root = xmlDoc.rootElement();
 assertEquals(1, root.childElementCount());
 Element element = root.child(0);
 assertEquals("element", element.tagName());
 assertEquals("value", element.attr("attr"));
 assertEquals("Content", element.text());

 List<Node> fragNodes = new XmlTreeBuilder().parseFragment(XML_DECLARATION + INPUT_XML, "base-uri", ParseErrorList.noTracking(), ParseSettings.preserveCase).childNodes();
 assertEquals(3, fragNodes.size());
 assertNotNull(fragNodes.get(0));
 assertTrue(fragNodes.get(0) instanceof DocumentType);
 assertNotNull(fragNodes.get(1));
 assertTrue(fragNodes.get(1) instanceof Element);
 assertNotNull(fragNodes.get(2));
 assertTrue(fragNodes.get(2) instanceof TextNode);
 }

 @Test
 public void testParseWithErrors() {
 ParseErrorList errorList = new ParseErrorList();
 Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(INPUT_XML), "base-uri", errorList, ParseSettings.preserveCase);
 assertTrue(errorList.size() > 0);
 }

 @Test
 public void testProcessStartTag() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 String input = "<root>";
 Element root = builder.parseFragment(input, "base-uri", ParseErrorList.noTracking(), ParseSettings.preserveCase).get(0);
 assertEquals("root", root.tagName());
 }

 @Test
 public void testProcessEndTag() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 String input = "<root><element/></root>";
 Element root = builder.parseFragment(input, "base-uri", ParseErrorList.noTracking(), ParseSettings.preserveCase).get(0);
 assertEquals(1, root.childElementCount());
 Element element = root.child(0);
 assertEquals(0, element.childElementCount());
 }

 @Test
 public void testProcessComment() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 String input = "<!-- Comment -->";
 List<Node> fragNodes = builder.parseFragment(input, "base-uri", ParseErrorList.noTracking(), ParseSettings.preserveCase).childNodes();
 assertEquals(1, fragNodes.size());
 assertTrue(fragNodes.get(0) instanceof Comment);
 }

 @Test
 public void testProcessCharacter() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 String input = "<root>Text</root>";
 List<Node> fragNodes = builder.parseFragment(input, "base-uri", ParseErrorList.noTracking(), ParseSettings.preserveCase).childNodes();
 assertEquals(3, fragNodes.size());
 assertNotNull(fragNodes.get(0));
 assertTrue(fragNodes.get(0) instanceof Element);
 assertNotNull(fragNodes.get(1));
 assertTrue(fragNodes.get(1) instanceof TextNode);
 assertNull(fragNodes.get(2));
 }

 @Test
 public void testProcessDoctype() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 String input = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ]>";
 List<Node> fragNodes = builder.parseFragment(input, "base-uri", ParseErrorList.noTracking(), ParseSettings.preserveCase).childNodes();
 assertEquals(2, fragNodes.size());
 assertNotNull(fragNodes.get(0));
 assertTrue(fragNodes.get(0) instanceof DocumentType);
 assertNull(fragNodes.get(1));
 }

 @Test
 public void testPopStackToClose() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 String input = "<root><element><element2></element2></element></root>";
 List<Node> fragNodes = builder.parseFragment(input, "base-uri", ParseErrorList.noTracking(), ParseSettings.preserveCase).childNodes();
 assertEquals(4, fragNodes.size());
 Element root = (Element) fragNodes.get(0);
 Element element = (Element) fragNodes.get(1);
 Element element2 = (Element) fragNodes.get(2);

 builder.popStackToClose(element.tag());
 assertEquals(3, root.childElementCount());
 assertFalse(element.parent().equals(root));
 }
}
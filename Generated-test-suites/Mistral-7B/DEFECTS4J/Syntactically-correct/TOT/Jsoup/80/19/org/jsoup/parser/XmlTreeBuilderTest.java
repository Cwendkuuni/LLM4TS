package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseEmptyXml() {
 Document doc = xmlTreeBuilder.parse("", "");
 assertTrue(doc.childNodes().isEmpty());
 }

 @Test
 public void testParseSimpleXml() {
 String xml = "<root><element attr=\"value\">Content</element></root>";
 Document doc = xmlTreeBuilder.parse(xml, "");
 Element root = doc.rootElement();
 assertEquals("root", root.tagName());
 Element element = root.child(0);
 assertEquals("element", element.tagName());
 assertEquals("value", element.attribute("attr"));
 assertEquals("Content", element.text());
 }

 @Test
 public void testParseXmlWithComments() {
 String xml = "<root><!--comment--><element attr=\"value\">Content</element></root>";
 Document doc = xmlTreeBuilder.parse(xml, "");
 Element root = doc.rootElement();
 assertEquals("root", root.tagName());
 Element element = root.child(0);
 assertEquals("element", element.tagName());
 assertEquals("value", element.attribute("attr"));
 assertEquals("Content", element.text());
 // check comment
 assertEquals(1, doc.childNodes().size());
 Node commentNode = doc.childNodes().get(0);
 assertTrue(commentNode instanceof Comment);
 assertEquals("comment", commentNode.nodeName());
 }

 @Test
 public void testParseXmlWithSelfClosingTag() {
 String xml = "<root><element tag=\"selfClosingTag\" /></root>";
 Document doc = xmlTreeBuilder.parse(xml, "");
 Element root = doc.rootElement();
 assertEquals("root", root.tagName());
 assertEquals(1, root.childNodes().size());
 Element element = root.child(0);
 assertEquals("selfClosingTag", element.tagName());
 assertFalse(element.hasText());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXmlWithInvalidXml() {
 String xml = "<root><element tag=\"<script>alert('Hello')\">Content</element>";
 Document doc = xmlTreeBuilder.parse(xml, "");
 fail("Expected IllegalArgumentException but got: " + doc);
 }

 @Test
 public void testParseXmlFragment() {
 String xml = "<root><element attr=\"value\">Content</element></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xml, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Document doc = new Document();
 doc.appendChild(nodes.get(0));
 Element root = doc.rootElement();
 assertEquals("root", root.tagName());
 Element element = root.child(0);
 assertEquals("element", element.tagName());
 assertEquals("value", element.attribute("attr"));
 assertEquals("Content", element.text());
 }
}
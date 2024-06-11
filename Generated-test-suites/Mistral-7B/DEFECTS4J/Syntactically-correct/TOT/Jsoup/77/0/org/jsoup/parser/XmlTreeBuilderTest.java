package org.jsoup.parser.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.ParseErrorList;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test for XmlTreeBuilder class
 */
public class XmlTreeBuilderTest {

 private static XmlTreeBuilder xmlTreeBuilder;

 @BeforeClass
 public static void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 /**
 * Test parsing a valid XML document
 */
 @Test
 public void testParseValidXml() {
 String xml = "<root><element1 attr1=\"value1\">Content1</element1><element2 attr2=\"value2\"><subElement attr3=\"value3\">Content3</subElement></element2></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "http://test.com");
 Element root = doc.select("root").first();
 Element element1 = root.select("element1").first();
 Element element2 = root.select("element2").first();
 Element subElement = element2.select("subElement").first();
 assertEquals("root", root.nodeName());
 assertEquals("element1", element1.nodeName());
 assertEquals("element2", element2.nodeName());
 assertEquals("subElement", subElement.nodeName());
 assertEquals("value1", element1.attr("attr1"));
 assertEquals("value2", element2.attr("attr2"));
 assertEquals("value3", subElement.attr("attr3"));
 assertEquals("Content1", element1.text());
 assertEquals("Content3", subElement.text());
 }

 /**
 * Test parsing an XML document with a processing instruction
 */
 @Test
 public void testParseXmlWithProcessingInstruction() {
 String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><element>Content</element></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "http://test.com");
 Element root = doc.select("root").first();
 Element element = root.select("element").first();
 assertEquals("root", root.nodeName());
 assertEquals("element", element.nodeName());
 assertNotNull(doc.outputSettings().xmlDeclaration());
 }

 /**
 * Test parsing an XML document with an empty element
 */
 @Test
 public void testParseXmlWithEmptyElement() {
 String xml = "<root><emptyElement/></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "http://test.com");
 Element root = doc.select("root").first();
 Element emptyElement = root.select("emptyElement").first();
 assertEquals("root", root.nodeName());
 assertEquals("emptyElement", emptyElement.nodeName());
 assertEquals(0, emptyElement.childNodes().size());
 }

 /**
 * Test parsing an XML document with a comment
 */
 @Test
 public void testParseXmlWithComment() {
 String xml = "<root><!-- this is a comment --><element>Content</element></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "http://test.com");
 Element root = doc.select("root").first();
 Element element = root.select("element").first();
 assertEquals("root", root.nodeName());
 assertEquals("element", element.nodeName());
 assertEquals(1, root.childNodes().size());
 assertNotNull(root.childNodes().get(0).getData());
 assertEquals("this is a comment", root.childNodes().get(0).getData());
 }

 /**
 * Test parsing an XML document with an invalid token
 */
 @Test(expected = AssertionError.class)
 public void testParseXmlWithInvalidToken() {
 String xml = "<root><!-- this is a comment --><invalidToken></root>";
 xmlTreeBuilder.parse(new StringReader(xml), "http://test.com");
 }

 /**
 * Test parsing an XML fragment
 */
 @Test
 public void testParseXmlFragment() {
 String xmlFragment = "<element attr1=\"value1\">Content1</element>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element element = (Element) nodes.get(0);
 assertEquals("element", element.nodeName());
 assertEquals("value1", element.attr("attr1"));
 assertEquals("Content1", element.text());
 }
}
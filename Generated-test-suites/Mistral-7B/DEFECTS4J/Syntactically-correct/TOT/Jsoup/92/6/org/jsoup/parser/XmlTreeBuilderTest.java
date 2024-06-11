package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseSettings;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Parser parser;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parser = new Parser(xmlTreeBuilder);
 }

 @Test
 public void testParseValidXml() {
 String xml = "<root><element attr=\"value\">Content</element></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), null);
 assertEquals("root", doc.tagName());
 Element root = doc.getElementsByTag("root").first();
 Element element = root.getElementsByTag("element").first();
 assertEquals("element", element.tagName());
 assertEquals("value", element.attr("attr"));
 assertEquals("Content", element.text());
 }

 @Test
 public void testParseEmptyXml() {
 Document doc = xmlTreeBuilder.parse(new StringReader(""), null);
 assertEquals("", doc.outerHtml());
 }

 @Test
 public void testParseInvalidXml() {
 String xml = "<root><element><invalid/></element>";
 try {
 xmlTreeBuilder.parse(new StringReader(xml), null);
 fail("Expected ParseException");
 } catch (Exception e) {
 assertTrue(e instanceof org.jsoup.parser.ParseException);
 }
 }

 @Test
 public void testParseXmlWithSelfClosingTag() {
 String xml = "<root><element tag='self-closing'/></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), null);
 Element element = doc.getElementsByTag("element").first();
 assertTrue(element.isSelfClosing());
 }

 @Test
 public void testParseXmlWithComments() {
 String xml = "<root><!-- comment1 --><element attr=\"value\"><!-- comment2 --></element><!-- comment3 --></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), null);
 List<Node> commentNodes = doc.getElementsByTag("!--");
 assertEquals(3, commentNodes.size());
 }

 @Test
 public void testParseXmlWithDoctype() {
 String xml = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ]><root><element attr=\"value\"></element></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), null);
 DocumentType doctype = doc.doctype();
 assertEquals("root", doctype.nodeName());
 Element element = doc.getElementsByTag("element").first();
 assertEquals("element", element.tagName());
 assertEquals("value", element.attr("attr"));
 }

 @Test
 public void testParseXmlFragment() {
 String xmlFragment = "<element attr=\"value\">Content</element>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, null, parser);
 Element element = (Element) nodes.get(0);
 assertEquals("element", element.tagName());
 assertEquals("value", element.attr("attr"));
 assertEquals("Content", element.text());
 }

 @Test
 public void testParseXmlFragmentWithContext() {
 String xmlFragment = "<root><element attr=\"value\">Content</element></root>";
 Element context = Jsoup.parse("<root></root>").getElementsByTag("root").first();
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, context, null, parser);
 Element element = (Element) nodes.get(0);
 assertEquals("element", element.tagName());
 assertEquals("value", element.attr("attr"));
 assertEquals("Content", element.text());
 }
}
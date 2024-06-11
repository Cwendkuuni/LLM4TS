package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.ParseSettings;
import org.jsoup.helper.Validate;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParse() {
 String input = "<root><child element='value'/><anotherChild/></root>";
 String baseUri = "http://www.example.com";
 Document doc = xmlTreeBuilder.parse(new StringReader(input), baseUri);

 Element root = doc.getElementById("root");
 assertEquals("root", root.nodeName());
 assertNotNull(root);

 Element child = root.getElementsByTag("child").first();
 assertEquals("child", child.nodeName());
 assertEquals("value", child.getAttribute("element"));
 assertNotNull(child);

 Element anotherChild = root.getElementsByTag("anotherChild").first();
 assertEquals("anotherChild", anotherChild.nodeName());
 assertNull(anotherChild.attributes());
 assertNotNull(anotherChild);
 }

 @Test
 public void testProcessStartTag() {
 String input = "<root><child element='value'/><anotherChild/></root>";
 String baseUri = "http://www.example.com";
 XmlTreeBuilder xmlTreeBuilderSpy = new SpyXmlTreeBuilder(xmlTreeBuilder); // extend XmlTreeBuilder for logging

 List<Node> nodes = xmlTreeBuilderSpy.parseFragment(input, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element root = (Element) nodes.get(0);

 assertEquals(2, nodes.size());
 assertEquals("root", root.nodeName());
 assertNotNull(root);

 Element child = (Element) nodes.get(1);
 assertEquals("child", child.nodeName());
 assertEquals("value", child.getAttribute("element"));
 assertNotNull(child);
 }

 @Test(expected = RuntimeException.class)
 public void testUnexpectedTokenType() {
 String input = "<root><child element='value'/><anotherChild/><foo>";
 String baseUri = "http://www.example.com";
 XmlTreeBuilder xmlTreeBuilderSpy = new SpyXmlTreeBuilder(xmlTreeBuilder); // extend XmlTreeBuilder for logging

 List<Node> nodes = xmlTreeBuilderSpy.parseFragment(input, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 // this test case should fail with an exception
 }

 @Test
 public void testProcessComment() {
 String input = "<!-- This is a comment --><root><child element='value'/><anotherChild/></root>";
 String baseUri = "http://www.example.com";
 XmlTreeBuilder xmlTreeBuilderSpy = new SpyXmlTreeBuilder(xmlTreeBuilder); // extend XmlTreeBuilder for logging

 List<Node> nodes = xmlTreeBuilderSpy.parseFragment(input, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element root = (Element) nodes.get(0);

 assertEquals(3, nodes.size());
 assertNotNull(root);

 Comment comment = (Comment) nodes.get(1);
 assertEquals("This is a comment", comment.getData());
 assertNotNull(comment);
 }

 @Test
 public void testProcessEndTag() {
 String input = "<root><child element='value'/><child></child></root>";
 String baseUri = "http://www.example.com";
 XmlTreeBuilder xmlTreeBuilderSpy = new SpyXmlTreeBuilder(xmlTreeBuilder); // extend XmlTreeBuilder for logging

 List<Node> nodes = xmlTreeBuilderSpy.parseFragment(input, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element root = (Element) nodes.get(0);

 assertEquals(3, nodes.size());
 assertEquals("root", root.nodeName());
 assertNotNull(root);

 Element child1 = (Element) nodes.get(1);
 assertEquals("child", child1.nodeName());
 assertEquals("value", child1.getAttribute("element"));
 assertNotNull(child1);

 Element child2 = (Element) nodes.get(2);
 assertEquals("child", child2.nodeName());
 assertNull(child2.attributes());
 assertNotNull(child2);
 }

 @Test
 public void testProcessCharacter() {
 String input = "<root><child>value</child></root>";
 String baseUri = "http://www.example.com";
 XmlTreeBuilder xmlTreeBuilderSpy = new SpyXmlTreeBuilder(xmlTreeBuilder); // extend XmlTreeBuilder for logging

 List<Node> nodes = xmlTreeBuilderSpy.parseFragment(input, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element root = (Element) nodes.get(0);

 assertEquals(3, nodes.size());
 assertEquals("root", root.nodeName());
 assertNotNull(root);

 Element child = (Element) nodes.get(1);
 assertEquals("child", child.nodeName());
 assertEquals("value", child.childNodes().get(0).nodeValue());
 assertNotNull(child);
 }

 @Test
 public void testProcessDocumentType() {
 String input = "<!DOCTYPE root [<!ATTLIST child element CDATA #REQUIRED>]<root><child/></root>";
 String baseUri = "http://www.example.com";
 XmlTreeBuilder xmlTreeBuilderSpy = new SpyXmlTreeBuilder(xmlTreeBuilder); // extend XmlTreeBuilder for logging

 List<Node> nodes = xmlTreeBuilderSpy.parseFragment(input, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element root = (Element) nodes.get(0);

 assertEquals(3, nodes.size());
 assertEquals("root", root.nodeName());
 assertNotNull(root);

 DocumentType doctype = (DocumentType) nodes.get(1);
 assertEquals("root", doctype.nodeName());
 assertEquals("root", doctype.getName());
 assertEquals("http://www.w3.org/1998/10/XML/DTD", doctype.getPublicId());
 assertNull(doctype.getSystemId());
 assertNull(doctype.getPubSysKey());
 assertNotNull(doctype);

 Element child = (Element) nodes.get(2);
 assertEquals("child", child.nodeName());
 assertNull(child.attributes());
 assertNotNull(child);
 }

 @Test
 public void testProcessCData() {
 String input = "<root><![CDATA[value]]></root>";
 String baseUri = "http://www.example.com";
 XmlTreeBuilder xmlTreeBuilderSpy = new SpyXmlTreeBuilder(xmlTreeBuilder); // extend XmlTreeBuilder for logging

 List<Node> nodes = xmlTreeBuilderSpy.parseFragment(input, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element root = (Element) nodes.get(0);

 assertEquals(2, nodes.size());
 assertEquals("root", root.nodeName());
 assertNotNull(root);

 CDataNode cdata = (CDataNode) nodes.get(1);
 assertEquals("value", cdata.getData());
 assertNotNull(cdata);
 }
}

 class SpyXmlTreeBuilder extends XmlTreeBuilder {
 private XmlTreeBuilder xmlTreeBuilder;

 public SpyXmlTreeBuilder(XmlTreeBuilder xmlTreeBuilder) {
 this.xmlTreeBuilder = xmlTreeBuilder;
 }

 @Override
 public void initialiseParse(Reader input, String baseUri, ParseErrorList errors, ParseSettings settings) {
 super.initialiseParse(input, baseUri, errors, settings);
 System.out.println("Initialise parse called.");
 }

 @Override
 protected void initialiseParse(Reader input, String baseUri, ParseErrorList errors, ParseSettings settings) {
 super.initialiseParse(input, baseUri, errors, settings);
 xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings); // call superclass for real parsing
 }

 @Override
 public Document parse(Reader input, String baseUri) {
 Document document = xmlTreeBuilder.parse(input, baseUri);
 System.out.println("Document parsed.");
 return document;
 }

 @Override
 public Document parse(String input, String baseUri) {
 Document document = xmlTreeBuilder.parse(input, baseUri);
 System.out.println("Document parsed.");
 return document;
 }
}
package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParse() {
 String xmlString = "<root><element attr=\"value\"><subElement /></element></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlString), "testBaseUrl");
 Element root = document.getRootElement();
 assertEquals("root", root.tagName());
 assertNotNull(root.child(0));
 assertEquals("element", root.child(0).tagName());
 assertNotNull(root.child(0).child(0));
 assertEquals("subElement", root.child(0).child(0).tagName());
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<element attr=\"value\"><subElement /></element>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "testBaseUrl", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element element = (Element) nodes.get(0);
 assertEquals("element", element.tagName());
 assertNotNull(element.child(0));
 assertEquals("subElement", element.child(0).tagName());
 }

 @Test
 public void testEmptyDocument() {
 Document document = xmlTreeBuilder.parse(new StringReader(""), "testBaseUrl");
 assertNotNull(document);
 assertNull(document.getRootElement());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 xmlTreeBuilder.parse(null, "testBaseUrl");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 xmlTreeBuilder.parse(new StringReader("<root><element></root>"), null);
 }

 @Test
 public void testParseWithDoctype() {
 String xmlString = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ]><root><element attr=\"value\"><subElement /></element></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlString), "testBaseUrl");
 Element root = document.getRootElement();
 assertEquals("root", root.tagName());
 assertNotNull(root.child(0));
 assertEquals("element", root.child(0).tagName());
 assertNotNull(root.child(0).child(0));
 assertEquals("subElement", root.child(0).child(0).tagName());
 DocumentType doctype = root.document().doctype();
 assertNotNull(doctype);
 assertEquals("root", doctype.name());
 }

 @Test
 public void testParseComment() {
 String xmlString = "<!-- This is a comment --><root><element attr=\"value\"><subElement /></element></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlString), "testBaseUrl");
 Element root = document.getRootElement();
 assertNotNull(root.childNodes().get(0));
 assertEquals(Comment.class.getName(), root.childNodes().get(0).getClass().getName());
 assertEquals("This is a comment", root.childNodes().get(0).text());
 }

 @Test
 public void testParseCharacter() {
 String xmlString = "<root>&#123;</root>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlString), "testBaseUrl");
 TextNode textNode = (TextNode) document.getRootElement().childNodes().get(0);
 assertEquals("&#123;", textNode.text());
 }

 @Test
 public void testParseSelfClosingTag() {
 String xmlString = "<root><element /></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlString), "testBaseUrl");
 Element root = document.getRootElement();
 assertNotNull(root.child(0));
 assertEquals("element", root.child(0).tagName());
 assertNull(root.child(0).childNodes());
 }
}
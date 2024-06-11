package org.jsoup.parser;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 private Document xmlDoc;

 @Before
 public void setUp() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlDoc = xmlTreeBuilder.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<root>\n" +
         "<element1 attr1=\"value1\">Content1</element1>\n" +
         "<element2>\n" +
         "<subElement attr2=\"value2\">Content2</subElement>\n" +
         "</element2>\n" +
         "</root>", "http://example.com");
 }

 @After
 public void tearDown() {
 xmlDoc = null;
 }

 @Test
 public void testParseValidXmlWithAttributes() {
 Element root = xmlDoc.rootElement();
 Assert.assertEquals("root", root.tagName());
 Element element1 = root.getElementsByTagName("element1").get(0);
 Assert.assertEquals("element1", element1.tagName());
 Assert.assertEquals("value1", element1.attr("attr1"));
 Assert.assertEquals("Content1", element1.text());
 Element subElement = element1.getElementsByTagName("subElement").get(0);
 Assert.assertEquals("subElement", subElement.tagName());
 Assert.assertEquals("value2", subElement.attr("attr2"));
 Assert.assertEquals("Content2", subElement.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidXml() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 try {
 xmlTreeBuilder.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<root>\n" +
         "<element1 attr1=\"value1\">Content1</element1>\n" +
         "<element2>\n" +
         "<subElement attr2=\"value2\">Content2</subElement>\n" +
         "</root>", "http://example.com");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 XmlTreeBuilder xmlTreeBuilder2 = new XmlTreeBuilder();
 String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 try {
 xmlTreeBuilder2.parse(new StringReader(input), "http://example.com");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseXMLWithEmptyElement() {
 Document emptyXmlDoc = XmlTreeBuilder.newInstance().parse("<root></root>", "http://example.com");
 Element root = emptyXmlDoc.rootElement();
 Assert.assertEquals("root", root.tagName());
 Assert.assertTrue(root.children().isEmpty());
 }

 @Test
 public void testParseXMLWithSelfClosingTag() {
 Document xmlDocSelfClosing = XmlTreeBuilder.newInstance().parse("<root><selfClosingTag/></root>", "http://example.com");
 Element root = xmlDocSelfClosing.rootElement();
 Assert.assertEquals("root", root.tagName());
 List<Element> selfClosingTags = root.getElementsByTagName("selfClosingTag");
 Assert.assertEquals(1, selfClosingTags.size());
 Element selfClosingTag = selfClosingTags.get(0);
 Assert.assertTrue(selfClosingTag.children().isEmpty());
 }

 @Test
 public void testParseXMLWithEmptyComment() {
 Document xmlDocWithComment = XmlTreeBuilder.newInstance().parse("<root><!-- --></root>", "http://example.com");
 Element root = xmlDocWithComment.rootElement();
 List<Node> comments = root.getElementsByTagName("comment");
 Assert.assertEquals(1, comments.size());
 Comment comment = (Comment) comments.get(0);
 Assert.assertEquals("<!-- -->", comment.data());
 }

 @Test
 public void testParseXMLWithCDataSection() {
 Document xmlDocWithCdata = XmlTreeBuilder.newInstance().parse("<root><![CDATA[This is some CDATA section]]></root>", "http://example.com");
 Element root = xmlDocWithCdata.rootElement();
 List<Node> cdataSections = root.getElementsByTagName("cdata-section");
 Assert.assertEquals(1, cdataSections.size());
 CDataNode cdataNode = (CDataNode) cdataSections.get(0);
 Assert.assertEquals("This is some CDATA section", cdataNode.data());
 }

 @Test
 public void testParseXMLWithDoctype() {
 Document xmlDocWithDoctype = XmlTreeBuilder.newInstance().parse("<!DOCTYPE root SYSTEM \"root.dtd\">\n" +
         "<root>\n" +
         "<element attr=\"value\">Content</element>\n" +
         "</root>", "http://example.com");
 Element root = xmlDocWithDoctype.rootElement();
 DocumentType docType = root.getDoctype();
 Assert.assertEquals("root", docType.getName());
 }

 @Test
 public void testParseXMLWithFragment() {
 String xmlFragment = "<root>\n" +
         "<element1 attr1=\"value1\">Content1</element1>\n" +
         "<element2>\n" +
         "<subElement attr2=\"value2\">Content2</subElement>\n" +
         "</element2>\n";
 List<Node> nodes = XmlTreeBuilder.newInstance().parseFragment(xmlFragment, null, "http://example.com").get(0).childNodes();
 Element root = (Element) nodes.get(0);
 Assert.assertEquals("root", root.tagName());
 Element element1 = (Element) nodes.get(1);
 Assert.assertEquals("element1", element1.tagName());
 Assert.assertEquals("value1", element1.attr("attr1"));
 Assert.assertEquals("Content1", element1.text());
 Element subElement = (Element) nodes.get(3);
 Assert.assertEquals("subElement", subElement.tagName());
 Assert.assertEquals("value2", subElement.attr("attr2"));
 Assert.assertEquals("Content2", subElement.text());
 }

 @Test
 public void testParseXMLWithFragmentAndContext() {
 String inputXmlFragment = "<root>\n" +
         "<element attr=\"value\">Content</element>\n" +
         "<element>\n" +
         "<subElement attr2=\"value2\">Content2</subElement>\n" +
         "</element>\n";
 Element context = Jsoup.parse("<context>\n" +
         "<parentElement attr=\"value3\">ParentContent</parentElement>\n" +
         "</context>", "http://example.com").get(0);
 List<Node> nodes = XmlTreeBuilder.newInstance().parseFragment(inputXmlFragment, context, "http://example.com").get(0).childNodes();
 Element root = (Element) nodes.get(0);
 Assert.assertEquals("root", root.tagName());
 Element element = (Element) nodes.get(1);
 Assert.assertEquals("element", element.tagName());
 Assert.assertEquals("value", element.attr("attr"));
 Assert.assertEquals("Content", element.text());
 Element subElement = (Element) nodes.get(3);
 Assert.assertEquals("subElement", subElement.tagName());
 Assert.assertEquals("value2", subElement.attr("attr2"));
 Assert.assertEquals("Content2", subElement.text());
 Element parentElement = context.getElementsByTagName("parentElement").get(0);
 Assert.assertEquals("parentElement", parentElement.tagName());
 Assert.assertEquals("ParentContent", parentElement.text());
 }
}
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XMLElement;
import org.jsoup.nodes.XMLAnnotation;
import org.jsoup.nodes.XMLDeclaration;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.AttributeSelector;
import org.jsoup.nodes.BaseElement;
import org.junit.Assert;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 @Test
 public void testDefaultSettings() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root><tag attr=\"value\"><subtag /></tag></root>";
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://test.com");

 Assert.assertEquals("root", xmlDoc.tagName());
 List<Node> rootChildren = xmlDoc.childNodes();
 Assert.assertEquals(1, rootChildren.size());
 Element rootElement = (Element) rootChildren.get(0);
 Assert.assertEquals("tag", rootElement.tagName());
 List<Node> tagChildren = rootElement.childNodes();
 Assert.assertEquals(1, tagChildren.size());
 Assert.assertTrue(tagChildren.get(0) instanceof Element);
 }

 @Test
 public void testParseFragment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlFragment = "<tag attr=\"value\"><subtag /></tag>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "http://test.com", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);

 Element tagElement = (Element) nodes.get(0);
 Assert.assertEquals("tag", tagElement.tagName());
 List<Node> subtagChildren = tagElement.childNodes();
 Assert.assertEquals(0, subtagChildren.size());
 }

 @Test
 public void testProcessStartTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<tag attr=\"value\"><subtag /></tag>";
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://test.com");
 Element tagElement = (Element) xmlDoc.childNodes().get(0);
 Element subtagElement = tagElement.childNodes().get(0);

 Assert.assertEquals("tag", tagElement.tagName());
 Assert.assertEquals(1, tagElement.childNodes().size());
 Assert.assertEquals("subtag", subtagElement.tagName());
 Assert.assertEquals(0, subtagElement.childNodes().size());
 }

 @Test
 public void testProcessEndTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<tag><subtag /></tag>";
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://test.com");
 Element tagElement = (Element) xmlDoc.childNodes().get(0);

 Assert.assertEquals("tag", tagElement.tagName());
 Assert.assertEquals(1, tagElement.childNodes().size());
 Element subtagElement = (Element) tagElement.childNodes().get(0);
 tagElement.removeChild(subtagElement);
 Assert.assertEquals(0, tagElement.childNodes().size());
 }

 @Test
 public void testProcessComment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<!-- This is a comment -->";
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://test.com");
 List<Node> nodes = xmlDoc.childNodes();
 Assert.assertEquals(1, nodes.size());
 Comment comment = (Comment) nodes.get(0);
 Assert.assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testProcessCharacter() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<tag>&amp;<subtag />&lt;<subsubtag />&gt;</tag>";
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://test.com");
 List<Node> nodes = xmlDoc.childNodes();
 Element tagElement = (Element) nodes.get(0);
 List<Node> tagChildren = tagElement.childNodes();
 Assert.assertEquals(3, tagChildren.size());
 Assert.assertTrue(tagChildren.get(0) instanceof TextNode);
 Assert.assertEquals("&amp;", ((TextNode) tagChildren.get(0)).getData());
 Assert.assertTrue(tagChildren.get(1) instanceof Element);
 Assert.assertEquals("subtag", tagChildren.get(1).tagName());
 Assert.assertEquals(0, tagChildren.get(1).childNodes().size());
 Assert.assertTrue(tagChildren.get(2) instanceof TextNode);
 Assert.assertEquals("&lt;<subsubtag />&gt;", ((TextNode) tagChildren.get(2)).getData());
 }

 @Test
 public void testProcessDoctype() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<!DOCTYPE root SYSTEM \"test.dtd\">";
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://test.com");
 DocumentType doctypeNode = (DocumentType) xmlDoc.childNodes().get(0);
 Assert.assertEquals("root", doctypeNode.nodename());
 Assert.assertEquals("test.dtd", doctypeNode.systemId());
 }

 @Test(expected = AssertionError.class)
 public void testInvalidTokenType() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<tag>"; // missing token type
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://test.com");
 }
}
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.StringReader;
import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Doctype;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;

import org.junit.Test;

public class XmlTreeBuilderTest {

 private static final String TEST_XML = "<root><element attr=\"value\"><subelement/><subelement/></element></root>";

 @Test
 public void testParse() {
 Document doc = new XmlTreeBuilder().parse(new StringReader(TEST_XML), "http://test.com");
 assertEquals("root", doc.tagName());
 assertNotNull(doc.childNodes());
 assertEquals(1, doc.childNodes().size());
 Element root = (Element) doc.childNodes().get(0);
 assertEquals("element", root.tagName());
 assertNotNull(root.childNodes());
 assertEquals(2, root.childNodes().size());
 Element element = (Element) root.childNodes().get(0);
 assertEquals("subelement", element.tagName());
 assertNull(element.childNodes());
 element = (Element) root.childNodes().get(1);
 assertNotNull(element.childNodes());
 assertEquals(0, element.childNodes().size());
 }

 @Test
 public void testParseFragment() {
 List<Node> fragment = new XmlTreeBuilder().parseFragment("<root><element attr=\"value\"><subelement/><subelement/></element></root>", "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Document doc = Jsoup.parse(fragment.toString(), "http://test.com", Parser.xmlParser());
 assertEquals("root", doc.tagName());
 assertNotNull(doc.childNodes());
 assertEquals(1, doc.childNodes().size());
 Element root = (Element) doc.childNodes().get(0);
 assertEquals("element", root.tagName());
 assertNotNull(root.childNodes());
 assertEquals(2, root.childNodes().size());
 Element element = (Element) root.childNodes().get(0);
 assertEquals("subelement", element.tagName());
 assertNull(element.childNodes());
 element = (Element) root.childNodes().get(1);
 assertNotNull(element.childNodes());
 assertEquals(0, element.childNodes().size());
 }

 @Test
 public void testParseWithEmptyFragment() {
 List<Node> fragment = new XmlTreeBuilder().parseFragment("", "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertNotNull(fragment);
 assertFalse(fragment.isEmpty());
 Document doc = Jsoup.parse(fragment.toString(), "http://test.com", Parser.xmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testProcessStartTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Element root = new Element("root");
 xmlTreeBuilder.initialiseParse(new StringReader(TEST_XML), "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.runParser();
 Element element = null;
 for (Object node : xmlTreeBuilder.stack) {
 if (node instanceof Element) {
 element = (Element) node;
 break;
 }
 }
 assertEquals("element", element.tagName());
 }

 @Test
 public void testProcessEndTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Element root = new Element("root");
 Element element = new Element("element");
 root.appendChild(element);
 xmlTreeBuilder.stack.add(root);
 xmlTreeBuilder.stack.add(element);
 Token endTag = new Token.EndTag("element");
 xmlTreeBuilder.process(endTag);
 assertEquals(1, xmlTreeBuilder.stack.size());
 assertEquals(root, xmlTreeBuilder.stack.get(0));
 }

 @Test
 public void testProcessComment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Token comment = new Token.Comment("<!-- comment -->");
 xmlTreeBuilder.process(comment);
 Comment commentNode = (Comment) xmlTreeBuilder.currentElement().childNodes().get(0);
 assertEquals("comment", commentNode.getData());
 }

 @Test
 public void testProcessCharacter() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Token character = new Character('a');
 xmlTreeBuilder.process(character);
 TextNode textNode = (TextNode) xmlTreeBuilder.currentElement().childNodes().get(xmlTreeBuilder.currentElement().childNodes().size() - 1);
 assertEquals("a", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Doctype doctype = new Doctype("html", null, null);
 xmlTreeBuilder.process(new Token.Doctype("html"));
 assertEquals("html", xmlTreeBuilder.doc.doctype().tagName());
 }

 @Test
 public void testProcessEOF() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse(new StringReader(TEST_XML), "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.runParser();
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test(expected = AssertionError.class)
 public void testValidateFail() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.process(new Token(-1));
 }
}
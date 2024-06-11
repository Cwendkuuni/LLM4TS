package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.helper.Validate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for XmlTreeBuilder
 */
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 /**
 * Test method for {@link XmlTreeBuilder#parse(String, String)}
 */
 @Test
 public void testParse() {
 String xmlString = "<root><child tag='test'/><anotherChild tag='test2'/></root>";
 Document xmlDoc = xmlTreeBuilder.parse(xmlString, "");

 Element root = xmlDoc.getRootElement();
 assertEquals("root", root.tagName());
 assertEquals(2, root.childNodeSize());

 Element child = root.getChild(0);
 assertEquals("child", child.tagName());
 assertEquals("test", child.attribute("tag"));

 Element anotherChild = root.getChild(1);
 assertEquals("anotherChild", anotherChild.tagName());
 assertEquals("test2", anotherChild.attribute("tag"));
 }

 /**
 * Test method for {@link XmlTreeBuilder#parseFragment(String, String, ParseErrorList, ParseSettings)}
 */
 @Test
 public void testParseFragment() {
 String xmlFragment = "<child tag='test'/><anotherChild tag='test2'/>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);

 Element root = (Element) nodes.get(0);
 assertEquals("child", root.tagName());
 assertEquals("test", root.attribute("tag"));

 nodes = nodes.subList(1, nodes.size());
 Element anotherRoot = (Element) nodes.get(0);
 assertEquals("anotherChild", anotherRoot.tagName());
 assertEquals("test2", anotherRoot.attribute("tag"));
 }

 /**
 * Test method for {@link XmlTreeBuilder#initialiseParse(String, String, ParseErrorList, ParseSettings)}
 */
 @Test
 public void testInitialiseParse() {
 String invalidXml = "<root><child tag='test'></child><!invalid>";
 Validate.expect(IllegalArgumentException.class);
 xmlTreeBuilder.initialiseParse(invalidXml, "");
 }

 /**
 * Test method for {@link XmlTreeBuilder#process(Token)}
 */
 @Test
 public void testProcess() {
 // Given
 String xmlString = "<root><child tag='test'/><anotherChild tag='test2'/></root>";
 Tokenizer tokenizer = new Tokenizer(xmlString);
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 XmlTreeBuilder.ParseSettings settings = XmlTreeBuilder.ParseSettings.preserveCase;
 XmlTreeBuilder.ParseSettings defaultSettings = XmlTreeBuilder.ParseSettings.preserveCase;
 XmlTreeBuilder.ParseErrorList errors = XmlTreeBuilder.ParseErrorList.noTracking();
 xmlTreeBuilder.initialiseParse(xmlString, "", errors, settings);

 // When
 Token token = tokenizer.next();

 // Then
 assertTrue(xmlTreeBuilder.process(token));
 }

 /**
 * Test method for {@link XmlTreeBuilder#insertNode(Node)}
 */
 @Test
 public void testInsertNode() {
 // Given
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = new Document();
 Element root = new Element("root", "", new Attribute("xmlns", "http://www.w3.org/2000/xmlns/"));
 doc.setRootElement(root);
 xmlTreeBuilder.initialiseParse("", "", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);
 stack.push(doc);

 // When
 TextNode textNode = new TextNode("test", "");
 xmlTreeBuilder.insertNode(textNode);
 Element element = new Element("element", "", new Attribute("test", "test"));
 xmlTreeBuilder.insertNode(element);

 // Then
 assertEquals(2, doc.childNodeSize());
 assertEquals("test", doc.child(0).toString());
 assertEquals("element", doc.child(1).tagName());
 }

 /**
 * Test method for {@link XmlTreeBuilder#insert(Token.StartTag)}
 */
 @Test
 public void testInsert() {
 // Given
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = new Document();
 Element root = new Element("root", "", new Attribute("xmlns", "http://www.w3.org/2000/xmlns/"));
 doc.setRootElement(root);
 xmlTreeBuilder.initialiseParse("", "", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);
 stack.push(doc);

 // When
 Token.StartTag startTag = new Token.StartTag("child", new StringAttribute("tag", "test"));
 xmlTreeBuilder.insert(startTag);

 // Then
 assertEquals(1, doc.childNodeSize());
 assertEquals("child", doc.child(0).tagName());
 assertEquals("test", doc.child(0).attribute("tag"));
 }

 /**
 * Test method for {@link XmlTreeBuilder#insert(Token.Comment)}
 */
 @Test
 public void testInsertComment() {
 // Given
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = new Document();
 xmlTreeBuilder.initialiseParse("<!--test-->", "", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);
 stack.push(doc);

 // When
 Token.Comment commentToken = new Token.Comment("test");
 xmlTreeBuilder.insert(commentToken);

 // Then
 assertEquals(1, doc.childNodeSize());
 assertEquals("-- test --", doc.child(0).toString());
 }

 /**
 * Test method for {@link XmlTreeBuilder#insert(Token.Character)}
 */
 @Test
 public void testInsertCharacter() {
 // Given
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = new Document();
 xmlTreeBuilder.initialiseParse("", "", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);
 stack.push(doc);

 // When
 Token.Character characterToken = new Token.Character('t');
 xmlTreeBuilder.insert(characterToken);

 // Then
 assertEquals(1, doc.childNodeSize());
 assertEquals("t", doc.child(0).toString());
 }

 /**
 * Test method for {@link XmlTreeBuilder#insert(Token.Doctype)}
 */
 @Test
 public void testInsertDoctype() {
 // Given
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = new Document();
 xmlTreeBuilder.initialiseParse("<!DOCTYPE root SYSTEM 'test.dtd'>", "", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);
 stack.push(doc);

 // When
 Token.Doctype doctype = new Token.Doctype("root", null, null, null);
 xmlTreeBuilder.insert(doctype);

 // Then
 assertEquals(1, doc.childNodeSize());
 assertEquals("root", doc.child(0).tagName());
 }

 /**
 * Test method for {@link XmlTreeBuilder#popStackToClose(Token.EndTag)}
 */
 @Test
 public void testPopStackToClose() {
 // Given
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = new Document();
 Element root = new Element("root", "", new Attribute("xmlns", "http://www.w3.org/2000/xmlns/"));
 doc.setRootElement(root);
 xmlTreeBuilder.initialiseParse("", "", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);
 stack.push(doc);
 stack.push(root);

 // When
 Token.EndTag endTag = new Token.EndTag("child");
 xmlTreeBuilder.popStackToClose(endTag);

 // Then
 assertEquals(0, stack.size());
 }
}
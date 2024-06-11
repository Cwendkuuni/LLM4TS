package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Tokenizer;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for {@link XmlTreeBuilder}.
 */
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = xmlTreeBuilder.parse(new StringReader("<root><element attr=\"value\"><child/><comment>Hello World!</comment></element></root>"), null);
 }

 @Test
 public void testInitialiseParse() {
 assertEquals("root", document.tagName());
 assertSame(xmlTreeBuilder, document.parser());
 }

 @Test
 public void testParse() {
 assertEquals(4, document.childNodeSize());
 Element root = document.elementSibling(0);
 assertEquals("root", root.tagName());
 assertEquals("value", root.attribute(0).getValue());
 Element element = root.elementChild(0);
 assertEquals("element", element.tagName());
 assertNull(element.childNodes().get(0).prevSibling());
 Comment comment = (Comment) element.childNodes().get(1);
 assertEquals("Hello World!", comment.data());
 }

 @Test
 public void testProcessStartTag() {
 Tokenizer tokenizer = new Tokenizer(new StringReader("<root><element attr=\"value\"><child/></element></root>"));
 assertEquals(StartTag.class, tokenizer.nextToken().getClass());
 xmlTreeBuilder.initialiseParse(tokenizer, null, new Parser(xmlTreeBuilder));
 assertEquals(1, xmlTreeBuilder.stack.size());
 Element root = (Element) xmlTreeBuilder.stack.get(0);
 assertEquals("root", root.tagName());
 xmlTreeBuilder.process(tokenizer.nextToken());
 assertEquals(2, xmlTreeBuilder.stack.size());
 Element element = (Element) xmlTreeBuilder.stack.get(1);
 assertEquals("element", element.tagName());
 xmlTreeBuilder.process(tokenizer.nextToken());
 assertEquals(3, xmlTreeBuilder.stack.size());
 Element child = (Element) xmlTreeBuilder.stack.get(2);
 assertEquals(0, child.childNodeSize());
 xmlTreeBuilder.process(tokenizer.nextToken());
 assertEquals(2, xmlTreeBuilder.stack.size());
 xmlTreeBuilder.process(tokenizer.nextToken()); // end tag
 assertEquals(1, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testProcessComment() {
 Tokenizer tokenizer = new Tokenizer(new StringReader("<root><!--comment--></root>"));
 assertEquals(Comment.class, tokenizer.nextToken().getClass());
xmlTreeBuilder.initialiseParse(tokenizer, null, new Parser(xmlTreeBuilder));
 assertEquals(1, xmlTreeBuilder.stack.size());
 Comment comment = (Comment) xmlTreeBuilder.stack.get(0);
 assertEquals("comment", comment.getData());
 xmlTreeBuilder.process(tokenizer.nextToken());
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testProcessCharacter() {
 Tokenizer tokenizer = new Tokenizer(new StringReader("<root><![CDATA[hello world]]></root>"));
 assertEquals(Character.class, tokenizer.nextToken().getClass());
 assertEquals(Character.Type.cdata, tokenizer.nextToken().asCharacter().type());
 xmlTreeBuilder.initialiseParse(tokenizer, null, new Parser(xmlTreeBuilder));
 assertEquals(1, xmlTreeBuilder.stack.size());
 CDataNode cdataNode = (CDataNode) xmlTreeBuilder.stack.get(0);
 assertEquals("hello world", cdataNode.data());
 xmlTreeBuilder.process(tokenizer.nextToken());
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testProcessDoctype() {
 Tokenizer tokenizer = new Tokenizer(new StringReader("<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED> ]>"));
 assertEquals(Doctype.class, tokenizer.nextToken().getClass());
 xmlTreeBuilder.initialiseParse(tokenizer, null, new Parser(xmlTreeBuilder));
 assertEquals(1, xmlTreeBuilder.stack.size());
 DocumentType documentType = (DocumentType) xmlTreeBuilder.stack.get(0);
 assertEquals("root", documentType.getName());
 List<Attribute> attributes = documentType.getAttributes();
 assertEquals(1, attributes.size());
 Attribute attribute = attributes.get(0);
 assertEquals("attr", attribute.getName());
 assertEquals("CDATA", attribute.getValue());
 xmlTreeBuilder.process(tokenizer.nextToken());
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<root><element attr=\"value\"><child/><comment>Hello World!</comment></element></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, new Parser(xmlTreeBuilder));
 assertEquals(4, nodes.size());
 Element root = (Element) nodes.get(0);
 assertEquals("root", root.tagName());
 Element element = (Element) nodes.get(1);
 assertEquals("element", element.tagName());
 assertNull(element.childNodes().get(0).prevSibling());
 Comment comment = (Comment) nodes.get(2);
 assertEquals("Hello World!", comment.data());
 }

 @Test
 public void testParseFragmentWithContext() {
 String xmlFragment = "<root><element attr=\"value\"><child/><comment>Hello World!</comment></element></root>";
 Element context = new Element("context");
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, context, null, new Parser(xmlTreeBuilder));
 assertEquals(4, nodes.size());
 Element root = (Element) nodes.get(0);
 assertEquals("root", root.tagName());
 Element element = (Element) nodes.get(1);
 assertEquals("element", element.tagName());
 assertNull(element.childNodes().get(0).prevSibling());
 Comment comment = (Comment) nodes.get(2);
 assertEquals("Hello World!", comment.data());
 }
}
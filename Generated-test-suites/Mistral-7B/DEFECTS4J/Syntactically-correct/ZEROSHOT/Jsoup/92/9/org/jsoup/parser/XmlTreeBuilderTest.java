package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.AttributeTag;
import org.jsoup.parser.Token.CommentToken;
import org.jsoup.parser.Token.DoctypeToken;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.CharacterDataToken;
import org.jsoup.parser.Tokenizer;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 @Before
 public void setUp() {
 }

 @Test
 public void testParse() {
 String xmlString = "<root><element attr=\"value1\">Content1<subElement attr2=\"value2\"/></element></root>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString));
 assertNotNull(xmlDoc);
 assertEquals("root", xmlDoc.tagName());

 List<Node> nodes = xmlDoc.childNodes();
 assertEquals(1, nodes.size());
 Element rootElement = (Element) nodes.get(0);
 assertEquals("root", rootElement.tagName());

 List<Node> children = rootElement.childNodes();
 assertEquals(1, children.size());
 Element element = (Element) children.get(0);
 assertEquals("element", element.tagName());

 List<Attribute> attributes = element.attributes();
 assertEquals(1, attributes.size());
 Attribute attr = attributes.get(0);
 assertEquals("attr", attr.getKey());
 assertEquals("value1", attr.getValue());

 List<Node> subElementChildren = subElement.childNodes();
 assertEquals(0, subElementChildren.size());
 }

 @Test
 public void testParseWithBaseUri() {
 String xmlString = "<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><element attr=\"value1\">Content1<subElement attr2=\"value2\"/></element></root>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
 assertNotNull(xmlDoc);
 assertEquals("root", xmlDoc.tagName());

 List<Node> nodes = xmlDoc.childNodes();
 assertEquals(1, nodes.size());
 Element rootElement = (Element) nodes.get(0);
 assertEquals("root", rootElement.tagName());

 List<Attribute> attr1 = rootElement.attributes();
 assertEquals(2, attr1.size());
 Attribute xmlnsXsi = attr1.get(0);
 assertEquals("xmlns:xsi", xmlnsXsi.getKey());
 assertEquals("http://www.w3.org/2001/XMLSchema-instance", xmlnsXsi.getValue());

 Attribute xmlnsXsd = attr1.get(1);
 assertEquals("xmlns:xsd", xmlnsXsd.getKey());
 assertEquals("http://www.w3.org/2001/XMLSchema", xmlnsXsd.getValue());

 List<Node> children = rootElement.childNodes();
 assertEquals(1, children.size());
 Element element = (Element) children.get(0);
 assertEquals("element", element.tagName());

 List<Attribute> attributes = element.attributes();
 assertEquals(1, attributes.size());
 Attribute attr = attributes.get(0);
 assertEquals("attr", attr.getKey());
 assertEquals("value1", attr.getValue());

 List<Node> subElementChildren = ((Element) children.get(0)).childNodes();
 assertEquals(0, subElementChildren.size());
 }

 @Test
 public void testProcessStartTag() {
 Tokenizer tokenizer = new Tokenizer(new StringReader("<root><element attr=\"value1\">Content<subElement attr2=\"value2\"/></element></root>"));
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 XmlTreeBuilder.ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
 Parser parser = new Parser(xmlTreeBuilder, parseSettings);

 List<Token> tokens = new ArrayList<>();
 while (tokenizer.nextToken() != null) {
 tokens.add(tokenizer.previousToken());
 }

 for (Token token : tokens) {
 XmlTreeBuilder.Token xmlTreeBuilderToken = null;
 switch (token.type) {
 case StartTag:
 xmlTreeBuilderToken = token.asStartTag();
 assertEquals("<element", xmlTreeBuilderToken.asTag().getName());
 assertEquals("attr", xmlTreeBuilderToken.attributes().get(0).getName());
 assertEquals("value1", xmlTreeBuilderToken.attributes().get(0).getValue());
 assertNull(xmlTreeBuilderToken.isSelfClosing());
 break;
 case EndTag:
 // End tags do not have any attributes or data in XmlTreeBuilder.
 break;
 case Comment:
 // Comments are not processed in XmlTreeBuilder.
 break;
 case Character:
 // Characters are not processed in XmlTreeBuilder.
 break;
 case Doctype:
 // Doctype declarations are not processed in XmlTreeBuilder.
 break;
 case EOF:
 break;
 default:
 Validate.fail("Unexpected token type: " + token.type);
 }
 parser.parse(token, null);
 }
 }

 @Test
 public void testProcessEndTag() {
 Tokenizer tokenizer = new Tokenizer(new StringReader("<root><element><subElement/></element></root>"));
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 XmlTreeBuilder.ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
 Parser parser = new Parser(xmlTreeBuilder, parseSettings);

 List<Token> tokens = new ArrayList<>();
 while (tokenizer.nextToken() != null) {
 tokens.add(tokenizer.previousToken());
 }

 for (Token token : tokens) {
 XmlTreeBuilder.Token xmlTreeBuilderToken = null;
 switch (token.type) {
 case StartTag:
 xmlTreeBuilderToken = token.asStartTag();
 assertEquals("<element", xmlTreeBuilderToken.asTag().getName());
 assertEquals(1, xmlTreeBuilderToken.asTag().attributeSize());
 assertEquals("subElement", xmlTreeBuilderToken.asEndTag().getName());
 break;
 case EndTag:
 assertEquals("subElement", xmlTreeBuilderToken.getName());
 break;
 case Comment:
 // Comments are not processed in XmlTreeBuilder.
 break;
 case Character:
 // Characters are not processed in XmlTreeBuilder.
 break;
 case Doctype:
 // Doctype declarations are not processed in XmlTreeBuilder.
 break;
 case EOF:
 break;
 default:
 Validate.fail("Unexpected token type: " + token.type);
 }
 parser.parse(token, null);
 }
 }

 @Test
 public void testProcessCharacterData() {
 Tokenizer tokenizer = new Tokenizer(new StringReader("<root><element>Content</element></root>"));
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 XmlTreeBuilder.ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
 Parser parser = new Parser(xmlTreeBuilder, parseSettings);

 List<Token> tokens = new ArrayList<>();
 while (tokenizer.nextToken() != null) {
 tokens.add(tokenizer.previousToken());
 }

 for (Token token : tokens) {
 XmlTreeBuilder.Token xmlTreeBuilderToken = null;
 switch (token.type) {
 case StartTag:
 // Start tags do not have character data in XmlTreeBuilder.
 break;
 case EndTag:
 // End tags do not have character data in XmlTreeBuilder.
 break;
 case Comment:
 // Comments are not processed in XmlTreeBuilder.
 break;
 case Character:
 assertEquals("Content", ((XmlTreeBuilder.CharacterDataToken) token).getData());
 break;
 case Doctype:
 // Doctype declarations are not processed in XmlTreeBuilder.
 break;
 case EOF:
 break;
 default:
 Validate.fail("Unexpected token type: " + token.type);
 }
 parser.parse(token, null);
 }
 }

 @Test
 public void testProcessDoctype() {
 Tokenizer tokenizer = new Tokenizer(new StringReader("<!DOCTYPE root SYSTEM \"root.dtd\">"));
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 XmlTreeBuilder.ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
 Parser parser = new Parser(xmlTreeBuilder, parseSettings);

 List<Token> tokens = new ArrayList<>();
 while (tokenizer.nextToken() != null) {
 tokens.add(tokenizer.previousToken());
 }

 for (Token token : tokens) {
 XmlTreeBuilder.Token xmlTreeBuilderToken = null;
 switch (token.type) {
 case StartTag:
 // Start tags do not have doctype declarations in XmlTreeBuilder.
 break;
 case EndTag:
 // End tags do not have doctype declarations in XmlTreeBuilder.
 break;
 case Comment:
 // Comments are not processed in XmlTreeBuilder.
 break;
 case Character:
 // Characters are not processed in XmlTreeBuilder.
 break;
 case Doctype:
 assertEquals("root", ((XmlTreeBuilder.DoctypeToken) token).getName());
 assertEquals("root.dtd", ((XmlTreeBuilder.DoctypeToken) token).getSystemIdentifier());
 break;
 case EOF:
 break;
 default:
 Validate.fail("Unexpected token type: " + token.type);
 }
 parser.parse(token, null);
 }
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<element attr=\"value1\">Content<subElement attr2=\"value2\"/></element>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document xmlDoc = xmlTreeBuilder.parseFragment(xmlFragment, null, null);
 assertNotNull(xmlDoc);

 List<Node> nodes = xmlDoc.childNodes();
 assertEquals(1, nodes.size());
 Element element = (Element) nodes.get(0);
 assertEquals("element", element.tagName());

 List<Attribute> attributes = element.attributes();
 assertEquals(1, attributes.size());
 Attribute attr = attributes.get(0);
 assertEquals("attr", attr.getKey());
 assertEquals("value1", attr.getValue());

 List<Node> subElementChildren = ((Element) nodes.get(0)).childNodes();
 assertEquals(1, subElementChildren.size());
 Element subElement = (Element) subElementChildren.get(0);
 assertEquals("subElement", subElement.tagName());

 List<Attribute> subElementAttributes = subElement.attributes();
 assertEquals(1, subElementAttributes.size());
 Attribute subElementAttr = subElementAttributes.get(0);
 assertEquals("attr2", subElementAttr.getKey());
 assertEquals("value2", subElementAttr.getValue());
 }

 @Test
 public void testParseFragmentWithBaseUri() {
 String xmlFragment = "<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><element attr=\"value1\">Content<subElement attr2=\"value2\"/></element></root>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document xmlDoc = xmlTreeBuilder.parseFragment(xmlFragment, null, "http://example.com");
 assertNotNull(xmlDoc);
 assertEquals("root", xmlDoc.tagName());

 List<Node> nodes = xmlDoc.childNodes();
 assertEquals(1, nodes.size());
 Element rootElement = (Element) nodes.get(0);
 assertEquals("root", rootElement.tagName());

 List<Attribute> attr1 = rootElement.attributes();
 assertEquals(2, attr1.size());
 Attribute xmlnsXsi = attr1.get(0);
 assertEquals("xmlns:xsi", xmlnsXsi.getKey());
 assertEquals("http://www.w3.org/2001/XMLSchema-instance", xmlnsXsi.getValue());

 Attribute xmlnsXsd = attr1.get(1);
 assertEquals("xmlns:xsd", xmlnsXsd.getKey());
 assertEquals("http://www.w3.org/2001/XMLSchema", xmlnsXsd.getValue());

 List<Node> children = rootElement.childNodes();
 assertEquals(1, children.size());
 Element element = (Element) children.get(0);
 assertEquals("element", element.tagName());

 List<Attribute> attributes = element.attributes();
 assertEquals(1, attributes.size());
 Attribute attr = attributes.get(0);
 assertEquals("attr", attr.getKey());
 assertEquals("value1", attr.getValue());

 List<Node> subElementChildren = ((Element) children.get(0)).childNodes();
 assertEquals(0, subElementChildren.size());
 }
}
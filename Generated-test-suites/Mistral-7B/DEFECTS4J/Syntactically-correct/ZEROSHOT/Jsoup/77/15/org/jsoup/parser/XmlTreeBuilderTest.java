package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.List;

/**
 * Test class for XmlTreeBuilder
 */
public class XmlTreeBuilderTest {

 @Test
 public void testParse() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root><child attr=\"value\"><grandChild/></child></root>";
 String baseUri = "file:/test.xml"; // for testing baseUri

 // Test parsing a string
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), baseUri);
 Element rootElement = xmlDoc.rootElement();
 assertEquals("root", rootElement.tagName());
 assertEquals(1, rootElement.childNodeSize());

 // Test parsing a reader
 Reader xmlInput = new StringReader(xmlString);
 Document xmlDocFromReader = xmlTreeBuilder.parse(xmlInput, baseUri);
 Element rootElementFromReader = xmlDocFromReader.rootElement();
 assertEquals("root", rootElementFromReader.tagName());
 assertEquals(1, rootElementFromReader.childNodeSize());
 }

 @Test
 public void testParseFragment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlFragment = "<child attr=\"value\"><grandChild/></child>";
 String baseUri = "file:/test.xml"; // for testing baseUri

 // Test parsing a fragment
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(1, nodes.size());
 Element childElement = (Element) nodes.get(0);
 assertEquals("child", childElement.tagName());
 assertEquals(1, childElement.childNodeSize());
 }

 @Test
 public void testProcessTokenStartTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root><child attr=\"value\"><grandChild/></child></root>";
 String baseUri = "file:/test.xml"; // for testing baseUri

 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), baseUri);
 Element rootElement = xmlDoc.rootElement();
 assertEquals("root", rootElement.tagName());

 Element childElement = rootElement.child(0);
 assertEquals("child", childElement.tagName());
 assertEquals("value", childElement.attr("attr"));

 // Test self-closing tag
 String xmlStringSelfClosing = "<root><selfClosingTag/></root>";
 Document xmlDocSelfClosing = xmlTreeBuilder.parse(new StringReader(xmlStringSelfClosing), baseUri);
 Element rootElementSelfClosing = xmlDocSelfClosing.rootElement();
 assertEquals("root", rootElementSelfClosing.tagName());
 assertEquals(1, rootElementSelfClosing.childNodeSize());
 Element selfClosingTag = rootElementSelfClosing.child(0);
 assertTrue(selfClosingTag.isSelfClosing());
 }

 @Test
 public void testProcessTokenEndTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root><child><grandChild/></child></root>";
 String baseUri = "file:/test.xml"; // for testing baseUri

 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), baseUri);
 Element rootElement = xmlDoc.rootElement();
 Element childElement = rootElement.child(0);
 assertEquals("child", childElement.tagName());
 assertEquals(1, childElement.childNodeSize());

 // Test popStackToClose
 String xmlStringWithNestedTags = "<root><child><grandChild><greatGrandChild/></greatGrandChild></grandChild></child></root>";
 Document xmlDocWithNestedTags = xmlTreeBuilder.parse(new StringReader(xmlStringWithNestedTags), baseUri);
 Element rootElementWithNestedTags = xmlDocWithNestedTags.rootElement();
 Element childElementWithNestedTags = rootElementWithNestedTags.child(0);
 Element grandChildElement = childElementWithNestedTags.child(0);
 Element greatGrandChildElement = grandChildElement.child(0);

 // Test closing 'grandChild' tag
 xmlTreeBuilder.popStackToClose(new Token.EndTag("grandChild"));
 assertEquals(1, childElementWithNestedTags.childNodeSize());

 // Test closing 'child' tag
 xmlTreeBuilder.popStackToClose(new Token.EndTag("child"));
 assertEquals(0, rootElementWithNestedTags.childNodeSize());
 }

 @Test
 public void testProcessTokenComment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root><!-- comment --> <child></child></root>";
 String baseUri = "file:/test.xml"; // for testing baseUri

 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), baseUri);
 Element rootElement = xmlDoc.rootElement();
 assertEquals("root", rootElement.tagName());
 assertEquals(1, rootElement.childNodeSize());

 Element childElement = rootElement.child(0);
 assertEquals("child", childElement.tagName());
 assertEquals(0, childElement.childNodeSize());

 // Test comment with bogus xml declaration
 String xmlStringWithBogusDeclaration = "<root><!--!DOCTYPE root SYSTEM \"file:/test.xml\" [<!ATTLIST child attr CDATA #REQUIRED]--><child></child></root>";
 Document xmlDocWithBogusDeclaration = xmlTreeBuilder.parse(new StringReader(xmlStringWithBogusDeclaration), baseUri);
 Element rootElementWithBogusDeclaration = xmlDocWithBogusDeclaration.rootElement();
 assertEquals("root", rootElementWithBogusDeclaration.tagName());
 assertEquals(1, rootElementWithBogusDeclaration.childNodeSize());

 Element childElementWithBogusDeclaration = rootElementWithBogusDeclaration.child(0);
 assertEquals("child", childElementWithBogusDeclaration.tagName());
 assertEquals(0, childElementWithBogusDeclaration.childNodeSize());

 XmlDeclaration xmlDeclaration = (XmlDeclaration) rootElementWithBogusDeclaration.child(0);
 assertEquals("root", xmlDeclaration.tagName());
 assertEquals("file:/test.xml", xmlDeclaration.getPublicIdentifier());
 assertEquals(null, xmlDeclaration.getSystemIdentifier());
 assertTrue(xmlDeclaration.isStandalone());
 }

 @Test
 public void testProcessTokenCharacter() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root>&amp;<child>&lt;<grandChild>&gt;</grandChild></child></root>";
 String baseUri = "file:/test.xml"; // for testing baseUri

 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), baseUri);
 Element rootElement = xmlDoc.rootElement();
 assertEquals("root", rootElement.tagName());
 assertEquals(1, rootElement.childNodeSize());

 Element childElement = rootElement.child(0);
 assertEquals("child", childElement.tagName());
 assertEquals(1, childElement.childNodeSize());

 TextNode ampersand = (TextNode) childElement.child(0);
 assertEquals("&amp;", ampersand.text());

 Element grandChildElement = childElement.child(1);
 assertEquals("grandChild", grandChildElement.tagName());
 assertEquals(1, grandChildElement.childNodeSize());

 TextNode lessThan = (TextNode) grandChildElement.child(0);
 assertEquals("&lt;", lessThan.text());

 TextNode greaterThan = (TextNode) grandChildElement.child(1);
 assertEquals("&gt;", greaterThan.text());
 }

 @Test
 public void testProcessTokenDoctype() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root><!DOCTYPE root SYSTEM \"file:/test.xml\" [<!ATTLIST child attr CDATA #REQUIRED]>\n<child/></root>";
 String baseUri = "file:/test.xml"; // for testing baseUri

 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), baseUri);
 Element rootElement = xmlDoc.rootElement();
 assertEquals("root", rootElement.tagName());
 assertEquals(1, rootElement.childNodeSize());

 Element childElement = rootElement.child(0);
 assertEquals("child", childElement.tagName());
 assertEquals(0, childElement.childNodeSize());

 DocumentType doctypeNode = (DocumentType) rootElement.child(1);
 assertEquals("root", doctypeNode.tagName());
 assertEquals("file:/test.xml", doctypeNode.getPublicIdentifier());
 assertEquals("file:/test.xml", doctypeNode.getSystemIdentifier());
 assertNull(doctypeNode.getPubSysKey());
 }
}
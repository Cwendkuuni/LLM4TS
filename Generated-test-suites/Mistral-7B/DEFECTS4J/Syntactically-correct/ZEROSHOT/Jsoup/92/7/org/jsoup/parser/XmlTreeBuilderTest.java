package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class XmlTreeBuilderTest {

 @Test
 void testInitialiseParse() {
 // Given
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Reader input = new StringReader("<xml/>");
 String baseUri = "http://test.com";
 Parser parser = new Parser(xmlTreeBuilder);

 // When
 xmlTreeBuilder.initialiseParse(input, baseUri, parser);

 // Then
 Assertions.assertNotNull(xmlTreeBuilder.doc);
 assertEquals(baseUri, xmlTreeBuilder.doc.baseUri());
 }

 @Test
 void testParse() {
 // Given
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<xml><tag></tag></xml>";
 String baseUri = "http://test.com";

 // When
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), baseUri);

 // Then
 Assertions.assertNotNull(doc);
 Assertions.assertEquals(1, doc.childNodes().size());
 }

 @Test
 void testParseWithBaseUri() {
 // Given
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<xml><tag href=\"test.html\"></tag></xml>";
 String baseUri = "http://test.com";

 // When
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), baseUri);

 // Then
 Assertions.assertNotNull(doc);
 Assertions.assertEquals("test.html", doc.select("tag").attr("href"));
 }

 @Test
 void testProcess() {
 // Given
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<xml><tag></tag></xml>";
 String baseUri = "http://test.com";
 Parser parser = new Parser(xmlTreeBuilder);

 // When
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), baseUri);

 // Then
 Executable processMethod = () -> xmlTreeBuilder.runParser();
 processMethod.execute();

 // And then
 List<Node> nodes = doc.childNodes();
 assertEquals(1, nodes.size());
 }

 @Test
 void testInsertNode() {
 // Given
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<xml><tag></tag></xml>";
 String baseUri = "http://test.com";
 Parser parser = new Parser(xmlTreeBuilder);
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), baseUri);

 // When
 Element tagElement = new Element("tag");
 xmlTreeBuilder.insertNode(tagElement);

 // Then
 List<Node> nodes = doc.childNodes();
 assertEquals(2, nodes.size());
 }

 @Test
 void testInsertComment() {
 // Given
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<xml><!--test--></xml>";
 String baseUri = "http://test.com";
 Parser parser = new Parser(xmlTreeBuilder);
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), baseUri);

 // When
 xmlTreeBuilder.insert(new Token.Comment("test"));

 // Then
 List<Node> nodes = doc.childNodes();
 assertEquals(1, nodes.size());
 }

 @Test
 void testInsertCData() {
 // Given
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<xml><![CDATA[test]]></xml>";
 String baseUri = "http://test.com";
 Parser parser = new Parser(xmlTreeBuilder);
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), baseUri);

 // When
 xmlTreeBuilder.insert(new Token.Character("![CDATA[test]]"));

 // Then
 List<Node> nodes = doc.childNodes();
 assertEquals(1, nodes.size());
 }

 @Test
 void testInsertTextNode() {
 // Given
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<xml><tag>test</tag></xml>";
 String baseUri = "http://test.com";
 Parser parser = new Parser(xmlTreeBuilder);
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), baseUri);

 // When
 xmlTreeBuilder.insert(new Token.Character("test"));

 // Then
 List<Node> nodes = doc.childNodes();
 assertEquals(1, nodes.size());
 assertEquals("test", nodes.get(0).text());
 }

 @Test
 void testInsertElement() {
 // Given
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<xml></xml>";
 String baseUri = "http://test.com";
 Parser parser = new Parser(xmlTreeBuilder);
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), baseUri);

 // When
 Element tagElement = new Element("tag");
 xmlTreeBuilder.insert(new Token.StartTag("tag"));
 xmlTreeBuilder.insert(tagElement);
 xmlTreeBuilder.insert(new Token.EndTag("tag"));

 // Then
 List<Node> nodes = doc.childNodes();
 assertEquals(1, nodes.size());
 }

 @Test
 void testInsertDocumentType() {
 // Given
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<!DOCTYPE xml SYSTEM \"test.dtd\">";
 String baseUri = "http://test.com";
 Parser parser = new Parser(xmlTreeBuilder);
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), baseUri);

 // When
 xmlTreeBuilder.insert(new Token.Doctype(new DocumentType("xml", null, null)));

 // Then
 List<Node> nodes = doc.childNodes();
 assertEquals(1, nodes.size());
 }

 @Test
 void testPopStackToClose() {
 // Given
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<xml><tag></tag></xml>";
 String baseUri = "http://test.com";
 Parser parser = new Parser(xmlTreeBuilder);
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), baseUri);

 // When
 xmlTreeBuilder.pushElement(new Element("tag"));
 xmlTreeBuilder.popStackToClose(new Token.EndTag("tag"));

 // Then
 List<Node> nodes = doc.childNodes();
 assertEquals(1, nodes.size());
 }

 @Test
 void testParseFragment() {
 // Given
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlFragment = "<tag/>";
 String baseUri = "http://test.com";
 Parser parser = new Parser(xmlTreeBuilder);

 // When
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, baseUri, parser);

 // Then
 assertEquals(1, nodes.size());
 }

 @Test
 void testParseFragmentWithContext() {
 // Given
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlFragment = "<tag/>";
 String baseUri = "http://test.com";
 Parser parser = new Parser(xmlTreeBuilder);
 Element context = new Element("context");

 // When
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, context, baseUri, parser);

 // Then
 assertEquals(1, nodes.size());
 }

 @Test
 void testUnexpectedTokenType() {
 // Given
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<xml>";
 String baseUri = "http://test.com";
 Parser parser = new Parser(xmlTreeBuilder);

 // When & Then
 Assertions.assertThrows(RuntimeException.class, () -> xmlTreeBuilder.parse(new StringReader(xmlString), baseUri));
 }
}
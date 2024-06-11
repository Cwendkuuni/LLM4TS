package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document expectedDocument;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 expectedDocument = Jsoup.parse("<root><element attr='value'><subElement attr2='value2' /></subElement></element></root>", "baseUrl");
 }

 @Test
 public void testParse() {
 Document actualDocument = xmlTreeBuilder.parse("<root><element attr='value'><subElement attr2='value2' /></subElement></element></root>", "baseUrl");
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testParseFragment() {
 List<Node> fragments = xmlTreeBuilder.parseFragment("<root><element attr='value'><subElement attr2='value2' /></subElement></element></root>", "baseUrl", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(expectedDocument.childNodes(), fragments);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 xmlTreeBuilder.parse(null, "baseUrl");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 xmlTreeBuilder.parse("<root/>", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyInput() {
 xmlTreeBuilder.parse("", "baseUrl");
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token(Token.TokenType.StartTag, "<element attr='value'>");
 Element element = xmlTreeBuilder.insert(startTagToken);
 assertEquals("element", element.nodeName());
 assertEquals("value", element.attribute("attr"));
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token(Token.TokenType.EndTag, "</element>");
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertTrue(xmlTreeBuilder.stack.isEmpty());
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token(Token.TokenType.Comment, "-- This is a comment -->");
 xmlTreeBuilder.insert(commentToken);
 assertEquals("-- This is a comment -->", xmlTreeBuilder.doc.childNodes().get(0).nodeValue());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token(Token.TokenType.Character, 'A');
 xmlTreeBuilder.insert(characterToken);
 Element textNode = (Element) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("A", textNode.childNodes().get(0).nodeValue());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token(Token.TokenType.Doctype, "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED> ]>");
 DocumentType documentType = (DocumentType) xmlTreeBuilder.insert(doctypeToken);
 assertEquals("root", documentType.nodeName());
 assertEquals("CDATA", documentType.getPublicIdentifier().schemaType());
 }
}
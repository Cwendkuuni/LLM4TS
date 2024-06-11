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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class XmlTreeBuilderTest {

 @Mock
 private Parser parser;

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = xmlTreeBuilder.parse(new StringReader(""), "", parser);
 }

 @Test
 public void testInitialiseParse() {
 // Check if document is placed on the stack
 assertEquals(document, xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testParse() {
 // Check if parse method returns correct document
 Document parsedDocument = xmlTreeBuilder.parse(new StringReader("<root><child></child></root>"), "");
 assertEquals(parsedDocument, document);
 }

 @Test
 public void testProcessStartTag() {
 // Create a mock start tag
 Token startTag = new Token.StartTag("root", null, null, null, null, null, null);

 // Call insert method with the mock start tag
 xmlTreeBuilder.insert(startTag);

 // Check if new element is added to the document
 Element rootElement = document.elementSiblings().get(0);
 assertEquals("root", rootElement.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 // Create a mock end tag
 Token endTag = new Token.EndTag("root");

 // Call popStackToClose method with the mock end tag
 xmlTreeBuilder.popStackToClose(endTag);

 // Check if the stack is empty
 assertTrue(xmlTreeBuilder.stack.isEmpty());
 }

 @Test
 public void testProcessComment() {
 // Create a mock comment
 Token comment = new Token.Comment("<!-- This is a comment -->");

 // Call insert method with the mock comment
 xmlTreeBuilder.insert(comment);

 // Check if new comment node is added to the document
 Comment commentNode = document.childNodes().get(1);
 assertEquals("This is a comment", commentNode.text());
 }

 @Test
 public void testProcessCharacter() {
 // Create a mock character
 Token character = new Token.Character('a');

 // Call insert method with the mock character
 xmlTreeBuilder.insert(character);

 // Check if new text node is added to the document
 TextNode textNode = document.childNodes().get(1);
 assertEquals("a", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 // Create a mock doctype
 Token doctype = new Token.Doctype("html", null, null);

 // Call insert method with the mock doctype
 xmlTreeBuilder.insert(doctype);

 // Check if new document type node is added to the document
 DocumentType documentType = document.childNodes().get(0);
 assertEquals("html", documentType.nodeName());
 }

 @Test
 public void testParseFragment() {
 // Create input fragment
 String inputFragment = "<root><child></child></root>";

 // Call parseFragment method
 List<Node> parsedFragment = xmlTreeBuilder.parseFragment(inputFragment, "", parser);

 // Check if the parsed fragment contains the expected nodes
 assertEquals(4, parsedFragment.size());
 Node rootNode = parsedFragment.get(0);
 assertEquals("root", rootNode.nodeName());
 Node childNode = rootNode.childNodes().get(0);
 assertEquals("child", childNode.nodeName());
 }

 @Test
 public void testParseFragmentWithContext() {
 // Create input fragment
 String inputFragment = "<root><child></child></root>";

 // Create a mock context element
 Element context = new Element("parent");

 // Call parseFragment method
 List<Node> parsedFragment = xmlTreeBuilder.parseFragment(inputFragment, context, "", parser);

 // Check if the parsed fragment contains the expected nodes
 assertEquals(4, parsedFragment.size());
 Node rootNode = parsedFragment.get(0);
 assertEquals("root", rootNode.nodeName());
 Node childNode = rootNode.childNodes().get(0);
 assertEquals("child", childNode.nodeName());

 // Check if the context element is a parent of the root element
 assertTrue(context.hasChild(rootNode));
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 // Create a mock token with unexpected type
 Token unexpectedToken = new Token(123, "test");

 // Call process method with the mock token
 xmlTreeBuilder.process(unexpectedToken);
 }
}
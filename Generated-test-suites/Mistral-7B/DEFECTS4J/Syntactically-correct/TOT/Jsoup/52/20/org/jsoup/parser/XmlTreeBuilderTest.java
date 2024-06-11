package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document xmlDoc;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlDoc = Jsoup.parse("<root><child></child></root>", null, Parser.xmlParser());
 }

 @Test
 public void testProcessStartTag() {
 // Test valid start tag
 Token startTagToken = new Token.StartTag("child", null, null);
 Node node = xmlTreeBuilder.insert(startTagToken);
 assertEquals("child", node.getNodeName());
 assertNotNull(node.getParentNode());
 }

 @Test
 public void testProcessEndTag() {
 // Test valid end tag
 Token endTagToken = new Token.EndTag("child");
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertNull(xmlDoc.getElementsByTagName("child").item(0).getParentNode());
 }

 @Test
 public void testProcessComment() {
 // Test valid comment
 Token commentToken = new Token.Comment("This is a comment");
 xmlTreeBuilder.insert(commentToken);
 Node commentNode = xmlDoc.getElementsByTagName("comment").item(0);
 assertEquals("This is a comment", commentNode.getTextContent());
 }

 @Test
 public void testProcessCharacter() {
 // Test valid character
 Token characterToken = new Token.Character('A');
 xmlTreeBuilder.insert(characterToken);
 Node characterNode = xmlDoc.getElementsByTagName("text").item(0);
 assertEquals("A", characterNode.getTextContent());
 }

 @Test
 public void testProcessDoctype() {
 // Test doctype
 Token doctypeToken = new Token.Doctype("html", null, null, null);
 xmlTreeBuilder.insert(doctypeToken);
 DocumentType doctypeNode = (DocumentType) xmlDoc.getDocumentElement().getFirstChild();
 assertEquals("html", doctypeNode.getNodeName());
 assertNull(doctypeNode.getPublicIdentifier());
 assertNull(doctypeNode.getSystemIdentifier());
 }

 @Test
 public void testProcessSelfClosingTag() {
 // Test self-closing tag
 Token selfClosingTagToken = new Token.StartTag("img", null, null, true);
 xmlTreeBuilder.insert(selfClosingTagToken);
 Element imgElement = (Element) xmlDoc.getElementsByTagName("img").item(0);
 assertEquals("img", imgElement.getNodeName());
 assertNotNull(imgElement.getParentNode());
 assertNull(imgElement.getFirstChild());
 }

 @Test
 public void testProcessUnexpectedTokenType() {
 // Test unexpected token type
 Token unexpectedToken = new Token(123);
 xmlTreeBuilder.process(unexpectedToken); // this should cause an error
 }

 @Test
 public void testParseFragment() {
 // Test parsing a fragment of XML
 String inputFragment = "<root><child><grandchild/></child></root>";
 List<Node> parsedNodes = xmlTreeBuilder.parseFragment(inputFragment, null, null);
 Node rootNode = parsedNodes.get(0);
 assertEquals("root", rootNode.getNodeName());
 assertNotNull(rootNode.getFirstChild());
 Node childNode = rootNode.getFirstChild();
 assertEquals("child", childNode.getNodeName());
 assertNotNull(childNode.getFirstChild());
 Node grandchildNode = childNode.getFirstChild();
 assertEquals("grandchild", grandchildNode.getNodeName());
 assertNull(grandchildNode.getFirstChild());
 }
}
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlDeclaration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.StringReader;
import java.util.List;

class XmlTreeBuilderTest {

 @Test
 void testInitialiseParse() {
 // Arrange
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 StringReader input = new StringReader("<root><child/></root>");
 String baseUri = "http://example.com";

 // Act
 xmlTreeBuilder.initialiseParse(input, baseUri, new Parser(xmlTreeBuilder));

 // Assert
 Assertions.assertNotNull(xmlTreeBuilder.doc, "Document should not be null.");
 Assertions.assertEquals("root", xmlTreeBuilder.doc.tagName(), "Document tag name should be 'root'.");
 }

 @Test
 void testParseDocument() {
 // Arrange
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<root><child/></root>";
 String baseUri = "http://example.com";

 // Act
 Document document = xmlTreeBuilder.parse(new StringReader(input), baseUri);

 // Assert
 Assertions.assertNotNull(document, "Document should not be null.");
 Assertions.assertEquals("root", document.tagName(), "Document tag name should be 'root'.");
 List<Node> childNodes = document.childNodes();
 Assertions.assertEquals(1, childNodes.size(), "Document should have one child node.");
 Assertions.assertTrue(childNodes.get(0) instanceof Element, "Document's first child node should be an Element.");
 }

 @Test
 void testProcessStartTag() {
 // Arrange
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Token startTagToken = new Token.StartTag("child");

 // Act
 Element childElement = xmlTreeBuilder.insert(startTagToken);

 // Assert
 Assertions.assertNotNull(childElement, "Child element should not be null.");
 Assertions.assertEquals("child", childElement.tagName(), "Child element tag name should be 'child'.");
 }

 @Test
 void testProcessEndTag() {
 // Arrange
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Element childElement = new Element("child");
 Element parentElement = new Element("root");
 parentElement.appendChild(childElement);
 xmlTreeBuilder.stack.add(parentElement);
 Token endTagToken = new Token.EndTag("child");

 // Act
 xmlTreeBuilder.popStackToClose(endTagToken);

 // Assert
 Assertions.assertEquals(0, xmlTreeBuilder.stack.size(), "Stack size should be 0 after popping end tag.");
 }

 @Test
 void testProcessComment() {
 // Arrange
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String commentData = "This is a comment.";
 Token commentToken = new Token.Comment(commentData);

 // Act
 xmlTreeBuilder.insert(commentToken);

 // Assert
 Assertions.assertTrue(xmlTreeBuilder.doc.childNodes().get(0) instanceof Comment, "First child node should be a Comment.");
 Assertions.assertEquals(commentData, ((Comment) xmlTreeBuilder.doc.childNodes().get(0)).data(), "Comment data should match.");
 }

 @Test
 void testProcessCharacter() {
 // Arrange
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String characterData = "Hello World!";
 Token characterToken = new Token.Character(characterData);

 // Act
 xmlTreeBuilder.insert(characterToken);

 // Assert
 Assertions.assertTrue(xmlTreeBuilder.doc.childNodes().get(0) instanceof TextNode, "First child node should be a TextNode.");
 Assertions.assertEquals(characterData, ((TextNode) xmlTreeBuilder.doc.childNodes().get(0)).text(), "Character data should match.");
 }

 @Test
 void testProcessDoctype() {
 // Arrange
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String doctypeName = "html";
 String publicId = "public id";
 String systemId = "system id";
 Token doctypeToken = new Token.Doctype(doctypeName, publicId, systemId);

 // Act
 xmlTreeBuilder.insert(doctypeToken);

 // Assert
 Assertions.assertTrue(xmlTreeBuilder.doc.childNodes().get(0) instanceof DocumentType, "First child node should be a DocumentType.");
 Assertions.assertEquals(doctypeName, ((DocumentType) xmlTreeBuilder.doc.childNodes().get(0)).tagName(), "Doctype tag name should match.");
 Assertions.assertEquals(publicId, ((DocumentType) xmlTreeBuilder.doc.childNodes().get(0)).publicId(), "Doctype public id should match.");
 Assertions.assertEquals(systemId, ((DocumentType) xmlTreeBuilder.doc.childNodes().get(0)).systemId(), "Doctype system id should match.");
 }

 @Test
 void testParseFragment() {
 // Arrange
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String inputFragment = "<child></child>";
 String baseUri = "http://example.com";

 // Act
 List<Node> nodeList = xmlTreeBuilder.parseFragment(inputFragment, null, baseUri, new Parser(xmlTreeBuilder));

 // Assert
 Assertions.assertNotNull(nodeList, "Node list should not be null.");
 Assertions.assertEquals(1, nodeList.size(), "Node list should contain one node.");
 Assertions.assertTrue(nodeList.get(0) instanceof Element, "Node list's first node should be an Element.");
 }

 @Test
 void testParseFragmentWithContext() {
 // Arrange
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String inputFragment = "<child></child>";
 Element context = new Element("root");
 String baseUri = "http://example.com";

 // Act
 List<Node> nodeList = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, new Parser(xmlTreeBuilder));

 // Assert
 Assertions.assertNotNull(nodeList, "Node list should not be null.");
 Assertions.assertEquals(1, nodeList.size(), "Node list should contain one node.");
 Assertions.assertTrue(nodeList.get(0) instanceof Element, "Node list's first node should be an Element.");
 Assertions.assertEquals(context, nodeList.get(0).parent(), "Node's parent should be the context Element.");
 }

 @Test
 void testUnexpectedTokenType() {
 // Arrange
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 // Act and Assert
 Assertions.assertThrows(IllegalArgumentException.class, () -> xmlTreeBuilder.process(new Token(-1, "Unexpected token type")));
 }
}
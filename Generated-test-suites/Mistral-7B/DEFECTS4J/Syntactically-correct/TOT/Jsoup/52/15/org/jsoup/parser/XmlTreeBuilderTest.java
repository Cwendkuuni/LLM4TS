import org.jsoup.Jsoup;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class XmlTreeBuilderTest {

 @Test
 void testParseFragment() {
 String xmlFragment = "<root><child tag='test'/><anotherChild tag='test'/></root>";
 Document parsedXml = XmlTreeBuilder.parseFragment(xmlFragment, null, new ParseErrorList());

 assertNotNull(parsedXml);
 Node root = parsedXml.getDocumentElement();
 assertNotNull(root);
 assertEquals("root", root.getNodeName());

 NodeList childNodes = root.getChildNodes();
 assertNotNull(childNodes);
 assertEquals(2, childNodes.getLength());

 Node firstChild = childNodes.item(0);
 assertNotNull(firstChild);
 assertEquals("child", firstChild.getNodeName());
 assertNull(firstChild.getAttributes().getItem(0));

 Node secondChild = childNodes.item(1);
 assertNotNull(secondChild);
 assertEquals("anotherChild", secondChild.getNodeName());
 assertNotNull(secondChild.getAttributes().getItem(0));
 assertEquals("test", secondChild.getAttributes().getNamedItem("tag").getNodeValue());
 }

 @Test
 void testParseFragmentWithEmptyFragment() {
 String xmlFragment = "";
 Document parsedXml = XmlTreeBuilder.parseFragment(xmlFragment, null, new ParseErrorList());
 assertNull(parsedXml);
 }

 @Test
 void testParseFragmentWithInvalidXml() {
 String xmlFragment = "<root><child tag='test'>";
 assertThrows(IllegalArgumentException.class, () -> XmlTreeBuilder.parseFragment(xmlFragment, null, new ParseErrorList()));
 }

 @Test
 void testParseFragmentWithInvalidUri() {
 String xmlFragment = "<root xmlns:x='xs:/'><child tag='test'/></root>";
 assertThrows(IllegalArgumentException.class, () -> XmlTreeBuilder.parseFragment(xmlFragment, null, new ParseErrorList()));
 }

 @Test
 void testParseFragmentWithEmptyStack() {
 String xmlFragment = "<root></root>";
 Document parsedXml = XmlTreeBuilder.parseFragment(xmlFragment, null, new ParseErrorList());
 Node root = parsedXml.getDocumentElement();
 assertNotNull(root);
 assertEquals("root", root.getNodeName());
 assertNull(root.getPreviousSibling());
 assertNull(root.getNextSibling());
 assertNull(root.getParentNode());
 }

 @Test
 void testParseFragmentWithSelfClosingTag() {
 String xmlFragment = "<img src='image.jpg'/>";
 Document parsedXml = XmlTreeBuilder.parseFragment(xmlFragment, null, new ParseErrorList());
 Node img = parsedXml.getDocumentElement();
 assertNotNull(img);
 assertEquals("img", img.getNodeName());
 assertFalse(img.hasAttributes());
 }

 @Test
 void testParseFragmentWithCommentAndXmlDeclaration() {
 String xmlFragment = "<!-- This is a comment --><!DOCTYPE html SYSTEM 'about:legacy-compat'>\n<root><child tag='test'/></root>";
 Document parsedXml = XmlTreeBuilder.parseFragment(xmlFragment, null, new ParseErrorList());
 assertNotNull(parsedXml);
 Node root = parsedXml.getDocumentElement();
 assertNotNull(root);
 assertEquals("root", root.getNodeName());
 NodeList commentNodes = root.getChildNodes();
 assertNotNull(commentNodes);
 assertEquals(1, commentNodes.getLength());
 Node commentNode = commentNodes.item(0);
 assertNotNull(commentNode);
 assertEquals("!-- This is a comment --", commentNode.getNodeValue());
 }
}
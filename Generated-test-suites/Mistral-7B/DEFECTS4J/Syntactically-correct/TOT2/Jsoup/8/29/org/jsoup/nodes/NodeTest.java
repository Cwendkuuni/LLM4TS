package org.jsoup.nodes;

import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Assert.*;

public class NodeTest {

 // Test for constructor with baseUri and attributes
 @Test
 public void testNodeConstructorWithBaseUriAndAttributes() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("id", "testNode");
 Node node = new Element("div", baseUri, attributes);
 assertEquals("div", node.nodeName());
 assertEquals(baseUri, node.baseUri());
 assertEquals(attributes, node.attributes());
 }

 // Test for constructor with baseUri only
 @Test
 public void testNodeConstructorWithBaseUriOnly() {
 String baseUri = "http://example.com";
 Node node = new Element("div", baseUri);
 assertEquals("div", node.nodeName());
 assertEquals(baseUri, node.baseUri());
 assertTrue(node.childNodes().isEmpty());
 }

 // Test for default constructor
 @Test
 public void testNodeDefaultConstructor() {
 Node node = new Element();
 assertTrue(node.childNodes().isEmpty());
 assertNull(node.attributes());
 }

 // Test for getNodeName method
 @Test
 public void testgetNodeName() {
 String nodeName = "div";
 Node node = new Element(nodeName);
 assertEquals(nodeName, node.nodeName());
 }

 // Test for getAttribute method
 @Test
 public void testGetAttribute() {
 String attributeKey = "id";
 String attributeValue = "testAttribute";
 Node node = new Element("div");
 node.attr(attributeKey, attributeValue);
 assertEquals(attributeValue, node.attr(attributeKey));
 }

 // Test for getAllAttributes method
 @Test
 public void testGetAllAttributes() {
 String attributeKey1 = "id";
 String attributeValue1 = "testAttribute1";
 String attributeKey2 = "class";
 String attributeValue2 = "testClass";
 Node node = new Element("div");
 node.attr(attributeKey1, attributeValue1);
 node.attr(attributeKey2, attributeValue2);
 Attributes attributes = node.attributes();
 assertEquals(attributeValue1, attributes.get(attributeKey1));
 assertEquals(attributeValue2, attributes.get(attributeKey2));
 }

 // Test for setting and getting an attribute
 @Test
 public void testSettingAndGettingAnAttribute() {
 String attributeKey = "id";
 String attributeValue = "testAttribute";
 Node node = new Element("div");
 node.attr(attributeKey, attributeValue);
 assertEquals(attributeValue, node.attr(attributeKey));
 }

 // Test for checking if an attribute exists
 @Test
 public void testCheckingIfAnAttributeExists() {
 String attributeKey = "id";
 String attributeValue = "testAttribute";
 Node node = new Element("div");
 node.attr(attributeKey, attributeValue);
 assertTrue(node.hasAttr(attributeKey));
 }

 // Test for removing an attribute
 @Test
 public void testRemovingAnAttribute() {
 String attributeKey = "id";
 String attributeValue = "testAttribute";
 Node node = new Element("div");
 node.attr(attributeKey, attributeValue);
 node.removeAttr(attributeKey);
 assertFalse(node.hasAttr(attributeKey));
 }

 // Test for getting baseUri
 @Test
 public void testGettingBaseUri() {
 String baseUri = "http://example.com";
 Node node = new Element("div", baseUri);
 assertEquals(baseUri, node.baseUri());
 }

 // Test for setting baseUri
 @Test
 public void testSettingBaseUri() {
 String baseUri = "http://example.com";
 String newBaseUri = "http://anotherexample.com";
 Node node = new Element("div", baseUri);
 node.setBaseUri(newBaseUri);
 assertEquals(newBaseUri, node.baseUri());
 }

 // Test for getting an absolute URL from a URL attribute that may be relative
 @Test
 public void testAbsUrl() {
 String baseUri = "http://example.com";
 String relativeUrl = "./test.html";
 Node node = new Element("a", baseUri);
 node.attr("href", relativeUrl);
 assertEquals("http://example.com/test.html", node.absUrl("href"));
 }

 // Test for getting a child node by index
 @Test
 public void testGettingAChildNodeByIndex() {
 String childNodeName = "p";
 Node parentNode = new Element("div");
 Node childNode = new Element(childNodeName);
 parentNode.addChild(childNode);
 assertEquals(childNode, parentNode.childNode(0));
 }

 // Test for getting a child node's siblings
 @Test
 public void testGettingASiblingNode() {
 String siblingNodeName = "p";
 Node parentNode = new Element("div");
 Node childNode1 = new Element(childNodeName);
 Node childNode2 = new Element(childNodeName);
 parentNode.addChild(childNode1);
 parentNode.addChild(childNode2);
 Node siblingNode = parentNode.childNode(1);
 assertEquals(childNode2, siblingNode);
 }

 // Test for getting a parent node
 @Test
 public void testGettingAParentNode() {
 String parentNodeName = "body";
 String childNodeName = "p";
 Node parentNode = new Element(parentNodeName);
 Node childNode = new Element(childNodeName);
 parentNode.addChild(childNode);
 assertEquals(parentNode, childNode.parent());
 }

 // Test for removing a node from the DOM tree
 @Test
 public void testRemovingANodeFromTheDOMTree() {
 String nodeName = "p";
 Node parentNode = new Element("div");
 Node nodeToRemove = new Element(nodeName);
 parentNode.addChild(nodeToRemove);
 parentNode.removeChild(nodeToRemove);
 assertTrue(parentNode.childNodes().isEmpty());
 }

 // Test for replacing a node in the DOM tree
 @Test
 public void testReplacingANodeInTheDOMTree() {
 String nodeNameToReplace = "p";
 String newNodeName = "span";
 Node parentNode = new Element("div");
 Node nodeToReplace = new Element(nodeNameToReplace);
 Node newNode = new Element(newNodeName);
 parentNode.addChild(nodeToReplace);
 parentNode.replaceChild(nodeToReplace, newNode);
 assertEquals(newNode, parentNode.childNode(0));
 }

 // Test for edge cases and exception handling
 @Test(expected = IllegalArgumentException.class)
 public void testExceptionHandling() {
 String baseUri = null;
 Node node = new Element("div", baseUri);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExceptionHandling2() {
 Attributes attributes = null;
 Node node = new Element("div", "", attributes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExceptionHandling3() {
 String attributeKey = null;
 Node node = new Element("div");
 node.attr(attributeKey, "testAttribute");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExceptionHandling4() {
 String attributeValue = null;
 Node node = new Element("div");
 node.attr("attributeKey", attributeValue);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testExceptionHandling5() {
 Node parentNode = new Element("div");
 Node childNode = new Element("p");
 parentNode.removeChild(childNode);
 parentNode.childNode(1);
 }
}
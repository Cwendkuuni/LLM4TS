package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodeTest {

 @Test
 public void testNodeConstructorWithBaseUriAndAttributes() {
 Node node = new Node("http://www.example.com", new Attributes());
 assertEquals("http://www.example.com", node.baseUri());
 assertNotNull(node.attributes());
 }

 @Test
 public void testNodeConstructorWithBaseUriOnly() {
 Node node = new Node("http://www.example.com");
 assertEquals("http://www.example.com", node.baseUri());
 assertNull(node.attributes());
 }

 @Test
 public void testDefaultConstructor() {
 Node node = new Node();
 assertTrue(node.childNodes().isEmpty());
 assertNull(node.attributes());
 }

 @Test
 public void testNodeName() {
 fail("Abstract class, cannot instantiate or test node name.");
 }

 @Test
 public void testGetAttribute() {
 Node node = new Elements.SingleTagElement("a", new Attributes().put("href", "http://www.example.com"));
 assertEquals("http://www.example.com", node.attr("href"));
 }

 @Test
 public void testGetAttributeAbsUrl() {
 Node node = new Elements.SingleTagElement("a", new Attributes().put("abs:href", "//www.example.com"));
 assertEquals("http://www.example.com", node.absUrl("href"));
 }

 @Test
 public void testGetAttributes() {
 Node node = new Elements.SingleTagElement("a", new Attributes().put("href", "http://www.example.com").put("class", "example"));
 Attributes attributes = node.attributes();
 List<String> attributeNames = new ArrayList<>(attributes.size());
 for (String name : attributes.keySet())
 attributeNames.add(name);
 List<String> expectedAttributeNames = new ArrayList<>(2);
 expectedAttributeNames.add("class");
 expectedAttributeNames.add("href");
 assertEquals(expectedAttributeNames, attributeNames);
 }

 @Test
 public void testSetAttribute() {
 Node node = new Elements.SingleTagElement("a");
 node.attr("class", "example");
 node.attr("href", "http://www.example.com");
 assertEquals("http://www.example.com", node.attr("href"));
 assertEquals("example", node.attr("class"));
 }

 @Test
 public void testHasAttribute() {
 Node node = new Elements.SingleTagElement("a", new Attributes().put("href", "http://www.example.com"));
 assertTrue(node.hasAttr("href"));
 assertFalse(node.hasAttr("class"));
 }

 @Test
 public void testRemoveAttribute() {
 Node node = new Elements.SingleTagElement("a", new Attributes().put("href", "http://www.example.com").put("class", "example"));
 node.removeAttr("class");
 assertFalse(node.hasAttr("class"));
 }

 @Test
 public void testGetBaseUri() {
 Node node = new Elements.SingleTagElement("a", new Attributes().put("abs:href", "//www.example.com"));
 assertEquals("http://www.example.com", node.baseUri());
 }

 @Test
 public void testSetBaseUri() {
 Node node = new Elements.SingleTagElement("a");
 node.setBaseUri("http://www.example.com");
 assertEquals("http://www.example.com", node.baseUri());
 }

 @Test
 public void testGetChildNode() {
 Node parentNode = new Elements.SingleTagElement("div");
 Node childNode = new Elements.SingleTagElement("p");
 parentNode.appendChild(childNode);
 assertEquals(childNode, parentNode.childNode(0));
 }

 @Test
 public void testGetChildren() {
 Node parentNode = new Elements.SingleTagElement("div");
 Node childNode1 = new Elements.SingleTagElement("p");
 Node childNode2 = new Elements.SingleTagElement("p");
 parentNode.appendChild(childNode1);
 parentNode.appendChild(childNode2);
 List<Node> children = parentNode.childNodes();
 assertEquals(2, children.size());
 assertEquals(childNode1, children.get(0));
 assertEquals(childNode2, children.get(1));
 }

 @Test
 public void testChildNodesAsArray() {
 Node parentNode = new Elements.SingleTagElement("div");
 Node childNode1 = new Elements.SingleTagElement("p");
 Node childNode2 = new Elements.SingleTagElement("p");
 parentNode.appendChild(childNode1);
 parentNode.appendChild(childNode2);
 Node[] children = parentNode.childNodesAsArray();
 assertEquals(2, children.length);
 assertEquals(childNode1, children[0]);
 assertEquals(childNode2, children[1]);
 }

 @Test
 public void testGetParent() {
 Node childNode = new Elements.SingleTagElement("p");
 Node parentNode = new Elements.SingleTagElement("div");
 parentNode.appendChild(childNode);
 assertEquals(parentNode, childNode.parent());
 }

 @Test
 public void testGetOwnerDocument() {
 Node childNode = new Elements.SingleTagElement("p");
 Node parentNode = new Elements.SingleTagElement("div");
 Document document = new Document();
 parentNode.ownerDocument(document);
 parentNode.appendChild(childNode);
 assertEquals(document, childNode.ownerDocument());
 }

 @Test
 public void testRemove() {
 Node parentNode = new Elements.SingleTagElement("div");
 Node childNode = new Elements.SingleTagElement("p");
 parentNode.appendChild(childNode);
 parentNode.removeChild(childNode);
 assertTrue(parentNode.childNodes().isEmpty());
 }

 @Test
 public void testReplaceWith() {
 Node parentNode = new Elements.SingleTagElement("div");
 Node oldChildNode = new Elements.SingleTagElement("p");
 Node newChildNode = new Elements.SingleTagElement("p");
 parentNode.appendChild(oldChildNode);
 parentNode.replaceChild(oldChildNode, newChildNode);
 assertFalse(parentNode.childNodes().contains(oldChildNode));
 assertEquals(newChildNode, parentNode.childNode(0));
 }

 @Test
 public void testSetParentNode() {
 Node childNode = new Elements.SingleTagElement("p");
 Node parentNode = new Elements.SingleTagElement("div");
 childNode.setParentNode(parentNode);
 assertEquals(parentNode, childNode.parent());
 }

 @Test
 public void testReplaceChild() {
 Node parentNode = new Elements.SingleTagElement("div");
 Node oldChildNode = new Elements.SingleTagElement("p");
 Node newChildNode = new Elements.SingleTagElement("p");
 parentNode.appendChild(oldChildNode);
 parentNode.replaceChild(oldChildNode, newChildChildNode);
 assertFalse(parentNode.childNodes().contains(oldChildNode));
 assertEquals(newChildNode, parentNode.childNode(0));
 }

 @Test
 public void testRemoveChild() {
 Node parentNode = new Elements.SingleTagElement("div");
 Node oldChildNode = new Elements.SingleTagElement("p");
 parentNode.appendChild(oldChildNode);
 parentNode.removeChild(oldChildNode);
 assertTrue(parentNode.childNodes().isEmpty());
 }

 @Test
 public void testAddChildren() {
 Node parentNode = new Elements.SingleTagElement("div");
 Node childNode1 = new Elements.SingleTagElement("p");
 Node childNode2 = new Elements.SingleTagElement("p");
 parentNode.addChildren(childNode1, childNode2);
 assertEquals(2, parentNode.childNodes().size());
 assertEquals(childNode1, parentNode.childNode(0));
 assertEquals(childNode2, parentNode.childNode(1));
 }

 @Test
 public void testAddChildrenWithIndex() {
 Node parentNode = new Elements.SingleTagElement("div");
 Node childNode1 = new Elements.SingleTagElement("p");
 Node childNode2 = new Elements.SingleTagElement("p");
 parentNode.addChildren(1, childNode1, childNode2);
 assertEquals(2, parentNode.childNodes().size());
 assertEquals(childNode1, parentNode.childNode(1));
 assertEquals(childNode2, parentNode.childNode(2));
 }
}
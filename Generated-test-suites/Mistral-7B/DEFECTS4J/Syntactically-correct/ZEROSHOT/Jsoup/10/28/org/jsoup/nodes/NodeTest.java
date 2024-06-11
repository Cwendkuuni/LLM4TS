package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NodeTest {

 @Test
 public void testNodeCreation() {
 Node node = new Node("baseUri", new Attributes());
 assertEquals("baseUri", node.baseUri());
 assertTrue(node.childNodes().isEmpty());
 assertNull(node.parentNode());
 }

 @Test
 public void testNodeCreationWithAttributes() {
 Node node = new Node("baseUri", new Attributes().put("attr1", "value1").put("attr2", "value2"));
 assertEquals("baseUri", node.baseUri());
 assertEquals("value1", node.attr("attr1"));
 assertEquals("value2", node.attr("attr2"));
 }

 @Test
 public void testEmptyNodeCreation() {
 Node node = new Node();
 assertTrue(node.childNodes().isEmpty());
 assertNull(node.parentNode());
 }

 @Test
 public void testAbstractNodeMethods() {
 Node node = new Node("baseUri", new Attributes());
 assertEquals("baseUri", node.baseUri());
 assertNull(node.parentNode());

 String attrValue = "value";
 node.attr("attr", attrValue);
 assertEquals(attrValue, node.attr("attr"));
 assertFalse(node.hasAttr("attr2"));

 String absUrl = "http://example.com";
 node.absUrl("attr").equals(absUrl);
 assertEquals("", node.absUrl("nonexistentattr"));
 }

 @Test
 public void testAttributeRemoval() {
 Node node = new Node("baseUri", new Attributes().put("attr1", "value1").put("attr2", "value2"));
 node.removeAttr("attr1");
 assertFalse(node.hasAttr("attr1"));
 }

 @Test
 public void testAttributeSetting() {
 Node node = new Node("baseUri", new Attributes().put("attr1", "value1").put("attr2", "value2"));
 node.attr("attr1", "newValue1");
 node.attr("attr2", "newValue2");
 assertEquals("newValue1", node.attr("attr1"));
 assertEquals("newValue2", node.attr("attr2"));
 }

 @Test
 public void testChildNodes() {
 Node parentNode = new Node("baseUri", new Attributes());
 Node childNode1 = new Node("child1BaseUri", new Attributes());
 Node childNode2 = new Node("child2BaseUri", new Attributes());

 parentNode.addChildren(childNode1, childNode2);
 List<Node> childNodes = parentNode.childNodes();

 assertEquals(2, childNodes.size());
 assertEquals(childNode1, childNodes.get(0));
 assertEquals(childNode2, childNodes.get(1));
 }

 @Test
 public void testParentNode() {
 Node childNode1 = new Node("child1BaseUri", new Attributes());
 Node childNode2 = new Node("child2BaseUri", new Attributes());
 Node parentNode = new Node("baseUri", new Attributes());

 parentNode.addChildren(childNode1, childNode2);
 assertEquals(parentNode, childNode1.parent());
 assertEquals(parentNode, childNode2.parent());
 }

 @Test
 public void testNodeTraversal() {
 Node parentNode = new Node("baseUri", new Attributes());
 Node childNode1 = new Node("child1BaseUri", new Attributes());
 Node childNode2 = new Node("child2BaseUri", new Attributes());
 Node grandChildNode = new Node("grandChildBaseUri", new Attributes());

 parentNode.addChildren(childNode1, childNode2);
 childNode1.addChildren(grandChildNode);

 List<Node> siblings = parentNode.siblingNodes();
 assertEquals(0, siblings.size());

 List<Node> children = parentNode.childNodes();
 assertEquals(3, children.size());
 assertEquals(childNode1, children.get(0));
 assertEquals(childNode2, children.get(1));
 assertEquals(grandChildNode, children.get(2));
 }

 @Test
 public void testNodeRemoval() {
 Node parentNode = new Node("baseUri", new Attributes());
 Node childNode1 = new Node("child1BaseUri", new Attributes());
 Node childNode2 = new Node("child2BaseUri", new Attributes());

 parentNode.addChildren(childNode1, childNode2);
 parentNode.removeChild(childNode1);
 List<Node> children = parentNode.childNodes();
 assertEquals(1, children.size());
 assertEquals(childNode2, children.get(0));
 }
}
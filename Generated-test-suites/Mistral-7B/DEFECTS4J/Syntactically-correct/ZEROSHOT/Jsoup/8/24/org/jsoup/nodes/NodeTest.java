package org.jsoup.nodes;

import org.junit.Assert; import org.junit.Test; import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NodeTest {

 @Test
 public void testNodeCreation() {
 Node node = new Node("baseUri", new Attributes());
 Assert.assertEquals("baseUri", node.baseUri());
 Assert.assertEquals(Collections.emptyList(), node.childNodes());
 Assert.assertNull(node.parentNode());
 Assert.assertNull(node.attributes());
 }

 @Test
 public void testNodeCreationWithAttributes() {
 Node node = new Node("baseUri", new Attributes().put("attr1", "value1").put("attr2", "value2"));
 Assert.assertEquals("baseUri", node.baseUri());
 Assert.assertEquals("value1", node.attr("attr1"));
 Assert.assertEquals("value2", node.attr("attr2"));
 Assert.assertEquals(Collections.emptyList(), node.childNodes());
 Assert.assertNull(node.parentNode());
 }

 @Test
 public void testAbstractNodeMethods() {
 Node node = new Node("baseUri", new Attributes());

 Assert.assertEquals("", node.nodeName());
 Assert.assertEquals("", node.attr("nonExistentAttr"));
 Assert.assertEquals(Collections.emptyMap(), node.attributes());

 Assert.assertFalse(node.hasAttr("nonExistentAttr"));
 Assert.assertFalse(node.removeAttr("nonExistentAttr").hasAttr("nonExistentAttr"));

 Assert.assertEquals("", node.absUrl("nonExistentAttr"));
 Assert.assertFalse(node.hasAttr("attr1"));
 Assert.assertEquals("value1", node.attr("attr1"));
 Assert.assertEquals("value1", node.absUrl("attr1"));
 Assert.assertEquals("value2", node.attr("attr2"));
 Assert.assertEquals("value2", node.absUrl("attr2"));

 Assert.assertNull(node.childNode(0));
 Assert.assertEquals(Collections.emptyList(), node.childNodes());
 Assert.assertNull(node.parent());

 Assert.assertEquals("baseUri", node.baseUri());
 Assert.assertEquals(node, node.ownerDocument());

 Node childNode = new Node("childNode", new Attributes());
 node.addChildren(childNode);
 Assert.assertEquals(childNode, node.childNode(0));
 Assert.assertEquals(childNode, node.childNodes().get(0));
 Assert.assertEquals(1, node.childNodes().size());
 Assert.assertEquals(childNode, node.childNodesAsArray()[0]);
 Assert.assertEquals(node, childNode.parent());
 }

 @Test
 public void testNodeParentAndChildren() {
 Node parentNode = new Node("parent", new Attributes());
 Node node1 = new Node("node1", new Attributes());
 Node node2 = new Node("node2", new Attributes());
 parentNode.addChildren(node1, node2);

 Assert.assertNull(node1.parentNode());
 Assert.assertEquals(parentNode, node1.parent());
 Assert.assertEquals(node2, node1.nextSibling());
 Assert.assertEquals(node1, node2.previousSibling());
 Assert.assertEquals(new ArrayList<>(Arrays.asList(node1, node2)), parentNode.childNodes());
 }

 @Test
 public void testNodeRemove() {
 Node parentNode = new Node("parent", new Attributes());
 Node node1 = new Node("node1", new Attributes());
 Node node2 = new Node("node2", new Attributes());
 parentNode.addChildren(node1, node2);

 Assert.assertEquals(2, parentNode.childNodes().size());
 parentNode.removeChild(node1);
 Assert.assertEquals(1, parentNode.childNodes().size());
 Assert.assertNull(node1.parent());
 Assert.assertEquals(node2, parentNode.childNode(0));
 }
}
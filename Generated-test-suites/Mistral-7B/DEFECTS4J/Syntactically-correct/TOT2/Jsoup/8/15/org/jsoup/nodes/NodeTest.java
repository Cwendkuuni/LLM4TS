package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NodeTest {

 private static Node createTestNode(String baseUri) {
 return new ElementsNode("test", baseUri, new Attributes()) {
 @Override
 public String nodeName() {
 return "test";
 }
 };
 }

 @Test
 public void testAttr() {
 Node node = createTestNode("http://example.com");
 node.attr("test", "hello");
 assertEquals("hello", node.attr("test"));
 assertEquals("", node.attr("nonExistentAttr"));
 }

 @Test
 public void testHasAttr() {
 Node node = createTestNode("http://example.com");
 node.attr("test", "hello");
 assertTrue(node.hasAttr("test"));
 assertFalse(node.hasAttr("nonExistentAttr"));
 }

 @Test
 public void testRemoveAttr() {
 Node node = createTestNode("http://example.com");
 node.attr("test", "hello");
 node.removeAttr("test");
 assertFalse(node.hasAttr("test"));
 }

 @Test
 public void testAbsUrl() {
 Node node = createTestNode("http://example.com");
 node.attr("test", "relative-url.html");
 assertEquals("http://example.com/relative-url.html", node.absUrl("test"));
 node.attr("test", "http://another-example.com/absolute-url.html");
 assertEquals("http://another-example.com/absolute-url.html", node.absUrl("test"));
 }

 @Test
 public void testChildNodes() {
 Node parent = createTestNode("http://example.com");
 Node child1 = new ElementsNode("child1", parent.baseUri(), new Attributes());
 Node child2 = new ElementsNode("child2", parent.baseUri(), new Attributes());
 parent.addChildren(child1, child2);
 List<Node> children = parent.childNodes();
 assertEquals(2, children.size());
 assertEquals(child1, children.get(0));
 assertEquals(child2, children.get(1));
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 new Node("", null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullAttributes() {
 new Node("", new Attributes());
 }

 @Test
 public void testSetBaseUri() {
 Node node = createTestNode("http://example.com");
 node.setBaseUri("http://another-example.com");
 assertEquals("http://another-example.com", node.baseUri());
 }

 @Test
 public void testIndexedChildNodes() {
 Node parent = createTestNode("http://example.com");
 Node child1 = new ElementsNode("child1", parent.baseUri(), new Attributes());
 Node child2 = new ElementsNode("child2", parent.baseUri(), new Attributes());
 parent.addChildren(child1, child2);
 assertEquals(child1, parent.childNode(0));
 assertEquals(child2, parent.childNode(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testNegativeChildNodeIndex() {
 Node parent = createTestNode("http://example.com");
 parent.addChildren(Collections.emptyList());
 parent.childNode(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testChildNodeIndexOutOfBounds() {
 Node parent = createTestNode("http://example.com");
 Node child1 = new ElementsNode("child1", parent.baseUri(), new Attributes());
 Node child2 = new ElementsNode("child2", parent.baseUri(), new Attributes());
 parent.addChildren(child1, child2);
 parent.addChildren(new ElementsNode("child3", parent.baseUri(), new Attributes()));
 assertEquals(child2, parent.childNode(1));
 assertEquals(null, parent.childNode(2));
 }

 @Test
 public void testListChildNodes() {
 Node parent = createTestNode("http://example.com");
 Node child1 = new ElementsNode("child1", parent.baseUri(), new Attributes());
 Node child2 = new ElementsNode("child2", parent.baseUri(), new Attributes());
 parent.addChildren(child1, child2);
 List<Node> children = parent.childNodes();
 assertEquals(2, children.size());
 assertEquals(child1, children.get(0));
 assertEquals(child2, children.get(1));
 }

 @Test
 public void testArrayChildNodes() {
 Node parent = createTestNode("http://example.com");
 Node child1 = new ElementsNode("child1", parent.baseUri(), new Attributes());
 Node child2 = new ElementsNode("child2", parent.baseUri(), new Attributes());
 parent.addChildren(child1, child2);
 Node[] children = parent.childNodesAsArray();
 assertEquals(2, children.length);
 assertEquals(child1, children[0]);
 assertEquals(child2, children[1]);
 }
}
package org.jsoup.nodes;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class NodeTest {

 @Test
 public void testEmptyNode() {
 Node emptyNode = new Node();
 assertEquals("", emptyNode.nodeName());
 assertEquals(null, emptyNode.attributes());
 assertNull(emptyNode.parentNode());
 assertNull(emptyNode.baseUri());
 assertSame(Collections.EMPTY_LIST, emptyNode.childNodes());
 }

 @Test
 public void testNodeWithAttributes() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("id", "testId");
 attributes.put("class", "testClass");

 Node node = new Element("div", baseUri, attributes);
 assertEquals("div", node.nodeName());
 assertEquals(attributes, node.attributes());
 assertEquals(baseUri, node.baseUri());
 assertNull(node.parentNode());

 List<Node> children = new ArrayList<>();
 Node child1 = new TextNode("Child 1");
 Node child2 = new TextNode("Child 2");
 children.add(child1);
 children.add(child2);

 node.addChildren(child1, child2);
 assertEquals(children.size(), node.childNodes().size());
 assertSame(child1, node.childNode(0));
 assertSame(child2, node.childNode(1));
 }

 @Test
 public void testNodeWithChildren() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("id", "testId");

 Node parentNode = new Element("div", baseUri, attributes);

 Node child1 = new Element("p", baseUri, new Attributes());
 child1.setText("Child 1");

 Node child2 = new TextNode("Child 2");
 child2.setText("Text node");

 parentNode.addChildren(child1, child2);
 assertEquals(2, parentNode.childNodes().size());
 assertEquals(child1, parentNode.childNode(0));
 assertEquals(child2, parentNode.childNode(1));
 }

 @Test
 public void testNodeWithParent() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("id", "testId");

 Node childNode = new Element("p", baseUri, attributes);
 childNode.setText("Child");

 Node parentNode = new Element("div", baseUri, new Attributes());
 parentNode.addChild(childNode);

 assertEquals(childNode, parentNode.childNode(0));
 assertEquals(parentNode, childNode.parentNode());
 }

 @Test
 public void testNodeWithBaseUri() {
 String baseUri = "http://example.com";
 String childUri = "child.html";
 Attributes attributes = new Attributes();

 Node node = new Element("a", baseUri, attributes);
 node.attr("href", childUri);

 assertEquals(baseUri, node.baseUri());
 assertEquals(childUri, node.absUrl("href"));
 }

 @Test
 public void testNodeWithInvalidBaseUri() {
 String baseUri = null;
 Attributes attributes = new Attributes();

 Node node = new Element("a", baseUri, attributes);
 node.attr("href", "child.html");

 assertEquals("", node.baseUri());
 assertNull(node.absUrl("href"));
 }

 @Test
 public void testNodeWithMissingAttribute() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();

 Node node = new Element("a", baseUri, attributes);
 assertFalse(node.hasAttr("href"));
 assertNull(node.attr("href"));
 }

 @Test
 public void testNodeWithEmptyAttributeValue() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("href", "");

 Node node = new Element("a", baseUri, attributes);
 assertFalse(node.hasAttr("href"));
 assertNull(node.attr("href"));
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 Attributes attributes = new Attributes();

 Node node = new Element("a", null, attributes);
 node.attr("href", "child.html");
 }

 @Test(expected = NullPointerException.class)
 public void testNullAttributes() {
 String baseUri = "http://example.com";

 Node node = new Element("a", baseUri, null);
 node.attr("href", "child.html");
 }
}
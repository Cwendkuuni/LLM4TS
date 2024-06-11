import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class NodeTest {

 private Node createTestNode(String baseUri, String nodeName, String attributeKey, String attributeValue) {
 Node node = null;
 try {
 node = new Element(nodeName);
 node.setBaseUri(baseUri);
 node.attr(attributeKey, attributeValue);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Failed to create test node.");
 }
 return node;
 }

 @Test
 public void testNodeName() {
 Node node = new Element("div");
 assertEquals("div", node.nodeName());
 }

 @Test
 public void testAttr() {
 String baseUri = "http://example.com";
 String attributeKey = "class";
 String attributeValue = "test";
 Node node = createTestNode(baseUri, "div", attributeKey, attributeValue);

 assertEquals(attributeValue, node.attr(attributeKey));
 assertEquals("", node.attr("notExistentAttribute"));

 String absAttributeValue = node.absUrl(attributeKey);
 assertEquals(baseUri + "#" + attributeKey, absAttributeValue);
 }

 @Test
 public void testAttributes() {
 String baseUri = "http://example.com";
 String attributeKey = "class";
 String attributeValue = "test";
 Node node = createTestNode(baseUri, "div", attributeKey, attributeValue);
 Attributes attributes = node.attributes();

 assertEquals(attributeValue, attributes.get(attributeKey));
 assertEquals(0, attributes.size());
 }

 @Test
 public void testSetAttr() {
 String baseUri = "http://example.com";
 String attributeKey = "class";
 String attributeValue = "test";
 Node node = new Element("div");
 node.setBaseUri(baseUri);

 node.attr(attributeKey, attributeValue);
 assertEquals(attributeValue, node.attr(attributeKey));

 node.removeAttr(attributeKey);
 assertEquals("", node.attr(attributeKey));
 }

 @Test
 public void testHasAttr() {
 String baseUri = "http://example.com";
 String attributeKey = "class";
 String attributeValue = "test";
 Node node = createTestNode(baseUri, "div", attributeKey, attributeValue);

 assertTrue(node.hasAttr(attributeKey));
 assertFalse(node.hasAttr("notExistentAttribute"));
 }

 @Test
 public void testRemoveAttr() {
 String baseUri = "http://example.com";
 String attributeKey = "class";
 String attributeValue = "test";
 Node node = createTestNode(baseUri, "div", attributeKey, attributeValue);

 node.removeAttr(attributeKey);
 assertFalse(node.hasAttr(attributeKey));
 }

 @Test
 public void testBaseUri() {
 String baseUri = "http://example.com";
 Node node = new Element("div");
 node.setBaseUri(baseUri);

 assertEquals(baseUri, node.baseUri());
 node.setBaseUri("");
 assertNull(node.baseUri());
 }

 @Test
 public void testAbsUrl() {
 String baseUri = "http://example.com";
 String absoluteUrl = "http://example.com/test.html";
 Node node = new Element("a");
 node.attr("href", "test.html");
 node.setBaseUri(baseUri);

 assertEquals(absoluteUrl, node.absUrl("href"));
 assertEquals("", node.absUrl(""));
 }

 @Test
 public void testChildNode() {
 String baseUri = "http://example.com";
 Node parentNode = new Element("div");
 parentNode.setBaseUri(baseUri);
 Node childNode = new Element("p");
 parentNode.addChildren(childNode);

 assertEquals(childNode, parentNode.childNode(0));
 assertNull(parentNode.childNode(1));
 }

 @Test
 public void testChildren() {
 String baseUri = "http://example.com";
 Node parentNode = new Element("div");
 parentNode.setBaseUri(baseUri);
 Node childNode1 = new Element("p");
 Node childNode2 = new Element("p");
 parentNode.addChildren(childNode1, childNode2);

 List<Node> children = parentNode.childNodes();
 assertEquals(2, children.size());
 assertSame(childNode1, children.get(0));
 assertSame(childNode2, children.get(1));
 }

 @Test
 public void testChildNodesAsArray() {
 String baseUri = "http://example.com";
 Node parentNode = new Element("div");
 parentNode.setBaseUri(baseUri);
 Node childNode1 = new Element("p");
 Node childNode2 = new Element("p");
 parentNode.addChildren(childNode1, childNode2);

 Node[] children = parentNode.childNodesAsArray();
 assertEquals(2, children.length);
 assertSame(childNode1, children[0]);
 assertSame(childNode2, children[1]);
 }

 @Test
 public void testParentNode() {
 String baseUri = "http://example.com";
 Node childNode = new Element("p");
 Node parentNode = new Element("div");
 parentNode.setBaseUri(baseUri);
 childNode.setParentNode(parentNode);

 assertEquals(parentNode, childNode.parent());
 assertNull(childNode.parent());
 }

 @Test
 public void testOwnerDocument() {
 String baseUri = "http://example.com";
 Node childNode = new Element("p");
 Node parentNode = new Element("div");
 parentNode.setBaseUri(baseUri);
 childNode.setParentNode(parentNode);

 assertEquals(parentNode.ownerDocument(), childNode.ownerDocument());
 }

 @Test
 public void testRemove() {
 String baseUri = "http://example.com";
 Node parentNode = new Element("div");
 parentNode.setBaseUri(baseUri);
 Node childNode = new Element("p");
 parentNode.addChild(childNode);
 parentNode.removeChild(childNode);

 assertFalse(parentNode.hasChild(childNode));
 assertNull(childNode.parent());
 }

 @Test
 public void testReplaceChild() {
 String baseUri = "http://example.com";
 Node parentNode = new Element("div");
 parentNode.setBaseUri(baseUri);
 Node childNode1 = new Element("p");
 Node childNode2 = new Element("p");
 parentNode.addChild(childNode1);
 parentNode.replaceChild(childNode1, childNode2);

 assertFalse(parentNode.hasChild(childNode1));
 assertEquals(childNode2, parentNode.childNode(0));
 }

 @Test
 public void testAddChildren() {
 String baseUri = "http://example.com";
 Node parentNode = new Element("div");
 parentNode.setBaseUri(baseUri);
 Node childNode1 = new Element("p");
 Node childNode2 = new Element("p");
 parentNode.addChildren(childNode1, childNode2);

 assertEquals(2, parentNode.childNodes().size());
 assertEquals(childNode1, parentNode.childNode(0));
 assertEquals(childNode2, parentNode.childNode(1));
 }

 @Test
 public void testAddChildrenWithIndex() {
 String baseUri = "http://example.com";
 Node parentNode = new Element("div");
 parentNode.setBaseUri(baseUri);
 Node childNode1 = new Element("p");
 Node childNode2 = new Element("p");
 parentNode.addChildren(1, childNode1, childNode2);

 assertEquals(2, parentNode.childNodes().size());
 assertEquals(childNode2, parentNode.childNode(0));
 assertEquals(childNode1, parentNode.childNode(1));
 }

 @Test(expected = NullPointerException.class)
 public void testNullParentNode() {
 Node childNode = new Element("p");
 childNode.setParentNode(null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testNegativeIndexChildNode() {
 String baseUri = "http://example.com";
 Node parentNode = new Element("div");
 parentNode.setBaseUri(baseUri);
 Node childNode1 = new Element("p");
 parentNode.addChild(childNode1);

 parentNode.childNode(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexOutOfBoundsChildNodes() {
 String baseUri = "http://example.com";
 Node parentNode = new Element("div");
 parentNode.setBaseUri(baseUri);
 Node childNode1 = new Element("p");
 Node childNode2 = new Element("p");
 parentNode.addChildren(childNode1, childNode2);

 parentNode.childNodes(3);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexOutOfBoundsSiblingNodes() {
 String baseUri = "http://example.com";
 Node parentNode = new Element("div");
 parentNode.setBaseUri(baseUri);
 Node childNode1 = new Element("p");
 parentNode.addChild(childNode1);

 parentNode.siblingNodes(1);
 }
}
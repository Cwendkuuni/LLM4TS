import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class NodeTest {

 @Test
 public void testNodeCreation() {
 Node node = new Node("", new Attributes());
 assertNotNull(node);
 assertEquals("", node.nodeName());
 assertNull(node.parentNode());
 assertNull(node.childNodes());
 assertNull(node.attributes());
 assertNull(node.baseUri());
 assertNull(node.siblingIndex());
 }

 @Test
 public void testNodeCreationWithBaseUri() {
 Node node = new Node("http://test.com", new Attributes());
 assertNotNull(node);
 assertEquals("", node.nodeName());
 assertNull(node.parentNode());
 assertNull(node.childNodes());
 assertNull(node.attributes());
 assertEquals("http://test.com", node.baseUri());
 assertNull(node.siblingIndex());
 }

 @Test
 public void testNodeCreationWithoutBaseUriOrAttributes() {
 Node node = new Node();
 assertNotNull(node);
 assertEquals("", node.nodeName());
 assertNull(node.parentNode());
 assertNull(node.childNodes());
 assertNull(node.attributes());
 assertNull(node.baseUri());
 assertNull(node.siblingIndex());
 }

 @Test
 public void testNodeName() {
 Node node = new Node("", new Attributes());
 node.attr("tag", "test");
 assertEquals("test", node.nodeName());
 }

 @Test
 public void testGetAttribute() {
 Node node = new Node("", new Attributes().put("foo", "bar"));
 assertEquals("bar", node.attr("foo"));
 }

 @Test
 public void testGetAttributeAbsUrl() {
 Node node = new Node("", new Attributes().put("href", "test.html"));
 assertEquals("test.html", node.absUrl("href"));
 }

 @Test
 public void testGetAttributeAbsUrlNotFound() {
 Node node = new Node("", new Attributes());
 assertEquals("", node.absUrl("foo"));
 }

 @Test
 public void testGetAttributeAbsUrlMalformedUrl() {
 Node node = new Node("", new Attributes().put("base", "http://test.com"));
 assertEquals("", node.absUrl("foo:bar"));
 }

 @Test
 public void testGetAllAttributes() {
 Node node = new Node("", new Attributes().put("foo", "bar").put("baz", "qux"));
 Attributes attributes = node.attributes();
 assertEquals(2, attributes.size());
 assertTrue(attributes.hasKey("foo"));
 assertEquals("bar", attributes.get("foo"));
 assertTrue(attributes.hasKey("baz"));
 assertEquals("qux", attributes.get("baz"));
 }

 @Test
 public void testSettingAttribute() {
 Node node = new Node("", new Attributes());
 node.attr("foo", "bar");
 assertEquals("bar", node.attr("foo"));
 }

 @Test
 public void testSettingAttributeOverwrites() {
 Node node = new Node("", new Attributes().put("foo", "bar").put("foo", "qux"));
 assertEquals("qux", node.attr("foo"));
 }

 @Test
 public void testRemovingAttribute() {
 Node node = new Node("", new Attributes().put("foo", "bar"));
 node.removeAttr("foo");
 assertFalse(node.hasAttr("foo"));
 }

 @Test
 public void testHasAttribute() {
 Node node = new Node("", new Attributes().put("foo", "bar"));
 assertTrue(node.hasAttr("foo"));
 }

 @Test
 public void testHasAttributeFalse() {
 Node node = new Node("", new Attributes());
 assertFalse(node.hasAttr("foo"));
 }

 @Test
 public void testGetBaseUri() {
 Node node = new Node("", new Attributes()).attr("base", "http://test.com");
 assertEquals("http://test.com", node.baseUri());
 }

 @Test
 public void testSettingBaseUri() {
 Node node = new Node("", new Attributes());
 node.setBaseUri("http://test.com");
 assertEquals("http://test.com", node.baseUri());
 }

 @Test
 public void testGetChildNodes() {
 Node parent = new Node("", new Attributes());
 Node child1 = new Node("", new Attributes()).attr("tag", "child1");
 Node child2 = new Node("", new Attributes()).attr("tag", "child2");
 parent.addChildren(child1, child2);
 List<Node> children = parent.childNodes();
 assertEquals(2, children.size());
 assertEquals(child1, children.get(0));
 assertEquals(child2, children.get(1));
 }

 @Test
 public void testGetChildNodesEmpty() {
 Node node = new Node("", new Attributes());
 List<Node> children = node.childNodes();
 assertTrue(children.isEmpty());
 }

 @Test
 public void testGetParent() {
 Node child = new Node("", new Attributes()).attr("tag", "child");
 Node parent = new Node("", new Attributes()).addChild(child);
 assertEquals(parent, child.parent());
 }

 @Test
 public void testGetParentNull() {
 Node node = new Node("", new Attributes());
 assertEquals(null, node.parent());
 }

 @Test
 public void testGetDocument() {
 Node child = new Node("", new Attributes()).attr("tag", "child");
 Node parent = new Node("", new Attributes()).addChild(child);
 Document document = new Document();
 parent.setParentNode(document);
 assertEquals(document, child.ownerDocument());
 }

 @Test
 public void testRemove() {
 Node parent = new Node("", new Attributes());
 Node child = new Node("", new Attributes()).attr("tag", "child");
 parent.addChild(child);
 parent.removeChild(child);
 assertEquals(0, parent.childNodes().size());
 }

 @Test
 public void testReplaceWith() {
 Node parent = new Node("", new Attributes());
 Node oldChild = new Node("", new Attributes()).attr("tag", "oldChild");
 Node newChild = new Node("", new Attributes()).attr("tag", "newChild");
 parent.addChild(oldChild);
 parent.replaceWith(newChild, oldChild);
 assertEquals(1, parent.childNodes().size());
 assertEquals(newChild, parent.childNodes().get(0));
 }

 @Test
 public void testSetParentNode() {
 Node child = new Node("", new Attributes()).attr("tag", "child");
 Node parent = new Node("", new Attributes());
 child.setParentNode(parent);
 assertEquals(parent, child.parentNode());
 }

 @Test
 public void testReplaceChild() {
 Node parent = new Node("", new Attributes());
 Node out = new Node("", new Attributes()).attr("tag", "out");
 Node in = new Node("", new Attributes()).attr("tag", "in");
 parent.addChild(out);
 parent.replaceChild(in, out);
 assertEquals(1, parent.childNodes().size());
 assertEquals(in, parent.childNodes().get(0));
 }

 @Test
 public void testRemoveChild() {
 Node parent = new Node("", new Attributes());
 Node out = new Node("", new Attributes()).attr("tag", "out");
 parent.addChild(out);
 parent.removeChild(out);
 assertEquals(0, parent.childNodes().size());
 }

 @Test
 public void testAddChildren() {
 Node parent = new Node("", new Attributes());
 Node child1 = new Node("", new Attributes()).attr("tag", "child1");
 Node child2 = new Node("", new Attributes()).attr("tag", "child2");
 parent.addChildren(child1, child2);
 assertEquals(2, parent.childNodes().size());
 }

 @Test
 public void testAddChildrenWithIndex() {
 Node parent = new Node("", new Attributes());
 Node child1 = new Node("", new Attributes()).attr("tag", "child1");
 Node child2 = new Node("", new Attributes()).attr("tag", "child2");
 parent.addChildren(1, child1, child2);
 assertEquals(2, parent.childNodes().size());
 assertEquals(child1, parent.childNodes().get(0));
 assertEquals(child2, parent.childNodes().get(1));
 }

 @Test
 public void testToString() {
 Node node = new Node("", new Attributes()).outerHtml();
 assertEquals(node.outerHtml(), node.toString());
 }
}
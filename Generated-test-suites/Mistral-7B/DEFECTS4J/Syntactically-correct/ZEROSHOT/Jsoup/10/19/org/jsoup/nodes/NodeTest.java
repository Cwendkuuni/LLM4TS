import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import static org.junit.Assert.*;

public class NodeTest {

 @Test
 public void testNodeWithBaseUriAndAttributes() {
 Node node = new Element("div");
 String baseUri = "http://test.com";
 Attributes attributes = new Attributes();
 attributes.put("class", "testClass");
 node.setBaseUri(baseUri);
 node.setAttributes(attributes);

 assertEquals("div", node.nodeName());
 assertEquals("http://test.com", node.baseUri());
 assertEquals("testClass", node.attr("class"));
 }

 @Test
 public void testNodeWithoutBaseUriOrAttributes() {
 Node node = new TextNode("Test Node");
 assertEquals("", node.baseUri());
 assertEquals("", node.attr("class"));
 }

 @Test
 public void testNodeDefaultConstructor() {
 Node node = new Node();
 assertEquals(Collections.EMPTY_LIST, node.childNodes());
 assertNull(node.attributes());
 }

 @Test
 public void testGetChildNodeByIndex() {
 Node parent = new Element("div");
 Node child1 = new Element("p");
 Node child2 = new Element("p");
 parent.addChildren(child1, child2);
 assertEquals(child1, parent.childNode(0));
 assertEquals(child2, parent.childNode(1));
 }

 @Test
 public void testGetChildren() {
 Node parent = new Element("div");
 Node child1 = new Element("p");
 Node child2 = new Element("p");
 parent.addChildren(child1, child2);
 List<Node> children = parent.childNodes();
 assertEquals(2, children.size());
 assertTrue(children.contains(child1));
 assertTrue(children.contains(child2));
 }

 @Test
 public void testAbsUrl() {
 Node node = new Element("a");
 String baseUri = "http://test.com";
 node.setBaseUri(baseUri);
 node.attr("href", "test.html");
 assertEquals("http://test.com/test.html", node.absUrl("href"));
 }

 @Test
 public void testAbsUrlWithRelativeUrl() {
 Node node = new Element("a");
 String baseUri = "http://test.com/home.html";
 node.attr("href", "contact.html");
 assertEquals("http://test.com/contact.html", node.absUrl("href"));
 }

 @Test
 public void testAbsUrlWithAbsoluteUrl() {
 Node node = new Element("a");
 String baseUri = "http://test.com/home.html";
 node.attr("href", "http://test2.com/contact.html");
 assertEquals("http://test2.com/contact.html", node.absUrl("href"));
 }

 @Test
 public void testHasAttr() {
 Node node = new Element("div");
 node.attr("class", "test");
 assertTrue(node.hasAttr("class"));
 }

 @Test
 public void testHasAttrWithNullAttributeKey() {
 Node node = new Element("div");
 assertFalse(node.hasAttr(null));
 }

 @Test
 public void testRemoveAttr() {
 Node node = new Element("div");
 node.attr("class", "test");
 node.removeAttr("class");
 assertFalse(node.hasAttr("class"));
 }

 @Test
 public void testSetBaseUri() {
 Node node = new Element("div");
 node.setBaseUri("http://test.com");
 assertEquals("http://test.com", node.baseUri());
 }

 @Test
 public void testGetParent() {
 Node parent = new Element("div");
 Node child = new Element("p");
 parent.addChildren(child);
 assertEquals(parent, child.parent());
 assertNull(child.parent());
 }

 @Test
 public void testRemove() {
 Node parent = new Element("div");
 Node child = new Element("p");
 parent.addChildren(child);
 parent.removeChild(child);
 assertFalse(parent.childNodes().contains(child));
 }

 @Test
 public void testReplaceWith() {
 Node parent = new Element("div");
 Node oldChild = new Element("p");
 Node newChild = new Element("p");
 parent.addChildren(oldChild);
 parent.replaceWith(newChild, oldChild);
 assertFalse(parent.childNodes().contains(oldChild));
 assertTrue(parent.childNodes().contains(newChild));
 }

 @Test
 public void testClone() {
 Node originalNode = new Element("div");
 Node clonedNode = originalNode.clone();
 assertNotSame(originalNode, clonedNode);
 }

 @Test
 public void testEquals() {
 Node originalNode = new Element("div");
 Node clonedNode = originalNode.clone();
 assertTrue(originalNode.equals(clonedNode));
 }

 @Test
 public void testHashCode() {
 Node originalNode = new Element("div");
 Node clonedNode = originalNode.clone();
 int originalHashCode = originalNode.hashCode();
 int clonedHashCode = clonedNode.hashCode();
 assertEquals(originalHashCode, clonedHashCode);
 }
}
import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;

public class NodeTest {

 @Test
 public void testNodeConstruction() {
 Node node = new Node("http://example.com", new Attributes());
 Assert.assertEquals("http://example.com", node.baseUri());
 Assert.assertNotNull(node.attributes());
 }

 @Test
 public void testNodeWithNullBaseUriAndAttributes() {
 Node node = new Node();
 Assert.assertNull(node.baseUri());
 Assert.assertNull(node.attributes());
 }

 @Test
 public void testNodeWithEmptyListChildren() {
 Node node = new Node("http://example.com", new Attributes());
 List<Node> childNodes = node.childNodes();
 Assert.assertEquals(Collections.EMPTY_LIST, childNodes);
 }

 @Test
 public void testNodeGetNodeName() {
 Node node = new Element("div");
 Assert.assertEquals("div", node.nodeName());
 }

 @Test(expected = RuntimeException.class)
 public void testNodeEmptyConstructor() {
 Node node = new Node();
 }

 @Test
 public void testAttrMethod() {
 Node node = new Element("a", new Attributes().put("href", "http://example.com"));
 String href = node.attr("href");
 Assert.assertEquals("http://example.com", href);
 }

 @Test
 public void testAbsUrlMethod() {
 Node node = new Element("a", new Attributes().put("href", "/path/to/page.html"));
 String absUrl = node.absUrl("href");
 String expectedAbsUrl = "http://example.com/path/to/page.html";
 Assert.assertEquals(expectedAbsUrl, absUrl);
 }

 @Test
 public void testAbsUrlWithAbsoluteUrl() {
 Node node = new Element("a", new Attributes().put("href", "http://example.com/path/to/page.html"));
 String absUrl = node.absUrl("href");
 Assert.assertEquals("http://example.com/path/to/page.html", absUrl);
 }

 @Test
 public void testGetChildNodeByIndex() {
 Node document = new Document();
 Node child1 = new Element("p", new Attributes());
 Node child2 = new Element("p", new Attributes());
 document.addChildren(child1, child2);
 Node child = document.childNode(1);
 Assert.assertEquals(child2, child);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetChildNodeByInvalidIndex() {
 Node document = new Document();
 Node child1 = new Element("p", new Attributes());
 Node child2 = new Element("p", new Attributes());
 document.addChildren(child1);
 Node child = document.childNode(1);
 }

 @Test
 public void testGetChildrenList() {
 Node document = new Document();
 Node child1 = new Element("p", new Attributes());
 Node child2 = new Element("p", new Attributes());
 document.addChildren(child1, child2);
 List<Node> children = document.childNodes();
 Assert.assertEquals(2, children.size());
 Assert.assertTrue(children.contains(child1));
 Assert.assertTrue(children.contains(child2));
 }

 @Test
 public void testRemoveChild() {
 Node document = new Document();
 Node child1 = new Element("p", new Attributes());
 Node child2 = new Element("p", new Attributes());
 document.addChildren(child1, child2);
 document.removeChild(child1);
 List<Node> children = document.childNodes();
 Assert.assertEquals(1, children.size());
 Assert.assertFalse(children.contains(child1));
 }

 @Test
 public void testSetBaseUri() {
 Node node = new Element("a", new Attributes().put("href", "/path/to/page.html"));
 node.setBaseUri("http://example.com");
 String absUrl = node.absUrl("href");
 String expectedAbsUrl = "http://example.com/path/to/page.html";
 Assert.assertEquals(expectedAbsUrl, absUrl);
 }

 @Test
 public void testHasAttr() {
 Node node = new Element("a", new Attributes().put("href", "/path/to/page.html").put("class", "example"));
 assertTrue(node.hasAttr("href"));
 assertTrue(node.hasAttr("class"));
 }

 @Test
 public void testHasAttrWithInvalidKey() {
 Node node = new Element("a", new Attributes().put("href", "/path/to/page.html"));
 assertFalse(node.hasAttr("invalidKey"));
 }

 @Test
 public void testSetAttr() {
 Node node = new Element("a");
 node.attr("href", "/path/to/page.html");
 String href = node.attr("href");
 Assert.assertEquals("/path/to/page.html", href);
 }

 @Test
 public void testRemoveAttr() {
 Node node = new Element("a", new Attributes().put("href", "/path/to/page.html"));
 node.removeAttr("href");
 String href = node.attr("href");
 Assert.assertEquals("", href);
 }

 @Test
 public void testGetBaseUri() {
 Node node = new Element("a", new Attributes().put("href", "/path/to/page.html"));
 node.setBaseUri("http://example.com");
 String baseUri = node.baseUri();
 Assert.assertEquals("http://example.com", baseUri);
 }

 @Test
 public void testSetParentNode() {
 Node parent = new Document();
 Node child = new Element("p");
 child.setParentNode(parent);
 Node grandParent = new Document();
 child.setParentNode(grandParent);
 Assert.assertNull(child.parent());
 Assert.assertEquals(grandParent, child.parent());
 }

 @Test
 public void testReplaceChild() {
 Node document = new Document();
 Node oldChild = new Element("p", new Attributes().put("id", "oldChild"));
 Node newChild = new Element("p", new Attributes().put("id", "newChild"));
 document.addChildren(oldChild);
 document.replaceChild(oldChild, newChild);
 Assert.assertEquals(newChild, document.childNode(0));
 Assert.assertNull(document.childNode(1));
 }

 @Test
 public void testRemove() {
 Node document = new Document();
 Node child = new Element("p");
 document.addChildren(child);
 document.remove();
 Assert.assertEquals(Collections.EMPTY_LIST, document.childNodes());
 }

 @Test
 public void testReplaceWith() {
 Node document = new Document();
 Node oldChild = new Element("p", new Attributes().put("id", "oldChild"));
 Node newChild = new Element("p", new Attributes().put("id", "newChild"));
 document.addChildren(oldChild);
 document.replaceWith(newChild);
 Assert.assertEquals(newChild, document.childNode(0));
 Assert.assertNull(document.childNode(1));
 }

 @Test
 public void testAddChildren() {
 Node document = new Document();
 Node child1 = new Element("p", new Attributes().put("id", "child1"));
 Node child2 = new Element("p", new Attributes().put("id", "child2"));
 document.addChildren(child1, child2);
 List<Node> children = document.childNodes();
 Assert.assertEquals(2, children.size());
 Assert.assertTrue(children.contains(child1));
 Assert.assertTrue(children.contains(child2));
 }

 @Test
 public void testAddChildrenWithInvalidElement() {
 Node document = new Document();
 Node child = new Attributes();
 document.addChildren(child);
 List<Node> children = document.childNodes();
 Assert.assertEquals(0, children.size());
 }

 @Test
 public void testAddChildrenWithNullElements() {
 Node document = new Document();
 Node[] children = new Node[0];
 document.addChildren(children);
 List<Node> nodes = document.childNodes();
 Assert.assertEquals(0, nodes.size());
 }

 @Test
 public void testToString() {
 Node node = new Element("p");
 String expectedToString = "<p></p>";
 Assert.assertEquals(expectedToString, node.toString());
 }
}
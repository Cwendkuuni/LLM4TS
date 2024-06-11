import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.AttributesFactory;
import org.jsoup.helper.StringUtil;
import static org.junit.Assert.*;

public class NodeTest {

 @Test
 public void testNodeConstructor() {
 Node node = new Node("http://example.com", new Attributes());
 assertEquals("http://example.com", node.baseUri());
 assertNotNull(node.attributes());
 assertTrue(node.childNodes().isEmpty());
 }

 @Test
 public void testNodeAbstractConstructor() {
 Node node = new Node();
 assertTrue(node.childNodes().isEmpty());
 assertNull(node.attributes());
 }

 @Test
 public void testNodeGetNodeName() {
 Node node = new Element("div");
 assertEquals("div", node.nodeName());
 }

 @Test(expected = AssertionError.class)
 public void testNodeGetNodeNameFail() {
 Node node = new Node();
 try {
 assertEquals("notDiv", node.nodeName());
 } catch (AssertionError e) {
 fail("Should not have thrown an AssertionError");
 }
 }

 @Test
 public void testAttrMethod() {
 Node node = new Element("a");
 node.attr("href", "http://example.com");
 assertEquals("http://example.com", node.attr("href"));
 assertFalse(node.hasAttr("class"));
 node.attr("class", "example");
 assertTrue(node.hasAttr("class"));
 assertNotNull(node.attr("class"));
 assertEquals("example", node.attr("class"));
 }

 @Test
 public void testAbsUrlMethod() {
 Node node = new Element("a", new Attributes().put("href", "about"));
 assertEquals("about", node.absUrl("href"));
 assertEquals("", node.absUrl("nonExistentAttr"));
 }

 @Test
 public void testChildNodeMethod() {
 Node parentNode = new Element("ul");
 Node childNode = new Element("li");
 parentNode.addChildren(childNode);
 Node retrievedChild = parentNode.childNode(0);
 assertEquals(childNode, retrievedChild);
 }

 @Test
 public void testChildNodesMethod() {
 Node parentNode = new Element("ul");
 Node childNode1 = new Element("li");
 Node childNode2 = new Element("li");
 parentNode.addChildren(childNode1, childNode2);
 List<Node> childNodes = parentNode.childNodes();
 assertEquals(2, childNodes.size());
 assertEquals(childNode1, childNodes.get(0));
 assertEquals(childNode2, childNodes.get(1));
 }

 @Test
 public void testRemoveChildMethod() {
 Node parentNode = new Element("ul");
 Node childNode = new Element("li");
 parentNode.addChildren(childNode);
 parentNode.removeChild(childNode);
 List<Node> childNodes = parentNode.childNodes();
 assertTrue(childNodes.isEmpty());
 }

 @Test
 public void testAddChildrenMethod() {
 Node parentNode = new Element("ul");
 Node childNode1 = new Element("li", new Attributes().put("class", "child1"));
 Node childNode2 = new Element("li", new Attributes().put("class", "child2"));
 parentNode.addChildren(childNode1, childNode2);
 List<Node> childNodes = parentNode.childNodes();
 assertEquals(2, childNodes.size());
 assertEquals(childNode1, childNodes.get(0));
 assertEquals(childNode2, childNodes.get(1));
 }

 @Test
 public void testAttributesMethod() {
 Node node = new Element("a", new Attributes().put("href", "http://example.com").put("class", "example"));
 Attributes attributes = node.attributes();
 assertEquals("http://example.com", attributes.get("href"));
 assertEquals("example", attributes.get("class"));
 assertTrue(attributes.hasKey("href"));
 assertTrue(attributes.hasKey("class"));
 }

 @Test
 public void testSetBaseUriMethod() {
 Node node = new Element("a", new Attributes().put("href", "about"));
 node.setBaseUri("https://example.com");
 assertEquals("https://example.comabout", node.absUrl("href"));
 }

 @Test(expected = MalformedURLException.class)
 public void testSetBaseUriMalformedUrlException() {
 Node node = new Element("a", new Attributes().put("href", "about"));
 try {
 node.setBaseUri("http://example.com//");
 } catch (MalformedURLException e) {
 // expected
 }
 }

 @Test
 public void testHasAttrMethod() {
 Node node = new Element("a", new Attributes().put("href", "about").put("class", "example"));
 assertTrue(node.hasAttr("href"));
 assertTrue(node.hasAttr("class"));
 assertFalse(node.hasAttr("nonExistentAttr"));
 }

 @Test
 public void testRemoveAttrMethod() {
 Node node = new Element("a", new Attributes().put("href", "http://example.com").put("class", "example"));
 node.removeAttr("class");
 assertFalse(node.hasAttr("class"));
 assertNull(node.attr("class"));
 }

 @Test
 public void testCloneMethod() {
 Node originalNode = new Element("div", new Attributes().put("id", "original"));
 Node clonedNode = originalNode.clone();
 assertEquals(originalNode, clonedNode.clone()); // test for identity
 assertNotSame(originalNode, clonedNode); // test for distinctness
 }

 @Test
 public void testCloneWithParentMethod() {
 Node parentNode = new Document();
 Node originalNode = new Element("div", new Attributes().put("id", "original"), parentNode);
 Node clonedNode = originalNode.clone();
 Node clonedParentNode = new Document();
 clonedNode.setParentNode(clonedParentNode);
 assertEquals(originalNode, clonedNode.clone()); // test for identity
 assertNotSame(originalNode, clonedNode); // test for distinctness
 }

 @Test
 public void testCloneOrphanMethod() {
 Node originalNode = new Element("div", new Attributes().put("id", "original"));
 Node clonedNode = originalNode.clone();
 assertEquals(originalNode, clonedNode.clone()); // test for identity
 assertNull(clonedNode.parentNode());
 }

 @Test
 public void testSetParentNodeMethod() {
 Node childNode = new Element("li");
 Node parentNode = new Element("ul");
 childNode.setParentNode(parentNode);
 assertEquals(parentNode, childNode.parentNode());
 }

 @Test
 public void testReplaceChildMethod() {
 Node parentNode = new Element("ul");
 Node oldChildNode = new Element("li");
 Node newChildNode = new Element("li");
 parentNode.addChildren(oldChildNode);
 parentNode.replaceChild(oldChildNode, newChildNode);
 List<Node> childNodes = parentNode.childNodes();
 assertEquals(1, childNodes.size());
 assertEquals(newChildNode, childNodes.get(0));
 }

 @Test
 public void testRemoveMethod() {
 Node parentNode = new Element("ul");
 Node childNode = new Element("li");
 parentNode.addChildren(childNode);
 parentNode.remove();
 assertTrue(parentNode.isEmpty());
 }

 @Test
 public void testReplaceWithMethod() {
 Node parentNode = new Element("ul");
 Node oldChildNode = new Element("li");
 Node newChildNode = new Element("li");
 parentNode.addChildren(oldChildNode);
 parentNode.replaceWith(newChildNode);
 List<Node> childNodes = parentNode.childNodes();
 assertEquals(1, childNodes.size());
 assertEquals(newChildNode, childNodes.get(0));
 }
}
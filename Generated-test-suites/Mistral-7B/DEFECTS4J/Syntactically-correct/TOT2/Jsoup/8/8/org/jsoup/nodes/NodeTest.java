import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

 @Test
 public void testNodeCreation() {
 Node node = new Element("div");
 assertEquals("div", node.nodeName());
 assertNull(node.parentNode());
 assertNull(node.attributes());
 assertNull(node.baseUri());
 assertFalse(node.hasAttr("class"));
 assertEquals("", node.attr("class"));
 }

 @Test
 public void testNodeWithAttributes() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("id", "myId");
 attributes.put("class", "myClass");
 Node node = new Element("div", baseUri, attributes);
 assertEquals("div", node.nodeName());
 assertEquals(baseUri, node.baseUri());
 assertTrue(node.hasAttr("id"));
 assertEquals("myId", node.attr("id"));
 assertTrue(node.hasAttr("class"));
 assertEquals("myClass", node.attr("class"));
 }

 @Test
 public void testNodeWithChildren() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 Node parentNode = new Element("div", baseUri, attributes);
 Node childNode1 = new Element("p", baseUri, attributes);
 Node childNode2 = new Element("p", baseUri, attributes);
 parentNode.addChildren(childNode1, childNode2);
 assertEquals(2, parentNode.childNodes().size());
 assertEquals(childNode1, parentNode.childNode(0));
 assertEquals(childNode2, parentNode.childNode(1));
 }

 @Test
 public void testNodeWithBaseUri() {
 String baseUri = "http://example.com";
 String srcAttribute = "http://subexample.com/image.jpg";
 Node node = new Element("img", baseUri, new Attributes().put("src", srcAttribute));
 assertEquals("img", node.nodeName());
 assertEquals("http://example.com", node.baseUri());
 assertEquals("http://subexample.com/image.jpg", node.absUrl("src"));
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 String baseUri = null;
 Node node = new Element("div", baseUri, new Attributes());
 }

 @Test(expected = NullPointerException.class)
 public void testNullAttributes() {
 String baseUri = "http://example.com";
 Node node = new Element("div", baseUri, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeSizeChildNodesList() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 Node node = new Element("div", baseUri, attributes);
 node.childNodes = new ArrayList<>(Collections.nCopies(-1, new Element("p", baseUri, attributes)));
 }

 @Test
 public void testRemoveChild() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 Node parentNode = new Element("div", baseUri, attributes);
 Node childNode1 = new Element("p", baseUri, attributes);
 Node childNode2 = new Element("p", baseUri, attributes);
 parentNode.addChildren(childNode1, childNode2);
 parentNode.removeChild(childNode1);
 assertEquals(1, parentNode.childNodes().size());
 assertNull(parentNode.childNode(0));
 }

 @Test
 public void testSetParentNode() {
 String baseUri = "http://example.com";
 String childUri = "http://child.example.com";
 Attributes attributes = new Attributes();
 Node parentNode = new Element("div", baseUri, attributes);
 Node childNode = new Element("p", childUri, attributes);
 childNode.setParentNode(parentNode);
 assertEquals(parentNode, childNode.parentNode());
 }

 @Test
 public void testReplaceChild() {
 String baseUri = "http://example.com";
 String child1Uri = "http://child1.example.com";
 String child2Uri = "http://child2.example.com";
 Attributes attributes = new Attributes();
 Node parentNode = new Element("div", baseUri, attributes);
 Node child1 = new Element("p", child1Uri, attributes);
 Node child2 = new Element("p", child2Uri, attributes);
 parentNode.addChildren(child1, child2);
 parentNode.replaceChild(child1, child2);
 assertEquals(child2, parentNode.childNode(0));
 assertEquals(child1, parentNode.childNode(1));
 }

 @Test
 public void testAddChildren() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 Node parentNode = new Element("div", baseUri, attributes);
 Node child1 = new Element("p", baseUri, attributes);
 Node child2 = new Element("p", baseUri, attributes);
 parentNode.addChildren(child1, child2);
 assertEquals(2, parentNode.childNodes().size());
 assertEquals(child1, parentNode.childNode(0));
 assertEquals(child2, parentNode.childNode(1));
 }

 @Test
 public void testAddChildrenWithTraversal() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 Node parentNode = new Element("div", baseUri, attributes);
 Node child1 = new Element("p", baseUri, attributes);
 Node child2 = new Element("p", baseUri, attributes);
 Node child3 = new Element("p", baseUri, attributes);
 parentNode.addChildren(child1, child2, child3);
 assertEquals(3, parentNode.childNodes().size());
 assertEquals(child1, parentNode.childNode(0));
 assertEquals(child2, parentNode.childNode(1));
 assertEquals(child3, parentNode.childNode(2));
 }

 @Test
 public void testRemove() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 Node parentNode = new Element("div", baseUri, attributes);
 Node childNode = new Element("p", baseUri, attributes);
 parentNode.addChildren(childNode);
 parentNode.remove();
 assertNull(parentNode);
 }

 @Test
 public void testReplaceWith() {
 String baseUri = "http://example.com";
 String oldChildText = "Old child text";
 String newChildText = "New child text";
 Attributes attributes = new Attributes();
 Node parentNode = new Element("div", baseUri, attributes);
 Node oldChild = new TextNode(oldChildText);
 Node newChild = new TextNode(newChildText);
 parentNode.addChildren(oldChild);
 parentNode.replaceWith(newChild);
 assertEquals(newChildText, parentNode.childNode(0).text());
 }
}
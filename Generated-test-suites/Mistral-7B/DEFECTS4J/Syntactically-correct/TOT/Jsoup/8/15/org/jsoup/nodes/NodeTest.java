import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.AttributesImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

 @Test
 public void testNodeCreation() {
 Node node = new Element("div");
 assertEquals("div", node.nodeName());
 assertNull(node.parentNode());
 assertNotNull(node.attributes());
 assertFalse(node.hasAttr("class"));
 }

 @Test
 public void testNodeCreationWithAttributes() {
 String baseUri = "https://example.com";
 AttributesImpl attributes = new AttributesImpl();
 attributes.put("class", "example");
 Node node = new Element("div", baseUri, attributes);
 assertEquals("div", node.nodeName());
 assertEquals(baseUri, node.baseUri());
 assertEquals("example", node.attr("class"));
 assertFalse(node.hasAttr("id"));
 }

 @Test
 public void testNodeCreationWithoutBaseUriOrAttributes() {
 Node node = new Element("div");
 assertEquals("div", node.nodeName());
 assertNull(node.parentNode());
 assertNotNull(node.attributes());
 assertTrue(node.attributes().isEmpty());
 }

 @Test
 public void testNodeCreationWithEmptyList() {
 Node node = new Element("div");
 List<Node> childNodes = node.childNodes();
 assertTrue(childNodes.isEmpty());
 }

 @Test
 public void testAbsUrl() throws Exception {
 Node node = new Element("a", "http://example.com");
 node.attributes().put("href", "about.html");
 assertEquals("http://example.com/about.html", node.absUrl("href"));
 node.attributes().put("href", "http://example2.com/about.html");
 assertEquals("http://example2.com/about.html", node.absUrl("href"));
 }

 @Test(expected = MalformedURLException.class)
 public void testAbsUrlMalformedBaseUri() throws Exception {
 Node node = new Element("a", "example.com");
 node.attributes().put("href", "/about.html");
 node.absUrl("href");
 }

 @Test(expected = MalformedURLException.class)
 public void testAbsUrlMalformedRelativeUrl() throws Exception {
 Node node = new Element("a", "http://example.com");
 node.attributes().put("href", "about://example2.com");
 node.absUrl("href");
 }

 @Test
 public void testChildNodes() {
 Node parent = new Element("div");
 Node child1 = new Element("p");
 Node child2 = new Element("p");
 parent.addChildren(child1, child2);
 List<Node> children = parent.childNodes();
 assertEquals(2, children.size());
 assertEquals(child1, children.get(0));
 assertEquals(child2, children.get(1));
 }

 @Test
 public void testRemoveChild() {
 Node parent = new Element("div");
 Node child1 = new Element("p");
 Node child2 = new Element("p");
 parent.addChildren(child1, child2);
 parent.removeChild(child1);
 List<Node> children = parent.childNodes();
 assertEquals(1, children.size());
 assertNotSame(child1, children.get(0));
 assertSame(child2, children.get(0));
 }

 @Test
 public void testAddChildren() {
 Node parent = new Element("div");
 Node child1 = new Element("p");
 Node child2 = new Element("p");
 parent.addChildren(child1, child2);
 List<Node> children = parent.childNodes();
 assertEquals(2, children.size());
 assertEquals(child1, children.get(0));
 assertEquals(child2, children.get(1));
 }

 @Test
 public void testSetParentNode() {
 Node child = new Element("p");
 Node parent = new Element("div");
 child.setParentNode(parent);
 assertEquals(parent, child.parentNode());
 }

 @Test
 public void testHasAttr() {
 Node node = new Element("div");
 node.attributes().put("class", "example");
 assertTrue(node.hasAttr("class"));
 assertTrue(node.hasAttr("class", "example"));
 assertFalse(node.hasAttr("id"));
 }

 @Test
 public void testSetAttr() {
 Node node = new Element("div");
 node.setAttr("class", "example");
 assertEquals("example", node.attr("class"));
 }

 @Test
 public void testRemoveAttr() {
 Node node = new Element("div");
 node.attributes().put("class", "example");
 node.removeAttr("class");
 assertFalse(node.hasAttr("class"));
 }

 @Test
 public void testAttrWithEmptyString() {
 Node node = new Element("div");
 node.setAttr("class", "");
 assertFalse(node.hasAttr("class"));
 }

 @Test
 public void testAttrWithNull() {
 Node node = new Element("div");
 node.setAttr("class", null);
 assertFalse(node.hasAttr("class"));
 }

 @Test
 public void testAttributesToString() {
 Node node = new Element("div");
 node.attributes().put("class", "example");
 node.attributes().put("id", "example");
 assertEquals("class=\"example\" id=\"example\"", node.attributes().toString());
 }

 @Test
 public void testTextNodeCreation() {
 Node node = new TextNode("Hello World");
 assertEquals("Hello World", node.outerHtml());
 }
}
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class NodeTest {

 @Test
 public void testNodeCreation() {
 Node node = new Element("div");
 assertNotNull(node);
 assertEquals("div", node.nodeName());
 assertNull(node.parentNode());
 assertNull(node.childNodes());
 assertNull(node.attributes());
 }

 @Test
 public void testNodeWithAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("class", "test-class");
 Node node = new Element("div").attrs(attributes);
 assertEquals("div", node.nodeName());
 assertEquals("test-class", node.attr("class"));
 }

 @Test
 public void testAbsUrl() {
 Node node = new Element("a").attr("href", "http://example.com");
 assertEquals("http://example.com", node.absUrl("href"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbsUrlNullAttribute() {
 Node node = new Element("a");
 node.absUrl(null);
 }

 @Test
 public void testAbsUrlEmptyAttribute() {
 Node node = new Element("a").attr("href", "");
 assertEquals("", node.absUrl("href"));
 }

 @Test
 public void testAbsUrlRelativeUrl() {
 Node node = new Element("a").attr("href", "relative.html");
 Node parent = new Document();
 parent.appendElement(node);
 parent.setBaseUri("http://example.com");
 assertEquals("http://example.com/relative.html", node.absUrl("href"));
 }

 @Test
 public void testAbsUrlBaseUri() {
 Node node = new Element("a").attr("href", "base.html");
 Node parent = new Document();
 parent.appendElement(node);
 parent.setBaseUri("/base/path");
 assertEquals("/base/path/base.html", node.absUrl("href"));
 }

 @Test
 public void testAbsUrlMalformedURL() {
 Node node = new Element("a").attr("href", "http://example.com/page.html");
 Node parent = new Document();
 parent.appendElement(node);
 parent.setBaseUri("http://example.com");
 assertNull(node.absUrl("href"));
 }

 @Test
 public void testChildNodes() {
 Node parent = new Element("div");
 Node child1 = new Element("p").text("Child 1");
 Node child2 = new Element("p").text("Child 2");
 parent.appendChildren(child1, child2);
 List<Node> children = parent.childNodes();
 assertEquals(2, children.size());
 assertEquals(child1, children.get(0));
 assertEquals(child2, children.get(1));
 }

 @Test
 public void testParentNode() {
 Node child = new Element("p").text("Child");
 Node parent = new Element("div").appendChild(child);
 assertEquals(child, parent.childNodes().get(0));
 assertEquals(parent, child.parentNode());
 }

 @Test
 public void testParentNodeNull() {
 Node node = new Element("div");
 assertNull(node.parentNode());
 }

 @Test
 public void testRemoveChild() {
 Node parent = new Element("div");
 Node child = new Element("p").text("Child");
 parent.appendChild(child);
 parent.removeChild(child);
 List<Node> children = parent.childNodes();
 assertFalse(children.contains(child));
 }

 @Test
 public void testReplaceChild() {
 Node parent = new Element("div");
 Node oldChild = new Element("p").text("Old Child");
 Node newChild = new Element("p").text("New Child");
 parent.appendChild(oldChild);
 parent.replaceChild(oldChild, newChild);
 List<Node> children = parent.childNodes();
 assertEquals(1, children.size());
 assertEquals(newChild, children.get(0));
 }

 @Test
 public void testSetParentNode() {
 Node child = new Element("p").text("Child");
 Node parent1 = new Element("div").appendChild(child);
 child.setParentNode(null);
 assertNull(child.parentNode());
 Node parent2 = new Element("div");
 child.setParentNode(parent2);
 assertEquals(child, parent2.childNodes().get(0));
 }

 @Test
 public void testSetSiblingIndex() {
 Node first = new Element("p").text("First");
 Node second = new Element("p").text("Second");
 Node parent = new Element("div");
 parent.appendChildren(first, second);
 first.setSiblingIndex(0);
 second.setSiblingIndex(1);
 List<Node> children = parent.childNodes();
 assertEquals(0, first.siblingIndex());
 assertEquals(1, second.siblingIndex());
 }

 @Test
 public void testGetTextContent() {
 Node node = new Element("p").text("Hello World!");
 assertEquals("Hello World!", node.text());
 }

 @Test
 public void testGetTextContentEmpty() {
 Node node = new Element("p");
 assertEquals("", node.text());
 }

 @Test
 public void testGetTextContentWithChildren() {
 Node node = new Element("p").appendChildren(new TextNode("Text "), new Element("b").text("Child"));
 assertEquals("Text ", node.textBeforeChildren());
 assertEquals("Child", node.textAfterChildren());
 assertEquals("Text Child", node.text());
 }
}
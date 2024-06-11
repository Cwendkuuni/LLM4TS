import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class NodeTest {

 @Test
 public void testNodeCreation() {
 Node node = new Node("http://example.com", new Attributes());
 assertNotNull(node);
 assertNotNull(node.baseUri());
 assertNotNull(node.attributes());
 assertEquals("", node.nodeName());
 assertNotNull(node.childNodes());
 assertSame(Collections.EMPTY_LIST, node.childNodes());
 }

 @Test
 public void testNodeCreationWithNullBaseUri() {
 try {
 Node node = new Node(null, new Attributes());
 fail("Expected exception: NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testNodeCreationWithNullAttributes() {
 Node node = new Node("http://example.com", null);
 assertNotNull(node);
 assertNotNull(node.baseUri());
 assertNull(node.attributes());
 }

 @Test
 public void testNodeCreationWithEmptyAttributes() {
 Node node = new Node("http://example.com", new Attributes());
 assertNotNull(node);
 assertNotNull(node.baseUri());
 assertNotNull(node.attributes());
 assertEquals(Collections.emptyMap(), node.attributes());
 }

 @Test
 public void testNodeCreationNullParent() {
 Node parent = null;
 Node node = new Node("http://example.com", new Attributes());
 node.setParentNode(parent);
 assertNull(node.parentNode());
 }

 @Test
 public void testNodeSetParentNode() {
 Document doc = new Document();
 Node parent = doc.createElement("parent");
 Node node = new Node("http://example.com", new Attributes());
 node.setParentNode(parent);
 assertSame(parent, node.parentNode());
 }

 @Test
 public void testNodeToString() {
 Node node = new Node("http://example.com", new Attributes());
 assertEquals(node.toString(), node.outerHtml());
 }

 @Test
 public void testNodeOuterHtmlWithChildren() throws MalformedURLException {
 Document doc = new Document();
 Element parent = doc.createElement("parent");
 doc.appendChild(parent);
 Element child1 = doc.createElement("child1");
 child1.setAttribute("attr1", "value1");
 parent.appendChild(child1);
 Element child2 = doc.createElement("child2");
 parent.appendChild(child2);
 Node node = parent;
 StringBuilder accum = new StringBuilder();
 node.outerHtml(accum);
 String expected = "<parent><child1 attr1=\"value1\"></child1><child2></child2></parent>";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testNodeOuterHtmlWithTextNode() throws MalformedURLException {
 Document doc = new Document();
 Element parent = doc.createElement("parent");
 doc.appendChild(parent);
 TextNode textNode = doc.createTextNode("Hello World!");
 parent.appendChild(textNode);
 Node node = parent;
 StringBuilder accum = new StringBuilder();
 node.outerHtml(accum);
 String expected = "<parent>" + TextNode.Text("Hello World!") + "</parent>";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testNodeAbsUrlEmptyAttribute() {
 Node node = new Element("a", new Attributes());
 node.setAttribute("href", "");
 assertEquals("", node.absUrl("href"));
 }

 @Test
 public void testNodeAbsUrlRelativeUrl() throws MalformedURLException {
 Node node = new Element("a", new Attributes());
 node.setAttribute("href", "foo.html");
 URL base = new URL("http://example.com/bar/baz/");
 node.setBaseUri(base.toString());
 assertEquals("http://example.com/bar/baz/foo.html", node.absUrl("href"));
 }

 @Test
 public void testNodeAbsUrlAbsoluteUrl() throws MalformedURLException {
 Node node = new Element("a", new Attributes());
 node.setAttribute("href", "http://example.com/foo.html");
 URL base = new URL("http://example.com/bar/baz/");
 node.setBaseUri(base.toString());
 assertEquals("http://example.com/foo.html", node.absUrl("href"));
 }

 @Test
 public void testNodeAbsUrlNullBaseUri() {
 Node node = new Element("a", new Attributes());
 node.setAttribute("href", "http://example.com/foo.html");
 node.setBaseUri(null);
 assertEquals("http://example.com/foo.html", node.absUrl("href"));
 }

 @Test
 public void testNodeAbsUrlMissingAttribute() {
 Node node = new Element("a", new Attributes());
 assertEquals("", node.absUrl("href"));
 }

 @Test
 public void testNodeAbsUrlInvalidUrl() {
 Node node = new Element("a", new Attributes());
 node.setAttribute("href", "foo:/bar");
 assertEquals("", node.absUrl("href"));
 }

 @Test
 public void testNodeHasAttr() {
 Node node = new Element("a", new Attributes());
 node.setAttribute("href", "http://example.com");
 assertTrue(node.hasAttr("href"));
 assertFalse(node.hasAttr("foo"));
 }

 @Test
 public void testNodeGetAttribute() {
 Node node = new Element("a", new Attributes());
 node.setAttribute("href", "http://example.com");
 assertEquals("http://example.com", node.attr("href"));
 }

 @Test
 public void testNodeGetAttributeWithPrefix() {
 Node node = new Element("a", new Attributes());
 node.setAttribute("abs:href", "http://example.com");
 assertEquals("http://example.com", node.attr("abs:href"));
 }

 @Test
 public void testNodeGetAttributeMissing() {
 Node node = new Element("a", new Attributes());
 assertEquals("", node.attr("foo"));
 }

 @Test
 public void testNodeGetAttributeNull() {
 Node node = new Element("a", null);
 assertEquals("", node.attr("foo"));
 }

 @Test
 public void testNodeSetAttribute() {
 Node node = new Element("a", new Attributes());
 node.setAttribute("href", "http://example.com");
 assertEquals("http://example.com", node.attr("href"));
 node.attr("href", "http://example.org");
 assertEquals("http://example.org", node.attr("href"));
 }

 @Test
 public void testNodeRemoveAttribute() {
 Node node = new Element("a", new Attributes());
 node.setAttribute("href", "http://example.com");
 node.removeAttr("href");
 assertEquals("", node.attr("href"));
 }

 @Test
 public void testNodeSetBaseUri() {
 Node node = new Node("http://example.com", new Attributes());
 node.setBaseUri("http://example.org/");
 assertEquals("http://example.org", node.baseUri());
 }

 @Test
 public void testNodeRemoveChild() {
 Document doc = new Document();
 Element parent = doc.createElement("parent");
 doc.appendChild(parent);
 Element child1 = doc.createElement("child1");
 Element child2 = doc.createElement("child2");
 parent.appendChild(child1);
 parent.appendChild(child2);
 parent.removeChild(child1);
 assertEquals(1, parent.childNodes().size());
 assertSame(child2, parent.childNodes().get(0));
 }

 @Test
 public void testNodeAddChildren() {
 Document doc = new Document();
 Node parent = doc.createElement("parent");
 List<Node> children = new ArrayList<>();
 children.add(doc.createElement("child1"));
 children.add(doc.createElement("child2"));
 parent.addChildren(children.toArray(new Node[children.size()]));
 assertEquals(2, parent.childNodes().size());
 }

 @Test
 public void testNodeAddChildrenWithNullParent() {
 List<Node> children = new ArrayList<>();
 children.add(new Element("child1"));
 children.add(new Element("child2"));
 try {
 children.get(0).addChildren(children.toArray(new Node[children.size()]));
 fail("Expected exception: IllegalStateException");
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testNodeAddChildrenWithNullChild() {
 Node parent = new Element("parent");
 List<Node> children = new ArrayList<>();
 Node child = null;
 try {
 parent.addChildren(child);
 fail("Expected exception: NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testNodeAddChildrenWithDuplicateChild() {
 Node parent = new Element("parent");
 Element child = new Element("child");
 parent.addChildren(child);
 parent.addChildren(child);
 assertEquals(1, parent.childNodes().size());
 }

 @Test
 public void testNodeReplaceChild() {
 Document doc = new Document();
 Element parent = doc.createElement("parent");
 doc.appendChild(parent);
 Element child1 = doc.createElement("child1");
 Element child2 = doc.createElement("child2");
 parent.appendChild(child1);
 parent.replaceChild(child1, child2);
 assertEquals(1, parent.childNodes().size());
 assertSame(child2, parent.childNodes().get(0));
 }

 @Test
 public void testNodeReplaceChildWithNullChild() {
 Document doc = new Document();
 Element parent = doc.createElement("parent");
 doc.appendChild(parent);
 Element child1 = doc.createElement("child1");
 parent.appendChild(child1);
 parent.replaceChild(null, child1);
 assertEquals(1, parent.childNodes().size());
 assertSame(child1, parent.childNodes().get(0));
 }

 @Test
 public void testNodeReplaceChildWithDifferentParentChild() {
 Document doc1 = new Document();
 Element parent1 = doc1.createElement("parent1");
 doc1.appendChild(parent1);
 Element child1 = doc1.createElement("child1");
 parent1.appendChild(child1);
 Document doc2 = new Document();
 Element parent2 = doc2.createElement("parent2");
 doc2.appendChild(parent2);
 Element child2 = doc2.createElement("child2");
 parent2.appendChild(child2);
 parent1.replaceChild(child1, child2);
 assertEquals(1, parent1.childNodes().size());
 assertSame(child2, parent1.childNodes().get(0));
 assertEquals(1, parent2.childNodes().size());
 assertSame(child1, parent2.childNodes().get(0));
 }

 @Test
 public void testNodeRemove() {
 Document doc = new Document();
 Element parent = doc.createElement("parent");
 doc.appendChild(parent);
 Element child = doc.createElement("child");
 parent.appendChild(child);
 parent.remove();
 assertEquals(0, child.parentNodes().size());
 assertNull(child.parentNode());
 }
}
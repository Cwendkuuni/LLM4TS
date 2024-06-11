import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeTest {

// Test for constructor with baseUri and attributes
@Test
public void testNodeWithBaseUriAndAttributes() {
Node node = new Element("div") {
{
setBaseUri("http://example.com");
setAttributes(new Attributes() {{
put("class", "example");
put("id", "test");
}});
}
};
assertEquals("div", node.nodeName());
assertEquals("http://example.com", node.baseUri());
assertEquals("example", node.attr("class"));
assertEquals("test", node.attr("id"));
}

// Test for constructor without baseUri, attributes, and children
@Test
public void testNodeWithoutBaseUriAndAttributes() {
Node node = new Node();
assertEquals(Collections.EMPTY_LIST, node.childNodes());
assertNull(node.baseUri());
assertNull(node.attributes());
}

// Test for getNodeName() method
@Test
public void testgetNodeName() {
Element div = new Element("div");
assertEquals("div", div.nodeName());
Element p = new Element("p");
assertEquals("p", p.nodeName());
}

// Test for getting and setting attributes
@Test
public void testAttr() {
Element div = new Element("div");
div.attr("class", "example");
assertEquals("example", div.attr("class"));
div.attr("class", "newExample");
assertEquals("newExample", div.attr("class"));
div.removeAttr("class");
assertEquals("", div.attr("class"));
}

// Test for hasAttr() method
@Test
public void testHasAttr() {
Element div = new Element("div");
div.attr("class", "example");
assertTrue(div.hasAttr("class"));
assertTrue(div.hasAttr("id"));
assertFalse(div.hasAttr("nonexistent"));
}

// Test for getAttributes() method
@Test
public void testGetAttributes() {
Element div = new Element("div");
Attributes attributes = div.attributes();
assertEquals(attributes.getClass(), Attributes.class);
assertTrue(attributes.hasKey("class"));
assertEquals("", attributes.get("nonexistent"));
}

// Test for setBaseUri() method
@Test
public void testSetBaseUri() {
Element div = new Element("div");
div.setBaseUri("http://example.com");
assertEquals("http://example.com", div.baseUri());
}

// Test for getChildNode() method
@Test
public void testGetChildNode() {
Element parent = new Element("ul");
Element child = new Element("li");
parent.addChild(child);
Node node = parent.childNode(0);
assertEquals(child, node);
}

// Test for getChildren() method
@Test
public void testGetChildren() {
Element parent = new Element("ul");
Element child1 = new Element("li");
Element child2 = new Element("li");
parent.addChildren(child1, child2);
List<Node> children = parent.childNodes();
assertEquals(2, children.size());
assertEquals(child1, children.get(0));
assertEquals(child2, children.get(1));
}

// Test for removeChild() method
@Test
public void testRemoveChild() {
Element parent = new Element("ul");
Element child1 = new Element("li");
Element child2 = new Element("li");
parent.addChildren(child1, child2);
parent.removeChild(child1);
List<Node> children = parent.childNodes();
assertEquals(1, children.size());
assertEquals(child2, children.get(0));
}

// Test for replaceChild() method
@Test
public void testReplaceChild() {
Element parent = new Element("ul");
Element child1 = new Element("li");
Element child2 = new Element("li");
parent.addChildren(child1, child2);
parent.replaceChild(child1, child2);
List<Node> children = parent.childNodes();
assertEquals(1, children.size());
assertEquals(child2, children.get(0));
}

// Test for addChildren() method
@Test
public void testAddChildren() {
Element parent = new Element("ul");
Element child1 = new Element("li");
Element child2 = new Element("li");
parent.addChildren(child1, child2);
List<Node> children = parent.childNodes();
assertEquals(2, children.size());
assertEquals(child1, children.get(0));
assertEquals(child2, children.get(1));
}

// Test for equals() and hashCode() methods
@Test
public void testEqualsAndHashCode() {
Element e1 = new Element("div");
Element e2 = new Element("div");
assertEquals(e1, e1);
assertNotEquals(e1, e2);
Element e3 = new Element("div");
assertEquals(e1, e3);
assertNotEquals(e1, new Object());
}

// Test for clone() method
@Test
public void testClone() {
Element parent = new Element("ul");
Element child1 = new Element("li");
Element child2 = new Element("li");
parent.addChildren(child1, child2);
Node clonedNode = parent.clone();
assertEquals(2, clonedNode.childNodes().size());
assertEquals(child1, clonedNode.childNodes().get(0));
assertEquals(child2, clonedNode.childNodes().get(1));
}

// Test for outerHtml() method with empty node
@Test
public void testOuterHtmlEmptyNode() {
Element node = new Element("div");
String html = node.outerHtml();
assertEquals("", html);
}

// Test for outerHtml() method with node containing text
@Test
public void testOuterHtmlTextNode() {
Element parent = new Element("div");
TextNode textNode = new TextNode("Hello World");
parent.addChild(textNode);
String html = parent.outerHtml();
assertEquals("Hello World", html);
}

// Test for outerHtml() method with node containing children
@Test
public void testOuterHtmlNodeWithChildren() {
Element parent = new Element("div");
Element child1 = new Element("p");
child1.setText("Child 1");
Element child2 = new Element("p");
child2.setText("Child 2");
parent.addChildren(child1, child2);
String html = parent.outerHtml();
assertEquals("<div><p>Child 1</p><p>Child 2</p></div>", html);
}

// Test for absUrl() method with empty baseUri
@Test
public void testAbsUrlEmptyBaseUri() {
Element node = new Element("a") {{
setBaseUri("");
setAttributes(new Attributes() {{
put("href", "test.html");
}});
}};
assertEquals("", node.absUrl("href"));
}

// Test for absUrl() method with absolute baseUri
@Test
public void testAbsUrlAbsoluteBaseUri() {
Element node = new Element("a") {{
setBaseUri("http://example.com/");
setAttributes(new Attributes() {{
put("href", "relative.html");
}});
}};
assertEquals("http://example.com/relative.html", node.absUrl("href"));
}

// Test for absUrl() method with relative baseUri
@Test
public void testAbsUrlRelativeBaseUri() {
Element node = new Element("a") {{
setBaseUri("/path/to/");
setAttributes(new Attributes() {{
put("href", "relative.html");
}});
}};
assertEquals("/path/to/relative.html", node.absUrl("href"));
}

// Test for absUrl() method with malformed baseUri
@Test(expected = MalformedURLException.class)
public void testAbsUrlMalformedBaseUri() {
Element node = new Element("a") {{
setBaseUri("http://example.com/path/to/");
setAttributes(new Attributes() {{
put("href", "");
}});
}};
node.absUrl("href");
}

// Test for absUrl() method with malformed attribute value
@Test(expected = MalformedURLException.class)
public void testAbsUrlMalformedAttributeValue() {
Element node = new Element("a") {{
setBaseUri("http://example.com/");
setAttributes(new Attributes() {{
put("href", "data:text/html,<html>");
}});
}};
node.absUrl("href");
}
}
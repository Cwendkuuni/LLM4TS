package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class NodeTest {

 private Node root;

 @Before
 public void setup() {
 root = new Element("html", new Attributes().put("baseHref", "http://example.com"));
 }

 @Test
 public void testBasicNodeCreation() {
 Node node = new Node("div", new Attributes());
 assertEquals("div", node.nodeName());
 assertNull(node.parentNode());
 assertNotNull(node.baseUri());
 assertEquals("", node.attr("foo"));
 }

 @Test
 public void testNodeWithAttributes() {
 Node node = new Element("a", new Attributes().put("href", "http://example.com").put("target", "_blank"));
 assertEquals("a", node.nodeName());
 assertEquals("http://example.com", node.absUrl("href"));
 assertEquals("_blank", node.attr("target"));
 }

 @Test
 public void testElementWithChildren() {
 Element parent = new Element("ul");
 Element child1 = new Element("li", new Attributes().put("class", "first"));
 Element child2 = new Element("li", new Attributes().put("class", "second"));
 parent.appendChild(child1);
 parent.appendChild(child2);

 assertEquals(2, parent.childNodes().size());
 assertEquals("li", child1.nodeName());
 assertEquals("li", child2.nodeName());
 assertEquals("first", child1.attributes().get("class"));
 assertEquals("second", child2.attributes().get("class"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 Node node = new Node("div", new Attributes());
 node.setBaseUri(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullAttributes() {
 Node node = new Node("div");
 node.setBaseUri("http://example.com");
 node.setAttributes(null);
 }

 @Test
 public void testAbsUrl() {
 Node link = new Element("a", new Attributes().put("href", "images/image.jpg"));
 assertEquals("images/image.jpg", link.absUrl("href"));

 Node baseLink = new Element("base", new Attributes().put("href", "/test"));
 root.appendChild(baseLink);
 root.appendChild(link);
 assertEquals("/test/images/image.jpg", link.absUrl("href"));
 }

 @Test
 public void testRemoveAttribute() {
 Node node = new Element("input", new Attributes().put("type", "text").put("name", "username"));
 assertEquals("text", node.attr("type"));
 node.removeAttr("type");
 assertEquals("", node.attr("type"));
 }

 @Test
 public void testSetAttribute() {
 Node node = new Element("input", new Attributes());
 node.attr("type", "text");
 node.attr("name", "username");
 assertEquals("text", node.attr("type"));
 assertEquals("username", node.attr("name"));
 }

 @Test
 public void testHasAttribute() {
 Node node = new Element("input", new Attributes().put("type", "text").put("name", "username"));
 assertTrue(node.hasAttr("type"));
 assertTrue(node.hasAttr("name"));
 assertFalse(node.hasAttr("class"));
 }

 @Test
 public void testRemoveChild() {
 Element parent = new Element("ul");
 Element child1 = new Element("li", new Attributes().put("class", "first"));
 Element child2 = new Element("li", new Attributes().put("class", "second"));
 parent.appendChild(child1);
 parent.appendChild(child2);
 parent.removeChild(child1);
 assertEquals(1, parent.childNodes().size());
 }
}
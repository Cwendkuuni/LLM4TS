import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class NodeTest {

 private Node node;
 private Document document;

 @Before
 public void setup() {
 node = new Element("div");
 document = new Document();
 document.appendChild(node);
 }

 // Test for nodeName() method
 @Test
 public void testNodeName() {
 assertEquals("div", node.nodeName());
 }

 // Test for attr() method with valid attribute
 @Test
 public void testAttrWithValidAttribute() {
 node.attr("class", "test");
 assertEquals("test", node.attr("class"));
 }

 // Test for attr() method with invalid attribute key
 @Test(expected = IllegalArgumentException.class)
 public void testAttrWithInvalidAttributeKey() {
 node.attr("invalidKey", "value");
 }

 // Test for attr() method with missing attribute
 @Test
 public void testAttrWithMissingAttribute() {
 assertEmpty(node.attr("nonExistentAttribute"));
 }

 // Test for absUrl() method with absolute URL
 @Test
 public void testAbsUrlWithAbsoluteUrl() throws MalformedURLException {
 node.attr("href", "https://www.example.com");
 assertEquals("https://www.example.com", node.absUrl("href"));
 }

 // Test for absUrl() method with relative URL
 @Test
 public void testAbsUrlWithRelativeUrl() throws MalformedURLException {
 node.attr("href", "/test.html");
 URL base = new URL("https://www.example.com/");
 assertEquals("https://www.example.com/test.html", node.absUrl("href", base));
 }

 // Test for absUrl() method with missing attribute
 @Test
 public void testAbsUrlWithMissingAttribute() {
 assertEmpty(node.absUrl("nonExistentAttribute"));
 }

 // Test for childNode() method
 @Test
 public void testChildNode() {
 Element child = new Element("p");
 node.appendChild(child);
 assertEquals(child, node.childNode(0));
 }

 // Test for childNodes() method
 @Test
 public void testChildNodes() {
 Element child1 = new Element("p");
 Element child2 = new Element("p");
 node.appendChild(child1);
 node.appendChild(child2);
 List<Node> childNodes = node.childNodes();
 assertEquals(2, childNodes.size());
 assertEquals(child1, childNodes.get(0));
 assertEquals(child2, childNodes.get(1));
 }

 // Test for parent() method
 @Test
 public void testParent() {
 Element parent = new Element("body");
 parent.appendChild(node);
 assertEquals(parent, node.parent());
 }

 // Test for parent() method with no parent
 @Test
 public void testParentWithNoParent() {
 assertNull(node.parent());
 }

 // Test for removeChild() method
 @Test
 public void testRemoveChild() {
 Element child = new Element("p");
 node.appendChild(child);
 node.removeChild(child);
 assertTrue(node.childNodes().isEmpty());
 }

 // Test for replaceChild() method
 @Test
 public void testReplaceChild() {
 Element child1 = new Element("p");
 Element child2 = new Element("p");
 node.appendChild(child1);
 node.replaceChild(child2, child1);
 assertEquals(child2, node.childNodes().get(0));
 }

 // Test for addChildren() method
 @Test
 public void testAddChildren() {
 Element child1 = new Element("p");
 Element child2 = new Element("p");
 node.addChildren(child1, child2);
 assertEquals(2, node.childNodes().size());
 assertEquals(child1, node.childNodes().get(0));
 assertEquals(child2, node.childNodes().get(1));
 }

 // Test for hasAttr() method
 @Test
 public void testHasAttr() {
 node.attr("class", "test");
 assertTrue(node.hasAttr("class"));
 }

 // Test for hasAttr() method with missing attribute
 @Test
 public void testHasAttrWithMissingAttribute() {
 assertFalse(node.hasAttr("nonExistentAttribute"));
 }

 // Test for removeAttr() method
 @Test
 public void testRemoveAttr() {
 node.attr("class", "test");
 node.removeAttr("class");
 assertFalse(node.hasAttr("class"));
 }

 // Test for setBaseUri() method
 @Test
 public void testSetBaseUri() {
 node.setBaseUri("https://www.example.com/");
 assertEquals("https://www.example.com/", node.baseUri());
 }

 // Test for absUrl() method with empty baseUri
 @Test
 public void testAbsUrlWithEmptyBaseUri() throws MalformedURLException {
 node.setBaseUri("");
 assertEmpty(node.absUrl("href"));
 }

 // Test for setParentNode() method
 @Test
 public void testSetParentNode() {
 Element parent = new Element("body");
 node.setParentNode(parent);
 assertEquals(parent, node.parent());
 }

 // Test for setParentNode() method with null parent
 @Test(expected = IllegalArgumentException.class)
 public void testSetParentNodeWithNullParent() {
 node.setParentNode(null);
 }

 // Test for setSiblingIndex() method
 @Test
 public void testSetSiblingIndex() {
 Element sibling = new Element("p");
 node.appendChild(sibling);
 node.setSiblingIndex(1);
 assertEquals(sibling, node.previousSibling());
 }

 // Test for outerHtml() method
 @Test
 public void testOuterHtml() {
 StringBuilder accum = new StringBuilder(32*1024);
 node.outerHtml(accum);
 assertEquals("<div></div>", accum.toString());
 }

 // Test for equals() method
 @Test
 public void testEquals() {
 Node otherNode = new Element("div");
 assertTrue(node.equals(node));
 assertFalse(node.equals(otherNode));
 }

 // Test for hashCode() method
 @Test
 public void testHashCode() {
 Node otherNode = new Element("div");
 int hashCode1 = node.hashCode();
 int hashCode2 = otherNode.hashCode();
 assertNotEquals(hashCode1, hashCode2);
 }
}
import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Document.OutputSettings;

public class NodeTest {

 @Test
 public void testNodeConstructor() {
 Node node = new Element("div");
 assertTrue(node instanceof Node);
 assertNull(node.parentNode());
 assertEquals("", node.baseUri());
 assertNotNull(node.attributes());
 assertEquals(0, node.childNodes().size());
 assertEquals("div", node.nodeName());
 }

 @Test
 public void testNodeConstructorWithAttributes() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes().put("class", "example");
 Node node = new Element("div", baseUri, attributes);

 assertTrue(node instanceof Node);
 assertNotNull(node.parentNode());
 assertEquals(baseUri, node.baseUri());
 assertEquals("example", node.attr("class"));
 assertEquals(0, node.childNodes().size());
 assertEquals("div", node.nodeName());
 }

 @Test
 public void testNodeConstructorWithoutArguments() {
 Node node = new Node();
 assertNull(node.parentNode());
 assertEquals("", node.baseUri());
 assertEquals(null, node.attributes());
 assertEquals(Collections.EMPTY_LIST, node.childNodes());
 assertFalse(node instanceof Element);
 }

 @Test
 public void testAttrMethod() {
 Element node = new Element("a", "", new Attributes().put("href", "/path/to/page"));
 assertEquals("/path/to/page", node.attr("href"));
 assertEquals("", node.attr("nonexistentattr"));
 }

 @Test
 public void testAttrMethodWithAbsPrefix() {
 Element node = new Element("a", "", new Attributes().put("href", "path/to/page"));
 assertEquals("http://" + node.baseUri() + "/path/to/page", node.absUrl("href"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttrMethodWithNullAttributeKey() {
 Element node = new Element("a", "", new Attributes());
 node.attr(null, "value");
 }

 @Test
 public void testAttributesMethod() {
 Element node = new Element("a", "", new Attributes().put("href", "/path/to/page"));
 Attributes attributes = node.attributes();
 assertEquals("href", attributes.keySet().iterator().next());
 assertEquals("/path/to/page", attributes.get("href"));
 }

 @Test
 public void testAttrMethodWithNullAttributeValue() {
 Element node = new Element("a", "", new Attributes().put("href", "/path/to/page"));
 node.attr("href", null);
 assertEquals("", node.attr("href"));
 }

 @Test
 public void testHasAttrMethod() {
 Element node = new Element("a", "", new Attributes().put("href", "/path/to/page"));
 assertTrue(node.hasAttr("href"));
 assertFalse(node.hasAttr("nonexistentattr"));
 }

 @Test
 public void testRemoveAttrMethod() {
 Element node = new Element("a", "", new Attributes().put("href", "/path/to/page"));
 node.removeAttr("href");
 assertEquals("", node.attr("href"));
 }

 @Test
 public void testBaseUriMethod() {
 Element node = new Element("a", "http://example.com", new Attributes());
 assertEquals("http://example.com", node.baseUri());
 }

 @Test
 public void testSetBaseUriMethod() {
 Element node = new Element("a", "", new Attributes());
 node.setBaseUri("http://example.com");
 assertEquals("http://example.com", node.baseUri());
 }

 @Test
 public void testAbsUrlMethod() {
 Element node = new Element("a", "", new Attributes().put("href", "/path/to/page"));
 assertEquals("http://example.com/path/to/page", node.absUrl("href"));
 }

 @Test
 public void testAbsUrlMethodWithAbsoluteUrlAttributeValue() {
 Element node = new Element("a", "", new Attributes().put("href", "http://example.com"));
 assertEquals("http://example.com", node.absUrl("href"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbsUrlMethodWithNullAttributeKey() {
 Element node = new Element("a", "", new Attributes());
 node.absUrl(null);
 }

 @Test(expected = MalformedURLException.class)
 public void testAbsUrlMethodWithInvalidUrl() {
 Element node = new Element("a", "", new Attributes().put("href", "foo:bar"));
 }

 @Test
 public void testChildNodeMethod() {
 Element parent = new Element("div");
 Element child = new Element("p");
 parent.appendChild(child);
 assertEquals(child, parent.childNode(0));
 }

 @Test
 public void testChildrenMethod() {
 Element parent = new Element("div");
 Element child1 = new Element("p");
 Element child2 = new Element("p");
 parent.appendChild(child1);
 parent.appendChild(child2);
 assertEquals(2, parent.childNodes().size());
 }

 @Test
 public void testChildNodesAsArrayMethod() {
 Element parent = new Element("div");
 Element child1 = new Element("p");
 Element child2 = new Element("p");
 parent.appendChild(child1);
 parent.appendChild(child2);
 Node[] children = parent.childNodesAsArray();
 assertEquals(2, children.length);
 assertEquals(child1, children[0]);
 assertEquals(child2, children[1]);
 }

 @Test
 public void testParentMethod() {
 Element child = new Element("p");
 Element parent = new Element("div");
 parent.appendChild(child);
 assertEquals(parent, child.parent());
 }

 @Test
 public void testOwnerDocumentMethod() {
 Element child = new Element("p");
 Document document = new Document();
 document.appendChild(child);
 assertEquals(document, child.ownerDocument());
 }

 @Test
 public void testRemoveMethod() {
 Element parent = new Element("div");
 Element child = new Element("p");
 parent.appendChild(child);
 parent.removeChild(child);
 assertEquals(0, parent.childNodes().size());
 }

 @Test
 public void testReplaceChildMethod() {
 Element parent = new Element("div");
 Element oldChild = new Element("p");
 Element newChild = new Element("p");
 parent.appendChild(oldChild);
 parent.replaceChild(oldChild, newChild);
 assertEquals(1, parent.childNodes().size());
 assertEquals(newChild, parent.childNodes().get(0));
 }

 @Test
 public void testSetParentNodeMethod() {
 Element child = new Element("p");
 Element parent = new Element("div");
 child.setParentNode(parent);
 assertEquals(parent, child.parent());
 }

 @Test
 public void testSetSiblingIndexMethod() {
 Element sibling1 = new Element("p");
 Element sibling2 = new Element("p");
 Element parent = new Element("div");
 parent.appendChild(sibling1);
 parent.appendChild(sibling2);
 sibling2.setSiblingIndex(1);
 assertEquals(1, sibling2.siblingIndex());
 assertEquals(sibling1, sibling2.previousSibling());
 assertEquals(sibling2, sibling1.nextSibling());
 }

 @Test
 public void testOuterHtmlMethod() {
 Element node = new Element("div");
 node.outerHtml("test");
 assertEquals("<div></div>", node.outerHtml());
 }

 @Test
 public void testOuterHtmlMethodWithCustomIndentAmount() {
 Document document = new Document();
 OutputSettings settings = new OutputSettings();
 settings.indentAmount(4);
 Element node = new Element("div");
 node.outerHtml(document, new StringBuilder(), settings);
 assertEquals("<div>\n\n</div>", node.outerHtml());
 }

 @Test
 public void testOuterHtmlMethodWithTextNodeChildren() {
 Element parent = new Element("div");
 TextNode textNode1 = new TextNode("Hello");
 TextNode textNode2 = new TextNode("World");
 parent.appendChild(textNode1);
 parent.appendChild(textNode2);
 StringBuilder accum = new StringBuilder(256);
 parent.outerHtml(accum);
 assertEquals("<div>" + textNode1.text() + textNode2.text() + "</div>", accum.toString());
 }

 @Test
 public void testOuterHtmlMethodWithElementChildren() {
 Element parent = new Element("div");
 Element child1 = new Element("p", "", new Attributes().put("class", "child1"));
 Element child2 = new Element("p", "", new Attributes().put("class", "child2"));
 parent.appendChild(child1);
 parent.appendChild(child2);
 StringBuilder accum = new StringBuilder(256);
 parent.outerHtml(accum);
 assertEquals("<div>" + child1.outerHtml() + child2.outerHtml() + "</div>", accum.toString());
 }
}
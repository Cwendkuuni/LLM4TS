import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;
import static org.junit.Assert.*;

public class NodeTest {

 @Test
 public void testNewNode() {
 Node node = new Element("div"); // constructor with attributes
 assertEquals("div", node.nodeName());
 assertNull(node.parentNode());
 assertEquals(0, node.siblingIndex());
 assertNull(node.attributes());
 assertNull(node.baseUri());
 assertEquals(0, node.childNodes().size());
 }

 @Test
 public void testNewNodeWithAttributes() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes() {{
 put("id", "test");
 put("class", "test");
 put("style", "color: red;");
 put("title", "Test Node");
 put("abs:href", baseUri);
 }};
 Node node = new Element("a", baseUri, attributes);
 assertEquals("a", node.nodeName());
 assertEquals(baseUri, node.baseUri());
 assertEquals("test", node.attr("id"));
 assertEquals("test", node.attr("class"));
 assertEquals("color: red;", node.attr("style"));
 assertEquals("Test Node", node.attr("title"));
 assertEquals(baseUri, node.absUrl("href"));
 }

 @Test
 public void testNullBaseUri() {
 String baseUri = null;
 Node node = new Element("a", baseUri, new Attributes());
 assertEquals("a", node.nodeName());
 assertNull(node.baseUri());
 }

 @Test(expected = NullPointerException.class)
 public void testNullAttributeKey() {
 Node node = new Element("div");
 try {
 node.attr(null, "value");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testSetAttribute() {
 Node node = new Element("div");
 node.attr("id", "test");
 assertEquals("test", node.attr("id"));
 }

 @Test
 public void testRemoveAttribute() {
 Node node = new Element("div").attr("id", "test");
 node.removeAttr("id");
 assertEquals("", node.attr("id"));
 }

 @Test
 public void testHasAttribute() {
 Node node = new Element("div").attr("id", "test");
 assertTrue(node.hasAttr("id"));
 assertTrue(node.hasAttr("class"));
 assertFalse(node.hasAttr("foo"));
 }

 @Test
 public void testGetAttributeWithPrefix() {
 Node node = new Element("a").attr("abs:href", "http://example.com");
 assertEquals("http://example.com", node.absUrl("href"));
 }

 @Test
 public void testGetEmptyAttribute() {
 Node node = new Element("div");
 assertEquals("", node.attr("foo"));
 }

 @Test
 public void testGetNonExistentAttribute() {
 Node node = new Element("div");
 assertEquals("", node.attr("foo"));
 }

 @Test
 public void testListChildren() {
 Node parent = new Element("div");
 Node child1 = new Element("p");
 Node child2 = new TextNode("Text Node");
 parent.addChildren(child1, child2);
 assertEquals(2, parent.childNodes().size());
 assertTrue(parent.childNodes().contains(child1));
 assertTrue(parent.childNodes().contains(child2));
 }

 @Test
 public void testGetChildNode() {
 Node parent = new Element("div");
 Node child = new Element("p");
 parent.addChildren(child);
 assertEquals(child, parent.childNode(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetChildNodeOutOfBounds() {
 Node parent = new Element("div");
 assertEquals(0, parent.childNodes().size());
 try {
 parent.childNode(1);
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testSetParentNode() {
 Node child = new Element("p");
 Node parent = new Element("div");
 child.setParentNode(parent);
 assertEquals(parent, child.parentNode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetParentNodeNull() {
 Node child = new Element("p");
 child.setParentNode(null);
 }

 @Test
 public void testReplaceChild() {
 Node parent = new Element("div");
 Node child1 = new Element("p");
 Node child2 = new Element("p");
 parent.addChildren(child1);
 parent.replaceChild(child1, child2);
 assertEquals(child2, parent.childNode(0));
 assertEquals(child1, parent.childNodes().get(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReplaceChildNullChild() {
 Node parent = new Element("div");
 Node child = null;
 parent.replaceChild(child, new Element("p"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReplaceChildNullParent() {
 Node child = new Element("p");
 Node parent = null;
 child.setParentNode(parent);
 parent.replaceChild(child, new Element("p"));
 }

 @Test
 public void testRemoveChild() {
 Node parent = new Element("div");
 Node child = new Element("p");
 parent.addChildren(child);
 parent.removeChild(child);
 assertEquals(0, parent.childNodes().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveChildNullChild() {
 Node parent = new Element("div");
 Node child = null;
 parent.removeChild(child);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveChildNullParent() {
 Node child = new Element("p");
 Node parent = null;
 child.setParentNode(parent);
 parent.removeChild(child);
 }

 @Test
 public void testClone() {
 Node original = new Element("div").attr("id", "test");
 Node cloned = original.clone();
 assertEquals(original, cloned);
 assertEquals("div", cloned.nodeName());
 assertEquals("test", cloned.attr("id"));
 assertNotSame(original, cloned);
 }

 @Test
 public void testCloneWithChildren() {
 Node parent = new Element("div");
 Node child1 = new Element("p").attr("id", "test1");
 Node child2 = new Element("p").attr("id", "test2");
 parent.addChildren(child1, child2);
 Node cloned = parent.clone();
 assertEquals(parent, cloned);
 assertEquals(2, cloned.childNodes().size());
 assertNotSame(child1, cloned.childNodes().get(0));
 assertNotSame(child2, cloned.childNodes().get(1));
 }

 @Test
 public void testCloneWithAttributes() {
 String baseUri = "http://example.com";
 Node original = new Element("a", baseUri, new Attributes() {{
 put("id", "test");
 put("href", "http://example.com/test");
 }});
 Node cloned = original.clone();
 assertEquals(original, cloned);
 assertEquals("a", cloned.nodeName());
 assertEquals("test", cloned.attr("id"));
 assertEquals("http://example.com", cloned.baseUri());
 assertEquals("http://example.com/test", cloned.absUrl("href"));
 }

 @Test
 public void testOuterHtml() {
 Node node = new Element("div").attr("id", "test");
 StringBuilder accum = new StringBuilder();
 node.outerHtml(accum);
 assertEquals("<div id=\"test\"></div>", accum.toString());
 }

 @Test
 public void testOuterHtmlWithAttributeValue() {
 Element node = new Element("input").attr("type", "text").attr("value", "Test");
 StringBuilder accum = new StringBuilder();
 node.outerHtml(accum);
 assertEquals("<input type=\"text\" value=\"Test\"></input>", accum.toString());
 }

 @Test
 public void testOuterHtmlWithNestedNodes() {
 Node parent = new Element("div");
 Node child = new Element("p").attr("id", "test");
 parent.addChildren(child);
 StringBuilder accum = new StringBuilder();
 parent.outerHtml(accum);
 assertEquals("<div><p id=\"test\"></p></div>", accum.toString());
 }

 @Test
 public void testToString() {
 Node node = new Element("div").attr("id", "test");
 assertEquals("<div id=\"test\"></div>", node.toString());
 }
}
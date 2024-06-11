import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.AttributesImpl;

public class NodeTest {

 @Test
 public void testNodeCreation() {
 Node node = new Element("div");
 Assert.assertNotNull(node);
 Assert.assertEquals("div", node.nodeName());
 }

 @Test
 public void testNodeAttributes() {
 Element element = new Element("a");
 AttributesImpl attributes = new AttributesImpl();
 attributes.put("href", "http://example.com");
 element.attributes().putAll(attributes);

 Assert.assertEquals("http://example.com", element.attr("href"));
 Assert.assertEquals("", element.attr("nonExistentAttr"));
 }

 @Test
 public void testBaseUri() {
 Attributes attributes = new AttributesImpl();
 Attributes baseAttributes = new AttributesImpl();
 baseAttributes.put("baseUri", "http://example.com");

 Element element = new Element("a");
 element.attributes().putAll(attributes);
 element.setBaseUri(baseAttributes);

 Assert.assertEquals("http://example.com", element.baseUri());
 }

 @Test
 public void testAbsUrl() {
 Attributes attributes = new AttributesImpl();
 attributes.put("href", "relative.html");
 Element element = new Element("a");
 element.attributes().putAll(attributes);

 String baseUri = "http://example.com";
 Attributes baseAttributes = new AttributesImpl();
 baseAttributes.put("baseUri", baseUri);
 element.setBaseUri(baseAttributes);

 Assert.assertEquals("http://example.com/relative.html", element.absUrl("href"));
 Assert.assertEquals("", element.absUrl("nonExistentAttr"));

 try {
 URL url = new URL(baseUri, "/relative.html");
 Assert.assertEquals(url.toExternalForm(), element.absUrl("href"));
 } catch (MalformedURLException e) {
 // expected
 }
 }

 @Test
 public void testChildNodes() {
 Element parent = new Element("ul");
 Element child1 = new Element("li");
 Element child2 = new Element("li");

 parent.addChildren(child1, child2);

 List<Node> children = parent.childNodes();
 Node firstChild = children.get(0);
 Node secondChild = children.get(1);

 Assert.assertEquals(child1, firstChild);
 Assert.assertEquals(child2, secondChild);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetChildNodeNegativeIndex() {
 Element parent = new Element("ul");
 Element child = new Element("li");
 parent.addChildren(child);

 try {
 Node node = parent.childNode(-1);
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testRemoveChild() {
 Element parent = new Element("ul");
 Element child = new Element("li");
 parent.addChildren(child);

 parent.removeChild(child);

 List<Node> children = parent.childNodes();
 assertTrue(children.isEmpty());
 }

 @Test
 public void testReplaceChild() {
 Element parent = new Element("ul");
 Element child1 = new Element("li");
 Element child2 = new Element("li");
 parent.addChildren(child1);

 parent.replaceChild(child1, child2);
 List<Node> children = parent.childNodes();
 Node replacedNode = children.get(0);

 Assert.assertEquals(child2, replacedNode);
 Assert.assertEquals(child1, parent.childNodesAsArray()[1]);
 }
}
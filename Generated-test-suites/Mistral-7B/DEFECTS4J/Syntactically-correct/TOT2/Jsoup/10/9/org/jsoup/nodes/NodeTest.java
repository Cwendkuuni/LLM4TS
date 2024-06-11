import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;
import java.net.MalformedURLException;
import java.net.URL;

public class NodeTest {

 @Test
 public void testNodeCreation() {
 Node node = new Element("div");
 Validate.notNull(node);
 Validate.notNull(node.baseUri());
 Validate.notNull(node.attributes());
 }

 @Test
 public void testEmptyNodeCreation() {
 Node node = new Node();
 Validate.assertNull(node.baseUri());
 Validate.assertNull(node.attributes());
 }

 @Test
 public void testNodeName() {
 Node node = new Element("div");
 Assert.assertEquals("div", node.nodeName());
 }

 @Test
 public void testAttrMethod() {
 Element elem = new Element("a");
 elem.attr("href", "http://example.com");
 Assert.assertEquals("http://example.com", elem.attr("href"));
 Assert.assertEquals("", elem.attr("nonExistent"));
 }

 @Test
 public void testAbsUrlMethod() {
 Element elem = new Element("a");
 elem.attr("href", "about.html");
 Assert.assertEquals("about.html", elem.absUrl("href"));
 Element base = new Element("html");
 base.attr("baseHref", "http://example.com/");
 elem.parentNode = base;
 Assert.assertEquals("http://example.com/about.html", elem.absUrl("href"));
 }

 @Test
 public void testChildNodeAccess() {
 Element parent = new Element("ul");
 Element child1 = new Element("li");
 child1.text("Child 1");
 parent.appendChild(child1);
 Node child2 = new TextNode("Child 2");
 parent.appendChild(child2);
 Node child3 = parent.childNode(0);
 Assert.assertEquals("Child 1", child3.nodeName());
 Assert.assertEquals("Child 1", child3.text());
 Assert.assertEquals("Child 2", child4.text());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testChildNodeAccessOutOfBounds() {
 Element parent = new Element("ul");
 Element child1 = new Element("li");
 parent.appendChild(child1);
 Assert.assertNull(parent.childNode(1)); // Should be null as there's only one child
 }

 @Test
 public void testParentNodeAccess() {
 Element child = new Element("li");
 Element parent = new Element("ul");
 child.parentNode = parent;
 Assert.assertEquals(parent, child.parent());
 }

 @Test
 public void testParentNodeAccessNull() {
 Element child = new Element("li");
 Assert.assertNull(child.parent());
 }

 @Test
 public void testSetBaseUri() {
 Element node = new Element("div");
 node.setBaseUri("http://example.com/");
 Assert.assertEquals("http://example.com/", node.baseUri());
 }

 @Test(expected = MalformedURLException.class)
 public void testInvalidBaseUri() {
 Element node = new Element("div");
 node.setBaseUri("//example.com");
 }

 @Test
 public void testAttributes() {
 Element elem = new Element("img");
 elem.attr("src", "image.jpg");
 elem.attr("alt", "Image description");
 elem.attr("title", "Title for image");
 Assert.assertEquals("image.jpg", elem.attr("src"));
 Assert.assertEquals("Image description", elem.attr("alt"));
 Assert.assertEquals("Title for image", elem.attr("title"));
 Assert.assertEquals(3, elem.attributes().size());
 }

 @Test
 public void testSetAttributes() {
 Element elem = new Element("a");
 Attributes attributes = new Attributes();
 attributes.put("href", "http://example.com");
 attributes.put("target", "_blank");
 elem.attributes = attributes;
 Assert.assertEquals("http://example.com", elem.attr("href"));
 Assert.assertEquals("_blank", elem.attr("target"));
 }

 @Test
 public void testHasAttr() {
 Element elem = new Element("img");
 elem.attr("src", "image.jpg");
 Assert.assertTrue(elem.hasAttr("src"));
 Assert.assertFalse(elem.hasAttr("nonExistent"));
 }

 @Test
 public void testRemoveAttr() {
 Element elem = new Element("a");
 elem.attr("href", "http://example.com");
 elem.removeAttr("href");
 Assert.assertEquals("", elem.attr("href"));
 }

 @Test
 public void testSetBaseUriWithValidURL() {
 Element node = new Element("div");
 try {
 node.setBaseUri(new URL("http://example.com/"));
 } catch (MalformedURLException e) {
 Assert.fail("Failed setting base URI with a valid URL.");
 }
 Assert.assertEquals("http://example.com/", node.baseUri());
 }

 @Test(expected = MalformedURLException.class)
 public void testSetBaseUriWithInvalidURL() {
 Element node = new Element("div");
 node.setBaseUri("//example.com");
 }
}
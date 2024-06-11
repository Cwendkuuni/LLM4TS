import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import static org.junit.Assert.*;

public class NodeTest {

 // Test case for constructor with baseUri and attributes
 @Test
 public void testNodeWithBaseUriAndAttributes() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("class", "example");
 Node node = new Element("div", baseUri, attributes);
 assertEquals("div", node.nodeName());
 assertEquals(baseUri, node.baseUri());
 assertEquals("example", node.attr("class"));
 }

 // Test case for constructor with baseUri only
 @Test
 public void testNodeWithBaseUriOnly() {
 String baseUri = "http://example.com";
 Node node = new Element("div", baseUri);
 assertEquals("div", node.nodeName());
 assertEquals(baseUri, node.baseUri());
 assertEquals("", node.attr("class"));
 }

 // Test case for default constructor
 @Test
 public void testDefaultConstructor() {
 Node node = new Node();
 assertEquals(Collections.EMPTY_LIST, node.childNodes());
 assertEquals(null, node.attributes());
 }

 // Test case for getNodeName method
 @Test
 public void testgetNodeName() {
 Node node = new Element("div");
 assertEquals("div", node.nodeName());
 }

 // Test case for getAttribute method
 @Test
 public void testgetAttribute() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("class", "example");
 Node node = new Element("div", baseUri, attributes);
 assertEquals("example", node.attr("class"));
 assertEquals("", node.attr("nonExistentAttribute"));
 }

 // Test case for getAttributes method
 @Test
 public void testgetAttributes() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("class", "example");
 Node node = new Element("div", baseUri, attributes);
 Attributes retrievedAttributes = node.attributes();
 assertEquals(attributes, retrievedAttributes);
 }

 // Test case for setAttribute method
 @Test
 public void testsetAttribute() {
 String baseUri = "http://example.com";
 Node node = new Element("div", baseUri);
 node.attr("class", "example");
 assertEquals("example", node.attr("class"));
 }

 // Test case for hasAttribute method
 @Test
 public void testHasAttribute() {
 String baseUri = "http://example.com";
 Node node = new Element("div", baseUri);
 node.attr("class", "example");
 assertTrue(node.hasAttr("class"));
 }

 // Test case for removeAttribute method
 @Test
 public void testRemoveAttribute() {
 String baseUri = "http://example.com";
 Node node = new Element("div", baseUri);
 node.attr("class", "example");
 node.removeAttr("class");
 assertFalse(node.hasAttr("class"));
 }

 // Test case for getBaseUri method
 @Test
 public void testGetBaseUri() {
 String baseUri = "http://example.com";
 Node node = new Element("div", baseUri);
 assertEquals(baseUri, node.baseUri());
 }

 // Test case for setBaseUri method
 @Test
 public void testSetBaseUri() {
 String baseUri = "http://example.com";
 String newBaseUri = "http://newexample.com";
 Node node = new Element("div", baseUri);
 node.setBaseUri(newBaseUri);
 assertEquals(newBaseUri, node.baseUri());
 }

 // Test case for absUrl method for absolute URL
 @Test
 public void testAbsUrlForAbsoluteUrl() {
 String baseUri = "http://example.com";
 String attributeValue = "http://newexample.com";
 Node node = new Element("a", baseUri);
 node.attr("href", attributeValue);
 assertEquals(attributeValue, node.absUrl("href"));
 }

 // Test case for absUrl method for relative URL
 @Test
 public void testAbsUrlForRelativeUrl() {
 String baseUri = "http://example.com";
 String attributeValue = "style.css";
 Node node = new Element("link", baseUri);
 node.attr("href", attributeValue);
 URL url = new URL(baseUri, attributeValue);
 assertEquals(url.toString(), node.absUrl("href"));
 }

 // Test case for getChildNode method
 @Test
 public void testGetChildNode() {
 String baseUri = "http://example.com";
 Node parentNode = new Element("div", baseUri);
 Node childNode = new Element("p", baseUri);
 parentNode.addChild(childNode);
 Node retrievedChildNode = parentNode.childNode(0);
 assertEquals(childNode, retrievedChildNode);
 }

 // Test case for getChildNodes method
 @Test
 public void testGetChildNodes() {
 String baseUri = "http://example.com";
 Node parentNode = new Element("div", baseUri);
 Node childNode1 = new Element("p", baseUri);
 Node childNode2 = new Element("p", baseUri);
 parentNode.addChildren(childNode1, childNode2);
 List<Node> childNodes = parentNode.childNodes();
 assertEquals(2, childNodes.size());
 assertEquals(childNode1, childNodes.get(0));
 assertEquals(childNode2, childNodes.get(1));
 }

 // Test case for removeChild method
 @Test
 public void testRemoveChild() {
 String baseUri = "http://example.com";
 Node parentNode = new Element("div", baseUri);
 Node childNode = new Element("p", baseUri);
 parentNode.addChild(childNode);
 parentNode.removeChild(childNode);
 List<Node> childNodes = parentNode.childNodes();
 assertEquals(0, childNodes.size());
 }
}
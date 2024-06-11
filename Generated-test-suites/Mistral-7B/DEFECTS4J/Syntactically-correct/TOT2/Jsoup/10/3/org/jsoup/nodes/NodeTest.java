import org.jsoup.nodes.*;
import org.junit.Assert.*;
import org.junit.Test;

public class NodeTest {

 @Test
 public void testNodeCreation() {
 Node node = new Elements.SingleTagElement("div");
 Validate.notNull(node);
 Validate.notNull(node.baseUri());
 Validate.notNull(node.attributes());
 }

 @Test
 public void testNodeCreationWithAttributes() {
 String baseUri = "http://example.com";
 String attributeKey = "class";
 String attributeValue = "testClass";
 Node node = new Elements.SingleTagElement("div")
 .attr(attributeKey, attributeValue)
 .attr("id", "testId");
 Validate.notNull(node);
 Validate.notNull(node.baseUri());
 Validate.notNull(node.attributes());
 Validate.assertEquals(attributeValue, node.attr(attributeKey));
 Validate.assertEquals("testId", node.attr("id"));
 }

 @Test
 public void testNodeCreationWithoutBaseUri() {
 Node node = new Elements.SingleTagElement("div");
 Validate.assertNull(node.baseUri());
 }

 @Test
 public void testNodeCreationWithoutAttributes() {
 Node node = new Elements.SingleTagElement("div");
 Validate.notNull(node);
 Validate.assertNull(node.attributes());
 }

 @Test
 public void testNodeGetNodeName() {
 Node node = new Elements.SingleTagElement("div");
 String nodeName = node.nodeName();
 Assert.assertEquals("div", nodeName);
 }

 @Test
 public void testNodeGetAttribute() {
 String baseUri = "http://example.com";
 String attributeKey = "class";
 String attributeValue = "testClass";
 Node node = new Elements.SingleTagElement("div")
 .attr(attributeKey, attributeValue)
 .attr("id", "testId");
 String attrValue = node.attr(attributeKey);
 Assert.assertEquals(attributeValue, attrValue);
 String id = node.attr("id");
 Assert.assertEquals("testId", id);
 }

 @Test
 public void testNodeGetAttributeWhenAttributeDoesNotExist() {
 Node node = new Elements.SingleTagElement("div");
 String attrValue = node.attr("nonExistentAttribute");
 Assert.assertEquals("", attrValue);
 }

 @Test
 public void testNodeGetAttributeWithAbsPrefix() {
 String baseUri = "http://example.com";
 String attributeKey = "href";
 String attributeValue = "test.html";
 Node node = new Elements.SingleTagElement("a").attr("href", attributeValue);
 String absUrl = node.absUrl(attributeKey);
 Assert.assertEquals("http://example.com/test.html", absUrl);
 }

 @Test(expected = MalformedURLException.class)
 public void testNodeGetAbsoluteUrlWithInvalidUrl() {
 String baseUri = "http://example.com";
 String attributeKey = "invalidUrl";
 Node node = new Elements.SingleTagElement("a").attr("abs:" + attributeKey, "foo:bar");
 String absUrl = node.absUrl(attributeKey);
 }

 @Test
 public void testNodeGetChildNode() {
 Node parentNode = new Elements.SingleTagElement("div");
 Node childNode = new Elements.SingleTagElement("p");
 parentNode.appendChild(childNode);
 Node child = parentNode.childNode(0);
 Assert.assertEquals(childNode, child);
 }

 @Test
 public void testNodeGetChildren() {
 Node parentNode = new Elements.SingleTagElement("div");
 Node childNode1 = new Elements.SingleTagElement("p");
 Node childNode2 = new Elements.SingleTagElement("p");
 parentNode.appendChild(childNode1);
 parentNode.appendChild(childNode2);
 List<Node> children = parentNode.childNodes();
 List<Node> expectedChildren = new ArrayList<>();
 expectedChildren.add(childNode1);
 expectedChildren.add(childNode2);
 Assert.assertEquals(expectedChildren, children);
 }

 @Test
 public void testNodeRemoveAttribute() {
 String baseUri = "http://example.com";
 String attributeKey = "class";
 String attributeValue = "testClass";
 Node node = new Elements.SingleTagElement("div")
 .attr(attributeKey, attributeValue)
 .attr("id", "testId");
 node.removeAttr(attributeKey);
 String attrValue = node.attr(attributeKey);
 Assert.assertEquals("", attrValue);
 }

 @Test
 public void testNodeSetBaseUri() {
 String baseUri = "http://example.com";
 String newBaseUri = "http://newexample.com";
 Node node = new Elements.SingleTagElement("div");
 node.setBaseUri(newBaseUri);
 String nodeBaseUri = node.baseUri();
 Assert.assertEquals(newBaseUri, nodeBaseUri);
 }
}
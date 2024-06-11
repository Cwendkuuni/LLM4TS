import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.Test;

public class NodeTest {

 @Test
 public void testNodeConstructorWithBaseUriAndAttributes() {
 Node node = new Node("http://example.com", new Attributes().put("class", "example"));
 Assert.assertEquals("example", node.nodeName());
 Assert.assertEquals("http://example.com", node.baseUri());
 Assert.assertEquals(1, node.childNodes().size());
 Assert.assertTrue(node.hasAttr("class"));
 Assert.assertEquals("example", node.attr("class"));
 }

 @Test
 public void testNodeConstructorWithBaseUriOnly() {
 Node node = new Node("http://example.com");
 Assert.assertEquals("", node.nodeName());
 Assert.assertEquals("http://example.com", node.baseUri());
 Assert.assertEquals(0, node.childNodes().size());
 }

 @Test
 public void testNodeDefaultConstructor() {
 Node node = new Node();
 Assert.assertEquals(0, node.childNodes().size());
 Assert.assertNull(node.baseUri());
 Assert.assertNull(node.attributes());
 }

 @Test
 public void testNodeAttrMethod() {
 Node node = new Node("http://example.com", new Attributes().put("class", "example").put("id", "example"));
 Assert.assertEquals("example", node.attr("class"));
 Assert.assertEquals("example", node.attr("class").toLowerCase());
 Assert.assertEquals("example", node.attr("id"));
 Assert.assertFalse(node.hasAttr("nonexistentattr"));
 }

 @Test
 public void testNodeAttrMethodWithAbsUrl() {
 Node node = new Node("http://example.com", new Attributes().put("abs:href", "http://example.com/test.html"));
 Assert.assertEquals("test.html", node.absUrl("href"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNodeAttrMethodWithNullAttributeKey() {
 Node node = new Node("http://example.com", new Attributes());
 node.attr(null, "value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNodeAttrMethodWithNullAttributeValue() {
 Node node = new Node("http://example.com", new Attributes().put("class", "example"));
 node.attr("class", null);
 }

 @Test
 public void testNodeHasAttrMethod() {
 Node node = new Node("http://example.com", new Attributes().put("class", "example"));
 Assert.assertTrue(node.hasAttr("class"));
 Assert.assertFalse(node.hasAttr("nonexistentattr"));
 }

 @Test
 public void testNodeRemoveAttrMethod() {
 Node node = new Node("http://example.com", new Attributes().put("class", "example").put("id", "example"));
 node.removeAttr("class");
 Assert.assertFalse(node.hasAttr("class"));
 }

 @Test
 public void testNodeSetBaseUriMethod() {
 Node node = new Node("http://example.com", new Attributes());
 node.setBaseUri("http://test.com");
 Assert.assertEquals("http://test.com", node.baseUri());
 }

 @Test(expected = NullPointerException.class)
 public void testNodeSetBaseUriMethodWithNullBaseUri() {
 Node node = new Node("http://example.com", new Attributes());
 node.setBaseUri(null);
 }

 @Test
 public void testNodeChildNodeMethod() {
 Node parentNode = new Node("http://example.com", new Attributes());
 Node childNode = new Node("http://child.example.com", new Attributes());
 parentNode.addChildren(childNode);
 Assert.assertEquals(1, parentNode.childNodes().size());
 Assert.assertEquals(childNode, parentNode.childNode(0));
 }

 @Test
 public void testNodeChildNodesMethod() {
 Node parentNode = new Node("http://example.com", new Attributes());
 Node childNode1 = new Node("http://child1.example.com", new Attributes());
 Node childNode2 = new Node("http://child2.example.com", new Attributes());
 parentNode.addChildren(childNode1, childNode2);
 List<Node> childNodes = parentNode.childNodes();
 Assert.assertEquals(2, childNodes.size());
 Assert.assertEquals(childNode1, childNodes.get(0));
 Assert.assertEquals(childNode2, childNodes.get(1));
 }

 @Test
 public void testNodeRemoveChildMethod() {
 Node parentNode = new Node("http://example.com", new Attributes());
 Node childNode1 = new Node("http://child1.example.com", new Attributes());
 Node childNode2 = new Node("http://child2.example.com", new Attributes());
 parentNode.addChildren(childNode1, childNode2);
 parentNode.removeChild(childNode1);
 List<Node> childNodes = parentNode.childNodes();
 Assert.assertEquals(1, childNodes.size());
 Assert.assertEquals(childNode2, childNodes.get(0));
 }

 @Test
 public void testSetParentNodeMethod() {
 Node childNode = new Node();
 Node parentNode = new Node();
 childNode.setParentNode(parentNode);
 Assert.assertEquals(childNode, parentNode.childNodes().get(0));
 }

 @Test
 public void testReplaceChildMethod() {
 Node parentNode = new Node("http://example.com", new Attributes());
 Node oldChild = new Node("http://oldchild.example.com", new Attributes());
 Node newChild = new Node("http://newchild.example.com", new Attributes());
 parentNode.addChildren(oldChild);
 parentNode.replaceChild(oldChild, newChild);
 List<Node> childNodes = parentNode.childNodes();
 Assert.assertEquals(1, childNodes.size());
 Assert.assertEquals(newChild, childNodes.get(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReplaceChildMethodWithNullChild() {
 Node parentNode = new Node("http://example.com", new Attributes());
 Node newChild = new Node("http://newchild.example.com", new Attributes());
 parentNode.replaceChild(null, newChild);
 }

 @Test
 public void testSetSiblingIndexMethod() {
 Node node1 = new Node();
 Node node2 = new Node();
 Node node3 = new Node();
 node1.setSiblingIndex(1);
 node2.setSiblingIndex(2);
 node3.setSiblingIndex(3);
 Assert.assertEquals(1, node1.siblingIndex());
 Assert.assertEquals(2, node2.siblingIndex());
 Assert.assertEquals(3, node3.siblingIndex());
 }

 @Test
 public void testOuterHtmlMethod() {
 Node node = new Node("http://example.com", new Attributes().put("class", "example"));
 StringBuilder accum = new StringBuilder();
 node.outerHtml(accum);
 Assert.assertEquals("<html><head></head><body><node class=\"example\"></node></body></html>", accum.toString());
 }

 @Test
 public void testOuterHtmlHeadMethod() {
 Node node = new Node("http://example.com", new Attributes().put("class", "example"));
 StringBuilder accum = new StringBuilder();
 node.outerHtmlHead(accum, 0, null);
 Assert.assertEquals("<html><head>", accum.toString());
 }

 @Test
 public void testOuterHtmlTailMethod() {
 Node node = new Node("http://example.com", new Attributes().put("class", "example"));
 StringBuilder accum = new StringBuilder();
 node.outerHtmlTail(accum, 0, null);
 Assert.assertEquals("</body></html>", accum.toString());
 }

 @Test
 public void testToStringMethod() {
 Node node = new Node("http://example.com", new Attributes().put("class", "example"));
 Assert.assertEquals("<node class=\"example\"></node>", node.toString());
 }

 @Test
 public void testEqualsMethod() {
 Node node1 = new Node("http://example.com", new Attributes().put("class", "example"));
 Node node2 = new Node("http://example.com", new Attributes().put("class", "example"));
 Node node3 = new Node("http://example.com", new Attributes().put("class", "example").put("id", "example"));
 Assert.assertEquals(node1, node1);
 Assert.assertEquals(node1, node2);
 Assert.assertNotEquals(node1, node3);
 }

 @Test
 public void testHashCodeMethod() {
 Node node1 = new Node("http://example.com", new Attributes().put("class", "example"));
 Node node2 = new Node("http://example.com", new Attributes().put("class", "example").put("id", "example"));
 Assert.assertEquals(node1.hashCode(), node1.hashCode());
 Assert.assertNotEquals(node1.hashCode(), node2.hashCode());
 }
}
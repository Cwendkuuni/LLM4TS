import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;

public class NodeTest {

 // Test constructor with baseUri and attributes
 @Test
 public void testNodeConstructorWithBaseUriAndAttributes() {
 Node node = new Node("http://test.com", new Attributes());
 Assert.assertNotNull(node);
 Assert.assertEquals("http://test.com", node.baseUri());
 }

 // Test constructor with only baseUri
 @Test
 public void testNodeConstructorWithBaseUriOnly() {
 Node node = new Node("http://test.com");
 Assert.assertNotNull(node);
 Assert.assertEquals("http://test.com", node.baseUri());
 }

 // Test constructor without baseUri or attributes
 @Test
 public void testNodeConstructorWithoutBaseUriOrAttributes() {
 Node node = new Node();
 Assert.assertNotNull(node);
 Assert.assertEquals(Collections.EMPTY_LIST, node.childNodes());
 Assert.assertNull(node.attributes());
 }

 // Test getNodeName method
 @Test
 public void testgetNodeName() {
 Node node = new Node("http://test.com", new Attributes());
 Assert.assertNotNull(node.nodeName());
 }

 // Test getAttribute method with existing attribute
 @Test
 public void testgetAttributeWithExistingAttribute() {
 Node node = new Node("http://test.com", new Attributes().put("test", "value"));
 Assert.assertEquals("value", node.attr("test"));
 }

 // Test getAttribute method with non-existing attribute
 @Test
 public void testgetAttributeWithNonExistingAttribute() {
 Node node = new Node("http://test.com", new Attributes());
 Assert.assertEquals("", node.attr("nonExistingAttribute"));
 }

 // Test getAttribute method with abs: prefix
 @Test
 public void testgetAttributeWithAbsPrefix() {
 Node node = new Node("http://test.com", new Attributes().put("href", "test"));
 Assert.assertEquals("test", node.attr("abs:href"));
 }

 // Test getAllAttributes method
 @Test
 public void testgetAllAttributes() {
 Node node = new Node("http://test.com", new Attributes().put("test1", "value1").put("test2", "value2"));
 Assert.assertEquals(2, node.attributes().size());
 Assert.assertEquals("value1", node.attributes().get("test1"));
 Assert.assertEquals("value2", node.attributes().get("test2"));
 }

 // Test setting an attribute
 @Test
 public void testSettingAnAttribute() {
 Node node = new Node("http://test.com");
 node.attr("test", "value");
 Assert.assertEquals("value", node.attr("test"));
 }

 // Test testing if an element has an attribute
 @Test
 public void testHasAttr() {
 Node node = new Node("http://test.com", new Attributes().put("test", "value"));
 Assert.assertTrue(node.hasAttr("test"));
 }

 // Test testing if an element does not have an attribute
 @Test
 public void testHasAttrWhenAttributeDoesNotExist() {
 Node node = new Node("http://test.com");
 Assert.assertFalse(node.hasAttr("nonExistingAttribute"));
 }

 // Test removing an attribute
 @Test
 public void testRemovingAnAttribute() {
 Node node = new Node("http://test.com", new Attributes().put("test", "value"));
 node.removeAttr("test");
 Assert.assertFalse(node.hasAttr("test"));
 }

 // Test getting baseUri
 @Test
 public void testGetBaseUri() {
 Node node = new Node("http://test.com", new Attributes());
 Assert.assertEquals("http://test.com", node.baseUri());
 }

 // Test setting baseUri
 @Test
 public void testSettingBaseUri() {
 Node node = new Node("http://test.com");
 node.setBaseUri("http://newBaseUri.com");
 Assert.assertEquals("http://newBaseUri.com", node.baseUri());
 }

 // Test getting absolute URL from an attribute
 @Test
 public void testGettingAbsoluteUrlFromAnAttribute() {
 Node node = new Node("http://test.com", new Attributes().put("href", "relativeUrl"));
 Assert.assertEquals("", node.absUrl("href")); // empty as it's relative

 Node nodeWithBaseUri = new Node("http://test.com/parent", new Attributes().put("href", "relativeUrl"));
 Assert.assertEquals("http://test.com/parent/relativeUrl", nodeWithBaseUri.absUrl("href"));
 }

 // Test getting child node by index
 @Test
 public void testGettingChildNodeByIndex() {
 Node parent = new Node("http://test.com");
 Node child1 = new Node("http://child1.com", new Attributes());
 Node child2 = new Node("http://child2.com", new Attributes());
 parent.addChildren(child1, child2);
 Node child = parent.childNode(0);
 Assert.assertEquals(child1, child);
 }

 // Test getting children
 @Test
 public void testGettingChildren() {
 Node parent = new Node("http://test.com");
 Node child1 = new Node("http://child1.com", new Attributes());
 Node child2 = new Node("http://child2.com", new Attributes());
 parent.addChildren(child1, child2);
 List<Node> children = parent.childNodes();
 Assert.assertEquals(2, children.size());
 Assert.assertEquals(child1, children.get(0));
 Assert.assertEquals(child2, children.get(1));
 }

 // Test getting children as array
 @Test
 public void testGettingChildrenAsArray() {
 Node parent = new Node("http://test.com");
 Node child1 = new Node("http://child1.com", new Attributes());
 Node child2 = new Node("http://child2.com", new Attributes());
 parent.addChildren(child1, child2);
 Node[] children = parent.childNodesAsArray();
 Assert.assertArrayEquals(new Node[]{child1, child2}, children);
 }

 // Test getting parent node
 @Test
 public void testGettingParentNode() {
 Node child = new Node("http://child.com");
 Node parent = new Node("http://parent.com");
 child.setParentNode(parent);
 Node parentNode = child.parent();
 Assert.assertEquals(parent, parentNode);
 }

 // Test getting document associated with this node
 @Test
 public void testGettingDocumentAssociatedWithThisNode() {
 Node node = new Node("http://test.com");
 Document document = new Document();
 document.appendChild(node);
 Assert.assertEquals(document, node.ownerDocument());
 }

 // Test removing this node from the DOM tree
 @Test
 public void testRemovingThisNodeFromTheDOMTree() {
 Node parent = new Node("http://test.com");
 Node child = new Node("http://child.com");
 parent.addChildren(child);
 parent.removeChild(child);
 Assert.assertEquals(Collections.EMPTY_LIST, parent.childNodes());
 }

 // Test replacing this node in the DOM with the supplied node
 @Test
 public void testReplacingThisNodeInTheDOMWithTheSuppliedNode() {
 Node parent = new Node("http://test.com");
 Node oldChild = new Node("http://oldChild.com");
 Node newChild = new Node("http://newChild.com");
 parent.addChildren(oldChild);
 parent.replaceChild(oldChild, newChild);
 Assert.assertEquals(newChild, parent.childNode(0));
 }

 // Test setting parent node
 @Test
 public void testSettingParentNode() {
 Node child = new Node("http://child.com");
 Node parent = new Node("http://parent.com");
 child.setParentNode(parent);
 Assert.assertEquals(parent, child.parent());
 }

 // Test replacing child
 @Test
 public void testReplacingChild() {
 Node parent = new Node("http://test.com");
 Node oldChild = new Node("http://oldChild.com");
 Node newChild = new Node("http://newChild.com");
 parent.addChildren(oldChild);
 parent.replaceChild(oldChild, newChild);
 Assert.assertEquals(newChild, parent.childNode(0));
 }

 // Test removing child
 @Test
 public void testRemovingChild() {
 Node parent = new Node("http://test.com");
 Node child = new Node("http://child.com");
 parent.addChildren(child);
 parent.removeChild(child);
 Assert.assertEquals(Collections.EMPTY_LIST, parent.childNodes());
 }

 // Test adding children
 @Test
 public void testAddingChildren() {
 Node parent = new Node("http://test.com");
 Node child1 = new Node("http://child1.com");
 Node child2 = new Node("http://child2.com");
 parent.addChildren(child1, child2);
 List<Node> children = parent.childNodes();
 Assert.assertEquals(2, children.size());
 Assert.assertEquals(child1, children.get(0));
 Assert.assertEquals(child2, children.get(1));
 }

 // Test adding children with index
 @Test
 public void testAddingChildrenWithIndex() {
 Node parent = new Node("http://test.com");
 Node child1 = new Node("http://child1.com");
 Node child2 = new Node("http://child2.com");
 parent.addChildren(1, child1, child2);
 List<Node> children = parent.childNodes();
 Assert.assertEquals(3, children.size());
 Assert.assertEquals(child1, children.get(1));
 Assert.assertEquals(child2, children.get(2));
 }
}
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

 // Test constructor with baseUri and attributes
 @Test
 public void testNodeWithBaseUriAndAttributes() {
 Node node = new Element("div", "baseUri", new Attributes().put("class", "test"));
 assertEquals("div", node.nodeName());
 assertEquals("baseUri", node.baseUri());
 assertEquals("test", node.attr("class"));
 }

 // Test constructor without baseUri and with attributes
 @Test
 public void testNodeWithoutBaseUriAndWithAttributes() {
 Node node = new Element("div", new Attributes().put("class", "test"));
 assertEquals("div", node.nodeName());
 assertEquals("", node.baseUri());
 assertEquals("test", node.attr("class"));
 }

 // Test default constructor
 @Test
 public void testDefaultConstructor() {
 Node node = new Node();
 assertEquals(Collections.EMPTY_LIST, node.childNodes());
 assertNull(node.attributes());
 }

 // Test getNodeName method
 @Test
 public void testgetNodeName() {
 Node node = new Element("div");
 assertEquals("div", node.nodeName());
 }

 // Test getAttribute method
 @Test
 public void testgetAttribute() {
 Node node = new Element("div").attr("class", "test");
 assertEquals("test", node.attr("class"));
 }

 // Test getAttributes method
 @Test
 public void testgetAttributes() {
 Node node = new Element("div").attr("class", "test").attr("id", "testId");
 Attributes attributes = node.attributes();
 assertEquals("class", attributes.keySet().iterator().next());
 assertEquals("test", attributes.get("class"));
 assertEquals("testId", attributes.get("id"));
 }

 // Test setAttribute method
 @Test
 public void testsetAttribute() {
 Node node = new Element("div");
 node.attr("class", "test");
 assertEquals("test", node.attr("class"));
 }

 // Test hasAttribute method
 @Test
 public void testhasAttribute() {
 Node node = new Element("div").attr("class", "test");
 assertTrue(node.hasAttr("class"));
 }

 // Test removeAttribute method
 @Test
 public void testremoveAttribute() {
 Node node = new Element("div").attr("class", "test");
 node.removeAttr("class");
 assertFalse(node.hasAttr("class"));
 }

 // Test getBaseUri method
 @Test
 public void testgetBaseUri() {
 Node node = new Element("div", "baseUri", new Attributes());
 assertEquals("baseUri", node.baseUri());
 }

 // Test setBaseUri method
 @Test
 public void testsetBaseUri() {
 Node node = new Element("div");
 node.setBaseUri("testBaseUri");
 assertEquals("testBaseUri", node.baseUri());
 }

 // Test absUrl method
 @Test
 public void testabsUrl() {
 Node node = new Element("div").attr("abs:href", "test.html");
 assertEquals("test.html", node.absUrl("href"));
 }

 // Test getChildNode method
 @Test
 public void testchildNode() {
 Element parent = new Element("div");
 Element child = new Element("p");
 parent.addChildren(child);
 Node node = parent.childNode(0);
 assertEquals("p", node.nodeName());
 }

 // Test getChildNodes method
 @Test
 public void testchildNodes() {
 Element parent = new Element("div");
 Element child1 = new Element("p");
 Element child2 = new Element("p");
 parent.addChildren(child1, child2);
 List<Node> nodes = parent.childNodes();
 assertEquals(2, nodes.size());
 assertEquals("p", nodes.get(0).nodeName());
 assertEquals("p", nodes.get(1).nodeName());
 }

 // Test getParent method
 @Test
 public void testparent() {
 Element child = new Element("p");
 Element parent = new Element("div");
 child.setParentNode(parent);
 Node node = child;
 assertEquals(parent, node.parent());
 }

 // Test getOwnerDocument method
 @Test
 public void testownerDocument() {
 Element child = new Element("p");
 Document document = new Document();
 child.setOwnerDocument(document);
 Node node = child;
 assertEquals(document, node.ownerDocument());
 }

 // Test remove method
 @Test
 public void testremove() {
 Element parent = new Element("div");
 Element child = new Element("p");
 parent.addChildren(child);
 parent.remove();
 assertTrue(parent.childNodes().isEmpty());
 }

 // Test replaceWith method
 @Test
 public void testreplaceWith() {
 Element parent = new Element("div");
 Element child1 = new Element("p");
 Element child2 = new Element("p");
 parent.addChildren(child1);
 parent.replaceWith(child2);
 assertEquals(1, parent.childNodes().size());
 assertEquals("p", parent.childNodes().get(0).nodeName());
 }

 // Test setParentNode method
 @Test
 public void testsetParentNode() {
 Element child = new Element("p");
 Element parent1 = new Element("div");
 Element parent2 = new Element("div");
 child.setParentNode(parent1);
 child.setParentNode(parent2);
 assertEquals(parent2, child.parent());
 }

 // Test replaceChild method
 @Test
 public void testreplaceChild() {
 Element parent = new Element("div");
 Element oldChild = new Element("p");
 Element newChild = new Element("p");
 parent.addChildren(oldChild);
 parent.replaceChild(oldChild, newChild);
 assertEquals("p", parent.childNodes().get(0).nodeName());
 assertEquals("p", parent.childNodes().get(0).nodeName());
 }

 // Test removeChild method
 @Test
 public void testremoveChild() {
 Element parent = new Element("div");
 Element child1 = new Element("p");
 Element child2 = new Element("p");
 parent.addChildren(child1, child2);
 parent.removeChild(child1);
 assertEquals(1, parent.childNodes().size());
 assertEquals("p", parent.childNodes().get(0).nodeName());
 }

 // Test addChildren method
 @Test
 public void testaddChildren() {
 Element parent = new Element("div");
 Element child1 = new Element("p");
 Element child2 = new Element("p");
 parent.addChildren(child1, child2);
 assertEquals(2, parent.childNodes().size());
 }

 // Test addChildren method with array
 @Test
 public void testaddChildrenWithArray() {
 Element parent = new Element("div");
 Element child1 = new Element("p");
 Element child2 = new Element("p");
 Element child3 = new Element("p");
 parent.addChildren(new Element[] {child1, child2, child3});
 assertEquals(3, parent.childNodes().size());
 }

 // Test addChildren method with list
 @Test
 public void testaddChildrenWithList() {
 Element parent = new Element("div");
 List<Node> children = new ArrayList<>();
 children.add(new Element("p"));
 children.add(new Element("p"));
 children.add(new Element("p"));
 parent.addChildren(children);
 assertEquals(3, parent.childNodes().size());
 }

 // Test siblingNodes method
 @Test
 public void testsiblingNodes() {
 Element parent = new Element("div");
 Element sibling1 = new Element("p");
 Element sibling2 = new Element("p");
 Element child = new Element("p");
 parent.addChildren(sibling1, sibling2, child);
 List<Node> siblings = parent.siblingNodes();
 assertEquals(2, siblings.size());
 assertEquals("p", siblings.get(0).nodeName());
 assertEquals("p", siblings.get(1).nodeName());
 }

 // Test nextSibling method
 @Test
 public void testnextSibling() {
 Element parent = new Element("div");
 Element sibling1 = new Element("p");
 Element child = new Element("p");
 Element sibling2 = new Element("p");
 parent.addChildren(sibling1, child, sibling2);
 Node sibling = child.nextSibling();
 assertEquals("p", sibling.nodeName());
 }

 // Test previousSibling method
 @Test
 public void testpreviousSibling() {
 Element parent = new Element("div");
 Element sibling1 = new Element("p");
 Element child = new Element("p");
 Element sibling2 = new Element("p");
 parent.addChildren(sibling1, child, sibling2);
 Node sibling = child.previousSibling();
 assertEquals("p", sibling.nodeName());
 }

 // Test siblingIndex method
 @Test
 public void testsiblingIndex() {
 Element parent = new Element("div");
 Element sibling1 = new Element("p");
 Element child = new Element("p");
 Element sibling2 = new Element("p");
 parent.addChildren(sibling1, child, sibling2);
 assertEquals(1, child.siblingIndex());
 }

 // Test outerHtml method
 @Test
 public void testouterHtml() {
 Element element = new Element("div").attr("class", "test").attr("id", "testId");
 assertEquals("<div class=\"test\" id=\"testId\"></div>", element.outerHtml());
 }

 // Test outerHtmlHead method
 @Test
 public void testouterHtmlHead() {
 Element element = new Element("div").attr("class", "test").attr("id", "testId");
 StringBuilder accum = new StringBuilder();
 element.outerHtmlHead(accum, 0, new Document.OutputSettings());
 assertEquals("<div", accum.toString());
 }

 // Test outerHtmlTail method
 @Test
 public void testouterHtmlTail() {
 Element element = new Element("div").attr("class", "test").attr("id", "testId");
 StringBuilder accum = new StringBuilder();
 element.outerHtmlTail(accum, 0, new Document.OutputSettings());
 assertEquals("></div>", accum.toString());
 }

 // Test equals method
 @Test
 public void testequals() {
 Element element1 = new Element("div").attr("class", "test1").attr("id", "testId1");
 Element element2 = new Element("div").attr("class", "test2").attr("id", "testId2");
 assertFalse(element1.equals(element2));
 }

 // Test equals method with same object
 @Test
 public void testequalsWithSameObject() {
 Element element = new Element("div");
 assertTrue(element.equals(element));
 }

 // Test hashCode method
 @Test
 public void testhashCode() {
 Element element1 = new Element("div").attr("class", "test1").attr("id", "testId1");
 Element element2 = new Element("div").attr("class", "test2").attr("id", "testId2");
 int hashCode1 = element1.hashCode();
 int hashCode2 = element2.hashCode();
 assertNotEquals(hashCode1, hashCode2);
 }
}
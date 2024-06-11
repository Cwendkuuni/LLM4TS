import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

 private Node simpleNode;
 private Node parentNode;
 private Node textNode;
 private Node childNode1;
 private Node childNode2;
 private Document doc;

 @Before
 public void setUp() {
 simpleNode = new Element("simple") {
 @Override
 public String nodeName() {
 return "simple";
 }
 };
 parentNode = new Element("parent") {
 @Override
 public String nodeName() {
 return "parent";
 }
 };
 textNode = new TextNode("Hello World");
 childNode1 = new Element("child1") {
 @Override
 public String nodeName() {
 return "child1";
 }
 };
 childNode2 = new Element("child2") {
 @Override
 public String nodeName() {
 return "child2";
 }
 };
 doc = new Document();
 }

 @Test
 public void testConstructor_ValidParams() {
 Node node = new Element("node", "http://example.com") {
 @Override
 public String nodeName() {
 return "node";
 }
 };
 assertEquals("node", node.nodeName());
 assertEquals("http://example.com", node.baseUri());
 assertNotNull(node.attributes());
 }

 @Test
 public void testAttr_ValidAttributeKey() {
 simpleNode.attr("class", "foo");
 assertEquals("foo", simpleNode.attr("class"));
 }

 @Test
 public void testAttr_InvalidAttributeKey() {
 simpleNode.attr("invalidKey", "value");
 assertEquals("", simpleNode.attr("invalidKey"));
 }

 @Test
 public void testAttr_AbsUrl() {
 simpleNode.absUrl("href", "http://example.com");
 assertEquals("http://example.com", simpleNode.absUrl("href"));
 }

 @Test
 public void testAttr_MissingAttribute() {
 assertEquals("", simpleNode.attr("nonExistentAttribute"));
 }

 @Test
 public void testHasAttr() {
 simpleNode.attr("class", "foo");
 assertTrue(simpleNode.hasAttr("class"));
 }

 @Test
 public void testHasAttr_MissingAttribute() {
 assertFalse(simpleNode.hasAttr("nonExistentAttribute"));
 }

 @Test
 public void testRemoveAttr() {
 simpleNode.attr("class", "foo");
 simpleNode.removeAttr("class");
 assertFalse(simpleNode.hasAttr("class"));
 }

 @Test
 public void testSetBaseUri() {
 simpleNode.setBaseUri("http://example.com");
 assertEquals("http://example.com", simpleNode.baseUri());
 }

 @Test(expected = NullPointerException.class)
 public void testSetBaseUri_NullBaseUri() {
 simpleNode.setBaseUri(null);
 }

 @Test
 public void testChildNode() {
 parentNode.appendChild(simpleNode);
 Node child = parentNode.childNode(0);
 assertEquals(simpleNode, child);
 }

 @Test
 public void testChildNodes() {
 parentNode.appendChild(simpleNode);
 parentNode.appendChild(childNode1);
 List<Node> children = parentNode.childNodes();
 assertEquals(2, children.size());
 assertEquals(simpleNode, children.get(0));
 assertEquals(childNode1, children.get(1));
 }

 @Test
 public void testChildNodesAsArray() {
 parentNode.appendChild(simpleNode);
 parentNode.appendChild(childNode1);
 Node[] children = parentNode.childNodesAsArray();
 assertEquals(2, children.length);
 assertEquals(simpleNode, children[0]);
 assertEquals(childNode1, children[1]);
 }

 @Test
 public void testParent() {
 simpleNode.appendChild(textNode);
 assertEquals(simpleNode, textNode.parent());
 }

 @Test
 public void testParent_NullParent() {
 assertEquals(null, simpleNode.parent());
 }

 @Test
 public void testOwnerDocument() {
 simpleNode.appendChild(textNode);
 assertEquals(doc, simpleNode.ownerDocument());
 }

 @Test
 public void testOwnerDocument_NullOwnerDocument() {
 assertEquals(null, simpleNode.ownerDocument());
 }

 @Test
 public void testRemove() {
 parentNode.appendChild(simpleNode);
 parentNode.removeChild(simpleNode);
 assertFalse(parentNode.hasChild(simpleNode));
 }

 @Test
 public void testReplaceWith() {
 parentNode.appendChild(simpleNode);
 parentNode.appendChild(childNode1);
 parentNode.replaceChild(simpleNode, childNode2);
 assertFalse(parentNode.hasChild(simpleNode));
 assertTrue(parentNode.hasChild(childNode2));
 }

 @Test
 public void testSetParentNode() {
 simpleNode.setParentNode(parentNode);
 assertEquals(parentNode, simpleNode.parentNode);
 }

 @Test(expected = NullPointerException.class)
 public void testSetParentNode_NullParentNode() {
 simpleNode.setParentNode(null);
 }

 @Test
 public void testReplaceChild() {
 parentNode.appendChild(simpleNode);
 parentNode.appendChild(childNode1);
 Node out = parentNode.getChildNodes().get(0);
 parentNode.replaceChild(out, childNode2);
 assertEquals(childNode2, parentNode.getChildNodes().get(0));
 assertEquals(simpleNode, parentNode.getChildNodes().get(1));
 }

 @Test
 public void testAddChildren() {
 parentNode.appendChild(simpleNode);
 parentNode.addChildren(childNode1, childNode2);
 assertEquals(3, parentNode.childNodes().size());
 }

 @Test
 public void testAddChildren_NullChildren() {
 parentNode.appendChild(simpleNode);
 parentNode.addChildren(null);
 assertEquals(1, parentNode.childNodes().size());
 }

 @Test
 public void testAddChildren_DuplicateChildren() {
 parentNode.appendChild(simpleNode);
 parentNode.addChildren(simpleNode);
 assertEquals(2, parentNode.childNodes().size());
 }

 @Test
 public void testSiblingNodes() {
 parentNode.appendChild(simpleNode);
 parentNode.appendChild(childNode1);
 List<Node> siblings = parentNode.siblingNodes();
 assertEquals(2, siblings.size());
 assertEquals(childNode1, siblings.get(0));
 assertEquals(simpleNode, siblings.get(1));
 }

 @Test
 public void testNextSibling() {
 Node sibling = new Element("sibling") {
 @Override
 public String nodeName() {
 return "sibling";
 }
 };
 parentNode.appendChild(simpleNode);
 parentNode.appendChild(sibling);
 Node nextSibling = simpleNode.nextSibling();
 assertEquals(sibling, nextSibling);
 }

 @Test
 public void testNextSibling_NullNextSibling() {
 Node sibling = new Element("sibling") {
 @Override
 public String nodeName() {
 return "sibling";
 }
 };
 parentNode.appendChild(simpleNode);
 assertEquals(null, simpleNode.nextSibling());
 parentNode.appendChild(sibling);
 assertEquals(sibling, simpleNode.nextSibling());
 }

 @Test
 public void testPreviousSibling() {
 Node sibling = new Element("sibling") {
 @Override
 public String nodeName() {
 return "sibling";
 }
 };
 parentNode.appendChild(simpleNode);
 parentNode.appendChild(sibling);
 Node previousSibling = simpleNode.previousSibling();
 assertEquals(null, previousSibling);
 parentNode.removeChild(sibling);
 previousSibling = simpleNode.previousSibling();
 assertEquals(null, previousSibling);
 parentNode.appendChild(sibling);
 previousSibling = simpleNode.previousSibling();
 assertEquals(sibling, previousSibling);
 }

 @Test
 public void testPreviousSibling_NullPreviousSibling() {
 parentNode.appendChild(simpleNode);
 assertEquals(null, simpleNode.previousSibling());
 parentNode.appendChild(new TextNode(" "));
 Node previousSibling = simpleNode.previousSibling();
 assertEquals(null, previousSibling);
 }

 @Test
 public void testOuterHtml() {
 simpleNode.outerHtmlHead(accum, 0, out);
 simpleNode.outerHtml(accum, 0, out);
 simpleNode.outerHtmlTail(accum, 0, out);
 assertEquals("<simple></simple>\n", accum.toString());
 }

 @Test
 public void testOuterHtml_TextNode() {
 textNode.outerHtmlHead(accum, 0, out);
 textNode.outerHtml(accum, 0, out);
 textNode.outerHtmlTail(accum, 0, out);
 assertEquals("Hello World", accum.toString());
 }
}
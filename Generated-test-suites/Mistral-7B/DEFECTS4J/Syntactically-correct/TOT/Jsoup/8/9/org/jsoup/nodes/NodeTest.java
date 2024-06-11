import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

 private Node rootNode;

 @Before
 public void setUp() {
 String baseUri = "http://example.com";
 Attributes attributes = new Attributes();
 attributes.put("id", "root");
 rootNode = new Element("html", baseUri, attributes);
 }

 @Test
 public void testNodeName() {
 Node node = new TextNode("Test Node");
 assertEquals("Test Node", node.nodeName());
 }

 @Test
 public void testAttr() {
 Element linkEl = new Element("a", rootNode.baseUri(), attributes());
 linkEl.attr("href", "test.html");
 assertEquals("test.html", linkEl.attr("href"));
 assertEquals("", linkEl.attr("nonexistent"));
 }

 @Test
 public void testAttributes() {
 Element pEl = new Element("p", rootNode.baseUri(), attributes());
 pEl.attr("class", "test");
 pEl.attr("style", "color:red;");
 assertEquals("class=test style=color:red;", pEl.attributes().toString());
 }

 @Test
 public void testHasAttr() {
 Element imgEl = new Element("img", rootNode.baseUri(), attributes());
 imgEl.attr("src", "test.png");
 assertTrue(imgEl.hasAttr("src"));
 assertFalse(imgEl.hasAttr("nonexistent"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeChildSize() {
 Node parentNode = new Element("div", rootNode.baseUri(), attributes());
 List<Node> children = new ArrayList<Node>(5);
 parentNode.addChildren(children.toArray(new Node[5]));
 try {
 parentNode.addChildren(-1, new Element("p", rootNode.baseUri(), attributes()));
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAbsUrl() {
 Element linkEl = new Element("a", rootNode.baseUri(), attributes());
 linkEl.attr("href", "test.html");
 assertEquals("http://example.com/test.html", linkEl.absUrl("href"));
 assertEquals("", linkEl.absUrl("nonexistent"));
 }

 @Test(expected = MalformedURLException.class)
 public void testMalformedBaseUri() {
 String invalidBaseUri = "javascript:alert('Invalid Base URI');";
 try {
 new Node(invalidBaseUri, new Attributes());
 fail();
 } catch (MalformedURLException e) {
 // expected
 }
 }

 @Test
 public void testChildNodes() {
 Element pEl = new Element("p", rootNode.baseUri(), attributes());
 Element childEl = new Element("span", rootNode.baseUri(), attributes());
 pEl.addChild(childEl);
 Node[] children = pEl.childNodesAsArray();
 assertEquals(1, children.length);
 assertEquals(childEl, children[0]);
 }

 @Test
 public void testParent() {
 Element pEl = new Element("p", rootNode.baseUri(), attributes());
 Element childEl = new Element("span", rootNode.baseUri(), attributes());
 pEl.addChild(childEl);
 assertEquals(pEl, childEl.parent());
 }

 @Test
 public void testRemoveChild() {
 Element pEl = new Element("p", rootNode.baseUri(), attributes());
 Element childEl = new Element("span", rootNode.baseUri(), attributes());
 pEl.addChild(childEl);
 pEl.removeChild(childEl);
 assertEquals(0, pEl.childNodes().size());
 }

 @Test
 public void testReplaceChild() {
 Element pEl = new Element("p", rootNode.baseUri(), attributes());
 Element oldChildEl = new Element("span", rootNode.baseUri(), attributes());
 Element newChildEl = new Element("i", rootNode.baseUri(), attributes());
 pEl.addChild(oldChildEl);
 pEl.replaceChild(oldChildEl, newChildEl);
 assertEquals(1, pEl.childNodes().size());
 assertEquals(newChildEl, pEl.childNodes().get(0));
 }

 @Test
 public void testListOperations() {
 Element parentEl = new Element("div", rootNode.baseUri(), attributes());
 Node[] children = new Node[3];
 for (int i = 0; i < children.length; i++) {
 children[i] = new Element("p", rootNode.baseUri(), attributes());
 parentEl.addChild(children[i]);
 }
 List<Node> childList = parentEl.childNodes();
 assertEquals(3, childList.size());
 for (int i = 0; i < children.length; i++) {
 assertEquals(children[i], childList.get(i));
 }
 }

 @Test
 public void testSiblingNodes() {
 Element p1 = new Element("p", rootNode.baseUri(), attributes());
 Element p2 = new Element("p", rootNode.baseUri(), attributes());
 Element p3 = new Element("p", rootNode.baseUri(), attributes());
 rootNode.addChildren(p1, p2, p3);
 assertEquals(p1, p2.previousSibling());
 assertEquals(p3, p2.nextSibling());
 }

 @Test
 public void testSetBaseUri() {
 Element newRoot = new Element("html", "http://newbase.com", attributes());
 Node oldRoot = rootNode;
 rootNode = newRoot;
 assertEquals("http://newbase.com", rootNode.baseUri());
 assertEquals("http://newbase.com", oldRoot.absUrl("href"));
 }
}
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.*;

public class NodeTest {

 private Node rootNode;

 @Before
 public void setUp() {
 rootNode = new Document().parse("<html><head></head><body><div id='test'><p>Test</p></div></body></html>").getElementsByTag("div").first();
 }

 @Test
 public void testNodeName() {
 assertEquals("div", rootNode.nodeName());
 }

 @Test
 public void testAttr() {
 Element testDiv = (Element) rootNode;
 testDiv.attr("class", "testClass");
 assertEquals("testClass", testDiv.attr("class"));
 }

 @Test(expected = NullPointerException.class)
 public void testNullAttributeKey() {
 rootNode.attr(null, "value");
 }

 @Test(expected = NullPointerException.class)
 public void testNullParentUri() {
 Node node = new Node("", null);
 node.setBaseUri(null);
 assertNull(node.absUrl("test"));
 }

 @Test(expected = MalformedURLException.class)
 public void testMalformedBaseUri() {
 Node node = new Node("http://localhost:8080/", null);
 try {
 node.setBaseUri("java:/invalid/path");
 } catch (IllegalArgumentException e) {
 assertEquals("baseUri cannot be set to java:/", e.getMessage());
 }
 }

 @Test
 public void testChildNodes() {
 Node pNode = rootNode.childNodes().get(1);
 assertEquals("p", pNode.nodeName());
 assertEquals(1, pNode.childNodes().size());
 TextNode textNode = (TextNode) pNode.childNodes().get(0);
 assertEquals("Test", textNode.text());
 }

 @Test
 public void testParentNode() {
 Element divNode = (Element) rootNode;
 Node pNode = divNode.childNodes().get(0);
 assertEquals(divNode, pNode.parent());
 }

 @Test
 public void testRemoveChild() {
 Element divNode = (Element) rootNode;
 Node pNode = divNode.childNodes().get(0);
 divNode.removeChild(pNode);
 assertEquals(0, divNode.childNodes().size());
 }

 @Test
 public void testReplaceChild() {
 Element divNode = (Element) rootNode;
 Node pNode = divNode.childNodes().get(0);
 Node newNode = new Element("span");
 divNode.replaceChild(pNode, newNode);
 assertEquals(1, divNode.childNodes().size());
 assertEquals("span", divNode.childNodes().get(0).nodeName());
 }

 @Test
 public void testAttributes() {
 Element divNode = (Element) rootNode;
 Attributes attributes = divNode.attributes();
 attributes.put("data-test", "testValue");
 assertEquals("testValue", divNode.attr("data-test"));
 }
}
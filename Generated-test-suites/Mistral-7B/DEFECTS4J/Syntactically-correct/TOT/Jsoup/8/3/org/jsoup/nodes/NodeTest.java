import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

 private Node rootNode;

 @Before
 public void setUp() {
 String baseUri = "http://example.com";
 rootNode = new Element("html", new Attributes().put("lang", "en"));
 rootNode.setBaseUri(baseUri);
 }

 @Test
 public void testNodeName() {
 Element pElement = new Element("p");
 assertEquals("p", pElement.nodeName());
 }

 @Test
 public void testAttr() {
 Element linkElement = new Element("a", new Attributes().put("href", "http://example.com"));
 assertEquals("http://example.com", linkElement.attr("href"));
 assertEquals("", linkElement.attr("unknown"));
 Element imgElement = new Element("img", new Attributes().put("src", "image.png"));
 assertEquals("image.png", imgElement.absUrl("src"));
 }

 @Test
 public void testAttributes() {
 Element pElement = new Element("p", null);
 pElement.attr("class", "myclass");
 pElement.attr("id", "myid");
 Element qElement = new Element("q");
 pElement.appendChild(qElement);
 Element spanElement = new Element("span", new Attributes().put("title", "My Title"));
 pElement.appendChild(spanElement);
 assertEquals(2, pElement.childNodes().size());
 assertEquals("myclass", pElement.attr("class"));
 assertEquals("My Title", spanElement.attr("title"));
 assertEquals(Collections.singletonMap("class", "myclass"), pElement.attributes().asMap());
 }

 @Test
 public void testHasAttr() {
 Element pElement = new Element("p", new Attributes().put("class", "myclass"));
 assertTrue(pElement.hasAttr("class"));
 assertFalse(pElement.hasAttr("unknown"));
 }

 @Test
 public void testRemoveAttr() {
 Element pElement = new Element("p", new Attributes().put("class", "myclass"));
 pElement.removeAttr("class");
 assertFalse(pElement.hasAttr("class"));
 }

 @Test
 public void testBaseUri() {
 Element linkElement = new Element("a", new Attributes().put("href", "page.html"));
 linkElement.setBaseUri("http://example.com");
 assertEquals("http://example.com/page.html", linkElement.absUrl("href"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 Node node = new Node("", null); // use default constructor, no baseUri
 assertEquals("", node.baseUri());
 try {
 node.absUrl("href");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testChildNodes() {
 Element pElement = new Element("p");
 TextNode textNode = new TextNode("The quick brown fox jumps over the lazy dog.");
 pElement.appendChild(textNode);
 assertEquals(1, pElement.childNodes().size());
 assertEquals("The quick brown fox jumps over the lazy dog.", textNode.text());
 }

 @Test
 public void testGetChildNodeByIndex() {
 Element pElement = new Element("p");
 TextNode textNode1 = new TextNode("The quick brown fox jumps");
 TextNode textNode2 = new TextNode(" over the lazy dog.");
 pElement.appendChild(textNode1);
 pElement.appendChild(textNode2);
 assertEquals(textNode1, pElement.childNode(0));
 assertEquals(textNode2, pElement.childNode(1));
 }

 @Test
 public void testGetParentNode() {
 Element pElement = new Element("p");
 Element divElement = new Element("div", new Attributes().put("id", "myDiv"));
 divElement.appendChild(pElement);
 assertEquals(divElement, pElement.parent());
 assertEquals(null, pElement.parent());
 }

 @Test
 public void testOwnerDocument() {
 Element pElement = new Element("p");
 Document document = new Document();
 document.appendChild(pElement);
 assertEquals(document, pElement.ownerDocument());
 }

 @Test
 public void testRemove() {
 Element pElement = new Element("p");
 Element divElement = new Element("div", new Attributes().put("id", "myDiv"));
 divElement.appendChild(pElement);
 pElement.remove();
 assertEquals(0, divElement.childNodes().size());
 }

 @Test
 public void testReplaceWith() {
 Element pElement = new Element("p");
 Element divElement = new Element("div", new Attributes().put("id", "myDiv"));
 divElement.appendChild(pElement);
 Element newPElement = new Element("p", new Attributes().put("class", "newClass"));
 pElement.replaceWith(newPElement);
 assertEquals("newClass", divElement.childNode(0).attributes().get("class"));
 }

 @Test
 public void testTraverseWithNodeTraversor() {
 Element divElement = new Element("div");
 Element pElement1 = new Element("p", new Attributes().put("class", "class1"));
 Element pElement2 = new Element("p", new Attributes().put("class", "class2"));
 divElement.appendChild(pElement1);
 divElement.appendChild(pElement2);
 Elements pElements = divElement.select("p");
 assertFalse(pElements.isEmpty());
 assertEquals(2, pElements.size());
 }

 @Test
 public void testOuterHtml() {
 Element pElement = new Element("p");
 pElement.outerHtmlHead(new StringBuilder(), 1, null);
 pElement.outerHtmlTail(new StringBuilder(), 1, null);
 assertEquals("<p></p>", pElement.outerHtml());
 TextNode textNode = new TextNode("Inner text");
 pElement.appendChild(textNode);
 pElement.outerHtmlHead(new StringBuilder(), 1, null);
 pElement.outerHtmlTail(new StringBuilder(), 1, null);
 assertEquals("<p>Inner text</p>", pElement.outerHtml());
 }
}
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.Attributes;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

 private Node rootNode;

 @Before
 public void setup() {
 rootNode = new Document().parse("<html><body><div id='test'><p>Hello World</p></div></body></html>").getElementsByTag("html").first();
 }

 @Test
 public void testNodeName() {
 assertEquals("html", rootNode.nodeName());
 }

 @Test
 public void testAttr() {
 Element divElement = (Element) rootNode.getElementsByTag("div").first();
 Attribute idAttribute = divElement.attr("id");
 assertEquals("test", idAttribute.getValue());
 }

 @Test
 public void testAttrAbsUrl() {
 Element imgElement = (Element) rootNode.getElementsByTag("img").first();
 String absUrl = imgElement.absUrl("src");
 assertTrue(absUrl.startsWith("http://"));
 }

 @Test
 public void testGetText() {
 Element pElement = (Element) rootNode.getElementsByTag("p").first();
 String text = pElement.text();
 assertEquals("Hello World", text);
 }

 @Test
 public void testHasAttr() {
 Element divElement = (Element) rootNode.getElementsByTag("div").first();
 assertTrue(divElement.hasAttr("id"));
 }

 @Test
 public void testRemoveAttr() {
 Element divElement = (Element) rootNode.getElementsByTag("div").first();
 divElement.removeAttr("id");
 assertFalse(divElement.hasAttr("id"));
 }

 @Test
 public void testSetAttr() {
 Element divElement = (Element) rootNode.getElementsByTag("div").first();
 divElement.attr("data-test", "value");
 assertEquals("value", divElement.attr("data-test"));
 }

 @Test
 public void testChildNodes() {
 Element divElement = (Element) rootNode.getElementsByTag("div").first();
 Node firstChild = divElement.childNodes().get(0);
 assertEquals("p", firstChild.nodeName());
 }

 @Test
 public void testChildNodesAsArray() {
 Element divElement = (Element) rootNode.getElementsByTag("div").first();
 Node[] children = divElement.childNodesAsArray();
 assertEquals(1, children.length);
 assertEquals("p", children[0].nodeName());
 }

 @Test
 public void testParent() {
 Element divElement = (Element) rootNode.getElementsByTag("div").first();
 assertEquals(rootNode, divElement.parent());
 }

 @Test
 public void testRemove() {
 Element divElement = (Element) rootNode.getElementsByTag("div").first();
 divElement.remove();
 assertFalse(rootNode.getElementsByTag("div").isEmpty());
 }

 @Test
 public void testReplaceWith() {
 Element divElement = (Element) rootNode.getElementsByTag("div").first();
 Element replacement = new Element("span");
 divElement.replaceWith(replacement);
 assertEquals(1, rootNode.getElementsByTag("span").size());
 assertEquals(0, rootNode.getElementsByTag("div").size());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetParentNull() {
 Element divElement = (Element) rootNode.getElementsByTag("div").first();
 divElement.setParentNode(null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testChildNodeNegativeIndex() {
 Element divElement = (Element) rootNode.getElementsByTag("div").first();
 Node child = divElement.childNode(-1);
 assertNull(child);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testChildNodeIndexOutOfBounds() {
 Element divElement = (Element) rootNode.getElementsByTag("div").first();
 Node child = divElement.childNodes().get(divElement.childNodes().size());
 assertNull(child);
 }

 @Test
 public void testHasTextChild() {
 Element divElement = (Element) rootNode.getElementsByTag("div").first();
 TextNode textNode = (TextNode) divElement.childNodes().get(0);
 assertTrue(textNode.text().isBlank());
 }

 @Test
 public void testGetTextWithLeadingAndTrailingWhitespace() {
 Element divElement = (Element) rootNode.getElementsByTag("div").first();
 TextNode textNode = (TextNode) divElement.childNodes().get(0);
 String text = textNode.text();
 assertEquals(" Hello World ", text);
 }

 @Test
 public void testGetTextWithLeadingAndTrailingNonWhitespace() {
 Element divElement = (Element) rootNode.getElementsByTag("div").first();
 TextNode textNode = (TextNode) divElement.childNodes().get(0);
 String text = textNode.text();
 assertEquals("Hello World", text);
 }

 @Test
 public void testGetTextWithLeadingWhitespace() {
 Element pElement = (Element) rootNode.getElementsByTag("p").first();
 TextNode textNode = (TextNode) pElement.childNodes().get(0);
 String text = textNode.text();
 assertEquals(" Hello World", text);
 }

 @Test
 public void testGetTextWithTrailingWhitespace() {
 Element pElement = (Element) rootNode.getElementsByTag("p").first();
 TextNode textNode = (TextNode) pElement.childNodes().get(pElement.childNodes().size() - 1);
 String text = textNode.text();
 assertEquals("Hello World ", text);
 }

 @Test
 public void testGetTextWithMultipleTextNodes() {
 Element divElement = (Element) rootNode.getElementsByTag("div").first();
 TextNode firstTextNode = (TextNode) divElement.childNodes().get(0);
 TextNode secondTextNode = (TextNode) divElement.childNodes().get(1);
 String text = firstTextNode.text() + secondTextNode.text();
 assertEquals("Hello World", text);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetParentNodeInvalid() {
 Element divElement = (Element) rootNode.getElementsByTag("div").first();
 Node invalidParent = new Document();
 divElement.setParentNode(invalidParent);
 }

 @Test
 public void testSetSiblingIndex() {
 Element divElement = (Element) rootNode.getElementsByTag("div").first();
 divElement.setSiblingIndex(1);
 Element sibling = (Element) rootNode.getElementsByTag("div").get(1);
 assertEquals(divElement, sibling.previousSibling());
 }

 @Test
 public void testSetSiblingIndexNegative() {
 Element divElement = (Element) rootNode.getElementsByTag("div").first();
 divElement.setSiblingIndex(-1);
 assertNull(divElement.previousSibling());
 }

 @Test
 public void testSetSiblingIndexOutOfBounds() {
 Element divElement = (Element) rootNode.getElementsByTag("div").first();
 divElement.setSiblingIndex(rootNode.getElementsByTag("div").size());
 assertNull(divElement.nextSibling());
 }
}
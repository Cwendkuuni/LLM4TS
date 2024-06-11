import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import static org.junit.Assert.*;

public class NodeTest {

 private Node node;
 private Document document;

 @Before
 public void setUp() {
 document = new Document();
 node = new Element("div", "div", document.baseUri(), new Attributes());
 }

 @Test
 public void testNodeName() {
 String expected = "div";
 String actual = node.nodeName();
 assertEquals(expected, actual);
 }

 @Test
 public void testAttr() {
 Attributes attributes = new Attributes();
 attributes.put("class", "testClass");
 node.attr("class", attributes);
 String actual = node.attr("class");
 String expected = "testClass";
 assertEquals(expected, actual);
 }

 @Test
 public void testHasAttr() {
 assertTrue(node.hasAttr("class"));
 assertFalse(node.hasAttr("nonexistent"));
 }

 @Test
 public void testRemoveAttr() {
 node.removeAttr("class");
 assertFalse(node.hasAttr("class"));
 }

 @Test
 public void testAbsUrl() {
 String baseUri = "http://example.com";
 node.setAttribute("href", "relative.html");
 String actual = node.absUrl("href");
 String expected = "http://example.com/relative.html";
 assertEquals(expected, actual);
 }

 @Test
 public void testChildNodes() {
 Element child = new Element("p", "p", document.baseUri(), new Attributes());
 node.appendChild(child);
 List<Node> actual = node.childNodes();
 List<Node> expected = Arrays.asList(child);
 assertEquals(expected.size(), actual.size());
 for (int i = 0; i < expected.size(); i++) {
 Node expectedNode = expected.get(i);
 Node actualNode = actual.get(i);
 assertEquals(expectedNode.getClass(), actualNode.getClass());
 assertEquals(expectedNode.nodeName(), actualNode.nodeName());
 }
 }

 @Test
 public void testParent() {
 Document parentDocument = new Document();
 Element parent = new Element("body", "body", parentDocument.baseUri(), new Attributes());
 parentDocument.appendChild(parent);
 parent.appendChild(node);
 Node actual = node.parent();
 assertEquals(parent.getClass(), actual.getClass());
 assertEquals(parent, actual);
 }

 @Test
 public void testOwnerDocument() {
 Document actual = node.ownerDocument();
 assertEquals(document.getClass(), actual.getClass());
 assertEquals(document, actual);
 }

 @Test
 public void testRemove() {
 Document doc = new Document();
 Element parent = new Element("body", "body", doc.baseUri(), new Attributes());
 doc.appendChild(parent);
 parent.appendChild(node);
 node.remove();
 boolean actual = parent.childNodes().isEmpty();
 assertTrue(actual);
 }

 @Test
 public void testReplaceWith() {
 Document doc = new Document();
 Element parent = new Element("body", "body", doc.baseUri(), new Attributes());
 doc.appendChild(parent);
 Element child = new Element("p", "p", doc.baseUri(), new Attributes());
 parent.appendChild(node);
 node.replaceWith(child);
 int actualSize = parent.childNodes().size();
 int expectedSize = 1;
 assertEquals(expectedSize, actualSize);
 }
}
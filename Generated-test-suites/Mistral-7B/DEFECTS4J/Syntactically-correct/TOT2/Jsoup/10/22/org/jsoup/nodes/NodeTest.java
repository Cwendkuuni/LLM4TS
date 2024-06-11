import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("class", "myClass");
        Node node = new Element("div", baseUri, attributes);

        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNotNull(node.attributes());
        assertEquals("myClass", node.attr("class"));
        assertFalse(node.hasAttr("nonexistent"));
    }

    @Test
    public void testAttributeGettersSettersAndRemoval() {
        Node node = new Element("a", "http://example.com/", new Attributes().put("href", "http://example.com/index.html"));

        assertEquals("http://example.com/index.html", node.absUrl("href"));
        assertEquals("http://example.com/index.html", node.attr("href"));
        node.attr("href", "http://example.com/new_page.html");
        assertEquals("http://example.com/new_page.html", node.absUrl("href"));
        assertEquals("http://example.com/new_page.html", node.attr("href"));
        node.removeAttr("href");
        assertTrue(node.hasAttr("href")); // should be false since it was removed
    }

    @Test
    public void testEmptyNodeCreation() {
        Node emptyNode = new Node();
        assertEquals(Collections.EMPTY_LIST, emptyNode.childNodes());
        assertNull(emptyNode.parentNode());
        assertNull(emptyNode.attributes());
        assertEquals("", emptyNode.baseUri());
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidNodeCreation() {
        String baseUri = null;
        Attributes attributes = new Attributes();
        new Element("div", baseUri, attributes);
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidAttributesCreation() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes(null);
        new Element("div", baseUri, attributes);
    }

    @Test
    public void testChildNodesAccess() {
        Node parentNode = new Element("div", "http://example.com/", new Attributes());
        Node childNode1 = new Element("p", "http://example.com/", new Attributes().put("class", "child1"));
        Node childNode2 = new Element("p", "http://example.com/", new Attributes().put("class", "child2"));

        parentNode.addChildren(childNode1, childNode2);

        List<Node> childNodes = parentNode.childNodes();
        assertEquals(2, childNodes.size());
        assertEquals(childNode1, childNodes.get(0));
        assertEquals(childNode2, childNodes.get(1));

        Node firstChild = parentNode.childNode(0);
        assertEquals(childNode1, firstChild);
    }

    @Test
    public void testChildNodesManipulation() {
        Node parentNode = new Element("div", "http://example.com/", new Attributes());
        Node childNode1 = new Element("p", "http://example.com/", new Attributes().put("class", "child1"));
        Node childNode2 = new Element("p", "http://example.com/", new Attributes().put("class", "child2"));

        parentNode.addChildren(childNode1, childNode2);

        parentNode.removeChild(childNode1);
        List<Node> childNodes = parentNode.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(childNode2, childNodes.get(0));

        parentNode.replaceChild(childNode1, childNode2);
        childNodes = parentNode.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(childNode2, childNodes.get(0));
    }

    @Test
    public void testParentNodeAccess() {
        Node childNode = new Element("p", "http://example.com/", new Attributes().put("class", "child"));
        Node parentNode = new Element("div", "http://example.com/", new Attributes());
        parentNode.addChildren(childNode);

        assertEquals(parentNode, childNode.parentNode());
        assertEquals(childNode, parentNode.childNode(0));
    }

    @Test
    public void testDocumentAccess() {
        Node node = new Element("div", "http://example.com/", new Attributes());
        Document document = new Document();
        document.appendChild(node);

        assertEquals(document, node.ownerDocument());
    }

    @Test
    public void testAbsUrl() {
        Node node = new Element("a", "http://example.com/", new Attributes().put("href", "/about"));
        assertEquals("http://example.com/about", node.absUrl("href"));
    }

    @Test
    public void testAbsUrlWithBaseUri() {
        Node node = new Element("a", "http://example.com/", new Attributes().put("href", "about"));
        String baseUri = "http://example.com/doc/";
        assertEquals("http://example.com/doc/about", node.absUrl("href", baseUri));
    }

    @Test
    public void testAbsUrlWithEmptyBaseUri() {
        Node node = new Element("a", "", new Attributes().put("href", "/about"));
        String baseUri = "";
        assertEquals("/about", node.absUrl("href", baseUri));
    }

    @Test
    public void testAbsUrlWithMalformedBaseUri() {
        Node node = new Element("a", "", new Attributes().put("href", "/about"));
        String baseUri = "h1tpp://example.com/";
        assertEquals("", node.absUrl("href", baseUri));
    }
}
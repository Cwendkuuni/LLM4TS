import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;

public class NodeTest {

    @Test
    public void testNodeConstruction() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "example");

        Node node1 = new Node(baseUri, attributes);
        Node node2 = new Node(baseUri);
        Node node3 = new Node();

        assertEquals("example", node1.nodeName());
        assertEquals("http://example.com", node1.baseUri());
        assertEquals(attributes, node1.attributes());

        assertEquals(null, node2.nodeName());
        assertEquals("http://example.com", node2.baseUri());
        assertEquals(new Attributes(), node2.attributes());

        assertEquals(null, node3.nodeName());
        assertEquals(Collections.EMPTY_LIST, node3.childNodes());
        assertEquals(null, node3.attributes());
        assertEquals(null, node3.baseUri());
    }

    @Test
    public void testAbstractMethods() {
        throw new UnsupportedOperationException("Abstract methods cannot be tested directly.");
    }

    @Test
    public void testGetAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "example");

        Node node = new Element("div", baseUri, attributes);
        String attributeValue = node.attr("class");

        assertEquals("example", attributeValue);
    }

    @Test
    public void testGetAllAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        attributes.put("id", "exampleId");

        Node node = new Element("div", baseUri, attributes);

        Attributes attributesTest = node.attributes();

        assertEquals("example", attributesTest.get("class"));
        assertEquals("exampleId", attributesTest.get("id"));
    }

    @Test
    public void testSetAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node node = new Element("div", baseUri, attributes);

        node.attr("class", "example");
        node.attr("id", "exampleId");

        Attributes attributesTest = node.attributes();

        assertEquals("example", attributesTest.get("class"));
        assertEquals("exampleId", attributesTest.get("id"));
    }

    @Test
    public void testHasAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "example");

        Node node = new Element("div", baseUri, attributes);

        assertTrue(node.hasAttr("class"));
        assertTrue(node.hasAttr("id"));
        assertFalse(node.hasAttr("nonExistentAttribute"));
    }

    @Test
    public void testRemoveAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "example");

        Node node = new Element("div", baseUri, attributes);

        node.removeAttr("class");

        Attributes attributesTest = node.attributes();

        assertFalse(attributesTest.hasKey("class"));
    }

    @Test
    public void testGetBaseUri() {
        String baseUri = "http://example.com";
        Node node = new Element("div", baseUri, new Attributes());

        assertEquals(baseUri, node.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        String oldBaseUri = "http://oldexample.com";
        String newBaseUri = "http://newexample.com";
        Node node = new Element("div", oldBaseUri, new Attributes());

        node.setBaseUri(newBaseUri);

        assertEquals(newBaseUri, node.baseUri());
    }

    @Test
    public void testGetChildNode() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode = new Element("p", baseUri, attributes);

        parentNode.appendChild(childNode);

        Node childNodeTest = parentNode.childNode(0);

        assertEquals(childNode, childNodeTest);
    }

    @Test
    public void testGetChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode1 = new Element("p", baseUri, attributes);
        Node childNode2 = new Element("p", baseUri, attributes);

        parentNode.appendChild(childNode1);
        parentNode.appendChild(childNode2);

        List<Node> childrenTest = parentNode.childNodes();

        assertEquals(2, childrenTest.size());
        assertEquals(childNode1, childrenTest.get(0));
        assertEquals(childNode2, childrenTest.get(1));
    }

    @Test
    public void testGetParent() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node childNode = new Element("p", baseUri, attributes);
        Node parentNode = new Element("div", baseUri, attributes);

        parentNode.appendChild(childNode);
        childNode.setParentNode(parentNode);

        assertEquals(parentNode, childNode.parent());
        assertEquals(childNode, parentNode.childNode(0));
    }

    @Test
    public void testRemove() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode = new Element("p", baseUri, attributes);

        parentNode.appendChild(childNode);

        parentNode.removeChild(childNode);

        List<Node> childrenTest = parentNode.childNodes();

        assertEquals(0, childrenTest.size());
    }

    @Test
    public void testReplaceWith() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Element("div", baseUri, attributes);
        Node oldChildNode = new Element("p", baseUri, attributes);
        Node newChildNode = new Element("p", baseUri, attributes);

        parentNode.appendChild(oldChildNode);

        parentNode.replaceChild(oldChildNode, newChildNode);

        List<Node> childrenTest = parentNode.childNodes();

        assertEquals(1, childrenTest.size());
        assertEquals(newChildNode, childrenTest.get(0));
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("class", "example");

        Node node = new Element("div", baseUri, attributes);

        Node clonedNode = node.clone();

        assertEquals(node.nodeName(), clonedNode.nodeName());
        assertEquals(node.baseUri(), clonedNode.baseUri());
        assertEquals(node.attributes(), clonedNode.attributes());

        List<Node> childrenTest = node.childNodes();
        List<Node> clonedChildrenTest = clonedNode.childNodes();

        assertEquals(childrenTest.size(), clonedChildrenTest.size());

        for (int i = 0; i < childrenTest.size(); i++) {
            Node child = childrenTest.get(i);
            Node clonedChild = clonedChildrenTest.get(i);

            assertEquals(child.nodeName(), clonedChild.nodeName());
            assertEquals(child.baseUri(), clonedChild.baseUri());
            assertEquals(child.attributes(), clonedChild.attributes());
        }
    }
}
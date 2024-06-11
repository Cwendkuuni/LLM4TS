import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");

        Node node = new Node(baseUri, attributes);

        assertEquals("testNode", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
        assertNull(node.parentNode());
        assertEquals(Collections.EMPTY_LIST, node.childNodes());
    }

    @Test
    public void testNodeCreationWithEmptyAttributes() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);

        assertEquals("", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNull(node.attributes());
        assertNull(node.parentNode());
        assertEquals(Collections.EMPTY_LIST, node.childNodes());
    }

    @Test
    public void testNodeCreationWithoutBaseUriOrAttributes() {
        Node node = new Node();

        assertNull(node.nodeName());
        assertNull(node.baseUri());
        assertNull(node.attributes());
        assertNull(node.parentNode());
        assertEquals(Collections.EMPTY_LIST, node.childNodes());
    }

    // Add test cases for other methods here...
}
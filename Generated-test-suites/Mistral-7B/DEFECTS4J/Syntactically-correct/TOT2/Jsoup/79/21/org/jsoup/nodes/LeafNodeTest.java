import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.LeafNode;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new TextNode("Test");
        assertFalse(leafNode.hasAttributes());

        LeafNode leafNodeWithAttributes = new Attributes().put("test", "attribute");
        assertTrue(leafNodeWithAttributes.hasAttributes());
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new TextNode("Test");
        Attributes attributes = leafNode.attributes();
        assertNull(attributes);

        LeafNode leafNodeWithAttributes = new Attributes().put("test", "attribute");
        Attributes attributes1 = leafNodeWithAttributes.attributes();
        assertEquals("test", attributes1.getClass().getSimpleName());
        assertEquals("attribute", attributes1.get("test"));
    }

    @Test(expected = NullPointerException.class)
    public void testCoreValueWithNullNodeName() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.coreValue(null);
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new TextNode("Test");
        String coreValue = leafNode.coreValue();
        assertEquals("Test", coreValue);
    }

    @Test
    public void testCoreValueWithEmptyString() {
        LeafNode leafNode = new TextNode("");
        String coreValue = leafNode.coreValue();
        assertEquals("", coreValue);
    }

    @Test
    public void testCoreValueWithInvalidString() {
        LeafNode leafNode = new TextNode("\u0001");
        String coreValue = leafNode.coreValue();
        assertEquals("\u0001", coreValue);
    }

    @Test
    public void testAttributesWithNullKey() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.attr(null, "Test");
    }

    @Test
    public void testAttributesWithEmptyStringKey() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.attr("", "Test");
    }

    @Test
    public void testAttributesWithInvalidStringKey() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.attr("\u0001", "Test");
    }

    @Test
    public void testAttributesWithSameKey() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.attr("test", "attribute1");
        leafNode.attr("test", "attribute2");
        Attributes attributes = leafNode.attributes();
        assertEquals("attribute2", attributes.get("test"));
    }

    @Test
    public void testHasAttrWithNullKey() {
        LeafNode leafNode = new TextNode("Test");
        assertFalse(leafNode.hasAttr(null));
    }

    @Test
    public void testHasAttrWithEmptyStringKey() {
        LeafNode leafNode = new TextNode("Test");
        assertFalse(leafNode.hasAttr(""));
    }

    @Test
    public void testHasAttrWithInvalidStringKey() {
        LeafNode leafNode = new TextNode("Test");
        assertFalse(leafNode.hasAttr("\u0001"));
    }

    @Test
    public void testRemoveAttrWithNullKey() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.removeAttr(null);
        Attributes attributes = leafNode.attributes();
        assertNotNull(attributes);
        assertTrue(attributes.isEmpty());
    }

    @Test
    public void testRemoveAttrWithEmptyStringKey() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.attr("test", "Test");
        leafNode.removeAttr("");
        Attributes attributes = leafNode.attributes();
        assertNotNull(attributes);
        assertFalse(attributes.isEmpty());
        assertFalse(attributes.hasKey("test"));
    }

    @Test
    public void testRemoveAttrWithInvalidStringKey() {
        LeafNode leafNode = new TextNode("Test");
        leafNode.attr("\u0001", "Test");
        leafNode.removeAttr("\u0001");
        Attributes attributes = leafNode.attributes();
        assertNotNull(attributes);
        assertFalse(attributes.isEmpty());
        assertFalse(attributes.hasKey("\u0001"));
    }

    @Test
    public void testAbsUrlWithNullKey() {
        LeafNode leafNode = new TextNode("Test");
        String absUrl = leafNode.absUrl(null);
        assertEquals("", absUrl);
    }

    @Test
    public void testAbsUrlWithEmptyStringKey() {
        LeafNode leafNode = new TextNode("Test");
        String absUrl = leafNode.absUrl("");
        assertEquals("", absUrl);
    }

    @Test
    public void testAbsUrlWithInvalidStringKey() {
        LeafNode leafNode = new TextNode("Test");
        String absUrl = leafNode.absUrl("\u0001");
        assertEquals("", absUrl);
    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new TextNode("Test");
        String baseUri = leafNode.baseUri();
        assertEquals("", baseUri);

        TextNode parentNode = new TextNode("Parent");
        leafNode.parent(parentNode);
        String baseUri1 = leafNode.baseUri();
        assertEquals("Parent", baseUri1);

        Element parentElement = new Elements.Element("Parent", new Attributes());
        leafNode.parent(parentElement);
        String baseUri2 = leafNode.baseUri();
        assertEquals("Parent", baseUri2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildNodeSize() {
        LeafNode leafNode = new TextNode("Test");
        int childNodeSize = leafNode.childNodeSize();
        assertEquals(0, childNodeSize);

        leafNode.add(new TextNode("Child"));
        int childNodeSize1 = leafNode.childNodeSize();
        assertEquals(1, childNodeSize1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new TextNode("Test");
        List<Node> childNodes = leafNode.ensureChildNodes();
        assertNotNull(childNodes);
        assertTrue(childNodes.isEmpty());

        leafNode.add(new TextNode("Child1"));
        leafNode.add(new TextNode("Child2"));
        List<Node> childNodes1 = leafNode.ensureChildNodes();
        assertNotNull(childNodes1);
        assertEquals(2, childNodes1.size());
    }
}
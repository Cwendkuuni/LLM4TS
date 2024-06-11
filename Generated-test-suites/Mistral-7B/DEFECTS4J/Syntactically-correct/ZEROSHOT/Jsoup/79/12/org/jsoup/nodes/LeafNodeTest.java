import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class LeafNodeTest {

    private LeafNode leafNode;

    @Before
    public void setUp() {
        leafNode = new TextNode("Hello World");
    }

    @Test
    public void testHasAttributes() {
        assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testAttributes() {
        assertNull(leafNode.attributes());

        Attributes attributes = new Attributes();
        attributes.put("test", "value");
        leafNode.coreValue("test");

        assertEquals(attributes, leafNode.attributes());
        assertEquals("value", leafNode.attr("test"));
    }

    @Test
    public void testCoreValue() {
        assertEquals("Hello World", leafNode.coreValue());

        leafNode.coreValue("New Value");
        assertEquals("New Value", leafNode.coreValue());
    }

    @Test
    public void testAttr() {
        assertEquals("Hello World", leafNode.attr(Node.TAG_NAME));
        assertEquals("Hello World", leafNode.coreValue());

        leafNode.coreValue("New Value");
        assertEquals("New Value", leafNode.attr(Node.TAG_NAME));

        leafNode.attr(Node.TAG_NAME, "Test Value");
        assertEquals("Test Value", leafNode.attr(Node.TAG_NAME));
        assertEquals("New Value", leafNode.coreValue());
    }

    @Test
    public void testHasAttr() {
        assertFalse(leafNode.hasAttr(Node.TAG_NAME));

        leafNode.attr(Node.TAG_NAME, "Test Value");
        assertTrue(leafNode.hasAttr(Node.TAG_NAME));

        assertFalse(leafNode.hasAttr("non-existent-attribute"));
    }

    @Test
    public void testRemoveAttr() {
        leafNode.attr(Node.TAG_NAME, "Test Value");
        assertEquals("Test Value", leafNode.attr(Node.TAG_NAME));

        leafNode.removeAttr(Node.TAG_NAME);
        assertFalse(leafNode.hasAttr(Node.TAG_NAME));
        assertNull(leafNode.attr(Node.TAG_NAME));
        assertEquals("", leafNode.coreValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildNodeSize() {
        leafNode.childNodeSize();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        leafNode.ensureChildNodes();
    }
}